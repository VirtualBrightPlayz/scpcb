#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "../Utils/MathUtil.h"
#include "../Scripting/NativeDefinitions/ConsoleDefinition.h"

float Console::Message::lineHeight;
float Console::Message::bottomOfConsoleWindow;

Console::Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io) : Menu(wrld, "console") {
    //TODO: Figure out alignment issues and simplify this.
    float frameX = -GUIComponent::SCALE_MAGNITUDE * con->getAspectRatio();
    float frameY = -GUIComponent::SCALE_MAGNITUDE + 2.f;
    float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * con->getAspectRatio();
    float frameHeight = GUIComponent::SCALE_MAGNITUDE;
    frame = new GUIFrame(um, kb, con, frameX, frameY, frameWidth, frameHeight);

    float inputY = frameY + frameHeight;
    input = new GUITextInput(um, font, kb, con, io, frameX, inputY, frameWidth, 5.f, true);

    windowMaxLineCount = (int) ((frameHeight - 3.f) / (font->getHeight() / con->getHeight() * GUIComponent::SCALE_MAGNITUDE + 2.f));

    Message::lineHeight = (font->getHeight() / con->getHeight() * frameHeight + 2.f);
    Message::bottomOfConsoleWindow = windowMaxLineCount * Message::lineHeight + 1.f;
    commandHistoryIndex = -1;

    windowScrollOffset = 0.f;

    this->uiMesh = um;
    this->font = font;
    this->keyBinds = kb;
    this->config = con;

    registerInternalCommands();
}

Console::~Console() {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        delete interCommands[i];
    }
}

void Console::update(const PGE::Vector2f& mousePosition, const PGE::Vector2i& mouseWheelDelta) {
    input->update(mousePosition);

    bool windowChanged = false;
    if (keyBinds->enter->isHit() && !input->getText().equals("")) {
        executeCommand(input->getText());
        input->setText("");

        windowChanged = true;
    }

    if (keyBinds->upArrow->isHit() || keyBinds->downArrow->isHit()) {
        int newIndex = keyBinds->upArrow->isHit() ? commandHistoryIndex + 1 : commandHistoryIndex - 1;
        if (newIndex < -1) {
            commandHistoryIndex = (int)commandHistory.size() - 1;
        } else if (newIndex >= commandHistory.size()) {
            commandHistoryIndex = -1;
        } else {
            commandHistoryIndex = newIndex;
        }

        if (commandHistoryIndex > -1) {
            input->setText(commandHistory[commandHistoryIndex]);
        } else {
            input->setText("");
        }
        windowChanged = true;
    }

    if (mouseWheelDelta.y != 0) {
        //Scroll only in the area of existing messages available.
        windowScrollOffset = MathUtil::clampFloat(
                windowScrollOffset + (mouseWheelDelta.y * 0.3f),
                (float)-MathUtil::maxInt(0, (int)messageHistory.size() - windowMaxLineCount),
                0.f);
        windowChanged = true;
    }

    if (windowChanged) {
        updateMessageWindow();
    }
}

void Console::render() const {
    uiMesh->startRender();

    frame->render();
    input->render();

    int lowestDrawn = (int)messageHistory.size() - MathUtil::floor(MathUtil::absFloat(windowScrollOffset));
    for (int i = MathUtil::maxInt(0, lowestDrawn - windowMaxLineCount); i < lowestDrawn; i++) {
        messageHistory[i].text->render();
    }

    uiMesh->endRender();
}

void Console::executeCommand(const PGE::String& in) {
    commandHistoryIndex = -1;
    addConsoleMessage("> " + in, PGE::Color::Yellow);
    commandHistory.push_front(in);

    std::vector<PGE::String> vect = in.split(" ", true);
    PGE::String command = vect[0].toLower();
    vect.erase(vect.begin());
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (command.equals(interCommands[i]->getName())) {
            interCommands[i]->execute(this, vect);
            return;
        }
    }
    addConsoleMessage("No command found", PGE::Color::Red);
}

Console::Message::Message(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt, const PGE::String& resp, const PGE::Color& color) {
    text = new GUIText(um, kb, con, fnt, 3.f / con->getAspectRatio(), 0.f, Alignment::Left | Alignment::Top);
    text->rt = { resp, color };
}

void Console::Message::setLinePositionFromBottom(float line) {
    linePositionFromBottom = line;
    text->setY(bottomOfConsoleWindow - line * lineHeight);
}

void Console::addConsoleMessage(const PGE::String& resp, const PGE::Color& color) {
    messageHistory.push_back(Message(uiMesh, keyBinds, config, font, resp, color));
    updateMessageWindow();
    windowScrollOffset = 0;
}

void Console::logWarning(const PGE::String& resp) {
    addConsoleMessage(resp, PGE::Color::Yellow);
}

void Console::logError(const PGE::String& resp) {
    addConsoleMessage(resp, PGE::Color::Red);
}

void Console::updateMessageWindow() {
    for (int i = 0; i < messageHistory.size(); i++) {
        messageHistory[i].setLinePositionFromBottom(windowScrollOffset + messageHistory.size()-1-i);
    }
}

void Console::showHelp(const PGE::String& com) {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (com.equals(interCommands[i]->getName())) {
            addConsoleMessage(interCommands[i]->getHelpText());
        }
    }
}

void Console::listCommands() {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        addConsoleMessage(interCommands[i]->getName());
    }
}

void Console::clear() {
    messageHistory.clear();
    commandHistory.clear();
    commandHistoryIndex = 0;
    updateMessageWindow();
}

void Console::showHelp() {
    addConsoleMessage("Noone can help you now");
}

class HelpCommand : public Command {
public:
    PGE::String getName() const override {
        return "help";
    }

    PGE::String getHelpText() const override {
        return "Provides a description of a specified command. Use it with no parameters to list all commands.";
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        if (params.size() > 0) {
            console->showHelp(params.front());
        } else {
            console->showHelp();
        }
    }
};

class ListCommand : public Command {
public:
    PGE::String getName() const override {
        return "list";
    }

    PGE::String getHelpText() const override {
        return "Lists all commands";
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        console->listCommands();
    }
};

class ClearCommand : public Command {
public:
    PGE::String getName() const override {
        return "clear";
    }

    PGE::String getHelpText() const override {
        return "Clears the console";
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        console->clear();
    }
};

class ScriptCommand : public Command {
private:
    asIScriptFunction* func;
    asIScriptContext* scriptContext;

    const PGE::String name;
    const PGE::String helpText;
    const bool caseSensitive;

public:
    bool duplicateName = false;

    ScriptCommand(const PGE::String& nm, const PGE::String& ht, asIScriptFunction* f, asIScriptContext* context, bool cs) : name(nm), helpText(ht), caseSensitive(cs) {
        func = f;
        scriptContext = context;
    }

    PGE::String getName() const override {
        if (duplicateName) {
            return (PGE::String(func->GetModuleName()) + ":" + name).toLower();
        } else {
            return PGE::String(name).toLower();
        }
    }

    PGE::String getHelpText() const override {
        return helpText.isEmpty() ? "No help available for this command." : helpText;
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        if (func->GetParamCount() != params.size()) {
            console->addConsoleMessage("ARGUMENT SIZE MISMATCH", PGE::Color::Red);
            return;
        }
        if (scriptContext->Prepare(func) < 0) { throw std::runtime_error("ptooey! 2"); }
        PGE::String errMsg;
        int paramTypeId;
        for (unsigned int i = 0; i < func->GetParamCount(); i++) {
            if (func->GetParam(i, &paramTypeId) >= 0) {
                if (paramTypeId == asTYPEID_BOOL) {
                    if (params[i].toLower().equals("true") || params[i].equals("1")) {
                        scriptContext->SetArgByte(i, 1);
                    } else if (params[i].toLower().equals("false") || params[i].equals("0")) {
                        scriptContext->SetArgByte(i, 0);
                    } else {
                        errMsg = "NOT BOOL";
                        break;
                    }
                } else if (paramTypeId == asTYPEID_INT32) {
                    int arg = std::stoi(params[i].cstr());
                    scriptContext->SetArgDWord(i, arg);
                    if (!MathUtil::eqFloats((float)arg, params[i].toFloat())) { //If the end user enters a float.
                        console->addConsoleMessage("Loss of data!", PGE::Color::Yellow);
                    }
                } else if (paramTypeId == asTYPEID_FLOAT) {
                    scriptContext->SetArgFloat(i, std::stof(params[i].cstr()));
                } else if (paramTypeId == scriptContext->GetEngine()->GetStringFactoryReturnTypeId()) {
                    scriptContext->SetArgObject(i, (void*) &(caseSensitive ? params[i] : params[i].toLower()));
                }
            } else {
                throw std::runtime_error("ptooey! 3");
            }
        }
        if (errMsg.isEmpty()) {
            scriptContext->Execute();
        } else {
            console->addConsoleMessage(errMsg, PGE::Color::Red);
        }
        scriptContext->Unprepare();
    }
};

void Console::registerInternalCommands() {
    interCommands.push_back(new HelpCommand());
    interCommands.push_back(new ListCommand());
    interCommands.push_back(new ClearCommand());
}

void Console::registerExternalCommand(const PGE::String& name, const PGE::String& helpText, asIScriptFunction* f, asIScriptContext* context, bool caseSensitive) {
    for (unsigned int i = 0; i < f->GetParamCount(); i++) {
        int paramTypeId;
        if (f->GetParam(i, &paramTypeId) < 0) {
            throw std::runtime_error("ptooey?");
        } else {
            if (paramTypeId != asTYPEID_BOOL && paramTypeId != asTYPEID_INT32 && paramTypeId != asTYPEID_FLOAT && paramTypeId != context->GetEngine()->GetStringFactoryReturnTypeId()) {
                throw std::runtime_error("STINKY INVALID TYPE");
            }
        }
    }
    ScriptCommand* newCommand = new ScriptCommand(name, helpText, f, context, caseSensitive);
    ScriptCommand* otherCommand;
    for (int i = 0; i < interCommands.size(); i++) {
        otherCommand = dynamic_cast<ScriptCommand*>(interCommands[i]);
        if (otherCommand != nullptr) {
            if (otherCommand->getName().equals(otherCommand->duplicateName ? (PGE::String(f->GetModuleName()) + ":" + name).toLower() : name.toLower())) { //This will also catch a command that already has had its name changed.
                otherCommand->duplicateName = true;
                newCommand->duplicateName = true;
                if (otherCommand->getName().equals(newCommand->getName())) { throw std::runtime_error("lol double command"); }
                break;
            }
        } else if (interCommands[i]->getName().equals(PGE::String(f->GetName()).toLower())) { //Check if name is occupied by a vanilla command.
            newCommand->duplicateName = true;
        }
    }
    interCommands.push_back(newCommand);
}

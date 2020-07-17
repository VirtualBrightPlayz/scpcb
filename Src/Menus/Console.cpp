#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "../Utils/MathUtil.h"
#include "../Scripting/NativeDefinitions/ConsoleDefinition.h"
#include "../Scripting/ScriptObject.h"
#include "../Scripting/ScriptFunction.h"
#include "../Scripting/ScriptClass.h"
#include <angelscript.h>
#include "../Scripting/AngelScriptAddons/scriptarray/scriptarray.h"

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

    windowMaxLineCount = (frameHeight - 3.f) / (font->getHeight() / con->getHeight() * GUIComponent::SCALE_MAGNITUDE + 2.f);

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
    for (int i = 0; i < (int)commands.size(); i++) {
        delete commands[i];
    }
}

void Console::onOpen() {
    input->select();
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
            commandHistoryIndex = commandHistory.size() - 1;
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
                -MathUtil::maxInt(0, messageHistory.size() - windowMaxLineCount),
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

    int lowestDrawn = messageHistory.size() - MathUtil::floor(MathUtil::absFloat(windowScrollOffset));
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
    for (int i = 0; i < (int)commands.size(); i++) {
        if (command.equals(commands[i]->getName().toLower())) {
            commands[i]->execute(this, vect);
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

void Console::todo_test() {
    addConsoleMessage("hey gotem", PGE::Color::Green);
}

void Console::showHelp(const PGE::String& com) {
    for (int i = 0; i < (int)commands.size(); i++) {
        if (com.equals(commands[i]->getName())) {
            addConsoleMessage(commands[i]->getHelpText());
        }
    }
}

void Console::showHelp() {
    addConsoleMessage("Noone can help you now");
}

class TestCommand : public Command {
public:
    PGE::String getName() const override {
        return "test";
    }

    PGE::String getHelpText() const override {
        return "AAAAAAAAAAAAA";
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        console->todo_test();
    }
};

class HelpCommand : public Command {
public:
    PGE::String getName() const override {
        return "help";
    }

    PGE::String getHelpText() const override {
        return "You really need help";
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        if (params.size() > 0) {
            console->showHelp(params.front());
        } else {
            console->showHelp();
        }
    }
};

class ScriptCommand : public Command {
private:
    ScriptObject* scriptObject;
    ScriptFunction* nameFunc;
    ScriptFunction* helpFunc;
    ScriptFunction* executeFunc;

    int stringTypeID;

public:
    ScriptCommand(ScriptClass* clss) {
        scriptObject = clss->createNewObject();

        nameFunc = scriptObject->getFunction("getName");
        helpFunc = scriptObject->getFunction("getHelp");
        executeFunc = scriptObject->getFunction("execute");

        asIScriptEngine* engine = scriptObject->getAngelScriptObject()->GetEngine();
        stringTypeID = engine->GetStringFactoryReturnTypeId();
    }

    virtual ~ScriptCommand() {
        delete scriptObject;
    }

    PGE::String getName() const override {
        nameFunc->execute(scriptObject);
        return nameFunc->getReturnString();
    }

    PGE::String getHelpText() const override {
        helpFunc->execute(scriptObject);
        return helpFunc->getReturnString();
    }

    void execute(Console* console, const std::vector<PGE::String>& params) const override {
        asIScriptFunction* func = executeFunc->getAngelScriptFunction();
        asIScriptContext* scriptContext = executeFunc->getFunctionContext();

        scriptContext->Prepare(func);
        scriptContext->SetObject(scriptObject->getAngelScriptObject());

        if (func->GetParamCount() != params.size()) {
            console->addConsoleMessage("ARGUMENT SIZE MISMATCH", PGE::Color::Red);
            return;
        }

        PGE::String errMsg;
        for (int i = 0; i < func->GetParamCount(); i++) {
            int paramTypeId;
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
                }
                else if (paramTypeId == asTYPEID_INT32) {
                    try {
                        int arg = std::stoi(params[i].cstr());
                        // If the end user enters a float
                        if (!MathUtil::eqFloats(arg, std::stof(params[i].cstr()))) {
                            errMsg = "NOT INT";
                            break;
                        }

                        scriptContext->SetArgDWord(i, arg);
                    } catch (std::invalid_argument) {
                        errMsg = "NOT INT";
                        break;
                    }
                } else if (paramTypeId == asTYPEID_FLOAT) {
                    try {
                        scriptContext->SetArgFloat(i, std::stof(params[i].cstr()));
                    } catch (std::invalid_argument) {
                        errMsg = "NOT FLOAT";
                        break;
                    }
                } else if (paramTypeId == stringTypeID) {
                    PGE::String huh = params[i];
                    scriptContext->SetArgObject(i, (void*)&huh);
                }
            } else {
                throw std::runtime_error("ptooey! 3");
            }
        }

        if (errMsg.isEmpty()) {
            scriptContext->Execute();
            scriptContext->Unprepare();
        } else {
            console->addConsoleMessage(errMsg, PGE::Color::Red);
        }
    }
};

void Console::registerInternalCommands() {
    commands.push_back(new TestCommand());
    commands.push_back(new HelpCommand());
}

void Console::registerExternalCommands(const std::vector<ScriptClass*>& commandClasses) {
    for (int i = 0; i < (int)commandClasses.size(); i++) {
        commands.push_back(new ScriptCommand(commandClasses[i]));
    }
}

#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "../Utils/MathUtil.h"

float Console::Message::lineHeight;
float Console::Message::bottomOfConsoleWindow;

Console* Console::_console;

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

    _console = this;

    registerInternalCommands();
}

Console::~Console() {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        delete interCommands[i];
    }
}

void Console::update(const PGE::Vector2f& mousePosition) {
    input->update(mousePosition);

    bool windowChanged = false;
    if (keyBinds->enter->isHit() && !input->getText().equals("")) {
        executeCommand(input->getText());
        input->setText("");

        windowChanged = true;
    }

    /*if (keyBinds->upArrow->isHit() || keyBinds->downArrow->isHit()) {
        int newIndex = keyBinds->upArrow->isHit() ? commandHistoryIndex + 1 : commandHistoryIndex - 1;
        if (newIndex < -1) {
            commandHistoryIndex = commandHistory.size() - 1;
        } else if (newIndex >= commandHistory.size()) {
            commandHistoryIndex = -1;
        }
        else {
            commandHistoryIndex = newIndex;
        }

        if (commandHistoryIndex > -1) {
            input->setText(commandHistory[commandHistoryIndex]);
        } else {
            input->setText("");
        }
        windowChanged = true;
    }*/

    if ((keyBinds->upArrow->isHit() || keyBinds->downArrow->isHit())) {
        //Scroll only in the area of existing messages available.
        windowScrollOffset = MathUtil::clampFloat(
                windowScrollOffset + (keyBinds->upArrow->isHit() ? 0.5f : -0.5f),
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
    PGE::String command = vect[0];
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

void Console::updateMessageWindow() {
    for (int i = 0; i < messageHistory.size(); i++) {
        messageHistory[i].setLinePositionFromBottom(windowScrollOffset + messageHistory.size()-1-i);
    }
}

void Console::todo_test() {
    addConsoleMessage("hey gotem", PGE::Color::Green);
}

void Console::showHelp(const PGE::String& com) {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (com.equals(interCommands[i]->getName())) {
            addConsoleMessage(interCommands[i]->getHelpText());
        }
    }
}

void Console::showHelp() {
    addConsoleMessage("Noone can help you now");
}

class TestCommand : public InternalCommand {
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

class HelpCommand : public InternalCommand {
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

void Console::registerInternalCommands() {
    interCommands.push_back(new TestCommand());
    interCommands.push_back(new HelpCommand());
}

Console* Console::getConsole() {
    return _console;
}

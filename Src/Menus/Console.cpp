#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "../Utils/MathUtil.h"

float Console::Message::lineHeight;
float Console::Message::bottomOfConsoleWindow;

Console::Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io) : Menu(wrld, "console") {
    //TODO: Figure out alignment issues and simplify this.
    float frameX = -GUIComponent::SCALE_MAGNITUDE * con->getAspectRatio();
    float frameY = -GUIComponent::SCALE_MAGNITUDE + 2.f;
    float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * con->getAspectRatio();
    float frameHeight = GUIComponent::SCALE_MAGNITUDE;
    frame = new GUIFrame(um, kb, con, frameX, frameY, frameWidth, frameHeight);
    Message::bottomOfConsoleWindow = frameY + frameHeight;

    float inputY = frameY + frameHeight;
    input = new GUITextInput(um, font, kb, con, io, frameX, inputY, frameWidth, 5.f, true);

    Message::lineHeight = con->getHeight();
    consoleWindowLineCount = (frameHeight - 3.f) / (font->getHeight() / con->getHeight() * GUIComponent::SCALE_MAGNITUDE + 2.f);
    lowestLine = 0;

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

void Console::update(const PGE::Vector2f& mousePosition) {
    input->update(mousePosition);

    bool windowChanged = false;
    if (keyBinds->enter->isHit() && !input->getText().equals("")) {
        executeCommand(input->getText());
        input->setText("");

        windowChanged = true;
    }

    if (keyBinds->upArrow->isHit() || keyBinds->downArrow->isHit()) {
        lowestLine = MathUtil::clampInt(lowestLine + (keyBinds->upArrow->isHit() ? 2 : -2), 0, MathUtil::maxInt((int)commandHistory.size(), 0));
        /*if (lowestLine > 0) {
            input->setText(outputLines[outputLines.size() - lowestLine].text.substr(2));
        } else {
            input->setText("");
        }*/
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

    /*for (int i = 0; i < outputCompsSize; i++) {
        outputComps[i]->render();
    }*/

    uiMesh->endRender();
}

void Console::executeCommand(const PGE::String& in) {
    lowestLine = 0;
    addConsoleMessage("> " + in, PGE::Color::Yellow);
    std::vector<PGE::String> vect = in.split(" ", true);
    PGE::String command = vect[0];
    vect.erase(vect.begin());
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (command.equals(interCommands[i]->getName())) {
            interCommands[i]->execute(this, vect);
            return;
        }
    }
    addConsoleMessage("No command found :(((", PGE::Color::Red);
}

Console::Message::Message(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt) {
    text = new GUIText(um, kb, con, fnt, 0.f, 0.f, Alignment::Left);
    setLinePositionFromBottom(0.f);
}

void Console::Message::setLinePositionFromBottom(float line) {
    linePositionFromBottom = line;
    text->setY(bottomOfConsoleWindow + (lineHeight * line));
}

void Console::addConsoleMessage(const PGE::String& resp, const PGE::Color& color) {
    messageHistory.push_back(Message(uiMesh, keyBinds, config, font));
    updateMessageWindow();
}

void Console::updateMessageWindow() {
    /*for (int i = outputCompsSize - 1; i >= 0; i--) {
        if (i < outputLines.size() - lowestLine) {
            outputComps[i]->rt = outputLines[outputLines.size() - i - 1 - lowestLine];
        } else {
            outputComps[i]->rt = {};
        }
    }*/
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
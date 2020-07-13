#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "GUI/GUIText.h"

Console::Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io) : Menu(wrld, "console") {
    //TODO: Figure out alignment issues and simplify this.
    float frameX = -GUIComponent::SCALE_MAGNITUDE * con->getAspectRatio();
    float frameY = -GUIComponent::SCALE_MAGNITUDE + 2.f;
    float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * con->getAspectRatio();
    float frameHeight = GUIComponent::SCALE_MAGNITUDE;
    frame = new GUIFrame(um, kb, con, frameX, frameY, frameWidth, frameHeight);

    float inputY = frameY + frameHeight;
    input = new GUITextInput(um, font, kb, con, io, frameX, inputY, frameWidth, 5.f, true);

    outputCompsSize = (frameHeight - 3.f) / (font->getHeight() / con->getHeight() * GUIComponent::SCALE_MAGNITUDE + 2.f);
    outputComps = new GUIText*[outputCompsSize];
    lowestLine = 0;

    for (int i = 0; i < outputCompsSize; i++) {
        outputComps[i] = new GUIText(um, kb, con, font, 3.f / con->getAspectRatio(), (outputCompsSize - i) * (font->getHeight() / con->getHeight() * frameHeight + 2.f) + 1.f, Alignment::Left | Alignment::Top);
    }

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

    if (keyBinds->enter->isHit() && !input->getText().equals("")) {
        executeCommand(input->getText());
    }

    if (keyBinds->upArrow->isHit() || keyBinds->downArrow->isHit()) {
        lowestLine = std::clamp(lowestLine + (keyBinds->upArrow->isHit() ? 2 : -2), 0, std::max((int)outputLines.size(), 0));
        if (lowestLine > 0) {
            input->setText(outputLines[outputLines.size() - lowestLine].text.substr(2));
        } else {
            input->setText("");
        }
        updateOutput();
    }
}

void Console::render() const {
    uiMesh->startRender();

    frame->render();
    input->render();

    for (int i = 0; i < outputCompsSize; i++) {
        outputComps[i]->render();
    }

    uiMesh->endRender();
}

void Console::executeCommand(const PGE::String& in) {
    lowestLine = 0;
    addResponse("> " + in, PGE::Color::Yellow);
    std::vector<PGE::String> vect = in.split(" ", true);
    PGE::String command = vect[0];
    vect.erase(vect.begin());
    input->setText("");
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (command.equals(interCommands[i]->getName())) {
            interCommands[i]->execute(this, vect);
            return;
        }
    }
    addResponse("No command found :(((", PGE::Color::Red);
}

void Console::addResponse(const PGE::String& resp, const PGE::Color& color) {
    outputLines.push_back({ resp, color });
    updateOutput();
}

void Console::updateOutput() {
    for (int i = outputCompsSize - 1; i >= 0; i--) {
        if (i < outputLines.size() - lowestLine) {
            outputComps[i]->rt = outputLines[outputLines.size() - i - 1 - lowestLine];
        } else {
            outputComps[i]->rt = {};
        }
    }
}

void Console::todo_test() {
    addResponse("hey gotem", PGE::Color::Green);
}

void Console::showHelp(const PGE::String& com) {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (com.equals(interCommands[i]->getName())) {
            addResponse(interCommands[i]->getHelpText());
        }
    }
}

void Console::showHelp() {
    addResponse("Noone can help you now");
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
#include "Console.h"
#include "../Save/Config.h"
#include "GUI/GUIComponent.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"

Console::Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io) : Menu(wrld, "console") {
    float frameX = -GUIComponent::SCALE_MAGNITUDE * con->getAspectRatio();
    float frameY = -GUIComponent::SCALE_MAGNITUDE + 2.f;
    float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * con->getAspectRatio();
    float frameHeight = GUIComponent::SCALE_MAGNITUDE;
    frame = new GUIFrame(um, kb, con, frameX, frameY, frameWidth, frameHeight);

    float inputY = frameY + frameHeight;
    input = new GUITextInput(um, font, kb, con, io, frameX, inputY, frameWidth, 5.f, true);

    uiMesh = um;
    this->font = font;
    this->keyBinds = kb;

    registerInternalCommands();
}

Console::~Console() {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        delete interCommands[i];
    }
}

void Console::update(const PGE::Vector2f& mousePosition) {
    input->update(mousePosition);

    if (keyBinds->enter->isHit()) {
        std::vector<PGE::String> vect = input->getText().split(" ", true);
        if (vect.size() > 0) {
            PGE::String command = vect[0];
            vect.erase(vect.begin());

            executeCommand(command, vect);
            //input->setText("");
        }
    }
}

void Console::render() const {
    uiMesh->startRender();

    frame->render();
    input->render();

    uiMesh->endRender();
}

void Console::executeCommand(const PGE::String& com, const std::vector<PGE::String>& params) {
    for (int i = 0; i < (int)interCommands.size(); i++) {
        if (com.equals(interCommands[i]->getName())) {
            interCommands[i]->execute(this, params);
        }
    }
}

void Console::todo_test() {
    input->setText("hey gotem");
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

void Console::registerInternalCommands() {
    interCommands.push_back(new TestCommand());
}
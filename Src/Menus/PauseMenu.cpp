#include "PauseMenu.h"

#include <iostream>

#include "../Utils/LocalizationManager.h"
#include "../World/World.h"

PauseMenu::PauseMenu(World* wrld, UIMesh* um, Font* largeFnt, KeyBinds* kb, Config* con, LocalizationManager* lm, PGE::IO* io) : Menu(wrld, "pausemenu") {
    float btnWidth = 40.f;
    float btnHeight = 7.f;

    float btnSpacing = 10.f;
    float btnX = -btnWidth / 2.f;
    float btnY = -38.f;

    newgame = new GUIButton(um, largeFnt, kb, con, lm, btnX, btnY, btnWidth, btnHeight, "MainMenu.NewGame");
    btnY += btnSpacing;
    loadgame = new GUIButton(um, largeFnt, kb, con, lm, btnX, btnY, btnWidth, btnHeight, "MainMenu.LoadGame");
    btnY += btnSpacing;
    options = new GUIButton(um, largeFnt, kb, con, lm, btnX, btnY, btnWidth, btnHeight, "MainMenu.Options");
    btnY += btnSpacing;
    quit = new GUIButton(um, largeFnt, kb, con, lm, btnX, btnY, btnWidth, btnHeight, "MainMenu.Quit");
    btnY += btnSpacing;
    inputTest = new GUITextInput(um, largeFnt, kb, con, io, btnX, btnY, btnWidth, btnHeight, false, 1000, "banansunday.yogurt", 15);

    float quitFrameWidth = 60.f;
    float quitFrameHeight = 30.f;
    quitFrame = new GUIFrame(um, kb, con, -quitFrameWidth / 2.f, -quitFrameHeight / 2.f, quitFrameWidth, quitFrameHeight);
    quitText = new GUIText(um, kb, con, largeFnt, lm, 0.f, -5.f, true, true, Alignment::CenterXY);
    quitText->setText("MainMenu.Quit.Prompt");
    quitYes = new GUIButton(um, largeFnt, kb, con, lm, -25.f, 5.f, 12.f, 4.f, "MainMenu.Quit.Yes");
    quitNo = new GUIButton(um, largeFnt, kb, con, lm, 13.f, 5.f, 12.f, 4.f, "MainMenu.Quit.No");

    uiMesh = um;

    setState(SubState::Main);
    mainMenu = false;
}

PauseMenu::~PauseMenu() {
    delete newgame;
    delete loadgame;
    delete options;
    delete quit;
    delete inputTest;

    delete quitFrame;
    delete quitText;
    delete quitYes;
    delete quitNo;
}

void PauseMenu::setState(SubState state) {
    currState = state;

    if (state == SubState::Options) { setOptionsTab(OptionsTab::Graphics); }

    // The menu's still visible when the quit prompt's on screen but it's disabled.
    newgame->locked = currState == SubState::Quitting;
    loadgame->locked = currState == SubState::Quitting;
    options->locked = currState == SubState::Quitting;
    quit->locked = currState == SubState::Quitting;
}

void PauseMenu::setOptionsTab(OptionsTab tab) {
    currOptionsTab = tab;
}

void PauseMenu::onEscapeHit() {
    if (mainMenu) { return; }
    switch (currState) {
        case SubState::Main: {
            detach();
        } break;
        case SubState::Quitting: {
            setState(SubState::Main);
        } break;
    }
}

void PauseMenu::update(const PGE::Vector2f& mousePosition, const PGE::Vector2i& mouseWheelDelta) {
    switch (currState) {
        case SubState::Main: {
            newgame->update(mousePosition);
            loadgame->update(mousePosition);
            options->update(mousePosition);
            quit->update(mousePosition);
            inputTest->update(mousePosition);

            if (newgame->isClicked()) {
                std::cout << "New Game" << std::endl;
            } else if (loadgame->isClicked()) {
                std::cout << "Load Game" << std::endl;
            } else if (options->isClicked()) {
                std::cout << "Options Game" << std::endl;
            } else if (quit->isClicked()) {
                setState(SubState::Quitting);
            }
        } break;
        case SubState::Quitting: {
            quitFrame->update(mousePosition);
            quitYes->update(mousePosition);
            quitNo->update(mousePosition);

            if (quitNo->isClicked()) {
                setState(SubState::Main);
            } else if (quitYes->isClicked()) {
                world->quit();
            }
        } break;
    }
}

void PauseMenu::render() const {
    uiMesh->startRender();

    if (currState == SubState::Main || currState == SubState::Quitting) {
        newgame->render();
        loadgame->render();
        options->render();
        quit->render();
        inputTest->render();
    }

    switch (currState) {
        case SubState::Quitting: {
            quitFrame->render();
            quitText->render();
            quitYes->render();
            quitNo->render();
        } break;
    }

    uiMesh->endRender();
}

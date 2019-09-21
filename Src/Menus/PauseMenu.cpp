#include <iostream>

#include "PauseMenu.h"
#include "../Utils/TextMgmt.h"
#include "../World/World.h"

PauseMenu::PauseMenu(World* wrld, UIMesh* um, Font* largeFnt, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io) : Menu(wrld, "pausemenu") {
    float btnWidth = 40.f;
    float btnHeight = 7.f;

    float btnSpacing = 10.f;
    float btnX = -btnWidth / 2.f;
    float btnY = -38.f;

    newgame = new GUIButton(um, largeFnt, kb, con, tm, btnX, btnY, btnWidth, btnHeight, "mnu_new");
    btnY += btnSpacing;
    loadgame = new GUIButton(um, largeFnt, kb, con, tm, btnX, btnY, btnWidth, btnHeight, "mnu_load");
    btnY += btnSpacing;
    options = new GUIButton(um, largeFnt, kb, con, tm, btnX, btnY, btnWidth, btnHeight, "mnu_opt");
    btnY += btnSpacing;
    quit = new GUIButton(um, largeFnt, kb, con, tm, btnX, btnY, btnWidth, btnHeight, "mnu_quit");
    btnY += btnSpacing;
    inputTest = new GUITextInput(um, largeFnt, kb, con, io, btnX, btnY, btnWidth, btnHeight, "banansunday.yogurt", 15);

    float quitFrameWidth = 60.f;
    float quitFrameHeight = 30.f;
    quitFrame = new GUIFrame(um, kb, con, -quitFrameWidth / 2.f, -quitFrameHeight / 2.f, quitFrameWidth, quitFrameHeight);
    quitYes = new GUIButton(um, largeFnt, kb, con, tm, -31.f, 10.f, 3.f, 1.f, "mnu_quit_yes");
    quitNo = new GUIButton(um, largeFnt, kb, con, tm, 29.f, 10.f, 3.f, 1.f, "mnu_quit_no");

    uiMesh = um;
    largeFont = largeFnt;
    keyBinds = kb;
    
    setState(SubState::Main);
    mainMenu = false;
}

void PauseMenu::setState(SubState state) {
    currState = state;

    if (currState == SubState::Options) { setOptionsTab(OptionsTab::Graphics); }

    // The menu's still visible when the quit prompt's on screen but it's disabled.
    newgame->locked = currState == SubState::Quitting;
    loadgame->locked = currState == SubState::Quitting;
    options->locked = currState == SubState::Quitting;
    quit->locked = currState == SubState::Quitting;
}

void PauseMenu::setOptionsTab(OptionsTab tab) {

}

void PauseMenu::onEscapeHit() {
    if (mainMenu) { return; }
    if (currState == SubState::Main) {
        detach();
    }
}

void PauseMenu::update(const PGE::Vector2f& mousePosition) {
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
            quitYes->render();
            quitNo->render();
        } break;
    }

    uiMesh->endRender();
}

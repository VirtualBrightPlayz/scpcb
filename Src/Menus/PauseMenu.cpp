#include <iostream>

#include "PauseMenu.h"

PauseMenu::PauseMenu(UIMesh* um, Font* largeFnt, KeyBinds* kb, Config* con) {
    float btnWidth = 40.f;
    float btnHeight = 7.f;

    float btnSpacing = 7.3f;
    float btnX = -btnWidth / 2.f;
    float btnY = -35.f;

    newgame = new GUIButton(um, largeFnt, kb, con, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    loadgame = new GUIButton(um, largeFnt, kb, con, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    options = new GUIButton(um, largeFnt, kb, con, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    quit = new GUIButton(um, largeFnt, kb, con, btnX, btnY, btnWidth, btnHeight);

    float quitFrameWidth = 60.f;
    float quitFrameHeight = 30.f;
    quitFrame = new GUIFrame(um, kb, con, -quitFrameWidth / 2.f, -quitFrameHeight / 2.f, quitFrameWidth, quitFrameHeight);
    quitYes = new GUIButton(um, largeFnt, kb, con, -31.f, 10.f, 3.f, 1.f);
    quitNo = new GUIButton(um, largeFnt, kb, con, 29.f, 10.f, 3.f, 1.f);

    uiMesh = um;
    largeFont = largeFnt;
    keyBinds = kb;
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

void PauseMenu::show() {
    setState(PauseMenu::SubState::Main);
}

void PauseMenu::hide() {
    setState(PauseMenu::SubState::Hidden);
}

void PauseMenu::update(World* world, PGE::Vector2f mousePosition) {
    switch (currState) {
        case SubState::Main: {
            newgame->update(mousePosition);
            loadgame->update(mousePosition);
            options->update(mousePosition);
            quit->update(mousePosition);

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

    if (keyBinds->escape->isHit() && !isMainMenu) {
        world->setGameState(GameState::Playing);
    }
}

void PauseMenu::render(const World* world) {
    if (world->getGameState() != GameState::PauseMenu) { return; }

    uiMesh->startRender();

    if (currState == SubState::Main || currState == SubState::Quitting) {
        newgame->render();
        loadgame->render();
        options->render();
        quit->render();
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

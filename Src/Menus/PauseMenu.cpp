#include "PauseMenu.h"

PauseMenu::PauseMenu(UIMesh* um, KeyBinds* kb) {
    float btnWidth = 40;
    float btnHeight = 7;

    float btnSpacing = 7.3f;
    float btnX = -btnWidth / 2.f;
    float btnY = -40.f;

    newgame = new GUIButton(um, kb, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    loadgame = new GUIButton(um, kb, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    options = new GUIButton(um, kb, btnX, btnY, btnWidth, btnHeight);
    btnY += btnSpacing;
    quit = new GUIButton(um, kb, btnX, btnY, btnWidth, btnHeight);

    float quitFrameWidth = 60.f;
    float quitFrameHeight = 30.f;
    quitFrame = new GUIFrame(um, kb, -quitFrameWidth / 2.f, -quitFrameHeight / 2.f, quitFrameWidth, quitFrameHeight);
    quitYes = new GUIButton(um, kb, -31.f, 10.f, 3.f, 1.f);
    quitNo = new GUIButton(um, kb, 29.f, 10.f, 3.f, 1.f);

    setState(SubState::Main);

    uiMesh = um;
}

void PauseMenu::setState(SubState state) {
    currState = state;

    newgame->setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    loadgame->setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    options->setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    quit->setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    // The menu's still visible when the quit prompt's on screen but it's disabled.
    newgame->locked = currState == SubState::Quitting;
    loadgame->locked = currState == SubState::Quitting;
    options->locked = currState == SubState::Quitting;
    quit->locked = currState == SubState::Quitting;

    quitFrame->setVisibility(currState == SubState::Quitting);
    quitYes->setVisibility(currState == SubState::Quitting);
    quitNo->setVisibility(currState == SubState::Quitting);
}

void PauseMenu::setOptionsTab(OptionsTab tab) {

}

void PauseMenu::update(World* world, PGE::Vector2f mousePosition) {
    switch (currState) {
        case SubState::Main: {
            newgame->update(mousePosition);
            loadgame->update(mousePosition);
            newgame->update(mousePosition);
            newgame->update(mousePosition);
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

void PauseMenu::render() {
    uiMesh->startRender();

    switch (currState) {
        case SubState::Main: {
            newgame->render();
            loadgame->render();
            newgame->render();
            newgame->render();
        } break;

        case SubState::Quitting: {
            quitFrame->render();
            quitYes->render();
            quitNo->render();
        } break;
    }

    uiMesh->endRender();
}

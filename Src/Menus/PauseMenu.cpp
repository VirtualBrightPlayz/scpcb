#include "PauseMenu.h"

PauseMenu::PauseMenu(const UIAssets* assets) {
    float btnWidth = 40;
    float btnHeight = 7;

    float btnSpacing = 7.3f;
    float btnX = -btnWidth / 2.f;
    float btnY = -40.f;

    newgame = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    loadgame = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    options = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    quit = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);

    float quitFrameWidth = 60.f;
    float quitFrameHeight = 30.f;
    quitFrame = GUIFrame(-quitFrameWidth / 2.f, -quitFrameHeight / 2.f, quitFrameWidth, quitFrameHeight, assets);
    quitYes = GUIButton(-31.f, 10.f, 3.f, 1.f, assets);
    quitNo = GUIButton(29.f, 10.f, 3.f, 1.f, assets);

    setState(SubState::Main);
}

void PauseMenu::setState(SubState state) {
    currState = state;

    newgame.setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    loadgame.setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    options.setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    quit.setVisibility(currState == SubState::Main || currState == SubState::Quitting);
    // The menu's still visible when the quit prompt's on screen but it's disabled.
    newgame.locked = currState == SubState::Quitting;
    loadgame.locked = currState == SubState::Quitting;
    options.locked = currState == SubState::Quitting;
    quit.locked = currState == SubState::Quitting;

    quitFrame.setVisibility(currState == SubState::Quitting);
    quitYes.setVisibility(currState == SubState::Quitting);
    quitNo.setVisibility(currState == SubState::Quitting);
}

void PauseMenu::setOptionsTab(OptionsTab tab) {

}

void PauseMenu::update(World* world, PGE::Vector2f mousePosition) {
    switch (currState) {
        case SubState::Main: {
            newgame.update(mousePosition);
            loadgame.update(mousePosition);
            newgame.update(mousePosition);
            newgame.update(mousePosition);
        } break;

        case SubState::Quitting: {
            quitFrame.update(mousePosition);
            quitYes.update(mousePosition);
            quitNo.update(mousePosition);

            if (quitNo.isClicked()) {
                setState(SubState::Main);
            } else if (quitYes.isClicked()) {
                world->quit();
            }
        } break;
    }
}

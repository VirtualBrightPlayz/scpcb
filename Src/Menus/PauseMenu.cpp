#include "PauseMenu.h"

PauseMenu::PauseMenu(const UIAssets* assets) {
    float btnWidth = 40;
    float btnHeight = 7;

    float btnSpacing = 7.3f;
    float btnX = -btnWidth / 2.f;
    float btnY = 0.28f;

    newgame = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    loadgame = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    options = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);
    btnY += btnSpacing;
    quit = GUIButton(btnX, btnY, btnWidth, btnHeight, assets);

    setState(SubState::Main);
}

void PauseMenu::setState(SubState state) {
    currState = state;

    newgame.setVisibility(currState == SubState::Main);
    loadgame.setVisibility(currState == SubState::Main);
    options.setVisibility(currState == SubState::Main);
    quit.setVisibility(currState == SubState::Main);

    quitPrompt.setVisibility(currState == SubState::Quitting);
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

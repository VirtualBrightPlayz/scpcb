#include <bbgraphics.h>
#include <bbinput.h>

#include "PauseMenu.h"
#include "../Player/Player.h"
#include "../AssetMgmt/Assets.h"
#include "../AssetMgmt/Audio.h"
#include "../Config/Options.h"
#include "../GameMain.h"
#include "Menu.h"
#include "MainMenu.h"

namespace CBN {

PauseMenu* pauseMenu = nullptr;

PauseMenu::PauseMenu() {
    currState = PauseMenuState::Main;

    int width = (int)(390 * MenuScale);
    int height = (int)(60 * MenuScale);
    int x = userOptions->screenWidth / 2 - bbImageWidth(uiAssets->pauseMenuBG) / 2;
    int y = userOptions->screenHeight / 2 - bbImageHeight(uiAssets->pauseMenuBG) / 2;
    x += (int)(132 * MenuScale);
    y += (int)(132 * MenuScale);

    y += (int)(72 * MenuScale);
    btnResume = GUIButton(x, y, width, height, "Resume", true, false);
    btnDeadLoadGame = GUIButton(x, y, width, height, "Load Game", true, false);
    y += (int)(75 * MenuScale);
    btnLoadGame = GUIButton(x, y, width, height, "Load Game", true, false);
    btnDeadQuit = GUIButton(x, y, width, height, "Quit", true, false);
    y += (int)(75 * MenuScale);
    btnOptions = GUIButton(x, y, width, height, "Options", true, false);
    y += (int)(75 * MenuScale);
    btnQuit = GUIButton(x, y, width, height, "Quit", true, false);
}

void PauseMenu::update() {
    if (CurrGameState == GAMESTATE_PAUSED) {
        switch (currState) {
            case PauseMenuState::Main: {
                btnLoadGame.locked = !GameSaved;

                // TODO: Options/Load Game.
                btnResume.update();
                btnLoadGame.update();
                btnOptions.update();
                btnQuit.update();

                if (btnResume.isMouseUp()) {
                    CurrGameState = GAMESTATE_PLAYING;
                    ResumeSounds();
                    bbMouseXSpeed();
                    bbMouseYSpeed();
                    bbMouseZSpeed();
                    mouse_x_speed_1 = 0.f;
                    mouse_y_speed_1 = 0.f;
                } else if (btnQuit.isMouseUp()) {
                    //TODO: ask for saving
                    NullGame();
                    CurrGameState = GAMESTATE_MAINMENU;
                    CurrSave = "";
                    bbFlushKeys();
                }
            } break;
            case PauseMenuState::Dead: {
                btnDeadLoadGame.locked = !GameSaved;

                btnDeadLoadGame.update();
                btnDeadQuit.update();

                if (btnDeadQuit.isMouseUp()) {
                    NullGame();
                    CurrGameState = GAMESTATE_MAINMENU;
                    CurrSave = "";
                    bbFlushKeys();
                }
            } break;
        }
    }
}

void PauseMenu::draw() {
    if (CurrGameState == GAMESTATE_PAUSED) {
        String titleText = "";

        int x = userOptions->screenWidth / 2 - bbImageWidth(uiAssets->pauseMenuBG) / 2;
        int y = userOptions->screenHeight / 2 - bbImageHeight(uiAssets->pauseMenuBG) / 2;
        bbDrawImage(uiAssets->pauseMenuBG, x, y);

        x = (int)(x + 132 * MenuScale);
        y = (int)(y + 122 * MenuScale);

        switch (currState) {
            case PauseMenuState::Main: {
                titleText = "PAUSED";
                btnResume.draw();
                btnLoadGame.draw();
                btnOptions.draw();
                btnQuit.draw();
            } break;
            case PauseMenuState::Dead: {
                titleText = "YOU DIED";
                btnDeadLoadGame.draw();
                btnDeadQuit.draw();
            } break;
        }
        bbSetFont(uiAssets->font[1]);
        bbText(x, (int)(y - 77 * MenuScale), titleText, false, true);
        bbSetFont(uiAssets->font[0]);
    }
}

}
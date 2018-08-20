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

    int width = 390;
    int height = 60;
    int x = 1024 / 2 - 600 / 2;
    int y = 1024 / 2 - 600 / 2;
    x += 132;
    y += 132;

    y += 72;
    btnResume = GUIButton(x, y, width, height, "Resume", true);
    btnDeadLoadGame = GUIButton(x, y, width, height, "Load Game", true);
    y += 75;
    btnLoadGame = GUIButton(x, y, width, height, "Load Game", true);
    btnDeadQuit = GUIButton(x, y, width, height, "Quit", true);
    y += 75;
    btnOptions = GUIButton(x, y, width, height, "Options", true);
    y += 75;
    btnQuit = GUIButton(x, y, width, height, "Quit", true);
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
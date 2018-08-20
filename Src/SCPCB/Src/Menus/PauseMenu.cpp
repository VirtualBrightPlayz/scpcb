#include <bbgraphics.h>
#include <bbinput.h>

#include "PauseMenu.h"
#include "../Player/Player.h"
#include "../AssetMgmt/Assets.h"
#include "../AssetMgmt/Audio.h"
#include "../Config/Options.h"
#include "Menu.h"
#include "MainMenu.h"

namespace CBN {

PauseMenu* pauseMenu = nullptr;

PauseMenu::PauseMenu() {
    int width = 390;
    int height = 60;
    int x = userOptions->screenWidth / 2 - bbImageWidth(uiAssets->pauseMenuBG) / 2;
    int y = userOptions->screenHeight / 2 - bbImageHeight(uiAssets->pauseMenuBG) / 2;
    x += 132;
    y += 132;

    y += 72;
    btnResume = GUIButton(x, y, width, height, "Resume", true);
    y += 75;
    btnLoadGame = GUIButton(x, y, width, height, "Load Game", true);
    y += 75;
    btnOptions = GUIButton(x, y, width, height, "Options", true);
    y += 75;
    btnQuit = GUIButton(x, y, width, height, "Quit", true);
}

void PauseMenu::update() {
    if (CurrGameState == GAMESTATE_PAUSED) {
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
            mainMenu = new MainMenu();
            CurrSave = "";
            bbFlushKeys();
        }

        bbSetFont(uiAssets->font[0]);
    }
}

}
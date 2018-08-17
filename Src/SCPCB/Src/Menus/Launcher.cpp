#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbmath.h>

#include "Launcher.h"
#include "../MathUtils/MathUtils.h"
#include "../Config/INI.h"
#include "../Config/Options.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../AssetMgmt/Assets.h"
#include "GUI/GUIButton.h"
#include "GUI/GUITick.h"

namespace CBN {

// Globals.
Launcher* launcher = nullptr;

Launcher::Launcher() {
    this->width = (int)(Min((float)GetINIInt(OptionFile, "launcher", "launcher width"), 1024.f));
    this->height = (int)(Min((float)GetINIInt(OptionFile, "launcher", "launcher height"), 768.f));

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            this->resWidths.push_back(bbGfxModeWidth(i));
            this->resHeights.push_back(bbGfxModeHeight(i));
        }
    }

    verifyResolution();

    Graphics3DExt(this->width, this->height, 0, 2);
    bbAppTitle("SCP - Containment Breach Launcher");

    MenuScale = 1;

    this->background = bbLoadImage("GFX/menu/launcher.jpg");

    uiAssets = new UIAssets();

    btnLaunch = new GUIButton(this->width - 30 - 90, this->height - 50 - 55, 100, 30, "LAUNCH", false, false);
    btnExit = new GUIButton(this->width - 30 - 90, this->height - 50, 100, 30, "EXIT", false, false);
    tckFullscreen = new GUITick(40 + 430 - 15, 260 - 55 + 5 - 8, "Fullscreen", userOptions->fullscreen, false);
    tckUseLauncher = new GUITick(40 + 430 - 15, 260 - 55 + 95 + 8, "Use launcher", userOptions->launcher, false);

    bbSetBuffer(bbBackBuffer());

    bbSetFont(uiAssets->font[0]);
}

Launcher::~Launcher() {
    bbFreeImage(this->background);
    delete uiAssets;
    delete btnLaunch;
    delete btnExit;
    delete tckFullscreen;
    delete tckUseLauncher;
}

void Launcher::verifyResolution() {
    selectedGFXMode = resWidths.size()-1;
    for (int i = 0;i<resWidths.size();i++) {
        if (userOptions->screenWidth == resWidths[i] && userOptions->screenHeight == resHeights[i]) {
            selectedGFXMode = i;
            return;
        }
    }
}

void Launcher::update() {
    int x = 40;
    int y = 280 - 65;

    int i;
    for (int i = 0; i < resWidths.size(); i++) {
        if (MouseOn(x - 1, y - 1, 100, 20)) {
            if (MouseHit1) {
                this->selectedGFXMode = i;
            }
        }

        y = y+20;
        if (y >= 240 - 65 + (this->height - 80 - 260)) {
            y = 280 - 65;
            x = x + 100;
        }
    }

    //-----------------------------------------------------------------
    x = 30;
    y = 369;

    y = y + 10;
    for (i = 1; i <= bbCountGfxDrivers(); i++) {
        if (MouseOn(x - 1, y - 1, 290, 20)) {
            if (MouseHit1) {
                userOptions->gfxDriver = i;
            }
        }

        y = y + 20;
    }

    btnLaunch->update();
    btnExit->update();
    tckFullscreen->update();
    tckUseLauncher->update();

    userOptions->fullscreen = tckFullscreen->ticked;
    userOptions->launcher = tckUseLauncher->ticked;

    if (btnLaunch->isMouseHit()) {
        userOptions->screenWidth = this->resWidths[this->selectedGFXMode];
        userOptions->screenHeight = this->resHeights[this->selectedGFXMode];

        userOptions->gfxDriver = userOptions->gfxDriver;

        SaveOptionsINI();
        delete this; //TODO: wtf am i doing here
        InitializeMainGame();

        return;
    }

    if (btnExit->isMouseHit()) {
        //End(); TODO: Re-implement.
    }
}

void Launcher::draw() {
    bbSetFont(uiAssets->font[0]);

    bbColor(0,0,0);
    bbRect(0, 0, launcher->width, launcher->height, true);

    bbColor(255, 255, 255);
    bbDrawImage(launcher->background, 0, 0);

    bbText(20, 240 - 65, "Resolution:");

    int x = 40;
    int y = 280 - 65;

    int i;
    for (i = 0; i < resWidths.size(); i++) {
        if (this->selectedGFXMode == i) {
            bbColor(0, (int)((bbSin(bbMilliSecs() / 10.f) + 1) * 45), (int)((bbSin(bbMilliSecs() / 10.f) + 1) * 95));
            bbRect(x - 1, y - 1, 100, 20, false);
        }

        bbColor(0, 0, 0);
        bbText(x, y, String(this->resWidths[i]) + "x" + String(this->resHeights[i]));
        if (MouseOn(x - 1, y - 1, 100, 20)) {
            bbColor(100, 100, 100);
            bbRect(x - 1, y - 1, 100, 20, false);
        }

        y = y+20;
        if (y >= 240 - 65 + (this->height - 80 - 260)) {
            y = 280 - 65;
            x = x + 100;
        }
    }

    bbColor(255, 255, 255);
    x = 30;
    y = 369;
    bbRect(x - 10, y, 340, 95);
    bbText(x - 10, y - 25, "Graphics:");

    y = y + 10;
    for (i = 1; i <= bbCountGfxDrivers(); i++) {
        bbColor(0, 0, 0);
        if (userOptions->gfxDriver == i) {
            bbRect(x - 1, y - 1, 290, 20, false);
        }
        //text(x, y, bbGfxDriverName(i))

        LimitText(bbGfxDriverName(i), x, y, 290, false);
        if (MouseOn(x - 1, y - 1, 290, 20)) {
            bbColor(100, 100, 100);
            bbRect(x - 1, y - 1, 290, 20, false);
        }

        y = y + 20;
    }

    tckFullscreen->draw();
    bbText(40 + 430 + 15,       262 - 55 + 5 - 8, "Fullscreen");

    tckUseLauncher->draw();
    bbText(40 + 430 + 15,       262 - 55 + 95 + 8, "Use launcher");

    bbText(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+String(this->resWidths[launcher->selectedGFXMode]) + "x" + String(this->resHeights[launcher->selectedGFXMode]));

    //If (GfxModeWidths(SelectedGFXMode)<G_viewport_width) Then
    //	Text(40+ 260 + 65, 262 - 55 + 160, "(upscaled to")
    //ElseIf ((GfxModeWidths(SelectedGFXMode)>G_viewport_width)) Then
    //	Text(40+ 260 + 65, 262 - 55 + 160, "(downscaled to")
    //EndIf

    //Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ")")

    btnLaunch->draw();
    btnExit->draw();
    bbFlip();
}

}

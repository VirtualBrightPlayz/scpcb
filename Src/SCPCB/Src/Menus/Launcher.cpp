#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>
#include <bbmath.h>
#include <StringType.h>

#include "Launcher.h"
#include "../MathUtils/MathUtils.h"
#include "../INI.h"
#include "../Options.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../Assets.h"

namespace CBN {

// Globals.
Launcher* launcher = nullptr;

Launcher::Launcher() {
    this->width = (int)(Min(GetINIInt(OptionFile, "launcher", "launcher width"), 1024));
    this->height = (int)(Min(GetINIInt(OptionFile, "launcher", "launcher height"), 768));

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            this->resWidths.push_back(bbGfxModeWidth(i));
            this->resHeights.push_back(bbGfxModeHeight(i));
        }
    }

    this->selectedGFXMode = VerifyResolution();

    Graphics3DExt(this->width, this->height, 0, 2);
    bbAppTitle("SCP - Containment Breach Launcher");

    MenuScale = 1;

    this->background = bbLoadImage("GFX/menu/launcher.jpg");

    uiAssets = new UIAssets();

    bbSetBuffer(bbBackBuffer());

    bbSetFont(uiAssets->font[0]);
}

Launcher::~Launcher() {
    bbFreeImage(this->background);
    delete uiAssets;
}

void Launcher::update() {
    int x = 40;
    int y = 280 - 65;

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            if (MouseOn(x - 1, y - 1, 100, 20)) {
                if (MouseHit1) {
                    this->selectedGFXMode = i-1;
                }
            }

            y = y+20;
            if (y >= 240 - 65 + (this->height - 80 - 260)) {
                y = 280 - 65;
                x = x + 100;
            }
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

    userOptions->fullscreen = UpdateUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions->fullscreen);

    userOptions->launcher = UpdateUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions->launcher);

    if (UpdateUIButton(this->width - 30 - 90, this->height - 50 - 55, 100, 30, "LAUNCH", false)) {
        userOptions->screenWidth = this->resWidths[this->selectedGFXMode];
        userOptions->screenHeight = this->resHeights[this->selectedGFXMode];

        userOptions->gfxDriver = userOptions->gfxDriver;

        SaveOptionsINI();
        delete this; //TODO: wtf am i doing here
        InitializeMainGame();

        return;
    }

    if (UpdateUIButton(this->width - 30 - 90, this->height - 50, 100, 30, "EXIT", false)) {
        //End(); TODO: Re-implement.
    }
}

void Launcher::draw() {
    bbSetFont(uiAssets->font[0]);

    bbColor(0,0,0);
    bbRect(0, 0, launcher->width, launcher->height, true);

    bbColor(255, 255, 255);
    bbDrawImage(launcher->background, 0, 0);

    bbText(20, 240 - 65, "Resolution: ");

    int x = 40;
    int y = 280 - 65;

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            bbColor(0, (bbSin(bbMilliSecs() / 10)+1)*45, (bbSin(bbMilliSecs() / 10) + 1)*95);

            if (this->selectedGFXMode == (i-1)) {
                bbRect(x - 1, y - 1, 100, 20, false);
            }

            bbColor(0, 0, 0);
            bbText(x, y, String(this->resWidths[i - 1]) + "x" + String(this->resHeights[i - 1]));
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
    }

    bbColor(255, 255, 255);
    x = 30;
    y = 369;
    bbRect(x - 10, y, 340, 95);
    bbText(x - 10, y - 25, "bbGraphics:");

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

    DrawUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions->fullscreen);
    bbText(40 + 430 + 15,       262 - 55 + 5 - 8, "Fullscreen");

    bbColor(255, 255, 255);
    //Text(40 + 430 + 15, 262 - 55 + 35 - 8, "Borderless",False,False)
    //Text(40 + 430 + 15, 262 - 55 + 35 + 12, "windowed mode",False,False)

    DrawUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions->launcher);
    bbText(40 + 430 + 15,       262 - 55 + 95 + 8, "Use launcher");

    bbText(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+String(this->resWidths[launcher->selectedGFXMode]) + "x" + String(this->resHeights[launcher->selectedGFXMode]));

    //If (GfxModeWidths(SelectedGFXMode)<G_viewport_width) Then
    //	Text(40+ 260 + 65, 262 - 55 + 160, "(upscaled to")
    //ElseIf ((GfxModeWidths(SelectedGFXMode)>G_viewport_width)) Then
    //	Text(40+ 260 + 65, 262 - 55 + 160, "(downscaled to")
    //EndIf

    //Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ")")

    DrawUIButton(launcher->width - 30 - 90, launcher->height - 50 - 55, 100, 30, "LAUNCH", false);

    DrawUIButton(launcher->width - 30 - 90, launcher->height - 50, 100, 30, "EXIT", false);
    bbFlip();
}

}

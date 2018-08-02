#include "Launcher.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Launcher*> Launcher::list;
Launcher::Launcher() {
    list.push_back(this);
}
Launcher::~Launcher() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Launcher::getListSize() {
    return list.size();
}
Launcher* Launcher::getObject(int index) {
    return list[index];
}

// Globals.
Launcher* launcher = nullptr;

// Functions.
Launcher* CreateLauncher() {
    Launcher* launch = new Launcher();

    launch->width = Int(Min(GetINIInt(OptionFile, "launcher", "launcher width"), 1024));
    launch->height = Int(Min(GetINIInt(OptionFile, "launcher", "launcher height"), 768));

    launch->resWidths = CreateIntArray(bbCountGfxModes3D());
    launch->resHeights = CreateIntArray(bbCountGfxModes3D());

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            SetIntArrayElem(launch->resWidths, bbGfxModeWidth(i), i - 1);
            SetIntArrayElem(launch->resHeights, bbGfxModeHeight(i), i - 1);
        }
    }

    launch->selectedGFXMode = VerifyResolution();

    Graphics3DExt(launch->width, launch->height, 0, 2);
    bbAppTitle("SCP - Containment Breach Launcher");

    MenuScale = 1;

    launch->background = bbLoadImage("GFX/menu/launcher.jpg");

    InitializeUIAssets();

    bbSetBuffer(bbBackBuffer());

    bbSetFont(uiAssets->font[0]);

    return launch;
}

void DestroyLauncher(Launcher* launch) {
    bbFreeImage(launch->background);

    ReleaseUIAssets();

    delete launch;
}

void UpdateLauncher() {
    int x = 40;
    int y = 280 - 65;

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            if (MouseOn(x - 1, y - 1, 100, 20)) {
                if (MouseHit1) {
                    launcher->selectedGFXMode = i-1;
                }
            }

            y = y+20;
            if (y >= 240 - 65 + (launcher->height - 80 - 260)) {
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

    if (UpdateUIButton(launcher->width - 30 - 90, launcher->height - 50 - 55, 100, 30, "LAUNCH", false)) {
        userOptions->screenWidth = GetIntArrayElem(launcher->resWidths, launcher->selectedGFXMode);
        userOptions->screenHeight = GetIntArrayElem(launcher->resHeights, launcher->selectedGFXMode);

        userOptions->gfxDriver = userOptions->gfxDriver;

        SaveOptionsINI();
        DestroyLauncher(launcher);
        launcher = nullptr;

        InitializeMainGame();

        return;
    }

    if (UpdateUIButton(launcher->width - 30 - 90, launcher->height - 50, 100, 30, "EXIT", false)) {
        End();
    }
}

void DrawLauncher() {
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
            bbColor(0, 0, 0);

            if (launcher->selectedGFXMode == (i-1)) {
                bbRect(x - 1, y - 1, 100, 20, false);
            }

            bbText(x, y, (String(GetIntArrayElem(launcher->resWidths, i - 1)) + "x" + String(GetIntArrayElem(launcher->resHeights, i - 1))));
            if (MouseOn(x - 1, y - 1, 100, 20)) {
                bbColor(100, 100, 100);
                bbRect(x - 1, y - 1, 100, 20, false);
            }

            y = y+20;
            if (y >= 240 - 65 + (launcher->height - 80 - 260)) {
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

    bbText(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+String(GetIntArrayElem(launcher->resWidths, launcher->selectedGFXMode)) + "x" + String(GetIntArrayElem(launcher->resHeights, launcher->selectedGFXMode)));

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

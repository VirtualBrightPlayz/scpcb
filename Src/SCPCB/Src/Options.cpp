#include "Options.h"

#include <StringType.h>

#include "INI.h"
#include "MathUtils/MathUtils.h"

namespace CBN {

// Globals.
KeyBinds* keyBinds;
Options* userOptions;

// Functions.
void LoadOptionsINI() {
    String optGen = "general";
    String optGra = "graphics";
    String optAud = "audio";
    String optBin = "binds";
    String optHud = "hud";

    userOptions->introEnabled = GetINIInt(OptionFile, optGen, "intro enabled");
    userOptions->mouseSensitivity = GetINIFloat(OptionFile, optGen, "mouse sensitivity");
    userOptions->invertMouseY = GetINIInt(OptionFile, optGen, "invert mouse y");
    userOptions->consoleOpenOnError = GetINIInt(OptionFile, optGen, "open console on error");

    //Min is 640x480.
    userOptions->screenWidth = (int)(Max(GetINIInt(OptionFile, optGra, "screen width"), 640));
    userOptions->screenHeight = (int)(Max(GetINIInt(OptionFile, optGra, "screen height"), 480));
    userOptions->fullscreen = GetINIInt(OptionFile, optGra, "fullscreen");
    userOptions->gfxDriver = (int)(Max(GetINIInt(OptionFile, optGra, "gfx driver"), 1));
    userOptions->screenGamma = GetINIFloat(OptionFile, optGra, "screengamma");
    userOptions->framelimit = GetINIInt(OptionFile, optGra, "framelimit");
    userOptions->vsync = GetINIInt(OptionFile, optGra, "vsync");

    userOptions->musicVolume = GetINIFloat(OptionFile, optAud, "music volume");
    userOptions->sndVolume = GetINIFloat(OptionFile, optAud, "sound volume");

    userOptions->hudEnabled = GetINIInt(OptionFile, optHud, "HUD enabled");
    userOptions->showFPS = GetINIInt(OptionFile, optHud, "show FPS");

    keyBinds->right = GetINIInt(OptionFile, optBin, "Right key");
    keyBinds->left = GetINIInt(OptionFile, optBin, "Left key");
    keyBinds->up = GetINIInt(OptionFile, optBin, "Up key");
    keyBinds->down = GetINIInt(OptionFile, optBin, "Down key");

    keyBinds->blink = GetINIInt(OptionFile, optBin, "Blink key");
    keyBinds->sprint = GetINIInt(OptionFile, optBin, "Sprint key");
    keyBinds->inv = GetINIInt(OptionFile, optBin, "Inventory key");
    keyBinds->crouch = GetINIInt(OptionFile, optBin, "Crouch key");

    keyBinds->save = GetINIInt(OptionFile, optBin, "Save key");
    keyBinds->console = GetINIInt(OptionFile, optBin, "Console key");

    userOptions->launcher = GetINIInt(OptionFile, "launcher", "launcher enabled");
}

void SaveOptionsINI() {
    String optGen = "general";
    String optGra = "graphics";
    String optAud = "audio";
    String optBin = "binds";
    String optHud = "hud";

    PutINIValue(OptionFile, optGen, "intro enabled", String(userOptions->introEnabled));
    PutINIValue(OptionFile, optGen, "mouse sensitivity", String(userOptions->mouseSensitivity));
    PutINIValue(OptionFile, optGen, "invert mouse y", String(userOptions->invertMouseY));
    PutINIValue(OptionFile, optGen, "open console on error", String(userOptions->consoleOpenOnError));

    PutINIValue(OptionFile, optGra, "screen width", String(userOptions->screenWidth));
    PutINIValue(OptionFile, optGra, "screen height", String(userOptions->screenHeight));
    PutINIValue(OptionFile, optGra, "fullscreen", String(userOptions->fullscreen));
    PutINIValue(OptionFile, optGra, "gfx driver", String(userOptions->gfxDriver));
    PutINIValue(OptionFile, optGra, "screengamma", String(userOptions->screenGamma));
    PutINIValue(OptionFile, optGra, "framelimit", String(userOptions->framelimit));
    PutINIValue(OptionFile, optGra, "vsync", String(userOptions->vsync));

    PutINIValue(OptionFile, optAud, "music volume", String(userOptions->musicVolume));
    PutINIValue(OptionFile, optAud, "sound volume", String(userOptions->sndVolume));
    PutINIValue(OptionFile, optHud, "HUD enabled", String(userOptions->hudEnabled));
    PutINIValue(OptionFile, optHud, "show FPS", String(userOptions->showFPS));

    PutINIValue(OptionFile, optBin, "Right key", String(keyBinds->right));
    PutINIValue(OptionFile, optBin, "Left key", String(keyBinds->left));
    PutINIValue(OptionFile, optBin, "Up key", String(keyBinds->up));
    PutINIValue(OptionFile, optBin, "Down key", String(keyBinds->down));

    PutINIValue(OptionFile, optBin, "Blink key", String(keyBinds->blink));
    PutINIValue(OptionFile, optBin, "Sprint key", String(keyBinds->sprint));
    PutINIValue(OptionFile, optBin, "Inventory key", String(keyBinds->inv));
    PutINIValue(OptionFile, optBin, "Crouch key", String(keyBinds->crouch));

    PutINIValue(OptionFile, optBin, "Save key", String(keyBinds->save));
    PutINIValue(OptionFile, optBin, "Console key", String(keyBinds->console));

    PutINIValue(OptionFile, "launcher", "launcher enabled", String(userOptions->launcher));
}

}

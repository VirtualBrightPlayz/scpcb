#include "Options.h"


#include "INI.h"
#include "../MathUtils/MathUtils.h"

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

    userOptions->introEnabled = getINIInt(OptionFile, optGen, "intro enabled");
    userOptions->mouseSensitivity = getINIFloat(OptionFile, optGen, "mouse sensitivity");
    userOptions->invertMouseY = getINIInt(OptionFile, optGen, "invert mouse y");
    userOptions->consoleOpenOnError = getINIInt(OptionFile, optGen, "open console on error");

    //Min is 640x480.
    userOptions->screenWidth = (int)(Max((float)getINIInt(OptionFile, optGra, "screen width"), 640.f));
    userOptions->screenHeight = (int)(Max((float)getINIInt(OptionFile, optGra, "screen height"), 480.f));
    userOptions->fullscreen = getINIInt(OptionFile, optGra, "fullscreen");
    userOptions->gfxDriver = (int)(Max((float)getINIInt(OptionFile, optGra, "gfx driver"), 1.f));
    userOptions->screenGamma = getINIFloat(OptionFile, optGra, "screengamma");
    userOptions->framelimit = getINIInt(OptionFile, optGra, "framelimit");
    userOptions->vsync = getINIInt(OptionFile, optGra, "vsync");

    userOptions->musicVolume = getINIFloat(OptionFile, optAud, "music volume");
    userOptions->sndVolume = getINIFloat(OptionFile, optAud, "sound volume");

    userOptions->hudEnabled = getINIInt(OptionFile, optHud, "HUD enabled");
    userOptions->showFPS = getINIInt(OptionFile, optHud, "show FPS");

    keyBinds->right = getINIInt(OptionFile, optBin, "Right key");
    keyBinds->left = getINIInt(OptionFile, optBin, "Left key");
    keyBinds->up = getINIInt(OptionFile, optBin, "Up key");
    keyBinds->down = getINIInt(OptionFile, optBin, "Down key");

    keyBinds->blink = getINIInt(OptionFile, optBin, "Blink key");
    keyBinds->sprint = getINIInt(OptionFile, optBin, "Sprint key");
    keyBinds->inv = getINIInt(OptionFile, optBin, "Inventory key");
    keyBinds->crouch = getINIInt(OptionFile, optBin, "Crouch key");

    keyBinds->save = getINIInt(OptionFile, optBin, "Save key");
    keyBinds->console = getINIInt(OptionFile, optBin, "Console key");

    userOptions->launcher = getINIInt(OptionFile, "launcher", "launcher enabled");
}

void SaveOptionsINI() {
    String optGen = "general";
    String optGra = "graphics";
    String optAud = "audio";
    String optBin = "binds";
    String optHud = "hud";

    putINIValue(OptionFile, optGen, "intro enabled", String(userOptions->introEnabled));
    putINIValue(OptionFile, optGen, "mouse sensitivity", String(userOptions->mouseSensitivity));
    putINIValue(OptionFile, optGen, "invert mouse y", String(userOptions->invertMouseY));
    putINIValue(OptionFile, optGen, "open console on error", String(userOptions->consoleOpenOnError));

    putINIValue(OptionFile, optGra, "screen width", String(userOptions->screenWidth));
    putINIValue(OptionFile, optGra, "screen height", String(userOptions->screenHeight));
    putINIValue(OptionFile, optGra, "fullscreen", String(userOptions->fullscreen));
    putINIValue(OptionFile, optGra, "gfx driver", String(userOptions->gfxDriver));
    putINIValue(OptionFile, optGra, "screengamma", String(userOptions->screenGamma));
    putINIValue(OptionFile, optGra, "framelimit", String(userOptions->framelimit));
    putINIValue(OptionFile, optGra, "vsync", String(userOptions->vsync));

    putINIValue(OptionFile, optAud, "music volume", String(userOptions->musicVolume));
    putINIValue(OptionFile, optAud, "sound volume", String(userOptions->sndVolume));
    putINIValue(OptionFile, optHud, "HUD enabled", String(userOptions->hudEnabled));
    putINIValue(OptionFile, optHud, "show FPS", String(userOptions->showFPS));

    putINIValue(OptionFile, optBin, "Right key", String(keyBinds->right));
    putINIValue(OptionFile, optBin, "Left key", String(keyBinds->left));
    putINIValue(OptionFile, optBin, "Up key", String(keyBinds->up));
    putINIValue(OptionFile, optBin, "Down key", String(keyBinds->down));

    putINIValue(OptionFile, optBin, "Blink key", String(keyBinds->blink));
    putINIValue(OptionFile, optBin, "Sprint key", String(keyBinds->sprint));
    putINIValue(OptionFile, optBin, "Inventory key", String(keyBinds->inv));
    putINIValue(OptionFile, optBin, "Crouch key", String(keyBinds->crouch));

    putINIValue(OptionFile, optBin, "Save key", String(keyBinds->save));
    putINIValue(OptionFile, optBin, "Console key", String(keyBinds->console));

    putINIValue(OptionFile, "launcher", "launcher enabled", String(userOptions->launcher));
}

}

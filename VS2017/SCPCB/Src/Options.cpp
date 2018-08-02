#include "Options.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<KeyBinds*> KeyBinds::list;
KeyBinds::KeyBinds() {
    list.push_back(this);
}
KeyBinds::~KeyBinds() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int KeyBinds::getListSize() {
    return list.size();
}
KeyBinds* KeyBinds::getObject(int index) {
    return list[index];
}

std::vector<Options*> Options::list;
Options::Options() {
    list.push_back(this);
}
Options::~Options() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Options::getListSize() {
    return list.size();
}
Options* Options::getObject(int index) {
    return list[index];
}

// Constants.
const String OptionFile = "options.ini";

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
    userOptions->screenWidth = Int(Max(GetINIInt(OptionFile, optGra, "screen width"), 640));
    userOptions->screenHeight = Int(Max(GetINIInt(OptionFile, optGra, "screen height"), 480));
    userOptions->fullscreen = GetINIInt(OptionFile, optGra, "fullscreen");
    userOptions->gfxDriver = Int(Max(GetINIInt(OptionFile, optGra, "gfx driver"), 1));
    userOptions->screenGamma = GetINIFloat(OptionFile, optGra, "screengamma");
    userOptions->framelimit = GetINIInt(OptionFile, optGra, "framelimit");
    userOptions->vsync = GetINIInt(OptionFile, optGra, "vsync");

    userOptions->musicVolume = GetINIFloat(OptionFile, optAud, "music volume");
    userOptions->sndVolume = GetINIFloat(OptionFile, optAud, "sound volume");

    userOptions->hudEnabled = GetINIInt(OptionFile, optHud, "HUD enabled");
    userOptions->showFPS = GetINIInt(OptionFile, optHud, "show FPS");

    keyBinds->rght = GetINIInt(OptionFile, optBin, "bbRight key");
    keyBinds->lft = GetINIInt(OptionFile, optBin, "bbLeft key");
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

    PutINIValue(OptionFile, optGen, "intro enabled", Str(userOptions->introEnabled));
    PutINIValue(OptionFile, optGen, "mouse sensitivity", Str(userOptions->mouseSensitivity));
    PutINIValue(OptionFile, optGen, "invert mouse y", Str(userOptions->invertMouseY));
    PutINIValue(OptionFile, optGen, "open console on error", Str(userOptions->consoleOpenOnError));

    PutINIValue(OptionFile, optGra, "screen width", Str(userOptions->screenWidth));
    PutINIValue(OptionFile, optGra, "screen height", Str(userOptions->screenHeight));
    PutINIValue(OptionFile, optGra, "fullscreen", Str(userOptions->fullscreen));
    PutINIValue(OptionFile, optGra, "gfx driver", Str(userOptions->gfxDriver));
    PutINIValue(OptionFile, optGra, "screengamma", Str(userOptions->screenGamma));
    PutINIValue(OptionFile, optGra, "framelimit", Str(userOptions->framelimit));
    PutINIValue(OptionFile, optGra, "vsync", Str(userOptions->vsync));

    PutINIValue(OptionFile, optAud, "music volume", Str(userOptions->musicVolume));
    PutINIValue(OptionFile, optAud, "sound volume", Str(userOptions->sndVolume));
    PutINIValue(OptionFile, optHud, "HUD enabled", Str(userOptions->hudEnabled));
    PutINIValue(OptionFile, optHud, "show FPS", Str(userOptions->showFPS));

    PutINIValue(OptionFile, optBin, "bbRight key", Str(keyBinds->rght));
    PutINIValue(OptionFile, optBin, "bbLeft key", Str(keyBinds->lft));
    PutINIValue(OptionFile, optBin, "Up key", Str(keyBinds->up));
    PutINIValue(OptionFile, optBin, "Down key", Str(keyBinds->down));

    PutINIValue(OptionFile, optBin, "Blink key", Str(keyBinds->blink));
    PutINIValue(OptionFile, optBin, "Sprint key", Str(keyBinds->sprint));
    PutINIValue(OptionFile, optBin, "Inventory key", Str(keyBinds->inv));
    PutINIValue(OptionFile, optBin, "Crouch key", Str(keyBinds->crouch));

    PutINIValue(OptionFile, optBin, "Save key", Str(keyBinds->save));
    PutINIValue(OptionFile, optBin, "Console key", Str(keyBinds->console));

    PutINIValue(OptionFile, "launcher", "launcher enabled", Str(userOptions->launcher));
}

}

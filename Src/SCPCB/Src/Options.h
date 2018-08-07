#ifndef OPTIONS_H_INCLUDED
#define OPTIONS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct KeyBinds {
public:
    int right;
    int left;
    int up;
    int down;

    int blink;
    int sprint;
    int inv;
    int crouch;

    int save;
    int console;
};

struct Options {
public:

    //General
    bool introEnabled;
    float mouseSensitivity;
    bool invertMouseY;
    bool consoleOpenOnError; // TODO: Remove?

    //Graphics
    int screenWidth;
    int screenHeight;
    bool fullscreen;
    int gfxDriver;
    float screenGamma;
    int framelimit;
    bool vsync;

    //Audio
    float musicVolume;
    float sndVolume;

    //HUD
    bool hudEnabled;
    bool showFPS;

    bool launcher;

};

// Constants.
const String OptionFile("options.ini");

// Globals.
extern KeyBinds* keyBinds;
extern Options* userOptions;

// Functions.
void LoadOptionsINI();

void SaveOptionsINI();

}
#endif // OPTIONS_H_INCLUDED

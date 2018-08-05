#ifndef OPTIONS_H_INCLUDED
#define OPTIONS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct KeyBinds {
private:
    static std::vector<KeyBinds*> list;

public:
    KeyBinds();
    ~KeyBinds();
    static int getListSize();
    static KeyBinds* getObject(int index);

    //TODO: an't call it right/left because blitz
    int rght;
    int lft;
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
private:
    static std::vector<Options*> list;

public:
    Options();
    ~Options();
    static int getListSize();
    static Options* getObject(int index);

    //General
    bool introEnabled;
    float mouseSensitivity;
    int invertMouseY;
    int consoleOpenOnError;

    //Graphics
    int screenWidth;
    int screenHeight;
    int fullscreen;
    int gfxDriver;
    float screenGamma;
    int framelimit;
    int vsync;

    //Audio
    float musicVolume;
    float sndVolume;

    //HUD
    int hudEnabled;
    int showFPS;

    int launcher;

};

// Constants.
extern const String OptionFile;

// Globals.
extern KeyBinds* keyBinds;
extern Options* userOptions;

// Functions.
void LoadOptionsINI();

void SaveOptionsINI();

}
#endif // OPTIONS_H_INCLUDED

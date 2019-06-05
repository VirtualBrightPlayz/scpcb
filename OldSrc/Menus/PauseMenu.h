#ifndef PAUSEMENU_H_INCLUDED
#define PAUSEMENU_H_INCLUDED
#include "GUI/GUIButton.h"

namespace CBN {

enum class PauseMenuState {
    Main = 0,
    Dead = 1,
    Options = 2,
    Quit = 3
};

class PauseMenu {
private:
    GUIButton btnResume;
    GUIButton btnLoadGame;
    GUIButton btnOptions;
    GUIButton btnQuit;

    GUIButton btnDeadLoadGame;
    GUIButton btnDeadQuit;
public:
    PauseMenu();

    PauseMenuState currState;

    void update();
    void draw();
};
extern PauseMenu* pauseMenu;

}
#endif // PAUSEMENU_H_INCLUDED

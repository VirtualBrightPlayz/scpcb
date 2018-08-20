#ifndef PAUSEMENU_H_INCLUDED
#define PAUSEMENU_H_INCLUDED
#include "GUI/GUIButton.h"

namespace CBN {

class PauseMenu {
private:
    GUIButton btnResume;
    GUIButton btnLoadGame;
    GUIButton btnOptions;
    GUIButton btnQuit;
public:
    PauseMenu();

    void update();
    void draw();
};
extern PauseMenu* pauseMenu;

}
#endif // PAUSEMENU_H_INCLUDED

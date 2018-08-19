#ifndef MAINMENU_H_INCLUDED
#define MAINMENU_H_INCLUDED
#include <vector>
#include "GUI/GUIButton.h"
#include "GUI/GUITick.h"
#include "GUI/GUIInput.h"

namespace CBN {

enum class MainMenuState {
    Main = 0,
    NewGame = 1,
    LoadGame = 2,
    CustomMap = 3,
    Options = 4
};

class MainMenu {
private:
    MainMenuState currState;

    float blinkTimer[2];
    float blinkDuration[2];
    String flashStr;
    int flashStrX;
    int flashStrY;
public:
    MainMenu();

    GUIButton btnNewGame;
    GUIButton btnStartGame;
    GUIButton btnLoadMap;
    GUITick tckIntro;
    GUIInput txtName;
    GUIInput txtSeed;

    GUIButton btnLoadGame;
    std::vector<std::pair<GUIButton, GUIButton>> btnSaveFiles;

    GUIButton btnOptions;

    GUIButton btnQuit;
    GUIButton btnBack;

    void update();
    void draw();
};
extern MainMenu* mainMenu;

}
#endif // MAINMENU_H_INCLUDED

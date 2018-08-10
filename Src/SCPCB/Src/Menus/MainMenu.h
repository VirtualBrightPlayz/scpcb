#ifndef MAINMENU_H_INCLUDED
#define MAINMENU_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
const int GAMESUBSTATE_MAINMENU_MAIN = 0;
const int GAMESUBSTATE_MAINMENU_NEWGAME = 1;
const int GAMESUBSTATE_MAINMENU_CUSTOMMAP = 2;
const int GAMESUBSTATE_MAINMENU_LOADGAME = 3;
const int GAMESUBSTATE_MAINMENU_OPTIONS = 4;
const int MAINMENU_BUTTON_NEWGAME = 0;
const int MAINMENU_BUTTON_LOADGAME = 1;
const int MAINMENU_BUTTON_OPTIONS = 2;
const int MAINMENU_BUTTON_QUIT = 3;
const int MAINMENU_BUTTON_COUNT = 4;

// Functions.
void UpdateMainMenu();

void DrawMainMenu();

}
#endif // MAINMENU_H_INCLUDED

#ifndef MAINMENU_H_INCLUDED
#define MAINMENU_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int GAMESUBSTATE_MAINMENU_MAIN;
extern const int GAMESUBSTATE_MAINMENU_NEWGAME;
extern const int GAMESUBSTATE_MAINMENU_CUSTOMMAP;
extern const int GAMESUBSTATE_MAINMENU_LOADGAME;
extern const int GAMESUBSTATE_MAINMENU_OPTIONS;
extern const int MAINMENU_BUTTON_NEWGAME;
extern const int MAINMENU_BUTTON_LOADGAME;
extern const int MAINMENU_BUTTON_OPTIONS;
extern const int MAINMENU_BUTTON_QUIT;
extern const int MAINMENU_BUTTON_COUNT;

// Functions.
void UpdateMainMenu();

void DrawMainMenu();

}
#endif // MAINMENU_H_INCLUDED

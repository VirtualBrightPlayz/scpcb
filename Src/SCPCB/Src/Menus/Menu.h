#ifndef MENU_H_INCLUDED
#define MENU_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int GAMESTATE_MAINMENU;
extern const int GAMESTATE_PLAYING;
extern const int GAMESTATE_PAUSED;
extern const int GAMESTATE_CONSOLE;
extern const int GAMESTATE_INVENTORY;
extern const int GAMESTATE_SCP294;
extern const int GAMESTATE_ENDING;
extern const int GAMESTATE_LAUNCHER;
extern const int GAMESUBSTATE_PAUSED_MAIN;
extern const int GAMESUBSTATE_PAUSED_DEAD;
extern const int GAMESUBSTATE_PAUSED_OPTIONS;

// Globals.
extern String SelectedEnding;
extern int CurrGameState;
extern int CurrGameSubstate;
extern int CurrGameStatePage;
extern float MsgTimer;
extern String Msg;
extern String DeathMSG;
extern int AccessCode;
extern String KeypadInput;
extern float KeypadTimer;
extern String KeypadMSG;
extern int DrawHandIcon;
extern float MenuScale;
extern String RandomSeed;
extern float MenuBlinkTimer[2];
extern float MenuBlinkDuration[2];
extern String MenuStr;
extern int MenuStrX;
extern int MenuStrY;
extern int SelectedInputBox;
extern String SavePath;
extern String SaveMSG;
extern String CurrSave;

// Functions.
int IsPaused();

void DrawTiledImageRect(int img, int srcX, int srcY, int srcwidth, int srcheight, int x, int y, int width, int height);

String rInput(String aString);

int MouseOn(int x, int y, int width, int height);

String UpdateInputBox(int x, int y, int width, int height, String Txt, int ID = 0);

String DrawInputBox(int x, int y, int width, int height, String Txt, int ID = 0);

void DrawFrame(int x, int y, int width, int height, int xoffset = 0, int yoffset = 0);

void DrawUIButton(int x, int y, int width, int height, String txt, int bigfont = true);

int UpdateUIButton(int x, int y, int width, int height, String txt = "", int waitForMouseUp = false);

void DrawUITick(int x, int y, int selected, int locked = false);

int UpdateUITick(int x, int y, int selected, int locked = false);

float UpdateSlideBar(int x, int y, int width, float value);

void DrawSlideBar(int x, int y, int width, float value);

void RowText(String A, int X, int Y, int W, int H, int align = 0, float Leading = 1);

int LimitText(String txt, int x, int y, int width, int usingAA = true);

void DrawTooltip(String message);

void DrawPointer();

}
#endif // MENU_H_INCLUDED

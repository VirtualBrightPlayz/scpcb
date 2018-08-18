#ifndef MENU_H_INCLUDED
#define MENU_H_INCLUDED
#include <vector>
#include <StringType.h>

class bbImage;

namespace CBN {

// Constants.
const int GAMESTATE_MAINMENU = 0;
const int GAMESTATE_PLAYING = 1;
const int GAMESTATE_PAUSED = 2;
const int GAMESTATE_CONSOLE = 3;
const int GAMESTATE_INVENTORY = 4;
const int GAMESTATE_SCP294 = 5;
const int GAMESTATE_ENDING = 6;
const int GAMESTATE_LAUNCHER = 7;
const int GAMESUBSTATE_PAUSED_MAIN = 0;
const int GAMESUBSTATE_PAUSED_DEAD = 1;
const int GAMESUBSTATE_PAUSED_OPTIONS = 2;

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
extern int SelectedInputBox;
extern String SavePath;
extern String SaveMSG;
extern String CurrSave;

// Functions.
int IsPaused();

void DrawTiledImageRect(bbImage* img, int srcX, int srcY, int srcwidth, int srcheight, int x, int y, int width, int height);

String rInput(const String& aString);

bool MouseOn(int x, int y, int width, int height);

String UpdateInputBox(int x, int y, int width, int height, const String& txt, int ID = 0);

void DrawInputBox(int x, int y, int width, int height, const String& txt, int ID = 0);

void DrawFrame(int x, int y, int width, int height, int xoffset = 0, int yoffset = 0);

void DrawUIButton(int x, int y, int width, int height, const String& txt, int bigfont = true);

int UpdateUIButton(int x, int y, int width, int height, const String& txt = "", int waitForMouseUp = false);

void DrawUITick(int x, int y, int selected, int locked = false);

int UpdateUITick(int x, int y, int selected, int locked = false);

float UpdateSlideBar(int x, int y, int width, float value);

void DrawSlideBar(int x, int y, int width, float value);

void RowText(const String& A, int X, int Y, int W, int H, int align = 0, float Leading = 1);

void LimitText(const String& txt, int x, int y, int width, bool usingAA = true);

void DrawTooltip(const String& message);

void DrawPointer();

}
#endif // MENU_H_INCLUDED

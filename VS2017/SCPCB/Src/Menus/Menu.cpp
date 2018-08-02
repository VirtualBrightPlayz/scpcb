#include "Menu.h"
#include "include.h"

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
String SelectedEnding;
int CurrGameState = GAMESTATE_MAINMENU;
int CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
int CurrGameStatePage = 0;
float MsgTimer;
String Msg;
String DeathMSG;
int AccessCode;
String KeypadInput;
float KeypadTimer;
String KeypadMSG;
int DrawHandIcon;
float MenuScale;
String RandomSeed;
float MenuBlinkTimer[2];
float MenuBlinkDuration[2];
String MenuStr;
int MenuStrX;
int MenuStrY;
int SelectedInputBox;
String SavePath = "Saves/";
String SaveMSG;
String CurrSave;

// Functions.
int IsPaused() {
    return CurrGameState != GAMESTATE_PLAYING;
}

void DrawTiledImageRect(int img, int srcX, int srcY, int srcwidth, int srcheight, int x, int y, int width, int height) {
    int x2 = x;
    int y2;
    while (x2 < x+width) {
        y2 = y;
        while (y2 < y+height) {
            if (x2 + srcwidth > x + width) {
                srcwidth = srcwidth - Int(Max((x2 + srcwidth) - (x + width), 1));
            }
            if (y2 + srcheight > y + height) {
                srcheight = srcheight - Int(Max((y2 + srcheight) - (y + height), 1));
            }
            bbDrawImageRect(img, x2, y2, srcX, srcY, srcwidth, srcheight);
            y2 = y2 + srcheight;
        }
        x2 = x2 + srcwidth;
    }

}

String rInput(String aString) {
    int value = bbGetKey();
    int length = bbLen(aString);

    if (value == 8) {
        value = 0;
        if (length > 0) {
            aString = bbLeft(aString, length - 1);
        }
    }

    if (value == 13 | value == 0) {
        return aString;
    } else if ((value > 0 & value < 7 | value > 26 & value < 32 | value == 9)) {
        return aString;
    } else {
        aString = aString + bbChr(value);
        return aString;
    }
}

int MouseOn(int x, int y, int width, int height) {
    if (bbMouseX() > x & bbMouseX() < x + width) {
        if (bbMouseY() > y & bbMouseY() < y + height) {
            return true;
        }
    }
    return false;
}

String UpdateInputBox(int x, int y, int width, int height, String Txt, int ID = 0) {
    int MouseOnBox = false;
    if (MouseOn(x, y, width, height)) {
        MouseOnBox = true;
        if (MouseHit1) {
            SelectedInputBox = ID;
            bbFlushKeys();
        }
    }

    if ((!MouseOnBox) & MouseHit1 & SelectedInputBox == ID) {
        SelectedInputBox = 0;
    }

    if (SelectedInputBox == ID) {
        Txt = rInput(Txt);
    }

    return Txt;
}

String DrawInputBox(int x, int y, int width, int height, String Txt, int ID = 0) {
    //TextBox(x,y,width,height,Txt$)
    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);
    //Rect(x, y, width, height)
    bbColor(0, 0, 0);

    int MouseOnBox = false;
    if (MouseOn(x, y, width, height)) {
        bbColor(50, 50, 50);
        MouseOnBox = true;
    }

    bbRect(x + 2, y + 2, width - 4, height - 4);
    bbColor(255, 255, 255);

    if (SelectedInputBox == ID) {
        if ((TimeInPosMilliSecs() % 800) < 400) {
            bbRect(x + width / 2 + bbStringWidth(Txt) / 2 + 2, y + height / 2 - 5, 2, 12);
        }
    }

    bbText(x + width / 2, y + height / 2, Txt, true, true);
}

void DrawFrame(int x, int y, int width, int height, int xoffset = 0, int yoffset = 0) {
    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, xoffset, (y % 256), 512, 512, x, y, width, height);

    DrawTiledImageRect(uiAssets->tileBlack, yoffset, (y % 256), 512, 512, x+Int(3*MenuScale), y+Int(3*MenuScale), width-Int(6*MenuScale), height-Int(6*MenuScale));
}

void DrawUIButton(int x, int y, int width, int height, String txt, int bigfont = true) {

    DrawFrame (x, y, width, height);
    if (MouseOn(x, y, width, height)) {
        bbColor(30, 30, 30);
        bbRect(x + 4, y + 4, width - 8, height - 8);
    } else {
        bbColor(0, 0, 0);
    }

    bbColor(255, 255, 255);
    if (bigfont) {
        bbSetFont(uiAssets->font[1]);
    } else {
        bbSetFont(uiAssets->font[0]);
    }

    bbText(x + width / 2, y + height / 2, txt, true, true);
}

int UpdateUIButton(int x, int y, int width, int height, String txt = "", int waitForMouseUp = false) {
    int clicked = false;

    if (MouseOn(x, y, width, height)) {
        if (MouseHit1 & (!waitForMouseUp)) | (MouseUp1 & waitForMouseUp) {
            clicked = true;
            PlaySound_SM(sndManager->button);
        }
    }

    return clicked;
}

void DrawUITick(int x, int y, int selected, int locked = false) {
    int width = Int(20.0 * MenuScale);
    int height = Int(20 * MenuScale);

    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);
    //Rect(x, y, width, height)

    int Highlight = MouseOn(x, y, width, height) & (!locked);

    if (Highlight) {
        bbColor(50, 50, 50);
    } else {
        bbColor(0, 0, 0);
    }

    bbRect(x + 2, y + 2, width - 4, height - 4);

    if (selected) {
        if (Highlight) {
            bbColor(255,255,255);
        } else {
            bbColor(200,200,200);
        }
        DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x + 4, y + 4, width - 8, height - 8);
        //Rect(x + 4, y + 4, width - 8, height - 8)
    }

    bbColor(255, 255, 255);
}

int UpdateUITick(int x, int y, int selected, int locked = false) {
    int width = Int(20.0 * MenuScale);
    int height = Int(20.0 * MenuScale);

    int Highlight = MouseOn(x, y, width, height) & (!locked);

    if (Highlight) {
        if (MouseHit1) {
            selected = (!selected);
            PlaySound_SM(sndManager->button);
        }
    }

    return selected;
}

float UpdateSlideBar(int x, int y, int width, float value) {
    if (MouseDown1) {
        if (bbMouseX() >= x & bbMouseX() <= x + width + 14 & bbMouseY() >= y & bbMouseY() <= y + 20) {
            value = Min(Max((bbMouseX() - x) * 100 / width, 0), 100);
        }
    }

    return value;
}

void DrawSlideBar(int x, int y, int width, float value) {
    bbColor(255,255,255);
    bbRect(x, y, width + 14, 20,false);

    bbDrawImage(uiAssets->blinkBar, x + Int(width * value / 100.0)+3, y+3);

    bbColor(170,170,170);
    bbText(x - Int(50.0 * MenuScale), y + Int(4.0*MenuScale), "LOW");
    bbText(x + width + Int(38.0 * MenuScale), y+Int(4.0*MenuScale), "HIGH");
}

void RowText(String A, int X, int Y, int W, int H, int align = 0, float Leading = 1) {
    //Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
    //Leading is optional extra vertical spacing in pixels

    if (H<1) {
        H = 2048;
    }

    int LinesShown = 0;
    int Height = bbStringHeight(A) + Int(Leading);
    String b;
    int space;
    String temp;
    String trimmed;
    int extra;

    while (bbLen(A) > 0) {
        space = bbInstr(A, " ");
        if (space == 0) {
            space = bbLen(A);
        }
        temp = bbLeft(A, space);
        //we might ignore a final space
        trimmed = bbTrim(temp);
        //we haven't ignored it yet
        extra = 0;
        //ignore final space If doing so would make a word fit at End of Line:
        if (bbStringWidth(b + temp) > W) & (bbStringWidth(b + trimmed) <= W) {
            temp = trimmed;
            extra = 1;
        }

        //too big, so Print what will fit
        if (bbStringWidth(b + temp) > W) {
            if (align) {
                bbText(X + W / 2 - (bbStringWidth(b) / 2), LinesShown * Height + Y, b);
            } else {
                bbText(X, LinesShown * Height + Y, b);
            }

            LinesShown = LinesShown + 1;
            b = "";
            //append it To b$ (which will eventually be printed) And remove it from A$
        } else {
            b = b + temp;
            A = bbRight(A, bbLen(A) - (bbLen(temp) + extra));
        }

        //the Next Line(would be too tall, so leave)
        if (((LinesShown + 1) * Height) > H) {
            break;
        }
    }

    if (b != "") &((LinesShown + 1) <= H) {
        if (align) {
            //Print(any remaining Text If it'll fit vertically)
            bbText(X + W / 2 - (bbStringWidth(b) / 2), LinesShown * Height + Y, b);
        } else {
            //Print(any remaining Text If it'll fit vertically)
            bbText(X, LinesShown * Height + Y, b);
        }
    }
}

int LimitText(String txt, int x, int y, int width, int usingAA = true) {
    int TextLength;
    int UnFitting;
    int LetterWidth;
    if (usingAA) {
        if (txt == "" | width == 0) {
            return 0;
        }
        TextLength = bbStringWidth(txt);
        UnFitting = TextLength - width;
        //mahtuu
        if (UnFitting <= 0) {
            bbText(x, y, txt);
            //ei mahdu
        } else {
            LetterWidth = TextLength / bbLen(txt);

            bbText(x, y, bbLeft(txt, Int(Max(bbLen(txt) - UnFitting / LetterWidth - 4, 1))) + "...");
        }
    } else {
        if (txt == "" | width == 0) {
            return 0;
        }
        TextLength = bbStringWidth(txt);
        UnFitting = TextLength - width;
        //mahtuu
        if (UnFitting <= 0) {
            bbText(x, y, txt);
            //ei mahdu
        } else {
            LetterWidth = TextLength / bbLen(txt);

            bbText(x, y, bbLeft(txt, Int(Max(bbLen(txt) - UnFitting / LetterWidth - 4, 1))) + "...");
        }
    }
}

void DrawTooltip(String message) {
    float scale = userOptions->screenHeight/768.0;

    int width = (bbStringWidth(message))+Int(20.0*MenuScale);

    bbColor(25,25,25);
    bbRect(bbMouseX()+20,bbMouseY(),width,Int(19.0*scale),true);
    bbColor(150,150,150);
    bbRect(bbMouseX()+20,bbMouseY(),width,Int(19.0*scale),false);
    bbSetFont(uiAssets->font[0]);
    bbText(bbMouseX()+Int(20.0*MenuScale)+(width/2),bbMouseY()+Int(12.0*MenuScale), message, true, true);
}

void DrawPointer() {
    if (CurrGameState == GAMESTATE_PLAYING) {
        bbHidePointer();
        return;
    }

    bbShowPointer();
    if (userOptions->fullscreen) {
        bbDrawImage(uiAssets->cursorIMG, bbMouseX(), bbMouseY());
    }
}

}

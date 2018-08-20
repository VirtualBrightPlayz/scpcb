#include <bbgraphics.h>
#include <bbinput.h>
#include <bbstring.h>

#include "Menu.h"
#include "MainMenu.h"
#include "../MathUtils/MathUtils.h"
#include "../GameMain.h"
#include "../AssetMgmt/Assets.h"
#include "../AssetMgmt/Audio.h"
#include "../Config/Options.h"

namespace CBN {

// Globals.
String SelectedEnding;
int CurrGameState = GAMESTATE_MAINMENU;
int prevGameState = CurrGameState;
int CurrGameSubstate = 0;
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
String SavePath = "Saves/";
String SaveMSG;
String CurrSave;

// Functions.
int IsPaused() {
    return CurrGameState != GAMESTATE_PLAYING;
}

void DrawTiledImageRect(bbImage* img, int srcX, int srcY, int srcwidth, int srcheight, int x, int y, int width, int height) {
    int x2 = x;
    while (x2 < x+width) {
        int y2 = y;
        while (y2 < y+height) {
            if (x2 + srcwidth > x + width) {
                srcwidth = srcwidth - (int)(Max((float)((x2 + srcwidth) - (x + width)), 1.f));
            }
            if (y2 + srcheight > y + height) {
                srcheight = srcheight - (int)(Max((float)((y2 + srcheight) - (y + height)), 1.f));
            }
            bbDrawImageRect(img, x2, y2, srcX, srcY, srcwidth, srcheight);
            y2 = y2 + srcheight;
        }
        x2 = x2 + srcwidth;
    }

}

String rInput(const String& aString) {
    int value = bbGetKey();
    int length = aString.size();

    String retVal = aString;
    if (value == 8) {
        value = 0;
        if (length > 0) {
            retVal = retVal.substr(0, length - 1);
        }
    }

    if (value == 13 || value == 0) {
        return retVal;
    } else if (value > 0 && value < 7 || value > 26 && value < 32 || value == 9) {
        return retVal;
    } else {
        retVal = retVal + (char)value;
        return retVal;
    }
}

bool MouseOn(int x, int y, int width, int height) {
    if (bbMouseX() > x && bbMouseX() < x + width) {
        if (bbMouseY() > y && bbMouseY() < y + height) {
            return true;
        }
    }
    return false;
}

void DrawFrame(int x, int y, int width, int height, int xoffset, int yoffset) {
    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, xoffset, (y % 256), 512, 512, x, y, width, height);

    DrawTiledImageRect(uiAssets->tileBlack, yoffset, (y % 256), 512, 512, x+(int)(3*MenuScale), y+(int)(3*MenuScale), width-(int)(6*MenuScale), height-(int)(6*MenuScale));
}

float UpdateSlideBar(int x, int y, int width, float value) {
    if (MouseDown1) {
        if (bbMouseX() >= x && bbMouseX() <= x + width + 14 && bbMouseY() >= y && bbMouseY() <= y + 20) {
            value = Min(Max((bbMouseX() - x) * 100.f / width, 0.f), 100.f);
        }
    }

    return value;
}

void DrawSlideBar(int x, int y, int width, float value) {
    bbColor(255,255,255);
    bbRect(x, y, width + 14, 20,false);

    bbDrawImage(uiAssets->blinkBar, x + (int)(width * value / 100.f)+3, y+3);

    bbColor(170,170,170);
    bbText(x - (int)(50.f * MenuScale), y + (int)(4.f*MenuScale), "LOW");
    bbText(x + width + (int)(38.f * MenuScale), y+(int)(4.f*MenuScale), "HIGH");
}

void RowText(const String& A, int X, int Y, int W, int H, int align, float Leading) {
    //Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
    //Leading is optional extra vertical spacing in pixels

    if (H<1) {
        H = 2048;
    }

    int LinesShown = 0;
    int Height = bbStringHeight(A) + (int)(Leading);
    String b;
    int space;
    String temp;
    String trimmed;
    int extra;

    String txt = A;
    while (txt.size() > 0) {
        space = bbInstr(txt, " ");
        if (space == 0) {
            space = txt.size();
        }
        temp = txt.substr(0, space);
        //we might ignore a final space
        trimmed = temp.trim();
        //we haven't ignored it yet
        extra = 0;
        //ignore final space If doing so would make a word fit at End of Line:
        if (bbStringWidth(b + temp) > W && bbStringWidth(b + trimmed) <= W) {
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
            txt = bbRight(txt, txt.size() - (temp.size() + extra));
        }

        //the Next Line(would be too tall, so leave)
        if (((LinesShown + 1) * Height) > H) {
            break;
        }
    }

    if (!b.isEmpty() && (LinesShown + 1) <= H) {
        if (align) {
            //Print(any remaining Text If it'll fit vertically)
            bbText(X + W / 2 - (bbStringWidth(b) / 2), LinesShown * Height + Y, b);
        } else {
            //Print(any remaining Text If it'll fit vertically)
            bbText(X, LinesShown * Height + Y, b);
        }
    }
}

void LimitText(const String& txt, int x, int y, int width, bool usingAA) {
    int TextLength;
    int UnFitting;
    int LetterWidth;
    if (usingAA) {
        if (txt.isEmpty() || width == 0) {
            return;
        }
        TextLength = bbStringWidth(txt);
        UnFitting = TextLength - width;
        //mahtuu
        if (UnFitting <= 0) {
            bbText(x, y, txt);
            //ei mahdu
        } else {
            LetterWidth = TextLength / txt.size();

            bbText(x, y, txt.substr(0, (int)(Max((float)(txt.size() - UnFitting / LetterWidth - 4), 1.f))) + "...");
        }
    } else {
        if (txt.isEmpty() || width == 0) {
            return;
        }
        TextLength = bbStringWidth(txt);
        UnFitting = TextLength - width;
        //mahtuu
        if (UnFitting <= 0) {
            bbText(x, y, txt);
            //ei mahdu
        } else {
            LetterWidth = TextLength / txt.size();

            bbText(x, y, txt.substr(0, (int)(Max((float)(txt.size() - UnFitting / LetterWidth - 4), 1.f))) + "...");
        }
    }
}

void DrawTooltip(const String& message) {
    float scale = userOptions->screenHeight/768.f;

    int width = (bbStringWidth(message))+(int)(20.f*MenuScale);

    bbColor(25,25,25);
    bbRect(bbMouseX()+20,bbMouseY(),width,(int)(19.f*scale),true);
    bbColor(150,150,150);
    bbRect(bbMouseX()+20,bbMouseY(),width,(int)(19.f*scale),false);
    bbSetFont(uiAssets->font[0]);
    bbText(bbMouseX()+(int)(20.f*MenuScale)+(width/2),bbMouseY()+(int)(12.f*MenuScale), message, true, true);
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

#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>
#include <bbfilesystem.h>
#include <bbinput.h>
#include <bbmath.h>
#include <bbstring.h>
#include <StringType.h>

#include "LoadingScreen.h"
#include "../GameMain.h"
#include "../INI.h"
#include "../Audio.h"
#include "../Options.h"
#include "../Assets.h"
#include "../Menus/Menu.h"
#include "../FastResize.h"
#include "../MathUtils/MathUtils.h"

namespace CBN {

// Structs.
std::vector<LoadingScreen*> LoadingScreen::list;
LoadingScreen::LoadingScreen() {
    list.push_back(this);
}
LoadingScreen::~LoadingScreen() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int LoadingScreen::getListSize() {
    return list.size();
}
LoadingScreen* LoadingScreen::getObject(int index) {
    return list[index];
}

// Globals.
LoadingScreen* SelectedLoadingScreen;
int LoadingScreenAmount;
int LoadingScreenText;
bbImage* LoadingBack;

// Functions.
void InitLoadingScreens(const String& file) {
    String TemporaryString;
    int i;
    LoadingScreen* ls;

    bbFile* f = bbOpenFile(file);

    while (!bbEof(f)) {
        TemporaryString = bbReadLine(f).trim();
        if (TemporaryString.charAt(0) == '[') {
            TemporaryString = TemporaryString.substr(1, TemporaryString.size() - 2);

            ls = new LoadingScreen();
            LoadingScreenAmount = LoadingScreenAmount+1;
            ls->id = LoadingScreenAmount;

            ls->title = TemporaryString;
            ls->imgpath = GetINIString(file, TemporaryString, "image path");
            std::cout<<ls->imgpath<<"\n";

            ls->img = nullptr;

            for (i = 0; i <= 4; i++) {
                ls->txt[i] = GetINIString(file, TemporaryString, "text"+String(i+1));
                if (!ls->txt[i].isEmpty()) {
                    ls->txtamount = ls->txtamount+1;
                }
            }

            ls->disablebackground = GetINIInt(file, TemporaryString, "disablebackground");

            String alignmentX = GetINIString(file, TemporaryString, "align x").toLower();
            if (alignmentX.equals("left")) {
                ls->alignx = -1;
            } else if (alignmentX.equals("middle") || alignmentX.equals("center")) {
                ls->alignx = 0;
            } else if (alignmentX.equals("right")) {
                ls->alignx = 1;
            }

            String alignmentY = GetINIString(file, TemporaryString, "align y").toLower();
            if (alignmentY.equals("top") || alignmentY.equals("up")) {
                ls->aligny = -1;
            } else if (alignmentY.equals("middle") || alignmentY.equals("center")) {
                ls->aligny = 0;
            } else if (alignmentY.equals("bottom") || alignmentY.equals("down")) {
                ls->aligny = 1;
            }
        }
    }

    bbCloseFile(f);
}

void DrawLoading(int percent, int shortloading) {
    int x;
    int y;
    int i;
    String strtemp;
    int temp;
    LoadingScreen* ls = nullptr;
    int width;
    int height;

    if (percent == 0) {
        LoadingScreenText = 0;

        temp = bbRand(1,LoadingScreenAmount);
        for (int iterator109 = 0; iterator109 < LoadingScreen::getListSize(); iterator109++) {
            ls = LoadingScreen::getObject(iterator109);

            if (ls->id == temp) {
                if (ls->img==nullptr) {
                    ls->img = bbLoadImage("Loadingscreens/"+ls->imgpath);
                }
                SelectedLoadingScreen = ls;
                break;
            }
        }
    }

    int firstloop = true;
    do {
        //Color(0,0,0)
        //Rect(0,0,userOptions\screenWidth,userOptions\screenHeight,True)
        //Color(255, 255, 255)
        bbClsColor(0,0,0);
        bbCls();

        //Cls(True,False)

        if (percent > 24) {
            musicManager->update();
        }

        if (shortloading == false) {
            if (percent > (100.f / SelectedLoadingScreen->txtamount)*(LoadingScreenText+1)) {
                LoadingScreenText = LoadingScreenText+1;
            }
        }
        LoadingScreenText = (int)Min(LoadingScreenText,4);

        if (!SelectedLoadingScreen->disablebackground) {
            bbDrawImage(LoadingBack, userOptions->screenWidth/2 - bbImageWidth(LoadingBack)/2, userOptions->screenHeight/2 - bbImageHeight(LoadingBack)/2);
        }

        if (SelectedLoadingScreen->alignx == 0) {
            x = userOptions->screenWidth/2 - bbImageWidth(SelectedLoadingScreen->img)/2;
        } else if (( SelectedLoadingScreen->alignx == 1)) {
            x = userOptions->screenWidth - bbImageWidth(SelectedLoadingScreen->img);
        } else {
            x = 0;
        }

        if (SelectedLoadingScreen->aligny == 0) {
            y = userOptions->screenHeight/2 - bbImageHeight(SelectedLoadingScreen->img)/2;
        } else if (( SelectedLoadingScreen->aligny == 1)) {
            y = userOptions->screenHeight - bbImageHeight(SelectedLoadingScreen->img);
        } else {
            y = 0;
        }

        bbDrawImage(SelectedLoadingScreen->img, x, y);

        width = 300;
        height = 20;
        x = userOptions->screenWidth / 2 - width / 2;
        y = userOptions->screenHeight / 2 + 30 - 100;

        bbRect(x, y, width+4, height, false);
        for (i = 1; i <= (int)((width - 2) * (percent / 100.f) / 10); i++) {
            bbDrawImage(uiAssets->blinkBar, x + 3 + 10 * (i - 1), y + 3);
        }

        if (SelectedLoadingScreen->title.equals("CWM")) {

            if (!shortloading) {
                if (firstloop) {
                    if (percent == 0) {
                        PlaySound2(LoadTempSound("SFX/SCP/990/cwm1.cwm"));
                    } else if ((percent == 100)) {
                        PlaySound2(LoadTempSound("SFX/SCP/990/cwm2.cwm"));
                    }
                }
            }

            bbSetFont(uiAssets->font[1]);
            temp = bbRand(2,9);
            for (i = 0; i <= temp; i++) {
                strtemp = strtemp + bbChr(bbRand(48,122));
            }
            bbText(userOptions->screenWidth / 2, userOptions->screenHeight / 2 + 80, strtemp, true, true);

            if (percent == 0) {
                if (bbRand(5)==1) {
                    switch (bbRand(2)) {
                        case 1: {
                            SelectedLoadingScreen->txt[0] = "It will happen on " + bbCurrentDate() + ".";
                        } break;
                        case 2: {
                            SelectedLoadingScreen->txt[0] = bbCurrentTime();
                        } break;
                    }
                } else {
                    switch (bbRand(13)) {
                        case 1: {
                            SelectedLoadingScreen->txt[0] = "A very fine radio might prove to be useful.";
                        } break;
                        case 2: {
                            SelectedLoadingScreen->txt[0] = "ThIS PLaCE WiLL BUrN";
                        } break;
                        case 3: {
                            SelectedLoadingScreen->txt[0] = "You cannot control it.";
                        } break;
                        case 4: {
                            SelectedLoadingScreen->txt[0] = "eof9nsd3jue4iwe1fgj";
                        } break;
                        case 5: {
                            SelectedLoadingScreen->txt[0] = "YOU NEED TO TRUST IT";
                        } break;
                        case 6: {
                            SelectedLoadingScreen->txt[0] = "Look my friend in the eye when you address him, isn't that the way of the gentleman?";
                        } break;
                        case 7: {
                            SelectedLoadingScreen->txt[0] = "???____??_???__????n?";
                        } break;
                        case 8:
                        case 9: {
                            SelectedLoadingScreen->txt[0] = "Jorge has been expecting you.";
                        } break;
                        case 10: {
                            SelectedLoadingScreen->txt[0] = "???????????";
                        } break;
                        case 11: {
                            SelectedLoadingScreen->txt[0] = "Make her a member of the midnight crew.";
                        } break;
                        case 12: {
                            SelectedLoadingScreen->txt[0] = "oncluded that coming here was a mistake. We have to turn back.";
                        } break;
                        case 13: {
                            SelectedLoadingScreen->txt[0] = "This alloy contains the essence of my life.";
                        } break;
                    }
                }
            }

            strtemp = SelectedLoadingScreen->txt[0];
            temp = (int)(SelectedLoadingScreen->txt[0].size()-bbRand(5));
            //temp
            for (i = 0; i <= bbRand(10,15); i++) {
                strtemp = SelectedLoadingScreen->txt[0].replace(bbMid(SelectedLoadingScreen->txt[0],bbRand(1,strtemp.size()-1),1),bbChr(bbRand(130,250)));
            }
            bbSetFont(uiAssets->font[0]);
            RowText(strtemp, userOptions->screenWidth / 2-200, userOptions->screenHeight / 2 +120,400,300,true);
        } else {

            bbColor(0,0,0);
            bbSetFont(uiAssets->font[1]);
            bbText(userOptions->screenWidth / 2 + 1, userOptions->screenHeight / 2 + 80 + 1, SelectedLoadingScreen->title, true, true);
            bbSetFont(uiAssets->font[0]);
            RowText(SelectedLoadingScreen->txt[LoadingScreenText], userOptions->screenWidth / 2-200+1, userOptions->screenHeight / 2 +120+1,400,300,true);

            bbColor(255,255,255);
            bbSetFont(uiAssets->font[1]);
            bbText(userOptions->screenWidth / 2, userOptions->screenHeight / 2 +80, SelectedLoadingScreen->title, true, true);
            bbSetFont(uiAssets->font[0]);
            RowText(SelectedLoadingScreen->txt[LoadingScreenText], userOptions->screenWidth / 2-200, userOptions->screenHeight / 2 +120,400,300,true);

        }

        bbColor(0,0,0);
        bbText(userOptions->screenWidth / 2 + 1, userOptions->screenHeight / 2 - 100 + 1, "LOADING - " + String(percent) + " %", true, true);
        bbColor(255,255,255);
        bbText(userOptions->screenWidth / 2, userOptions->screenHeight / 2 - 100, "LOADING - " + String(percent) + " %", true, true);

        if (percent == 100) {
            //If (firstloop And SelectedLoadingScreen\title <> "CWM") Then PlaySound2(HorrorSFX(8) ;TODO: fix)
            timing->prevTime = bbMilliSecs();
            bbText(userOptions->screenWidth / 2, userOptions->screenHeight - 50, "PRESS ANY KEY TO CONTINUE", true, true);
        } else {
            bbFlushKeys();
            bbFlushMouse();
        }

        //not by any means a perfect solution
        //Not even proper gamma correction but it's a nice looking alternative that works in windowed mode
        if (userOptions->screenGamma>1.f) {
            bbCopyRect(0,0,userOptions->screenWidth,userOptions->screenHeight,1024-userOptions->screenWidth/2,1024-userOptions->screenHeight/2,bbBackBuffer(),bbTextureBuffer(fresize_texture));
            bbEntityBlend(fresize_image,1);
            bbClsColor(0,0,0);
            bbCls();
            ScaleRender(-1.f/(float)(userOptions->screenWidth),1.f/(float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth));
            bbEntityFX(fresize_image,1+32);
            bbEntityBlend(fresize_image,3);
            bbEntityAlpha(fresize_image,userOptions->screenGamma-1.f);
            ScaleRender(-1.f/(float)(userOptions->screenWidth),1.f/(float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth));
            //todo: maybe optimize this if it's too slow, alternatively give players the option to disable gamma
        } else if ((userOptions->screenGamma<1.f)) {
            bbCopyRect(0,0,userOptions->screenWidth,userOptions->screenHeight,1024-userOptions->screenWidth/2,1024-userOptions->screenHeight/2,bbBackBuffer(),bbTextureBuffer(fresize_texture));
            bbEntityBlend(fresize_image,1);
            bbClsColor(0,0,0);
            bbCls();
            ScaleRender(-1.f/(float)(userOptions->screenWidth),1.f/(float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth));
            bbEntityFX(fresize_image,1+32);
            bbEntityBlend(fresize_image,2);
            bbEntityAlpha(fresize_image,1.f);
            bbSetBuffer(bbTextureBuffer(fresize_texture2));
            bbClsColor((int)(255.f*userOptions->screenGamma),(int)(255.f*userOptions->screenGamma),(int)(255.f*userOptions->screenGamma));
            bbCls();
            bbSetBuffer(bbBackBuffer());
            ScaleRender(-1.f/(float)(userOptions->screenWidth),1.f/(float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth),2048.f / (float)(userOptions->screenWidth));
            bbSetBuffer(bbTextureBuffer(fresize_texture2));
            bbClsColor(0,0,0);
            bbCls();
            bbSetBuffer(bbBackBuffer());
        }
        bbEntityFX(fresize_image,1);
        bbEntityBlend(fresize_image,1);
        bbEntityAlpha(fresize_image,1.f);

        bbFlip(false);

        firstloop = false;
        if (percent != 100) {
            break;
        }

    } while (bbGetKey()==0 && !bbMouseHit(1));

    if (percent >= 100) {
        musicManager->restoreDefaultMusic();
    }
}

}

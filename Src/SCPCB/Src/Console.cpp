//library includes
#include <bbgraphics.h>
#include <bbinput.h>
#include <StringType.h>

//project includes
#include "Assets.h"
#include "Console.h"
#include "GameMain.h"
#include "Menus/Menu.h"
#include "Options.h"

namespace CBN {

// Structs.
std::vector<ConsoleMsg*> ConsoleMsg::list;
ConsoleMsg::ConsoleMsg() {
    list.push_back(this);
}
ConsoleMsg::~ConsoleMsg() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int ConsoleMsg::getListSize() {
    return list.size();
}
ConsoleMsg* ConsoleMsg::getObject(int index) {
    return list[index];
}

// Globals.
String ConsoleInput;
float ConsoleScroll;
int ConsoleScrollDragging;
int ConsoleMouseMem;
ConsoleMsg* ConsoleReissue = nullptr;
int ConsoleR = 255;
int ConsoleG = 255;
int ConsoleB = 255;
int ConsoleFlush;
int ConsoleFlushSnd = 0;
int ConsoleMusFlush = 0;
int DebugHUD;

// Functions.
void CreateConsoleMsg(String txt, int r = -1, int g = -1, int b = -1, int isCommand = false) {
    ConsoleMsg* c = new ConsoleMsg();
    //TODO: Re-implement.
    //Insert c Before First ConsoleMsg

    c->txt = txt;
    c->isCommand = isCommand;

    c->r = r;
    c->g = g;
    c->b = b;

    if (c->r<0) {
        c->r = ConsoleR;
    }
    if (c->g<0) {
        c->g = ConsoleG;
    }
    if (c->b<0) {
        c->b = ConsoleB;
    }
}

void DrawConsole() {
    if (CurrGameState==GAMESTATE_CONSOLE) {
        bbSetFont(uiAssets->consoleFont);

        int x = 0;
        int y = userOptions->screenHeight-(int)(300.0*MenuScale);
        int width = userOptions->screenWidth;
        int height = (int)(300.0*MenuScale-30.0*MenuScale);

        DrawFrame(x,y,width,height+(int)(30.0*MenuScale));

        int consoleHeight = 0;
        int scrollbarHeight = 0;

        for (int iterator40 = 0; iterator40 < ConsoleMsg::getListSize(); iterator40++) {
            ConsoleMsg* cm = ConsoleMsg::getObject(iterator40);

            consoleHeight = consoleHeight + (int)(15.0*MenuScale);
        }
        scrollbarHeight = (int)(((float)(height)/(float)(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        bbColor(50,50,50);
        int inBar = MouseOn(x+width-(int)(26.0*MenuScale),y,(int)(26.0*MenuScale),height);
        if (inBar) {
            bbColor(70,70,70);
        }
        bbRect(x+width-(int)(26.0*MenuScale),y,(int)(26.0*MenuScale),height,true);


        bbColor(120,120,120);
        int inBox = MouseOn(x+width-(int)(23.0*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.0*MenuScale),scrollbarHeight);
        if (inBox) {
            bbColor(200,200,200);
        }
        if (ConsoleScrollDragging) {
            bbColor(255,255,255);
        }
        bbRect(x+width-(int)(23.0*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.0*MenuScale),scrollbarHeight,true);

        bbColor(255, 255, 255);

        int tempY = y + height - (int)(25.0*MenuScale) - (int)(ConsoleScroll);
        int count = 0;
        for (int i = 0; i < ConsoleMsg::getListSize(); i++) {
			ConsoleMsg* cm = ConsoleMsg::getObject(i);

            count++;
            if (count>1000) {
                delete cm;
            } else {
                if (tempY >= y && tempY < y + height - (int)(20.0*MenuScale)) {
                    if (cm==ConsoleReissue) {
                        bbColor(cm->r/4,cm->g/4,cm->b/4);
                        bbRect(x, tempY -(int)(2.0*MenuScale),width-(int)(30.0*MenuScale),(int)(24.0*MenuScale),true);
                    }
                    bbColor(cm->r,cm->g,cm->b);
                    if (cm->isCommand) {
                        bbText(x + (int)(20.0*MenuScale), tempY, "> "+cm->txt);
                    } else {
                        bbText(x + (int)(20.0*MenuScale), tempY, cm->txt);
                    }
                }
				tempY -= (int)(15.0*MenuScale);
            }

        }

        bbColor(255,255,255);

        DrawInputBox(x, y + height, width, (int)(30.0*MenuScale), ConsoleInput, 2);
    }
}

void UpdateConsole() {
    if (CurrGameState==GAMESTATE_CONSOLE) {
        ConsoleR = 255;
        ConsoleG = 255;
        ConsoleB = 255;

        int x = 0;
        int y = userOptions->screenHeight-(int)(300.0*MenuScale);
        int width = userOptions->screenWidth;
        int height = (int)(300.0*MenuScale-30.0*MenuScale);

        int consoleHeight = 0;
        int scrollbarHeight = 0;
        for (int i = 0; i < ConsoleMsg::getListSize(); i++) {
            ConsoleMsg* cm = ConsoleMsg::getObject(i);

            consoleHeight = consoleHeight + (int)(15.0*MenuScale);
        }
        scrollbarHeight = (int)(((float)(height)/(float)(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        int inBar = MouseOn(x+width-(int)(26.0*MenuScale),y,(int)(26.0*MenuScale),height);

        int inBox = MouseOn(x+width-(int)(23.0*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.0*MenuScale),scrollbarHeight);

        if (!bbMouseDown(1)) {
            ConsoleScrollDragging = false;
        } else if ((ConsoleScrollDragging)) {
            ConsoleScroll = ConsoleScroll+((bbMouseY()-ConsoleMouseMem)*height/scrollbarHeight);
            ConsoleMouseMem = bbMouseY();
        }

        if (!ConsoleScrollDragging) {
            if (MouseHit1) {
                if (inBox) {
                    ConsoleScrollDragging = true;
                    ConsoleMouseMem = bbMouseY();
                } else if ((inBar)) {
                    ConsoleScroll = ConsoleScroll+((bbMouseY()-(y+height))*consoleHeight/height+(height/2));
                    ConsoleScroll = ConsoleScroll/2;
                }
            }
        }

        int mouseScroll = bbMouseZSpeed();
        if (mouseScroll==1) {
            ConsoleScroll = ConsoleScroll - 15*MenuScale;
        } else if ((mouseScroll==-1)) {
            ConsoleScroll = ConsoleScroll + 15*MenuScale;
        }

        if (bbKeyHit(200)) {
            int reissuePos = 0;
            if (ConsoleReissue==nullptr) {
				// FIXME
                ConsoleReissue = First ConsoleMsg;

                while (ConsoleReissue!=nullptr) {
                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos - (int)(15.0*MenuScale);
                    ConsoleReissue = After ConsoleReissue;
                }

            } else {
                // FIXME
                cm = First ConsoleMsg;
                while (cm != nullptr) {
                    if (cm==ConsoleReissue) {
                        break;
                    }
                    reissuePos = reissuePos-(int)(15.0*MenuScale);
                    cm = After cm;
                }
                ConsoleReissue = After ConsoleReissue;
                reissuePos = reissuePos-(int)(15.0*MenuScale);

                while (true) {
                    if (ConsoleReissue==nullptr) {
                        ConsoleReissue = First ConsoleMsg;
                        reissuePos = 0;
                    }

                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos - (int)(15.0*MenuScale);
                    ConsoleReissue = After ConsoleReissue;
                }
            }

            if (ConsoleReissue!=nullptr) {
                ConsoleInput = ConsoleReissue->txt;
                ConsoleScroll = reissuePos+(height/2);
            }
        }

        if (bbKeyHit(208)) {
            reissuePos = -consoleHeight+(int)(15.0*MenuScale);
            if (ConsoleReissue==nullptr) {
                ConsoleReissue = Last ConsoleMsg;

                while (ConsoleReissue!=nullptr) {
                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos + (int)(15.0*MenuScale);
                    ConsoleReissue = Before ConsoleReissue;
                }

            } else {
                cm = Last ConsoleMsg;
                while (cm!=nullptr) {
                    if (cm==ConsoleReissue) {
                        break;
                    }
                    reissuePos = reissuePos+(int)(15.0*MenuScale);
                    cm = Before cm;
                }
                ConsoleReissue = Before ConsoleReissue;
                reissuePos = reissuePos+(int)(15.0*MenuScale);

                while (true) {
                    if (ConsoleReissue==nullptr) {
                        ConsoleReissue = Last ConsoleMsg;
                        reissuePos = -consoleHeight+(int)(15.0*MenuScale);
                    }

                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos + (int)(15.0*MenuScale);
                    ConsoleReissue = Before ConsoleReissue;
                }
            }

            if (ConsoleReissue!=nullptr) {
                ConsoleInput = ConsoleReissue->txt;
                ConsoleScroll = reissuePos+(height/2);
            }
        }

        if (ConsoleScroll<-consoleHeight+height) {
            ConsoleScroll = -consoleHeight+height;
        }
        if (ConsoleScroll>0) {
            ConsoleScroll = 0;
        }

        SelectedInputBox = 2;
        String oldConsoleInput = ConsoleInput;
        ConsoleInput = UpdateInputBox(x, y + height, width, (int)(30.0*MenuScale), ConsoleInput, 2);
        if (!oldConsoleInput.equals(ConsoleInput)) {
            ConsoleReissue = nullptr;
        }
        ConsoleInput = ConsoleInput.substr(0, 100);

        if (bbKeyHit(28) && !ConsoleInput.isEmpty()) {
            ConsoleReissue = nullptr;
            ConsoleScroll = 0;
            CreateConsoleMsg(ConsoleInput,255,255,0,true);

			String input;
			std::vector<String> args;
            if (ConsoleInput.findFirst(" ") > 0) {
				input = ConsoleInput.substr(0, ConsoleInput.findFirst(" ")).toLower();
            } else {
				input = ConsoleInput.toLower();
            }

            // TODO: Execute commands here.

            ConsoleInput = "";
        }
    }

    bbSetFont(uiAssets->font[0]);

}

}

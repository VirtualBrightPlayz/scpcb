//library includes
#include <bbgraphics.h>
#include <bbinput.h>

//project includes
#include "Assets.h"
#include "Console.h"
#include "GameMain.h"
#include "Menus/Menu.h"
#include "ConsoleCommands.h"
#include "Options.h"

namespace CBN {

// Structs.
std::vector<ConsoleMsg*> ConsoleMsg::list;
ConsoleMsg::ConsoleMsg() {
    txt = "";
    isCommand = false;
    r = -1;
    g = -1;
    b = -1;
}
ConsoleMsg::~ConsoleMsg() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}

// Globals.
String ConsoleInput = "";
float ConsoleScroll = 0.f;
int ConsoleScrollDragging = 0;
int ConsoleMouseMem = 0;
int ConsoleReissue = -1;
int ConsoleR = 255;
int ConsoleG = 255;
int ConsoleB = 255;
bool DebugHUD = false;

// Functions.
void ConsoleMsg::create(const String& txt, int r, int g, int b, bool isCommand) {
    ConsoleMsg* c = new ConsoleMsg();
    ConsoleMsg::list.insert(ConsoleMsg::list.begin(), c);

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
        int y = userOptions->screenHeight-(int)(300.f*MenuScale);
        int width = userOptions->screenWidth;
        int height = (int)(300.f*MenuScale-30.f*MenuScale);

        DrawFrame(x,y,width,height+(int)(30.f*MenuScale));

        int consoleHeight = 0;
        int scrollbarHeight = 0;

        for (int i = 0; i < ConsoleMsg::list.size(); i++) {
            ConsoleMsg* cm = ConsoleMsg::list[i];

            consoleHeight = consoleHeight + (int)(15.f*MenuScale);
        }
        scrollbarHeight = (int)(((float)(height)/(float)(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        bbColor(50,50,50);
        int inBar = MouseOn(x+width-(int)(26.f*MenuScale),y,(int)(26.f*MenuScale),height);
        if (inBar) {
            bbColor(70,70,70);
        }
        bbRect(x+width-(int)(26.f*MenuScale),y,(int)(26.f*MenuScale),height,true);


        bbColor(120,120,120);
        int inBox = MouseOn(x+width-(int)(23.f*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.f*MenuScale),scrollbarHeight);
        if (inBox) {
            bbColor(200,200,200);
        }
        if (ConsoleScrollDragging) {
            bbColor(255,255,255);
        }
        bbRect(x+width-(int)(23.f*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.f*MenuScale),scrollbarHeight,true);

        bbColor(255, 255, 255);

        int tempY = y + height - (int)(25.f*MenuScale) - (int)(ConsoleScroll);
        int count = 0;
        for (int i = 0; i < ConsoleMsg::list.size(); i++) {
			ConsoleMsg* cm = ConsoleMsg::list[i];

            count++;
            if (count>1000) {
                delete cm;
            } else {
                if (tempY >= y && tempY < y + height - (int)(20.f*MenuScale)) {
                    if (i==ConsoleReissue) {
                        bbColor(cm->r/4,cm->g/4,cm->b/4);
                        bbRect(x, tempY -(int)(2.f*MenuScale),width-(int)(30.f*MenuScale),(int)(24.f*MenuScale),true);
                    }
                    bbColor(cm->r,cm->g,cm->b);
                    if (cm->isCommand) {
                        bbText(x + (int)(20.f*MenuScale), tempY, "> "+cm->txt);
                    } else {
                        bbText(x + (int)(20.f*MenuScale), tempY, cm->txt);
                    }
                }
				tempY -= (int)(15.f*MenuScale);
            }

        }

        bbColor(255,255,255);

        DrawInputBox(x, y + height, width, (int)(30.f*MenuScale), ConsoleInput, 2);
    }
}

void UpdateConsole() {
    if (CurrGameState==GAMESTATE_CONSOLE) {
        ConsoleR = 255;
        ConsoleG = 255;
        ConsoleB = 255;

        int x = 0;
        int y = userOptions->screenHeight-(int)(300.f*MenuScale);
        int width = userOptions->screenWidth;
        int height = (int)(300.f*MenuScale-30.f*MenuScale);

        int consoleHeight = 0;
        int scrollbarHeight = 0;
        for (int i = 0; i < ConsoleMsg::list.size(); i++) {
            ConsoleMsg* cm = ConsoleMsg::list[i];

            consoleHeight = consoleHeight + (int)(15.f*MenuScale);
        }
        scrollbarHeight = (int)(((float)(height)/(float)(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        bool inBar = MouseOn(x+width-(int)(26.f*MenuScale),y,(int)(26.f*MenuScale),height);

        bool inBox = MouseOn(x+width-(int)(23.f*MenuScale),y+height-scrollbarHeight+(int)(ConsoleScroll*scrollbarHeight/height),(int)(20.f*MenuScale),scrollbarHeight);

        if (!bbMouseDown(1)) {
            ConsoleScrollDragging = false;
        } else if (ConsoleScrollDragging) {
            ConsoleScroll = ConsoleScroll+((bbMouseY()-ConsoleMouseMem)*height/scrollbarHeight);
            ConsoleMouseMem = bbMouseY();
        }

        if (!ConsoleScrollDragging) {
            if (MouseHit1) {
                if (inBox) {
                    ConsoleScrollDragging = true;
                    ConsoleMouseMem = bbMouseY();
                } else if (inBar) {
                    ConsoleScroll = ConsoleScroll+((bbMouseY()-(y+height))*consoleHeight/height+(height/2));
                    ConsoleScroll = ConsoleScroll/2;
                }
            }
        }

        int mouseScroll = bbMouseZSpeed();
        if (mouseScroll==1) {
            ConsoleScroll = ConsoleScroll - 15*MenuScale;
        } else if (mouseScroll==-1) {
            ConsoleScroll = ConsoleScroll + 15*MenuScale;
        }

        if (bbKeyHit(200) && ConsoleMsg::list.size() > 0) {
            int initIndex = ConsoleReissue;
            if (initIndex<0 || initIndex >= ConsoleMsg::list.size()) {
                initIndex = 0;
            }
            int index = ConsoleReissue + 1;

            while (index != ConsoleReissue) {
                index %= ConsoleMsg::list.size();
                if (ConsoleMsg::list[index]->isCommand) {
                    break;
                }
                index++;
            }
            if (ConsoleMsg::list[index]->isCommand) {
                ConsoleReissue = index;
                ConsoleInput = ConsoleMsg::list[index]->txt;
                ConsoleScroll = -ConsoleReissue * (15.0f*MenuScale) + (height / 2);
            }
        }

        if (bbKeyHit(208) && ConsoleMsg::list.size() > 0) {
            int initIndex = ConsoleReissue;
            if (initIndex<0 || initIndex >= ConsoleMsg::list.size()) {
                initIndex = 0;
            }
            int index = ConsoleReissue - 1;

            while (index != initIndex) {
                if (index<0) { index += ConsoleMsg::list.size(); }
                if (ConsoleMsg::list[index]->isCommand) {
                    break;
                }
                index--;
            }
            if (ConsoleMsg::list[index]->isCommand) {
                ConsoleReissue = index;
                ConsoleInput = ConsoleMsg::list[index]->txt;
                ConsoleScroll = -ConsoleReissue * (15.0f*MenuScale) + (height / 2);
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
        ConsoleInput = UpdateInputBox(x, y + height, width, (int)(30.f*MenuScale), ConsoleInput, 2);
        if (!oldConsoleInput.equals(ConsoleInput)) {
            ConsoleReissue = -1;
        }
        ConsoleInput = ConsoleInput.substr(0, 100).toLower();

        if (bbKeyHit(28) && !ConsoleInput.isEmpty()) {
            ConsoleInput = ConsoleInput.trim(); 
            ConsoleReissue = -1;
            ConsoleScroll = 0;
            ConsoleMsg::create(ConsoleInput,255,255,0,true);

			String input;
			std::vector<String> args;
            if (ConsoleInput.findFirst(" ") > 0) {
				input = ConsoleInput.substr(0, ConsoleInput.findFirst(" "));
                args = ConsoleInput.substr(input.size()).split(" ", true);
            } else {
				input = ConsoleInput;
            }

            ConsoleCmd::executeCommand(input, args);
            ConsoleInput = "";
        }
    }

    bbSetFont(uiAssets->font[0]);

}

}

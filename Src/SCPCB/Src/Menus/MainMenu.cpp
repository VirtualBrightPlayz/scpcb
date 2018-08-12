#include <StringType.h>
#include <bbmath.h>
#include <bbinput.h>
#include <bbgraphics.h>

#include "MainMenu.h"
#include "Menu.h"
#include "../INI.h"
#include "../Options.h"
#include "../GameMain.h"
#include "../Audio.h"
#include "../Save.h"
#include "../Difficulty.h"
#include "../MathUtils/MathUtils.h"
#include "../Assets.h"

namespace CBN {

// Functions.
void UpdateMainMenu() {
    int x;
    int y;
    int width;
    int height;
    int n;
    int i;
    String strtemp;

    int mouseHitButton;
    if (CurrGameSubstate == GAMESUBSTATE_MAINMENU_MAIN) {
        for (i = 0; i < MAINMENU_BUTTON_COUNT; i++) {
            mouseHitButton = false;
            x = (int)(159.f * MenuScale);
            y = (int)((286.f + 100.f * i) * MenuScale);

            width = (int)(400 * MenuScale);
            height = (int)(70 * MenuScale);

            mouseHitButton = UpdateUIButton(x, y, width, height);

            switch (i) {
                case MAINMENU_BUTTON_NEWGAME: {
                    RandomSeed = "";
                    if (mouseHitButton) {
                        if (bbRand(15)==1) {
                            switch (bbRand(14)) {
                                case 1: {
                                    RandomSeed = "NIL";
                                } break;
                                case 2: {
                                    RandomSeed = "NO";
                                } break;
                                case 3: {
                                    RandomSeed = "d9341";
                                } break;
                                case 4: {
                                    RandomSeed = "5CP_I73";
                                } break;
                                case 5: {
                                    RandomSeed = "DONTBLINK";
                                } break;
                                case 6: {
                                    RandomSeed = "CRUNCH";
                                } break;
                                case 7: {
                                    RandomSeed = "die";
                                } break;
                                case 8: {
                                    RandomSeed = "HTAED";
                                } break;
                                case 9: {
                                    RandomSeed = "rustledjim";
                                } break;
                                case 10: {
                                    RandomSeed = "larry";
                                } break;
                                case 11: {
                                    RandomSeed = "JORGE";
                                } break;
                                case 12: {
                                    RandomSeed = "dirtymetal";
                                } break;
                                case 13: {
                                    RandomSeed = "whatpumpkin";
                                } break;
                                case 14: {
                                    RandomSeed = "BOYO";
                                } break;
                            }
                        } else {
                            n = bbRand(4,8);
                            for (i = 0; i < n; i++) {
                                if (bbRand(3)==1) {
                                    RandomSeed = RandomSeed + String(bbRand(0,9));
                                } else {
                                    RandomSeed = RandomSeed + (char)bbRand(97,122);
                                }
                            }
                        }

                        //RandomSeed = MilliSecs()
                        CurrGameSubstate = GAMESUBSTATE_MAINMENU_NEWGAME;
                    }
                } break;
                case MAINMENU_BUTTON_LOADGAME: {
                    if (mouseHitButton) {
                        CurrGameSubstate = GAMESUBSTATE_MAINMENU_LOADGAME;
                    }
                } break;
                case MAINMENU_BUTTON_OPTIONS: {
                    if (mouseHitButton) {
                        CurrGameSubstate = GAMESUBSTATE_MAINMENU_OPTIONS;
                    }
                } break;
                case MAINMENU_BUTTON_QUIT: {
                    if (mouseHitButton) {
                        //End(); // TODO: Fix.
                    }
                } break;
            }
        }

    } else {

        x = (int)(159.f * MenuScale);
        y = (int)(286.f * MenuScale);

        width = (int)(400.f * MenuScale);
        height = (int)(70.f * MenuScale);

        if (UpdateUIButton(x + width + (int)(20.f * MenuScale), y, (int)(580.f * MenuScale - width - 20.f * MenuScale), height, "BACK")) {
            switch (CurrGameSubstate) {
                case GAMESUBSTATE_MAINMENU_NEWGAME: {
                    PutINIValue(OptionFile, "general", "intro enabled", String(userOptions->introEnabled));
                    CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
                } break;
                case GAMESUBSTATE_MAINMENU_OPTIONS: {
                    SaveOptionsINI();

                    CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
                } break;
                case GAMESUBSTATE_MAINMENU_CUSTOMMAP: {
                    CurrGameSubstate = GAMESUBSTATE_MAINMENU_NEWGAME;
                    MouseHit1 = false;
                } break;
                default: {
                    CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
                } break;
            }
        }

        switch (CurrGameSubstate) {
            case GAMESUBSTATE_MAINMENU_NEWGAME: {
                //[Block]

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = (int)(y + height + 20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(330.f * MenuScale);

                CurrSave = UpdateInputBox(x + (int)(150.f * MenuScale), y + (int)(15.f * MenuScale), (int)(200.f * MenuScale), (int)(30.f * MenuScale), CurrSave, 1);
                CurrSave = CurrSave.substr(0, 15);

                userOptions->introEnabled = UpdateUITick(x + (int)(280.f * MenuScale), y + (int)(110.f * MenuScale), userOptions->introEnabled);

                for (i = SAFE; i <= CUSTOM; i++) {
                    if (UpdateUITick(x + (int)(20.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), (SelectedDifficulty == difficulties[i]))) {
                        SelectedDifficulty = difficulties[i];
                    }
                }

                if (SelectedDifficulty->customizable) {
                    SelectedDifficulty->permaDeath = UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(165.f * MenuScale), (SelectedDifficulty->permaDeath));

                    if (UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(195.f * MenuScale), SelectedDifficulty->saveType == SAVEANYWHERE & (!SelectedDifficulty->permaDeath), SelectedDifficulty->permaDeath)) {
                        SelectedDifficulty->saveType = SAVEANYWHERE;
                    } else {
                        SelectedDifficulty->saveType = SAVEONSCREENS;
                    }

                    SelectedDifficulty->aggressiveNPCs = UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(225.f * MenuScale), SelectedDifficulty->aggressiveNPCs);

                    //Other factor's difficulty
                    if (MouseHit1) {
                        if (MouseOn(x + (int)(155.f * MenuScale), y+(int)(251.f*MenuScale), bbImageWidth(uiAssets->arrow[1]), bbImageHeight(uiAssets->arrow[1]))) {
                            if (SelectedDifficulty->otherFactors < HARD) {
                                SelectedDifficulty->otherFactors = SelectedDifficulty->otherFactors + 1;
                            } else {
                                SelectedDifficulty->otherFactors = EASY;
                            }
                            PlaySound_SM(sndManager->button);
                        }
                    }
                }

                if (UpdateUIButton(x, y + height + (int)(20.f * MenuScale), (int)(160.f * MenuScale), (int)(70.f * MenuScale), "Load map")) {
                    CurrGameSubstate = GAMESUBSTATE_MAINMENU_CUSTOMMAP;
                    LoadSavedMaps();
                }

                if (UpdateUIButton(x + (int)(420.f * MenuScale), y + height + (int)(20.f * MenuScale), (int)(160.f * MenuScale), (int)(70.f * MenuScale), "START")) {
                    if (!CurrSave.isEmpty()) {
                        CurrSave = "untitled";
                    }

                    if (RandomSeed.isEmpty()) {
                        RandomSeed = String(TimeInPosMilliSecs());
                    }
                    strtemp = "";
                    bbSeedRnd(SeedStringToInt(RandomSeed));

                    //					SameFound = False
                    //					For i = 1 To SaveGameAmount
                    //						If (SaveGames(i - 1) = CurrSave) Then SameFound=SameFound+1
                    //					Next
                    //
                    //					If (SameFound > 0) Then CurrSave = CurrSave + " (" + String(SameFound + 1) + ")"

                    LoadEntities();
                    InitNewGame();
                    CurrGameState = GAMESTATE_PLAYING;
                    bbFlushKeys();
                    bbFlushMouse();

                    PutINIValue(OptionFile, "general", "intro enabled", String(userOptions->introEnabled));
                }

                //[End Block]
                //load game
            } break;
            case GAMESUBSTATE_MAINMENU_LOADGAME: {
                //[Block]

                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(300.f * MenuScale);

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(296.f * MenuScale);

                //[End Block]
                //options
            } break;
            case GAMESUBSTATE_MAINMENU_OPTIONS: {
                //[Block]

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(60.f * MenuScale);
                //If (UpdateUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False)) Then MainMenuTab = 3
                //If (UpdateUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False)) Then MainMenuTab = 5
                //If (UpdateUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False)) Then MainMenuTab = 6
                //If (UpdateUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False)) Then MainMenuTab = 7
                y = y + (int)(70.f * MenuScale);

                //				If MainMenuTab = 3 ;Graphics
                //					;[Block]
                //					height = 300 * MenuScale
                //
                //					y=y+20*MenuScale
                //
                //					userOptions\hudEnabled = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)
                //
                //					y=y+30*MenuScale
                //
                //					UpdateUITick(x + 310 * MenuScale, y + MenuScale, False, True)
                //
                //					y=y+30*MenuScale
                //
                //					userOptions\vsync = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
                //
                //					y=y+40*MenuScale
                //
                //					userOptions\screenGamma = (UpdateSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.f)/50.f)
                //
                //					y = y + 50*MenuScale
                //
                //					y=y+30*MenuScale
                //					;[End Block]
                //				ElseIf MainMenuTab = 5 ;Audio
                //					;[Block]
                //					height = 220 * MenuScale
                //
                //					y = y + 20*MenuScale
                //
                //					userOptions\musicVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.f)/100.f)
                //
                //					y = y + 40*MenuScale
                //
                //					userOptions\soundVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\soundVolume*100.f)/100.f)
                //					;[End Block]
                //				ElseIf MainMenuTab = 6 ;Controls
                //					;[Block]
                //					height = 230 * MenuScale
                //
                //					y = y + 20*MenuScale
                //
                //					userOptions\mouseSensitivity = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.f)/100.f)-0.5
                //
                //					y = y + 40*MenuScale
                //
                //					userOptions\invertMouseY(= UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY))
                //
                //					y = y + 30*MenuScale
                //
                //					y = y + 10*MenuScale
                //
                //					UpdateInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\up,210)],5)
                //
                //					UpdateInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\left,210)],3)
                //
                //					UpdateInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\down,210)],6)
                //
                //					UpdateInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\right,210)],4)
                //
                //					UpdateInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\save,210)],11)
                //
                //
                //					UpdateInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\blink,210)],7)
                //
                //					UpdateInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\sprint,210)],8)
                //
                //					UpdateInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\inv,210)],9)
                //
                //					UpdateInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\crouch,210)],10)
                //
                //					UpdateInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\console,210)],12)
                //
                //					For i = 0 To 227
                //						If (KeyHit(i)) Then key = i
                //break;
                //					Next
                //					If (key<>0) Then
                //						Select (SelectedInputBox)
                //							Case 3
                //								keyBinds\left = key
                //							Case 4
                //								keyBinds\right = key
                //							Case 5
                //								keyBinds\up = key
                //							Case 6
                //								keyBinds\down = key
                //							Case 7
                //								keyBinds\blink = key
                //							Case 8
                //								keyBinds\sprint = key
                //							Case 9
                //								keyBinds\inv = key
                //							Case 10
                //								keyBinds\crouch = key
                //							Case 11
                //								keyBinds\save = key
                //							Case 12
                //								keyBinds\console = key
                //						End Select
                //						SelectedInputBox = 0
                //					EndIf
                //					;[End Block]
                //				ElseIf MainMenuTab = 7 ;Advanced
                //					;[Block]
                //					height = 310 * MenuScale
                //
                //					y = y + 20*MenuScale
                //
                //					y = y + 30*MenuScale
                //
                //					userOptions\consoleOpenOnError = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
                //
                //					y = y + 50*MenuScale
                //
                //					ShowFPS% = UpdateUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
                //
                //					y = y + 30*MenuScale
                //
                //					If (UpdateUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.f)) Then
                //						CurrFrameLimit# = (UpdateSlideBar(x + 150*MenuScale, y+30*MenuScale, 100*MenuScale, CurrFrameLimit#*50.f)/50.f)
                //						CurrFrameLimit = Max(CurrFrameLimit, 0.1)
                //						userOptions\framelimit = CurrFrameLimit#*100.f
                //					Else
                //						CurrFrameLimit# = 0.f
                //						userOptions\framelimit = 0
                //					EndIf
                //
                //					y = y + 80*MenuScale
                //
                //					;[End Block]
                //				EndIf
                //[End Block]
                // load map
            } break;
            case GAMESUBSTATE_MAINMENU_CUSTOMMAP: {
                //[Block]
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(350.f * MenuScale);

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(350.f * MenuScale);

                //[End Block]
            } break;
        }

    }
}

void DrawMainMenu() {
    int x;
    int y;
    int width;
    int height;
    int i;

    bbColor(0,0,0);
    bbRect(0,0,userOptions->screenWidth,userOptions->screenHeight,true);

    bbDrawImage(uiAssets->back, 0, 0);

    if (TimeInPosMilliSecs() % (int)(MenuBlinkTimer[0]) >= bbRand((int)(MenuBlinkDuration[0]))) {
        bbDrawImage(uiAssets->scp173, userOptions->screenWidth - bbImageWidth(uiAssets->scp173), userOptions->screenHeight - bbImageHeight(uiAssets->scp173));
    }

    if (bbRand(300) == 1) {
        MenuBlinkTimer[0] = bbRand(4000, 8000);
        MenuBlinkDuration[0] = bbRand(200, 500);
    }

    bbSetFont(uiAssets->font[0]);

    MenuBlinkTimer[1] = MenuBlinkTimer[1]-timing->tickDuration;
    if (MenuBlinkTimer[1] < MenuBlinkDuration[1]) {
        bbColor(50, 50, 50);
        bbText(MenuStrX + bbRand(-5, 5), MenuStrY + bbRand(-5, 5), MenuStr, true);
        if (MenuBlinkTimer[1] < 0) {
            MenuBlinkTimer[1] = bbRand(700, 800);
            MenuBlinkDuration[1] = bbRand(10, 35);
            MenuStrX = (int)(bbRand(700, 1000) * MenuScale);
            MenuStrY = (int)(bbRand(100, 600) * MenuScale);

            switch (bbRand(0, 22)) {
                case 0:
                case 2:
                case 3: {
                    MenuStr = "DON'T BLINK";
                } break;
                case 4:
                case 5: {
                    MenuStr = "Secure. Contain. Protect.";
                } break;
                case 6:
                case 7:
                case 8: {
                    MenuStr = "You want happy endings? Fuck you.";
                } break;
                case 9:
                case 10:
                case 11: {
                    MenuStr = "Sometimes we would have had time to scream.";
                } break;
                case 12:
                case 19: {
                    MenuStr = "NIL";
                } break;
                case 13: {
                    MenuStr = "NO";
                } break;
                case 14: {
                    MenuStr = "black white black white black white gray";
                } break;
                case 15: {
                    MenuStr = "Stone does not care";
                } break;
                case 16: {
                    MenuStr = "9341";
                } break;
                case 17: {
                    MenuStr = "It controls the doors";
                } break;
                case 18: {
                    MenuStr = "e8m106]af173o+079m895w914";
                } break;
                case 20: {
                    MenuStr = "It has taken over everything";
                } break;
                case 21: {
                    MenuStr = "The spiral is growing";
                } break;
                case 22: {
                    MenuStr = "\"Some kind of gestalt effect due to massive reality damage.\"";
                } break;
            }
        }
    }

    bbSetFont(uiAssets->font[1]);

    bbDrawImage(uiAssets->scpText, userOptions->screenWidth / 2 - bbImageWidth(uiAssets->scpText) / 2, userOptions->screenHeight - (int)(20.f * MenuScale) - bbImageHeight(uiAssets->scpText));

    if (userOptions->screenWidth > 1240 * MenuScale) {
        DrawTiledImageRect(uiAssets->tileWhite, 0, 5, 512, (int)(7.f * MenuScale), (int)(985.f * MenuScale), (int)(407.f * MenuScale), (userOptions->screenWidth - (int)(1240 * MenuScale)) + 300, (int)(7.f * MenuScale));
    }

    String txt;
    if (CurrGameSubstate == GAMESUBSTATE_MAINMENU_MAIN) {
        for (i = 0; i <= MAINMENU_BUTTON_COUNT-1; i++) {
            x = (int)(159.f * MenuScale);
            y = (int)((286.f + 100.f * i) * MenuScale);

            width = (int)(400.f * MenuScale);
            height = (int)(70.f * MenuScale);

            switch (i) {
                case MAINMENU_BUTTON_NEWGAME: {
                    txt = "NEW GAME";
                } break;
                case MAINMENU_BUTTON_LOADGAME: {
                    txt = "LOAD GAME";
                } break;
                case MAINMENU_BUTTON_OPTIONS: {
                    txt = "OPTIONS";
                } break;
                case MAINMENU_BUTTON_QUIT: {
                    txt = "QUIT";
                } break;
            }

            DrawUIButton(x, y, width, height, txt);
        }
    } else {
        x = (int)(159.f * MenuScale);
        y = (int)(286.f * MenuScale);

        width = (int)(400.f * MenuScale);
        height = (int)(70.f * MenuScale);

        DrawFrame(x, y, width, height);

        DrawUIButton(x + width + (int)(20.f * MenuScale), y, (int)(580.f * MenuScale) - width - (int)(20.f * MenuScale), height, "BACK", false);

        switch (CurrGameSubstate) {
            case GAMESUBSTATE_MAINMENU_NEWGAME: {
                //[Block]

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                bbColor(255, 255, 255);
                bbSetFont(uiAssets->font[1]);
                bbText(x + width / 2, y + height / 2, "NEW GAME", true, true);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(330.f * MenuScale);

                DrawFrame(x, y, width, height);

                bbSetFont(uiAssets->font[0]);

                bbText(x + (int)(20.f * MenuScale), y + (int)(20.f * MenuScale), "Name:");
                DrawInputBox(x + (int)(150.f * MenuScale), y + (int)(15.f * MenuScale), (int)(200.f * MenuScale), (int)(30.f * MenuScale), CurrSave, 1);

                bbColor(255,255,255);
                //				If (SelectedMap = "") Then
                bbText(x + (int)(20.f * MenuScale), y + (int)(60.f * MenuScale), "Map seed:");
                DrawInputBox(x+(int)(150.f*MenuScale), y+(int)(55.f*MenuScale), (int)(200.f*MenuScale), (int)(30.f*MenuScale), RandomSeed, 3);
                //				Else
                //					Text(x + (int)(20.f * MenuScale), y + (int)(60.f * MenuScale), "Selected map:")
                //					Color(255, 255, 255)
                //					Rect(x+(int)(150.f*MenuScale), y+(int)(55.f*MenuScale), (int)(200.f*MenuScale), (int)(30.f*MenuScale))
                //					Color(0, 0, 0)
                //					Rect(x+(int)(150.f*MenuScale)+2, y+(int)(55.f*MenuScale)+2, (int)(200.f*MenuScale)-4, (int)(30.f*MenuScale)-4)
                //
                //					Color(255, 0,0)
                //					Text(x+(int)(150.f*MenuScale + 100.f*MenuScale), y+(int)(55.f*MenuScale + 15.f*MenuScale), SelectedMap, True, True)
                //
                //					DrawUIButton(x+(int)(370.f*MenuScale), y+(int)(55.f*MenuScale), (int)(120.f*MenuScale), (int)(30.f*MenuScale), "Deselect", False)
                //				EndIf

                bbText(x + (int)(20.f * MenuScale), y + (int)(110.f * MenuScale), "Enable intro sequence:");
                DrawUITick(x + (int)(280.f * MenuScale), y + (int)(110.f * MenuScale), userOptions->introEnabled);

                //Local modeName$, modeDescription$, selectedDescription$
                bbText(x + (int)(20.f * MenuScale), y + (int)(150.f * MenuScale), "Difficulty:");
                for (i = SAFE; i <= CUSTOM; i++) {
                    DrawUITick(x + (int)(20.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), SelectedDifficulty == difficulties[i]);

                    bbText(x + (int)(60.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), difficulties[i]->name);
                }

                bbColor(255, 255, 255);
                DrawFrame(x + (int)(150.f * MenuScale),y + (int)(155.f * MenuScale), (int)(410.f*MenuScale), (int)(150.f*MenuScale));

                if (SelectedDifficulty->customizable) {
                    DrawUITick(x + (int)(160.f * MenuScale), y + (int)(165.f * MenuScale), (SelectedDifficulty->permaDeath));
                    bbText(x + (int)(200.f * MenuScale), y + (int)(165.f * MenuScale), "Permadeath");

                    DrawUITick(x + (int)(160.f * MenuScale), y + (int)(195.f * MenuScale), SelectedDifficulty->saveType = SAVEANYWHERE & (!SelectedDifficulty->permaDeath), SelectedDifficulty->permaDeath);

                    bbText(x + (int)(200.f * MenuScale), y + (int)(195.f * MenuScale), "Save anywhere");

                    DrawUITick(x + (int)(160.f * MenuScale), y + (int)(225.f * MenuScale), SelectedDifficulty->aggressiveNPCs);
                    bbText(x + (int)(200.f * MenuScale), y + (int)(225.f * MenuScale), "Aggressive NPCs");

                    //Other factor's difficulty
                    bbColor(255,255,255);
                    bbDrawImage(uiAssets->arrow[1],x + (int)(155.f * MenuScale), y+(int)(251.f*MenuScale));

                    bbColor(255,255,255);
                    switch (SelectedDifficulty->otherFactors) {
                        case EASY: {
                            bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Easy");
                        } break;
                        case NORMAL: {
                            bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Normal");
                        } break;
                        case HARD: {
                            bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Hard");
                        } break;
                    }
                } else {
                    RowText(SelectedDifficulty->description, x+(int)(160.f*MenuScale), y+(int)(160.f*MenuScale), (int)((410.f-20.f)*MenuScale), (int)(200.f*MenuScale));
                }

                DrawUIButton(x, y + height + (int)(20.f * MenuScale), (int)(160.f * MenuScale), (int)(70.f * MenuScale), "Load map", false);

                bbSetFont(uiAssets->font[1]);

                DrawUIButton(x + (int)(420.f * MenuScale), y + height + (int)(20.f * MenuScale), (int)(160.f * MenuScale), (int)(70.f * MenuScale), "START", false);
                //[End Block]
                //load game
            } break;
            case GAMESUBSTATE_MAINMENU_LOADGAME: {
                //[Block]

                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(300.f * MenuScale);

                DrawFrame(x, y, width, height);

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                bbColor(255, 255, 255);
                bbSetFont(uiAssets->font[1]);
                bbText(x + width / 2, y + height / 2, "LOAD GAME", true, true);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(296.f * MenuScale);

                bbSetFont(uiAssets->font[0]);

                //				If (SaveGameAmount = 0) Then
                bbText(x + (int)(20.f * MenuScale), y + (int)(20.f * MenuScale), "No saved games.");
                //				Else
                //					x = x + (int)(20.f * MenuScale)
                //					y = y + (int)(20.f * MenuScale)
                //					For i = 1 To SaveGameAmount
                //						DrawFrame(x,y,(int)(540.f*MenuScale), (int)(70.f*MenuScale))
                //
                //						Text(x + (int)(20.f * MenuScale), y + (int)(10.f * MenuScale), SaveGames(i - 1))
                //						Text(x + (int)(20.f * MenuScale), y + (int)((10.f+23.f) * MenuScale), SaveGameTime(i - 1))
                //						Text(x + (int)(120.f * MenuScale), y + (int)((10.f+23.f) * MenuScale), SaveGameDate(i - 1))
                //
                //						If (SaveMSG = "") Then
                //							DrawUIButton(x + (int)(280.f * MenuScale), y + (int)(20.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale), "Load", False)
                //
                //							DrawUIButton(x + (int)(400.f * MenuScale), y + (int)(20.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale), "Delete", False)
                //
                //						Else
                //							DrawFrame(x + (int)(280.f * MenuScale), y + (int)(20.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale))
                //							Color(100, 100, 100)
                //							Text(x + (int)(330.f * MenuScale), y + (int)(35.f * MenuScale), "Load", True, True)
                //
                //							DrawFrame(x + (int)(400.f * MenuScale), y + (int)(20.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale))
                //							Color(100, 100, 100)
                //							Text(x + (int)(450.f * MenuScale), y + (int)(35.f * MenuScale), "Delete", True, True)
                //						EndIf
                //
                //						y = y + (int)(80.f * MenuScale)
                //
                //					Next
                //
                //					If (SaveMSG <> "") Then
                //						x = userOptions\screenWidth / 2
                //						y = userOptions\screenHeight / 2
                //						DrawFrame(x, y, (int)(400.f * MenuScale), (int)(200.f * MenuScale))
                //						Text(x + (int)(20.f * MenuScale), y + (int)(15.f * MenuScale), "Are you sure you want to delete this save?")
                //						DrawUIButton(x + (int)(250.f * MenuScale), y + (int)(150.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale), "Yes", False)
                //						DrawUIButton(x + (int)(50.f * MenuScale), y + (int)(150.f * MenuScale), (int)(100.f * MenuScale), (int)(30.f * MenuScale), "No", False)
                //					EndIf
                //				EndIf

                //[End Block]
                //options
            } break;
            case GAMESUBSTATE_MAINMENU_OPTIONS: {
                //[Block]

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                bbColor(255, 255, 255);
                bbSetFont(uiAssets->font[1]);
                bbText(x + width / 2, y + height / 2, "OPTIONS", true, true);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(60.f * MenuScale);
                DrawFrame(x, y, width, height);
                //				DrawUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False)
                //				DrawUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False)
                //				DrawUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False)
                //				DrawUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False)
                //				Color(0,255,0)
                //				If MainMenuTab = 3
                //					Rect(x+20*MenuScale,y+15*MenuScale,width/5,height/2,False)
                //				ElseIf MainMenuTab = 5
                //					Rect(x+160*MenuScale,y+15*MenuScale,width/5,height/2,False)
                //				ElseIf MainMenuTab = 6
                //					Rect(x+300*MenuScale,y+15*MenuScale,width/5,height/2,False)
                //				ElseIf MainMenuTab = 7
                //					Rect(x+440*MenuScale,y+15*MenuScale,width/5,height/2,False)
                //				EndIf
                bbSetFont(uiAssets->font[0]);
                y = y + (int)(70.f * MenuScale);

                //				If MainMenuTab = 3 ;Graphics
                //					;[Block]
                //					height = 300 * MenuScale
                //					DrawFrame(x, y, width, height)
                //
                //					y=y+20*MenuScale
                //
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Show HUD:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)
                //
                //					y=y+30*MenuScale
                //
                //					Color(100,100,100)
                //					Text(x + 20 * MenuScale, y, "Enable bump mapping:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, False, True)
                //					If MouseOn(x + 310 * MenuScale, y + MenuScale, 20*MenuScale,20*MenuScale)
                //						DrawTooltip("Not available in this version")
                //					EndIf
                //
                //					y=y+30*MenuScale
                //
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "VSync:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
                //
                //					y=y+40*MenuScale
                //
                //					DrawSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.f)
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Screen gamma")
                //
                //					y = y + 50*MenuScale
                //
                //					Color(100,100,100)
                //					Text(x + 20 * MenuScale, y, "Texture quality:")
                //					DrawImage(uiAssets\arrow[1],x + 310 * MenuScale, y-4*MenuScale)
                //
                //					Text(x + 340 * MenuScale, y + MenuScale, "DISABLED")
                //					If MouseOn(x + 310 * MenuScale, y-4*MenuScale, ImageWidth(uiAssets\arrow[1]),ImageHeight(uiAssets\arrow[1]))
                //						DrawTooltip("Not available in this version")
                //					EndIf
                //
                //					y=y+30*MenuScale
                //					;[End Block]
                //				ElseIf MainMenuTab = 5 ;Audio
                //					;[Block]
                //					height = 220 * MenuScale
                //					DrawFrame(x, y, width, height)
                //
                //					y = y + 20*MenuScale
                //
                //					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.f)
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Music volume:")
                //
                //					y = y + 40*MenuScale
                //
                //					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\soundVolume*100.f)
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Sound volume:")
                //					;[End Block]
                //				ElseIf MainMenuTab = 6 ;Controls
                //					;[Block]
                //					height = 230 * MenuScale
                //					DrawFrame(x, y, width, height)
                //
                //					y = y + 20*MenuScale
                //
                //					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.f)
                //					Color(255, 255, 255)
                //					Text(x + 20 * MenuScale, y, "Mouse sensitivity:")
                //
                //					y = y + 40*MenuScale
                //
                //					Color(255, 255, 255)
                //					Text(x + 20 * MenuScale, y, "Invert mouse Y-axis:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY)
                //
                //					y = y + 30*MenuScale
                //					Text(x + 20 * MenuScale, y, "Control configuration:")
                //					y = y + 10*MenuScale
                //
                //					Text(x + 20 * MenuScale, y + 20 * MenuScale, "Move Forward")
                //					DrawInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\up,210)],5)
                //					Text(x + 20 * MenuScale, y + 40 * MenuScale, "Strafe Left")
                //					DrawInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\left,210)],3)
                //					Text(x + 20 * MenuScale, y + 60 * MenuScale, "Move Backward")
                //					DrawInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\down,210)],6)
                //					Text(x + 20 * MenuScale, y + 80 * MenuScale, "Strafe Right")
                //					DrawInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\right,210)],4)
                //					Text(x + 20 * MenuScale, y + 100 * MenuScale, "Quick Save")
                //					DrawInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\save,210)],11)
                //
                //					Text(x + 280 * MenuScale, y + 20 * MenuScale, "Manual Blink")
                //					DrawInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\blink,210)],7)
                //					Text(x + 280 * MenuScale, y + 40 * MenuScale, "Sprint")
                //					DrawInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\sprint,210)],8)
                //					Text(x + 280 * MenuScale, y + 60 * MenuScale, "Open/Close Inventory")
                //					DrawInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\inv,210)],9)
                //					Text(x + 280 * MenuScale, y + 80 * MenuScale, "Crouch")
                //					DrawInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\crouch,210)],10)
                //					Text(x + 280 * MenuScale, y + 100 * MenuScale, "Open/Close Console")
                //					DrawInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\console,210)],12)
                //					;[End Block]
                //				ElseIf MainMenuTab = 7 ;Advanced
                //					;[Block]
                //					height = 310 * MenuScale
                //					DrawFrame(x, y, width, height)
                //
                //					y = y + 20*MenuScale
                //
                //					Color(255,255,255)
                //					y = y + 30*MenuScale
                //
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Open console on error:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
                //
                //					y = y + 50*MenuScale
                //
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Show FPS:")
                //					DrawUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
                //
                //					y = y + 30*MenuScale
                //
                //					Color(255,255,255)
                //					Text(x + 20 * MenuScale, y, "Framelimit:")
                //					Color(255,255,255)
                //					DrawUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.f)
                //
                //					y = y + 80*MenuScale
                //
                //					;[End Block]
                //				EndIf
                //[End Block]
                // load map
            } break;
            case GAMESUBSTATE_MAINMENU_CUSTOMMAP: {
                //[Block]
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(350.f * MenuScale);

                DrawFrame(x, y, width, height);

                x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                bbColor(255, 255, 255);
                bbSetFont(uiAssets->font[1]);
                bbText(x + width / 2, y + height / 2, "LOAD MAP", true, true);
                bbSetFont(uiAssets->font[0]);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(350.f * MenuScale);

                bbSetFont(uiAssets->font[0]);

                //				If (SavedMaps(0)="") Then
                bbText(x + (int)(20.f * MenuScale), y + (int)(20.f * MenuScale), "No saved maps. Use the Map Creator to create new maps.");
                //				Else
                //					x = x + (int)(20.f * MenuScale)
                //					y = y + (int)(20.f * MenuScale)
                //					For i = 0 To MAXSAVEDMAPS-1
                //						If (SavedMaps(i)<>"") Then
                //
                //							DrawUIButton(x + (int)(20.f * MenuScale), y + (int)(20.f * MenuScale), (int)(170.f * MenuScale), (int)(25.f * MenuScale), SavedMaps(i), False)
                //
                //							y=y+(int)(30.f*MenuScale)
                //							If (y > (int)((286.f+230.f) * MenuScale)) Then
                //								y = (int)(286.f*MenuScale + 2.f*MenuScale)
                //								x = x+(int)(175.f*MenuScale)
                //							EndIf
                //						Else
                //							Exit
                //						EndIf
                //					Next
                //				EndIf

                //[End Block]
            } break;
        }
    }

    //DrawTiledImageRect(MenuBack, 985 * MenuScale, 860 * MenuScale, 200 * MenuScale, 20 * MenuScale, 1200 * MenuScale, 866 * MenuScale, 300, 20 * MenuScale)



    bbSetFont(uiAssets->font[0]);
}

}

#include <StringType.h>
#include <bbmath.h>
#include <bbinput.h>
#include <bbgraphics.h>

#include "MainMenu.h"
#include "Menu.h"
#include "../Config/INI.h"
#include "../Config/Options.h"
#include "../GameMain.h"
#include "../AssetMgmt/Audio.h"
#include "../Save/Save.h"
#include "../Config/Difficulty.h"
#include "../MathUtils/MathUtils.h"
#include "../AssetMgmt/Assets.h"
#include "GUI/GUIButton.h"
#include "GUI/GUITick.h"

namespace CBN {

MainMenu* mainMenu = nullptr;

MainMenu::MainMenu() {
    int x = 159;
    int y = 286;
    int width = 400;
    int height = 70;

    btnNewGame = GUIButton(x, y, width, height, "NEW GAME", true);
    y += 100;
    btnLoadGame = GUIButton(x, y, width, height, "LOAD GAME", true);
    y += 100;
    btnOptions = GUIButton(x, y, width, height, "OPTIONS", true);
    y += 100;
    btnQuit = GUIButton(x, y, width, height, "QUIT", true);

    // Max magic numbers. Since the back button is always in the same spot,
    // this places it at the bottom right of any subscreen.
    y = 286;
    btnBack = GUIButton(x + width + 20, y, 580 - width - 20, height, "BACK");

    x = 160;
    y = y + height + 20;
    width = 580;
    height = 330;
    btnStartGame = GUIButton(x + 420, y + height + 20, 160, 70, "START");
    btnLoadMap = GUIButton(x, y + height + 20, 160, 70, "Load map");
    tckIntro = GUITick(x + 280, y + 110, "Enable intro enabled", -260, userOptions->introEnabled);
    txtName = GUIInput(x + 150, y + 15, 200, 30, "Name:", -130, 15);
    txtSeed = GUIInput(x + 150, y + 55, 200, 30, "Map seed:", -130, 15);

    currState = MainMenuState::Main;
    blinkTimer[0] = 1;
    blinkDuration[0] = 1;
    flashStr = "";
    flashStrX = 0;
    flashStrY = 0;
}

void MainMenu::update() {
    if (currState == MainMenuState::Main) {
        btnNewGame.update();
        btnLoadGame.update();
        btnOptions.update();
        btnQuit.update();

        if (btnNewGame.isMouseHit()) {
            RandomSeed = "";
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
                int n = bbRand(4,8);
                for (int i = 0; i < n; i++) {
                    if (bbRand(3)==1) {
                        RandomSeed = RandomSeed + String(bbRand(0,9));
                    } else {
                        RandomSeed = RandomSeed + (char)bbRand(97,122);
                    }
                }
            }
            txtSeed.setInput(RandomSeed);
            currState = MainMenuState::NewGame;
        } else if (btnLoadGame.isMouseHit()) {
            currState = MainMenuState::LoadGame;
        } else if (btnOptions.isMouseHit()) {
            currState = MainMenuState::Options;
        } else if (btnQuit.isMouseHit()) {
            //BrokenItem(); // TODO: Fix.
        }
    } else {
        btnBack.update();
        if (btnBack.isMouseHit()) {
            switch (currState) {
                case MainMenuState::NewGame: {
                    PutINIValue(OptionFile, "general", "intro enabled", String(userOptions->introEnabled));
                    txtName.selected = false;
                    txtSeed.selected = false;
                    currState = MainMenuState::Main;
                } break;

                case MainMenuState::CustomMap: {
                    currState = MainMenuState::NewGame;
                } break;

                case MainMenuState::Options:
                    SaveOptionsINI();
                default: {
                    currState = MainMenuState::Main;
                } break;
            }
        }

        switch (currState) {
            case MainMenuState::NewGame: {
                btnStartGame.update();
                btnLoadMap.update();
                tckIntro.update();
                txtName.update();
                txtSeed.update();

                userOptions->introEnabled = tckIntro.ticked;

                // for (i = SAFE; i <= CUSTOM; i++) {
                //     if (UpdateUITick(x + (int)(20.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), (SelectedDifficulty == difficulties[i]))) {
                //         SelectedDifficulty = difficulties[i];
                //     }
                // }

                // if (SelectedDifficulty->customizable) {
                //     SelectedDifficulty->permaDeath = UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(165.f * MenuScale), (SelectedDifficulty->permaDeath));

                //     if (UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(195.f * MenuScale), SelectedDifficulty->saveType == SAVEANYWHERE && (!SelectedDifficulty->permaDeath), SelectedDifficulty->permaDeath)) {
                //         SelectedDifficulty->saveType = SAVEANYWHERE;
                //     } else {
                //         SelectedDifficulty->saveType = SAVEONSCREENS;
                //     }

                //     SelectedDifficulty->aggressiveNPCs = UpdateUITick(x + (int)(160.f * MenuScale), y + (int)(225.f * MenuScale), SelectedDifficulty->aggressiveNPCs);

                //     //Other factor's difficulty
                //     if (MouseHit1) {
                //         if (MouseOn(x + (int)(155.f * MenuScale), y+(int)(251.f*MenuScale), bbImageWidth(uiAssets->arrow[1]), bbImageHeight(uiAssets->arrow[1]))) {
                //             if (SelectedDifficulty->otherFactors < HARD) {
                //                 SelectedDifficulty->otherFactors = SelectedDifficulty->otherFactors + 1;
                //             } else {
                //                 SelectedDifficulty->otherFactors = EASY;
                //             }
                //             PlaySound_SM(sndMgmt->button);
                //         }
                //     }
                // }

                if (btnLoadMap.isMouseHit()) {
                    currState = MainMenuState::CustomMap;
                    LoadSavedMaps();
                }

                if (btnStartGame.isMouseHit()) {
                    CurrSave = txtName.getInput();
                    RandomSeed = txtSeed.getInput();
                    if (CurrSave.isEmpty()) {
                        CurrSave = "untitled";
                    }

                    if (RandomSeed.isEmpty()) {
                        RandomSeed = String(TimeInPosMilliSecs());
                    }

                    bbSeedRnd(SeedStringToInt(RandomSeed));
                    // TODO: Checks for conflicting file names.
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

                    delete this;
                    return;
                }


                //load game
            } break;

            case MainMenuState::LoadGame: {
                int y = (int)(20.f * MenuScale);
                int width = (int)(580.f * MenuScale);
                int height = (int)(300.f * MenuScale);

                int x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(296.f * MenuScale);


                //options
            } break;

            case MainMenuState::Options: {
                int x = (int)(159.f * MenuScale);
                int y = (int)(286.f * MenuScale);

                int width = (int)(400.f * MenuScale);
                int height = (int)(70.f * MenuScale);

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
                //					userOptions\mouseSensitivity = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5f)*100.f)/100.f)-0.5f
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
                //					UpdateInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBindsight,210)],4)
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
                //								keyBindsight = key
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
                //						CurrFrameLimit = Max(CurrFrameLimit, 0.1f)
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

                // load map
            } break;

            case MainMenuState::CustomMap: {
                int y = (int)(20.f * MenuScale);
                int width = (int)(580.f * MenuScale);
                int height = (int)(350.f * MenuScale);

                int x = (int)(159.f * MenuScale);
                y = (int)(286.f * MenuScale);

                width = (int)(400.f * MenuScale);
                height = (int)(70.f * MenuScale);

                x = (int)(160.f * MenuScale);
                y = y + height + (int)(20.f * MenuScale);
                width = (int)(580.f * MenuScale);
                height = (int)(350.f * MenuScale);


            } break;
        }
    }
}

void MainMenu::draw() {
    int x;
    int y;
    int width;
    int height;

    bbColor(0,0,0);
    bbRect(0,0,userOptions->screenWidth,userOptions->screenHeight,true);

    bbDrawImage(uiAssets->back, 0, 0);

    if (TimeInPosMilliSecs() % (int)(blinkTimer[0]) >= bbRand((int)(blinkDuration[0]))) {
        bbDrawImage(uiAssets->scp173, userOptions->screenWidth - bbImageWidth(uiAssets->scp173), userOptions->screenHeight - bbImageHeight(uiAssets->scp173));
    }

    if (bbRand(300) == 1) {
        blinkTimer[0] = (float)bbRand(4000, 8000);
        blinkDuration[0] = (float)bbRand(200, 500);
    }

    bbSetFont(uiAssets->font[0]);

    blinkTimer[1] -= timing->tickDuration;
    if (blinkTimer[1] < blinkDuration[1]) {
        bbColor(50, 50, 50);
        bbText(flashStrX + bbRand(-5, 5), flashStrY + bbRand(-5, 5), flashStr, true);
        if (blinkTimer[1] < 0) {
            blinkTimer[1] = bbRnd(700, 800);
            blinkDuration[1] = bbRnd(10, 35);
            flashStrX = (int)(bbRand(700, 1000) * MenuScale);
            flashStrY = (int)(bbRand(100, 600) * MenuScale);

            switch (bbRand(0, 22)) {
                case 0:
                case 2:
                case 3: {
                    flashStr = "DON'T BLINK";
                } break;
                case 4:
                case 5: {
                    flashStr = "Secure. Contain. Protect.";
                } break;
                case 6:
                case 7:
                case 8: {
                    flashStr = "You want happy endings? Fuck you.";
                } break;
                case 9:
                case 10:
                case 11: {
                    flashStr = "Sometimes we would have had time to scream.";
                } break;
                case 12:
                case 19: {
                    flashStr = "NIL";
                } break;
                case 13: {
                    flashStr = "NO";
                } break;
                case 14: {
                    flashStr = "black white black white black white gray";
                } break;
                case 15: {
                    flashStr = "Stone does not care";
                } break;
                case 16: {
                    flashStr = "9341";
                } break;
                case 17: {
                    flashStr = "It controls the doors";
                } break;
                case 18: {
                    flashStr = "e8m106]af173o+079m895w914";
                } break;
                case 20: {
                    flashStr = "It has taken over everything";
                } break;
                case 21: {
                    flashStr = "The spiral is growing";
                } break;
                case 22: {
                    flashStr = "\"Some kind of gestalt effect due to massive reality damage.\"";
                } break;
            }
        }
    }

    bbSetFont(uiAssets->font[1]);

    bbDrawImage(uiAssets->scpText, userOptions->screenWidth / 2 - bbImageWidth(uiAssets->scpText) / 2, userOptions->screenHeight - (int)(20.f * MenuScale) - bbImageHeight(uiAssets->scpText));

    if (userOptions->screenWidth > 1240 * MenuScale) {
        DrawTiledImageRect(uiAssets->tileWhite, 0, 5, 512, (int)(7.f * MenuScale), (int)(985.f * MenuScale), (int)(407.f * MenuScale), (userOptions->screenWidth - (int)(1240 * MenuScale)) + 300, (int)(7.f * MenuScale));
    }

    if (currState == MainMenuState::Main) {
        btnNewGame.draw();
        btnLoadGame.draw();
        btnOptions.draw();
        btnQuit.draw();
    } else {
        x = (int)(159.f * MenuScale);
        y = (int)(286.f * MenuScale);

        width = (int)(400.f * MenuScale);
        height = (int)(70.f * MenuScale);

        DrawFrame(x, y, width, height);

        btnBack.draw();

        switch (currState) {
            case MainMenuState::NewGame: {
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

                txtName.draw();
                txtSeed.draw();
                //				If (SelectedMap = "") Then
                
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

                tckIntro.draw();

                //Local modeName$, modeDescription$, selectedDescription$
                // bbText(x + (int)(20.f * MenuScale), y + (int)(150.f * MenuScale), "Difficulty:");
                // for (i = SAFE; i <= CUSTOM; i++) {
                //     DrawUITick(x + (int)(20.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), SelectedDifficulty == difficulties[i]);

                //     bbText(x + (int)(60.f * MenuScale), y + (int)((180.f+30.f*i) * MenuScale), difficulties[i]->name);
                // }

                // bbColor(255, 255, 255);
                // DrawFrame(x + (int)(150.f * MenuScale),y + (int)(155.f * MenuScale), (int)(410.f*MenuScale), (int)(150.f*MenuScale));

                // if (SelectedDifficulty->customizable) {
                //     DrawUITick(x + (int)(160.f * MenuScale), y + (int)(165.f * MenuScale), (SelectedDifficulty->permaDeath));
                //     bbText(x + (int)(200.f * MenuScale), y + (int)(165.f * MenuScale), "Permadeath");

                //     DrawUITick(x + (int)(160.f * MenuScale), y + (int)(195.f * MenuScale), SelectedDifficulty->saveType = SAVEANYWHERE & (!SelectedDifficulty->permaDeath), SelectedDifficulty->permaDeath);

                //     bbText(x + (int)(200.f * MenuScale), y + (int)(195.f * MenuScale), "Save anywhere");

                //     DrawUITick(x + (int)(160.f * MenuScale), y + (int)(225.f * MenuScale), SelectedDifficulty->aggressiveNPCs);
                //     bbText(x + (int)(200.f * MenuScale), y + (int)(225.f * MenuScale), "Aggressive NPCs");

                //     //Other factor's difficulty
                //     bbColor(255,255,255);
                //     bbDrawImage(uiAssets->arrow[1],x + (int)(155.f * MenuScale), y+(int)(251.f*MenuScale));

                //     bbColor(255,255,255);
                //     switch (SelectedDifficulty->otherFactors) {
                //         case EASY: {
                //             bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Easy");
                //         } break;
                //         case NORMAL: {
                //             bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Normal");
                //         } break;
                //         case HARD: {
                //             bbText(x + (int)(200.f * MenuScale), y + (int)(255.f * MenuScale), "Other difficulty factors: Hard");
                //         } break;
                //     }
                // } else {
                //     RowText(SelectedDifficulty->description, x+(int)(160.f*MenuScale), y+(int)(160.f*MenuScale), (int)((410.f-20.f)*MenuScale), (int)(200.f*MenuScale));
                // }

                btnLoadMap.draw();

                bbSetFont(uiAssets->font[1]);

                btnStartGame.draw();

                //load game
            } break;
            case MainMenuState::LoadGame: {
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


                //options
            } break;
            case MainMenuState::Options: {
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
                //					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5f)*100.f)
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
                //					DrawInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBindsight,210)],4)
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

                // load map
            } break;
            case MainMenuState::CustomMap: {
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


            } break;
        }
    }

    //DrawTiledImageRect(MenuBack, 985 * MenuScale, 860 * MenuScale, 200 * MenuScale, 20 * MenuScale, 1200 * MenuScale, 866 * MenuScale, 300, 20 * MenuScale)

    bbSetFont(uiAssets->font[0]);
}

}

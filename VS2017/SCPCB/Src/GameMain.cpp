#include "GameMain.h"
#include "include.h"

#include <bbgraphics.h>
#include <bbblitz3d.h>
#include <StringType.h>

namespace CBN {

// Structs.
std::vector<Timing*> Timing::list;
Timing::Timing() {
    list.push_back(this);
}
Timing::~Timing() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Timing::getListSize() {
    return list.size();
}
Timing* Timing::getObject(int index) {
    return list[index];
}

// Constants.
const String VERSION = "1.CBN";
const int HIT_MAP = 1;
const int HIT_PLAYER = 2;
const int HIT_ITEM = 3;
const int HIT_APACHE = 4;
const int HIT_DEAD = 5;

// Globals.
int WireframeState;
int HalloweenTex;
Timing* timing;
float CurrFrameLimit;
int GameSaved;
int CanSave;
int MouseHit1;
int MouseDown1;
int MouseHit2;
int DoubleClick;
int LastMouseHit1;
int MouseUp1;
float CoffinDistance;
float ExplosionTimer;
int LightsOn = true;
int SoundTransmission;
int Brightness;
int SoundEmitter;
int TempSounds[10];
int TempSoundIndex = 0;
int RadioSquelch;
int RadioStatic;
int RadioBuzz;
int PlayerDetected;
float PrevInjuries;
float PrevBloodloss;
int NoTarget;
int AmbientLightRoomTex;
int AmbientLightRoomVal;
float NTF_1499PrevX;
float NTF_1499PrevY;
float NTF_1499PrevZ;
Room* NTF_1499PrevRoom;
float NTF_1499X;
float NTF_1499Y;
float NTF_1499Z;
int NTF_1499Sky;
int InFacility;
int IsZombie;
int room2gw_brokendoor;
float room2gw_x;
float room2gw_z;
String Input294;
int TeslaTexture;
int Monitor;
int MonitorTexture;
int CamBaseOBJ;
int CamOBJ;

// Functions.
int VerifyResolution() {
    int selectedMode = 1;

    int i;
    for (i = 1; i <= bbCountGfxModes3D(); i++) {
        if (bbGfxModeDepth(i) == 32) {
            if ((userOptions->screenWidth == bbGfxModeWidth(i)) & (userOptions->screenHeight == bbGfxModeHeight(i))) {
                selectedMode = i;
                break;
            }
        }
    }

    userOptions->screenWidth = bbGfxModeWidth(selectedMode);
    userOptions->screenHeight = bbGfxModeHeight(selectedMode);

    return selectedMode-1;
}

void SetTickrate(int tickrate) {
    timing->tickDuration = 70.0/(float)(tickrate);
}

void AddToTimingAccumulator(int milliseconds) {
    if (milliseconds<1 | milliseconds>500) {
        //DebugLog(milliseconds)
        return;
    }
    timing->accumulator = timing->accumulator+Max(0,(float)(milliseconds)*70.0/1000.0);
}

void ResetTimingAccumulator() {
    timing->accumulator = 0.0;
}

int EntryPoint() {
    keyBinds = new KeyBinds();
    userOptions = new Options();
    LoadOptionsINI();

    timing = new Timing();
    SetTickrate(60);

    sndManager = CreateSoundManager();

    if (userOptions->launcher) {
        CurrGameState = GAMESTATE_LAUNCHER;
        launcher = CreateLauncher();
    } else {
        InitializeMainGame();
    }

    Repeat;
    UpdateGame();
    Forever;
}

void InitializeMainGame() {
    CurrGameState = GAMESTATE_MAINMENU;
    CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;

    Graphics3DExt(userOptions->screenWidth, userOptions->screenHeight, 0, (1 + (!userOptions->fullscreen)));
    bbAppTitle("SCP - Containment Breach v"+VERSION);

    MenuScale = (userOptions->screenHeight / 1024.0);

    CurrFrameLimit = userOptions->framelimit;

    bbSetBuffer(bbBackBuffer());

    bbSeedRnd(bbMilliSecs());

    CanSave = true;

    LoadingBack = bbLoadImage("Loadingscreens/loadingback.jpg");
    InitLoadingScreens("Loadingscreens/loadingscreens.ini");

    InitializeUIAssets();

    musicManager = CreateMusicManager();
    SetNextMusicTrack(MUS_EZ, false);

    bbSetFont(uiAssets->font[1]);

    DrawLoading(0, true);

    Brightness = 50;

    DrawLoading(10, true);

    //TODO: doesn't need to be hardcoded
    //0 = light containment, 1 = heavy containment, 2 = entrance
    //AmbientSFXAmount(0)=8
    AmbientSFXAmount(1) = 11;
    AmbientSFXAmount(2) = 12;
    //3 = general, 4 = pre-breach
    //AmbientSFXAmount(3)=15
    AmbientSFXAmount(4) = 5;
    //5 = forest
    //AmbientSFXAmount(5)=10

    DrawLoading(20, true);

    int i;
    RadioSquelch = bbLoadSound("SFX/Radio/squelch.ogg");
    RadioStatic = bbLoadSound("SFX/Radio/static.ogg");
    RadioBuzz = bbLoadSound("SFX/Radio/buzz.ogg");

    DrawLoading(30, true);

    LoadDecals();
    LoadParticles();

    //TODO: Die.
    InFacility = true;

    //TODO: Remove.
    IsZombie = false;

    //TODO: This is fucking stupid.
    room2gw_brokendoor = false;
    room2gw_x = 0.0;
    room2gw_z = 0.0;

    DrawLoading(40,true);

    //TODO: remove/replace with functions
    SecondaryLightOn = true;
    RemoteDoorOn = true;
    Contained106 = false;

    DrawLoading(80,true);

    viewport_center_x = userOptions->screenWidth / 2;
    viewport_center_y = userOptions->screenHeight / 2;

    mouse_left_limit = 250;
    mouse_right_limit = userOptions->screenWidth - 250;
    mouse_top_limit = 150;
    mouse_bottom_limit = userOptions->screenHeight - 150;

    bbCollisions(HIT_PLAYER, HIT_MAP, 2, 2);
    bbCollisions(HIT_PLAYER, HIT_PLAYER, 1, 3);
    bbCollisions(HIT_ITEM, HIT_MAP, 2, 2);
    bbCollisions(HIT_APACHE, HIT_APACHE, 1, 2);
    bbCollisions(HIT_DEAD, HIT_MAP, 2, 2);

    DrawLoading(90, true);

    bbFlushKeys();
    bbFlushMouse();

    MenuBlinkTimer[0] = 1;
    MenuBlinkDuration[0] = 1;

    DrawLoading(100, true);
}

void UpdateGame() {
    int elapsedMilliseconds;
    timing->currTime = bbMilliSecs();
    elapsedMilliseconds = timing->currTime-timing->prevTime;
    AddToTimingAccumulator(elapsedMilliseconds);
    timing->prevTime = timing->currTime;

    //TODO: remove or replace
    //If (userOptions\framelimit > 0) Then
    //    ;Framelimit
    //	Local WaitingTime% = (1000.0 / userOptions\framelimit) - (MilliSecs() - LoopDelay)
    //	Delay(WaitingTime)
    //
    //   LoopDelay= MilliSecs()
    //EndIf

    //Counting the fps
    float instantFramerate = 1000.0/Max(1,elapsedMilliseconds);
    timing->fps = Max(0,timing->fps*0.99 + instantFramerate*0.01);

    int prevmousedown1;
    String rn;
    float darkA;
    int temp;

    //[Block]
    while (timing->accumulator>0.0) {
        timing->accumulator = timing->accumulator-timing->tickDuration;
        if (timing->accumulator<=0.0) {
            bbCaptureWorld();
        }

        bbCls();

        DoubleClick = false;
        MouseHit1 = bbMouseHit(1);
        if (MouseHit1) {
            if (TimeInPosMilliSecs() - LastMouseHit1 < 400) {
                DoubleClick = true;
            }
            LastMouseHit1 = TimeInPosMilliSecs();
        }

        prevmousedown1 = MouseDown1;
        MouseDown1 = bbMouseDown(1);
        if (prevmousedown1 == true & MouseDown1==false) {
            MouseUp1 = true;
        } else {
            MouseUp1 = false;
        }

        MouseHit2 = bbMouseHit(2);
        //TODO: A better way?
        if (CurrGameState != GAMESTATE_LAUNCHER) {
            UpdateMusic();
        }

        if (CurrGameState==GAMESTATE_LAUNCHER) {
            UpdateLauncher();
        } else if ((CurrGameState==GAMESTATE_MAINMENU)) {
            UpdateMainMenu();
        } else {
            if (!MouseDown1) & (!MouseHit1) {
                mainPlayer->grabbedEntity = 0;
            }

            DrawHandIcon = false;

            if (timing->tickDuration > 0) {
                UpdateSecurityCams();
            }

            if (bbKeyHit(keyBinds->inv)) {
                ToggleInventory(mainPlayer);
            }

            if (!IsPaused()) {
                //LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
                //CameraFogRange(mainPlayer\cam, mainPlayer\camFogNear*LightVolume,mainPlayer\camFogFar*LightVolume)
                //CameraFogColor(mainPlayer\cam, 0,0,0)
                //CameraFogMode(mainPlayer\cam,1)
                //CameraRange(mainPlayer\cam, 0.05, Min(mainPlayer\camFogFar*LightVolume*1.5,28))

                bbAmbientLight(Brightness, Brightness, Brightness);
                mainPlayer->loudness = CurveValue(0.0, mainPlayer->loudness, 5.0);

                CanSave = true;
                UpdateEmitters();
                MouseLook();
                UpdatePlayer();
                InFacility = CheckForPlayerInFacility();
                UpdateDoors();
                UpdateLevers();
                UpdateAssets();
                UpdateEvents();
                UpdateDecals();
                UpdateMTF();
                UpdateNPCs();
                UpdateItems();
                UpdateParticles();
                UpdateScreens();
                //TimeCheckpointMonitors()
                UpdateLeave1499();
            }

            //If (InfiniteStamina) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)

            bbUpdateWorld();
            ManipulateNPCBones();

            mainPlayer->blurTimer = Min(CurveValue(0.0, mainPlayer->blurTimer, 20.0),1000.0);
            //If (mainPlayer\blurTimer > 0.0) Then
            //	mainPlayer\blurTimer = Max(Min(0.95, mainPlayer\blurTimer / 1000.0), mainPlayer\blurTimer)
            //	mainPlayer\blurTimer = Max(mainPlayer\blurTimer - timing\tickDuration, 0.0)
            //EndIf

            //[Block]

            darkA = 0.0;
            if (!IsPaused()) {
                if (mainPlayer->sanity895 < 0) {
                    mainPlayer->sanity895 = Min(mainPlayer->sanity895 + timing->tickDuration, 0.0);
                    if (mainPlayer->sanity895 < (-200)) {
                        darkA = Max(Min((-mainPlayer->sanity895 - 200) / 700.0, 0.6), darkA);
                        if (!mainPlayer->dead) {
                            //HeartBeatVolume = Min(Abs(mainPlayer\sanity895+200)/500.0,1.0)
                            mainPlayer->heartbeatIntensity = Max(70 + Abs(mainPlayer->sanity895+200)/6.0,mainPlayer->heartbeatIntensity);
                        }
                    }
                }

                //TODO: fix
                //			If (EyeStuck > 0) Then
                //				mainPlayer\blinkTimer = mainPlayer\blinkFreq
                //				EyeStuck = Max(EyeStuck-timing\tickDuration,0)
                //
                //				If (EyeStuck < 9000) Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (9000-EyeStuck)*0.5)
                //				If (EyeStuck < 6000) Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.0),1.0)
                //				If (EyeStuck < 9000 And EyeStuck+timing\tickDuration =>9000) Then
                //					Msg = "The eyedrops are causing your eyes to tear up."
                //					MsgTimer = 70*6
                //				EndIf
                //			EndIf

                if (mainPlayer->blinkTimer < 0) {
                    if (mainPlayer->blinkTimer > - 5) {
                        darkA = Max(darkA, bbSin(Abs(mainPlayer->blinkTimer * 18.0)));
                    } else if ((mainPlayer->blinkTimer > - 15)) {
                        darkA = 1.0;
                    } else {
                        darkA = Max(darkA, Abs(bbSin(mainPlayer->blinkTimer * 18.0)));
                    }

                    if (mainPlayer->blinkTimer <= - 20) {
                        //Randomizes the frequency of blinking. Scales with difficulty.
                        switch (SelectedDifficulty->otherFactors) {
                            case EASY: {
                                mainPlayer->blinkFreq = bbRnd(490,700);
                            }
                            case NORMAL: {
                                mainPlayer->blinkFreq = bbRnd(455,665);
                            }
                            case HARD: {
                                mainPlayer->blinkFreq = bbRnd(420,630);
                            }
                        }
                        mainPlayer->blinkTimer = mainPlayer->blinkFreq;
                    }

                    mainPlayer->blinkTimer = mainPlayer->blinkTimer - timing->tickDuration;
                } else {
                    mainPlayer->blinkTimer = mainPlayer->blinkTimer - timing->tickDuration * 0.6 * mainPlayer->blinkEffect;
                    //TODO: fix
                    //If (EyeIrritation > 0) Then mainPlayer\blinkTimer=BlinkTimer-Min(EyeIrritation / 100.0 + 1.0, 4.0) * timing\tickDuration

                    darkA = Max(darkA, 0.0);
                }

                //TODO: fix
                //EyeIrritation = Max(0, EyeIrritation - timing\tickDuration)

                if (mainPlayer->blinkEffectTimer > 0) {
                    mainPlayer->blinkEffect = mainPlayer->blinkEffect - (timing->tickDuration/70);
                } else {
                    mainPlayer->blinkEffect = 1.0;
                }

                //TODO: reimplement
                //LightBlink = Max(LightBlink - (timing\tickDuration / 35.0), 0)
                //If (LightBlink > 0) Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3, 0.8)), 1.0)

                if (CurrGameState==GAMESTATE_SCP294) {
                    darkA = 1.0;
                }

                if (!IsPlayerWearingItem(mainPlayer,"nvgoggles")) {
                    darkA = Max((1.0-SecondaryLightOn)*0.9, darkA);
                }

                if (mainPlayer->dead) {
                    CurrGameState = GAMESTATE_PLAYING;
                    CurrGameSubstate = GAMESUBSTATE_PAUSED_DEAD;
                    mainPlayer->selectedItem = nullptr;
                    SelectedScreen = nullptr;
                    SelectedMonitor = nullptr;
                    //mainPlayer\blurTimer = Abs(mainPlayer\fallTimer*5)
                    //mainPlayer\fallTimer=mainPlayer\fallTimer-(timing\tickDuration*0.8)
                    if (mainPlayer->fallTimer < - 360) {
                        CurrGameState = GAMESTATE_PAUSED;
                        //TODO: fix
                        //If (SelectedEnding <> "") Then EndingTimer = Min(mainPlayer\fallTimer,-0.1)
                    }
                    darkA = Max(darkA, Min(Abs(mainPlayer->fallTimer / 400.0), 1.0));
                }

                if (mainPlayer->fallTimer < 0) {
                    CurrGameState = GAMESTATE_PLAYING;
                    mainPlayer->selectedItem = nullptr;
                    SelectedScreen = nullptr;
                    SelectedMonitor = nullptr;
                    mainPlayer->blurTimer = Abs(mainPlayer->fallTimer*10);
                    mainPlayer->fallTimer = mainPlayer->fallTimer-timing->tickDuration;
                    darkA = Max(darkA, Min(Abs(mainPlayer->fallTimer / 400.0), 1.0));
                }

                if (mainPlayer->selectedItem != nullptr) {
                    if (mainPlayer->selectedItem->template->name == "navigator" | mainPlayer->selectedItem->template->name == "nav") {
                        darkA = Max(darkA, 0.5);
                    }
                }
                if (SelectedScreen != nullptr) {
                    darkA = Max(darkA, 0.5);
                }

                bbEntityAlpha(mainPlayer->overlays[OVERLAY_BLACK], darkA);
            }

            if (mainPlayer->lightFlash > 0) {
                bbShowEntity(mainPlayer->overlays[OVERLAY_WHITE]);
                bbEntityAlpha(mainPlayer->overlays[OVERLAY_WHITE], Max(Min(mainPlayer->lightFlash + bbRnd(-0.2, 0.2), 1.0), 0.0));
                mainPlayer->lightFlash = Max(mainPlayer->lightFlash - (timing->tickDuration / 70.0), 0);
            } else {
                bbHideEntity(mainPlayer->overlays[OVERLAY_WHITE]);
                //EntityAlpha(Light, mainPlayer\lightFlash)
            }

            bbEntityColor(mainPlayer->overlays[OVERLAY_WHITE],255,255,255);

            //[End block]

            if (bbKeyHit(keyBinds->save)) {
                if (SelectedDifficulty->saveType == SAVEANYWHERE) {
                    rn = mainPlayer->currRoom->roomTemplate->name;
                    if (rn == "173" | rn == "exit1" | rn == "gatea") {
                        Msg = "You cannot save in this location.";
                        MsgTimer = 70 * 4;
                    } else if ((!CanSave)) {
                        Msg = "You cannot save at this moment.";
                        MsgTimer = 70 * 4;
                    } else {
                        SaveGame(SavePath + CurrSave + "/");
                    }
                } else if ((SelectedDifficulty->saveType == SAVEONSCREENS)) {
                    if (SelectedScreen==nullptr & SelectedMonitor==nullptr) {
                        Msg = "Saving is only permitted on clickable monitors scattered throughout the facility.";
                        MsgTimer = 70 * 4;
                    } else {
                        rn = mainPlayer->currRoom->roomTemplate->name;
                        if (rn == "173" | rn == "exit1" | rn == "gatea") {
                            Msg = "You cannot save in this location.";
                            MsgTimer = 70 * 4;
                        } else if ((!CanSave)) {
                            Msg = "You cannot save at this moment.";
                            MsgTimer = 70 * 4;
                        } else {
                            SaveGame(SavePath + CurrSave + "/");
                        }
                    }
                } else {
                    Msg = "Quick saving is disabled.";
                    MsgTimer = 70 * 4;
                }
            } else if ((SelectedDifficulty->saveType == SAVEONSCREENS & (SelectedScreen!=nullptr | SelectedMonitor!=nullptr))) {
                if ((Msg!="Game progress saved." & Msg!="You cannot save in this location."& Msg!="You cannot save at this moment.") | MsgTimer<=0) {
                    Msg = "Press "+KeyName[keyBinds->save]+" to save.";
                    MsgTimer = 70*4;
                }

                if (MouseHit2) {
                    SelectedMonitor = nullptr;
                }
            }

            if (bbKeyHit(keyBinds->console)) {
                if (CurrGameState==GAMESTATE_CONSOLE) {
                    ResumeSounds();
                    bbMouseXSpeed();
                    bbMouseYSpeed();
                    bbMouseZSpeed();
                    mouse_x_speed_1 = 0.0;
                    mouse_y_speed_1 = 0.0;
                    CurrGameState = GAMESTATE_PLAYING;
                } else {
                    PauseSounds();
                    CurrGameState = GAMESTATE_CONSOLE;
                }
                bbFlushKeys();
            }

            UpdateGUI();

            //TODO: fix
            //If (EndingTimer < 0) Then
            //	If (SelectedEnding <> "") Then DrawEnding()
            //Else
            UpdatePauseMenu();
            //EndIf

            UpdateConsole();

            if (MsgTimer > 0) {
                //TODO: change this variable's name because it's dumb as hell
                temp = false;
                if (CurrGameState!=GAMESTATE_INVENTORY) {
                    if (mainPlayer->selectedItem != nullptr) {
                        if (mainPlayer->selectedItem->template->name == "paper" | mainPlayer->selectedItem->template->name == "oldpaper") {
                            temp = true;
                        }
                    }
                }

                if (!temp) {
                    bbColor(0,0,0);
                    //, Min(MsgTimer / 2, 255)/255.0)
                    bbText((userOptions->screenWidth / 2)+1, (userOptions->screenHeight / 2) + 201, Msg, true, false);
                    //Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255))
                    bbColor(255,255,255);
                    //, Min(MsgTimer / 2, 255)/255.0)
                    bbText((userOptions->screenWidth / 2), (userOptions->screenHeight / 2) + 200, Msg, true, false);
                } else {
                    bbColor(0,0,0);
                    //, Min(MsgTimer / 2, 255)/255.0)
                    bbText((userOptions->screenWidth / 2)+1, Int((userOptions->screenHeight * 0.94) + 1), Msg, true, false);
                    //Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255))
                    bbColor(255,255,255);
                    //, Min(MsgTimer / 2, 255)/255.0)
                    bbText((userOptions->screenWidth / 2), Int((userOptions->screenHeight * 0.94)), Msg, true, false);
                }
                MsgTimer = MsgTimer-timing->tickDuration;
            }
        }
    }

    if (CurrGameState==GAMESTATE_LAUNCHER) {
        if (launcher!=nullptr) {
            DrawLauncher();
        }
    } else if ((CurrGameState==GAMESTATE_MAINMENU)) {
        DrawMainMenu();
    } else {
        RenderWorld2();

        UpdateBlur(bbSqr(mainPlayer->blurTimer/1400.0));

        DrawGUI();
        DrawPauseMenu();
        DrawConsole();

        bbColor(255, 255, 255);
        if (userOptions->showFPS) {
            bbSetFont(uiAssets->consoleFont);
            bbText(20, 20, "FPS: " + Str(Int(timing->fps)));
            bbSetFont(uiAssets->font[0]);
        }
    }
    DrawPointer();

    //not by any means a perfect solution
    //Not even proper gamma correction but it's a nice looking alternative that works in windowed mode
    if (userOptions->screenGamma>1.0) {
        bbCopyRect(0,0,userOptions->screenWidth,userOptions->screenHeight,1024-userOptions->screenWidth/2,1024-userOptions->screenHeight/2,bbBackBuffer(),bbTextureBuffer(fresize_texture));
        bbEntityBlend(fresize_image,1);
        bbClsColor(0,0,0);
        bbCls();
        ScaleRender(-1.0/Float(userOptions->screenWidth),1.0/Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth));
        bbEntityFX(fresize_image,1+32);
        bbEntityBlend(fresize_image,3);
        bbEntityAlpha(fresize_image,userOptions->screenGamma-1.0);
        ScaleRender(-1.0/Float(userOptions->screenWidth),1.0/Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth));
        //todo: maybe optimize this if it's too slow, alternatively give players the option to disable gamma
    } else if ((userOptions->screenGamma<1.0)) {
        bbCopyRect(0,0,userOptions->screenWidth,userOptions->screenHeight,1024-userOptions->screenWidth/2,1024-userOptions->screenHeight/2,bbBackBuffer(),bbTextureBuffer(fresize_texture));
        bbEntityBlend(fresize_image,1);
        bbClsColor(0,0,0);
        bbCls();
        ScaleRender(-1.0/Float(userOptions->screenWidth),1.0/Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth));
        bbEntityFX(fresize_image,1+32);
        bbEntityBlend(fresize_image,2);
        bbEntityAlpha(fresize_image,1.0);
        bbSetBuffer(bbTextureBuffer(fresize_texture2));
        bbClsColor(Int(255*userOptions->screenGamma), Int(255*userOptions->screenGamma), Int(255*userOptions->screenGamma));
        bbCls();
        bbSetBuffer(bbBackBuffer());
        ScaleRender(-1.0/Float(userOptions->screenWidth),1.0/Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth),2048.0 / Float(userOptions->screenWidth));
        bbSetBuffer(bbTextureBuffer(fresize_texture2));
        bbClsColor(0,0,0);
        bbCls();
        bbSetBuffer(bbBackBuffer());
    }
    bbEntityFX(fresize_image,1);
    bbEntityBlend(fresize_image,1);
    bbEntityAlpha(fresize_image,1.0);

    bbFlip(userOptions->vsync!=0);
    //[End block]
}

void UpdateGUI() {
    int temp;
    int x;
    int y;
    int z;
    int i;
    float yawvalue;
    float pitchvalue;
    float x2;
    float y2;
    float z2;
    int n;
    int xtemp;
    int ytemp;
    String strtemp;
    int buttonObj;
    int pvt;
    float projY;
    float scale;

    if (mainPlayer->closestButton != 0 & mainPlayer->selectedDoor == nullptr & CurrGameState==GAMESTATE_PLAYING) {
        if (MouseUp1) {
            MouseUp1 = false;
            if (mainPlayer->closestDoor != nullptr) {
                if (mainPlayer->closestDoor->code != "") {
                    mainPlayer->selectedDoor = mainPlayer->closestDoor;
                } else if ((!mainPlayer->disableControls)) {
                    PlayRangedSound_SM(sndManager->button, mainPlayer->cam, mainPlayer->closestButton);
                    UseDoor(mainPlayer->closestDoor,true);
                }
            }
        }
    }

    if (CurrGameState == GAMESTATE_SCP294) {
        Update294();
    }

    if (SelectedScreen != nullptr) {
        if (MouseUp1 | MouseHit2) {
            bbFreeImage(SelectedScreen->img);
            SelectedScreen->img = 0;
            SelectedScreen = nullptr;
            MouseUp1 = false;
        }
    }
    //TODO: cleanup
    int shouldDrawHUD = true;
    if (mainPlayer->selectedDoor != nullptr) {
        mainPlayer->selectedItem = nullptr;

        if (shouldDrawHUD) {
            buttonObj = GrabMesh("GFX/Map/Meshes/Button.b3d");
            pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true),bbEntityZ(mainPlayer->closestButton,true));
            bbRotateEntity(pvt, 0, bbEntityYaw(mainPlayer->closestButton,true)-180,0);
            bbMoveEntity(pvt, 0,0,0.22);
            bbPositionEntity(mainPlayer->head, bbEntityX(pvt),bbEntityY(pvt),bbEntityZ(pvt));
            bbPointEntity(mainPlayer->head, mainPlayer->closestButton);
            bbFreeEntity(pvt);

            bbCameraProject(mainPlayer->cam, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true)+bbMeshHeight(buttonObj)*0.015,bbEntityZ(mainPlayer->closestButton,true));
            projY = bbProjectedY();
            bbCameraProject(mainPlayer->cam, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true)-bbMeshHeight(buttonObj)*0.015,bbEntityZ(mainPlayer->closestButton,true));
            scale = (bbProjectedY()-projY)/462.0;

            x = Int(userOptions->screenWidth/2-bbImageWidth(uiAssets->keypadHUD)*scale/2);
            y = Int(userOptions->screenHeight/2-bbImageHeight(uiAssets->keypadHUD)*scale/2);

            DropAsset(buttonObj);

            bbSetFont(uiAssets->font[2]);
            if (KeypadMSG != "") {
                KeypadTimer = KeypadTimer-timing->tickDuration;

                if ((KeypadTimer % 70) < 35) {
                    bbText(userOptions->screenWidth/2, Int(y+124*scale), KeypadMSG, true,true);
                }
                if (KeypadTimer ==<0) {
                    KeypadMSG = "";
                    mainPlayer->selectedDoor = nullptr;
                    bbMouseXSpeed();
                    bbMouseYSpeed();
                    bbMouseZSpeed();
                    mouse_x_speed_1 = 0.0;
                    mouse_y_speed_1 = 0.0;
                }
            }

            x = Int(x+44*scale);
            y = Int(y+249*scale);

            for (n = 0; n <= 3; n++) {
                for (i = 0; i <= 2; i++) {
                    xtemp = x + Int(58.5*scale*n);
                    ytemp = y + Int((67*scale)*i);

                    temp = false;
                    if (MouseOn(xtemp, ytemp, Int(54*scale), Int(65*scale)) & KeypadMSG == "") {
                        if (MouseUp1) {
                            PlaySound_SM(sndManager->button);

                            switch ((n+1)+(i*4)) {
                                case 1,2,3: {
                                    KeypadInput = KeypadInput + Str((n+1)+(i*4));
                                }
                                case 4: {
                                    KeypadInput = KeypadInput + "0";
                                }
                                case 5,6,7: {
                                    KeypadInput = KeypadInput + Str((n+1)+(i*4)-1);
                                    //enter
                                }
                                case 8: {
                                    if (KeypadInput == mainPlayer->selectedDoor->code) {
                                        PlaySound_SM(sndManager->scannerUse);

                                        mainPlayer->selectedDoor->locked = 0;
                                        UseDoor(mainPlayer->selectedDoor,true);
                                        mainPlayer->selectedDoor = nullptr;
                                        bbMouseXSpeed();
                                        bbMouseYSpeed();
                                        bbMouseZSpeed();
                                        mouse_x_speed_1 = 0.0;
                                        mouse_y_speed_1 = 0.0;
                                    } else {
                                        PlaySound_SM(sndManager->scannerErr);
                                        KeypadMSG = "ACCESS DENIED";
                                        KeypadTimer = 210;
                                        KeypadInput = "";
                                    }
                                }
                                case 9,10,11: {
                                    KeypadInput = KeypadInput + Str((n+1)+(i*4)-2);
                                }
                                case 12: {
                                    KeypadInput = "";
                                }
                            }

                            if (bbLen(KeypadInput)> 4) {
                                KeypadInput = bbLeft(KeypadInput,4);
                            }
                        }

                    } else {
                        temp = false;
                    }

                }
            }

            if (MouseHit2) {
                mainPlayer->selectedDoor = nullptr;
                bbMouseXSpeed();
                bbMouseYSpeed();
                bbMouseZSpeed();
                mouse_x_speed_1 = 0.0;
                mouse_y_speed_1 = 0.0;
            }
        } else {
            mainPlayer->selectedDoor = nullptr;
        }
    } else {
        KeypadInput = "";
        KeypadTimer = 0;
        KeypadMSG = "";
    }

    //TODO: fix ;And EndingTimer = 0 Then
    if (bbKeyHit(1)) {
        if (IsPaused()) {
            if (CurrGameState==GAMESTATE_INVENTORY) {
                ToggleInventory(mainPlayer);
            } else {
                ResumeSounds();
                bbMouseXSpeed();
                bbMouseYSpeed();
                bbMouseZSpeed();
                mouse_x_speed_1 = 0.0;
                mouse_y_speed_1 = 0.0;
                CurrGameState = GAMESTATE_PLAYING;
            }
        } else {
            PauseSounds();
            CurrGameState = GAMESTATE_PAUSED;
        }

        //OptionsMenu = 0
        //QuitMSG = 0

        mainPlayer->selectedDoor = nullptr;
        SelectedScreen = nullptr;
        SelectedMonitor = nullptr;
    }

    UpdateInventory(mainPlayer);
}

void DrawGUI() {
    int temp;
    int x;
    int y;
    int z;
    int i;
    float yawvalue;
    float pitchvalue;
    float x2;
    float y2;
    float z2;
    int n;
    int xtemp;
    int ytemp;
    String strtemp;
    int width;
    int height;

    Event* e;
    Event* ev;
    Item* it;
    NPC* npc;
    int offset;
    int buttonObj;
    int pvt;
    float projY;
    float scale;

    //TODO: Re-implement.
    //	If (mainPlayer\currRoom\roomTemplate\name = "pocketdimension") Then
    //		For e = Each Event
    //			If (e\room = mainPlayer\currRoom And e\eventState > 600) Then
    //				If (mainPlayer\blinkTimer < -3 And mainPlayer\blinkTimer > -11) Then
    //					If (e\img = 0) Then
    //						If (mainPlayer\blinkTimer > -5 And Rand(30)=1) Then
    //							If (e\img = 0) Then e\img = LoadImage("GFX/npcs/106face.jpg")
    //						EndIf
    //					Else
    //						DrawImage(e\img, userOptions\screenWidth/2-Rand(390,310), userOptions\screenHeight/2-Rand(290,310))
    //					EndIf
    //				Else
    //					If (e\img <> 0) Then FreeImage(e\img
    e->img = 0);
    //				EndIf
    //
    //				Exit
    //			EndIf
    //		Next
    //	EndIf

    if (mainPlayer->closestButton != 0 & mainPlayer->selectedDoor == nullptr & CurrGameState==GAMESTATE_PLAYING) {
        yawvalue = WrapAngle(-bbDeltaYaw(mainPlayer->cam,mainPlayer->closestButton));
        if (yawvalue > 90 & yawvalue <= 180) {
            yawvalue = 90;
        }
        if (yawvalue > 180 & yawvalue < 270) {
            yawvalue = 270;
        }
        pitchvalue = WrapAngle(-bbDeltaPitch(mainPlayer->cam,mainPlayer->closestButton));
        if (pitchvalue > 90 & pitchvalue <= 180) {
            pitchvalue = 90;
        }
        if (pitchvalue > 180 & pitchvalue < 270) {
            pitchvalue = 270;
        }

        bbDrawImage(uiAssets->handIcon[HAND_ICON_TOUCH], Int(userOptions->screenWidth / 2 + bbSin(yawvalue) * (userOptions->screenWidth / 3) - 32), Int(userOptions->screenHeight / 2 - bbSin(pitchvalue) * (userOptions->screenHeight / 3) - 32));
    }

    if (mainPlayer->closestItem != nullptr) {
        yawvalue = -bbDeltaYaw(mainPlayer->cam, mainPlayer->closestItem->collider);
        if (yawvalue > 90 & yawvalue <= 180) {
            yawvalue = 90;
        }
        if (yawvalue > 180 & yawvalue < 270) {
            yawvalue = 270;
        }
        pitchvalue = -bbDeltaPitch(mainPlayer->cam, mainPlayer->closestItem->collider);
        if (pitchvalue > 90 & pitchvalue <= 180) {
            pitchvalue = 90;
        }
        if (pitchvalue > 180 & pitchvalue < 270) {
            pitchvalue = 270;
        }

        bbDrawImage(uiAssets->handIcon[HAND_ICON_GRAB], Int(userOptions->screenWidth / 2 + bbSin(yawvalue) * (userOptions->screenWidth / 3) - 32), Int(userOptions->screenHeight / 2 - bbSin(pitchvalue) * (userOptions->screenHeight / 3) - 32));
    }

    if (DrawHandIcon) {
        bbDrawImage(uiAssets->handIcon[HAND_ICON_TOUCH], userOptions->screenWidth / 2 - 32, userOptions->screenHeight / 2 - 32);
    }
    for (i = 0; i <= 3; i++) {
        if (mainPlayer->drawDirectionialArrow[i]) {
            x = userOptions->screenWidth / 2 - 32;
            y = userOptions->screenHeight / 2 - 32;
            switch (i) {
                case 0: {
                    y = y - 64 - 5;
                }
                case 1: {
                    x = x + 64 + 5;
                }
                case 2: {
                    y = y + 64 + 5;
                }
                case 3: {
                    x = x - 5 - 64;
                }
            }
            bbDrawImage(uiAssets->handIcon[HAND_ICON_TOUCH], x, y);
            bbColor(0, 0, 0);
            bbRect(x + 4, y + 4, 64 - 8, 64 - 8);
            bbDrawImage(uiAssets->arrow[i], x + 21, y + 21);
            mainPlayer->drawDirectionialArrow[i] = false;
        }
    }

    if (CurrGameState == GAMESTATE_SCP294) {
        Draw294();
    }

    if (userOptions->hudEnabled) {

        width = 204;
        height = 20;
        x = 80;
        y = userOptions->screenHeight - 95;

        bbColor(255, 255, 255);
        bbRect(x, y, width, height, false);
        for (i = 1; i <= Int(((width - 2) * (mainPlayer->blinkTimer / (mainPlayer->blinkFreq))) / 10); i++) {
            bbDrawImage(uiAssets->blinkBar, x + 3 + 10 * (i - 1), y + 3);
        }
        bbColor(0, 0, 0);
        bbRect(x - 50, y, 30, 30);

        //TODO: FIX
        //If (EyeIrritation > 0) Then
        //	Color(200, 0, 0)
        //	Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
        //EndIf

        bbColor(255, 255, 255);
        bbRect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, false);

        bbDrawImage(uiAssets->blinkIcon, x - 50, y);

        y = userOptions->screenHeight - 55;
        bbColor(255, 255, 255);
        bbRect(x, y, width, height, false);
        for (i = 1; i <= Int(((width - 2) * (mainPlayer->stamina / 100.0)) / 10); i++) {
            bbDrawImage(uiAssets->staminaBar, x + 3 + 10 * (i - 1), y + 3);
        }

        bbColor(0, 0, 0);
        bbRect(x - 50, y, 30, 30);

        bbColor(255, 255, 255);
        bbRect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, false);
        if (mainPlayer->crouching) {
            bbDrawImage(uiAssets->crouchIcon, x - 50, y);
        } else {
            bbDrawImage(uiAssets->sprintIcon, x - 50, y);
        }

        if (DebugHUD) {
            bbColor(255, 255, 255);
            bbSetFont(uiAssets->consoleFont);

            //Text(x + 250, 50, "Zone: " + (EntityZ(mainPlayer\collider)/8.0))
            bbText(x - 50, 50, "Player Position: (" + f2s(bbEntityX(mainPlayer->collider), 3) + ", " + f2s(bbEntityY(mainPlayer->collider), 3) + ", " + f2s(bbEntityZ(mainPlayer->collider), 3) + "), speed: "+f2s(mainPlayer->dropSpeed, 3));
            bbText(x - 50, 70, "Camera Position: (" + f2s(bbEntityX(mainPlayer->cam), 3)+ ", " + f2s(bbEntityY(mainPlayer->cam), 3) +", " + f2s(bbEntityZ(mainPlayer->cam), 3) + ")");
            bbText(x - 50, 100, "Player Rotation: (" + f2s(bbEntityPitch(mainPlayer->collider), 3) + ", " + f2s(bbEntityYaw(mainPlayer->collider), 3) + ", " + f2s(bbEntityRoll(mainPlayer->collider), 3) + ")");
            bbText(x - 50, 120, "Camera Rotation: (" + f2s(bbEntityPitch(mainPlayer->cam), 3)+ ", " + f2s(bbEntityYaw(mainPlayer->cam), 3) +", " + f2s(bbEntityRoll(mainPlayer->cam), 3) + ")");
            bbText(x - 50, 150, "Room: " + mainPlayer->currRoom->roomTemplate->name);


            for (int iterator64 = 0; iterator64 < Event::getListSize(); iterator64++) {
                ev = Event::getObject(iterator64);

                if (ev->room == mainPlayer->currRoom) {
                    bbText(x - 50, 170, "Room event: " + ev->name);
                    bbText(x - 50, 190, "state: " + Str(ev->eventState));
                    bbText(x - 50, 210, "state2: " + Str(ev->eventState2));
                    bbText(x - 50, 230, "state3: " + Str(ev->eventState3));
                    Exit;
                }
            }
            bbText(x - 50, 250, "Room coordinates: (" + Str(bbFloor(bbEntityX(mainPlayer->currRoom->obj) / 8.0 + 0.5)) + ", " + Str(bbFloor(bbEntityZ(mainPlayer->currRoom->obj) / 8.0 + 0.5)) + ")");
            bbText(x - 50, 280, "Stamina: " + f2s(mainPlayer->stamina, 3));
            bbText(x - 50, 300, "Dead: " + Str(mainPlayer->dead));
            bbText(x - 50, 320, "Blink timer: " + f2s(mainPlayer->blinkTimer, 3));
            bbText(x - 50, 340, "Injuries: " + Str(mainPlayer->injuries));
            bbText(x - 50, 360, "Bloodloss: " + Str(mainPlayer->bloodloss));
            bbText(x - 50, 390, "SCP - 173 Position (collider): (" + f2s(bbEntityX(Curr173->collider), 3) + ", " + f2s(bbEntityY(Curr173->collider), 3) + ", " + f2s(bbEntityZ(Curr173->collider), 3) + ")");
            bbText(x - 50, 410, "SCP - 173 Position (obj): (" + f2s(bbEntityX(Curr173->obj), 3) + ", " + f2s(bbEntityY(Curr173->obj), 3) + ", " + f2s(bbEntityZ(Curr173->obj), 3) + ")");
            //Text(x - 50, 410, "SCP - 173 Idle: " + Curr173\idle)
            bbText(x - 50, 430, "SCP - 173 State: " + Str(Curr173->state));
            bbText(x - 50, 450, "SCP - 106 Position: (" + f2s(bbEntityX(Curr106->obj), 3) + ", " + f2s(bbEntityY(Curr106->obj), 3) + ", " + f2s(bbEntityZ(Curr106->obj), 3) + ")");
            bbText(x - 50, 470, "SCP - 106 Idle: " + Str(Curr106->idle));
            bbText(x - 50, 490, "SCP - 106 State: " + Str(Curr106->state));
            offset = 0;

            for (int iterator65 = 0; iterator65 < NPC::getListSize(); iterator65++) {
                npc = NPC::getObject(iterator65);

                if (npc->npcType == NPCtype096) {
                    bbText(x - 50, 510, "SCP - 096 Position: (" + f2s(bbEntityX(npc->obj), 3) + ", " + f2s(bbEntityY(npc->obj), 3) + ", " + f2s(bbEntityZ(npc->obj), 3) + ")");
                    bbText(x - 50, 530, "SCP - 096 Idle: " + Str(npc->idle));
                    bbText(x - 50, 550, "SCP - 096 State: " + Str(npc->state));
                    bbText(x - 50, 570, "SCP - 096 Speed: " + f2s(npc->currSpeed, 5));
                }
                if (npc->npcType == NPCtypeMTF) {
                    bbText(x - 50, 600 + 60 * offset, "MTF " + Str(offset) + " Position: (" + f2s(bbEntityX(npc->obj), 3) + ", " + f2s(bbEntityY(npc->obj), 3) + ", " + f2s(bbEntityZ(npc->obj), 3) + ")");
                    bbText(x - 50, 640 + 60 * offset, "MTF " + Str(offset) + " State: " + Str(npc->state));
                    bbText(x - 50, 620 + 60 * offset, "MTF " + Str(offset) + " LastSeen: " + Str(npc->lastSeen));
                    offset = offset + 1;
                }
            }

            bbSetFont(uiAssets->font[0]);
        }

    }

    if (SelectedScreen != nullptr) {
        bbDrawImage(SelectedScreen->img, userOptions->screenWidth/2-bbImageWidth(SelectedScreen->img)/2,userOptions->screenHeight/2-bbImageHeight(SelectedScreen->img)/2);
    }

    //TODO: cleanup
    int shouldDrawHUD = true;
    if (mainPlayer->selectedDoor != nullptr) {
        mainPlayer->selectedItem = nullptr;

        if (shouldDrawHUD) {
            buttonObj = GrabMesh("GFX/Map/Meshes/Button.b3d");
            pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true),bbEntityZ(mainPlayer->closestButton,true));
            bbRotateEntity(pvt, 0, bbEntityYaw(mainPlayer->closestButton,true)-180,0);
            bbMoveEntity(pvt, 0,0,0.22);
            bbPositionEntity(mainPlayer->head, bbEntityX(pvt),bbEntityY(pvt),bbEntityZ(pvt));
            bbPointEntity(mainPlayer->head, mainPlayer->closestButton);
            bbFreeEntity(pvt);

            bbCameraProject(mainPlayer->cam, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true)+bbMeshHeight(buttonObj)*0.015,bbEntityZ(mainPlayer->closestButton,true));
            projY = bbProjectedY();
            bbCameraProject(mainPlayer->cam, bbEntityX(mainPlayer->closestButton,true),bbEntityY(mainPlayer->closestButton,true)-bbMeshHeight(buttonObj)*0.015,bbEntityZ(mainPlayer->closestButton,true));
            scale = (bbProjectedY()-projY)/462.0;

            x = Int(userOptions->screenWidth/2-bbImageWidth(uiAssets->keypadHUD)*scale/2);
            y = Int(userOptions->screenHeight/2-bbImageHeight(uiAssets->keypadHUD)*scale/2);

            DropAsset(buttonObj);

            bbSetFont(uiAssets->font[2]);
            if (KeypadMSG == "") {
                bbText(userOptions->screenWidth/2, Int(y+70*scale), "ACCESS CODE: ",true,true);
                bbSetFont(uiAssets->font[3]);
                bbText(userOptions->screenWidth/2, Int(y+124*scale), KeypadInput,true,true);
            }


        }
    }

    DrawInventory(mainPlayer);
}

void DrawPauseMenu() {
    String titleText = "PAUSED";
    int x;
    int y;
    int width;
    int height;

    if (CurrGameState == GAMESTATE_PAUSED) {
        width = bbImageWidth(uiAssets->pauseMenuBG);
        height = bbImageHeight(uiAssets->pauseMenuBG);
        x = userOptions->screenWidth / 2 - width / 2;
        y = userOptions->screenHeight / 2 - height / 2;

        bbDrawImage(uiAssets->pauseMenuBG, x, y);

        bbColor(255, 255, 255);

        x = Int(x+132*MenuScale);
        y = Int(y+122*MenuScale);

        if (mainPlayer->dead) {
            titleText = "YOU DIED";
        }
        bbSetFont(uiAssets->font[1]);
        bbText(x, Int(y-(122-45)*MenuScale), titleText,false,true);

        bbSetFont(uiAssets->font[0]);
        bbText(x, y, "Difficulty: "+SelectedDifficulty->name);
        bbText(x, Int(y+20*MenuScale), "Save: "+CurrSave);
        bbText(x, Int(y+40*MenuScale), "Map seed: "+RandomSeed);

        y = y+10;

        if (!mainPlayer->dead) {
            y = Int(y+72*MenuScale);

            DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Resume", true);
            y = y + Int(75*MenuScale);
            if (!SelectedDifficulty->permaDeath) {
                if (GameSaved) {
                    DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Load Game");
                } else {
                    DrawFrame(x,y,Int(390*MenuScale), Int(60*MenuScale));
                    bbColor(100, 100, 100);
                    bbSetFont(uiAssets->font[1]);
                    bbText(Int(x + (390*MenuScale) / 2), Int(y + (60*MenuScale) / 2), "Load Game", true, true);
                }
                y = Int(y + 75*MenuScale);
            }

            DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Options");
            y = Int(y + 75*MenuScale);

            DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Quit");
        } else {
            y = Int(y+104*MenuScale);
            if (GameSaved & (!SelectedDifficulty->permaDeath)) {
                DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Load Game");
            } else {
                DrawUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "");
                bbColor(50,50,50);
                bbText(Int(x + 185*MenuScale), Int(y + 30*MenuScale), "Load Game", true, true);
            }
            DrawUIButton(x, Int(y + 80*MenuScale), Int(390*MenuScale), Int(60*MenuScale), "Quit to Menu");
            y = Int(y + 80*MenuScale);
        }

        bbSetFont(uiAssets->font[0]);
        if (mainPlayer->dead) {
            RowText(DeathMSG, x, Int(y + 80*MenuScale), Int(390*MenuScale), Int(600*MenuScale));
        }
        //EndIf


    }

    bbSetFont(uiAssets->font[0]);

}

void UpdatePauseMenu() {
    int x;
    int y;
    int z;
    int width;
    int height;
    Room* r;
    int achvXImg;
    float scale;
    int separationConst;
    int imgSize;

    if (CurrGameState == GAMESTATE_PAUSED) {
        width = bbImageWidth(uiAssets->pauseMenuBG);
        height = bbImageHeight(uiAssets->pauseMenuBG);
        x = userOptions->screenWidth / 2 - width / 2;
        y = userOptions->screenHeight / 2 - height / 2;

        x = Int(x+132*MenuScale);
        y = Int(y+122*MenuScale);

        achvXImg = Int(x + (22*MenuScale));
        scale = userOptions->screenHeight/768.0;
        separationConst = Int(76*scale);
        imgSize = 64;

        y = y+10;

        if (!mainPlayer->dead) {
            y = Int(y+72*MenuScale);

            if (UpdateUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Resume", true)) {
                CurrGameState = GAMESTATE_PLAYING;
                ResumeSounds();
                bbMouseXSpeed();
                bbMouseYSpeed();
                bbMouseZSpeed();
                mouse_x_speed_1 = 0.0;
                mouse_y_speed_1 = 0.0;
            }

            y = Int(y + 75*MenuScale);
            if (!SelectedDifficulty->permaDeath) {
                if (GameSaved) {
                    if (UpdateUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Load Game")) {
                        DrawLoading(0);

                        CurrGameState = GAMESTATE_PLAYING;
                        LoadGame(SavePath + CurrSave + "/",true);

                        bbMoveMouse(viewport_center_x,viewport_center_y);
                        bbSetFont(uiAssets->font[0]);
                        bbHidePointer();

                        bbFlushKeys();
                        bbFlushMouse();
                        mainPlayer->disableControls = false;

                        UpdateRooms();

                        for (int iterator66 = 0; iterator66 < Room::getListSize(); iterator66++) {
                            r = Room::getObject(iterator66);

                            x = Int(Abs(bbEntityX(mainPlayer->collider) - bbEntityX(r->obj)));
                            z = Int(Abs(bbEntityZ(mainPlayer->collider) - bbEntityZ(r->obj)));

                            if (x < 12.0 & z < 12.0) {
                                //MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
                                if (x < 4.0 & z < 4.0) {
                                    if (Abs(bbEntityY(mainPlayer->collider) - bbEntityY(r->obj)) < 1.5) {
                                        mainPlayer->currRoom = r;
                                    }
                                    //MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
                                }
                            }
                        }

                        DrawLoading(100);

                        mainPlayer->dropSpeed = 0;

                        bbUpdateWorld(0.0);
                    }
                }
                y = Int(y + 75*MenuScale);
            }

            //If (UpdateUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Options")) Then OptionsMenu = 1 ;TODO: fix
            y = Int(y + 75*MenuScale);
            if (UpdateUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Quit")) {
                //TODO: ask for saving
                NullGame();
                CurrGameState = GAMESTATE_MAINMENU;
                CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
                CurrSave = "";
                bbFlushKeys();
            }
        } else {
            y = Int(y+104*MenuScale);
            if (GameSaved & (!SelectedDifficulty->permaDeath)) {
                if (UpdateUIButton(x, y, Int(390*MenuScale), Int(60*MenuScale), "Load Game")) {
                    DrawLoading(0);

                    CurrGameState = GAMESTATE_PLAYING;
                    LoadGame(SavePath + CurrSave + "/",true);

                    bbMoveMouse(viewport_center_x,viewport_center_y);
                    bbSetFont(uiAssets->font[0]);
                    bbHidePointer();

                    bbFlushKeys();
                    bbFlushMouse();
                    mainPlayer->disableControls = false;

                    UpdateRooms();

                    for (int iterator67 = 0; iterator67 < Room::getListSize(); iterator67++) {
                        r = Room::getObject(iterator67);

                        x = Int(Abs(bbEntityX(mainPlayer->collider) - bbEntityX(r->obj)));
                        z = Int(Abs(bbEntityZ(mainPlayer->collider) - bbEntityZ(r->obj)));

                        if (x < 12.0 & z < 12.0) {
                            //MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
                            if (x < 4.0 & z < 4.0) {
                                if (Abs(bbEntityY(mainPlayer->collider) - bbEntityY(r->obj)) < 1.5) {
                                    mainPlayer->currRoom = r;
                                }
                                //MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
                            }
                        }
                    }

                    DrawLoading(100);

                    mainPlayer->dropSpeed = 0;

                    bbUpdateWorld(0.0);
                }
            }
            if (UpdateUIButton(x, Int(y + 80*MenuScale), Int(390*MenuScale), Int(60*MenuScale), "Quit to Menu")) {
                NullGame();
                CurrGameState = GAMESTATE_MAINMENU;
                CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN;
                CurrSave = "";
                bbFlushKeys();
            }
            y = Int(y + 80*MenuScale);
        }
    }

    bbSetFont(uiAssets->font[0]);

}

String f2s(float n, int count) {
    return bbLeft(Str(n), bbLen(Str(Int(n)))+count+1);
}

float Animate2(int entity, float curr, int start, int quit, float speed, int loop = true) {

    float newTime;
    int temp;

    if (speed > 0.0) {
        newTime = Max(Min(curr + speed * timing->tickDuration,quit),start);

        if (loop) {
            if (newTime ==> quit) {
                //SetAnimTime(entity, start)
                newTime = start;
            } else {
                //SetAnimTime(entity, newTime)
            }
        } else {
            //SetAnimTime(entity, newTime)
        }
    } else {
        if (start < quit) {
            temp = start;
            start = quit;
            quit = temp;
        }

        if (loop) {
            newTime = curr + speed * timing->tickDuration;

            if (newTime < quit) {
                newTime = start;
            }
            if (newTime > start) {
                newTime = quit;
            }

            //SetAnimTime(entity, newTime)
        } else {
            //SetAnimTime(entity, Max(Min(curr + speed * timing\tickDuration,start),quit))
            newTime = Max(Min(curr + speed * timing->tickDuration,start),quit);
        }
    }

    bbSetAnimTime(entity, newTime);
    return newTime;

}

void UpdateInfect() {
    float temp;
    int i;
    Room* r;
    int tex;
    Decal* de;
    Particle* p;

    if (mainPlayer->infect008>0) {
        bbShowEntity(mainPlayer->overlays[OVERLAY_008]);

        if (mainPlayer->infect008 < 93.0) {
            temp = mainPlayer->infect008;
            mainPlayer->infect008 = Min(mainPlayer->infect008+timing->tickDuration*0.002,100);

            mainPlayer->blurTimer = Max(mainPlayer->infect008*3*(2.0-mainPlayer->crouchState),mainPlayer->blurTimer);

            //HeartBeatRate = Max(HeartBeatRate, 100)
            mainPlayer->heartbeatIntensity = Max(100, mainPlayer->infect008/120.0);

            bbEntityAlpha(mainPlayer->overlays[OVERLAY_008], Min(((mainPlayer->infect008*0.2)^2)/1000.0,0.5) * (bbSin(TimeInPosMilliSecs()/8.0)+2.0));

            for (i = 0; i <= 6; i++) {
                if (mainPlayer->infect008>i*15+10 & temp ==< i*15+10) {
                    PlaySound2(LoadTempSound("SFX/SCP/008/Voices" + Str(i) + ".ogg"));
                }
            }

            if (mainPlayer->infect008 > 20 & temp ==< 20.0) {
                Msg = "You feel kinda feverish.";
                MsgTimer = 70*6;
            } else if ((mainPlayer->infect008 > 40 & temp ==< 40.0)) {
                Msg = "You feel nauseated.";
                MsgTimer = 70*6;
            } else if ((mainPlayer->infect008 > 60 & temp ==< 60.0)) {
                Msg = "The nausea's getting worse.";
                MsgTimer = 70*6;
            } else if ((mainPlayer->infect008 > 80 & temp ==< 80.0)) {
                Msg = "You feel very faint.";
                MsgTimer = 70*6;
            } else if ((mainPlayer->infect008 ==>91.5)) {
                mainPlayer->blinkTimer = Max(Min(-10*(mainPlayer->infect008-91.5),mainPlayer->blinkTimer),-10);
                if (mainPlayer->infect008 >= 92.7 & temp < 92.7) {
                    for (int iterator68 = 0; iterator68 < Room::getListSize(); iterator68++) {
                        r = Room::getObject(iterator68);

                        if (r->roomTemplate->name=="008") {
                            bbPositionEntity(mainPlayer->collider, bbEntityX(r->objects[7],true),bbEntityY(r->objects[7],true),bbEntityZ(r->objects[7],true),true);
                            bbResetEntity(mainPlayer->collider);
                            r->npc[0] = CreateNPC(NPCtypeD, bbEntityX(r->objects[6],true),bbEntityY(r->objects[6],true)+0.2,bbEntityZ(r->objects[6],true));
                            r->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/008/KillScientist1.ogg");
                            r->npc[0]->soundChannels[0] = bbPlaySound(r->npc[0]->sounds[0]);
                            tex = bbLoadTexture("GFX/NPCs/classd/scientist2.jpg");
                            bbEntityTexture(r->npc[0]->obj, tex);
                            bbFreeTexture(tex);
                            r->npc[0]->state = 6;
                            mainPlayer->currRoom = r;
                            Exit;
                        }
                    }
                }
            }
        } else {
            temp = mainPlayer->infect008;
            mainPlayer->infect008 = Min(mainPlayer->infect008+timing->tickDuration*0.004,100);

            if (mainPlayer->infect008 < 94.7) {
                bbEntityAlpha(mainPlayer->overlays[OVERLAY_008], 0.5 * (bbSin(TimeInPosMilliSecs()/8.0)+2.0));
                mainPlayer->blurTimer = 900;

                if (mainPlayer->infect008 > 94.5) {
                    mainPlayer->blinkTimer = Max(Min(-50*(mainPlayer->infect008-94.5),mainPlayer->blinkTimer),-10);
                }
                bbPointEntity(mainPlayer->collider, mainPlayer->currRoom->npc[0]->collider);
                bbPointEntity(mainPlayer->currRoom->npc[0]->collider, mainPlayer->collider);
                mainPlayer->forceMove = 0.4;
                mainPlayer->injuries = 2.5;
                mainPlayer->bloodloss = 0;

                Animate2(mainPlayer->currRoom->npc[0]->obj, bbAnimTime(mainPlayer->currRoom->npc[0]->obj), 357, 381, 0.3);
            } else if ((mainPlayer->infect008 < 98.5)) {

                bbEntityAlpha(mainPlayer->overlays[OVERLAY_008], 0.5 * (bbSin(TimeInPosMilliSecs()/5.0)+2.0));
                mainPlayer->blurTimer = 950;

                if (temp < 94.7) {
                    mainPlayer->currRoom->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/008/KillScientist2.ogg");
                    mainPlayer->currRoom->npc[0]->soundChannels[0] = bbPlaySound(mainPlayer->currRoom->npc[0]->sounds[0]);

                    DeathMSG = "Subject D-9341 found ingesting Dr. [REDACTED] at Sector [REDACTED]. Subject was immediately terminated by Nine-Tailed Fox and sent for autopsy. ";
                    DeathMSG = DeathMSG + "SCP-008 infection was confirmed, after which the body was incinerated.";

                    Kill(mainPlayer);
                    de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(mainPlayer->currRoom->npc[0]->collider), 544*RoomScale + 0.01, bbEntityZ(mainPlayer->currRoom->npc[0]->collider),90,bbRnd(360),0);
                    de->size = 0.8;
                    bbScaleSprite(de->obj, de->size,de->size);
                } else if ((mainPlayer->overlays[OVERLAY_008] > 96)) {
                    mainPlayer->blinkTimer = Max(Min(-10*(mainPlayer->infect008-96),mainPlayer->blinkTimer),-10);
                } else {
                    //TODO: wtf??????
                    //KillTimer = Max(-350, KillTimer)
                }

                //TODO: this could break
                if (mainPlayer->currRoom->npc[0]->state2==0) {
                    Animate2(mainPlayer->currRoom->npc[0]->obj, bbAnimTime(mainPlayer->currRoom->npc[0]->obj), 13, 19, 0.3,false);
                    if (bbAnimTime(mainPlayer->currRoom->npc[0]->obj) ==> 19) {
                        mainPlayer->currRoom->npc[0]->state2 = 1;
                    }
                } else {
                    Animate2(mainPlayer->currRoom->npc[0]->obj, bbAnimTime(mainPlayer->currRoom->npc[0]->obj), 19, 13, -0.3);
                    if (bbAnimTime(mainPlayer->currRoom->npc[0]->obj) ==< 13) {
                        mainPlayer->currRoom->npc[0]->state2 = 0;
                    }
                }

                if (bbRand(50)==1) {
                    p = CreateParticle(bbEntityX(mainPlayer->currRoom->npc[0]->collider),bbEntityY(mainPlayer->currRoom->npc[0]->collider),bbEntityZ(mainPlayer->currRoom->npc[0]->collider), PARTICLE_BLOOD, bbRnd(0.05,0.1), 0.15, 200);
                    p->speed = 0.01;
                    p->sizeChange = 0.01;
                    p->a = 0.5;
                    p->aChange = -0.01;
                    bbRotateEntity(p->pvt, bbRnd(360),bbRnd(360),0);
                }

                bbPositionEntity(mainPlayer->head, bbEntityX(mainPlayer->currRoom->npc[0]->collider,true), bbEntityY(mainPlayer->currRoom->npc[0]->collider,true)+0.65,bbEntityZ(mainPlayer->currRoom->npc[0]->collider,true),true);
                bbRotateEntity(mainPlayer->head, (1.0+bbSin(TimeInPosMilliSecs()/5.0))*15, mainPlayer->currRoom->angle-180, 0, true);
                bbMoveEntity(mainPlayer->head, 0,0,0.4);
                bbTurnEntity(mainPlayer->head, 80+(bbSin(TimeInPosMilliSecs()/5.0))*30,(bbSin(TimeInPosMilliSecs()/5.0))*40,0);
            }
        }


    } else {
        bbHideEntity(mainPlayer->overlays[OVERLAY_008]);
    }
}

int Graphics3DExt(int width, int height, int depth = 32, int mode = 2) {
    //If (FE_InitExtFlag = 1) Then DeInitExt() ;prevent FastExt from breaking itself
    bbGraphics3D(width,height,depth,mode);
    InitFastResize();
    //InitExt()
}

void RenderWorld2() {
    int k;
    int l;
    float decayMultiplier = 1.0;
    int temp;
    int temp2;
    float dist;
    float yawvalue;
    float pitchvalue;
    float xvalue;
    float yvalue;
    NPC* np;

    bbCameraProjMode(ark_blur_cam,0);
    bbCameraProjMode(mainPlayer->cam,1);

    //TODO: fix
    //If (WearingNightVision>0 And WearingNightVision<3) Then
    //	AmbientLight(Min(Brightness*2,255), Min(Brightness*2,255), Min(Brightness*2,255))
    //ElseIf WearingNightVision=3
    //	AmbientLight(255,255,255)
    //ElseIf mainPlayer\currRoom<>Null
    //	If (mainPlayer\currRoom\roomTemplate\name<>"173") And (mainPlayer/currRoom\roomTemplate\name<>"exit1") And (mainPlayer/currRoom\roomTemplate/Name<>"gatea") Then
    //		AmbientLight(Brightness, Brightness, Brightness)
    //	EndIf
    //EndIf

    //IsNVGBlinking% = False
    //HideEntity(NVBlink)

    int hasBattery = 2;
    int power = 0;

    Item* wornItem = nullptr;

    if (wornItem!=nullptr) {
        if (wornItem->template->name != "nvgoggles" & wornItem->template->name != "supernv") {
            wornItem = nullptr;
        }
    }

    if (wornItem!=nullptr) {
        if (wornItem->template->name == "supernv") {
            decayMultiplier = 2.0;
        }

        power = Int(wornItem->state);
        //this nvg can't be used
        if (wornItem->state <= 0.0) {
            hasBattery = 0;
            Msg = "The batteries in these night vision goggles died.";
            mainPlayer->blinkTimer = -1.0;
            MsgTimer = 350;
        } else if ((wornItem->state <= 100.0)) {
            hasBattery = 1;
        }

        if (hasBattery) {
            bbRenderWorld(Max(0.0,1.0+(timing->accumulator/timing->tickDuration)));
        }
    } else {
        bbRenderWorld(Max(0.0,1.0+(timing->accumulator/timing->tickDuration)));
    }

    //If hasBattery=0 And WearingNightVision<>3
    //	;IsNVGBlinking% = True
    //	ShowEntity(NVBlink%)
    //EndIf

    if (mainPlayer->blinkTimer < - 16 | mainPlayer->blinkTimer > - 6) {
        //show a HUD
        if (IsPlayerWearingItem(mainPlayer,"supernv") & hasBattery!=0) {
            //NVTimer=NVTimer-timing\tickDuration

            //If (NVTimer<=0.0) Then
            for (int iterator69 = 0; iterator69 < NPC::getListSize(); iterator69++) {
                np = NPC::getObject(iterator69);

                np->nvX = bbEntityX(np->collider,true);
                np->nvY = bbEntityY(np->collider,true);
                np->nvZ = bbEntityZ(np->collider,true);
            }
            //IsNVGBlinking% = True
            //	ShowEntity(NVBlink%)
            //	If (NVTimer<=-10) Then
            //		NVTimer = 600.0
            //	EndIf
            //EndIf

            bbColor(255,255,255);

            bbSetFont(uiAssets->font[2]);

            bbText(userOptions->screenWidth/2,Int(20*MenuScale),"REFRESHING DATA IN",true,false);

            //Text(userOptions\screenWidth/2,Int(60*MenuScale),Max(f2s(NVTimer/60.0,1),0.0),True,False)
            bbText(userOptions->screenWidth/2,Int(100*MenuScale),"SECONDS",true,false);

            temp = bbCreatePivot();
            temp2 = bbCreatePivot();
            bbPositionEntity(temp, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));

            //*(NVTimer/600.0))
            bbColor(255,255,255);

            for (int iterator70 = 0; iterator70 < NPC::getListSize(); iterator70++) {
                np = NPC::getObject(iterator70);

                //don't waste your time if the string is empty
                if (np->nvName!="") {
                    bbPositionEntity(temp2,np->nvX,np->nvY,np->nvZ);
                    dist = bbEntityDistance(temp2,mainPlayer->collider);
                    //don't draw text if the NPC is too far away
                    if (dist<23.5) {
                        bbPointEntity(temp, temp2);
                        yawvalue = WrapAngle(bbEntityYaw(mainPlayer->cam) - bbEntityYaw(temp));
                        xvalue = 0.0;
                        if (yawvalue > 90 & yawvalue <= 180) {
                            xvalue = bbSin(90)/90*yawvalue;
                        } else if ((yawvalue > 180 & yawvalue < 270)) {
                            xvalue = bbSin(270)/yawvalue*270;
                        } else {
                            xvalue = bbSin(yawvalue);
                        }

                        pitchvalue = WrapAngle(bbEntityPitch(mainPlayer->cam) - bbEntityPitch(temp));
                        yvalue = 0.0;
                        if (pitchvalue > 90 & pitchvalue <= 180) {
                            yvalue = bbSin(90)/90*pitchvalue;
                        } else if ((pitchvalue > 180 & pitchvalue < 270)) {
                            yvalue = bbSin(270)/pitchvalue*270;
                        } else {
                            yvalue = bbSin(pitchvalue);
                        }

                        //If (Not IsNVGBlinking%) Then
                        bbText(Int(userOptions->screenWidth / 2 + xvalue * (userOptions->screenWidth / 2)), Int(userOptions->screenHeight / 2 - yvalue * (userOptions->screenHeight / 2)), np->nvName,true,true);
                        bbText(Int(userOptions->screenWidth / 2 + xvalue * (userOptions->screenWidth / 2)), Int(userOptions->screenHeight / 2 - yvalue * (userOptions->screenHeight / 2) + 30.0 * MenuScale), f2s(dist,1)+" m",true,true);
                        //EndIf
                    }
                }
            }

            bbFreeEntity(temp);
            bbFreeEntity (temp2);

            bbColor(0,0,55);
            for (k = 0; k <= 10; k++) {
                bbRect(45, Int(userOptions->screenHeight*0.5-(k*20)), 54, 10, true);
            }
            bbColor(0,0,255);
            for (l = 0; l <= Int(bbFloor((power+50)*0.01)); l++) {
                bbRect(45, Int(userOptions->screenHeight*0.5-(l*20)), 54, 10, true);
            }
            //DrawImage(NVGImages,40,userOptions\screenHeight*0.5+30,1)

            bbColor(255,255,255);
        } else if ((IsPlayerWearingItem(mainPlayer,"nvgoggles") & hasBattery!=0)) {
            bbColor(0,55,0);
            for (k = 0; k <= 10; k++) {
                bbRect(45, Int(userOptions->screenHeight*0.5-(k*20)), 54, 10, true);
            }
            bbColor(0,255,0);
            for (l = 0; l <= Int(bbFloor((power+50)*0.01)); l++) {
                bbRect(45, Int(userOptions->screenHeight*0.5-(l*20)), 54, 10, true);
            }
            //DrawImage(NVGImages,40,userOptions\screenHeight*0.5+30,0)
        }
    }

    //render sprites
    bbCameraProjMode(ark_blur_cam,2);
    bbCameraProjMode(mainPlayer->cam,0);
    bbRenderWorld();
    bbCameraProjMode(ark_blur_cam,0);

    if (mainPlayer->blinkTimer < - 16 | mainPlayer->blinkTimer > - 6) {
        if (wornItem!=nullptr) & (hasBattery==1) & ((TimeInPosMilliSecs() % 800) < 400) {
            bbColor(255,0,0);
            bbSetFont(uiAssets->font[2]);

            bbText(userOptions->screenWidth/2, Int(20*MenuScale), "WARNING: LOW BATTERY", true, false);
            bbColor(255,255,255);
        }
    }
}

int CheckForPlayerInFacility() {
    //False (=0): NPC is not in facility (mostly meant for "dimension1499")
    //True (=1): NPC is in facility
    //2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)

    if (bbEntityY(mainPlayer->collider)>100.0) {
        return false;
    }
    if (bbEntityY(mainPlayer->collider)< -10.0) {
        return 2;
    }
    if (bbEntityY(mainPlayer->collider)> 7.0 & bbEntityY(mainPlayer->collider)<=100.0) {
        return 2;
    }

    return true;
}

String CheckTriggers() {
    int i;
    float sx;
    float sy;
    float sz;
    int inside = -1;

    if (mainPlayer->currRoom->triggerboxAmount == 0) {
        return "";
    } else {
        for (i = 0; i <= mainPlayer->currRoom->triggerboxAmount-1; i++) {
            bbEntityAlpha(mainPlayer->currRoom->triggerbox[i],1.0);
            sx = EntityScaleX(mainPlayer->currRoom->triggerbox[i], 1);
            sy = Max(EntityScaleY(mainPlayer->currRoom->triggerbox[i], 1), 0.001);
            sz = EntityScaleZ(mainPlayer->currRoom->triggerbox[i], 1);
            GetMeshExtents(mainPlayer->currRoom->triggerbox[i]);
            bbEntityAlpha(mainPlayer->currRoom->triggerbox[i],0.0);
            if (bbEntityX(mainPlayer->collider)>((sx*Mesh_MinX)+mainPlayer->currRoom->x) & bbEntityX(mainPlayer->collider)<((sx*Mesh_MaxX)+mainPlayer->currRoom->x)) {
                if (bbEntityY(mainPlayer->collider)>((sy*Mesh_MinY)+mainPlayer->currRoom->y) & bbEntityY(mainPlayer->collider)<((sy*Mesh_MaxY)+mainPlayer->currRoom->y)) {
                    if (bbEntityZ(mainPlayer->collider)>((sz*Mesh_MinZ)+mainPlayer->currRoom->z) & bbEntityZ(mainPlayer->collider)<((sz*Mesh_MaxZ)+mainPlayer->currRoom->z)) {
                        inside = i;
                        Exit;
                    }
                }
            }
        }

        if (inside > -1) {
            return mainPlayer->currRoom->triggerboxName[inside];
        }
    }

}

}

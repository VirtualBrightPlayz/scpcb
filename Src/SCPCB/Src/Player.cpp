#include "Player.h"

namespace CBN {

// Structs.
std::vector<Player*> Player::list;
Player::Player() {
    list.push_back(this);
}
Player::~Player() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Player::getListSize() {
    return list.size();
}
Player* Player::getObject(int index) {
    return list[index];
}

// Globals.
Player* mainPlayer = nullptr;
int viewport_center_x;
int viewport_center_y;
float mouselook_x_inc = 0.3;
float mouselook_y_inc = 0.3;
int mouse_left_limit;
int mouse_right_limit;
int mouse_top_limit;
int mouse_bottom_limit;
float mouse_x_speed_1;
float mouse_y_speed_1;

// Functions.
Player* CreatePlayer() {
    Player* player = new Player();

    player->inventory = CreateInventory(PLAYER_INV_COUNT + WORNITEM_SLOT_COUNT);

    player->cam = bbCreateCamera();
    bbCameraViewport(player->cam, 0, 0, userOptions->screenWidth, userOptions->screenHeight);
    bbCameraRange(player->cam, 0.05, 60);
    bbCameraFogMode(player->cam, 1);
    //TODO: use constants
    bbCameraFogRange(player->cam, 0.05, 60);
    //TODO: Change tint based on zone?
    bbCameraFogColor(player->cam, 0, 0, 0);

    int fogTexture = bbLoadTexture("GFX/Overlays/fog.jpg", 1);
    //FogNVTexture = LoadTexture("GFX/fogNV.jpg", 1)

    float scaleWidth = userOptions->screenWidth / 1024.0;
    float scaleHeight = MenuScale * 0.8;

    //TODO: take ownership of ark_blur_cam
    int gasMaskTexture = bbLoadTexture("GFX/Overlays/GasmaskOverlay.jpg", 1);
    player->overlays[OVERLAY_GASMASK] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(player->overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_GASMASK], gasMaskTexture);
    bbEntityBlend(player->overlays[OVERLAY_GASMASK], 2);
    bbEntityFX(player->overlays[OVERLAY_GASMASK], 1);
    bbEntityOrder(player->overlays[OVERLAY_GASMASK], -1003);
    bbMoveEntity(player->overlays[OVERLAY_GASMASK], 0, 0, 1.0);
    bbHideEntity(player->overlays[OVERLAY_GASMASK]);

    int infectTexture = bbLoadTexture("GFX/Overlays/InfectOverlay.jpg", 1);
    player->overlays[OVERLAY_008] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(player->overlays[OVERLAY_008], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_008], infectTexture);
    bbEntityBlend(player->overlays[OVERLAY_008], 3);
    bbEntityFX(player->overlays[OVERLAY_008], 1);
    bbEntityOrder(player->overlays[OVERLAY_008], -1003);
    bbMoveEntity(player->overlays[OVERLAY_008], 0, 0, 1.0);
    //EntityAlpha(InfectOverlay, 255.0)
    bbHideEntity(player->overlays[OVERLAY_008]);

    int nvTexture = bbLoadTexture("GFX/Overlays/NightVisionOverlay.jpg", 1);
    player->overlays[OVERLAY_NIGHTVISION] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(player->overlays[OVERLAY_NIGHTVISION], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_NIGHTVISION], nvTexture);
    bbEntityBlend(player->overlays[OVERLAY_NIGHTVISION], 2);
    bbEntityFX(player->overlays[OVERLAY_NIGHTVISION], 1);
    bbEntityOrder(player->overlays[OVERLAY_NIGHTVISION], -1003);
    bbMoveEntity(player->overlays[OVERLAY_NIGHTVISION], 0, 0, 1.0);
    bbHideEntity(player->overlays[OVERLAY_NIGHTVISION]);

    //TODO: do we need this?
    //NVBlink = CreateSprite(ark_blur_cam)
    //ScaleSprite(NVBlink, Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
    //EntityColor(NVBlink,0,0,0)
    //EntityFX(NVBlink, 1)
    //EntityOrder(NVBlink, -1005)
    //MoveEntity(NVBlink, 0, 0, 1.0)
    //HideEntity(NVBlink)

    int darkTexture = bbCreateTexture(1024, 1024, 1 + 2);
    bbSetBuffer(bbTextureBuffer(darkTexture));
    bbCls();
    bbSetBuffer(bbBackBuffer());

    scaleWidth = userOptions->screenWidth / 1240.0;
    scaleHeight = userOptions->screenHeight / 960.0 * 0.8;

    player->overlays[OVERLAY_FOG] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(player->overlays[OVERLAY_FOG], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_FOG], fogTexture);
    bbEntityBlend(player->overlays[OVERLAY_FOG], 2);
    bbEntityOrder(player->overlays[OVERLAY_FOG], -1000);
    bbMoveEntity(player->overlays[OVERLAY_FOG], 0, 0, 1.0);

    player->overlays[OVERLAY_BLACK] = bbCreateSprite(player->cam);
    bbScaleSprite(player->overlays[OVERLAY_BLACK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_BLACK], darkTexture);
    bbEntityBlend(player->overlays[OVERLAY_BLACK], 1);
    bbEntityOrder(player->overlays[OVERLAY_BLACK], -1002);
    bbMoveEntity(player->overlays[OVERLAY_BLACK], 0, 0, 1.0);
    bbEntityAlpha(player->overlays[OVERLAY_BLACK], 0.0);

    int lightTexture = bbCreateTexture(1024, 1024, 1 + 2);
    bbSetBuffer(bbTextureBuffer(lightTexture));
    bbClsColor(255, 255, 255);
    bbCls();
    bbClsColor(0, 0, 0);
    bbSetBuffer(bbBackBuffer());

    player->overlays[OVERLAY_WHITE] = bbCreateSprite(player->cam);
    bbScaleSprite(player->overlays[OVERLAY_WHITE], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8));
    bbEntityTexture(player->overlays[OVERLAY_WHITE], lightTexture);
    bbEntityBlend(player->overlays[OVERLAY_WHITE], 1);
    bbEntityOrder(player->overlays[OVERLAY_WHITE], -1002);
    bbMoveEntity(player->overlays[OVERLAY_WHITE], 0, 0, 1.0);
    bbHideEntity(player->overlays[OVERLAY_WHITE]);

    bbFreeTexture(gasMaskTexture);
    bbFreeTexture(infectTexture);
    bbFreeTexture(nvTexture);
    bbFreeTexture(fogTexture);
    bbFreeTexture(darkTexture);
    bbFreeTexture(lightTexture);

    player->collider = bbCreatePivot();
    bbEntityRadius(player->collider, 0.15, 0.30);
    bbEntityPickMode(player->collider, 1);
    bbEntityType(player->collider, HIT_PLAYER);

    player->head = bbCreatePivot();
    bbEntityRadius(player->head, 0.15);
    bbEntityType(player->head, HIT_PLAYER);

    //Sounds
    player->breathingSFX = CreateIntArray(2, 5);

    int i;
    for (i = 0; i <= 4; i++) {
        SetIntArrayElem(player->breathingSFX, bbLoadSound("SFX/Character/D9341/breath"+String(i)+".ogg"), 0, i);
        SetIntArrayElem(player->breathingSFX, bbLoadSound("SFX/Character/D9341/breath"+String(i)+"gas.ogg"), 1, i);
    }
    for (i = 0; i <= 3; i++) {
        player->bloodDripSFX[i] = bbLoadSound("SFX/Character/D9341/BloodDrip" + String(i) + ".ogg");
    }
    for (i = 0; i <= 8; i++) {
        player->damageSFX[i] = bbLoadSound("SFX/Character/D9341/Damage" + String(i + 1) + ".ogg");
    }
    for (i = 0; i <= 2; i++) {
        player->coughSFX[i] = bbLoadSound("SFX/Character/D9341/Cough" + String(i + 1) + ".ogg");
    }
    player->heartbeatSFX = bbLoadSound("SFX/Character/D9341/Heartbeat.ogg");

    return player;
}

void DeletePlayer(Player* player) {
    //TODO: delete/drop worn items, delete inventory
    DeleteInventory(player->inventory);

    int i;
    for (i = 0; i <= OVERLAY_COUNT-1; i++) {
        bbFreeEntity(player->overlays[i]);
    }

    for (i = 0; i <= 4; i++) {
        bbFreeSound(GetIntArrayElem(player->breathingSFX, 0, i));
        bbFreeSound(GetIntArrayElem(player->breathingSFX, 1, i));
    }
    for (i = 0; i <= 3; i++) {
        bbFreeSound(player->bloodDripSFX[i]);
    }
    for (i = 0; i <= 8; i++) {
        bbFreeSound(player->damageSFX[i]);
    }
    for (i = 0; i <= 2; i++) {
        bbFreeSound(player->coughSFX[i]);
    }
    bbFreeSound(player->heartbeatSFX);

    delete player;
}

void UpdatePlayer() {
    float Sprint = 1.0;
    float Speed = 0.018;
    int i;
    float angle;
    float temp;
    int tempchn;
    int collidedFloor;
    int pvt;
    Decal* de;

    if (mainPlayer->superMan>0.0) {
        Speed = Speed * 3;

        mainPlayer->superMan = mainPlayer->superMan+timing->tickDuration;

        mainPlayer->camShake = bbSin(mainPlayer->superMan / 5.0) * (mainPlayer->superMan / 1500.0);

        if (mainPlayer->superMan > 70 * 50) {
            DeathMSG = "A Class D jumpsuit found in [DATA REDACTED]. Upon further examination, the jumpsuit was found to be filled with 12.5 kilograms of blue ash-like substance. ";
            DeathMSG = DeathMSG + "Chemical analysis of the substance remains non-conclusive. Most likely related to SCP-914.";
            Kill(mainPlayer);
            bbShowEntity(mainPlayer->overlays[OVERLAY_FOG]);
        } else {
            mainPlayer->blurTimer = 500;
            bbHideEntity(mainPlayer->overlays[OVERLAY_FOG]);
        }
    }

    //If (DeathTimer > 0) Then
    //	DeathTimer=DeathTimer-timing\tickDuration
    //	If (DeathTimer < 1) Then DeathTimer = -1.0
    //ElseIf DeathTimer < 0
    //	Kill(mainPlayer)
    //EndIf

    mainPlayer->stamina = Min(mainPlayer->stamina + 0.15 * timing->tickDuration, 100.0);

    if (mainPlayer->staminaEffectTimer > 0) {
        mainPlayer->staminaEffect = mainPlayer->staminaEffect - (timing->tickDuration/70);
    } else {
        mainPlayer->staminaEffect = 1.0;
    }

    if (mainPlayer->currRoom!=nullptr) {
        if (mainPlayer->currRoom->roomTemplate->name != "pocketdimension") {
            if (bbKeyDown(keyBinds->sprint)) {
                //out of breath
                if (mainPlayer->stamina < 5) {
                    if (!IsChannelPlaying(mainPlayer->breathChn)) {
                        mainPlayer->breathChn = bbPlaySound(GetIntArrayElem(mainPlayer->breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), 0));
                    }
                    //panting
                } else if ((mainPlayer->stamina < 50)) {
                    if (mainPlayer->breathChn == 0) {
                        mainPlayer->breathChn = bbPlaySound(GetIntArrayElem(mainPlayer->breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), bbRand(1, 3)));
                        bbChannelVolume(mainPlayer->breathChn, Min((70.0-mainPlayer->stamina)/70.0,1.0)*userOptions->sndVolume);
                    } else {
                        if (!IsChannelPlaying(mainPlayer->breathChn)) {
                            mainPlayer->breathChn = bbPlaySound(GetIntArrayElem(mainPlayer->breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), bbRand(1, 3)));
                            bbChannelVolume(mainPlayer->breathChn, Min((70.0-mainPlayer->stamina)/70.0,1.0)*userOptions->sndVolume);
                        }
                    }
                }
            }
        }
    }

    for (i = 0; i <= mainPlayer->inventory->size-1; i++) {
        if (mainPlayer->inventory->items[i]!=nullptr) {
            if (mainPlayer->inventory->items[i]->template->name=="finevest") {
                mainPlayer->stamina = Min(mainPlayer->stamina,60.0);
            }
        }
    }

    //If (IsZombie) Then Crouch = False

    if (Abs(mainPlayer->crouchState-mainPlayer->crouching)<0.001) {
        mainPlayer->crouchState = mainPlayer->crouching;
    } else {
        mainPlayer->crouchState = CurveValue(mainPlayer->crouching, mainPlayer->crouchState, 10.0);
    }

    if (!mainPlayer->noclip) {
        if (((bbKeyDown(keyBinds->down) ^ bbKeyDown(keyBinds->up)) | (bbKeyDown(keyBinds->rght) ^ bbKeyDown(keyBinds->lft)) & (!mainPlayer->disableControls)) | mainPlayer->forceMove>0) {

            // And (Not IsZombie)) Then
            if (mainPlayer->crouching == 0 & (bbKeyDown(keyBinds->sprint)) & mainPlayer->stamina > 0.0) {
                Sprint = 2.5;
                mainPlayer->stamina = mainPlayer->stamina - timing->tickDuration * 0.5 * (1.0/mainPlayer->staminaEffect);
                if (mainPlayer->stamina <= 0) {
                    mainPlayer->stamina = -20.0;
                }
            }

            if (mainPlayer->currRoom->roomTemplate->name == "pocketdimension") {
                if (bbEntityY(mainPlayer->collider)<2000*RoomScale | bbEntityY(mainPlayer->collider)>2608*RoomScale) {
                    mainPlayer->stamina = 0;
                    Speed = 0.015;
                    Sprint = 1.0;
                }
            }

            if (mainPlayer->forceMove>0) {
                Speed = Speed*mainPlayer->forceMove;
            }

            if (mainPlayer->selectedItem!=nullptr) {
                if (mainPlayer->selectedItem->template->name == "firstaid" | mainPlayer->selectedItem->template->name == "finefirstaid" | mainPlayer->selectedItem->template->name == "firstaid2") {
                    Sprint = 0;
                }
            }

            temp = (mainPlayer->camAnimState % 360);
            if (!mainPlayer->disableControls) {
                mainPlayer->camAnimState = (mainPlayer->camAnimState + timing->tickDuration * Min(Sprint, 1.5) * 7) % 720;
            }
            if (temp < 180 & (mainPlayer->camAnimState % 360) >= 180 & (!mainPlayer->dead)) {
                //TODO: define constants for each override state
                if (mainPlayer->footstepOverride==0) {
                    temp = GetMaterialStepSound(mainPlayer->collider);

                    if (Sprint == 1.0) {
                        mainPlayer->loudness = Max(4.0,mainPlayer->loudness);

                        if (temp == STEPSOUND_METAL) {
                            tempchn = PlaySound_SM(sndManager->footstepMetal[bbRand(0, 7)]);
                        } else {
                            tempchn = PlaySound_SM(sndManager->footstep[bbRand(0, 7)]);
                        }

                        bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.6))*userOptions->sndVolume);
                    } else {
                        mainPlayer->loudness = Max(2.5-(mainPlayer->crouching*0.6),mainPlayer->loudness);

                        if (temp == 1) {
                            tempchn = PlaySound_SM(sndManager->footstepMetalRun[bbRand(0, 7)]);
                        } else {
                            tempchn = PlaySound_SM(sndManager->footstepRun[bbRand(0, 7)]);
                        }

                        bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.6))*userOptions->sndVolume);
                    }
                } else if ((mainPlayer->footstepOverride==1)) {
                    tempchn = PlaySound_SM(sndManager->footstepPD[bbRand(0, 2)]);
                    bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.4))*userOptions->sndVolume);
                } else if ((mainPlayer->footstepOverride==2)) {
                    tempchn = PlaySound_SM(sndManager->footstep8601[bbRand(0, 2)]);
                    bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.4))*userOptions->sndVolume);
                } else if ((mainPlayer->footstepOverride==3)) {
                    if (Sprint == 1.0) {
                        mainPlayer->loudness = Max(4.0,mainPlayer->loudness);
                        tempchn = PlaySound_SM(sndManager->footstep[bbRand(0, 7)]);
                        bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.6))*userOptions->sndVolume);
                    } else {
                        mainPlayer->loudness = Max(2.5-(mainPlayer->crouching*0.6),mainPlayer->loudness);
                        tempchn = PlaySound_SM(sndManager->footstepRun[bbRand(0, 7)]);
                        bbChannelVolume(tempchn, (1.0-(mainPlayer->crouching*0.6))*userOptions->sndVolume);
                    }
                }

            }
        }
        //noclip on
    } else {
        if (bbKeyDown(keyBinds->sprint)) {
            Sprint = 2.5;
        } else if ((bbKeyDown(keyBinds->crouch))) {
            Sprint = 0.5;
        }
    }

    if (bbKeyHit(keyBinds->crouch) & (!mainPlayer->disableControls)) {
        mainPlayer->crouching = (!mainPlayer->crouching);
    }

    float temp2 = (Speed * Sprint) / (1.0+mainPlayer->crouchState);

    if (mainPlayer->noclip) {
        mainPlayer->camAnimState = 0;
        mainPlayer->moveSpeed = 0;
        mainPlayer->crouchState = 0;
        mainPlayer->crouching = 0;

        bbRotateEntity(mainPlayer->collider, WrapAngle(bbEntityPitch(mainPlayer->cam)), WrapAngle(bbEntityYaw(mainPlayer->cam)), 0);

        // * NoClipSpeed ;TODO: reimplement
        temp2 = temp2;

        if (bbKeyDown(keyBinds->down)) {
            bbMoveEntity(mainPlayer->collider, 0, 0, -temp2*timing->tickDuration);
        }
        if (bbKeyDown(keyBinds->up)) {
            bbMoveEntity(mainPlayer->collider, 0, 0, temp2*timing->tickDuration);
        }

        if (bbKeyDown(keyBinds->lft)) {
            bbMoveEntity(mainPlayer->collider, -temp2*timing->tickDuration, 0, 0);
        }
        if (bbKeyDown(keyBinds->rght)) {
            bbMoveEntity(mainPlayer->collider, temp2*timing->tickDuration, 0, 0);
        }

        bbResetEntity(mainPlayer->collider);
    } else {
        temp2 = temp2 / Max((mainPlayer->injuries+3.0)/3.0,1.0);
        if (mainPlayer->injuries > 0.5) {
            temp2 = temp2*Min((bbSin(mainPlayer->camAnimState/2)+1.2),1.0);
        }

        temp = false;
        //If (Not IsZombie%)
        if (bbKeyDown(keyBinds->down) & (!mainPlayer->disableControls)) {
            temp = true;
            angle = 180;
            if (bbKeyDown(keyBinds->lft)) {
                angle = 135;
            }
            if (bbKeyDown(keyBinds->rght)) {
                angle = -135;
            }
            // Or ForceMove>0
        } else if ((bbKeyDown(keyBinds->up) & (!mainPlayer->disableControls))) {
            temp = true;
            angle = 0;
            if (bbKeyDown(keyBinds->lft)) {
                angle = 45;
            }
            if (bbKeyDown(keyBinds->rght)) {
                angle = -45;
            }
        } else if ((mainPlayer->forceMove>0)) {
            temp = true;
            angle = mainPlayer->forceAngle;
        } else if ((!mainPlayer->disableControls)) {
            if (bbKeyDown(keyBinds->lft)) {
                angle = 90;
                temp = true;
            }
            if (bbKeyDown(keyBinds->rght)) {
                angle = -90;
                temp = true;
            }
        }
        //Else
        //	temp=True
        //	angle = ForceAngle
        //EndIf

        angle = WrapAngle(bbEntityYaw(mainPlayer->collider,true)+angle+90.0);

        if ((int)(temp)) {
            mainPlayer->moveSpeed = CurveValue(temp2, mainPlayer->moveSpeed, 20.0);
        } else {
            mainPlayer->moveSpeed = Max(CurveValue(0.0, mainPlayer->moveSpeed-0.1, 1.0),0.0);
        }

        if (!mainPlayer->disableControls) {
            bbTranslateEntity(mainPlayer->collider, bbCos(angle)*mainPlayer->moveSpeed * timing->tickDuration, 0, bbSin(angle)*mainPlayer->moveSpeed * timing->tickDuration, true);
        }

        collidedFloor = false;
        for (i = 1; i <= bbCountCollisions(mainPlayer->collider); i++) {
            if (bbCollisionY(mainPlayer->collider, i) < bbEntityY(mainPlayer->collider,true)) & (Abs(bbCollisionNY(mainPlayer->collider, i))>0.8) {
                collidedFloor = true;
            }
        }

        if (collidedFloor == true) {
            if (mainPlayer->dropSpeed < - 0.07) {
                if (mainPlayer->footstepOverride==0) {
                    if (GetMaterialStepSound(mainPlayer->collider) == 1) {
                        PlaySound_SM(sndManager->footstepMetal[bbRand(0, 7)]);
                    } else {
                        PlaySound_SM(sndManager->footstep[bbRand(0, 7)]);
                    }
                } else if ((mainPlayer->footstepOverride==1)) {
                    PlaySound_SM(sndManager->footstepPD[bbRand(0, 2)]);
                } else if ((mainPlayer->footstepOverride==2)) {
                    PlaySound_SM(sndManager->footstep8601[bbRand(0, 2)]);
                } else {
                    PlaySound_SM(sndManager->footstep[bbRand(0, 7)]);
                }
                mainPlayer->loudness = Max(3.0,mainPlayer->loudness);
            }
            mainPlayer->dropSpeed = 0;
        } else {
            mainPlayer->dropSpeed = Min(Max(mainPlayer->dropSpeed - 0.006 * timing->tickDuration, -2.0), 0.0);
        }

        if (!mainPlayer->disableControls) {
            bbTranslateEntity(mainPlayer->collider, 0, mainPlayer->dropSpeed * timing->tickDuration, 0);
        }
    }

    mainPlayer->forceMove = 0.0;

    if (mainPlayer->injuries > 1.0) {
        temp2 = mainPlayer->bloodloss;
        mainPlayer->blurTimer = Max(Max(bbSin(TimeInPosMilliSecs()/100.0)*mainPlayer->bloodloss*30.0,mainPlayer->bloodloss*2*(2.0-mainPlayer->crouchState)),mainPlayer->blurTimer);
        mainPlayer->bloodloss = Min(mainPlayer->bloodloss + (Min(mainPlayer->injuries,3.5)/300.0)*timing->tickDuration,100);

        if (temp2 <= 60 & mainPlayer->bloodloss > 60) {
            Msg = "You are feeling faint from the amount of blood you loss.";
            MsgTimer = 70*4;
        }
    }

    UpdateInfect();

    if (mainPlayer->bloodloss > 0) {
        if (bbRnd(200)<Min(mainPlayer->injuries,4.0)) {
            pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->collider)+bbRnd(-0.05,0.05),bbEntityY(mainPlayer->collider)-0.05,bbEntityZ(mainPlayer->collider)+bbRnd(-0.05,0.05));
            bbTurnEntity(pvt, 90, 0, 0);
            bbEntityPick(pvt,0.3);
            de = CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), bbPickedX(), bbPickedY()+0.005, bbPickedZ(), 90, bbRand(360), 0);
            de->size = bbRnd(0.03,0.08)*Min(mainPlayer->injuries,3.0);
            bbEntityAlpha(de->obj, 1.0);
            bbScaleSprite(de->obj, de->size, de->size);
            tempchn = PlaySound2(mainPlayer->bloodDripSFX[bbRand(0,3)]);
            bbChannelVolume(tempchn, bbRnd(0.0,0.8)*userOptions->sndVolume);
            bbChannelPitch(tempchn, bbRand(20000,30000));

            bbFreeEntity(pvt);
        }

        mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.0)+1.0)*mainPlayer->bloodloss*0.2);

        if (mainPlayer->bloodloss > 60) {
            mainPlayer->crouching = true;
        }
        if (mainPlayer->bloodloss >= 100) {
            Kill(mainPlayer);
            mainPlayer->heartbeatIntensity = 0.0;
        } else if ((mainPlayer->bloodloss > 80.0)) {
            mainPlayer->heartbeatIntensity = Max(150-(mainPlayer->bloodloss-80)*5,mainPlayer->heartbeatIntensity);
        } else if ((mainPlayer->bloodloss > 35.0)) {
            mainPlayer->heartbeatIntensity = Max(70+mainPlayer->bloodloss,mainPlayer->heartbeatIntensity);
        }
    }

    if (!mainPlayer->disableControls) {
        if (bbKeyHit(keyBinds->blink)) {
            mainPlayer->blinkTimer = 0;
        }
        if (bbKeyDown(keyBinds->blink) & mainPlayer->blinkTimer < - 10) {
            mainPlayer->blinkTimer = -10;
        }
    }


    if (mainPlayer->heartbeatIntensity > 0) {
        tempchn = PlaySound2(mainPlayer->heartbeatSFX);
        bbChannelVolume(tempchn, Max(Min((mainPlayer->heartbeatIntensity-80.0)/60.0,1.0),0.0)*userOptions->sndVolume);

        mainPlayer->heartbeatIntensity = mainPlayer->heartbeatIntensity - timing->tickDuration;
    }

    // Equipped items.
    if (IsPlayerWearingItem(mainPlayer, "gasmask")) {
        bbShowEntity(mainPlayer->overlays[OVERLAY_GASMASK]);
        //TODO: Add ElseIfs here for hazmat and nvgoggles.
    } else {
        bbHideEntity(mainPlayer->overlays[OVERLAY_GASMASK]);
    }

}

void MouseLook() {
    int i;
    float up;
    float roll;
    float the_yaw;
    float the_pitch;
    int collidedFloor;
    int pvt;
    Particle* p;

    mainPlayer->camShake = Max(mainPlayer->camShake - (timing->tickDuration / 10), 0);

    //CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.0)
    bbCameraZoom(mainPlayer->cam, Min(1.0+(mainPlayer->camZoom/400.0),1.1));
    mainPlayer->camZoom = Max(mainPlayer->camZoom - timing->tickDuration, 0);

    float Nan1 = NAN;

    if (mainPlayer->fallTimer >=0) {

        //HeadDropSpeed = 0

        up = (bbSin(mainPlayer->camAnimState) / (20.0+mainPlayer->crouchState*20.0))*0.6;
        roll = Max(Min(bbSin(mainPlayer->camAnimState*0.5)*2.5*Min(mainPlayer->injuries+0.25,3.0),8.0),-8.0);

        //tilt the camera to the side if the player is injured
        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), Max(Min(up*30*mainPlayer\injuries,50),-50))
        bbPositionEntity(mainPlayer->cam, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));
        bbRotateEntity(mainPlayer->cam, 0, bbEntityYaw(mainPlayer->collider), roll*0.5);

        bbMoveEntity(mainPlayer->cam, 0.0, up + 0.6 + mainPlayer->crouchState * -0.3, 0);

        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), 0)
        //moveentity player, side, up, 0
        // -- Update the smoothing que To smooth the movement of the mouse.
        mouse_x_speed_1 = CurveValue(bbMouseXSpeed() * (userOptions->mouseSensitivity + 0.6) , mouse_x_speed_1, 6.0 / (userOptions->mouseSensitivity + 1.0));
        if ((int)(mouse_x_speed_1) == (int)(Nan1)) {
            mouse_x_speed_1 = 0;
        }

        if (userOptions->invertMouseY) {
            mouse_y_speed_1 = CurveValue(-bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions->mouseSensitivity+1.0));
        } else {
            mouse_y_speed_1 = CurveValue(bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions->mouseSensitivity+1.0));
        }
        if ((int)(mouse_y_speed_1) == (int)(Nan1)) {
            mouse_y_speed_1 = 0;
        }

        //TODO: CHANGE THESE NAMES
        the_yaw = ((mouse_x_speed_1)) * mouselook_x_inc / (1.0+IsPlayerWearingItem(mainPlayer,"vest"));
        the_pitch = ((mouse_y_speed_1)) * mouselook_y_inc / (1.0+IsPlayerWearingItem(mainPlayer,"vest"));

        // Turn the user on the Y (yaw) axis.)
        bbTurnEntity(mainPlayer->collider, 0.0, -the_yaw, 0.0);
        mainPlayer->headPitch = mainPlayer->headPitch + the_pitch;
        // -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
        if (mainPlayer->headPitch > 70.0) {
            mainPlayer->headPitch = 70.0;
        }
        if (mainPlayer->headPitch < - 70.0) {
            mainPlayer->headPitch = -70.0;
        }

        // Pitch the user;s camera up And down.)
        bbRotateEntity(mainPlayer->cam, WrapAngle(mainPlayer->headPitch + bbRnd(-mainPlayer->camShake, mainPlayer->camShake)), WrapAngle(bbEntityYaw(mainPlayer->collider) + bbRnd(-mainPlayer->camShake, mainPlayer->camShake)), roll);

        if (mainPlayer->currRoom->roomTemplate->name == "pocketdimension") {
            if (bbEntityY(mainPlayer->collider)<2000*RoomScale | bbEntityY(mainPlayer->collider)>2608*RoomScale) {
                // Pitch the user;s camera up And down.)
                bbRotateEntity(mainPlayer->cam, WrapAngle(bbEntityPitch(mainPlayer->cam)),WrapAngle(bbEntityYaw(mainPlayer->cam)), roll+WrapAngle(bbSin(TimeInPosMilliSecs()/150.0)*30.0));
            }
        }

    } else {
        bbHideEntity(mainPlayer->collider);
        bbPositionEntity(mainPlayer->cam, bbEntityX(mainPlayer->head), bbEntityY(mainPlayer->head), bbEntityZ(mainPlayer->head));

        collidedFloor = false;
        for (i = 1; i <= bbCountCollisions(mainPlayer->head); i++) {
            if (bbCollisionY(mainPlayer->head, i) < bbEntityY(mainPlayer->head) - 0.01) {
                collidedFloor = true;
            }
        }

        if (collidedFloor == true) {
            //HeadDropSpeed# = 0
        } else {

            //TODO: reimplement head falling
            //If (KillAnim = 0) Then
            //	MoveEntity(Head, 0, 0, HeadDropSpeed)
            //	RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
            //	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
            //Else
            //	MoveEntity(Head, 0, 0, -HeadDropSpeed)
            //	RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
            //	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
            //EndIf

            //HeadDropSpeed# = HeadDropSpeed - 0.002 * timing\tickDuration
        }

        if (userOptions->invertMouseY) {
            bbTurnEntity(mainPlayer->cam, -bbMouseYSpeed() * 0.05 * timing->tickDuration, -bbMouseXSpeed() * 0.15 * timing->tickDuration, 0);
        } else {
            bbTurnEntity(mainPlayer->cam, bbMouseYSpeed() * 0.05 * timing->tickDuration, -bbMouseXSpeed() * 0.15 * timing->tickDuration, 0);
        }

    }

    //DUST PARTICLES
    if (bbRand(35) == 1) {
        pvt = bbCreatePivot();
        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam, true), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true));
        bbRotateEntity(pvt, 0, bbRnd(360), 0);
        if (bbRand(2) == 1) {
            bbMoveEntity(pvt, 0, bbRnd(-0.5, 0.5), bbRnd(0.5, 1.0));
        } else {
            bbMoveEntity(pvt, 0, bbRnd(-0.5, 0.5), bbRnd(0.5, 1.0));
        }

        p = CreateParticle(bbEntityX(pvt), bbEntityY(pvt), bbEntityZ(pvt), PARTICLE_DUST, 0.002, 0, 300);
        p->speed = 0.001;
        bbRotateEntity(p->pvt, bbRnd(-20, 20), bbRnd(360), 0);

        p->sizeChange = -0.00001;

        bbFreeEntity(pvt);
    }

    // -- Limit the mouse;s movement. Using this method produces smoother mouselook movement than centering the mouse Each loop.
    if (bbMouseX() > mouse_right_limit) | (bbMouseX() < mouse_left_limit) | (bbMouseY() > mouse_bottom_limit) | (bbMouseY() < mouse_top_limit) {
        bbMoveMouse(viewport_center_x, viewport_center_y);
    }

    //TODO: Move this to MovePlayer().
    //If (wearingGasMask Or wearingHazmat Or wearing1499) Then
    //	If (wearingGasMask = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
    //	If (wearing1499 = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
    //	If (wearingHazmat = 2) Then
    //		mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
    //	ElseIf ((wearingHazmat=1)) Then
    //		mainPlayer\stamina = Min(60, mainPlayer\stamina)
    //	EndIf
    //
    //	ShowEntity(mainPlayer\overlays[OVERLAY_GASMASK])
    //Else
    //	HideEntity(mainPlayer\overlays[OVERLAY_GASMASK])
    //EndIf

    //If (Not wearingNightVision=0) Then
    //	ShowEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
    //	If (wearingNightVision=2) Then
    //		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
    //		AmbientLightRooms(15)
    //	ElseIf ((wearingNightVision=3)) Then
    //		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,0,0)
    //		AmbientLightRooms(15)
    //	Else
    //		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,255,0)
    //		AmbientLightRooms(15)
    //	EndIf
    //	;EntityTexture(Fog, FogNVTexture)
    //Else
    //	AmbientLightRooms(0)
    //	HideEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
    //	;EntityTexture(Fog, FogTexture)
    //EndIf
}

int SpaceInInventory(Player* player) {
    int itemCount;
    int i;
    for (i = WORNITEM_SLOT_COUNT; i <= player->inventory->size-1; i++) {
        if (player->inventory->items[i] != nullptr) {
            itemCount = itemCount + 1;
        }
    }

    if (itemCount < player->inventory->size-WORNITEM_SLOT_COUNT) {
        return true;
    }

    return false;
}

void Kill(Player* player) {
    if (player->godMode) {
        return;
    }

    if (player->breathChn != 0) {
        if (IsChannelPlaying(player->breathChn)) {
            bbStopChannel(player->breathChn);
        }
    }

    if (!player->dead) {
        //KillAnim = Rand(0,1)
        PlaySound2(mainPlayer->damageSFX[0]);
        if (SelectedDifficulty->permaDeath) {
            bbDeleteFile(bbCurrentDir() + SavePath + CurrSave+"/save.txt");
            bbDeleteDir(SavePath + CurrSave);
        }

        player->dead = true;
        player->fallTimer = Min(-1,player->fallTimer);
        bbShowEntity(player->head);
        bbPositionEntity(player->head, bbEntityX(player->cam, true), bbEntityY(player->cam, true), bbEntityZ(player->cam, true), true);
        bbResetEntity(player->head);
        bbRotateEntity(player->head, 0, bbEntityYaw(player->cam), 0);
    }
}

}

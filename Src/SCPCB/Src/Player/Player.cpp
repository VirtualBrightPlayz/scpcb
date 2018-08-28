#include <bbaudio.h>
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <bbinput.h>
#include <bbmath.h>

#include "Player.h"
#include "../AssetMgmt/Audio.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../MiscGFX/Dreamfilter.h"
#include "../Map/Decals.h"
#include "../GameMain.h"
#include "../Items/Item.h"
#include "../Items/Inventory.h"
#include "../MathUtils/MathUtils.h"
#include "../Map/MapSystem.h"
#include "../Menus/Menu.h"
#include "../Config/Options.h"
#include "../Map/Particles.h"

namespace CBN {

// Structs.
Player::Player() {
    memset(this, 0, sizeof(Player));

    inventory = new Inventory(PLAYER_INV_COUNT);

    this->cam = bbCreateCamera();
    bbCameraViewport(this->cam, 0, 0, userOptions->screenWidth, userOptions->screenHeight);
    bbCameraRange(this->cam, 0.05f, 60);
    bbCameraFogMode(this->cam, 1);
    //TODO: use constants
    bbCameraFogRange(this->cam, 0.05f, 60);
    //TODO: Change tint based on zone?
    bbCameraFogColor(this->cam, 0, 0, 0);

    Texture* fogTexture = bbLoadTexture("GFX/Overlays/fog.jpg", 1);
    //FogNVTexture = LoadTexture("GFX/fogNV.jpg", 1)

    float scaleWidth = userOptions->screenWidth / 1024.f;
    float scaleHeight = MenuScale * 0.8f;

    //TODO: take ownership of ark_blur_cam
    Texture* gasMaskTexture = bbLoadTexture("GFX/Overlays/GasmaskOverlay.jpg", 1);
    this->overlays[OVERLAY_GASMASK] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(this->overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_GASMASK], gasMaskTexture);
    bbEntityBlend(this->overlays[OVERLAY_GASMASK], 2);
    bbEntityFX(this->overlays[OVERLAY_GASMASK], 1);
    bbEntityOrder(this->overlays[OVERLAY_GASMASK], -1003);
    bbMoveEntity(this->overlays[OVERLAY_GASMASK], 0, 0, 1.f);
    bbHideEntity(this->overlays[OVERLAY_GASMASK]);

    Texture* infectTexture = bbLoadTexture("GFX/Overlays/InfectOverlay.jpg", 1);
    this->overlays[OVERLAY_008] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(this->overlays[OVERLAY_008], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_008], infectTexture);
    bbEntityBlend(this->overlays[OVERLAY_008], 3);
    bbEntityFX(this->overlays[OVERLAY_008], 1);
    bbEntityOrder(this->overlays[OVERLAY_008], -1003);
    bbMoveEntity(this->overlays[OVERLAY_008], 0, 0, 1.f);
    //EntityAlpha(InfectOverlay, 255.f)
    bbHideEntity(this->overlays[OVERLAY_008]);

    Texture* nvTexture = bbLoadTexture("GFX/Overlays/NightVisionOverlay.jpg", 1);
    this->overlays[OVERLAY_NIGHTVISION] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(this->overlays[OVERLAY_NIGHTVISION], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_NIGHTVISION], nvTexture);
    bbEntityBlend(this->overlays[OVERLAY_NIGHTVISION], 2);
    bbEntityFX(this->overlays[OVERLAY_NIGHTVISION], 1);
    bbEntityOrder(this->overlays[OVERLAY_NIGHTVISION], -1003);
    bbMoveEntity(this->overlays[OVERLAY_NIGHTVISION], 0, 0, 1.f);
    bbHideEntity(this->overlays[OVERLAY_NIGHTVISION]);

    //TODO: do we need this?
    //NVBlink = CreateSprite(ark_blur_cam)
    //ScaleSprite(NVBlink, Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f))
    //EntityColor(NVBlink,0,0,0)
    //EntityFX(NVBlink, 1)
    //EntityOrder(NVBlink, -1005)
    //MoveEntity(NVBlink, 0, 0, 1.f)
    //HideEntity(NVBlink)

    Texture* darkTexture = bbCreateTexture(1024, 1024, 1 + 2);
    bbSetBuffer(bbTextureBuffer(darkTexture));
    bbCls();
    bbSetBuffer(bbBackBuffer());

    scaleWidth = userOptions->screenWidth / 1240.f;
    scaleHeight = userOptions->screenHeight / 960.f * 0.8f;

    this->overlays[OVERLAY_FOG] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(this->overlays[OVERLAY_FOG], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_FOG], fogTexture);
    bbEntityBlend(this->overlays[OVERLAY_FOG], 2);
    bbEntityOrder(this->overlays[OVERLAY_FOG], -1000);
    bbMoveEntity(this->overlays[OVERLAY_FOG], 0, 0, 1.f);

    this->overlays[OVERLAY_BLACK] = bbCreateSprite(this->cam);
    bbScaleSprite(this->overlays[OVERLAY_BLACK], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_BLACK], darkTexture);
    bbEntityBlend(this->overlays[OVERLAY_BLACK], 1);
    bbEntityOrder(this->overlays[OVERLAY_BLACK], -1002);
    bbMoveEntity(this->overlays[OVERLAY_BLACK], 0, 0, 1.f);
    bbEntityAlpha(this->overlays[OVERLAY_BLACK], 0.f);

    Texture* lightTexture = bbCreateTexture(1024, 1024, 1 + 2);
    bbSetBuffer(bbTextureBuffer(lightTexture));
    bbClsColor(255, 255, 255);
    bbCls();
    bbClsColor(0, 0, 0);
    bbSetBuffer(bbBackBuffer());

    this->overlays[OVERLAY_WHITE] = bbCreateSprite(this->cam);
    bbScaleSprite(this->overlays[OVERLAY_WHITE], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(this->overlays[OVERLAY_WHITE], lightTexture);
    bbEntityBlend(this->overlays[OVERLAY_WHITE], 1);
    bbEntityOrder(this->overlays[OVERLAY_WHITE], -1002);
    bbMoveEntity(this->overlays[OVERLAY_WHITE], 0, 0, 1.f);
    bbHideEntity(this->overlays[OVERLAY_WHITE]);

    bbFreeTexture(gasMaskTexture);
    bbFreeTexture(infectTexture);
    bbFreeTexture(nvTexture);
    bbFreeTexture(fogTexture);
    bbFreeTexture(darkTexture);
    bbFreeTexture(lightTexture);

    this->collider = bbCreatePivot();
    bbEntityRadius(this->collider, 0.15f, 0.30f);
    bbEntityPickMode(this->collider, 1);
    bbEntityType(this->collider, HIT_PLAYER);

    this->head = bbCreatePivot();
    bbEntityRadius(this->head, 0.15f);
    bbEntityType(this->head, HIT_PLAYER);

    //Sounds
    for (int i = 0; i < 5; i++) {
        this->breathingSFX[0][i] = bbLoadSound("SFX/Character/D9341/breath" + String(i) + ".ogg");
        this->breathingSFX[1][i] = bbLoadSound("SFX/Character/D9341/breath" + String(i) + "gas.ogg");
    }
    for (int i = 0; i < 4; i++) {
        this->bloodDripSFX[i] = bbLoadSound("SFX/Character/D9341/BloodDrip" + String(i) + ".ogg");
    }
    for (int i = 0; i < 9; i++) {
        this->damageSFX[i] = bbLoadSound("SFX/Character/D9341/Damage" + String(i + 1) + ".ogg");
    }
    for (int i = 0; i < 3; i++) {
        this->coughSFX[i] = bbLoadSound("SFX/Character/D9341/Cough" + String(i + 1) + ".ogg");
    }
    this->heartbeatSFX = bbLoadSound("SFX/Character/D9341/Heartbeat.ogg");
}

Player::~Player() {
    //TODO: delete/drop worn items, delete inventory
    delete inventory;

    for (int i = 0; i < OVERLAY_COUNT; i++) {
        bbFreeEntity(this->overlays[i]);
    }

    for (int i = 0; i < 5; i++) {
        bbFreeSound(this->breathingSFX[0][i]);
        bbFreeSound(this->breathingSFX[1][i]);
    }
    for (int i = 0; i < 4; i++) {
        bbFreeSound(this->bloodDripSFX[i]);
    }
    for (int i = 0; i < 9; i++) {
        bbFreeSound(this->damageSFX[i]);
    }
    for (int i = 0; i < 3; i++) {
        bbFreeSound(this->coughSFX[i]);
    }
    bbFreeSound(this->heartbeatSFX);
}

// Globals.
Player* mainPlayer = nullptr;
int viewport_center_x;
int viewport_center_y;
float mouselook_x_inc = 0.3f;
float mouselook_y_inc = 0.3f;
int mouse_left_limit;
int mouse_right_limit;
int mouse_top_limit;
int mouse_bottom_limit;
float mouse_x_speed_1;
float mouse_y_speed_1;

// Functions.
void Player::update() {
    // TODO: Remove.
    if (bbKeyHit(49)) {
        noclip = !noclip;
    }
    godMode = true;

    float Sprint = 1.f;
    float Speed = 0.018f;
    gxChannel* tempChn = nullptr;

    //If (DeathTimer > 0) Then
    //	DeathTimer=DeathTimer-timing\tickDuration
    //	If (DeathTimer < 1) Then DeathTimer = -1.f
    //ElseIf DeathTimer < 0
    //	Kill(mainPlayer)
    //EndIf

    stamina = Min(stamina + 0.15f * timing->tickDuration, 100.f);

    if (staminaEffectTimer > 0) {
        staminaEffect = staminaEffect - (timing->tickDuration/70);
    } else {
        staminaEffect = 1.f;
    }

    if (currRoom!=nullptr) {
        if (!currRoom->roomTemplate->name.equals("pocketdimension")) {
            if (bbKeyDown(keyBinds->sprint)) {
                //out of breath
                if (stamina < 5) {
                    if (!bbChannelPlaying(breathChn)) {
                        breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][0]);
                    }
                    //panting
                } else if ((stamina < 50)) {
                    if (breathChn == 0) {
                        breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][bbRand(1, 3)]);
                        bbChannelVolume(breathChn, Min((70.f-stamina)/70.f,1.f)*userOptions->sndVolume);
                    } else {
                        if (!bbChannelPlaying(breathChn)) {
                            breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][bbRand(1, 3)]);
                            bbChannelVolume(breathChn, Min((70.f-stamina)/70.f,1.f)*userOptions->sndVolume);
                        }
                    }
                }
            }
        }
    }

    //If (IsZombie) Then Crouch = False

    if (abs(crouchState-crouching)<0.001f) {
        crouchState = crouching;
    } else {
        crouchState = CurveValue(crouching, crouchState, 10.f);
    }

    if (!noclip) {
        if (((bbKeyDown(keyBinds->down) ^ bbKeyDown(keyBinds->up)) | (bbKeyDown(keyBinds->right) ^ bbKeyDown(keyBinds->left)) && (!disableControls)) || forceMove>0) {

            // And (Not IsZombie)) Then
            if (crouching == 0 && (bbKeyDown(keyBinds->sprint)) && stamina > 0.f) {
                Sprint = 2.5f;
                stamina = stamina - timing->tickDuration * 0.5f * (1.f/staminaEffect);
                if (stamina <= 0) {
                    stamina = -20.f;
                }
            }

            if (currRoom->roomTemplate->name.equals("pocketdimension")) {
                if (bbEntityY(collider)<2000*RoomScale || bbEntityY(collider)>2608*RoomScale) {
                    stamina = 0;
                    Speed = 0.015f;
                    Sprint = 1.f;
                }
            }

            if (forceMove>0) {
                Speed = Speed*forceMove;
            }

            float temp = modFloat(camAnimState, 360);
            if (!disableControls) {
                camAnimState = modFloat(camAnimState + timing->tickDuration * Min(Sprint, 1.5f) * 7, 720);
            }
            if (temp < 180 && modFloat(camAnimState, 360) >= 180 && !dead) {
                //TODO: define constants for each override state
                if (footstepOverride==0) {
                    temp = (float)GetMaterialStepSound(collider);

                    if (Sprint == 1.f) {
                        loudness = Max(4.f,loudness);

                        if (temp == STEPSOUND_METAL) {
                            tempChn = PlaySound_SM(sndMgmt->footstepMetal[bbRand(0, 7)]);
                        } else {
                            tempChn = PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
                        }

                        bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
                    } else {
                        loudness = Max(2.5f-(crouching*0.6f),loudness);

                        if (temp == 1) {
                            tempChn = PlaySound_SM(sndMgmt->footstepMetalRun[bbRand(0, 7)]);
                        } else {
                            tempChn = PlaySound_SM(sndMgmt->footstepRun[bbRand(0, 7)]);
                        }

                        bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
                    }
                } else if (footstepOverride==1) {
                    tempChn = PlaySound_SM(sndMgmt->footstepPD[bbRand(0, 2)]);
                    bbChannelVolume(tempChn, (1.f-(crouching*0.4f))*userOptions->sndVolume);
                } else if (footstepOverride==2) {
                    tempChn = PlaySound_SM(sndMgmt->footstep8601[bbRand(0, 2)]);
                    bbChannelVolume(tempChn, (1.f-(crouching*0.4f))*userOptions->sndVolume);
                } else if (footstepOverride==3) {
                    if (Sprint == 1.f) {
                        loudness = Max(4.f,loudness);
                        tempChn = PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
                        bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
                    } else {
                        loudness = Max(2.5f-(crouching*0.6f),loudness);
                        tempChn = PlaySound_SM(sndMgmt->footstepRun[bbRand(0, 7)]);
                        bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
                    }
                }

            }
        }
        //noclip on
    } else {
        if (bbKeyDown(keyBinds->sprint)) {
            Sprint = 2.5f;
        } else if ((bbKeyDown(keyBinds->crouch))) {
            Sprint = 0.5f;
        }
    }

    if (bbKeyHit(keyBinds->crouch) && (!disableControls)) {
        crouching = (!crouching);
    }

    float temp2 = (Speed * Sprint) / (1.f+crouchState);

    if (noclip) {
        camAnimState = 0;
        moveSpeed = 0;
        crouchState = 0;
        crouching = 0;

        bbRotateEntity(collider, WrapAngle(bbEntityPitch(cam)), WrapAngle(bbEntityYaw(cam)), 0);

        // * NoClipSpeed ;TODO: reimplement
        temp2 = temp2;

        if (bbKeyDown(keyBinds->down)) {
            bbMoveEntity(collider, 0, 0, -temp2*timing->tickDuration);
        }
        if (bbKeyDown(keyBinds->up)) {
            bbMoveEntity(collider, 0, 0, temp2*timing->tickDuration);
        }

        if (bbKeyDown(keyBinds->left)) {
            bbMoveEntity(collider, -temp2*timing->tickDuration, 0, 0);
        }
        if (bbKeyDown(keyBinds->right)) {
            bbMoveEntity(collider, temp2*timing->tickDuration, 0, 0);
        }

        bbResetEntity(collider);
    } else {
        temp2 = temp2 / Max((injuries+3.f)/3.f,1.f);
        if (injuries > 0.5f) {
            temp2 = temp2*Min((bbSin(camAnimState/2)+1.2f),1.f);
        }

        bool temp = false;
        float angle = 0.0f;
        //If (Not IsZombie%)
        if (bbKeyDown(keyBinds->down) && (!disableControls)) {
            temp = true;
            angle = 180;
            if (bbKeyDown(keyBinds->left)) {
                angle = 135;
            }
            if (bbKeyDown(keyBinds->right)) {
                angle = -135;
            }
            // Or ForceMove>0
        } else if ((bbKeyDown(keyBinds->up) && (!disableControls))) {
            temp = true;
            angle = 0;
            if (bbKeyDown(keyBinds->left)) {
                angle = 45;
            }
            if (bbKeyDown(keyBinds->right)) {
                angle = -45;
            }
        } else if ((forceMove>0)) {
            temp = true;
            angle = forceAngle;
        } else if ((!disableControls)) {
            if (bbKeyDown(keyBinds->left)) {
                angle = 90;
                temp = true;
            }
            if (bbKeyDown(keyBinds->right)) {
                angle = -90;
                temp = true;
            }
        }
        //Else
        //	temp=True
        //	angle = ForceAngle
        //EndIf

        angle = WrapAngle(bbEntityYaw(collider,true)+angle+90.f);

        if ((int)(temp)) {
            moveSpeed = CurveValue(temp2, moveSpeed, 20.f);
        } else {
            moveSpeed = Max(CurveValue(0.f, moveSpeed-0.1f, 1.f),0.f);
        }

        if (!disableControls) {
            bbTranslateEntity(collider, bbCos(angle)*moveSpeed * timing->tickDuration, 0, bbSin(angle)*moveSpeed * timing->tickDuration, true);
        }

        bool collidedFloor = false;
        for (int i = 1; i <= bbCountCollisions(collider); i++) {
            if (bbCollisionY(collider, i) < bbEntityY(collider,true) && abs(bbCollisionNY(collider, i))>0.8f) {
                collidedFloor = true;
            }
        }

        if (collidedFloor == true) {
            if (dropSpeed < - 0.07f) {
                if (footstepOverride==0) {
                    if (GetMaterialStepSound(collider) == 1) {
                        PlaySound_SM(sndMgmt->footstepMetal[bbRand(0, 7)]);
                    } else {
                        PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
                    }
                } else if ((footstepOverride==1)) {
                    PlaySound_SM(sndMgmt->footstepPD[bbRand(0, 2)]);
                } else if ((footstepOverride==2)) {
                    PlaySound_SM(sndMgmt->footstep8601[bbRand(0, 2)]);
                } else {
                    PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
                }
                loudness = Max(3.f,loudness);
            }
            dropSpeed = 0;
        } else {
            dropSpeed = Min(Max(dropSpeed - 0.006f * timing->tickDuration, -2.f), 0.f);
        }

        if (!disableControls) {
            bbTranslateEntity(collider, 0, dropSpeed * timing->tickDuration, 0);
        }
    }

    forceMove = 0.f;

    // if (injuries > 1.f) {
    //     temp2 = bloodloss;
    //     blurTimer = Max(Max(bbSin(TimeInPosMilliSecs()/100.f)*bloodloss*30.f,bloodloss*2*(2.f-crouchState)),blurTimer);
    //     bloodloss = Min(bloodloss + (Min(injuries,3.5f)/300.f)*timing->tickDuration,100);

    //     if (temp2 <= 60 && bloodloss > 60) {
    //         Msg = "You are feeling faint from the amount of blood you loss.";
    //         MsgTimer = 70*4;
    //     }
    // }

    UpdateInfect();

    if (bloodloss > 0) {
        if (bbRnd(200)<Min(injuries,4.f)) {
            Pivot* pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(collider)+bbRnd(-0.05f,0.05f),bbEntityY(collider)-0.05f,bbEntityZ(collider)+bbRnd(-0.05f,0.05f));
            bbTurnEntity(pvt, 90, 0, 0);
            bbEntityPick(pvt,0.3f);

            Decal* de = CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), bbPickedX(), bbPickedY()+0.005f, bbPickedZ(), 90.f, (float)bbRand(360), 0.f);
            de->size = bbRnd(0.03f,0.08f)*Min(injuries,3.f);
            bbEntityAlpha(de->obj, 1.f);
            bbScaleSprite(de->obj, de->size, de->size);
            tempChn = PlaySound2(bloodDripSFX[bbRand(0,3)]);
            bbChannelVolume(tempChn, bbRnd(0.f,0.8f)*userOptions->sndVolume);
            bbChannelPitch(tempChn, bbRand(20000,30000));

            bbFreeEntity(pvt);
        }

        camZoom = Max(camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.f)+1.f)*bloodloss*0.2f);

        if (bloodloss > 60) {
            crouching = true;
        }
        if (bloodloss >= 100) {
            Kill(mainPlayer);
            heartbeatIntensity = 0.f;
        } else if ((bloodloss > 80.f)) {
            heartbeatIntensity = Max(150-(bloodloss-80)*5,heartbeatIntensity);
        } else if ((bloodloss > 35.f)) {
            heartbeatIntensity = Max(70+bloodloss,heartbeatIntensity);
        }
    }

    if (!disableControls) {
        if (bbKeyHit(keyBinds->blink)) {
            blinkTimer = 0;
        }
        if (bbKeyDown(keyBinds->blink) && blinkTimer < - 10) {
            blinkTimer = -10;
        }
    }


    if (heartbeatIntensity > 0) {
        tempChn = PlaySound2(heartbeatSFX);
        bbChannelVolume(tempChn, Max(Min((heartbeatIntensity-80.f)/60.f,1.f),0.f)*userOptions->sndVolume);

        heartbeatIntensity -= timing->tickDuration;
    }

    // Equipped items.
    if (isEquipped("gasmask")) {
        bbShowEntity(overlays[OVERLAY_GASMASK]);
    } else {
        bbHideEntity(overlays[OVERLAY_GASMASK]);
    }

}

void MouseLook() {
    mainPlayer->camShake = Max(mainPlayer->camShake - (timing->tickDuration / 10), 0);

    //CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.f)
    bbCameraZoom(mainPlayer->cam, Min(1.f+(mainPlayer->camZoom/400.f),1.1f));
    mainPlayer->camZoom = Max(mainPlayer->camZoom - timing->tickDuration, 0);

    float Nan1 = NAN;

    if (mainPlayer->fallTimer >=0) {

        //HeadDropSpeed = 0

        float up = bbSin(mainPlayer->camAnimState) / (20.f+mainPlayer->crouchState*20.f)*0.6f;
        float roll = Max(Min(bbSin(mainPlayer->camAnimState*0.5f)*2.5f*Min(mainPlayer->injuries+0.25f,3.f),8.f),-8.f);

        //tilt the camera to the side if the player is injured
        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), Max(Min(up*30*mainPlayer\injuries,50),-50))
        bbPositionEntity(mainPlayer->cam, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));
        bbRotateEntity(mainPlayer->cam, 0, bbEntityYaw(mainPlayer->collider), roll*0.5f);

        bbMoveEntity(mainPlayer->cam, 0.f, up + 0.6f + mainPlayer->crouchState * -0.3f, 0);

        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), 0)
        //moveentity player, side, up, 0
        // -- Update the smoothing que To smooth the movement of the mouse.
        mouse_x_speed_1 = CurveValue(bbMouseXSpeed() * (userOptions->mouseSensitivity + 0.6f) , mouse_x_speed_1, 6.f / (userOptions->mouseSensitivity + 1.f));
        if ((int)(mouse_x_speed_1) == (int)(Nan1)) {
            mouse_x_speed_1 = 0;
        }

        if (userOptions->invertMouseY) {
            mouse_y_speed_1 = CurveValue(-bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6f), mouse_y_speed_1, 6.f/(userOptions->mouseSensitivity+1.f));
        } else {
            mouse_y_speed_1 = CurveValue(bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6f), mouse_y_speed_1, 6.f/(userOptions->mouseSensitivity+1.f));
        }
        if ((int)(mouse_y_speed_1) == (int)(Nan1)) {
            mouse_y_speed_1 = 0;
        }

        //TODO: CHANGE THESE NAMES
        float the_yaw = mouse_x_speed_1 * mouselook_x_inc;
        float the_pitch = mouse_y_speed_1 * mouselook_y_inc;

        // Turn the user on the Y (yaw) axis.)
        bbTurnEntity(mainPlayer->collider, 0.f, -the_yaw, 0.f);
        mainPlayer->headPitch = mainPlayer->headPitch + the_pitch;
        // -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
        if (mainPlayer->headPitch > 70.f) {
            mainPlayer->headPitch = 70.f;
        }
        if (mainPlayer->headPitch < - 70.f) {
            mainPlayer->headPitch = -70.f;
        }

        // Pitch the user's camera up And down.)
        bbRotateEntity(mainPlayer->cam, WrapAngle(mainPlayer->headPitch + bbRnd(-mainPlayer->camShake, mainPlayer->camShake)), WrapAngle(bbEntityYaw(mainPlayer->collider) + bbRnd(-mainPlayer->camShake, mainPlayer->camShake)), roll);

        if (mainPlayer->currRoom->roomTemplate->name.equals("pocketdimension")) {
            if (bbEntityY(mainPlayer->collider)<2000*RoomScale || bbEntityY(mainPlayer->collider)>2608*RoomScale) {
                // Pitch the user's camera up And down.)
                bbRotateEntity(mainPlayer->cam, WrapAngle(bbEntityPitch(mainPlayer->cam)),WrapAngle(bbEntityYaw(mainPlayer->cam)), roll+WrapAngle(bbSin(TimeInPosMilliSecs()/150.f)*30.f));
            }
        }

    } else {
        bbHideEntity(mainPlayer->collider);
        bbPositionEntity(mainPlayer->cam, bbEntityX(mainPlayer->head), bbEntityY(mainPlayer->head), bbEntityZ(mainPlayer->head));

        bool collidedFloor = false;
        for (int i = 1; i <= bbCountCollisions(mainPlayer->head); i++) {
            if (bbCollisionY(mainPlayer->head, i) < bbEntityY(mainPlayer->head) - 0.01f) {
                collidedFloor = true;
            }
        }

        if (collidedFloor) {
            //HeadDropSpeed# = 0
        } else {

            //TODO: reimplement head falling
            //If (KillAnim = 0) Then
            //	MoveEntity(Head, 0, 0, HeadDropSpeed)
            //	RotateEntity(Head, CurveAngle(-90.f, EntityPitch(Head), 20.f), EntityYaw(Head), EntityRoll(Head))
            //	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) - 40.f, EntityPitch(mainPlayer\cam), 40.f), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
            //Else
            //	MoveEntity(Head, 0, 0, -HeadDropSpeed)
            //	RotateEntity(Head, CurveAngle(90.f, EntityPitch(Head), 20.f), EntityYaw(Head), EntityRoll(Head))
            //	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) + 40.f, EntityPitch(mainPlayer\cam), 40.f), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
            //EndIf

            //HeadDropSpeed# = HeadDropSpeed - 0.002f * timing\tickDuration
        }

        if (userOptions->invertMouseY) {
            bbTurnEntity(mainPlayer->cam, -bbMouseYSpeed() * 0.05f * timing->tickDuration, -bbMouseXSpeed() * 0.15f * timing->tickDuration, 0);
        } else {
            bbTurnEntity(mainPlayer->cam, bbMouseYSpeed() * 0.05f * timing->tickDuration, -bbMouseXSpeed() * 0.15f * timing->tickDuration, 0);
        }

    }

    //DUST PARTICLES
    if (bbRand(35) == 1) {
        Pivot* pvt = bbCreatePivot();
        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam, true), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true));
        bbRotateEntity(pvt, 0, bbRnd(360), 0);
        if (bbRand(2) == 1) {
            bbMoveEntity(pvt, 0, bbRnd(-0.5f, 0.5f), bbRnd(0.5f, 1.f));
        } else {
            bbMoveEntity(pvt, 0, bbRnd(-0.5f, 0.5f), bbRnd(0.5f, 1.f));
        }

        Particle* p = CreateParticle(bbEntityX(pvt), bbEntityY(pvt), bbEntityZ(pvt), PARTICLE_DUST, 0.002f, 0, 300);
        p->speed = 0.001f;
        bbRotateEntity(p->pvt, bbRnd(-20, 20), bbRnd(360), 0);

        p->sizeChange = -0.00001f;

        bbFreeEntity(pvt);
    }

    // -- Limit the mouse;s movement. Using this method produces smoother mouselook movement than centering the mouse Each loop.
    if (bbMouseX() > mouse_right_limit || bbMouseX() < mouse_left_limit || bbMouseY() > mouse_bottom_limit || bbMouseY() < mouse_top_limit) {
        bbMoveMouse(viewport_center_x, viewport_center_y);
    }

    //TODO: Move this to MovePlayer().
    //If (wearingGasMask Or wearingHazmat Or wearing1499) Then
    //	If (wearingGasMask = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.f-mainPlayer\stamina)*0.01f*timing\tickDuration)
    //	If (wearing1499 = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.f-mainPlayer\stamina)*0.01f*timing\tickDuration)
    //	If (wearingHazmat = 2) Then
    //		mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.f-mainPlayer\stamina)*0.01f*timing\tickDuration)
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

void Player::toggleInventory() {
    if (CurrGameState == GAMESTATE_INVENTORY) {
        if (openInventory == inventory) {
            CurrGameState = GAMESTATE_PLAYING;
            ResumeSounds();
            bbMouseXSpeed();
            bbMouseYSpeed();
            bbMouseZSpeed();
            mouse_x_speed_1 = 0.f;
            mouse_y_speed_1 = 0.f;
        } else {
            openInventory = inventory;
        }
    } else {
        CurrGameState = GAMESTATE_INVENTORY;
        openInventory = inventory;
        PauseSounds();
    }

    mainPlayer->selectedItem = nullptr;
}

void Player::updateInventory() {
    openInventory->update();
    if (openInventory == inventory) {
        wornInventory->update();
    }
}

void Player::drawInventory() {
    openInventory->draw();
    if (openInventory == inventory) {
        wornInventory->draw();
    }
}

void Player::pickItem(Item* it) {
    if (!inventory->anyRoom()) {
        txtMgmt->setMsg(txtMgmt->lang["inv_full"]);
        return;
    }

    bbHideEntity(it->collider);
    inventory->addItem(it);
    PlaySound_SM(sndMgmt->itemPick[it->pickSound]);
    it->onPick();
}

void Player::useItem(Item* it) {
    // In the item is in an equip slot then unequip the item.
    if (it->parentInv == wornInventory) {
        it->parentInv->removeItem(it);
        inventory->addItem(it);
        PlaySound_SM(sndMgmt->itemPick[it->pickSound]);
    } else if (it->wornSlot != WornItemSlot::None) {
        // If this item is an equippable then equip it.
        int slot = (int)it->wornSlot;
        if (wornInventory->getItem(slot) != nullptr) {
            txtMgmt->setMsg(txtMgmt->lang["inv_alreadyequip"]);
            return;
        }

        it->parentInv->removeItem(it);
        wornInventory->setItem(it, slot);
        PlaySound_SM(sndMgmt->itemPick[it->pickSound]);
    }
    it->onUse();
}

void Player::dropItem(Item* it) {
    if (it->parentInv == wornInventory) {
        it->onUse(); // Has the de-equip message.
    }

    PlaySound_SM(sndMgmt->itemPick[it->pickSound]);
    it->parentInv->removeItem(it);
    it->parentInv = nullptr;

    bbShowEntity(it->collider);
    bbPositionEntity(it->collider, bbEntityX(cam), bbEntityY(cam), bbEntityZ(cam));
    bbRotateEntity(it->collider, bbEntityPitch(cam), bbEntityYaw(cam)+bbRnd(-20,20), 0);
    bbMoveEntity(it->collider, 0, -0.1f, 0.1f);
    bbRotateEntity(it->collider, 0, bbEntityYaw(cam)+bbRnd(-110,110), 0);

    bbResetEntity(it->collider);
}

void Player::moveItemToEmptySlot(Item* it, Inventory* to, int toIndex) {
    Inventory* from = it->parentInv;
    int fromIndex = from->getIndex(it);

    // Going from inv to equip slot? Check if it's ok.
    if (to == wornInventory) {
        WornItemSlot slot = (WornItemSlot)toIndex;
        if (slot != it->wornSlot) {
            txtMgmt->setMsg(txtMgmt->lang["inv_cantequip"]);
            return;
        }
    }

    // From equip slot to somewhere else?
    if (from == wornInventory) {
        it->onUse();
    }

    from->removeItem(it);
    to->setItem(it, toIndex);
}

bool Player::isEquipped(const String& itType) {
    for (int i = 0; i < wornInventory->getSize(); i++) {
        if (wornInventory->getItem(i)->getType().equals(itType)) {
            return true;
        }
    }
    return false;
}

void Kill(Player* player) {
    if (player->godMode) {
        return;
    }

    if (player->breathChn != 0) {
        if (bbChannelPlaying(player->breathChn)) {
            bbStopChannel(player->breathChn);
        }
    }

    if (!player->dead) {
        //KillAnim = Rand(0,1)
        PlaySound2(mainPlayer->damageSFX[0]);
        // TODO: Fix.
        // if (SelectedDifficulty->permaDeath) {
        //     bbDeleteFile(bbCurrentDir() + SavePath + CurrSave+"/save.txt");
        //     bbDeleteDir(SavePath + CurrSave);
        // }

        player->dead = true;
        player->fallTimer = Min(-1,player->fallTimer);
        bbShowEntity(player->head);
        bbPositionEntity(player->head, bbEntityX(player->cam, true), bbEntityY(player->cam, true), bbEntityZ(player->cam, true), true);
        bbResetEntity(player->head);
        bbRotateEntity(player->head, 0, bbEntityYaw(player->cam), 0);
    }
}

}

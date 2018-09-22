#include <bbaudio.h>
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <bbinput.h>
#include <bbmath.h>
#include <cmath>

#include "Player.h"
#include "../AssetMgmt/Audio.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../MiscGFX/Dreamfilter.h"
#include "../Map/Decals.h"
#include "../GameMain.h"
#include "../Items/Item.h"
#include "../Items/Inventory.h"
#include "../Items/GasMask.h"
#include "../MathUtils/MathUtils.h"
#include "../Map/MapSystem.h"
#include "../Menus/Menu.h"
#include "../Menus/PauseMenu.h"
#include "../Config/Options.h"
#include "../Map/Particles.h"

namespace CBN {

// TODO: Clean up.
Player::Player() {
    disableCameraControls = false;
    disableControls = false;

    moveAngle = 0.f;
    isMoving = false;
    isSprinting = false;
    crouched = false;
    speed = 0.f;
    speedMultiplier = 0.f;
    overlayBlackAlpha = 0.f;

    headPitch = 0.f;
    headYaw = 0.f;
    camShake = 0.f;
    camAnimState = 0.f;
    camZoom = 0.f;
    camFogNear = 0.f;
    camFogFar = 0.f;

    grabbedEntity = nullptr;
    closestItem = nullptr;
    selectedItem = nullptr;
    hoveredItemCell = nullptr;
    closestButton = nullptr;
    closestDoor = nullptr;
    selectedDoor = nullptr;

    crouching = false;
    crouchState = 0.f;
    moveSpeed = 0.f;
    dropSpeed = 0.f;

    blinkFreq = 0.f;
    blinkTimer = 0.f;
    stamina = 0.f;

    footstepOverride = 0;
    dead = false;
    fallTimer = 0.f;
    injuries = 0.f;
    bloodloss = 0.f;
    heartbeatIntensity = 0.f;
    infect008 = 0.f;
    sanity895 = 0.f;

    forceWalk = 0.f;
    forceAngle = 0.f;

    blinkEffect = 0.f;
    blinkEffectTimer = 0.f;
    staminaEffect = 0.f;
    staminaEffectTimer = 0.f;

    lightFlash = 0.f;
    blurTimer = 0.f;
    loudness = 0.f;

    breathChn = nullptr;
    currRoom = nullptr;

    godMode = false;
    noclip = false;

    inventory = new Inventory(PLAYER_INV_COUNT, 3);
    wornInventory = new Inventory(WORNITEM_SLOT_COUNT, WORNITEM_SLOT_COUNT);
    wornInventory->displayVertical = true;
    wornInventory->xOffset -= 600;
    openInventory = nullptr;

    cam = bbCreateCamera();
    bbCameraViewport(cam, 0, 0, userOptions->screenWidth, userOptions->screenHeight);
    bbCameraRange(cam, 0.05f, 60);
    bbCameraFogMode(cam, 1);
    //TODO: use constants
    bbCameraFogRange(cam, 0.05f, 60);
    //TODO: Change tint based on zone?
    bbCameraFogColor(cam, 0, 0, 0);

    Texture* fogTexture = bbLoadTexture("GFX/Overlays/fog.jpg", 1);
    //FogNVTexture = LoadTexture("GFX/fogNV.jpg", 1)

    // TODO: Re-scale these when the resolution changes.
    float scaleWidth = userOptions->screenWidth / 1024.f;
    float scaleHeight = MenuScale * 0.8f;

    //TODO: take ownership of ark_blur_cam
    Texture* gasMaskTexture = bbLoadTexture("GFX/Overlays/GasmaskOverlay.jpg", 1);
    overlays[OVERLAY_GASMASK] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(overlays[OVERLAY_GASMASK], gasMaskTexture);
    bbEntityBlend(overlays[OVERLAY_GASMASK], 2);
    bbEntityFX(overlays[OVERLAY_GASMASK], 1);
    bbEntityOrder(overlays[OVERLAY_GASMASK], -1003);
    bbTranslateEntity(overlays[OVERLAY_GASMASK], 0, 0, 1.f);
    bbHideEntity(overlays[OVERLAY_GASMASK]);

    Texture* infectTexture = bbLoadTexture("GFX/Overlays/InfectOverlay.jpg", 1);
    overlays[OVERLAY_008] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(overlays[OVERLAY_008], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(overlays[OVERLAY_008], infectTexture);
    bbEntityBlend(overlays[OVERLAY_008], 3);
    bbEntityFX(overlays[OVERLAY_008], 1);
    bbEntityOrder(overlays[OVERLAY_008], -1003);
    bbTranslateEntity(overlays[OVERLAY_008], 0, 0, 1.f);
    //EntityAlpha(InfectOverlay, 255.f)
    bbHideEntity(overlays[OVERLAY_008]);

    //Texture* nvTexture = bbLoadTexture("GFX/Overlays/NightVisionOverlay.jpg", 1);
    //overlays[OVERLAY_NIGHTVISION] = bbCreateSprite(ark_blur_cam);
    //bbScaleSprite(overlays[OVERLAY_NIGHTVISION], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    //bbEntityTexture(overlays[OVERLAY_NIGHTVISION], nvTexture);
    //bbEntityBlend(overlays[OVERLAY_NIGHTVISION], 2);
    //bbEntityFX(overlays[OVERLAY_NIGHTVISION], 1);
    //bbEntityOrder(overlays[OVERLAY_NIGHTVISION], -1003);
    //bbMoveEntity(overlays[OVERLAY_NIGHTVISION], 0, 0, 1.f);
    //bbHideEntity(overlays[OVERLAY_NIGHTVISION]);

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

    overlays[OVERLAY_FOG] = bbCreateSprite(ark_blur_cam);
    bbScaleSprite(overlays[OVERLAY_FOG], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(overlays[OVERLAY_FOG], fogTexture);
    bbEntityBlend(overlays[OVERLAY_FOG], 2);
    bbEntityOrder(overlays[OVERLAY_FOG], -1000);
    bbTranslateEntity(overlays[OVERLAY_FOG], 0, 0, 1.f);

    overlays[OVERLAY_BLACK] = bbCreateSprite(cam);
    bbScaleSprite(overlays[OVERLAY_BLACK], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(overlays[OVERLAY_BLACK], darkTexture);
    bbEntityBlend(overlays[OVERLAY_BLACK], 1);
    bbEntityOrder(overlays[OVERLAY_BLACK], -1002);
    bbTranslateEntity(overlays[OVERLAY_BLACK], 0, 0, 1.f);
    bbEntityAlpha(overlays[OVERLAY_BLACK], overlayBlackAlpha);

    Texture* lightTexture = bbCreateTexture(1024, 1024, 1 + 2);
    bbSetBuffer(bbTextureBuffer(lightTexture));
    bbClsColor(255, 255, 255);
    bbCls();
    bbClsColor(0, 0, 0);
    bbSetBuffer(bbBackBuffer());

    overlays[OVERLAY_WHITE] = bbCreateSprite(cam);
    bbScaleSprite(overlays[OVERLAY_WHITE], Max(scaleWidth, 1.f), Max(scaleHeight, 0.8f));
    bbEntityTexture(overlays[OVERLAY_WHITE], lightTexture);
    bbEntityBlend(overlays[OVERLAY_WHITE], 1);
    bbEntityOrder(overlays[OVERLAY_WHITE], -1002);
    bbTranslateEntity(overlays[OVERLAY_WHITE], 0, 0, 1.f);
    bbHideEntity(overlays[OVERLAY_WHITE]);

    bbFreeTexture(gasMaskTexture);
    bbFreeTexture(infectTexture);
    //bbFreeTexture(nvTexture);
    bbFreeTexture(fogTexture);
    bbFreeTexture(darkTexture);
    bbFreeTexture(lightTexture);

    collider = bbCreatePivot();
    bbEntityRadius(collider, 0.15f, 0.30f);
    bbEntityPickMode(collider, 1);
    bbEntityType(collider, HIT_PLAYER);

    head = bbCreatePivot();
    bbEntityRadius(head, 0.15f);
    bbEntityType(head, HIT_PLAYER);

    //Sounds
    for (int i = 0; i < 5; i++) {
        breathingSFX[0][i] = bbLoadSound("SFX/Character/D9341/breath" + String(i) + ".ogg");
        breathingSFX[1][i] = bbLoadSound("SFX/Character/D9341/breath" + String(i) + "gas.ogg");
    }
    for (int i = 0; i < 4; i++) {
        bloodDripSFX[i] = bbLoadSound("SFX/Character/D9341/BloodDrip" + String(i) + ".ogg");
    }
    for (int i = 0; i < 9; i++) {
        damageSFX[i] = bbLoadSound("SFX/Character/D9341/Damage" + String(i + 1) + ".ogg");
    }
    for (int i = 0; i < 3; i++) {
        coughSFX[i] = bbLoadSound("SFX/Character/D9341/Cough" + String(i + 1) + ".ogg");
    }
    heartbeatSFX = bbLoadSound("SFX/Character/D9341/Heartbeat.ogg");
}

Player::~Player() {
    delete inventory;
    delete wornInventory;

    for (int i = 0; i < OVERLAY_COUNT; i++) {
        bbFreeEntity(overlays[i]);
    }

    for (int i = 0; i < 5; i++) {
        bbFreeSound(breathingSFX[0][i]);
        bbFreeSound(breathingSFX[1][i]);
    }
    for (int i = 0; i < 4; i++) {
        bbFreeSound(bloodDripSFX[i]);
    }
    for (int i = 0; i < 9; i++) {
        bbFreeSound(damageSFX[i]);
    }
    for (int i = 0; i < 3; i++) {
        bbFreeSound(coughSFX[i]);
    }
    bbFreeSound(heartbeatSFX);
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
    isMoving = false;
    isSprinting = false;
    speedMultiplier = 1.f;
    speed = DEFAULT_SPEED;
    overlayBlackAlpha = 0.f;

    if (!disableCameraControls) {
        updateMouseInput();
    }
    mouseLook();


	if (!disableControls) {
		updateInput();
	}

    if (!noclip) {
        updateMove();
    } else {
        updateNoClip();
    }

    updateBlink();
    updateOverlays();
    updateItemUse();
}

void Player::updateInput() {
    // TODO: Remove.
    if (bbKeyHit(49)) {
        noclip = !noclip;
    }
    if (bbKeyHit(34)) {
        GasMask::spawn(bbEntityX(collider), bbEntityY(cam), bbEntityZ(collider));
    }

    // Moving.
    if (bbKeyDown(keyBinds->down)) {
        isMoving = true;

        if (bbKeyDown(keyBinds->left)) {
            moveAngle = 135;
        } else if (bbKeyDown(keyBinds->right)) {
            moveAngle = -135;
        } else {
            moveAngle = 180;
        }
    } else if (bbKeyDown(keyBinds->up)) {
        isMoving = true;
        moveAngle = 0;
        if (bbKeyDown(keyBinds->left)) {
            moveAngle = 45;
        } else if (bbKeyDown(keyBinds->right)) {
            moveAngle = -45;
        }
    } else if (forceWalk > 0) {
        isMoving = true;
        moveAngle = forceAngle;
    } else {
        if (bbKeyDown(keyBinds->left)) {
            moveAngle = 90;
            isMoving = true;
        }
        if (bbKeyDown(keyBinds->right)) {
            moveAngle = -90;
            isMoving = true;
        }
    }

    if ((bbKeyDown(keyBinds->down) ^ bbKeyDown(keyBinds->up)) | (bbKeyDown(keyBinds->right) ^ bbKeyDown(keyBinds->left))) {
        if (!crouching && (bbKeyDown(keyBinds->sprint)) && stamina > 0.f) {
            isSprinting = true;
        }

        // TODO: Re-implement.
        // if (currRoom->roomTemplate->name.equals("pocketdimension")) {
        //     if (bbEntityY(collider)<2000*RoomScale || bbEntityY(collider)>2608*RoomScale) {
        //         stamina = 0;
        //         Speed = 0.015f;
        //         Sprint = 1.f;
        //     }
        // }

        // TODO: Move somewhere more relevant.
        float temp = modFloat(camAnimState, 360);
        if (!disableControls) {
            camAnimState = modFloat(camAnimState + timing->tickDuration * Min(speedMultiplier, 1.5f) * 7, 720);
        }
        // if (temp < 180 && modFloat(camAnimState, 360) >= 180 && !dead) {
        //     //TODO: define constants for each override state
        //     if (footstepOverride==0) {
        //         temp = (float)GetMaterialStepSound(collider);

        //         if (Sprint == 1.f) {
        //             loudness = Max(4.f,loudness);

        //             if (temp == STEPSOUND_METAL) {
        //                 tempChn = PlaySound_SM(sndMgmt->footstepMetal[bbRand(0, 7)]);
        //             } else {
        //                 tempChn = PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
        //             }

        //             bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
        //         } else {
        //             loudness = Max(2.5f-(crouching*0.6f),loudness);

        //             if (temp == 1) {
        //                 tempChn = PlaySound_SM(sndMgmt->footstepMetalRun[bbRand(0, 7)]);
        //             } else {
        //                 tempChn = PlaySound_SM(sndMgmt->footstepRun[bbRand(0, 7)]);
        //             }

        //             bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
        //         }
        //     } else if (footstepOverride==1) {
        //         tempChn = PlaySound_SM(sndMgmt->footstepPD[bbRand(0, 2)]);
        //         bbChannelVolume(tempChn, (1.f-(crouching*0.4f))*userOptions->sndVolume);
        //     } else if (footstepOverride==2) {
        //         tempChn = PlaySound_SM(sndMgmt->footstep8601[bbRand(0, 2)]);
        //         bbChannelVolume(tempChn, (1.f-(crouching*0.4f))*userOptions->sndVolume);
        //     } else if (footstepOverride==3) {
        //         if (Sprint == 1.f) {
        //             loudness = Max(4.f,loudness);
        //             tempChn = PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
        //             bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
        //         } else {
        //             loudness = Max(2.5f-(crouching*0.6f),loudness);
        //             tempChn = PlaySound_SM(sndMgmt->footstepRun[bbRand(0, 7)]);
        //             bbChannelVolume(tempChn, (1.f-(crouching*0.6f))*userOptions->sndVolume);
        //         }
        //     }

        // }
    }

    // Blinking.
    if (bbKeyHit(keyBinds->blink)) {
        blinkTimer = 0;
    }
    if (bbKeyDown(keyBinds->blink) && blinkTimer < - 10) {
        blinkTimer = -10;
    }

    if (bbKeyHit(keyBinds->crouch)) {
        crouching = !crouching;
    }
}

void Player::updateMouseInput() {
    // -- Update the smoothing que To smooth the movement of the mouse.
    mouse_x_speed_1 = CurveValue(bbMouseXSpeed() * (userOptions->mouseSensitivity + 0.6f) , mouse_x_speed_1, 6.f / (userOptions->mouseSensitivity + 1.f));
    if ((int)(mouse_x_speed_1) == (int)(NAN)) {
        mouse_x_speed_1 = 0;
    }

    if (userOptions->invertMouseY) {
        mouse_y_speed_1 = CurveValue(-bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6f), mouse_y_speed_1, 6.f/(userOptions->mouseSensitivity+1.f));
    } else {
        mouse_y_speed_1 = CurveValue(bbMouseYSpeed() * (userOptions->mouseSensitivity + 0.6f), mouse_y_speed_1, 6.f/(userOptions->mouseSensitivity+1.f));
    }
    if ((int)(mouse_y_speed_1) == (int)(NAN)) {
        mouse_y_speed_1 = 0;
    }
}

void Player::updateMove() {
	if (isSprinting) {
		speedMultiplier = 2.5f;
		stamina -= timing->tickDuration * 0.5f * (1.f / staminaEffect);
		if (stamina <= 0) {
			stamina = -20.f;
		}
	} else {
		stamina = Min(stamina + 0.15f * timing->tickDuration, 100.f);
	}

    if (staminaEffectTimer > 0) {
        staminaEffect = staminaEffect - (timing->tickDuration/70);
    } else {
        staminaEffect = 1.f;
    }

    if (forceWalk > 0) {
        speed *= forceWalk;
    }

    // TODO: Re-implement.
    // if (isSprinting) {
    //     //out of breath
    //     if (stamina < 5) {
    //         if (!bbChannelPlaying(breathChn)) {
    //             breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][0]);
    //         }
    //     } else if (stamina < 50) {
    //         //panting
    //         if (breathChn == nullptr) {
    //             breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][bbRand(1, 3)]);
    //             bbChannelVolume(breathChn, Min((70.f-stamina)/70.f,1.f)*userOptions->sndVolume);
    //         } else {
    //             if (!bbChannelPlaying(breathChn)) {
    //                 breathChn = bbPlaySound(breathingSFX[isEquipped("gasmask")][bbRand(1, 3)]);
    //                 bbChannelVolume(breathChn, Min((70.f-stamina)/70.f,1.f)*userOptions->sndVolume);
    //             }
    //         }
    //     }
    // }

    // Transitioning from standing <-> crouching.
    if (abs(crouchState-crouching)<0.001f) {
        crouchState = crouching;
    } else {
        crouchState = CurveValue(crouching, crouchState, 10.f);
    }

    // Moving.
    float finalTranslation = (speed * speedMultiplier) / (1.f+crouchState);

    // TODO: Move to updateInjuries().
    finalTranslation /= Max((injuries+3.f)/3.f,1.f);
    if (injuries > 0.5f) {
        finalTranslation *= Min((bbSin(camAnimState/2)+1.2f),1.f);
    }

    moveAngle = WrapAngle(bbEntityYaw(collider,true)+moveAngle+90.f);

    if (isMoving) {
        moveSpeed = CurveValue(finalTranslation, moveSpeed, 20.f);
    } else {
        moveSpeed = Max(CurveValue(0.f, moveSpeed-0.1f, 1.f),0.f);
    }

    bbTranslateEntity(collider, bbCos(moveAngle)*moveSpeed * timing->tickDuration, 0, bbSin(moveAngle)*moveSpeed * timing->tickDuration, true);

    forceWalk = 0.f;

    // Floor collision.
	bool collidedWithFloor = false;
	for (int i = 1; i <= bbCountCollisions(collider); i++) {
		if (bbCollisionY(collider, i) < bbEntityY(collider, true) && abs(bbCollisionNY(collider, i)) > 0.8f) {
			collidedWithFloor = true;
		}
	}

	if (collidedWithFloor) {
        if (dropSpeed < - 0.07f) {
			// TODO: Re-implement.
            //if (footstepOverride==0) {
            //    if (GetMaterialStepSound(collider) == 1) {
            //        PlaySound_SM(sndMgmt->footstepMetal[bbRand(0, 7)]);
            //    } else {
            //        PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
            //    }
            //} else if ((footstepOverride==1)) {
            //    PlaySound_SM(sndMgmt->footstepPD[bbRand(0, 2)]);
            //} else if ((footstepOverride==2)) {
            //    PlaySound_SM(sndMgmt->footstep8601[bbRand(0, 2)]);
            //} else {
            //    PlaySound_SM(sndMgmt->footstep[bbRand(0, 7)]);
            //}
            loudness = Max(3.f,loudness);
        }
        dropSpeed = 0;
    } else {
        dropSpeed = Min(Max(dropSpeed - 0.006f * timing->tickDuration, -2.f), 0.f);
    }

    bbTranslateEntity(collider, 0, dropSpeed * timing->tickDuration, 0);
}

void Player::updateNoClip() {
    speed = 1.f;
    if (bbKeyDown(keyBinds->sprint)) {
        speed = 2.5f;
	} else if (bbKeyDown(keyBinds->crouch)) {
		speed = 0.5f;
	}

    camAnimState = 0;
    moveSpeed = 0;
    crouchState = 0;
    crouching = 0;
	stamina = 100.f;

    bbRotateEntity(collider, WrapAngle(bbEntityPitch(cam)), WrapAngle(bbEntityYaw(cam)), 0);

    // speed *= NoClipSpeed ;TODO: reimplement

    speed *= DEFAULT_SPEED * timing->tickDuration;
    if (bbKeyDown(keyBinds->down)) {
        bbMoveEntity(collider, 0, 0, -speed);
    }
    if (bbKeyDown(keyBinds->up)) {
        bbMoveEntity(collider, 0, 0, speed);
    }

    if (bbKeyDown(keyBinds->left)) {
        bbMoveEntity(collider, -speed, 0, 0);
    }
    if (bbKeyDown(keyBinds->right)) {
        bbMoveEntity(collider, speed, 0, 0);
    }

    bbResetEntity(collider);
}

void Player::mouseLook() {
    camShake = Max(camShake - (timing->tickDuration / 10), 0);

    //CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.f)
    bbCameraZoom(cam, Min(1.f+(camZoom/400.f),1.1f));
    camZoom = Max(camZoom - timing->tickDuration, 0);

    if (fallTimer >=0) {

        //HeadDropSpeed = 0

        float up = bbSin(camAnimState) / (20.f+crouchState*20.f)*0.6f;
        float roll = Max(Min(bbSin(camAnimState*0.5f)*2.5f*Min(injuries+0.25f,3.f),8.f),-8.f);

        //tilt the camera to the side if the player is injured
        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), Max(Min(up*30*mainPlayer\injuries,50),-50))
        bbPositionEntity(cam, bbEntityX(collider), bbEntityY(collider), bbEntityZ(collider));
        bbRotateEntity(cam, 0, bbEntityYaw(collider), roll*0.5f);

        bbMoveEntity(cam, 0.f, up + 0.6f + crouchState * -0.3f, 0);

        //RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), 0)
        //moveentity player, side, up, 0

        //TODO: CHANGE THESE NAMES
        float the_yaw = mouse_x_speed_1 * mouselook_x_inc;
        float the_pitch = mouse_y_speed_1 * mouselook_y_inc;

        // Turn the user on the Y (yaw) axis.)
        bbTurnEntity(collider, 0.f, -the_yaw, 0.f);
        headPitch += the_pitch;
        // -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
        if (headPitch > 70.f) {
            headPitch = 70.f;
        }
        if (headPitch < - 70.f) {
            headPitch = -70.f;
        }

        // Pitch the user's camera up And down.)
        bbRotateEntity(cam, WrapAngle(headPitch + bbRnd(-camShake, camShake)), WrapAngle(bbEntityYaw(collider) + bbRnd(-camShake, camShake)), roll);

        // TODO: Re-implement.
        // if (currRoom->roomTemplate->name.equals("pocketdimension")) {
        //     if (bbEntityY(collider)<2000*RoomScale || bbEntityY(collider)>2608*RoomScale) {
        //         // Pitch the user's camera up And down.)
        //         bbRotateEntity(cam, WrapAngle(bbEntityPitch(cam)),WrapAngle(bbEntityYaw(cam)), roll+WrapAngle(bbSin(TimeInPosMilliSecs()/150.f)*30.f));
        //     }
        // }

    } else {
        bbHideEntity(collider);
        bbPositionEntity(cam, bbEntityX(head), bbEntityY(head), bbEntityZ(head));

        bool collidedWithFloor = false;
        for (int i = 1; i <= bbCountCollisions(mainPlayer->head); i++) {
            if (bbCollisionY(mainPlayer->head, i) < bbEntityY(mainPlayer->head) - 0.01f) {
				collidedWithFloor = true;
            }
        }

        if (collidedWithFloor) {
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
            bbTurnEntity(cam, -bbMouseYSpeed() * 0.05f * timing->tickDuration, -bbMouseXSpeed() * 0.15f * timing->tickDuration, 0);
        } else {
            bbTurnEntity(cam, bbMouseYSpeed() * 0.05f * timing->tickDuration, -bbMouseXSpeed() * 0.15f * timing->tickDuration, 0);
        }

    }

    //DUST PARTICLES
    if (bbRand(35) == 1) {
        Pivot* pvt = bbCreatePivot();
        bbPositionEntity(pvt, bbEntityX(cam, true), bbEntityY(cam, true), bbEntityZ(cam, true));
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

void Player::updateBlink() {
    if (blinkTimer < 0) {
        if (blinkTimer > - 5) {
            overlayBlackAlpha = Max(overlayBlackAlpha, bbSin(abs(blinkTimer * 18.f)));
        } else if (blinkTimer > - 15) {
            overlayBlackAlpha = 1.f;
        } else {
            overlayBlackAlpha = Max(overlayBlackAlpha, abs(bbSin(blinkTimer * 18.f)));
        }

        if (blinkTimer <= - 20) {
            //Randomizes the frequency of blinking.
            // TODO: Scales with difficulty.
            blinkFreq = bbRnd(490,700);
            blinkTimer = blinkFreq;
        }

        blinkTimer -= timing->tickDuration;
    } else {
        blinkTimer -= timing->tickDuration * 0.6f * blinkEffect;
        //TODO: fix
        //If (EyeIrritation > 0) Then mainPlayer\blinkTimer=BlinkTimer-Min(EyeIrritation / 100.f + 1.f, 4.f) * timing\tickDuration

        overlayBlackAlpha = Max(overlayBlackAlpha, 0.f);
    }

    //TODO: fix
    //EyeIrritation = Max(0, EyeIrritation - timing\tickDuration)

    //TODO: fix
    //			If (EyeStuck > 0) Then
    //				mainPlayer\blinkTimer = mainPlayer\blinkFreq
    //				EyeStuck = Max(EyeStuck-timing\tickDuration,0)
    //
    //				If (EyeStuck < 9000) Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (9000-EyeStuck)*0.5f)
    //				If (EyeStuck < 6000) Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.f),1.f)
    //				If (EyeStuck < 9000 And EyeStuck+timing\tickDuration =>9000) Then
    //					Msg = "The eyedrops are causing your eyes to tear up."
    //					MsgTimer = 70*6
    //				EndIf
    //			EndIf

    if (blinkEffectTimer > 0) {
        blinkEffect -= (timing->tickDuration / 70);
    } else {
        blinkEffect = 1.f;
    }
}

void Player::updateOverlays() {
    bbEntityAlpha(overlays[OVERLAY_BLACK], overlayBlackAlpha);

    if (isEquipped("gasmask")) {
        bbShowEntity(overlays[OVERLAY_GASMASK]);
    } else {
        bbHideEntity(overlays[OVERLAY_GASMASK]);
    }

    //TODO: reimplement, move to Player struct.
    //LightBlink = Max(LightBlink - (timing\tickDuration / 35.f), 0)
    //If (LightBlink > 0) Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3f, 0.8f)), 1.f)
}

void Player::updateItemUse() {
    for (int i = 0; i < wornInventory->getSize(); i++) {
        Item* it = wornInventory->getItem(i);
        if (it != nullptr) {
            it->updateUse();
        }
    }
}

// TODO: Re-implement.
void Player::updateInjuries() {
    UpdateInfect();

    // if (injuries > 1.f) {
    //     temp2 = bloodloss;
    //     blurTimer = Max(Max(bbSin(TimeInPosMilliSecs()/100.f)*bloodloss*30.f,bloodloss*2*(2.f-crouchState)),blurTimer);
    //     bloodloss = Min(bloodloss + (Min(injuries,3.5f)/300.f)*timing->tickDuration,100);

    //     if (temp2 <= 60 && bloodloss > 60) {
    //         Msg = "You are feeling faint from the amount of blood you loss.";
    //         MsgTimer = 70*4;
    //     }
    // }

    // if (bloodloss > 0) {
    //     if (bbRnd(200)<Min(injuries,4.f)) {
    //         Pivot* pvt = bbCreatePivot();
    //         bbPositionEntity(pvt, bbEntityX(collider)+bbRnd(-0.05f,0.05f),bbEntityY(collider)-0.05f,bbEntityZ(collider)+bbRnd(-0.05f,0.05f));
    //         bbTurnEntity(pvt, 90, 0, 0);
    //         bbEntityPick(pvt,0.3f);

    //         Decal* de = CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), bbPickedX(), bbPickedY()+0.005f, bbPickedZ(), 90.f, (float)bbRand(360), 0.f);
    //         de->size = bbRnd(0.03f,0.08f)*Min(injuries,3.f);
    //         bbEntityAlpha(de->obj, 1.f);
    //         bbScaleSprite(de->obj, de->size, de->size);
    //         tempChn = PlaySound2(bloodDripSFX[bbRand(0,3)]);
    //         bbChannelVolume(tempChn, bbRnd(0.f,0.8f)*userOptions->sndVolume);
    //         bbChannelPitch(tempChn, bbRand(20000,30000));

    //         bbFreeEntity(pvt);
    //     }

    //     camZoom = Max(camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.f)+1.f)*bloodloss*0.2f);

    //     if (bloodloss > 60) {
    //         crouching = true;
    //     }
    //     if (bloodloss >= 100) {
    //         kill();
    //         heartbeatIntensity = 0.f;
    //     } else if (bloodloss > 80.f) {
    //         heartbeatIntensity = Max(150-(bloodloss-80)*5,heartbeatIntensity);
    //     } else if (bloodloss > 35.f) {
    //         heartbeatIntensity = Max(70+bloodloss,heartbeatIntensity);
    //     }
    // }

    // TODO: Re-implement.
    // if (heartbeatIntensity > 0) {
    //     tempChn = PlaySound2(heartbeatSFX);
    //     bbChannelVolume(tempChn, Max(Min((heartbeatIntensity-80.f)/60.f,1.f),0.f)*userOptions->sndVolume);

    //     heartbeatIntensity -= timing->tickDuration;
    // }
}

void Player::update895Sanity() {
    if (sanity895 < 0) {
        sanity895 = Min(sanity895 + timing->tickDuration, 0.f);
        if (sanity895 < (-200)) {
            overlayBlackAlpha = Max(Min((-sanity895 - 200) / 700.f, 0.6f), overlayBlackAlpha);
            if (!dead) {
                //HeartBeatVolume = Min(abs(mainPlayer\sanity895+200)/500.f,1.f)
                heartbeatIntensity = Max(70 + abs(sanity895+200)/6.f, heartbeatIntensity);
            }
        }
    }
}

void Player::updateDeathAnim() {
    if (dead) {
        CurrGameState = GAMESTATE_PLAYING;
        selectedItem = nullptr;
        SelectedScreen = nullptr;
        SelectedMonitor = nullptr;
        //mainPlayer\blurTimer = abs(mainPlayer\fallTimer*5)
        //mainPlayer\fallTimer=mainPlayer\fallTimer-(timing\tickDuration*0.8f)
        if (fallTimer < - 360) {
            CurrGameState = GAMESTATE_PAUSED;
            pauseMenu->currState = PauseMenuState::Dead;
            //TODO: fix
            //If (SelectedEnding <> "") Then EndingTimer = Min(mainPlayer\fallTimer,-0.1f)
        }
    }

    if (fallTimer < 0) {
        CurrGameState = GAMESTATE_PLAYING;
        selectedItem = nullptr;
        blurTimer = std::abs(fallTimer*10);
        fallTimer -= timing->tickDuration;
        overlayBlackAlpha = Max(overlayBlackAlpha, Min(abs(fallTimer / 400.f), 1.f));
    }
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

    selectedItem = nullptr;
}

void Player::updateInventory() {
    hoveredItemCell = nullptr;
    openInventory->update();
    if (openInventory == inventory) {
        wornInventory->update();
    }

    // if the mouse was released outside a slot, drop the item.
    if (MouseUp1 && selectedItem != nullptr && hoveredItemCell == nullptr) {
        dropItem(selectedItem);
        selectedItem = nullptr;
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
    it->dropSpeed = 0.f;
    inventory->addItem(it);
    PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
    it->onPick();
}

void Player::useItem(Item* it) {
    // In the item is in an equip slot then unequip the item.
    if (it->parentInv == wornInventory) {
        it->parentInv->removeItem(it);
        inventory->addItem(it);
        PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
    } else if (it->wornSlot != WornItemSlot::None) {
        // If this item is an equippable then equip it.
        int slot = (int)it->wornSlot;
        if (wornInventory->getItem(slot) != nullptr) {
            txtMgmt->setMsg(txtMgmt->lang["inv_alreadyequip"]);
            return;
        }

        it->parentInv->removeItem(it);
        wornInventory->setItem(it, slot);
        PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
    }
    it->onUse();
}

void Player::dropItem(Item* it) {
    bool wasEquipped = it->parentInv == wornInventory;
    PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
    it->parentInv->removeItem(it);

    if (wasEquipped) {
        it->onUse(); // Has the de-equip message.
    }

    bbShowEntity(it->collider);
    bbPositionEntity(it->collider, bbEntityX(cam), bbEntityY(cam), bbEntityZ(cam));
    bbRotateEntity(it->collider, bbEntityPitch(cam), bbEntityYaw(cam)+bbRnd(-20,20), 0);
    bbMoveEntity(it->collider, 0, -0.1f, 0.1f);
    bbRotateEntity(it->collider, 0, bbEntityYaw(cam)+bbRnd(-110,110), 0);

    bbResetEntity(it->collider);
    it->dropSpeed = 0.f;
}

void Player::moveItemToEmptySlot(Item* it, Inventory* to, int toIndex) {
    Inventory* from = it->parentInv;
    int fromIndex = from->getIndex(it);

    // Going from inv to equip slot? Check if it's ok.
    if (to == wornInventory && (WornItemSlot)toIndex != it->wornSlot) {
        txtMgmt->setMsg(txtMgmt->lang["inv_cantequip"]);
        return;
    }

    from->removeItem(it);
    to->setItem(it, toIndex);

    if (to == wornInventory || from == wornInventory) {
        PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
        it->onUse(); // Has the equip messages.
    }
}

bool Player::isEquipped(const String& itType) {
    for (int i = 0; i < wornInventory->getSize(); i++) {
        if (wornInventory->getItem(i) != nullptr && wornInventory->getItem(i)->getType().equals(itType)) {
            return true;
        }
    }
    return false;
}

void Player::kill() {
    if (godMode) {
        return;
    }

    if (breathChn != 0) {
        if (bbChannelPlaying(breathChn)) {
            bbStopChannel(breathChn);
        }
    }

    if (!dead) {
        //KillAnim = Rand(0,1)
        PlaySound2(damageSFX[0]);
        // TODO: Fix.
        // if (SelectedDifficulty->permaDeath) {
        //     bbDeleteFile(bbCurrentDir() + SavePath + CurrSave+"/save.txt");
        //     bbDeleteDir(SavePath + CurrSave);
        // }

        dead = true;
        fallTimer = Min(-1, fallTimer);
        bbShowEntity(head);
        bbPositionEntity(head, bbEntityX(cam, true), bbEntityY(cam, true), bbEntityZ(cam, true), true);
        bbResetEntity(head);
        bbRotateEntity(head, 0, bbEntityYaw(cam), 0);
    }
}

}

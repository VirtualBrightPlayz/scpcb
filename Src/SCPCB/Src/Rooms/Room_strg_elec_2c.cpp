#include <bbblitz3d.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_strg_elec_2c.h"

namespace CBN {

// Functions.
void FillRoom_strg_elec_2c(Room* r) {
    Door* d;
    Door* d2;
    SecurityCam* sc;
    Decal* de;
    Room* r2;
    SecurityCam* sc2;
    Item* it;
    int i;
    int xtemp;
    int ytemp;
    int ztemp;

    //, Bump
    int t1;

    d = CreateDoor(r->x + 64.f * RoomScale, 0.f, r->z + 368.f * RoomScale, 180, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    it = CreatePaper("docdan", r->x-400.f*RoomScale,1040.f*RoomScale,r->z+115.f*RoomScale);
    bbEntityParent(it->collider, r->obj);

    int n;
    for (n = 0; n <= 2; n++) {
        r->levers[n] = CreateLever();

        bbScaleEntity(r->levers[n]->obj, 0.04f, 0.04f, 0.04f);
        bbScaleEntity(r->levers[n]->baseObj, 0.04f, 0.04f, 0.04f);
        bbPositionEntity(r->levers[n]->obj, r->x - 240.f * RoomScale, r->y + 1104.f * RoomScale, r->z + (632.f - 64.f * n) * RoomScale, true);
        bbPositionEntity(r->levers[n]->baseObj, r->x - 240.f * RoomScale, r->y + 1104.f * RoomScale, r->z + (632.f - 64.f * n) * RoomScale, true);

        bbEntityParent(r->levers[n]->obj, r->obj);
        bbEntityParent(r->levers[n]->baseObj, r->obj);

        bbRotateEntity(r->levers[n]->baseObj, 0, -90, 0);
        bbRotateEntity(r->levers[n]->obj, 10, -90 - 180, 0);

        bbEntityPickMode(r->levers[n]->obj, 1, false);
        bbEntityRadius(r->levers[n]->obj, 0.1f);
    }

    sc = CreateSecurityCam(r->x-265.f*RoomScale, r->y+1280.f*RoomScale, r->z+105.f*RoomScale, r);
    sc->angle = 45;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    sc->id = 10;
}

void UpdateEvent_strg_elec_2c(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;
    float prevpitch;


    if (mainPlayer->currRoom == e->room) {

        bbEntityPick(mainPlayer->cam, 1.5f);

        for (i = 1; i <= 5; i += 2) {
            if (bbPickedEntity() == e->room->objects[i]) {
                DrawHandIcon = true;
                if (MouseHit1) {
                    mainPlayer->grabbedEntity = e->room->objects[i];
                }

                if (e->eventState == 0) {
                    if (i == 3) {
                        e->eventState = Max(e->eventState,1);
                        //PlaySound2(HorrorSFX(7))
                        PlaySound_SM(sndManager->lever);
                    }
                }
            }

            prevpitch = bbEntityPitch(e->room->objects[i]);

            if (MouseDown1 || MouseHit1) {
                if (mainPlayer->grabbedEntity != 0) {
                    if (mainPlayer->grabbedEntity == e->room->objects[i]) {
                        DrawHandIcon = true;
                        bbTurnEntity(e->room->objects[i], mouse_y_speed_1 * 2.5f, 0, 0);
                        bbRotateEntity(mainPlayer->grabbedEntity, Max(Min(bbEntityPitch(e->room->objects[i]), 85), -85), bbEntityYaw(e->room->objects[i]), 0);

                        mainPlayer->drawDirectionalArrow[0] = true;
                        mainPlayer->drawDirectionalArrow[2] = true;

                    }
                }
            } else {
                if (bbEntityPitch(e->room->objects[i]) > 0) {
                    bbRotateEntity(e->room->objects[i], CurveValue(85, bbEntityPitch(e->room->objects[i]), 10), bbEntityYaw(e->room->objects[i]), 0);
                } else {
                    bbRotateEntity(e->room->objects[i], CurveValue(-85, bbEntityPitch(e->room->objects[i]), 10), bbEntityYaw(e->room->objects[i]), 0);
                }
                mainPlayer->grabbedEntity = 0;
            }

            if (bbEntityPitch(e->room->objects[i]) > 83) {
                if (prevpitch <= 83) {
                    PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, e->room->objects[i]);
                }
                if (i == 3) {
                    SecondaryLightOn = CurveValue(1.f, SecondaryLightOn, 10.f);
                    if (prevpitch <= 83) {
                        PlayRangedSound_SM(sndManager->lightSwitch, mainPlayer->cam, e->room->objects[i]);
                    }
                } else {
                    RemoteDoorOn = true;
                }
            } else if ((bbEntityPitch(e->room->objects[i]) < -83)) {

                if (prevpitch >= -83) {
                    PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, e->room->objects[i]);
                }
                if (i == 3) {
                    if (prevpitch >= -83) {
                        PlayRangedSound_SM(sndManager->lightSwitch, mainPlayer->cam, e->room->objects[i]);
                        for (int iterator197 = 0; iterator197 < Room::getListSize(); iterator197++) {
                            r = Room::getObject(iterator197);

                            for (z = 0; z <= 19; z++) {
                                if (r->lightSprites[(int)(z)] != 0) {
                                    bbHideEntity(r->lightSprites[(int)(z)]);
                                }
                            }
                        }
                    }
                    SecondaryLightOn = CurveValue(0.f, SecondaryLightOn, 10.f);
                } else {
                    RemoteDoorOn = false;
                }
            }
        }

        if (e->eventState > 0 && e->eventState < 200) {
            e->eventState = e->eventState + timing->tickDuration;
            bbRotateEntity(e->room->levers[1]->obj, CurveValue(-85, bbEntityPitch(e->room->objects[3]), 5), bbEntityYaw(e->room->objects[3]), 0);
        }

    }

}

}

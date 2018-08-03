#include "Room_cont_914_1.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_cont_914_1(Room* r) {
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

    r->doors[2] = CreateDoor(r->x, 0, r->z-368.0*RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(r->doors[2]->buttons[0], r->x - 496.0 * RoomScale, 0.7, r->z - 272.0 * RoomScale, true);
    bbTurnEntity(r->doors[2]->buttons[0], 0, 90, 0);

    r->objects[0] = bbLoadMesh("GFX/Map/914key.x");
    r->objects[1] = bbLoadMesh("GFX/Map/914knob.x");

    for (i = 0; i <= 1; i++) {
        bbScaleEntity(r->objects[i], RoomScale, RoomScale, RoomScale);
        bbEntityPickMode(r->objects[i], 2);
    }

    bbPositionEntity(r->objects[0], r->x, r->y + 190.0 * RoomScale, r->z + 374.0 * RoomScale);
    bbPositionEntity(r->objects[1], r->x, r->y + 230.0 * RoomScale, r->z + 374.0 * RoomScale);
    bbEntityParent(r->objects[0], r->obj);
    bbEntityParent(r->objects[1], r->obj);

    d = CreateDoor(r->x - 624.0 * RoomScale, 0.0, r->z + 528.0 * RoomScale, 180, r, true);
    bbFreeEntity(d->obj2);
    d->obj2 = 0;
    bbFreeEntity(d->buttons[0]);
    d->buttons[0] = 0;
    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;
    r->doors[0] = d: d->autoClose == false;

    d = CreateDoor(r->x + 816.0 * RoomScale, 0.0, r->z + 528.0 * RoomScale, 180, r, true);
    bbFreeEntity(d->obj2);
    d->obj2 = 0;
    bbFreeEntity(d->buttons[0]);
    d->buttons[0] = 0;
    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;
    r->doors[1] = d;
    d->autoClose = false;

    r->objects[2] = bbCreatePivot();
    r->objects[3] = bbCreatePivot();
    bbPositionEntity(r->objects[2], r->x - 712.0 * RoomScale, 0.5, r->z + 640.0 * RoomScale);
    bbPositionEntity(r->objects[3], r->x + 728.0 * RoomScale, 0.5, r->z + 640.0 * RoomScale);
    bbEntityParent(r->objects[2], r->obj);
    bbEntityParent(r->objects[3], r->obj);

    it = CreatePaper("914_organic", r->x +954.0 * RoomScale, r->y +228.0 * RoomScale, r->z + 127.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("firstaid", r->x + 960.0 * RoomScale, r->y + 112.0 * RoomScale, r->z - 40.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);

    it = CreatePaper("drL4", r->x - 928.0 * RoomScale, 160.0 * RoomScale, r->z - 160.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_cont_914_1(Event* e) {
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
    String setting;

    //[Block]
    if (mainPlayer->currRoom == e->room) {
        if (!e->loaded) {
            e->sounds[0] = bbLoadSound("SFX/SCP/914/Refining.ogg");
            e->sounds[1] = bbLoadSound("SFX/SCP/914/PlayerUse.ogg");
            e->sounds[2] = bbLoadSound("SFX/SCP/914/PlayerDeath.ogg");

            e->loaded = true;
        }

        bbEntityPick(mainPlayer->cam, 1.0);
        if (bbPickedEntity() == e->room->objects[0]) {
            DrawHandIcon = true;
            if (MouseHit1) {
                mainPlayer->grabbedEntity = e->room->objects[0];
            }
        } else if ((bbPickedEntity() == e->room->objects[1])) {
            DrawHandIcon = true;
            if (MouseHit1) {
                mainPlayer->grabbedEntity = e->room->objects[1];
            }
        }

        if (MouseDown1 | MouseHit1) {
            //avain
            if (mainPlayer->grabbedEntity != 0) {
                if (mainPlayer->grabbedEntity == e->room->objects[0]) {
                    if (e->eventState == 0) {
                        DrawHandIcon = true;
                        bbTurnEntity(mainPlayer->grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5);

                        angle = WrapAngle(bbEntityRoll(e->room->objects[0]));
                        if (angle > 181) {
                            mainPlayer->drawDirectionialArrow[3] = true;
                        }
                        mainPlayer->drawDirectionialArrow[1] = true;

                        if (angle < 90) {
                            bbRotateEntity(mainPlayer->grabbedEntity, 0, 0, 361.0);
                        } else if ((angle < 180)) {
                            bbRotateEntity(mainPlayer->grabbedEntity, 0, 0, 180);
                        }

                        if (angle < 181 & angle > 90) {
                            for (int iterator167 = 0; iterator167 < Item::getListSize(); iterator167++) {
                                it = Item::getObject(iterator167);

                                if (it->collider != 0 & it->picked == false) {
                                    if (Abs(bbEntityX(it->collider) - (e->room->x - 712.0 * RoomScale)) < 200.0) {
                                        if (Abs(bbEntityY(it->collider) - (e->room->y + 648.0 * RoomScale)) < 104.0) {
                                            e->eventState = 1;
                                            e->soundChannels[0] = PlayRangedSound(e->sounds[0], mainPlayer->cam, e->room->objects[1]);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if ((mainPlayer->grabbedEntity == e->room->objects[1])) {
                    if (e->eventState == 0) {
                        DrawHandIcon = true;
                        bbTurnEntity(mainPlayer->grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5);

                        angle = WrapAngle(bbEntityRoll(e->room->objects[1]));
                        mainPlayer->drawDirectionialArrow[3] = true;
                        mainPlayer->drawDirectionialArrow[1] = true;

                        if (angle > 90) {
                            if (angle < 180) {
                                bbRotateEntity(mainPlayer->grabbedEntity, 0, 0, 90.0);
                            } else if ((angle < 270)) {
                                bbRotateEntity(mainPlayer->grabbedEntity, 0, 0, 270);
                            }
                        }

                    }
                }
            }
        } else {
            mainPlayer->grabbedEntity = 0;
        }

        if (mainPlayer->grabbedEntity != e->room->objects[1]) {
            angle = WrapAngle(bbEntityRoll(e->room->objects[1]));
            if (angle < 22.5) {
                angle = 0;
                setting = "1:1";
            } else if ((angle < 67.5)) {
                angle = 40;
                setting = "coarse";
            } else if ((angle < 180)) {
                angle = 90;
                setting = "rough";
            } else if ((angle > 337.5)) {
                angle = 359 - 360;
                setting = "1:1";
            } else if ((angle > 292.5)) {
                angle = 320 - 360;
                setting = "fine";
            } else {
                angle = 270 - 360;
                setting = "very fine";
            }
            bbRotateEntity(e->room->objects[1], 0, 0, CurveValue(angle, bbEntityRoll(e->room->objects[1]), 20));
        }

        for (i = 0; i <= 1; i++) {
            if (mainPlayer->grabbedEntity == e->room->objects[i]) {
                if (!bbEntityInView(e->room->objects[i], mainPlayer->cam)) {
                    mainPlayer->grabbedEntity = 0;
                } else if ((bbEntityDistance(e->room->objects[i], mainPlayer->cam) > 1.0)) {
                    mainPlayer->grabbedEntity = 0;
                }
            }
        }

        if (e->eventState > 0) {
            e->eventState = e->eventState + timing->tickDuration;

            e->room->doors[1]->open = false;
            if (e->eventState > 70 * 2) {
                e->room->doors[0]->open = false;
            }

            if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), bbEntityX(e->room->objects[2], true), bbEntityZ(e->room->objects[2], true)) < (170.0 * RoomScale)) {

                if (setting == "rough" | setting == "coarse") {
                    if (e->eventState > 70 * 2.6 & e->eventState - timing->tickDuration < 70 * 2.6) {
                        PlaySound2(e->sounds[2]);
                    }
                }

                if (e->eventState > 70 * 3) {
                    switch (setting) {
                        case "rough": {
                            Kill(mainPlayer);
                            mainPlayer->blinkTimer = -10;
                            if (e->soundChannels[0] != 0) {
                                bbStopChannel(e->soundChannels[0]);
                            }
                            DeathMSG = "\"A heavily mutilated corpse found inside the output booth of SCP-914. DNA testing identified the corpse as Class D Subject D-9341. ";
                            DeathMSG = DeathMSG + "The subject had obviously been \"refined\" by SCP-914 on the \"Rough\" setting, but we are still confused as to how he ";
                            DeathMSG = DeathMSG + "ended up inside the intake booth and who or what wound the key.\"";
                        }
                        case "coarse": {
                            mainPlayer->blinkTimer = -10;
                            if (e->eventState - timing->tickDuration < 70 * 3) {
                                PlaySound2(e->sounds[1]);
                            }
                        }
                        case "1:1": {
                            mainPlayer->blinkTimer = -10;
                            if (e->eventState - timing->tickDuration < 70 * 3) {
                                PlaySound2(e->sounds[1]);
                            }
                        }
                        case "fine", "very fine": {
                            mainPlayer->blinkTimer = -10;
                            if (e->eventState - timing->tickDuration < 70 * 3) {
                                PlaySound2(e->sounds[1]);
                            }
                        }
                    }
                }
            }

            if (e->eventState > (6 * 70)) {
                bbRotateEntity(e->room->objects[0], bbEntityPitch(e->room->objects[0]), bbEntityYaw(e->room->objects[0]), CurveAngle(0, bbEntityRoll(e->room->objects[0]),10.0));
            } else {
                bbRotateEntity(e->room->objects[0], bbEntityPitch(e->room->objects[0]), bbEntityYaw(e->room->objects[0]), 180);
            }

            if (e->eventState > (12 * 70)) {
                for (int iterator168 = 0; iterator168 < Item::getListSize(); iterator168++) {
                    it = Item::getObject(iterator168);

                    if (it->collider != 0 & it->picked == false) {
                        if (Distance(bbEntityX(it->collider), bbEntityZ(it->collider), bbEntityX(e->room->objects[2], true), bbEntityZ(e->room->objects[2], true)) < (180.0 * RoomScale)) {
                            //Use914(it, setting, EntityX(e\room\objects[3], True), EntityY(e\room\objects[3], True), EntityZ(e\room\objects[3], True))

                        }
                    }
                }

                if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), bbEntityX(e->room->objects[2], true), bbEntityZ(e->room->objects[2], true)) < (160.0 * RoomScale)) {
                    switch (setting) {
                        case "coarse": {
                            mainPlayer->injuries = 4.0;
                            Msg = "You notice countless small incisions all around your body. They are bleeding heavily.";
                            MsgTimer = 70*8;
                        }
                        case "1:1": {
                            userOptions->invertMouseY = (!userOptions->invertMouseY);
                        }
                        case "fine", "very fine": {
                            mainPlayer->superMan = 1.0;
                        }
                    }
                    mainPlayer->blurTimer = 1000;
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[3], true), bbEntityY(e->room->objects[3], true) + 1.0, bbEntityZ(e->room->objects[3], true));
                    bbResetEntity(mainPlayer->collider);
                    mainPlayer->dropSpeed = 0;
                }

                e->room->doors[0]->open = true;
                e->room->doors[1]->open = true;
                bbRotateEntity(e->room->objects[0], 0, 0, 0);
                e->eventState = 0;
            }
        }
    }
    //[End Block]
}

}

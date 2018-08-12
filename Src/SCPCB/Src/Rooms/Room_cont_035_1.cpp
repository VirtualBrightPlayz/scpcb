#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_cont_035_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_035_1(Room* r) {
    Door* d = CreateDoor(r->x - 296.f * RoomScale, 0, r->z - 672.f * RoomScale, 180, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->locked = true;
    r->doors[0] = d;
    bbPositionEntity(d->buttons[1], r->x - 164.f * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true), true);
    bbFreeEntity(d->buttons[0]);
    d->buttons[0] = 0;
    bbFreeEntity(d->obj2);
    d->obj2 = 0;

    Door* d2 = CreateDoor(r->x - 296.f * RoomScale, 0, r->z - 144.f * RoomScale, 0, r, false);
    d2->autoClose = false;
    d2->locked = true;
    r->doors[1] = d2;
    bbPositionEntity(d2->buttons[0], r->x - 432.f * RoomScale, bbEntityY(d2->buttons[0],true), r->z - 480.f * RoomScale, true);
    bbRotateEntity(d2->buttons[0], 0, 90, 0, true);
    bbFreeEntity(d2->buttons[1]);
    d2->buttons[1] = 0;
    bbFreeEntity(d2->obj2);
    d2->obj2 = 0;

    //door to the control room
    r->doors[2] = CreateDoor(r->x + 384.f * RoomScale, 0, r->z - 672.f * RoomScale, 180, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[2]->autoClose = false;

    //door to the storage room
    r->doors[3] = CreateDoor(r->x + 768.f * RoomScale, 0, r->z +512.f * RoomScale, 90, r, false, DOOR_TYPE_DEF, "", "5731");
    r->doors[3]->autoClose = false;

    d->linkedDoor = d2;
    d2->linkedDoor = d;

    for (int i = 0; i < 2; i++) {
        r->levers[i] = CreateLever();

        bbScaleEntity(r->levers[i]->baseObj, 0.04f, 0.04f, 0.04f);
        bbScaleEntity(r->levers[i]->obj, 0.04f, 0.04f, 0.04f);
        bbPositionEntity(r->levers[i]->baseObj, r->x + 210.f * RoomScale, r->y + 224.f * RoomScale, r->z - (208-i*76) * RoomScale, true);
        bbPositionEntity(r->levers[i]->obj, r->x + 210.f * RoomScale, r->y + 224.f * RoomScale, r->z - (208-i*76) * RoomScale, true);

        bbEntityParent(r->levers[i]->baseObj, r->obj);
        bbEntityParent(r->levers[i]->obj, r->obj);

        bbRotateEntity(r->levers[i]->baseObj, 0, -90-180, 0);
        bbRotateEntity(r->levers[i]->obj, -80, -90, 0);

        bbEntityPickMode(r->levers[i]->obj, 1, false);
        bbEntityRadius(r->levers[i]->obj, 0.1f);
    }

    //the control room
    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x + 456 * RoomScale, 0.5f, r->z + 400.f * RoomScale, true);

    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x - 576 * RoomScale, 0.5f, r->z + 640.f * RoomScale, true);

    for (int i = 0; i < 2; i++) {
        Emitter* em = CreateEmitter(r->x - 272.f * RoomScale, 10, r->z + (624.f-i*512) * RoomScale, 0);
        bbTurnEntity(em->obj, 90, 0, 0, true);
        bbEntityParent(em->obj, r->obj);
        em->randAngle = 15;
        em->speed = 0.05f;
        em->sizeChange = 0.007f;
        em->aChange = -0.006f;
        em->gravity = -0.24f;

        r->objects[5+i] = em->obj;
    }

    //the corners of the cont chamber (needed to calculate whether the player is inside the chamber)
    r->objects[7] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[7], r->x - 720 * RoomScale, 0.5f, r->z + 880.f * RoomScale, true);
    r->objects[8] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[8], r->x + 176 * RoomScale, 0.5f, r->z - 144.f * RoomScale, true);

    Item* it = CreatePaper("doc035ad", r->x + 248.f * RoomScale, r->y + 220.f * RoomScale, r->z + 576.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("radio", r->x - 544.f * RoomScale, 0.5f, r->z + 704.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("scp500", r->x + 1168*RoomScale, 224*RoomScale, r->z+576*RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("scp148", r->x - 360 * RoomScale, 0.5f, r->z + 644 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("doc035", r->x + 1168.f * RoomScale, 104.f * RoomScale, r->z + 608.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_cont_035_1(Event* e) {
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
    Door* dor;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;


    if (mainPlayer->currRoom == e->room) {
        //eventstate2 = has 035 told the code to the storage room (true/false)
        //eventstate3 = has the player opened the gas valves (0=no, 0<x<35*70 yes, x>35*70 the host has died)

        if (e->eventState == 0) {
            if (bbEntityDistance(mainPlayer->collider, e->room->objects[3])<2) {
                n = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[4],true),0.5f,bbEntityZ(e->room->objects[4],true));

                n->texture = "GFX/NPCs/035victim.jpg";
                bbHideEntity(n->obj);

                bbSetAnimTime(n->obj, 501);
                n->frame = 501;

                n->state = 6;

                e->eventState = 1;
            }

        } else if ((e->eventState > 0)) {
            if (e->room->npc[0]==nullptr) {
                for (int iterator153 = 0; iterator153 < NPC::getListSize(); iterator153++) {
                    n = NPC::getObject(iterator153);

                    if (n->texture.equals("GFX/NPCs/035victim.jpg")) {
                        e->room->npc[0] = n;

                        temp = (int)(e->room->npc[0]->frame);

                        bbFreeEntity(e->room->npc[0]->obj);
                        e->room->npc[0]->obj = bbLoadAnimMesh("GFX/NPCs/035.b3d");
                        x = 0.5f / bbMeshWidth(e->room->npc[0]->obj);
                        bbScaleEntity(e->room->npc[0]->obj, x,x,x);

                        bbSetAnimTime(e->room->npc[0]->obj, temp);

                        bbShowEntity(e->room->npc[0]->obj);

                        bbRotateEntity(n->collider, 0, e->room->angle+270, 0, true);

                        break;
                    }
                }
            }

            if (bbChannelPlaying(e->room->npc[0]->soundChannels[0])) {
                e->room->npc[0]->soundChannels[0] = LoopRangedSound(e->room->npc[0]->sounds[0], e->room->npc[0]->soundChannels[0], mainPlayer->cam, e->room->obj, 6.f);
            }

            if (e->eventState==1) {
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[3])<1.2f) {
                    if (bbEntityInView(e->room->npc[0]->obj, mainPlayer->cam)) {
                        PlaySound2(LoadTempSound("SFX/SCP/035/GetUp.ogg"));
                        e->eventState = 1.5f;
                    }
                }
            } else {

                if (e->room->doors[3]->open) {
                    e->eventState2 = Max(e->eventState2, 1);
                }

                //the door is closed
                if (e->eventState2 == 20) {
                    e->room->levers[0]->succ = true;
                }

                if (!e->room->levers[0]->succ) {
                    //the gas valves are open
                    temp = e->room->levers[1]->succ;
                    if (temp || (e->eventState3>25*70 && e->eventState3<50*70)) {
                        if (temp) {
                            bbPositionEntity(e->room->objects[5], bbEntityX(e->room->objects[5],true), 424.f*RoomScale, bbEntityZ(e->room->objects[5],true),true);
                            bbPositionEntity(e->room->objects[6], bbEntityX(e->room->objects[6],true), 424.f*RoomScale, bbEntityZ(e->room->objects[6],true),true);
                        } else {
                            bbPositionEntity(e->room->objects[5], bbEntityX(e->room->objects[5],true), 10, bbEntityZ(e->room->objects[5],true),true);
                            bbPositionEntity(e->room->objects[6], bbEntityX(e->room->objects[6],true), 10, bbEntityZ(e->room->objects[6],true),true);

                        }

                        if (e->eventState3 >-30*70) {
                            e->eventState3 = abs(e->eventState3)+timing->tickDuration;
                            if (e->eventState3 > 1 && e->eventState3-timing->tickDuration<=1) {
                                e->room->npc[0]->state = 0;
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Gased1.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                            } else if ((e->eventState3>15*70 && e->eventState3<25*70)) {
                                if (e->eventState3-timing->tickDuration<=15*70) {
                                    if (e->room->npc[0]->sounds[0]!=0) {
                                        bbFreeSound(e->room->npc[0]->sounds[0]);
                                        e->room->npc[0]->sounds[0] = 0;
                                    }
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Gased2.ogg");
                                    e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    SetNPCFrame( e->room->npc[0],553);
                                }
                                e->room->npc[0]->state = 6;

                                AnimateNPC(e->room->npc[0], 553, 529, -0.12f, false);
                            } else if ((e->eventState3>25*70 && e->eventState3<35*70)) {
                                e->room->npc[0]->state = 6;
                                AnimateNPC(e->room->npc[0], 529, 524, -0.08f, false);
                            } else if ((e->eventState3>35*70)) {
                                if (e->room->npc[0]->state == 6) {
                                    mainPlayer->sanity895 = -150*bbSin(bbAnimTime(e->room->npc[0]->obj)-524)*9;
                                    AnimateNPC(e->room->npc[0], 524, 553, 0.08f, false);
                                    if (e->room->npc[0]->frame==553) {
                                        e->room->npc[0]->state = 0;
                                    }
                                }

                                if (e->eventState3-timing->tickDuration<=35*70) {
                                    if (e->room->npc[0]->sounds[0]!=0) {
                                        bbFreeSound(e->room->npc[0]->sounds[0]);
                                        e->room->npc[0]->sounds[0] = 0;
                                    }
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedKilled1.ogg");
                                    e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    PlaySound2(LoadTempSound("SFX/SCP/035/KilledGetUp.ogg"));
                                    e->eventState = 60*70;
                                }
                            }
                        }
                        //gas valves closed
                    } else {

                        if (e->room->npc[0]->state == 6) {
                            if (e->room->npc[0]->frame>=501 && e->room->npc[0]->frame<=523) {
                                e->room->npc[0]->frame = Animate2(e->room->npc[0]->obj, bbAnimTime(e->room->npc[0]->obj), 501, 523, 0.08f, false);
                                if (e->room->npc[0]->frame==523) {
                                    e->room->npc[0]->state = 0;
                                }
                            }

                            if (e->room->npc[0]->frame>=524 && e->room->npc[0]->frame<=553) {
                                e->room->npc[0]->frame = Animate2(e->room->npc[0]->obj, bbAnimTime(e->room->npc[0]->obj), 524, 553, 0.08f, false);
                                if (e->room->npc[0]->frame==553) {
                                    e->room->npc[0]->state = 0;
                                }
                            }
                        }

                        bbPositionEntity(e->room->objects[5], bbEntityX(e->room->objects[5],true), 10, bbEntityZ(e->room->objects[5],true),true);
                        bbPositionEntity(e->room->objects[6], bbEntityX(e->room->objects[6],true), 10, bbEntityZ(e->room->objects[6],true),true);

                        if (e->room->npc[0]->state == 0) {
                            bbPointEntity(e->room->npc[0]->obj, mainPlayer->collider);
                            bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 15.f), 0);

                            if (bbRand(500)==1) {
                                if (bbEntityDistance(e->room->npc[0]->collider, e->room->objects[4])>2) {
                                    e->room->npc[0]->state2 = 1;
                                } else {
                                    e->room->npc[0]->state2 = 0;
                                }
                                e->room->npc[0]->state = 1;
                            }
                        } else if ((e->room->npc[0]->state == 1)) {
                            if (e->room->npc[0]->state2 == 1) {
                                bbPointEntity(e->room->npc[0]->obj, e->room->objects[4]);
                                if (bbEntityDistance(e->room->npc[0]->collider, e->room->objects[4])<0.2f) {
                                    e->room->npc[0]->state = 0;
                                }
                            } else {
                                bbRotateEntity(e->room->npc[0]->obj, 0, e->room->angle-180, 0, true);
                                if (bbEntityDistance(e->room->npc[0]->collider, e->room->objects[4])>2) {
                                    e->room->npc[0]->state = 0;
                                }
                            }

                            bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 15.f), 0);

                        }

                        if (e->eventState3 > 0) {
                            e->eventState3 = -e->eventState3;
                            //the host is dead
                            if (e->eventState3<-35*70) {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedKilled2.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                e->eventState = 60*70;
                            } else {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                if (e->eventState3<-20*70) {
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedStop2.ogg");
                                } else {
                                    e->eventState3 = -21*70;
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedStop1.ogg");
                                }

                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                e->eventState = 61*70;
                            }
                        } else {

                            e->eventState = e->eventState+timing->tickDuration;
                            if (e->eventState > 4*70 && e->eventState-timing->tickDuration <=4*70) {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Help1.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                e->eventState = 10*70;
                            } else if ((e->eventState > 20*70 && e->eventState-timing->tickDuration <=20*70)) {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Help2.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                            } else if ((e->eventState > 40*70 && e->eventState-timing->tickDuration <=40*70)) {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle1.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                            } else if ((e->eventState > 50*70 && e->eventState-timing->tickDuration <=50*70)) {
                                if (e->room->npc[0]->sounds[0]!=0) {
                                    bbFreeSound(e->room->npc[0]->sounds[0]);
                                    e->room->npc[0]->sounds[0] = 0;
                                }
                                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle2.ogg");
                                e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                            } else if ((e->eventState > 80*70 && e->eventState-timing->tickDuration <=80*70)) {
                                //skip the closet part if player has already opened it
                                if ((int)(e->eventState2)) {
                                    e->eventState = 130*70;
                                } else {
                                    //the host is dead
                                    if (e->eventState3<-30*70) {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedCloset.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                        //the gas valves haven't been opened
                                    } else if ((e->eventState3 == 0)) {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Closet1.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                        //gas valves have been opened but 035 isn't dead
                                    } else {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedCloset.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    }
                                }
                            } else if ((e->eventState > 80*70)) {
                                if ((int)(e->eventState2)) {
                                    e->eventState = Max(e->eventState,100*70);
                                }
                                if (e->eventState>110*70 && e->eventState-timing->tickDuration <=110*70) {
                                    if ((int)(e->eventState2)) {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Closet2.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                        e->eventState = 130*70;
                                    } else {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle3.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    }
                                } else if ((e->eventState>125*70 && e->eventState-timing->tickDuration <=125*70)) {
                                    if ((int)(e->eventState2)) {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Closet2.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    } else {
                                        if (e->room->npc[0]->sounds[0]!=0) {
                                            bbFreeSound(e->room->npc[0]->sounds[0]);
                                            e->room->npc[0]->sounds[0] = 0;
                                        }
                                        e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle4.ogg");
                                        e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                    }
                                } else if ((e->eventState>150*70 && e->eventState-timing->tickDuration <=150*70)) {
                                    if (e->room->npc[0]->sounds[0]!=0) {
                                        bbFreeSound(e->room->npc[0]->sounds[0]);
                                        e->room->npc[0]->sounds[0] = 0;
                                    }
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle5.ogg");
                                    e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                } else if ((e->eventState>200*70 && e->eventState-timing->tickDuration <=200*70)) {
                                    if (e->room->npc[0]->sounds[0]!=0) {
                                        bbFreeSound(e->room->npc[0]->sounds[0]);
                                        e->room->npc[0]->sounds[0] = 0;
                                    }
                                    e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Idle6.ogg");
                                    e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                                }
                            }

                        }

                    }

                    //the player has opened the door
                } else {
                    if (e->eventState2 < 10) {
                        e->room->doors[2]->open = false;
                        e->room->doors[2]->locked = true;

                        if (e->room->doors[1]->open == false) {
                            e->room->doors[0]->locked = false;
                            e->room->doors[1]->locked = false;
                            UseDoor(e->room->doors[1]);
                            e->room->doors[0]->locked = true;
                            e->room->doors[1]->locked = true;

                        }

                        if (e->eventState3==0) {
                            if (e->room->npc[0]->sounds[0]!=0) {
                                bbFreeSound(e->room->npc[0]->sounds[0]);
                                e->room->npc[0]->sounds[0] = 0;
                            }
                            e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/Escape.ogg");
                            e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                        } else if (abs(e->eventState3)>35*70) {
                            if (e->room->npc[0]->sounds[0]!=0) {
                                bbFreeSound(e->room->npc[0]->sounds[0]);
                                e->room->npc[0]->sounds[0] = 0;
                            }
                            e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/KilledEscape.ogg");
                            e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                        } else {
                            if (e->room->npc[0]->sounds[0]!=0) {
                                bbFreeSound(e->room->npc[0]->sounds[0]);
                                e->room->npc[0]->sounds[0] = 0;
                            }
                            e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/035/GasedEscape.ogg");
                            e->room->npc[0]->soundChannels[0] = bbPlaySound(e->room->npc[0]->sounds[0]);
                        }
                        e->eventState2 = 20;
                    }

                    if (e->eventState2 == 20) {
                        dist = bbEntityDistance(e->room->doors[0]->frameobj, e->room->npc[0]->collider);

                        e->room->npc[0]->state = 1;
                        if (dist > 2.5f) {
                            bbPointEntity(e->room->npc[0]->obj, e->room->doors[1]->frameobj);
                            bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 15.f), 0);
                        } else if ((dist > 0.7f)) {
                            if (bbChannelPlaying(e->room->npc[0]->soundChannels[0])) {
                                e->room->npc[0]->state = 0;
                                bbPointEntity(e->room->npc[0]->obj, mainPlayer->collider);
                                bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 15.f), 0);
                            } else {
                                bbPointEntity(e->room->npc[0]->obj, e->room->doors[0]->frameobj);
                                bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 15.f), 0);
                            }
                        } else {
                            RemoveNPC(e->room->npc[0]);
                            e->room->npc[0] = nullptr;
                            e->eventState = -1;
                            e->eventState2 = 0;
                            e->eventState3 = 0;
                            e->room->doors[0]->locked = false;
                            e->room->doors[1]->locked = false;
                            e->room->doors[2]->locked = false;
                            UseDoor(e->room->doors[1],false);
                            for (int iterator154 = 0; iterator154 < Door::getListSize(); iterator154++) {
                                dor = Door::getObject(iterator154);

                                if (dor->typ == DOOR_TYPE_HCZ) {
                                    if (abs(bbEntityX(e->room->obj)-bbEntityX(dor->frameobj,true))<4.5f) {
                                        if (abs(bbEntityZ(e->room->obj)-bbEntityZ(dor->frameobj,true))<4.5f) {
                                            UseDoor(dor,false);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                //eventstate > 1
            }

            //eventstate < 0 (SCP-035 has left)
        } else {

            if (e->room->levers[1]->succ) {
                bbPositionEntity(e->room->objects[5], bbEntityX(e->room->objects[5],true), 424.f*RoomScale, bbEntityZ(e->room->objects[5],true),true);
                bbPositionEntity(e->room->objects[6], bbEntityX(e->room->objects[6],true), 424.f*RoomScale, bbEntityZ(e->room->objects[6],true),true);
            } else {
                bbPositionEntity(e->room->objects[5], bbEntityX(e->room->objects[5],true), 10, bbEntityZ(e->room->objects[5],true),true);
                bbPositionEntity(e->room->objects[6], bbEntityX(e->room->objects[6],true), 10, bbEntityZ(e->room->objects[6],true),true);
            }

            //If (e\room\levers[0]\succ) Then
            //	If (e\room\doors[0]\open = True) Then UseDoor(e\room\doors[1])
            //Else
            //	If (e\room\doors[0]\open = False) Then UseDoor(e\room\doors[1])
            //EndIf

            temp = false;

            //player is inside the containment chamber
            if (bbEntityX(mainPlayer->collider)>Min(bbEntityX(e->room->objects[7],true),bbEntityX(e->room->objects[8],true))) {
                if (bbEntityX(mainPlayer->collider)<Max(bbEntityX(e->room->objects[7],true),bbEntityX(e->room->objects[8],true))) {
                    if (bbEntityZ(mainPlayer->collider)>Min(bbEntityZ(e->room->objects[7],true),bbEntityZ(e->room->objects[8],true))) {
                        if (bbEntityZ(mainPlayer->collider)<Max(bbEntityZ(e->room->objects[7],true),bbEntityZ(e->room->objects[8],true))) {
                            if (e->room->npc[0]==nullptr) {
                                if (e->room->npc[0]==nullptr) {
                                    e->room->npc[0] = CreateNPC(NPCtypeTentacle, 0,0,0);
                                }
                            }

                            bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[4],true), 0, bbEntityZ(e->room->objects[4],true));

                            if (e->room->npc[0]->state > 0) {
                                if (e->room->npc[1]==nullptr) {
                                    if (e->room->npc[1]==nullptr) {
                                        e->room->npc[1] = CreateNPC(NPCtypeTentacle, 0,0,0);
                                    }
                                }
                            }

                            mainPlayer->stamina = CurveValue(Min(60,mainPlayer->stamina), mainPlayer->stamina, 20.f);

                            temp = true;

                            if (e->sounds[0] == 0) {
                                LoadEventSound(e,"SFX/Room/035Chamber/Whispers1.ogg");
                            }
                            if (e->sounds[1] == 0) {
                                LoadEventSound(e,"SFX/Room/035Chamber/Whispers2.ogg",1);
                            }

                            e->eventState2 = Min(e->eventState2+(timing->tickDuration/6000),1.f);
                            e->eventState3 = CurveValue(e->eventState2, e->eventState3, 50);

                            if (!IsPlayerWearingItem(mainPlayer,"hazmatsuit3") && !IsPlayerWearingItem(mainPlayer,"gasmask3")) {
                                mainPlayer->sanity895 = mainPlayer->sanity895-timing->tickDuration*1.1f;
                                mainPlayer->blurTimer = bbSin(TimeInPosMilliSecs()/10)*abs(mainPlayer->sanity895);
                            }

                            if (!IsPlayerWearingItem(mainPlayer,"hazmatsuit3")) {
                                mainPlayer->injuries = mainPlayer->injuries + (timing->tickDuration/5000);
                            } else {
                                mainPlayer->injuries = mainPlayer->injuries + (timing->tickDuration/10000);
                            }

                            if (mainPlayer->dead == true && mainPlayer->bloodloss >=100) {
                                DeathMSG = "Class D Subject D-9341 found dead inside SCP-035's containment chamber. ";
                                DeathMSG = DeathMSG + "The subject exhibits heavy hemorrhaging of blood vessels around the eyes and inside the mouth and nose. ";
                                DeathMSG = DeathMSG + "Sent for autopsy.";
                            }
                        }
                    }
                }
            }

            if (e->room->npc[1]!=nullptr) {
                bbPositionEntity(e->room->npc[1]->collider, bbEntityX(e->room->obj,true), 0, bbEntityZ(e->room->obj,true));
                angle = WrapAngle(bbEntityYaw(e->room->npc[1]->collider)-e->room->angle);

                if (angle>90) {
                    if (angle < 225) {
                        bbRotateEntity(e->room->npc[1]->collider, 0, e->room->angle-89-180, 0);
                    } else {
                        bbRotateEntity(e->room->npc[1]->collider, 0, e->room->angle-1, 0);
                    }
                }
            }

            if (temp == false) {
                e->eventState2 = Max(e->eventState2-(timing->tickDuration/2000),0);
                e->eventState3 = Max(e->eventState3-(timing->tickDuration/100),0);
            }

            if (e->eventState3 > 0 && (!IsPlayerWearingItem(mainPlayer,"hazmatsuit3")) && (!IsPlayerWearingItem(mainPlayer,"gasmask3"))) {
                e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->obj, 10, e->eventState3);
                e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, e->room->obj, 10, (e->eventState3-0.5f)*2);
            }

        }

    } else {
        if (e->eventState==0) {
            if (e->sounds[0] == 0) {
                if (bbEntityDistance(mainPlayer->collider, e->room->obj) < 20) {
                    LoadEventSound(e,"SFX/Room/035Chamber/InProximity.ogg");
                    PlaySound2(e->sounds[0]);
                }
            }
        } else if ((e->eventState < 0)) {
            for (i = 0; i <= 1; i++) {
                if (e->room->npc[i]!=nullptr) {
                    RemoveNPC(e->room->npc[i]);
                    e->room->npc[i] = nullptr;
                }
            }
        }

    }

}

}

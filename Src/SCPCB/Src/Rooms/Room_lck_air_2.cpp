#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>
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
#include "Room_lck_air_2.h"

namespace CBN {

// Constants.
const int ROOM_LCKA2_TRIGGERPIVOT = 0;
const int ROOM_LCKA2_BROKENDOOR = 1;
const int ROOM_LCKA2_CORPSESPAWN = 2;
const int ROOM_LCKA2_PIPES = 3;
const int EVENT_LCKA2_ACTIVE = 0;
const int EVENT_LCKA2_LEAVING = 1;
const int EVENT_LCKA2_TIMER = 0;
const int EVENT_LCKA2_SPAWNEDCORPSE = 0;

// Functions.
void FillRoom_lck_air_2(Room* r) {
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
    int bd_temp;

    if (r->roomTemplate->name.equals("lck_air_broke_2")) {
        r->objects[ROOM_LCKA2_CORPSESPAWN] = bbCreatePivot(r->obj);
        bbPositionEntity(r->objects[ROOM_LCKA2_CORPSESPAWN], r->x - 156.825*RoomScale, -37.3458*RoomScale, r->z+121.364*RoomScale, true);

        de = CreateDecal(DECAL_BLOOD_SPLATTER,  r->x - 156.825*RoomScale, -37.3458*RoomScale, r->z+121.364*RoomScale,90,bbRnd(360),0);
        de->size = 0.5;
        bbScaleSprite(de->obj, de->size,de->size);
        bbEntityParent(de->obj, r->obj);
    }

    r->doors[0] = CreateDoor(r->x + 336.0 * RoomScale, 0.0, r->z - 382.0 * RoomScale, 0, r);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 580.822 * RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z - 606.679 * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 580.822 * RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z - 606.679 * RoomScale, true);
    r->doors[0]->locked = true;
    r->doors[0]->mtfClose = false;

    r->doors[1] = CreateDoor(r->x + 336.0 * RoomScale, 0.0, r->z + 462.0 * RoomScale, 180, r);
    bbPositionEntity(r->doors[1]->buttons[0], r->x + 580.822 * RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z - 606.679 * RoomScale, true);
    bbPositionEntity(r->doors[1]->buttons[1], r->x + 580.822 * RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z - 606.679 * RoomScale, true);
    r->doors[1]->locked = true;
    r->doors[1]->mtfClose = false;

    for (int iterator187 = 0; iterator187 < Room::getListSize(); iterator187++) {
        r2 = Room::getObject(iterator187);

        if (r2!=r) {
            if (r2->roomTemplate->name.equals("lck_air_2") || r2->roomTemplate->name.equals("lck_air_broke_2")) {
                //don't load the mesh again
                r->objects[ROOM_LCKA2_PIPES] = bbCopyMeshModelEntity((MeshModel*)r2->objects[ROOM_LCKA2_PIPES],r->obj);
                break;
            }
        }
    }
    if (r->objects[ROOM_LCKA2_PIPES]==0) {
        r->objects[ROOM_LCKA2_PIPES] = bbLoadMesh("GFX/Map/room2gw_pipes.b3d",r->obj);
    }
    bbEntityPickMode(r->objects[ROOM_LCKA2_PIPES],2);

    if (r->roomTemplate->name.equals("lck_air_2")) {
        r->objects[ROOM_LCKA2_TRIGGERPIVOT] = bbCreatePivot();
        //PositionEntity(r\objects[ROOM_LCKA2_TRIGGERPIVOT],r\x-48.0*RoomScale,128.0*RoomScale,r\z+320.0*RoomScale)
        bbPositionEntity(r->objects[ROOM_LCKA2_TRIGGERPIVOT],r->x+344.0*RoomScale,128.0*RoomScale,r->z);
        bbEntityParent(r->objects[ROOM_LCKA2_TRIGGERPIVOT],r->obj);

        bd_temp = false;
        if (room2gw_brokendoor) {
            if (room2gw_x == r->x) {
                if (room2gw_z == r->z) {
                    bd_temp = true;
                }
            }
        }

        if ((room2gw_brokendoor == 0 && bbRand(1,2)==1) || bd_temp) {
            //TODO: Not this.
            r->objects[ROOM_LCKA2_BROKENDOOR] = bbLoadMesh("GFX/Map/Meshes/door.b3d");
            bbScaleEntity(r->objects[ROOM_LCKA2_BROKENDOOR], (204.0 * RoomScale) / bbMeshWidth((MeshModel*)r->objects[ROOM_LCKA2_BROKENDOOR]), 312.0 * RoomScale / bbMeshHeight((MeshModel*)r->objects[ROOM_LCKA2_BROKENDOOR]), 16.0 * RoomScale / bbMeshDepth((MeshModel*)r->objects[ROOM_LCKA2_BROKENDOOR]));
            bbEntityType(r->objects[ROOM_LCKA2_BROKENDOOR], HIT_MAP);
            bbPositionEntity(r->objects[ROOM_LCKA2_BROKENDOOR], r->x + 336.0 * RoomScale, 0.0, r->z + 462.0 * RoomScale);
            bbRotateEntity(r->objects[ROOM_LCKA2_BROKENDOOR], 0, 180 + 180, 0);
            bbEntityParent(r->objects[ROOM_LCKA2_BROKENDOOR], r->obj);
            bbMoveEntity(r->objects[ROOM_LCKA2_BROKENDOOR],120.0,0,5.0);
            room2gw_brokendoor = true;
            room2gw_x = r->x;
            room2gw_z = r->z;
            bbFreeEntity(r->doors[1]->obj2);
            r->doors[1]->obj2 = 0;
        }
    }
}

void UpdateEvent_lck_air_2(Event* e) {
    float dist;
    int i;
    int temp;
    Pivot* pvt;
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
    MeshModel* d_ent;

    //[Block]
    e->room->doors[0]->locked = true;
    e->room->doors[1]->locked = true;

    int brokendoor = false;
    if (e->room->objects[ROOM_LCKA2_BROKENDOOR]!=0) {
        brokendoor = true;
    }

    if (mainPlayer->currRoom == e->room) {
        if (!e->intState[EVENT_LCKA2_ACTIVE]) {
            if (bbEntityDistance(e->room->objects[ROOM_LCKA2_TRIGGERPIVOT],mainPlayer->collider)<1.4 & (!e->intState[EVENT_LCKA2_LEAVING])) {
                e->intState[EVENT_LCKA2_ACTIVE] = true;
                if (brokendoor) {
                    if (e->sounds[1] != 0) {
                        bbFreeSound(e->sounds[1]);
                        e->sounds[1] = 0;
                    }
                    e->sounds[1] = bbLoadSound("SFX/Door/DoorSparks.ogg");
                    e->soundChannels[1] = PlayRangedSound(e->sounds[1],mainPlayer->cam,e->room->objects[ROOM_LCKA2_BROKENDOOR],5);
                }
                bbStopChannel(e->soundChannels[0]);
                e->soundChannels[0] = 0;
                if (e->sounds[0] != 0) {
                    bbFreeSound(e->sounds[0]);
                    e->sounds[0] = 0;
                }
                e->sounds[0] = bbLoadSound("SFX/Door/Airlock.ogg");
                e->room->doors[0]->locked = false;
                e->room->doors[1]->locked = false;
                UseDoor(e->room->doors[0]);
                UseDoor(e->room->doors[1]);
            } else if ((bbEntityDistance(e->room->objects[ROOM_LCKA2_TRIGGERPIVOT],mainPlayer->collider)>2.4)) {
                e->intState[EVENT_LCKA2_LEAVING] = false;
            }
        } else {
            if (e->floatState[EVENT_LCKA2_TIMER] < 70*7) {
                e->floatState[EVENT_LCKA2_TIMER] = e->floatState[EVENT_LCKA2_TIMER] + timing->tickDuration;
                e->room->doors[0]->open = false;
                e->room->doors[1]->open = false;
                if (e->floatState[EVENT_LCKA2_TIMER] < 70*1) {

                    if (brokendoor) {
                        pvt = bbCreatePivot();
                        d_ent = (MeshModel*)e->room->objects[ROOM_LCKA2_BROKENDOOR];
                        bbPositionEntity(pvt, bbEntityX(d_ent,true), bbEntityY(d_ent,true)+bbRnd(0.0,0.05), bbEntityZ(d_ent,true));
                        bbRotateEntity(pvt, 0, bbEntityYaw(d_ent,true)+90, 0);
                        bbMoveEntity(pvt,0,0,0.2);

                        for (i = 0; i <= 3; i++) {
                            p = CreateParticle(bbEntityX(pvt), bbEntityY(pvt), bbEntityZ(pvt), PARTICLE_SPARK, 0.002, 0, 25);
                            p->speed = bbRnd(0.01,0.05);
                            //RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
                            bbRotateEntity(p->pvt, bbRnd(-45,0), bbEntityYaw(pvt)+bbRnd(-10.0,10.0), 0);

                            p->size = 0.0075;
                            bbScaleSprite(p->sprite,p->size,p->size);

                            //EntityOrder(p\obj,-1)

                            p->aChange = -0.05;
                        }

                        bbFreeEntity(pvt);
                    }

                } else if ((e->floatState[EVENT_LCKA2_TIMER] > 70*3 & e->floatState[EVENT_LCKA2_TIMER] < 70*5.5)) {
                    pvt = bbCreatePivot(e->room->obj);
                    for (i = 0; i <= 1; i++) {
                        if (e->room->roomTemplate->name.equals("lck_ez_3")) {
                            if (i == 0) {
                                bbPositionEntity(pvt,-288.0*RoomScale,416.0*RoomScale,320.0*RoomScale,false);
                            } else {
                                bbPositionEntity(pvt,192.0*RoomScale,416.0*RoomScale,320.0*RoomScale,false);
                            }
                        } else {
                            if (i == 0) {
                                bbPositionEntity(pvt,312.0*RoomScale,416.0*RoomScale,-128.0*RoomScale,false);
                            } else {
                                bbPositionEntity(pvt,312.0*RoomScale,416.0*RoomScale,224.0*RoomScale,false);
                            }
                        }

                        p = CreateParticle(bbEntityX(pvt,true), bbEntityY(pvt,true), bbEntityZ(pvt,true), PARTICLE_SMOKE_WHITE, 0.8, 0, 50);
                        p->speed = 0.025;
                        bbRotateEntity(p->pvt, 90, 0, 0);

                        p->aChange = -0.02;
                    }

                    bbFreeEntity(pvt);
                    if (e->soundChannels[0] == 0) {
                        e->soundChannels[0] = PlayRangedSound(e->sounds[0],mainPlayer->cam,e->room->objects[ROOM_LCKA2_TRIGGERPIVOT],5);
                    }
                }
            } else {
                e->floatState[EVENT_LCKA2_TIMER] = 0.0;
                e->intState[EVENT_LCKA2_ACTIVE] = false;
                e->intState[EVENT_LCKA2_LEAVING] = true;
                if (e->room->doors[0]->open == false) {
                    e->room->doors[0]->locked = false;
                    e->room->doors[1]->locked = false;
                    UseDoor(e->room->doors[0]);
                    UseDoor(e->room->doors[1]);
                }
            }
        }

        if (brokendoor) {
            if (bbChannelPlaying(e->soundChannels[1])) {
                UpdateRangedSoundOrigin(e->soundChannels[1],mainPlayer->cam,e->room->objects[ROOM_LCKA2_BROKENDOOR],5);
            }
        }
        if (bbChannelPlaying(e->soundChannels[0])) {
            UpdateRangedSoundOrigin(e->soundChannels[0],mainPlayer->cam,e->room->objects[ROOM_LCKA2_TRIGGERPIVOT],5);
        }
    } else {
        e->intState[EVENT_LCKA2_LEAVING] = false;
    }
    //[End Block]
}

void UpdateEvent_lck_air_broke_2(Event* e) {
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

    //[Block]
    if (e->room->dist < 8) {
        if (!e->intState[EVENT_LCKA2_SPAWNEDCORPSE]) {
            e->room->npc[0] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->objects[ROOM_LCKA2_CORPSESPAWN],true), bbEntityY(e->room->objects[ROOM_LCKA2_CORPSESPAWN],true)+0.5, bbEntityZ(e->room->objects[ROOM_LCKA2_CORPSESPAWN],true));
            bbPointEntity(e->room->npc[0]->collider, e->room->obj);
            bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider),0, true);
            SetNPCFrame(e->room->npc[0], 906);
            e->room->npc[0]->state = 8;

            e->intState[EVENT_LCKA2_SPAWNEDCORPSE] = true;
        }
    }
    //[End Block]
}

}

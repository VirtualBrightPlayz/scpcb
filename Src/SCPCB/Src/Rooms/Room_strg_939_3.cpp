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
#include "Room_strg_939_3.h"

namespace CBN {

// Functions.
void FillRoom_strg_939_3(Room* r) {
    Door* d;
    Door* d2;
    SecurityCam* sc;
    Decal* de;
    Room* r2;
    SecurityCam* sc2;
    Emitter* em;
    Item* it;
    int i;
    float x;
    float z;
    int xtemp;
    int ytemp;
    int ztemp;

    //, Bump
    int t1;

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x, 240.0 * RoomScale, r->z + 752.0 * RoomScale, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 5840.0 * RoomScale, -5392.0 * RoomScale, r->z + 1360.0 * RoomScale, true);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 608.0 * RoomScale, 240.0 * RoomScale, r->z - 624.0 * RoomScale, true);

    r->objects[3] = bbCreatePivot(r->obj);
    //PositionEntity(r\objects[3], r\x + 720.0 * RoomScale, -5392.0 * RoomScale, r\z + 752.0 * RoomScale, True)
    bbPositionEntity(r->objects[3], r->x - 456.0 * RoomScale, -5392.0 * RoomScale, r->z - 1136 * RoomScale, true);

    //"waypoints" # 1
    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x + 2128.0 * RoomScale, -5550.0 * RoomScale, r->z + 2048.0 * RoomScale, true);

    r->objects[5] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[5], r->x + 2128.0 * RoomScale, -5550.0 * RoomScale, r->z - 1136.0 * RoomScale, true);

    r->objects[6] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[6], r->x + 3824.0 * RoomScale, -5550.0 * RoomScale, r->z - 1168.0 * RoomScale, true);

    r->objects[7] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[7], r->x + 3760.0 * RoomScale, -5550.0 * RoomScale, r->z + 2048.0 * RoomScale, true);

    r->objects[8] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[8], r->x + 4848.0 * RoomScale, -5550.0 * RoomScale, r->z + 112.0 * RoomScale, true);

    //"waypoints" # 2
    r->objects[9] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[9], r->x + 592.0 * RoomScale, -5550.0 * RoomScale, r->z + 6352.0 * RoomScale, true);

    r->objects[10] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[10], r->x + 2928.0 * RoomScale, -5550.0 * RoomScale, r->z + 6352.0 * RoomScale, true);

    r->objects[11] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[11], r->x + 2928.0 * RoomScale, -5550.0 * RoomScale, r->z + 5200.0 * RoomScale, true);

    r->objects[12] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[12], r->x + 592.0 * RoomScale, -5550.0 * RoomScale, r->z + 5200.0 * RoomScale, true);

    //"waypoints" # 3
    r->objects[13] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[13], r->x + 1136.0 * RoomScale, -5550.0 * RoomScale, r->z + 2944.0 * RoomScale, true);

    r->objects[14] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[14], r->x + 1104.0 * RoomScale, -5550.0 * RoomScale, r->z + 1184.0 * RoomScale, true);

    r->objects[15] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[15], r->x - 464.0 * RoomScale,  -5550.0 * RoomScale, r->z + 1216.0 * RoomScale, true);

    r->objects[16] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[16], r->x - 432.0 * RoomScale, -5550.0 * RoomScale, r->z + 2976.0 * RoomScale, true);

    //r\objects[20] = LoadMesh("GFX/Map/room3storage_hb.b3d",r\obj)
    //EntityPickMode(r\objects[20],2)
    //EntityType(r\objects[20],HIT_MAP)
    //EntityAlpha(r\objects[20],0.0)

    //Doors
    r->doors[0] = CreateDoor(r->x, 0.0, r->z + 448.0 * RoomScale, 0, r, true);
    bbPositionEntity(r->doors[0]->buttons[1], r->x - 160.0 * RoomScale, 0.7, r->z + 480.0 * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 160.0 * RoomScale, 0.7, r->z + 416.0 * RoomScale, true);

    r->doors[1] = CreateDoor(r->x + 5840.0 * RoomScale,  -5632.0 * RoomScale, r->z + 1048.0 * RoomScale, 0, r);
    bbPositionEntity(r->doors[1]->buttons[0], r->x + 6000.0 * RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z + 1008.0 * RoomScale, true);
    bbPositionEntity(r->doors[1]->buttons[1], r->x + 5680.0 * RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z + 1088.0 * RoomScale, true);

    r->doors[2] = CreateDoor(r->x + 608.0 * RoomScale, 0.0, r->z - 312.0 * RoomScale, 0, r, true);
    bbPositionEntity(r->doors[2]->buttons[1], r->x + 448.0 * RoomScale, 0.7, r->z - 272.0 * RoomScale, true);
    bbPositionEntity(r->doors[2]->buttons[0], r->x + 768.0 * RoomScale, 0.7, r->z - 352.0 * RoomScale, true);

    //r\doors[3] = CreateDoor(r\x + 720.0 * RoomScale,  -5632.0 * RoomScale, r\z + 1064.0 * RoomScale, 0, r)
    //PositionEntity(r\doors[3]\buttons[0], r\x + 896.0 * RoomScale, EntityY(r\doors[3]\buttons[0],True), r\z + 1024.0 * RoomScale, True)
    //PositionEntity(r\doors[3]\buttons[1], r\x + 544.0 * RoomScale, EntityY(r\doors[3]\buttons[1],True), r\z + 1104.0 * RoomScale, True)
    r->doors[3] = CreateDoor(r->x - 456.0 * RoomScale,  -5632.0 * RoomScale, r->z - 824.0 * RoomScale, 0, r);
    //X=+176 | Z=-40
    bbPositionEntity(r->doors[3]->buttons[0], r->x - 280.0*RoomScale, bbEntityY(r->doors[3]->buttons[0],true), r->z - 864.0 * RoomScale, true);
    //X=-176 | Z=+40
    bbPositionEntity(r->doors[3]->buttons[1], r->x - 632.0*RoomScale, bbEntityY(r->doors[3]->buttons[1],true), r->z - 784.0 * RoomScale, true);

    em = CreateEmitter(r->x + 5218.0 * RoomScale, -5584.0*RoomScale, r->z - 600* RoomScale, 0);
    bbTurnEntity(em->obj, 20, -100, 0, true);
    bbEntityParent(em->obj, r->obj);
    em->room = r;
    em->randAngle = 15;
    em->speed = 0.03;
    em->sizeChange = 0.01;
    em->aChange = -0.006;
    em->gravity = -0.2;

    switch (bbRand(3)) {
        case 1: {
            x = 2312;
            z = -952;
        } break;
        case 2: {
            x = 3032;
            z = 1288;
        } break;
        case 3: {
            x = 2824;
            z = 2808;
        } break;
    }

    it = CreateItem("nvgoggles", r->x + 1936.0 * RoomScale, r->y - 5496.0 * RoomScale, r->z - 944.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    de = CreateDecal(DECAL_BLOOD_SPLATTER,  r->x + x*RoomScale, -5632.0*RoomScale+0.01, r->z+z*RoomScale,90,bbRnd(360),0);
    de->size = 0.5;
    bbScaleSprite(de->obj, de->size,de->size);
    bbEntityParent(de->obj, r->obj);

    //Objects [20],[21],[22],[23]
    int n;
    for (n = 10; n <= 11; n++) {
        r->levers[n-10] = CreateLever();

        bbScaleEntity(r->levers[n-10]->obj, 0.04, 0.04, 0.04);
        bbScaleEntity(r->levers[n-10]->baseObj, 0.04, 0.04, 0.04);

        if (n == 10) {
            //r\z+6578
            bbPositionEntity(r->levers[n-10]->obj,r->x+3101*RoomScale,r->y-5461*RoomScale,r->z+6568*RoomScale,true);
            bbPositionEntity(r->levers[n-10]->baseObj,r->x+3101*RoomScale,r->y-5461*RoomScale,r->z+6568*RoomScale,true);
        } else {
            //r\z+3174
            bbPositionEntity(r->levers[n-10]->obj,r->x+1209*RoomScale,r->y-5461*RoomScale,r->z+3164*RoomScale,true);
            bbPositionEntity(r->levers[n-10]->baseObj,r->x+1209*RoomScale,r->y-5461*RoomScale,r->z+3164*RoomScale,true);
        }

        bbEntityParent(r->levers[n-10]->obj, r->obj);
        bbEntityParent(r->levers[n-10]->baseObj, r->obj);

        bbRotateEntity(r->levers[n-10]->baseObj, 0, 0, 0);
        bbRotateEntity(r->levers[n-10]->obj, -10, 0 - 180, 0);

        bbEntityPickMode(r->levers[n-10]->obj, 1, false);
        bbEntityRadius(r->levers[n-10]->obj, 0.1);
    }

    r->doors[4] = CreateDoor(r->x+56*RoomScale,r->y-5632*RoomScale,r->z+6344*RoomScale,90,r,false,DOOR_TYPE_HCZ);
    r->doors[4]->autoClose = false;
    r->doors[4]->open = false;
    for (i = 0; i <= 1; i++) {
        bbFreeEntity(r->doors[4]->buttons[i]);
        r->doors[4]->buttons[i] = 0;
    }

    d = CreateDoor(r->x+1157.0*RoomScale,r->y-5632.0*RoomScale,r->z+660.0*RoomScale,0,r,false,DOOR_TYPE_HCZ);
    d->locked = true;
    d->open = false;
    d->autoClose = false;
    for (i = 0; i <= 1; i++) {
        bbFreeEntity(d->buttons[i]);
        d->buttons[i] = 0;
    }

    d = CreateDoor(r->x+234.0*RoomScale,r->y-5632.0*RoomScale,r->z+5239.0*RoomScale,90,r,false,DOOR_TYPE_HCZ);
    d->locked = true;
    d->open = false;
    d->autoClose = false;
    for (i = 0; i <= 1; i++) {
        bbFreeEntity(d->buttons[i]);
        d->buttons[i] = 0;
    }

    d = CreateDoor(r->x+3446.0*RoomScale,r->y-5632.0*RoomScale,r->z+6369.0*RoomScale,90,r,false,DOOR_TYPE_HCZ);
    d->locked = true;
    d->open = false;
    d->autoClose = false;
    for (i = 0; i <= 1; i++) {
        bbFreeEntity(d->buttons[i]);
        d->buttons[i] = 0;
    }
}

void UpdateEvent_strg_939_3(Event* e) {
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
    if (mainPlayer->currRoom == e->room) {
        e->eventState2 = UpdateElevators2(e->eventState2, e->room->doors[0], e->room->doors[1],e->room->objects[0],e->room->objects[1], e);

        e->eventState3 = UpdateElevators2(e->eventState3, e->room->doors[2], e->room->doors[3],e->room->objects[2],e->room->objects[3], e);

        if (bbEntityY(mainPlayer->collider)<-4600*RoomScale) {

            //If (Music(7)=0) Then Music(7) = LoadSound("SFX/Music/Room3Storage.ogg") ;TODO: fix
            //ShouldPlay = 7

            //If (e\room\npc[0]=Null) Then
            //DrawLoading(0, True)
            //e\room\npc[0]=CreateNPC(NPCtype939, 0,0,0)

            //DrawLoading(20, True)
            //e\room\npc[1]=CreateNPC(NPCtype939, 0,0,0)

            //DrawLoading(50, True)
            //e\room\npc[2]=CreateNPC(NPCtype939, 0,0,0)

            //DrawLoading(100, True)
            //EndIf
            if (e->room->npc[2]==nullptr | e->eventState == 3) {
                if (e->eventState == 0) {
                    e->eventState = 1;
                } else if ((e->eventState == 1)) {
                    e->room->npc[0] = CreateNPC(NPCtype939, 0,0,0);
                    e->eventState = 2;
                } else if ((e->eventState == 2)) {
                    e->room->npc[1] = CreateNPC(NPCtype939, 0,0,0);
                    e->eventState = 3;
                } else if ((e->eventState == 3)) {
                    e->room->npc[2] = CreateNPC(NPCtype939, 0,0,0);
                    e->eventState = 0;
                }
            } else {
                if (e->eventState == 0) {
                    //Instance 1
                    bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[4],true),bbEntityY(e->room->objects[4],true)+0.2,bbEntityZ(e->room->objects[4],true));
                    bbResetEntity(e->room->npc[0]->collider);
                    e->room->npc[0]->state = 2;
                    e->room->npc[0]->state2 = 5;
                    e->room->npc[0]->prevState = 7;
                    //Instance 2
                    bbPositionEntity(e->room->npc[1]->collider, bbEntityX(e->room->objects[9],true),bbEntityY(e->room->objects[9],true)+0.2,bbEntityZ(e->room->objects[9],true));
                    bbResetEntity(e->room->npc[1]->collider);
                    e->room->npc[1]->state = 2;
                    e->room->npc[1]->state2 = 10;
                    e->room->npc[1]->prevState = 12;
                    //Instance 3
                    bbPositionEntity(e->room->npc[2]->collider, bbEntityX(e->room->objects[13],true),bbEntityY(e->room->objects[13],true)+0.2,bbEntityZ(e->room->objects[13],true));
                    bbResetEntity(e->room->npc[2]->collider);
                    e->room->npc[2]->state = 2;
                    e->room->npc[2]->state2 = 14;
                    e->room->npc[2]->prevState = 16;
                    //Other
                    e->eventState = 1;
                }

                if (e->room->doors[4]->open == false) {
                    if (e->room->levers[0]->succ | e->room->levers[1]->succ) {
                        e->room->doors[4]->open = true;
                        if (e->sounds[1] != 0) {
                            bbFreeSound(e->sounds[1]);
                            e->sounds[1] = 0;
                        }

                        e->sounds[1] = bbLoadSound("SFX/Door/Door2Open1_dist.ogg");
                        e->soundChannels[1] = PlayRangedSound(e->sounds[1],mainPlayer->cam,e->room->doors[4]->obj,400);

                        e->room->levers[0]->locked = true;
                        e->room->levers[1]->locked = true;
                    }
                }

                e->room->npc[0]->ignorePlayer = false;
                e->room->npc[2]->ignorePlayer = false;

                // TODO: No bad.
                //CurrTrigger = CheckTriggers();

                //switch (CurrTrigger) {
                //    case "939-1_fix": {
                //        e->room->npc[0]->ignorePlayer = true;
                //    }
                //    case "939-3_fix": {
                //        e->room->npc[2]->ignorePlayer = true;
                //    }
                //}

                if (bbChannelPlaying(e->soundChannels[1])) {
                    UpdateRangedSoundOrigin(e->soundChannels[1],mainPlayer->cam,e->room->doors[4]->obj,400);
                }

                if (bbEntityY(mainPlayer->collider)<-6400*RoomScale & mainPlayer->dead == false) {
                    DeathMSG = "";
                    PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Impact.ogg"));
                    mainPlayer->dead = true;
                }
            }
        } else {
            e->eventState = 0;
            if (e->room->npc[0]!=nullptr) {
                e->room->npc[0]->state = 66;
            }
            if (e->room->npc[1]!=nullptr) {
                e->room->npc[1]->state = 66;
            }
            if (e->room->npc[2]!=nullptr) {
                e->room->npc[2]->state = 66;
            }
        }
    } else {
        if (e->room->npc[0]!=nullptr) {
            e->room->npc[0]->state = 66;
        }
        if (e->room->npc[1]!=nullptr) {
            e->room->npc[1]->state = 66;
        }
        if (e->room->npc[2]!=nullptr) {
            e->room->npc[2]->state = 66;
        }
    }
    //[End Block]
}

}

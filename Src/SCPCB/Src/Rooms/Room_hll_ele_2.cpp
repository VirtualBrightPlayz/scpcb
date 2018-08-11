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
#include "Room_hll_ele_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_ele_2(Room* r) {
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

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x+888.0*RoomScale, 240.0*RoomScale, r->z, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x+1024.0*RoomScale-0.01, 120.0*RoomScale, r->z, true);

    r->doors[0] = CreateDoor(r->x + 448.0 * RoomScale, 0.0, r->z, 90, r);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 416.0 * RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z - 208.0 * RoomScale,true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 480.0 * RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z + 184.0 * RoomScale,true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    r->doors[0]->locked = true;
}

void UpdateEventRoom2elevator2(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;
    Texture* tex;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (e->room->dist < 8.0 & e->room->dist > 0) {

        de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[0],true), 0.0005, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);

        de = CreateDecal(DECAL_BLOOD_POOL, bbEntityX(e->room->objects[0],true), 0.002, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);
        de->size = 0.5;

        e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[0],true), 0.5, bbEntityZ(e->room->objects[0],true));
        e->room->npc[0]->texture = "GFX/NPCs/classd/gonzales.jpg";
        tex = bbLoadTexture(e->room->npc[0]->texture);
        bbEntityTexture(e->room->npc[0]->obj, tex);
        bbFreeTexture(tex);

        bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->obj)-80,0, true);

        SetNPCFrame(e->room->npc[0], 19);
        e->room->npc[0]->state = 8;

        RemoveEvent(e);
    }
    //[End Block]
}

void UpdateEvent_hll_ele_2(Event* e) {
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
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (e->eventState == 0) {
        if (e->room->dist < 8.0 & e->room->dist > 0) {
            e->room->npc[0] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->obj,true), 0.5, bbEntityZ(e->room->obj,true));
            bbPointEntity(e->room->npc[0]->collider, mainPlayer->collider);
            bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider),0, true);

            e->eventState = 1;
        }
    } else {
        if (e->eventState == 1) {
            if (e->room->dist<5.0 | bbRand(700)==1) {
                e->eventState = 2;

                e->room->npc[0]->state = 5;
                e->room->npc[0]->enemyX = bbEntityX(e->room->objects[1],true);
                e->room->npc[0]->enemyY = bbEntityY(e->room->objects[1],true);
                e->room->npc[0]->enemyZ = bbEntityZ(e->room->objects[1],true);
            }
        } else if ((e->eventState == 2)) {
            if (bbEntityDistance(e->room->npc[0]->collider,e->room->objects[1])<2.0) {
                e->room->doors[0]->open = false;
                //PlayRangedSound(CloseDoorSFX(0, 0), mainPlayer\cam, e\room\doors[0]\obj, 8.0)

                PlaySound2((LoadTempSound("SFX/Room/Room2ElevatorDeath.ogg")));

                e->eventState = 2.05;
            }
        } else if ((e->eventState < 13*70)) {
            e->eventState = e->eventState+timing->tickDuration;
            //6.7 - 7.4
            //8.6 - 10
            if (e->eventState > 6.7*70 & e->eventState < 7.4*70) {
                mainPlayer->camShake = 7.4-(e->eventState/70.0);
            } else if ((e->eventState > 8.6*70 & e->eventState < 10.6*70)) {
                mainPlayer->camShake = 10.6-(e->eventState/70.0);
            } else if ((e->eventState > 12.6*70)) {
                mainPlayer->camShake = 0;
                if (e->eventState-timing->tickDuration < 12.6*70 & e->room->npc[0]!=nullptr) {
                    RemoveNPC(e->room->npc[0]);
                    e->room->npc[0] = nullptr;

                    de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[0],true), 0.0005, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);

                    de = CreateDecal(DECAL_BLOOD_POOL, bbEntityX(e->room->objects[0],true), 0.002, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);
                    de->size = 0.5;

                    de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[1],true), bbEntityY(e->room->objects[1],true), bbEntityZ(e->room->objects[1],true),0,e->room->angle+270,0);
                    de->size = 0.9;
                }
                e->room->doors[0]->locked = false;
            }
        } else {
            if (e->room->doors[0]->open) {
                e->room->doors[0]->locked = true;
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

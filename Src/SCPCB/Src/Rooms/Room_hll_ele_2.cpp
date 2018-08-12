#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
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
    bbPositionEntity(r->objects[0], r->x+888.f*RoomScale, 240.f*RoomScale, r->z, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x+1024.f*RoomScale-0.01f, 120.f*RoomScale, r->z, true);

    r->doors[0] = CreateDoor(r->x + 448.f * RoomScale, 0.f, r->z, 90, r);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 416.f * RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z - 208.f * RoomScale,true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 480.f * RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z + 184.f * RoomScale,true);
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
    if (e->room->dist < 8.f && e->room->dist > 0) {

        de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[0],true), 0.0005f, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);

        de = CreateDecal(DECAL_BLOOD_POOL, bbEntityX(e->room->objects[0],true), 0.002f, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);
        de->size = 0.5f;

        e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[0],true), 0.5f, bbEntityZ(e->room->objects[0],true));
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
        if (e->room->dist < 8.f && e->room->dist > 0) {
            e->room->npc[0] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->obj,true), 0.5f, bbEntityZ(e->room->obj,true));
            bbPointEntity(e->room->npc[0]->collider, mainPlayer->collider);
            bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider),0, true);

            e->eventState = 1;
        }
    } else {
        if (e->eventState == 1) {
            if (e->room->dist<5.f || bbRand(700)==1) {
                e->eventState = 2;

                e->room->npc[0]->state = 5;
                e->room->npc[0]->enemyX = bbEntityX(e->room->objects[1],true);
                e->room->npc[0]->enemyY = bbEntityY(e->room->objects[1],true);
                e->room->npc[0]->enemyZ = bbEntityZ(e->room->objects[1],true);
            }
        } else if ((e->eventState == 2)) {
            if (bbEntityDistance(e->room->npc[0]->collider,e->room->objects[1])<2.f) {
                e->room->doors[0]->open = false;
                //PlayRangedSound(CloseDoorSFX(0, 0), mainPlayer\cam, e\room\doors[0]\obj, 8.f)

                PlaySound2((LoadTempSound("SFX/Room/Room2ElevatorDeath.ogg")));

                e->eventState = 2.05f;
            }
        } else if ((e->eventState < 13*70)) {
            e->eventState = e->eventState+timing->tickDuration;
            //6.7f - 7.4f
            //8.6f - 10
            if (e->eventState > 6.7f*70 && e->eventState < 7.4f*70) {
                mainPlayer->camShake = 7.4f-(e->eventState/70.f);
            } else if ((e->eventState > 8.6f*70 && e->eventState < 10.6f*70)) {
                mainPlayer->camShake = 10.6f-(e->eventState/70.f);
            } else if ((e->eventState > 12.6f*70)) {
                mainPlayer->camShake = 0;
                if (e->eventState-timing->tickDuration < 12.6f*70 && e->room->npc[0]!=nullptr) {
                    RemoveNPC(e->room->npc[0]);
                    e->room->npc[0] = nullptr;

                    de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[0],true), 0.0005f, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);

                    de = CreateDecal(DECAL_BLOOD_POOL, bbEntityX(e->room->objects[0],true), 0.002f, bbEntityZ(e->room->objects[0],true),90,bbRnd(360),0);
                    de->size = 0.5f;

                    de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(e->room->objects[1],true), bbEntityY(e->room->objects[1],true), bbEntityZ(e->room->objects[1],true),0,e->room->angle+270,0);
                    de->size = 0.9f;
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

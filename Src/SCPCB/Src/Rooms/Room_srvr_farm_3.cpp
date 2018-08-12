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
#include "Room_srvr_farm_3.h"

namespace CBN {

// Functions.
void FillRoom_srvr_farm_3(Room* r) {
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

    it = CreateItem("battery", r->x - 132.f * RoomScale, r->y - 368.f * RoomScale, r->z - 648.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 76.f * RoomScale, r->y - 368.f * RoomScale, r->z - 648.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 196.f * RoomScale, r->y - 368.f * RoomScale, r->z - 648.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    it = CreateItem("navigator", r->x + 124.f * RoomScale, r->y - 368.f * RoomScale, r->z - 648.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 736.f * RoomScale, -512.f * RoomScale, r->z - 400.f * RoomScale, true);
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x - 552.f * RoomScale, -512.f * RoomScale, r->z - 528.f * RoomScale, true);
    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 736.f * RoomScale, -512.f * RoomScale, r->z + 272.f * RoomScale, true);

    r->objects[3] = bbLoadMesh("GFX/npcs/duck_low_res.b3d");
    bbScaleEntity(r->objects[3], 0.07f, 0.07f, 0.07f);
    Texture* tex = bbLoadTexture("GFX/npcs/duck2.png");
    bbEntityTexture((Model*)r->objects[3], tex);
    bbPositionEntity(r->objects[3], r->x + 928.f * RoomScale, -640*RoomScale, r->z + 704.f * RoomScale);

    bbEntityParent(r->objects[3], r->obj);
    bbFreeTexture(tex);
}

void UpdateEvent_srvr_farm_3(Event* e) {
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
        if (e->eventState3==0 && Curr173->idle == 0) {
            if (mainPlayer->blinkTimer < -10) {
                temp = bbRand(0,2);
                bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[temp],true),bbEntityY(e->room->objects[temp],true),bbEntityZ(e->room->objects[temp],true));
                bbResetEntity(Curr173->collider);
                e->eventState3 = 1;
            }
        }

        if (e->room->objects[3]>0) {
            if (mainPlayer->blinkTimer<-8 && mainPlayer->blinkTimer >-12) {
                bbPointEntity(e->room->objects[3], mainPlayer->cam);
                bbRotateEntity(e->room->objects[3], 0, bbEntityYaw(e->room->objects[3],true),0, true);
            }
            if (e->eventState2 == 0) {
                e->eventState = CurveValue(0, e->eventState, 15.f);
                if (bbRand(800)==1) {
                    e->eventState2 = 1;
                }
            } else {
                e->eventState = e->eventState+(timing->tickDuration*0.5f);
                if (e->eventState > 360) {
                    e->eventState = 0;
                }

                if (bbRand(1200)==1) {
                    e->eventState2 = 0;
                }
            }

            bbPositionEntity(e->room->objects[3], bbEntityX(e->room->objects[3],true), (-608.f*RoomScale)+0.05f+bbSin(e->eventState+270)*0.05f, bbEntityZ(e->room->objects[3],true), true);
        }
    }
    //[End Block]
}

}

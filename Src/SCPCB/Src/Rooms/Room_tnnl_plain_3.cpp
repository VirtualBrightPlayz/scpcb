#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>

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
#include "Room_tnnl_plain_3.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_plain_3(Room* r) {
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
    bbPositionEntity(r->objects[0], r->x - 190.0*RoomScale, 4.0*RoomScale, r->z+190.0*RoomScale, true);
}

void UpdateEvent_tnnl_plain_3(Event* e) {
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
    if (e->eventState == 0) {
        e->room->npc[0] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->objects[0],true), bbEntityY(e->room->objects[0],true)+0.5, bbEntityZ(e->room->objects[0],true));
        bbPointEntity(e->room->npc[0]->collider, e->room->obj);
        bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider)+bbRnd(-20,20),0, true);
        SetNPCFrame(e->room->npc[0], 906);
        e->room->npc[0]->state = 8;

        e->eventState = 1;
        RemoveEvent(e);
    }
    //[End Block]
}

}

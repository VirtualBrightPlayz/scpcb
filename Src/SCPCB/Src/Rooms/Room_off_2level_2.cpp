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
#include "Room_off_2level_2.h"

namespace CBN {

// Functions.
void FillRoom_off_2level_2(Room* r) {
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

    if (bbRand(2)==1) {
        it = CreatePaper("docMTF", r->x + 744.0 * RoomScale, r->y +240.0 * RoomScale, r->z + 944.0 * RoomScale);
        bbEntityParent(it->collider, r->obj);
    } else {
        it = CreatePaper("docSC", r->x + 680.0 * RoomScale, r->y +240.0 * RoomScale, r->z + 944.0 * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    it = CreatePaper("docOBJC", r->x + 160.0 * RoomScale, r->y +240.0 * RoomScale, r->z + 568.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("radio", r->x - 1184.0 * RoomScale, r->y + 480.0 * RoomScale, r->z - 800.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    for (i = 0; i <= bbRand(0,1); i++) {
        it = CreateItem("eyedrops", r->x - 1529.0*RoomScale, r->y + 563.0 * RoomScale, r->z - 572.0*RoomScale + i*0.05);
        bbEntityParent(it->collider, r->obj);
    }

    it = CreateItem("battery", r->x - 1545.0 * RoomScale, r->y + 603.0 * RoomScale, r->z - 372.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 1540.0 * RoomScale, r->y + 603.0 * RoomScale, r->z - 340.0 * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 1529.0 * RoomScale, r->y + 603.0 * RoomScale, r->z - 308.0 * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    r->doors[0] = CreateDoor(r->x - 1056.0 * RoomScale, 384.0*RoomScale, r->z + 290.0 * RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[0], bbEntityX(r->doors[0]->buttons[0],true),bbEntityY(r->doors[0]->buttons[0],true),r->z + 161.0 * RoomScale,true);
    bbPositionEntity(r->doors[0]->buttons[1], bbEntityX(r->doors[0]->buttons[1],true),bbEntityY(r->doors[0]->buttons[1],true),r->z + 161.0 * RoomScale,true);
}

void UpdateEvent_off_2level_2(Event* e) {
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
        e->eventState = e->eventState+timing->tickDuration;
        if (e->eventState > 700) {
            if (bbEntityDistance(e->room->doors[0]->obj, mainPlayer->collider)>0.5) {
                if (bbEntityInView(e->room->doors[0]->obj, mainPlayer->cam)==false) {
                    DebugLog("%@@ = / {2E6C2==FD gi`h]c");
                    e->room->doors[0]->open = false;
                    RemoveEvent(e);
                }
            }
        }
    }
    //[End Block]
}

}

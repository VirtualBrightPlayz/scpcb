#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "Room_off_2level_2.h"

namespace CBN {

// Functions.
void FillRoom_off_2level_2(Room* r) {
    Item* it = nullptr;
    if (bbRand(2)==1) {
        it = CreatePaper("docMTF", r->x + 744.f * RoomScale, r->y +240.f * RoomScale, r->z + 944.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    } else {
        it = CreatePaper("docSC", r->x + 680.f * RoomScale, r->y +240.f * RoomScale, r->z + 944.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    it = CreatePaper("docOBJC", r->x + 160.f * RoomScale, r->y +240.f * RoomScale, r->z + 568.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("radio", r->x - 1184.f * RoomScale, r->y + 480.f * RoomScale, r->z - 800.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    for (int i = 0; i < bbRand(1,2); i++) {
        it = CreateItem("eyedrops", r->x - 1529.f*RoomScale, r->y + 563.f * RoomScale, r->z - 572.f*RoomScale + i*0.05f);
        bbEntityParent(it->collider, r->obj);
    }

    it = CreateItem("battery", r->x - 1545.f * RoomScale, r->y + 603.f * RoomScale, r->z - 372.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 1540.f * RoomScale, r->y + 603.f * RoomScale, r->z - 340.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x - 1529.f * RoomScale, r->y + 603.f * RoomScale, r->z - 308.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    r->doors[0] = CreateDoor(r->x - 1056.f * RoomScale, 384.f*RoomScale, r->z + 290.f * RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[0], bbEntityX(r->doors[0]->buttons[0],true),bbEntityY(r->doors[0]->buttons[0],true),r->z + 161.f * RoomScale,true);
    bbPositionEntity(r->doors[0]->buttons[1], bbEntityX(r->doors[0]->buttons[1],true),bbEntityY(r->doors[0]->buttons[1],true),r->z + 161.f * RoomScale,true);
}

void UpdateEvent_off_2level_2(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        e->eventState = e->eventState+timing->tickDuration;
        if (e->eventState > 700) {
            if (bbEntityDistance(e->room->doors[0]->obj, mainPlayer->collider)>0.5f) {
                if (bbEntityInView(e->room->doors[0]->obj, mainPlayer->cam)==false) {
                    e->room->doors[0]->open = false;
                    RemoveEvent(e);
                }
            }
        }
    }

}

}

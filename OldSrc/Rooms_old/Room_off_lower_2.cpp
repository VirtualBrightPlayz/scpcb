#include <bbblitz3d.h>
#include <bbmath.h>

#include "../MapSystem.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "Room_off_lower_2.h"

namespace CBN {

// Functions.
void FillRoom_off_lower_2(Room* r) {
    Item* it = CreatePaper("doc895", r->x - 800.f * RoomScale, r->y - 48.f * RoomScale, r->z + 368.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    if (bbRand(2) == 1) {
        it = CreatePaper("doc860", r->x - 800.f * RoomScale, r->y - 48.f * RoomScale, r->z - 464.f * RoomScale);
    } else {
        it = CreatePaper("doc093rm", r->x - 800.f * RoomScale, r->y - 48.f * RoomScale, r->z - 464.f * RoomScale);
    }
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("navigator", r->x - 336.f * RoomScale, r->y - 48.f * RoomScale, r->z - 480.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    r->objects[0] = bbLoadMesh("GFX/NPCs/ducks/duck.b3d");
    bbScaleEntity(r->objects[0], 0.07f, 0.07f, 0.07f);

    bbEntityParent(r->objects[0], r->obj);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x-808.f * RoomScale, -72.f * RoomScale, r->z - 40.f * RoomScale, true);
    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x-488.f * RoomScale, 160.f * RoomScale, r->z + 700.f * RoomScale, true);
    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x-488.f * RoomScale, 160.f * RoomScale, r->z - 668.f * RoomScale, true);
    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x-572.f * RoomScale, 350.f * RoomScale, r->z - 4.f * RoomScale, true);

    int temp = bbRand(1,4);
    bbPositionEntity(r->objects[0], bbEntityX(r->objects[temp],true),bbEntityY(r->objects[temp],true),bbEntityZ(r->objects[temp],true),true);
}

void UpdateEvent_off_lower_2(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (mainPlayer->blinkTimer<-8 && mainPlayer->blinkTimer >-12) {
            int temp = bbRand(1,4);
            bbPositionEntity(e->room->objects[0], bbEntityX(e->room->objects[temp],true),bbEntityY(e->room->objects[temp],true),bbEntityZ(e->room->objects[temp],true),true);
            bbRotateEntity(e->room->objects[0], 0, bbRnd(360), 0);
        }
    }
}

}

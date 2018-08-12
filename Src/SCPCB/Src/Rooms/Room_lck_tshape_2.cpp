#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "Room_lck_tshape_2.h"

namespace CBN {

// Functions.
void FillRoom_lck_tshape_2(Room* r) {
    Door* d = CreateDoor(r->x, 0, r->z + 528.f * RoomScale, 0, r, true);
    //: d\buttons[0] = False
    d->autoClose = false;
    bbPositionEntity(d->buttons[0], r->x - 832.f * RoomScale, 0.7f, r->z + 160.f * RoomScale, true);
    bbPositionEntity(d->buttons[1], r->x + 160.f * RoomScale, 0.7f, r->z + 536.f * RoomScale, true);
    //RotateEntity(d\buttons[1], 0, 90, 0, True)

    Door* d2 = CreateDoor(r->x, 0, r->z - 528.f * RoomScale, 180, r, true);
    d2->autoClose = false;
    bbFreeEntity(d2->buttons[0]);
    d2->buttons[0] = 0;
    bbPositionEntity(d2->buttons[1], r->x +160.f * RoomScale, 0.7f, r->z - 536.f * RoomScale, true);
    //RotateEntity(d2\buttons[1], 0, 90, 0, True)

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x - 832.f * RoomScale, 0.5f, r->z);
    bbEntityParent(r->objects[0], r->obj);

    d2->linkedDoor = d;
    d->linkedDoor = d2;

    d->open = false;
    d2->open = true;
}

void UpdateEventRoom2doors173(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (e->eventState == 0 && Curr173->idle == 0) {
            if (!bbEntityInView(Curr173->obj, mainPlayer->cam)) {
                e->eventState = 1;
                bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[0], true), 0.5f, bbEntityZ(e->room->objects[0], true));
                bbResetEntity(Curr173->collider);
                RemoveEvent(e);
            }
        }
    }

}

}

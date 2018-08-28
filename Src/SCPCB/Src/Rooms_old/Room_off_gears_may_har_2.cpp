#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Menus/Menu.h"
#include "Room_off_gears_may_har_2.h"

namespace CBN {

// Functions.
void FillRoom_off_gears_may_har_2(Room* r) {
    Door* d = CreateDoor(r->x + 240.f * RoomScale, 0.f, r->z + 448.f * RoomScale, 90, r, false, DOOR_TYPE_DEF, "", String(AccessCode));
    bbPositionEntity(d->buttons[0], r->x + 248.f * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x + 232.f * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x - 496.f * RoomScale, 0.f, r->z, 90, r, false, DOOR_TYPE_DEF, "", "ABCD");
    bbPositionEntity(d->buttons[0], r->x - 488.f * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x - 504.f * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;
    d->locked = true;

    d = CreateDoor(r->x + 240.f * RoomScale, 0.f, r->z - 576.f * RoomScale, 90, r, false, DOOR_TYPE_DEF, "", "7816");
    bbPositionEntity(d->buttons[0], r->x + 248.f * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x + 232.f * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;

    Item* it = CreatePaper("big_reveal", r->x + 736.f * RoomScale, r->y + 224.f * RoomScale, r->z + 544.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreateItem("vest", r->x + 608.f * RoomScale, r->y + 112.f * RoomScale, r->z + 32.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);

    it = CreatePaper("docIR106", r->x + 704.f * RoomScale, r->y + 183.f * RoomScale, r->z - 576.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreatePaper("journal", r->x + 912 * RoomScale, r->y + 176.f * RoomScale, r->z - 160.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreateItem("firstaid", r->x + 912.f * RoomScale, r->y + 112.f * RoomScale, r->z - 336.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);
}

}

#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_off_rosewood_2.h"

namespace CBN {

// Functions.
void FillRoom_off_rosewood_2(Room* r) {
    Door* d = CreateDoor(r->x + 1440.f * RoomScale, 224.f * RoomScale, r->z + 32.f * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    Item* it = CreateItem("scp420j", r->x + 1776.f * RoomScale, r->y + 400.f * RoomScale, r->z + 427.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("scp420j", r->x + 1808.f * RoomScale, r->y + 400.f * RoomScale, r->z + 435.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("docWar", r->x + 2248.f * RoomScale, r->y + 440.f * RoomScale, r->z + 372.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("radio", r->x + 2240.f * RoomScale, r->y + 320.f * RoomScale, r->z + 128.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

}

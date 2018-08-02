#include "Room_off_rosewood_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_off_rosewood_2(Room* r) {
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

    d = CreateDoor(r->x + 1440.0 * RoomScale, 224.0 * RoomScale, r->z + 32.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    it = CreateItem("scp420j", r->x + 1776.0 * RoomScale, r->y + 400.0 * RoomScale, r->z + 427.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("scp420j", r->x + 1808.0 * RoomScale, r->y + 400.0 * RoomScale, r->z + 435.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("docWar", r->x + 2248.0 * RoomScale, r->y + 440.0 * RoomScale, r->z + 372.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("radio", r->x + 2240.0 * RoomScale, r->y + 320.0 * RoomScale, r->z + 128.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

}

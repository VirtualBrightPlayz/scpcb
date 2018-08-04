#include "Room_srvr_pc_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_srvr_pc_2(Room* r) {
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

    d = CreateDoor(r->x + 264.0 * RoomScale, 0.0, r->z + 672.0 * RoomScale, 270, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    bbPositionEntity(d->buttons[0], r->x + 224.0 * RoomScale, bbEntityY(d->buttons[0],true), r->z + 880.0 * RoomScale, true);
    bbPositionEntity(d->buttons[1], r->x + 304.0 * RoomScale, bbEntityY(d->buttons[1],true), r->z + 840.0 * RoomScale, true);
    bbTurnEntity(d->buttons[1],0,0,0,true);
    d = CreateDoor(r->x -512.0 * RoomScale, -768.0*RoomScale, r->z -336.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d = CreateDoor(r->x -509.0 * RoomScale, -768.0*RoomScale, r->z -1037.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->locked = true;
    it = CreateItem("nvgoggles", r->x + 56.0154 * RoomScale, r->y - 648.0 * RoomScale, r->z + 749.638 * RoomScale);
    it->state = 20;
    bbRotateEntity(it->collider, 0, r->angle+bbRand(245), 0);
    bbEntityParent(it->collider, r->obj);
}

}

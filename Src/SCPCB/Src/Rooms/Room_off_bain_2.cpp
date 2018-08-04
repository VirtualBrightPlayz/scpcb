#include "Room_off_bain_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_off_bain_2(Room* r) {
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

    d = CreateDoor(r->x - 240.0 * RoomScale, 0.0, r->z, 90, r, false);
    d->open = false;
    d->autoClose = false;
}

}

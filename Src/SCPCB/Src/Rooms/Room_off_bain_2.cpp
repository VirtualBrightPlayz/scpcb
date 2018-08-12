#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_off_bain_2.h"

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

    d = CreateDoor(r->x - 240.f * RoomScale, 0.f, r->z, 90, r, false);
    d->open = false;
    d->autoClose = false;
}

}

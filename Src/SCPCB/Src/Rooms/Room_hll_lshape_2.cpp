#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_hll_lshape_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_lshape_2(Room* r) {
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

    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6], r->x + 640.f * RoomScale, 8.f * RoomScale, r->z - 896.f * RoomScale);
    bbEntityParent(r->objects[6], r->obj);
}

}

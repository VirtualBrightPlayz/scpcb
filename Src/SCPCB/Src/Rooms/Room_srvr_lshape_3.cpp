#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_srvr_lshape_3.h"

namespace CBN {

// Functions.
void FillRoom_srvr_lshape_3(Room* r) {
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

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x - 504.f * RoomScale, -512.f * RoomScale, r->z + 271.f * RoomScale, true);
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 628.f * RoomScale, -512.f * RoomScale, r->z + 271.f * RoomScale, true);
    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x - 532.f * RoomScale, -512.f * RoomScale, r->z - 877.f * RoomScale, true);

    it = CreatePaper("doc970", r->x + 960.f * RoomScale, r->y - 448.f * RoomScale, r->z + 251.f * RoomScale);
    bbRotateEntity(it->collider, 0, r->angle, 0);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("gasmask", r->x + 954.f * RoomScale, r->y - 504.f * RoomScale, r->z + 235.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

}

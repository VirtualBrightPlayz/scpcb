#include "Room_off_glss_3.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_off_glss_3(Room* r) {
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

    d = CreateDoor(r->x + 736.0 * RoomScale, 0.0, r->z + 240.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    bbPositionEntity(d->buttons[0], r->x + 892.0 * RoomScale, bbEntityY(d->buttons[0],true), r->z + 224.0 * RoomScale, true);
    bbPositionEntity(d->buttons[1], r->x + 892.0 * RoomScale, bbEntityY(d->buttons[1],true), r->z + 255.0 * RoomScale, true);
    bbFreeEntity(d->obj2);
    d->obj2 = 0;

    //r\objects[0] = LoadMesh("GFX/Map/room3offices_hb.b3d",r\obj)
    //EntityPickMode(r\objects[0],2)
    //EntityType(r\objects[0],HIT_MAP)
    //EntityAlpha(r\objects[0],0.0)
}

}

#include "Room_lck_ez_3.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_lck_ez_3(Room* r) {
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

    d = CreateDoor(r->x - 728.0 * RoomScale, 0.0, r->z - 458.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    d = CreateDoor(r->x - 223.0 * RoomScale, 0.0, r->z - 736.0 * RoomScale, -90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    r->doors[0] = CreateDoor(r->x - 459.0 * RoomScale, 0.0, r->z + 339.0 * RoomScale, 90, r);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 580.822 * RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z - 606.679 * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 580.822 * RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z - 606.679 * RoomScale, true);
    r->doors[0]->locked = true;
    r->doors[0]->mtfClose = false;

    r->doors[1] = CreateDoor(r->x + 385.0 * RoomScale, 0.0, r->z + 339.0 * RoomScale, 270, r);
    bbPositionEntity(r->doors[1]->buttons[0], r->x + 580.822 * RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z - 606.679 * RoomScale, true);
    bbPositionEntity(r->doors[1]->buttons[1], r->x + 580.822 * RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z - 606.679 * RoomScale, true);
    r->doors[1]->locked = true;
    r->doors[1]->mtfClose = false;
    bbFreeEntity(r->doors[1]->obj2);
    r->doors[1]->obj2 = 0;

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0],r->x-48.0*RoomScale,128.0*RoomScale,r->z+320.0*RoomScale);
    bbEntityParent(r->objects[0],r->obj);

    for (int iterator188 = 0; iterator188 < Room::getListSize(); iterator188++) {
        r2 = Room::getObject(iterator188);

        if (r2!=r) {
            if (r2->roomTemplate->name == "room3gw") {
                //don't load the mesh again
                r->objects[3] = bbCopyMeshModelEntity(r2->objects[3],r->obj);
                break;
            }
        }
    }
    if (r->objects[3]==0) {
        r->objects[3] = bbLoadMesh("GFX/Map/room3gw_pipes.b3d",r->obj);
    }
    bbEntityPickMode(r->objects[3],2);
}

}

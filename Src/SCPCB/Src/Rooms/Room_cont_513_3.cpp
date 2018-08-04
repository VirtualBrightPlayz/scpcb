#include "Room_cont_513_3.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_cont_513_3(Room* r) {
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

    d = CreateDoor(r->x - 704.0 * RoomScale, 0, r->z + 304.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    //: d\buttons[0] = False
    d->autoClose = false;
    bbPositionEntity(d->buttons[0], bbEntityX(d->buttons[0],true), bbEntityY(d->buttons[0],true), r->z + 288.0 * RoomScale, true);
    bbPositionEntity(d->buttons[1], bbEntityX(d->buttons[1],true), bbEntityY(d->buttons[1],true), r->z + 320.0 * RoomScale, true);

    sc = CreateSecurityCam(r->x-312.0 * RoomScale, r->y + 414*RoomScale, r->z + 656*RoomScale, r);
    sc->followPlayer = true;

    it = CreateItem("scp513", r->x - 32.0 * RoomScale, r->y + 196.0 * RoomScale, r->z + 688.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("drL3", r->x + 736.0 * RoomScale,1.0, r->z + 48.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("scp1048", r->x - 480.0 * RoomScale, 104.0*RoomScale, r->z - 176.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

}

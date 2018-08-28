#include <bbblitz3d.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "Room_lck_broke_2c.h"

namespace CBN {

// Functions.
void FillRoom_lck_broke_2c(Room* r) {
    Door* d = CreateDoor(r->x - 736.f * RoomScale, 0, r->z - 104.f * RoomScale, 0, r, true);
    d->timer = 70 * 5;
    d->autoClose = false;
    d->open = false;
    d->locked = true;

    bbEntityParent(d->buttons[0], 0);
    bbPositionEntity(d->buttons[0], r->x - 288.f * RoomScale, 0.7f, r->z - 640.f * RoomScale);
    bbEntityParent(d->buttons[0], r->obj);

    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;

    Door* d2 = CreateDoor(r->x + 104.f * RoomScale, 0, r->z + 736.f * RoomScale, 270, r, true);
    d2->timer = 70 * 5;
    d2->autoClose = false;
    d2->open = false;
    d2->locked = true;
    bbEntityParent(d2->buttons[0], 0);
    bbPositionEntity(d2->buttons[0], r->x + 640.f * RoomScale, 0.7f, r->z + 288.f * RoomScale);
    bbRotateEntity(d2->buttons[0], 0, 90, 0);
    bbEntityParent(d2->buttons[0], r->obj);

    bbFreeEntity(d2->buttons[1]);
    d2->buttons[1] = 0;

    d->linkedDoor = d2;
    d2->linkedDoor = d;

    float scale = RoomScale * 4.5f * 0.4f;

    r->objects[0] = bbCopyMeshModelEntity(Monitor);
    bbScaleEntity(r->objects[0],scale,scale,scale);
    bbPositionEntity(r->objects[0],r->x+668*RoomScale,1.1f,r->z-96.f*RoomScale,true);
    bbRotateEntity(r->objects[0],0,90,0);
    bbEntityParent(r->objects[0],r->obj);

    r->objects[1] = bbCopyMeshModelEntity(Monitor);
    bbScaleEntity(r->objects[1],scale,scale,scale);
    bbPositionEntity(r->objects[1],r->x+96.f*RoomScale,1.1f,r->z-668.f*RoomScale,true);
    bbEntityParent(r->objects[1],r->obj);
}

}

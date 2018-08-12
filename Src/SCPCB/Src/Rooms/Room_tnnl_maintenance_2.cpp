#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_tnnl_maintenance_2.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_maintenance_2(Room* r) {
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

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x + 2640.f * RoomScale, -2496.f * RoomScale, r->z + 400.f * RoomScale);
    bbEntityParent(r->objects[0], r->obj);

    r->objects[1] = bbCreatePivot();
    bbPositionEntity(r->objects[1], r->x - 4336.f * RoomScale, -2496.f * RoomScale, r->z - 2512.f * RoomScale);
    bbEntityParent(r->objects[1], r->obj);

    r->objects[2] = bbCreatePivot();
    bbRotateEntity(r->objects[2],0.f,180.f,0.f,true);
    bbPositionEntity(r->objects[2], r->x + 552.f * RoomScale, 240.f * RoomScale, r->z + 656.f * RoomScale);
    bbEntityParent(r->objects[2], r->obj);
    //
    r->objects[4] = bbCreatePivot();
    bbPositionEntity(r->objects[4], r->x - 552.f * RoomScale, 240.f * RoomScale, r->z - 656.f * RoomScale);
    bbEntityParent(r->objects[4], r->obj);
    //
    r->doors[0] = CreateDoor(r->x + 264.f * RoomScale, 0.f, r->z + 656.f * RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 224.f * RoomScale, 0.7, r->z + 480.f * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 304.f * RoomScale, 0.7, r->z + 832.f * RoomScale, true);

    r->doors[2] = CreateDoor(r->x - 264.f * RoomScale, 0.f, r->z - 656.f * RoomScale, 90, r, true);
    r->doors[2]->autoClose = false;
    r->doors[2]->open = true;
    bbPositionEntity(r->doors[2]->buttons[0], r->x - 224.f * RoomScale, 0.7, r->z - 480.f * RoomScale, true);
    bbPositionEntity(r->doors[2]->buttons[1], r->x - 304.f * RoomScale, 0.7, r->z - 832.f * RoomScale, true);
    //
    int temp = (((int)(AccessCode)*3) % 10000);
    if (temp < 1000) {
        temp = temp+1000;
    }
    d = CreateDoor(r->x,r->y,r->z,0, r, false, DOOR_TYPE_CONT, "", String(temp));
    bbPositionEntity(d->buttons[0], r->x + 224.f * RoomScale, r->y + 0.7, r->z - 384.f * RoomScale, true);
    bbRotateEntity(d->buttons[0], 0,-90,0,true);
    bbPositionEntity(d->buttons[1], r->x - 224.f * RoomScale, r->y + 0.7, r->z + 384.f * RoomScale, true);
    bbRotateEntity(d->buttons[1], 0,90,0,true);

    de = CreateDecal(DECAL_CORROSION, r->x + 64.f * RoomScale, 0.005, r->z + 144.f * RoomScale, 90, bbRand(360), 0);
    bbEntityParent(de->obj, r->obj);
    it = CreatePaper("drL6", r->x + 64.f * RoomScale, r->y +144.f * RoomScale, r->z - 384.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_tnnl_maintenance_2(Event* e) {
    //TODO: reimplement lol
}

}

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
    bbPositionEntity(r->objects[0], r->x + 2640.0 * RoomScale, -2496.0 * RoomScale, r->z + 400.0 * RoomScale);
    bbEntityParent(r->objects[0], r->obj);

    r->objects[1] = bbCreatePivot();
    bbPositionEntity(r->objects[1], r->x - 4336.0 * RoomScale, -2496.0 * RoomScale, r->z - 2512.0 * RoomScale);
    bbEntityParent(r->objects[1], r->obj);

    r->objects[2] = bbCreatePivot();
    bbRotateEntity(r->objects[2],0.0,180.0,0.0,true);
    bbPositionEntity(r->objects[2], r->x + 552.0 * RoomScale, 240.0 * RoomScale, r->z + 656.0 * RoomScale);
    bbEntityParent(r->objects[2], r->obj);
    //
    r->objects[4] = bbCreatePivot();
    bbPositionEntity(r->objects[4], r->x - 552.0 * RoomScale, 240.0 * RoomScale, r->z - 656.0 * RoomScale);
    bbEntityParent(r->objects[4], r->obj);
    //
    r->doors[0] = CreateDoor(r->x + 264.0 * RoomScale, 0.0, r->z + 656.0 * RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 224.0 * RoomScale, 0.7, r->z + 480.0 * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 304.0 * RoomScale, 0.7, r->z + 832.0 * RoomScale, true);

    r->doors[2] = CreateDoor(r->x - 264.0 * RoomScale, 0.0, r->z - 656.0 * RoomScale, 90, r, true);
    r->doors[2]->autoClose = false;
    r->doors[2]->open = true;
    bbPositionEntity(r->doors[2]->buttons[0], r->x - 224.0 * RoomScale, 0.7, r->z - 480.0 * RoomScale, true);
    bbPositionEntity(r->doors[2]->buttons[1], r->x - 304.0 * RoomScale, 0.7, r->z - 832.0 * RoomScale, true);
    //
    int temp = (((int)(AccessCode)*3) % 10000);
    if (temp < 1000) {
        temp = temp+1000;
    }
    d = CreateDoor(r->x,r->y,r->z,0, r, false, DOOR_TYPE_CONT, "", String(temp));
    bbPositionEntity(d->buttons[0], r->x + 224.0 * RoomScale, r->y + 0.7, r->z - 384.0 * RoomScale, true);
    bbRotateEntity(d->buttons[0], 0,-90,0,true);
    bbPositionEntity(d->buttons[1], r->x - 224.0 * RoomScale, r->y + 0.7, r->z + 384.0 * RoomScale, true);
    bbRotateEntity(d->buttons[1], 0,90,0,true);

    de = CreateDecal(DECAL_CORROSION, r->x + 64.0 * RoomScale, 0.005, r->z + 144.0 * RoomScale, 90, bbRand(360), 0);
    bbEntityParent(de->obj, r->obj);
    it = CreatePaper("drL6", r->x + 64.0 * RoomScale, r->y +144.0 * RoomScale, r->z - 384.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_tnnl_maintenance_2(Event* e) {
    //TODO: reimplement lol
}

}

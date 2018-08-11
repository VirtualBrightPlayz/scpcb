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
#include "Room_cont_714_860_1025_2.h"

namespace CBN {

// Functions.
void FillRoom_cont_714_860_1025_2(Room* r) {
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

    d = CreateDoor(r->x + 264.0 * RoomScale, 0, r->z, 90, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], r->x + 320.0 * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true), true);
    bbPositionEntity(d->buttons[1], r->x + 224.0 * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true), true);

    d = CreateDoor(r->x - 264.0 * RoomScale, 0, r->z+32*RoomScale, 270, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], r->x - 320.0 * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true), true);
    bbPositionEntity(d->buttons[1], r->x - 224.0 * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true), true);

    r->doors[1] = CreateDoor(r->x-560.0 * RoomScale, 0, r->z - 240.0 * RoomScale, 0, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[1]->autoClose = false;
    r->doors[1]->open = false;

    r->doors[2] = CreateDoor(r->x + 560.0 * RoomScale, 0, r->z - 272.0 * RoomScale, 180, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[2]->autoClose = false;
    r->doors[2]->open = false;

    r->doors[3] = CreateDoor(r->x + 560.0 * RoomScale, 0, r->z + 272.0 * RoomScale, 180, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[3]->autoClose = false;
    r->doors[3]->open = false;

    it = CreateItem("scp420j", r->x - 552.0 * RoomScale, r->y + 220.0 * RoomScale, r->z - 728.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("scp860", r->x + 568.0 * RoomScale, r->y + 178.0 * RoomScale, r->z + 760.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    sc = CreateSecurityCam(r->x + 560.0 * RoomScale, r->y + 386 * RoomScale, r->z - 416.0 * RoomScale, r);
    sc->angle = 180;
    sc->turn = 30;
    bbTurnEntity(sc->cameraObj, 30, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    sc = CreateSecurityCam(r->x - 560.0 * RoomScale, r->y + 386 * RoomScale, r->z - 416.0 * RoomScale, r);
    sc->angle = 180;
    sc->turn = 30;
    bbTurnEntity(sc->cameraObj, 30, 0, 0);
    bbEntityParent(sc->obj, r->obj);
}

}

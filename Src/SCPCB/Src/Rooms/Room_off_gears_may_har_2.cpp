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
#include "Room_off_gears_may_har_2.h"

namespace CBN {

// Functions.
void FillRoom_off_gears_may_har_2(Room* r) {
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

    d = CreateDoor(r->x + 240.0 * RoomScale, 0.0, r->z + 448.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, "", String(AccessCode));
    bbPositionEntity(d->buttons[0], r->x + 248.0 * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x + 232.0 * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x - 496.0 * RoomScale, 0.0, r->z, 90, r, false, DOOR_TYPE_DEF, "", "ABCD");
    bbPositionEntity(d->buttons[0], r->x - 488.0 * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x - 504.0 * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;
    d->locked = true;

    d = CreateDoor(r->x + 240.0 * RoomScale, 0.0, r->z - 576.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, "", "7816");
    bbPositionEntity(d->buttons[0], r->x + 248.0 * RoomScale, bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true),true);
    bbPositionEntity(d->buttons[1], r->x + 232.0 * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true),true);
    d->autoClose = false;
    d->open = false;

    it = CreatePaper("big_reveal", r->x + 736.0 * RoomScale, r->y + 224.0 * RoomScale, r->z + 544.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreateItem("vest", r->x + 608.0 * RoomScale, r->y + 112.0 * RoomScale, r->z + 32.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);

    it = CreatePaper("docIR106", r->x + 704.0 * RoomScale, r->y + 183.0 * RoomScale, r->z - 576.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreatePaper("journal", r->x + 912 * RoomScale, r->y + 176.0 * RoomScale, r->z - 160.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    it = CreateItem("firstaid", r->x + 912.0 * RoomScale, r->y + 112.0 * RoomScale, r->z - 336.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);
}

}

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
    bbPositionEntity(r->objects[0], r->x - 504.0 * RoomScale, -512.0 * RoomScale, r->z + 271.0 * RoomScale, true);
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 628.0 * RoomScale, -512.0 * RoomScale, r->z + 271.0 * RoomScale, true);
    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x - 532.0 * RoomScale, -512.0 * RoomScale, r->z - 877.0 * RoomScale, true);

    it = CreatePaper("doc970", r->x + 960.0 * RoomScale, r->y - 448.0 * RoomScale, r->z + 251.0 * RoomScale);
    bbRotateEntity(it->collider, 0, r->angle, 0);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("gasmask", r->x + 954.0 * RoomScale, r->y - 504.0 * RoomScale, r->z + 235.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

}

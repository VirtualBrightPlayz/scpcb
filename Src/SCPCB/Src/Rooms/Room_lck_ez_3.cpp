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
#include "Room_lck_ez_3.h"

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

    d = CreateDoor(r->x - 728.f * RoomScale, 0.f, r->z - 458.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    d = CreateDoor(r->x - 223.f * RoomScale, 0.f, r->z - 736.f * RoomScale, -90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    r->doors[0] = CreateDoor(r->x - 459.f * RoomScale, 0.f, r->z + 339.f * RoomScale, 90, r);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 580.822f * RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z - 606.679f * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 580.822f * RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z - 606.679f * RoomScale, true);
    r->doors[0]->locked = true;
    r->doors[0]->mtfClose = false;

    r->doors[1] = CreateDoor(r->x + 385.f * RoomScale, 0.f, r->z + 339.f * RoomScale, 270, r);
    bbPositionEntity(r->doors[1]->buttons[0], r->x + 580.822f * RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z - 606.679f * RoomScale, true);
    bbPositionEntity(r->doors[1]->buttons[1], r->x + 580.822f * RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z - 606.679f * RoomScale, true);
    r->doors[1]->locked = true;
    r->doors[1]->mtfClose = false;
    bbFreeEntity(r->doors[1]->obj2);
    r->doors[1]->obj2 = 0;

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0],r->x-48.f*RoomScale,128.f*RoomScale,r->z+320.f*RoomScale);
    bbEntityParent(r->objects[0],r->obj);

    for (int iterator188 = 0; iterator188 < Room::getListSize(); iterator188++) {
        r2 = Room::getObject(iterator188);

        if (r2!=r) {
            if (r2->roomTemplate->name.equals("room3gw")) {
                //don't load the mesh again
                r->objects[3] = bbCopyMeshModelEntity((MeshModel*)r2->objects[3],r->obj);
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

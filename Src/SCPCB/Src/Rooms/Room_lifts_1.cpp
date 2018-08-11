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
#include "Room_lifts_1.h"

namespace CBN {

// Functions.
void FillRoom_lifts_1(Room* r) {
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

    r->objects[0] = CreateButton(r->x + 96.0*RoomScale, r->y + 160.0 * RoomScale, r->z + 64.0 * RoomScale, 0,0,0);
    bbEntityParent(r->objects[0],r->obj);
    r->objects[1] = CreateButton(r->x - 96.0*RoomScale, r->y + 160.0 * RoomScale, r->z + 64.0 * RoomScale, 0,0,0);
    bbEntityParent(r->objects[1],r->obj);

    sc = CreateSecurityCam(r->x+384.0*RoomScale, r->y+(448-64)*RoomScale, r->z-960.0*RoomScale, r, true);
    sc->angle = 45;
    sc->turn = 45;
    sc->room = r;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    bbEntityParent(sc->obj, r->obj);
    sc->id = 3;
}

}

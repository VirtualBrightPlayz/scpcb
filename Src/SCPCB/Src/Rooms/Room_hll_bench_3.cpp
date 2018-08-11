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
#include "Room_hll_bench_3.h"

namespace CBN {

// Functions.
void FillRoom_hll_bench_3(Room* r) {
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

    sc = CreateSecurityCam(r->x-320.0*RoomScale, r->y+384.0*RoomScale, r->z+512.25*RoomScale, r);
    sc->angle = 225;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    //sc\followPlayer = True
    sc->id = 2;
}

}

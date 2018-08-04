#include "Room_hll_bench_3.h"
#include "include.h"

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

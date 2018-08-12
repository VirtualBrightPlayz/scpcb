#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_hll_bench_3.h"

namespace CBN {

// Functions.
void FillRoom_hll_bench_3(Room* r) {
    SecurityCam* sc = CreateSecurityCam(r->x-320.f*RoomScale, r->y+384.f*RoomScale, r->z+512.25f*RoomScale, r);
    sc->angle = 225;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    //sc\followPlayer = True
    sc->id = 2;
}

}

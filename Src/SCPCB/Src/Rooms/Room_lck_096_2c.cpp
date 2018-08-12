#include <bbblitz3d.h>
#include <bbmath.h>

#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_lck_096_2c.h"

namespace CBN {

// Functions.
void FillRoom_lck_096_2c(Room* r) {
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

    for (i = 0; i <= 5; i++) {
        de = CreateDecal(bbRand(DECAL_BLOOD_SPREAD, DECAL_BLOOD_SPLATTER), r->x+bbRnd(-392,520)*RoomScale, 3.f*RoomScale+bbRnd(0,0.001f), r->z+bbRnd(-392,520)*RoomScale,90,bbRnd(360),0);
        de->size = bbRnd(0.3f,0.6f);
        bbScaleSprite(de->obj, de->size,de->size);
        CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), r->x+bbRnd(-392,520)*RoomScale, 3.f*RoomScale+bbRnd(0,0.001f), r->z+bbRnd(-392,520)*RoomScale,90,bbRnd(360),0);
        de->size = bbRnd(0.1f,0.6f);
        bbScaleSprite(de->obj, de->size,de->size);
        CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), r->x+bbRnd(-0.5f,0.5f), 3.f*RoomScale+bbRnd(0,0.001f), r->z+bbRnd(-0.5f,0.5f),90,bbRnd(360),0);
        de->size = bbRnd(0.1f,0.6f);
        bbScaleSprite(de->obj, de->size,de->size);
    }

    sc = CreateSecurityCam(r->x + 512.f * RoomScale, r->y + 384 * RoomScale, r->z + 384.f * RoomScale, r, true);
    sc->angle = 45 + 90;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 668 * RoomScale, 1.1f, r->z - 96.f * RoomScale);
    bbTurnEntity(sc->scrObj, 0, 90, 0);
    bbEntityParent(sc->scrObj, r->obj);

    sc = CreateSecurityCam(r->x - 384.f * RoomScale, r->y + 384 * RoomScale, r->z - 512.f * RoomScale, r, true);
    sc->angle = 45 + 90 + 180;
    sc->turn = 45;

    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 96.f * RoomScale, 1.1f, r->z - 668.f * RoomScale);
    bbEntityParent(sc->scrObj, r->obj);
}

}

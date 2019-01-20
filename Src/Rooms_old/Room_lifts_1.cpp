#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Objects.h"
#include "Room_lifts_1.h"

namespace CBN {

// Functions.
void FillRoom_lifts_1(Room* r) {
    r->objects[0] = CreateButton(r->x + 96.f*RoomScale, r->y + 160.f * RoomScale, r->z + 64.f * RoomScale, 0,0,0);
    bbEntityParent(r->objects[0],r->obj);
    r->objects[1] = CreateButton(r->x - 96.f*RoomScale, r->y + 160.f * RoomScale, r->z + 64.f * RoomScale, 0,0,0);
    bbEntityParent(r->objects[1],r->obj);

    SecurityCam* sc = CreateSecurityCam(r->x+384.f*RoomScale, r->y+(448-64)*RoomScale, r->z-960.f*RoomScale, r, true);
    sc->angle = 45;
    sc->turn = 45;
    sc->room = r;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    bbEntityParent(sc->obj, r->obj);
    sc->id = 3;
}

}

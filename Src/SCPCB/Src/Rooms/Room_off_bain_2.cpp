#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_off_bain_2.h"

namespace CBN {

// Functions.
void FillRoom_off_bain_2(Room* r) {
    Door* d = CreateDoor(r->x - 240.f * RoomScale, 0.f, r->z, 90, r, false);
    d->open = false;
    d->autoClose = false;
}

}

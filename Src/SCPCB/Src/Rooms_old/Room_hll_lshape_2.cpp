#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "Room_hll_lshape_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_lshape_2(Room* r) {
    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6], r->x + 640.f * RoomScale, 8.f * RoomScale, r->z - 896.f * RoomScale);
    bbEntityParent(r->objects[6], r->obj);
}

}

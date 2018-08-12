#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "Room_hll_gas_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_gas_2(Room* r) {
    int i = 0;
    for (int xtemp = -1; xtemp <= 1; xtemp += 2) {
        for (int ztemp = -1; ztemp < 2; ztemp++) {
            Emitter* em = CreateEmitter(r->x + 202.f * RoomScale * xtemp, 8.f * RoomScale, r->z + 256.f * RoomScale * ztemp, 0);
            em->randAngle = 30;
            em->speed = 0.0045f;
            em->sizeChange = 0.007f;
            em->aChange = -0.016f;
            r->objects[i] = em->obj;
            if (i < 3) {
                bbTurnEntity(em->obj, 0, -90, 0, true);
            } else {
                bbTurnEntity(em->obj, 0, 90, 0, true);
            }
            bbTurnEntity(em->obj, -45, 0, 0, true);
            bbEntityParent(em->obj, r->obj);
            i++;
        }
    }

    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6], r->x + 640.f * RoomScale, 8.f * RoomScale, r->z - 896.f * RoomScale);
    bbEntityParent(r->objects[6], r->obj);

    r->objects[7] = bbCreatePivot();
    bbPositionEntity(r->objects[7], r->x - 864.f * RoomScale, -400.f * RoomScale, r->z - 632.f * RoomScale);
    bbEntityParent(r->objects[7],r->obj);
}

void UpdateEvent_hll_gas_2(Event* e) {
    if (Curr173->idle == 0) {
        if (e->room->dist < 8.f  && e->room->dist > 0) {
            if (!bbEntityVisible(Curr173->collider, mainPlayer->cam) && !bbEntityVisible(e->room->objects[6], mainPlayer->cam)) {
                bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[6], true), 0.5f, bbEntityZ(e->room->objects[6], true));
                bbResetEntity(Curr173->collider);
                RemoveEvent(e);
            }
        }
    }
}

}

#include <bbblitz3d.h>
#include <bbmath.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Items/Items.h"
#include "Room_lck_cam_2c.h"

namespace CBN {

// Functions.
void FillRoom_lck_cam_2c(Room* r) {
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

    d = CreateDoor(r->x - 736.f * RoomScale, 0, r->z - 104.f * RoomScale, 0, r, true);
    d->timer = 70 * 5;
    d->autoClose = false;
    d->open = false;

    bbEntityParent(d->buttons[0], 0);
    bbPositionEntity(d->buttons[0], r->x - 288.f * RoomScale, 0.7f, r->z - 640.f * RoomScale);
    bbEntityParent(d->buttons[0], r->obj);

    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;

    d2 = CreateDoor(r->x + 104.f * RoomScale, 0, r->z + 736.f * RoomScale, 270, r, true);
    d2->timer = 70 * 5;
    d2->autoClose = false;
    d2->open = false;
    bbEntityParent(d2->buttons[0], 0);
    bbPositionEntity(d2->buttons[0], r->x + 640.f * RoomScale, 0.7f, r->z + 288.f * RoomScale);
    bbRotateEntity(d2->buttons[0], 0, 90, 0);
    bbEntityParent(d2->buttons[0], r->obj);

    bbFreeEntity(d2->buttons[1]);
    d2->buttons[1] = 0;

    d->linkedDoor = d2;
    d2->linkedDoor = d;

    sc = CreateSecurityCam(r->x - 688.f * RoomScale, r->y + 384 * RoomScale, r->z + 688.f * RoomScale, r, true);
    sc->angle = 45 + 180;
    sc->turn = 45;
    sc->scrTexture = 0;
    bbEntityTexture(sc->scrObj, ScreenTexs[sc->scrTexture]);

    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 668 * RoomScale, 1.1f, r->z - 96.f * RoomScale);
    bbTurnEntity(sc->scrObj, 0, 90, 0);
    bbEntityParent(sc->scrObj, r->obj);

    sc = CreateSecurityCam(r->x - 112.f * RoomScale, r->y + 384 * RoomScale, r->z + 112.f * RoomScale, r, true);
    sc->angle = 45;
    sc->turn = 45;
    sc->scrTexture = 1;
    bbEntityTexture(sc->scrObj, ScreenTexs[sc->scrTexture]);

    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 96.f * RoomScale, 1.1f, r->z - 668.f * RoomScale);
    bbEntityParent(sc->scrObj, r->obj);

    Emitter* em = CreateEmitter(r->x - 175.f * RoomScale, 370.f * RoomScale, r->z + 656.f * RoomScale, 0);
    bbTurnEntity(em->obj, 90, 0, 0, true);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 20;
    em->speed = 0.05f;
    em->sizeChange = 0.007f;
    em->aChange = -0.006f;
    em->gravity = -0.24f;

    em = CreateEmitter(r->x - 655.f * RoomScale, 370.f * RoomScale, r->z + 240.f * RoomScale, 0);
    bbTurnEntity(em->obj, 90, 0, 0, true);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 20;
    em->speed = 0.05f;
    em->sizeChange = 0.007f;
    em->aChange = -0.006f;
    em->gravity = -0.24f;
}

void UpdateEventLockroom173(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (e->room->dist < 6.f  && e->room->dist > 0) {
        if (Curr173->idle == 2) {
            RemoveEvent(e);
        } else {
            if ((!bbEntityInView(Curr173->collider, mainPlayer->cam)) || bbEntityDistanceSquared(Curr173->collider, mainPlayer->collider)>15.f*15.f) {
                bbPositionEntity(Curr173->collider, e->room->x + bbCos(225-90 + e->room->angle) * 2, 0.6f, e->room->z + bbSin(225-90 + e->room->angle) * 2);
                bbResetEntity(Curr173->collider);
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

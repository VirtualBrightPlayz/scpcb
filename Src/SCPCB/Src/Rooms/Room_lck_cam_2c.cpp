#include "Room_lck_cam_2c.h"
#include "include.h"

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

    d = CreateDoor(r->x - 736.0 * RoomScale, 0, r->z - 104.0 * RoomScale, 0, r, true);
    d->timer = 70 * 5;
    d->autoClose = false;
    d->open = false;

    bbEntityParent(d->buttons[0], 0);
    bbPositionEntity(d->buttons[0], r->x - 288.0 * RoomScale, 0.7, r->z - 640.0 * RoomScale);
    bbEntityParent(d->buttons[0], r->obj);

    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;

    d2 = CreateDoor(r->x + 104.0 * RoomScale, 0, r->z + 736.0 * RoomScale, 270, r, true);
    d2->timer = 70 * 5;
    d2->autoClose = false: d2->open == false;
    bbEntityParent(d2->buttons[0], 0);
    bbPositionEntity(d2->buttons[0], r->x + 640.0 * RoomScale, 0.7, r->z + 288.0 * RoomScale);
    bbRotateEntity(d2->buttons[0], 0, 90, 0);
    bbEntityParent(d2->buttons[0], r->obj);

    bbFreeEntity(d2->buttons[1]);
    d2->buttons[1] = 0;

    d->linkedDoor = d2;
    d2->linkedDoor = d;

    sc = CreateSecurityCam(r->x - 688.0 * RoomScale, r->y + 384 * RoomScale, r->z + 688.0 * RoomScale, r, true);
    sc->angle = 45 + 180;
    sc->turn = 45;
    sc->scrTexture = 1;
    bbEntityTexture(sc->scrObj, ScreenTexs[sc->scrTexture]);

    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 668 * RoomScale, 1.1, r->z - 96.0 * RoomScale);
    bbTurnEntity(sc->scrObj, 0, 90, 0);
    bbEntityParent(sc->scrObj, r->obj);

    sc = CreateSecurityCam(r->x - 112.0 * RoomScale, r->y + 384 * RoomScale, r->z + 112.0 * RoomScale, r, true);
    sc->angle = 45;
    sc->turn = 45;
    sc->scrTexture = 1;
    bbEntityTexture(sc->scrObj, ScreenTexs[sc->scrTexture]);

    bbTurnEntity(sc->cameraObj, 40, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    bbPositionEntity(sc->scrObj, r->x + 96.0 * RoomScale, 1.1, r->z - 668.0 * RoomScale);
    bbEntityParent(sc->scrObj, r->obj);

    Emitter* em = CreateEmitter(r->x - 175.0 * RoomScale, 370.0 * RoomScale, r->z + 656.0 * RoomScale, 0);
    bbTurnEntity(em->obj, 90, 0, 0, true);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 20;
    em->speed = 0.05;
    em->sizeChange = 0.007;
    em->aChange = -0.006;
    em->gravity = -0.24;

    em = CreateEmitter(r->x - 655.0 * RoomScale, 370.0 * RoomScale, r->z + 240.0 * RoomScale, 0);
    bbTurnEntity(em->obj, 90, 0, 0, true);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 20;
    em->speed = 0.05;
    em->sizeChange = 0.007;
    em->aChange = -0.006;
    em->gravity = -0.24;
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
    if (e->room->dist < 6.0  & e->room->dist > 0) {
        if (Curr173->idle == 2) {
            RemoveEvent(e);
        } else {
            if ((!bbEntityInView(Curr173->collider, mainPlayer->cam)) | bbEntityDistance(Curr173->collider, mainPlayer->collider)>15.0) {
                bbPositionEntity(Curr173->collider, e->room->x + bbCos(225-90 + e->room->angle) * 2, 0.6, e->room->z + bbSin(225-90 + e->room->angle) * 2);
                bbResetEntity(Curr173->collider);
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

#include "Room_tnnl_nuke_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_nuke_2(Room* r) {
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

    //"tuulikaapin" ovi
    d = CreateDoor(r->x + 576.0 * RoomScale, 0.0, r->z - 152.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], r->x + 608.0 * RoomScale, bbEntityY(d->buttons[0],true), r->z - 284.0 * RoomScale,true);
    bbPositionEntity(d->buttons[1], r->x + 544.0 * RoomScale, bbEntityY(d->buttons[1],true), r->z - 284.0 * RoomScale,true);

    d = CreateDoor(r->x - 544.0 * RoomScale, 1504.0*RoomScale, r->z + 738.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], bbEntityX(d->buttons[0],true), bbEntityY(d->buttons[0],true), r->z + 608.0 * RoomScale,true);
    bbPositionEntity(d->buttons[1], bbEntityX(d->buttons[1],true), bbEntityY(d->buttons[1],true), r->z + 608.0 * RoomScale,true);

    //yl�kerran hissin ovi
    r->doors[0] = CreateDoor(r->x + 1192.0 * RoomScale, 0.0, r->z, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    //yl�kerran hissi
    r->objects[4] = bbCreatePivot();
    bbPositionEntity(r->objects[4], r->x + 1496.0 * RoomScale, 240.0 * RoomScale, r->z);
    bbEntityParent(r->objects[4], r->obj);
    //alakerran hissin ovi
    r->doors[1] = CreateDoor(r->x + 680.0 * RoomScale, 1504.0 * RoomScale, r->z, 90, r);
    r->doors[1]->autoClose = false;
    r->doors[1]->open = false;
    //alakerran hissi
    r->objects[5] = bbCreatePivot();
    bbPositionEntity(r->objects[5], r->x + 984.0 * RoomScale, 1744.0 * RoomScale, r->z);
    bbEntityParent(r->objects[5], r->obj);

    int n;
    for (n = 0; n <= 1; n++) {
        r->levers[n] = CreateLever();

        bbScaleEntity(r->levers[n]->obj, 0.04, 0.04, 0.04);
        bbScaleEntity(r->levers[n]->baseObj, 0.04, 0.04, 0.04);
        bbPositionEntity(r->levers[n]->obj, r->x - 975.0 * RoomScale, r->y + 1712.0 * RoomScale, r->z - (502.0-132.0*n) * RoomScale, true);
        bbPositionEntity(r->levers[n]->baseObj, r->x - 975.0 * RoomScale, r->y + 1712.0 * RoomScale, r->z - (502.0-132.0*n) * RoomScale, true);

        bbEntityParent(r->levers[n]->obj, r->obj);
        bbEntityParent(r->levers[n]->baseObj, r->obj);

        bbRotateEntity(r->levers[n]->baseObj, 0, -90-180, 0);
        bbRotateEntity(r->levers[n]->obj, 10, -90 - 180-180, 0);

        //EntityPickMode(r\levers[n]\obj, 2)
        bbEntityPickMode(r->levers[n]->obj, 1, false);
        bbEntityRadius(r->levers[n]->obj, 0.1);
        //makecollbox(r\levers[n]\obj)
    }

    it = CreatePaper("docWar", r->x - 768.0 * RoomScale, r->y + 1684.0 * RoomScale, r->z - 768.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("vest", r->x - 944.0 * RoomScale, r->y + 1652.0 * RoomScale, r->z - 656.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, -90, 0);

    it = CreatePaper("drL5", r->x + 800.0 * RoomScale, 88.0 * RoomScale, r->z + 256.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    sc = CreateSecurityCam(r->x+624.0*RoomScale, r->y+1888.0*RoomScale, r->z-312.0*RoomScale, r);
    sc->angle = 90;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    sc->id = 6;
}

void UpdateEvent_tnnl_nuke_2(Event* e) {
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
    if (mainPlayer->currRoom == e->room) {
        e->eventState2 = UpdateElevators(e->eventState2, e->room->doors[0], e->room->doors[1], e->room->objects[4], e->room->objects[5], e);

        e->eventState = e->room->levers[0]->succ;
    }
    //[End Block]
}

}

#include "Room_off_plain_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_off_plain_2(Room* r) {
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

    it = CreatePaper("doc106", r->x + 404.0 * RoomScale, r->y + 145.0 * RoomScale, r->z + 559.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("navigator", r->x + 305.0 * RoomScale, r->y + 153.0 * RoomScale, r->z + 944.0 * RoomScale);
    it->state = 20;
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("914_organic", r->x -137.0 * RoomScale, r->y + 153.0 * RoomScale, r->z + 464.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    //TODO: fix?
    //w.waypoints = CreateWaypoint(r\x - 32.0 * RoomScale, r\y + 66.0 * RoomScale, r\z + 288.0 * RoomScale, Null, r)
    //w2.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z - 448.0 * RoomScale, Null, r)
    //w\connected[0] = w2
    w->dist[0] = bbEntityDistance(w->obj, w2->obj);
    //w2\connected[0] = w
    w2->dist[0] = w->dist[0];
}

}

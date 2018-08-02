#include "Room_hll_dirty_3.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_hll_dirty_3(Room* r) {
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

    for (int iterator183 = 0; iterator183 < Room::getListSize(); iterator183++) {
        r2 = Room::getObject(iterator183);

        if (r2->roomTemplate->name == r->roomTemplate->name & r2 != r) {
            r->objects[0] = bbCopyEntity(r2->objects[0],r->obj);
            break;
        }
    }
    //If (r\objects[0]=0) Then r\objects[0] = LoadMesh("GFX/Map/room3z2_hb.b3d",r\obj)
    //EntityPickMode(r\objects[0],2)
    //EntityType(r\objects[0],HIT_MAP)
    //EntityAlpha(r\objects[0],0.0)
}

}

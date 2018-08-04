#include "Room_hll_plain_2.h"
#include "include.h"

namespace CBN {

// Functions.
void UpdateEventRoom2trick(Event* e) {
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
        if (bbEntityDistance(e->room->obj,mainPlayer->collider)<2.0) {
            if (bbEntityDistance(mainPlayer->collider, Curr173->obj)<6.0 | bbEntityDistance(mainPlayer->collider, Curr106->obj)<6.0) {
                RemoveEvent(e);
            } else {
                DebugLog("%@@ = / {2E6C2==FD gi`h]c");

                pvt = bbCreatePivot();
                bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider));
                bbPointEntity(pvt, e->room->obj);
                bbRotateEntity(pvt, 0, bbEntityYaw(pvt),0,true);
                bbMoveEntity(pvt, 0,0,bbEntityDistance(pvt,e->room->obj)*2);

                //mainPlayer\camShake = 5.0
                //mainPlayer\blurTimer = 100

                mainPlayer->blinkTimer = -10;

                bbPositionEntity(mainPlayer->collider, bbEntityX(pvt),bbEntityY(pvt)+0.05,bbEntityZ(pvt));
                bbUpdateWorld();
                //ResetEntity(Collider)

                bbTurnEntity(mainPlayer->collider, 0,180,0);

                bbFreeEntity(pvt);
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "Room_hll_plain_2.h"

namespace CBN {

// Functions.
void UpdateEventRoom2trick(Event* e) {
    float dist;
    int i;
    int temp;
    Pivot* pvt;
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


    if (mainPlayer->currRoom == e->room) {
        if (bbEntityDistance(e->room->obj,mainPlayer->collider)<2.f) {
            if (bbEntityDistance(mainPlayer->collider, Curr173->obj)<6.f || bbEntityDistance(mainPlayer->collider, Curr106->obj)<6.f) {
                RemoveEvent(e);
            } else {
                pvt = bbCreatePivot();
                bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider));
                bbPointEntity(pvt, e->room->obj);
                bbRotateEntity(pvt, 0, bbEntityYaw(pvt),0,true);
                bbMoveEntity(pvt, 0,0,bbEntityDistance(pvt,e->room->obj)*2);

                //mainPlayer\camShake = 5.f
                //mainPlayer\blurTimer = 100

                mainPlayer->blinkTimer = -10;

                bbPositionEntity(mainPlayer->collider, bbEntityX(pvt),bbEntityY(pvt)+0.05f,bbEntityZ(pvt));
                bbUpdateWorld();
                //ResetEntity(Collider)

                bbTurnEntity(mainPlayer->collider, 0,180,0);

                bbFreeEntity(pvt);
                RemoveEvent(e);
            }
        }
    }

}

}

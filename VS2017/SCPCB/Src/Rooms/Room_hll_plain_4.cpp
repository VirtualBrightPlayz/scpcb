#include "Room_hll_plain_4.h"
#include "include.h"

namespace CBN {

// Functions.
void UpdateEvent_hll_plain_4(Event* e) {
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
    if (e->eventState < TimeInPosMilliSecs()) {
        if (mainPlayer->currRoom != e->room) {
            if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->obj),bbEntityZ(e->room->obj))<16.0) {
                for (int iterator185 = 0; iterator185 < NPC::getListSize(); iterator185++) {
                    n = NPC::getObject(iterator185);

                    if (n->npcType == NPCtype049) {
                        if (n->state == 2 & bbEntityDistance(mainPlayer->collider,n->collider)>16.0) {
                            bbTFormVector(368, 528, 176, e->room->obj, 0);
                            bbPositionEntity(n->collider, bbEntityX(e->room->obj)+bbTFormedX(), bbTFormedY(), bbEntityZ(e->room->obj)+bbTFormedZ());
                            bbDebugLog(Str(bbTFormedX())+", "+Str(bbTFormedY())+", "+Str(bbTFormedZ()));
                            bbResetEntity(n->collider);
                            n->pathStatus = 0;
                            n->state = 4;
                            n->state2 = 0;
                            RemoveEvent(e);
                        }
                        Exit;
                    }
                }
            }
        }
        if (e!=nullptr) {
            e->eventState = TimeInPosMilliSecs()+5000;
        }
    }
    //[End Block]
}

}

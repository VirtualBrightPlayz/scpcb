#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../MathUtils/MathUtils.h"
#include "Room_hll_plain_4.h"

namespace CBN {

// Functions.
void UpdateEvent_hll_plain_4(Event* e) {
    if (e->eventState < TimeInPosMilliSecs()) {
        if (mainPlayer->currRoom != e->room) {
            if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->obj),bbEntityZ(e->room->obj))<16.f) {
                for (int iterator185 = 0; iterator185 < NPC::getListSize(); iterator185++) {
                    NPC* n = NPC::getObject(iterator185);

                    if (n->npcType == NPCtype049) {
                        if (n->state == 2 && bbEntityDistance(mainPlayer->collider,n->collider)>16.f) {
                            bbTFormVector(368, 528, 176, e->room->obj, 0);
                            bbPositionEntity(n->collider, bbEntityX(e->room->obj)+bbTFormedX(), bbTFormedY(), bbEntityZ(e->room->obj)+bbTFormedZ());
                            std::cout << String(bbTFormedX())+", "+String(bbTFormedY())+", "+String(bbTFormedZ());
                            bbResetEntity(n->collider);
                            n->pathStatus = 0;
                            n->state = 4;
                            n->state2 = 0;
                            RemoveEvent(e);
                        }
                        break;
                    }
                }
            }
        }
        if (e!=nullptr) {
            e->eventState = TimeInPosMilliSecs()+5000;
        }
    }

}

}

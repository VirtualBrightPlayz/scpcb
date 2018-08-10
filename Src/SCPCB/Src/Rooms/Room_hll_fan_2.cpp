#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_hll_fan_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_fan_2(Room* r) {
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
    for (int iterator184 = 0; iterator184 < Room::getListSize(); iterator184++) {
        r2 = Room::getObject(iterator184);

        if (r2!=r) {
            if (r2->roomTemplate->name.equals("room2_2")) {
                //don't load the mesh again
                r->objects[0] = bbCopyMeshModelEntity((MeshModel*)r2->objects[0]);
                break;
            }
        }
    }
    if (r->objects[0]==0) {
        r->objects[0] = bbLoadMesh("GFX/Map/fan.b3d");
    }
    bbScaleEntity(r->objects[0], RoomScale, RoomScale, RoomScale);
    bbPositionEntity(r->objects[0], r->x - 248 * RoomScale, 528 * RoomScale, r->z, 0);
    bbEntityParent(r->objects[0], r->obj);
}

void UpdateEventRoom2fan(Event* e) {
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
    //eventstate1 = timer for turning the fan on/off
    //eventstate2 = fan on/off
    //eventstate3 = the speed of the fan
    if (mainPlayer->currRoom == e->room) {
        bbTurnEntity(e->room->objects[0], e->eventState3*timing->tickDuration, 0, 0);
        if (e->eventState3 > 0.01) {
            e->room->soundCHN = LoopRangedSound (RoomAmbience[9], e->room->soundCHN, mainPlayer->cam, e->room->objects[0], 5.0, (e->eventState3/4.0));
        }
        e->eventState3 = CurveValue(e->eventState2*5, e->eventState3, 150.0);
    }

    if (e->room->dist < 16.0) {
        if (e->eventState < 0) {
            e->eventState = bbRand(15,30)*70;
            temp = (int)(e->eventState2);
            e->eventState2 = bbRand(0,1);
            if (mainPlayer->currRoom!=e->room) {
                e->eventState3 = e->eventState2*5;
            } else {
                //turn on the fan
                if (temp == 0 & e->eventState2 == 1.0) {
                    PlayRangedSound (LoadTempSound("SFX/ambient/Room ambience/FanOn.ogg"), mainPlayer->cam, e->room->objects[0], 8.0);
                    //turn off the fan
                } else if ((temp == 1 & e->eventState2 == 0.0)) {
                    PlayRangedSound (LoadTempSound("SFX/ambient/Room ambience/FanOff.ogg"), mainPlayer->cam, e->room->objects[0], 8.0);
                }
            }
        } else {
            e->eventState = e->eventState-timing->tickDuration;
        }
    }
    //[End Block]
}

}

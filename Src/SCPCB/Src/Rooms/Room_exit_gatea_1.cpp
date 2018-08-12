#include <bbblitz3d.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Objects.h"
#include "Room_exit_gatea_1.h"

namespace CBN {

// Functions.
void FillRoom_exit_gatea_1(Room* r) {
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

    //alempi hissi
    r->doors[0] = CreateDoor(r->x+744.f*RoomScale, 0, r->z+512.f*RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[1],r->x+688*RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z+368*RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[0],r->x+784*RoomScale, bbEntityY(r->doors[0]->buttons[0],true), r->z+656*RoomScale, true);
    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x+1048.f*RoomScale, 0, r->z+512.f*RoomScale, true);
    bbEntityParent(r->objects[0], r->obj);

    r->doors[1] = CreateDoor(r->x, 0, r->z - 360.f * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(r->doors[1]->buttons[1], r->x+416*RoomScale, bbEntityY(r->doors[0]->buttons[1],true), r->z-576*RoomScale, true);
    bbRotateEntity(r->doors[1]->buttons[1],0,r->angle-90,0,true);
    bbPositionEntity(r->doors[1]->buttons[0], r->x, 20.f, r->z, true);
}

void UpdateEvent_exit_gatea_1(Event* e) {
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
    Room* gatea;

    //[Block]
    if (mainPlayer->currRoom == e->room) {
        if (RemoteDoorOn==false) {
            e->room->doors[1]->locked = true;
        } else if ((RemoteDoorOn && e->eventState3==0)) {
            e->room->doors[1]->locked = false;
            if (e->room->doors[1]->open) {
                if (e->room->doors[1]->openstate > 50 || bbEntityDistance(mainPlayer->collider, e->room->doors[1]->frameobj)<0.5f) {
                    e->room->doors[1]->openstate = Min(e->room->doors[1]->openstate,50);
                    e->room->doors[1]->open = false;
                    PlayRangedSound (LoadTempSound("SFX/Door/DoorError.ogg"), mainPlayer->cam, e->room->doors[1]->frameobj);
                }
            }
        } else {
            e->room->doors[1]->locked = false;
            gatea = nullptr;
            for (int iterator178 = 0; iterator178 < Room::getListSize(); iterator178++) {
                r = Room::getObject(iterator178);

                if (r->roomTemplate->name.equals("gatea")) {
                    gatea = r;
                    break;
                }
            }

            e->eventState = UpdateElevators(e->eventState, e->room->doors[0], gatea->doors[1], e->room->objects[0], e->room->objects[1], e);
            if (Contained106 == false) {
                if (e->eventState < -1.5f && e->eventState+timing->tickDuration>= -1.5f) {
                    //PlaySound2(OldManSFX(3)) ;TODO: fix
                }
            }

            if (bbEntityDistance(mainPlayer->collider, e->room->objects[1])<4.f) {
                gatea->doors[1]->locked = true;
                mainPlayer->currRoom = gatea;
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

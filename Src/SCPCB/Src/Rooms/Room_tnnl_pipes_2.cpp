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
#include "Room_tnnl_pipes_2.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_pipes_2(Room* r) {
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

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 368.0 * RoomScale, 0.0, r->z, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x - 368.0 * RoomScale, 0.0, r->z, true);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 224.0 * RoomScale - 0.005, 192.0 * RoomScale, r->z, true);

    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x - 224.0 * RoomScale + 0.005, 192.0 * RoomScale, r->z, true);
}

void UpdateEventRoom2pipes106(Event* e) {
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
    Decal* d;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (!Contained106) {
        if (e->eventState == 0) {
            if (mainPlayer->currRoom == e->room) {
                e->eventState = 1;
            }
        } else {
            e->eventState = (e->eventState+timing->tickDuration*0.7);
            //0-50 = walk to the middle
            //50-200 = look around
            //200-250 = leave
            if (e->eventState < 50) {
                Curr106->idle = true;
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[0], true), bbEntityY(mainPlayer->collider) - 0.15, bbEntityZ(e->room->objects[0], true));
                bbPointEntity(Curr106->collider, e->room->objects[1]);
                bbMoveEntity(Curr106->collider, 0, 0, bbEntityDistance(e->room->objects[0], e->room->objects[1])*0.5 * (e->eventState / 50.0));
                AnimateNPC(Curr106, 284, 333, 0.02*35);
            } else if ((e->eventState < 200)) {
                Curr106->idle = true;
                AnimateNPC(Curr106, 334, 494, 0.2);

                bbPositionEntity(Curr106->collider, (bbEntityX(e->room->objects[0], true)+bbEntityX(e->room->objects[1], true))/2, bbEntityY(mainPlayer->collider) - 0.15, (bbEntityZ(e->room->objects[0], true)+bbEntityZ(e->room->objects[1], true))/2);
                //MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\objects[0], e\room\objects[1])*0.5)
                bbRotateEntity(Curr106->collider,0, CurveValue(e->eventState,bbEntityYaw(Curr106->collider),30.0),0,true);
                if (bbEntityDistance(Curr106->collider, mainPlayer->collider)<4.0) {
                    pvt = bbCreatePivot();
                    bbPositionEntity(pvt, bbEntityX(Curr106->collider),bbEntityY(Curr106->collider),bbEntityZ(Curr106->collider));
                    bbPointEntity(pvt, mainPlayer->collider);
                    if (WrapAngle(bbEntityYaw(pvt)-bbEntityYaw(Curr106->collider))<80) {
                        Curr106->state = -11;
                        Curr106->idle = false;
                        //PlaySound2(HorrorSFX(10)) ;TODO: fix
                        e->eventState = 260;
                    }
                    bbFreeEntity(pvt);
                }
            } else if ((e->eventState < 250)) {
                Curr106->idle = true;
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[0], true), bbEntityY(mainPlayer->collider) - 0.15, bbEntityZ(e->room->objects[0], true));
                bbPointEntity(Curr106->collider, e->room->objects[1]);
                //200-250     (- 150)      50-100
                bbMoveEntity(Curr106->collider, 0, 0, bbEntityDistance(e->room->objects[0], e->room->objects[1]) * ((e->eventState-150.0) / 100.0));
                AnimateNPC(Curr106, 284, 333, 0.02*35);
            }
            bbResetEntity(Curr106->collider);

            bbPositionEntity(Curr106->obj, bbEntityX(Curr106->collider), bbEntityY(Curr106->collider) - 0.15, bbEntityZ(Curr106->collider));
            bbRotateEntity(Curr106->obj, 0, bbEntityYaw(Curr106->collider), 0);

            //PositionEntity(Curr106\collider, EntityX(Curr106\collider), EntityY(mainPlayer\collider) - 0.20, EntityZ(Curr106\collider))

            if ((e->eventState / 250.0) > 0.3 & ((e->eventState - timing->tickDuration*0.7) / 250.0) <= 0.3) {
                //e\soundChannels[0] = PlaySound2(HorrorSFX(6))
                mainPlayer->blurTimer = 800;
                //90, Rnd(360), 0
                d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[2], true), bbEntityY(e->room->objects[2], true), bbEntityZ(e->room->objects[2], true), 0, e->room->angle - 90, bbRnd(360));
                d->timer = 90000;
                d->alpha = 0.01;
                d->alphaChange = 0.005;
                d->size = 0.1;
                d->sizeChange = 0.003;
            }

            if ((e->eventState / 250.0) > 0.65 & ((e->eventState - timing->tickDuration*0.7) / 250.0) <= 0.65) {
                d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[3], true), bbEntityY(e->room->objects[3], true), bbEntityZ(e->room->objects[3], true), 0, e->room->angle + 90, bbRnd(360));
                d->timer = 90000;
                d->alpha = 0.01;
                d->alphaChange = 0.005;
                d->size = 0.1;
                d->sizeChange = 0.003;
            }

            if (e->eventState > 250) {
                Curr106->idle = false;
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

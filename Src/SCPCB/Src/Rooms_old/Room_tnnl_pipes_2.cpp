#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../MathUtils/MathUtils.h"
#include "Room_tnnl_pipes_2.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_pipes_2(Room* r) {
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 368.f * RoomScale, 0.f, r->z, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x - 368.f * RoomScale, 0.f, r->z, true);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 224.f * RoomScale - 0.005f, 192.f * RoomScale, r->z, true);

    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x - 224.f * RoomScale + 0.005f, 192.f * RoomScale, r->z, true);
}

void UpdateEventRoom2pipes106(Event* e) {
    if (!Contained106) {
        if (e->eventState == 0) {
            if (mainPlayer->currRoom == e->room) {
                e->eventState = 1;
            }
        } else {
            e->eventState = (e->eventState+timing->tickDuration*0.7f);
            //0-50 = walk to the middle
            //50-200 = look around
            //200-250 = leave
            if (e->eventState < 50) {
                Curr106->idle = true;
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[0], true), bbEntityY(mainPlayer->collider) - 0.15f, bbEntityZ(e->room->objects[0], true));
                bbPointEntity(Curr106->collider, e->room->objects[1]);
                bbMoveEntity(Curr106->collider, 0, 0, bbEntityDistance(e->room->objects[0], e->room->objects[1])*0.5f * (e->eventState / 50.f));
                AnimateNPC(Curr106, 284, 333, 0.02f*35);
            } else if ((e->eventState < 200)) {
                Curr106->idle = true;
                AnimateNPC(Curr106, 334, 494, 0.2f);

                bbPositionEntity(Curr106->collider, (bbEntityX(e->room->objects[0], true)+bbEntityX(e->room->objects[1], true))/2, bbEntityY(mainPlayer->collider) - 0.15f, (bbEntityZ(e->room->objects[0], true)+bbEntityZ(e->room->objects[1], true))/2);
                //MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\objects[0], e\room\objects[1])*0.5f)
                bbRotateEntity(Curr106->collider,0, CurveValue(e->eventState,bbEntityYaw(Curr106->collider),30.f),0,true);
                if (bbEntityDistance(Curr106->collider, mainPlayer->collider)<4.f) {
                    Pivot* pvt = bbCreatePivot();
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
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[0], true), bbEntityY(mainPlayer->collider) - 0.15f, bbEntityZ(e->room->objects[0], true));
                bbPointEntity(Curr106->collider, e->room->objects[1]);
                //200-250     (- 150)      50-100
                bbMoveEntity(Curr106->collider, 0, 0, bbEntityDistance(e->room->objects[0], e->room->objects[1]) * ((e->eventState-150.f) / 100.f));
                AnimateNPC(Curr106, 284, 333, 0.02f*35);
            }
            bbResetEntity(Curr106->collider);

            bbPositionEntity(Curr106->obj, bbEntityX(Curr106->collider), bbEntityY(Curr106->collider) - 0.15f, bbEntityZ(Curr106->collider));
            bbRotateEntity(Curr106->obj, 0, bbEntityYaw(Curr106->collider), 0);

            //PositionEntity(Curr106\collider, EntityX(Curr106\collider), EntityY(mainPlayer\collider) - 0.20f, EntityZ(Curr106\collider))

            if ((e->eventState / 250.f) > 0.3f && ((e->eventState - timing->tickDuration*0.7f) / 250.f) <= 0.3f) {
                //e\soundChannels[0] = PlaySound2(HorrorSFX(6))
                mainPlayer->blurTimer = 800;
                //90, Rnd(360), 0
                Decal* d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[2], true), bbEntityY(e->room->objects[2], true), bbEntityZ(e->room->objects[2], true), 0, e->room->angle - 90, bbRnd(360));
                d->timer = 90000;
                d->alpha = 0.01f;
                d->alphaChange = 0.005f;
                d->size = 0.1f;
                d->sizeChange = 0.003f;
            }

            if ((e->eventState / 250.f) > 0.65f && ((e->eventState - timing->tickDuration*0.7f) / 250.f) <= 0.65f) {
                Decal* d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[3], true), bbEntityY(e->room->objects[3], true), bbEntityZ(e->room->objects[3], true), 0, e->room->angle + 90, bbRnd(360));
                d->timer = 90000;
                d->alpha = 0.01f;
                d->alphaChange = 0.005f;
                d->size = 0.1f;
                d->sizeChange = 0.003f;
            }

            if (e->eventState > 250) {
                Curr106->idle = false;
                RemoveEvent(e);
            }
        }
    }

}

}

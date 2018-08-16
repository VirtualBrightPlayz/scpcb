#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "Room_tnnl_elec_2.h"

namespace CBN {

// Functions.
void FillRoom_tnnl_elec_2(Room* r) {
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x, 544.f * RoomScale, r->z + 512.f * RoomScale, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x, 544.f * RoomScale, r->z - 512.f * RoomScale, true);
}

void UpdateEventTunnel2smoke(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (e->room->dist < 3.5f) {
            PlayRangedSound(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"), mainPlayer->cam, e->room->obj);
            for (int i = 0; i < 2; i++) {
                Emitter* em = CreateEmitter(bbEntityX(e->room->objects[i],true),bbEntityY(e->room->objects[i],true), bbEntityZ(e->room->objects[i],true),0);
                bbTurnEntity(em->obj, 90, 0, 0, true);
                bbEntityParent(em->obj, e->room->obj);
                em->size = 0.05f;
                em->randAngle = 10;
                em->speed = 0.06f;
                em->sizeChange = 0.007f;
                //EntityParent(em\obj, e\room\obj)

                for (int z = 0; z < 11; z++) {
                    Particle* p = CreateParticle(bbEntityX(em->obj, true), 448*RoomScale, bbEntityZ(em->obj, true), PARTICLE_SMOKE_WHITE, em->size, em->gravity, em->lifeTime);
                    p->speed = em->speed;
                    bbRotateEntity(p->pvt, bbRnd(360), bbRnd(360), 0, true);
                    p->size = 0.05f;
                    p->sizeChange = 0.008f;
                }

            }
            RemoveEvent(e);
        }
    }


}

void UpdateEvent_tnnl_elec_2(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (Curr173->idle == 2) {
            RemoveEvent(e);
        } else {
            if (e->eventState == 0) {
                if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), bbEntityX(e->room->obj), bbEntityZ(e->room->obj)) < 3.5f) {
                    PlaySound_SM(sndMgmt->lightSwitch);

                    //LightBlink = Rnd(0.f,1.f)*(e\eventState/200)
                    e->eventState = 1;
                }
            } else if ((e->eventState < 200)) {

                mainPlayer->blinkTimer = -10;
                if (e->eventState > 30) {
                    //LightBlink = 1.f
                    if (e->eventState-timing->tickDuration <= 30) {
                        PlaySound2(LoadTempSound("SFX/ambient/general/ambient3.ogg"));
                    }
                }
                if (e->eventState-timing->tickDuration <= 100 && e->eventState > 100) {
                    PlaySound2(LoadTempSound("SFX/ambient/general/ambient6.ogg"));
                    bbPositionEntity(Curr173->collider, bbEntityX(e->room->obj), 0.6f, bbEntityZ(e->room->obj));
                    bbResetEntity(Curr173->collider);
                    Curr173->idle = true;
                }
                //LightBlink = 1.f
                e->eventState = e->eventState + timing->tickDuration;
            } else {
                mainPlayer->blinkTimer = mainPlayer->blinkFreq;

                Curr173->idle = false;
                RemoveEvent(e);
            }
        }
    }

}

}

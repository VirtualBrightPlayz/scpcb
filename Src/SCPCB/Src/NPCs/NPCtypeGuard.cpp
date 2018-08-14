#include <bbblitz3d.h>
#include <bbmath.h>

#include "NPCs.h"
#include "../Config/INI.h"
#include "../GameMain.h"
#include "../AssetMgmt/Audio.h"
#include "../Player/Player.h"
#include "../MathUtils/MathUtils.h"
#include "../Map/Particles.h"
#include "NPCtypeGuard.h"

namespace CBN {

// Constants.
const int STATEGUARD_IDLE = 0;
const int STATEGUARD_LOOK = 1;
const int STATEGUARD_MOVE_TO_TARGET = 2;
const int STATEGUARD_SHOOT_TARGET = 3;
const int STATEGUARD_DEAD = 4;

// Functions.
void InitializeNPCtypeGuard(NPC* n) {
    n->nvName = "Humen";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.2f);
    bbEntityType(n->collider, HIT_PLAYER);

    LoadOrCopyMesh(n, "GFX/NPCs/guard/guard.b3d");

    n->speed = (GetINIFloat("Data/NPCs.ini", "Guard", "speed") / 100.f);

    float temp = (GetINIFloat("Data/NPCs.ini", "Guard", "scale") / 2.5f);
    bbScaleEntity(n->obj, temp, temp, temp);

    bbMeshCullBox(n->obj, -bbMeshWidth(n->obj), -bbMeshHeight(n->obj), -bbMeshDepth(n->obj), bbMeshWidth(n->obj)*2, bbMeshHeight(n->obj)*2, bbMeshDepth(n->obj)*2);
}

void UpdateNPCtypeGuard(NPC* n) {
    float dist;

    Object* head;
    float headangle;
    Pivot* pvt;
    Particle* p;

    float prevFrame = n->frame;

    switch ((int)n->state) {
        case STATEGUARD_LOOK: {
            head = bbFindChild(n->obj,"head");
            headangle = bbEntityYaw(head);

            if (n->target != nullptr) {
                n->targetX = bbEntityX(n->target->collider);
                n->targetY = bbEntityY(n->target->collider);
                n->targetZ = bbEntityZ(n->target->collider);
            }

            if (headangle > -45 && headangle < 45) {
                bbPointEntity(head,n->target->collider);
            }

            AnimateNPC(n,77,201,0.2f);
        }
        case STATEGUARD_MOVE_TO_TARGET: {
            bbRotateEntity(n->collider, 0, CurveAngle(bbVectorYaw(n->targetX-bbEntityX(n->collider), 0, n->targetZ-bbEntityZ(n->collider))+n->angle, bbEntityYaw(n->collider), 20.f), 0);

            dist = Distance(bbEntityX(n->collider), bbEntityZ(n->collider), n->targetX, n->targetZ);

            AnimateNPC(n, 1614, 1641, n->currSpeed * 30);

            if (dist > 2.f || dist < 1.f ) {
                n->currSpeed = CurveValue(n->speed * Sgn(dist - 1.5f) * 0.75f, n->currSpeed, 10.f);
            } else {
                n->currSpeed = CurveValue(0, n->currSpeed, 10.f);
            }

            if (n->currSpeed > 0.01f) {
                if (prevFrame > 1638 && n->frame < 1620) {
                    PlayRangedSound(sndManager->footstepMetal[bbRand(0,7)]->internal, mainPlayer->cam, n->collider, 8.f, bbRnd(0.5f, 0.7f));
                } else if ((prevFrame < 1627 && n->frame>=1627)) {
                    PlayRangedSound(sndManager->footstepMetal[bbRand(0,7)]->internal, mainPlayer->cam, n->collider, 8.f, bbRnd(0.5f, 0.7f));
                }
            }

            bbMoveEntity(n->collider, 0, 0, n->currSpeed * timing->tickDuration);

        }
        case STATEGUARD_SHOOT_TARGET: {
            //Raising gun to aim animation.
            AnimateNPC(n, 1539, 1553, 0.2f, false);

            pvt = bbCreatePivot();
            bbPositionEntity(pvt, n->targetX, n->targetY, n->targetZ);

            //TODO: Make relative to target.
            bbPointEntity(pvt, mainPlayer->collider);

            bbRotateEntity(pvt, Min(bbEntityPitch(pvt), 20), bbEntityYaw(pvt), 0);

            bbRotateEntity(n->collider, CurveAngle(bbEntityPitch(pvt), bbEntityPitch(n->collider), 10), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(n->collider), 10), 0, true);

            //Start shooting once the aiming animation is done.
            if (n->timer < 0 && n->frame>1550) {
                PlayRangedSound_SM(sndManager->gunshot[0], mainPlayer->cam, n->collider, 35);

                bbRotateEntity(pvt, bbEntityPitch(n->collider), bbEntityYaw(n->collider), 0, true);
                bbPositionEntity(pvt, bbEntityX(n->obj), bbEntityY(n->obj), bbEntityZ(n->obj));
                bbMoveEntity(pvt,0.8f*0.079f, 10.75f*0.079f, 6.9f*0.079f);

                bbPointEntity(pvt, mainPlayer->collider);

                p = CreateParticle(bbEntityX(n->obj, true), bbEntityY(n->obj, true), bbEntityZ(n->obj, true), PARTICLE_FLASH, 0.2f, 0.f, 5);
                bbPositionEntity(p->pvt, bbEntityX(n->obj), bbEntityY(n->obj), bbEntityZ(n->obj));
                bbRotateEntity(p->pvt, bbEntityPitch(n->collider), bbEntityYaw(n->collider), 0, true);
                bbMoveEntity(p->pvt,0.8f*0.079f, 10.75f*0.079f, 6.9f*0.079f);

                n->reload = 7;
            }

            bbFreeEntity(pvt);

            n->timer = n->timer - timing->tickDuration;
        }
        case STATEGUARD_DEAD: {
            if (n->frame <= 151) {
                SetNPCFrame(n,151);
            } else {
                AnimateNPC(n,113,151,0.2f);
            }
        }
    }
}

}

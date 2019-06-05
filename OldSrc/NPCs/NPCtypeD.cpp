#include <bbblitz3d.h>

#include "NPCs.h"
#include "../GameMain.h"
#include "../MathUtils/MathUtils.h"
#include "NPCtypeD.h"

namespace CBN {

// Functions.
void InitializeNPCtypeD(NPC* n) {
    n->nvName = "Human";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.32f);
    bbEntityType(n->collider, HIT_PLAYER);

    NPC* n2;
    for (int iterator137 = 0; iterator137 < NPC::getListSize(); iterator137++) {
        n2 = NPC::getObject(iterator137);

        if (n2->npcType == n->npcType && n2->obj != 0) {
            n->obj = bbCopyMeshModelEntity(n2->obj);
            break;
        }
    }

    if (n->obj == 0) {
        n->obj = bbLoadAnimMesh("GFX/NPCs/classd/classd.b3d");
    }

    float temp = 0.5f / bbMeshWidth(n->obj);
    bbScaleEntity(n->obj, temp, temp, temp);

    n->speed = 2.f / 100;

    bbMeshCullBox(n->obj, -bbMeshWidth(n->obj), -bbMeshHeight(n->obj), -bbMeshDepth(n->obj), bbMeshWidth(n->obj)*2, bbMeshHeight(n->obj)*2, bbMeshDepth(n->obj)*2);
}

void UpdateNPCtypeD(NPC* n) {
    bbRotateEntity(n->collider, 0, bbEntityYaw(n->collider), bbEntityRoll(n->collider), true);

    float prevFrame = bbAnimTime(n->obj);

    switch ((int)n->state) {
        case 0: {
            n->currSpeed = CurveValue(0.f, n->currSpeed, 5.f);
            Animate2(n->obj, bbAnimTime(n->obj), 210, 235, 0.1f);
            //walking
        }
        case 1: {
            if (n->state2 == 1.f) {
                n->currSpeed = CurveValue(n->speed*0.7f, n->currSpeed, 20.f);
            } else {
                n->currSpeed = CurveValue(0.015f, n->currSpeed, 5.f);
            }
            Animate2(n->obj, bbAnimTime(n->obj), 236, 260, n->currSpeed * 18);
            //running
        }
        case 2: {
            n->currSpeed = CurveValue(0.03f, n->currSpeed, 5.f);
            Animate2(n->obj, bbAnimTime(n->obj), 301, 319, n->currSpeed * 18);
        }
    }

    bbMoveEntity(n->collider, 0, 0, n->currSpeed * timing->tickDuration);

    bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider) - 0.32f, bbEntityZ(n->collider));

    bbRotateEntity(n->obj, bbEntityPitch(n->collider), bbEntityYaw(n->collider)-180.f, 0);
}

}

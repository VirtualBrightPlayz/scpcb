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
#include "Room_test_682_2.h"

namespace CBN {

// Functions.
void FillRoom_test_682_2(Room* r) {
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

    for (xtemp = 0; xtemp <= 1; xtemp++) {
        for (ztemp = -1; ztemp <= 1; ztemp++) {
            r->objects[xtemp * 3 + (ztemp + 1)] = bbCreatePivot();
            bbPositionEntity(r->objects[xtemp * 3 + (ztemp + 1)], r->x + (-236.f + 280.f * xtemp) * RoomScale, -700.f * RoomScale, r->z + 384.f * ztemp * RoomScale);
            bbEntityParent(r->objects[xtemp * 3 + (ztemp + 1)], r->obj);
        }
    }

    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6], r->x + 754.f * RoomScale, r->y - 1248.f * RoomScale, r->z);
    bbEntityParent(r->objects[6], r->obj);

    sc = CreateSecurityCam(r->x + 744.f * RoomScale, r->y - 856.f * RoomScale, r->z + 236.f * RoomScale, r);
    sc->followPlayer = true;

    CreateDoor(r->x + 720.f * RoomScale, 0, r->z, 0, r, false, DOOR_TYPE_HCZ, r->roomTemplate->name);

    CreateDoor(r->x - 624.f * RoomScale, -1280.f * RoomScale, r->z, 90, r, true, DOOR_TYPE_HCZ);

    it = CreatePaper("doc682", r->x + 656.f * RoomScale, r->y - 1200.f * RoomScale, r->z - 16.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_test_682_2(Event* e) {
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
    if (e != nullptr) {
        if (mainPlayer->currRoom == e->room) {
            if (e->eventState == 0) {
                e->room->objects[7] = bbLoadAnimMesh("GFX/npcs/scp-1048.b3d");
                bbScaleEntity(e->room->objects[7], 0.05,0.05,0.05);

                bbTFormPoint(bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider),0,e->room->obj);
                if (bbTFormedZ()==0) {
                    temp = -1;
                } else {
                    temp = (int)(-Sgn(bbTFormedZ()));
                }
                bbTFormPoint(-720,0,816*temp,e->room->obj,0);
                bbPositionEntity(e->room->objects[7],bbTFormedX(),0,bbTFormedZ());

                bbRotateEntity(e->room->objects[7],-90,e->room->angle-90,0);
                bbSetAnimTime(e->room->objects[7],297);
                e->eventState = 1;
            }
            if (bbEntityDistance(mainPlayer->collider, e->room->objects[6]) < 2.5 & e->eventState > 0) {
                PlaySound2(LoadTempSound("SFX/SCP/079/TestroomWarning.ogg"));
                for (i = 0; i <= 5; i++) {
                    em = CreateEmitter(bbEntityX(e->room->objects[i], true), bbEntityY(e->room->objects[i], true), bbEntityZ(e->room->objects[i], true), 0);
                    bbTurnEntity(em->obj, 90, 0, 0, true);
                    //entityParent(em\obj, e\room\obj)
                    em->randAngle = 5;
                    em->speed = 0.042;
                    em->sizeChange = 0.0025;
                }
                //Delete e
                e->eventState = e->eventState * -1;
            }
            if (e->room->objects[7]!=0) {
                Animate2((MeshModel*)e->room->objects[7],bbAnimTime(e->room->objects[7]),284,308,0.3);
                bbMoveEntity(e->room->objects[7],0,-0.008*timing->tickDuration,0);
                bbTFormPoint(bbEntityX(e->room->objects[7]),bbEntityY(e->room->objects[7]),bbEntityZ(e->room->objects[7]),0,e->room->obj);

                if (abs(bbTFormedX())>725) {
                    bbFreeEntity(e->room->objects[7]);
                    e->room->objects[7] = 0;
                    e->eventState = e->eventState *2;
                }
            }
            if (e->eventState == -2) {
                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

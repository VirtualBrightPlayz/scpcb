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
#include "Room_end_1.h"

namespace CBN {

// Functions.
void FillRoom_end_1(Room* r) {
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

    r->doors[0] = CreateDoor(r->x, 0, r->z + 1136 * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = false;
    bbFreeEntity(r->doors[0]->buttons[0]);
    r->doors[0]->buttons[0] = 0;
    bbFreeEntity(r->doors[0]->buttons[1]);
    r->doors[0]->buttons[1] = 0;
}

void UpdateEventEndroom106(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;
    Texture* tex;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (Contained106) {
        if (e->eventState == 0) {
            if (e->room->dist < 8 && e->room->dist > 0) {
                if (Curr106->state < 0) {
                    RemoveEvent(e);
                } else {
                    e->room->doors[0]->open = true;

                    e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->doors[0]->obj,true), 0.5f, bbEntityZ(e->room->doors[0]->obj,true));

                    tex = bbLoadTexture("GFX/NPCs/classd/janitor.jpg");
                    e->room->npc[0]->texture = "GFX/NPCs/classd/janitor.jpg";
                    bbEntityTexture(e->room->npc[0]->obj, tex);
                    bbFreeTexture(tex);

                    bbPointEntity(e->room->npc[0]->collider, e->room->obj);
                    bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider),0, true);
                    bbMoveEntity(e->room->npc[0]->collider, 0,0,0.5f);

                    e->room->doors[0]->open = false;
                    PlayRangedSound(LoadTempSound("SFX/Door/EndroomDoor.ogg"), mainPlayer->cam, e->room->obj, 15);

                    e->eventState = 1;
                }
            }
        } else if ((e->eventState == 1)) {
            if (mainPlayer->currRoom == e->room) {
                //PlaySound2(CloseDoorSFX(1,0))
                //PlaySound2(DecaySFX(0))
                //e\room\doors[0]\open = False
                e->room->npc[0]->state = 1;
                e->eventState = 2;

                e->sounds[0] = bbLoadSound("SFX/Character/Janitor/106Abduct.ogg");
                PlaySound2(e->sounds[0]);

                if (e->soundChannels[0]!=0) {
                    bbStopChannel(e->soundChannels[0]);
                }
            } else if ((e->room->dist < 8)) {
                if (e->sounds[0] == 0) {
                    e->sounds[0] = bbLoadSound("SFX/Character/Janitor/Idle.ogg");
                }
                e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->npc[0]->obj, 15.f);
            }
        } else if ((e->eventState == 2)) {
            dist = bbEntityDistance(e->room->npc[0]->collider, e->room->obj);
            if (dist<1.5f) {
                de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->obj), 0.01f, bbEntityZ(e->room->obj), 90, bbRand(360), 0);
                de->size = 0.05f;
                de->sizeChange = 0.008f;
                de->timer = 10000;
                UpdateDecals();
                e->eventState = 3;

                //PlaySound2(DecaySFX(1))
            }
        } else {
            dist = Distance(bbEntityX(e->room->npc[0]->collider),bbEntityZ(e->room->npc[0]->collider), bbEntityX(e->room->obj),bbEntityZ(e->room->obj));
            bbPositionEntity(Curr106->obj, bbEntityX(e->room->obj, true), 0.f, bbEntityZ(e->room->obj, true));
            //ResetEntity(Curr106\collider)
            bbPointEntity(Curr106->obj, e->room->npc[0]->collider);
            bbRotateEntity(Curr106->obj, 0, bbEntityYaw(Curr106->obj), 0, true);

            Curr106->idle = true;

            if (dist<0.4f) {
                if (e->room->npc[0]->state==1) {
                    //PlaySound2(HorrorSFX(10))
                    SetNPCFrame(e->room->npc[0],41);
                }
                e->eventState = e->eventState+timing->tickDuration/2;
                e->room->npc[0]->state = 6;
                e->room->npc[0]->currSpeed = CurveValue(0.f, e->room->npc[0]->currSpeed, 25.f);
                bbPositionEntity(e->room->npc[0]->collider, CurveValue(bbEntityX(e->room->obj, true), bbEntityX(e->room->npc[0]->collider), 25.f), 0.3f-e->eventState/70, CurveValue(bbEntityZ(e->room->obj, true), bbEntityZ(e->room->npc[0]->collider), 25.f));
                bbResetEntity(e->room->npc[0]->collider);

                //TurnEntity(e\room\npc[0]\collider,0,0,0.5f*timing\tickDuration)
                AnimateNPC(e->room->npc[0], 41, 58, 0.1f, false);

                AnimateNPC(Curr106, 206,112, -1.f, false);
            } else {
                AnimateNPC(Curr106, 112,206, 1.5f, false);
            }

            if (e->eventState > 35) {
                //PlayRangedSound(OldManSFX(Rand(1,2)), mainPlayer\cam, e\room\npc[0]\collider)

                bbPositionEntity(Curr106->obj, bbEntityX(Curr106->collider), -100.f, bbEntityZ(Curr106->collider), true);
                bbPositionEntity(Curr106->collider, bbEntityX(Curr106->collider), -100.f, bbEntityZ(Curr106->collider), true);

                Curr106->idle = false;
                if (bbEntityDistance(mainPlayer->collider, e->room->obj)<2.5f) {
                    Curr106->state = -0.1f;
                }

                RemoveNPC(e->room->npc[0]);

                RemoveEvent(e);
            }
        }
    }
    //[End Block]
}

}

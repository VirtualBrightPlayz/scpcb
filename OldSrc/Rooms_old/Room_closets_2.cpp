#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "Room_closets_2.h"

namespace CBN {

// Functions.
void FillRoom_closets_2(Room* r) {
    Item* it = CreatePaper("doc173", r->x + 736.f * RoomScale, r->y + 176.f * RoomScale, r->z + 736.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("gasmask", r->x + 736.f * RoomScale, r->y + 176.f * RoomScale, r->z + 544.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("battery", r->x + 736.f * RoomScale, r->y + 176.f * RoomScale, r->z - 448.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x + 730.f * RoomScale, r->y + 176.f * RoomScale, r->z - 496.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }
    if (bbRand(2) == 1) {
        it = CreateItem("battery", r->x + 740.f * RoomScale, r->y + 176.f * RoomScale, r->z - 560.f * RoomScale);
        bbEntityParent(it->collider, r->obj);
    }

    Item* clipboard = CreateItem("clipboard",r->x + 736.f * RoomScale, r->y + 224.f * RoomScale, r->z -480.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x-1120*RoomScale, -256*RoomScale, r->z+896*RoomScale, true);
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x-1232*RoomScale, -256*RoomScale, r->z-160*RoomScale, true);

    Door* d = CreateDoor(r->x - 240.f * RoomScale, 0.f, r->z, 90, r, false);
    d->open = false;
    d->autoClose = false;
    bbMoveEntity(d->buttons[0], 0.f, 0.f, 22.f * RoomScale);
    bbMoveEntity(d->buttons[1], 0.f, 0.f, 22.f * RoomScale);

    SecurityCam* sc = CreateSecurityCam(r->x, r->y + 704*RoomScale, r->z + 863*RoomScale, r);
    sc->angle = 180;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    sc->id = 0;
    //sc\followPlayer = True
}

void UpdateEvent_closets_2(Event* e) {
    if (e->eventState == 0) {
        if (!e->loaded) {
            if (e->room->npc[0]==nullptr) {
                e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[0],true),bbEntityY(e->room->objects[0],true),bbEntityZ(e->room->objects[0],true));
            }
            e->room->npc[0]->texture = "GFX/NPCs/classd/janitor.jpg";
            Texture* tex = bbLoadTexture(e->room->npc[0]->texture);

            bbEntityTexture(e->room->npc[0]->obj, tex);
            bbFreeTexture(tex);

            e->room->npc[0]->sounds[0] = bbLoadSound("SFX/Room/Storeroom/Escape1.ogg");

            e->room->npc[0]->soundChannels[0] = PlayRangedSound(e->room->npc[0]->sounds[0], mainPlayer->cam, e->room->npc[0]->collider, 12);

            if (e->room->npc[1]==nullptr) {
                e->room->npc[1] = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[1],true),bbEntityY(e->room->objects[1],true),bbEntityZ(e->room->objects[1],true));
            }
            e->room->npc[1]->texture = "GFX/NPCs/classd/scientist.jpg";
            tex = bbLoadTexture(e->room->npc[1]->texture);
            bbEntityTexture(e->room->npc[1]->obj, tex);

            bbFreeTexture(tex);

            e->room->npc[1]->sounds[0] = bbLoadSound("SFX/Room/Storeroom/Escape2.ogg");

            bbPointEntity(e->room->npc[0]->collider, e->room->npc[1]->collider);
            bbPointEntity(e->room->npc[1]->collider, e->room->npc[0]->collider);

            e->loaded = true;
            e->eventState = 1;
        }
    } else {
        // TODO: This would break the event if you stepped out in the middle of it.
        // Just putting this here to stop it from playing the event from any room.
        if (e->room == mainPlayer->currRoom) {
            e->eventState = e->eventState + timing->tickDuration;
            if (e->eventState < 70 * 3.5f) {
                bbRotateEntity(e->room->npc[1]->collider, 0.f, CurveAngle(e->room->angle + 90, bbEntityYaw(e->room->npc[1]->collider), 100.f), 0.f, true);

                e->room->npc[0]->state = 1;
                //TODO: 173Vent
                //If (e\eventState > 70*3.2f And e\eventState-timing\tickDuration =< 70*3.2f) Then PlayRangedSound(IntroSFX(15),mainPlayer\cam,e\room\obj,15.f)
            } else if (e->eventState < 70 * 6.5f) {
                if (e->eventState - timing->tickDuration < 70 * 3.5f) {
                    e->room->npc[0]->state = 0;
                    e->room->npc[1]->soundChannels[0] = PlayRangedSound(e->room->npc[1]->sounds[0], mainPlayer->cam, e->room->npc[1]->collider, 12.f);
                }

                if (e->eventState > 70 * 4.5f) {
                    bbPointEntity(e->room->npc[0]->obj, e->room->obj);
                    bbRotateEntity(e->room->npc[0]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[0]->obj), bbEntityYaw(e->room->npc[0]->collider), 30.f), 0, true);
                }
                bbPointEntity(e->room->npc[1]->obj, e->room->obj);
                bbTurnEntity(e->room->npc[1]->obj, 0, bbSin(e->eventState) * 25, 0);
                bbRotateEntity(e->room->npc[1]->collider, 0, CurveAngle(bbEntityYaw(e->room->npc[1]->obj), bbEntityYaw(e->room->npc[1]->collider), 30.f), 0, true);
            } else {
                if (e->eventState - timing->tickDuration < 70 * 6.5f) {
                    PlaySound_SM(sndMgmt->lightSwitch);
                }
                mainPlayer->blinkTimer = Max((70 * 6.5f - e->eventState) / 5.f - bbRnd(0.f, 2.f), -10);
                if (mainPlayer->blinkTimer == -10) {
                    //TODO: fix
                    //If (e\eventState > 70*7.5f And e\eventState-timing\tickDuration =< 70*7.5f) Then PlayRangedSound(NeckSnapSFX(0),mainPlayer\cam,e\room\npc[0]\collider,8.f)
                    //If (e\eventState > 70*8.f And e\eventState-timing\tickDuration =< 70*8.f) Then PlayRangedSound(NeckSnapSFX(1),mainPlayer\cam,e\room\npc[1]\collider,8.f)
                    SetNPCFrame(e->room->npc[0], 60);
                    e->room->npc[0]->state = 8;

                    SetNPCFrame(e->room->npc[1], 19);
                    e->room->npc[1]->state = 6;
                }

                if (e->eventState > 70 * 8.5f) {
                    bbPositionEntity(Curr173->collider, (bbEntityX(e->room->objects[0], true) + bbEntityX(e->room->objects[1], true)) / 2, bbEntityY(e->room->objects[0], true), (bbEntityZ(e->room->objects[0], true) + bbEntityZ(e->room->objects[1], true)) / 2);
                    bbPointEntity(Curr173->collider, mainPlayer->collider);
                    bbResetEntity(Curr173->collider);
                    RemoveEvent(e);
                }
            }
        }
    }

}

}

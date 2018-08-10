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
#include "Room_test_smallwindow_2.h"

namespace CBN {

// Constants.
const int ROOM_TSW2_173SPAWN = 0;
const int ROOM_TSW2_173JUMP = 1;
const int ROOM_TSW2_GLASS = 2;
const int EVENT_TSW2_173SPAWNED = 0;
const int EVENT_TSW2_173JUMPTIMER = 0;

// Functions.
void FillRoom_test_smallwindow_2(Room* r) {
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

    r->objects[ROOM_TSW2_173SPAWN] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x - 640.0 * RoomScale, 0.5, r->z - 912.0 * RoomScale);
    bbEntityParent(r->objects[0], r->obj);

    r->objects[ROOM_TSW2_173JUMP] = bbCreatePivot();
    bbPositionEntity(r->objects[1], r->x - 632.0 * RoomScale, 0.5, r->z - 16.0 * RoomScale);
    bbEntityParent(r->objects[1], r->obj);

    Texture* Glasstex = bbLoadTexture("GFX/Map/Textures/glass.png",1+2);
    r->objects[ROOM_TSW2_GLASS] = bbCreateSprite();
    bbEntityTexture((Sprite*)r->objects[ROOM_TSW2_GLASS],Glasstex);
    bbSpriteViewMode((Sprite*)r->objects[ROOM_TSW2_GLASS],2);
    bbScaleSprite((Sprite*)r->objects[ROOM_TSW2_GLASS],182.0*RoomScale*0.5, 192.0*RoomScale*0.5);
    bbPositionEntity(r->objects[ROOM_TSW2_GLASS], r->x - 595.0 * RoomScale, 224.0*RoomScale, r->z - 208.0 * RoomScale);
    bbTurnEntity(r->objects[ROOM_TSW2_GLASS],0,180,0);
    bbEntityParent(r->objects[ROOM_TSW2_GLASS], r->obj);
    bbHideEntity(r->objects[ROOM_TSW2_GLASS]);

    bbFreeTexture(Glasstex);

    r->doors[0] = CreateDoor(r->x - 240.0 * RoomScale, 0.0, r->z + 640.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = false;

    d = CreateDoor(r->x - 512.0 * RoomScale, 0.0, r->z + 384.0 * RoomScale, 0, r);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x - 816.0 * RoomScale, 0.0, r->z - 208.0 * RoomScale, 0, r);
    d->autoClose = false;
    d->open = false;
    bbFreeEntity(d->buttons[0]);
    d->buttons[0] = 0;
    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;

    it = CreateItem("navigator", r->x - 312.0 * RoomScale, r->y + 264.0 * RoomScale, r->z + 176.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEventTestroom173(Event* e) {
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
    if (mainPlayer->currRoom == e->room) {
        if (Curr173->idle == 0) {
            if (!e->intState[EVENT_TSW2_173SPAWNED]) {
                if (e->room->doors[0]->open) {
                    bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[0], true), 0.5, bbEntityZ(e->room->objects[0], true));
                    bbResetEntity(Curr173->collider);
                    e->intState[EVENT_TSW2_173SPAWNED] = true;
                }
            } else {
                bbShowEntity(e->room->objects[ROOM_TSW2_GLASS]);
                //start a timer for 173 breaking through the window
                e->floatState[EVENT_TSW2_173JUMPTIMER] = e->floatState[EVENT_TSW2_173JUMPTIMER]+timing->tickDuration;
                dist = bbEntityDistance(mainPlayer->collider, e->room->objects[1]);
                if (dist<1.0) {
                    //if close, increase the timer so that 173 is ready to attack
                    e->floatState[EVENT_TSW2_173JUMPTIMER] = Max(e->floatState[EVENT_TSW2_173JUMPTIMER], 70*12);
                } else if ((dist > 1.4)) {
                    //if the player moves a bit further and blinks, 173 attacks
                    if (e->floatState[EVENT_TSW2_173JUMPTIMER] > 70*12 & mainPlayer->blinkTimer <= -10) {
                        if (bbEntityDistance(Curr173->collider, e->room->objects[ROOM_TSW2_173SPAWN]) > 5.0) {
                            //if 173 is far away from the room (perhaps because the player
                            //left and 173 moved to some other room?) -> disable the event
                            RemoveEvent(e);
                        } else {
                            PlayRangedSound(LoadTempSound("SFX/General/GlassBreak.ogg"), mainPlayer->cam, Curr173->obj);
                            bbFreeEntity(e->room->objects[ROOM_TSW2_GLASS]);
                            bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[ROOM_TSW2_173SPAWN], true), 0.5, bbEntityZ(e->room->objects[1], true));
                            bbResetEntity(Curr173->collider);
                            RemoveEvent(e);
                        }
                    }
                }
            }
        }
    }
    //[End Block]
}

}

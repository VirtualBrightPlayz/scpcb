#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "Room_cont_500_1499_2.h"

namespace CBN {

// Functions.
void FillRoom_cont_500_1499_2(Room* r) {
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

    r->doors[0] = CreateDoor(r->x + 288.f*RoomScale, r->y, r->z + 576.f*RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[0]->open = false;
    r->doors[0]->locked = true;
    d = CreateDoor(r->x + 777.f*RoomScale, r->y, r->z + 671.f*RoomScale, 90, r);
    d = CreateDoor(r->x + 556.f*RoomScale, r->y, r->z + 296.f*RoomScale, 0, r);
    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0],r->x + 576.f*RoomScale,r->y+160.f*RoomScale,r->z+632.f*RoomScale);
    bbEntityParent(r->objects[0],r->obj);

    it = CreateItem("scp1499", r->x + 600.f * RoomScale, r->y + 176.f * RoomScale, r->z - 228.f * RoomScale);
    bbRotateEntity(it->collider, 0, r->angle, 0);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("badge", r->x + 364.f * RoomScale, r->y + 5.f * RoomScale, r->z + 716.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_cont_500_1499_2(Event* e) {
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
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    //If mainPlayer\currRoom = e\room
    if (e->room->dist < 15) {
        if (Contained106) {
            e->eventState = 2.f;
        }
        if (Curr106->state < 0) {
            e->eventState = 2.f;
        }

        if (e->eventState < 2.f) {
            if (e->eventState == 0.f) {
                LoadEventSound(e,"SFX/Character/Scientist/EmilyScream.ogg");
                e->soundChannels[0] = PlayRangedSound(e->sounds[0], mainPlayer->cam, e->room->objects[0], 100, 1.f);
                de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[0],true), e->room->y+2.f*RoomScale, bbEntityZ(e->room->objects[0],true), 90, bbRand(360), 0);
                de->size = 0.5f;
                bbEntityAlpha(de->obj, 0.8f);
                bbEntityFX(de->obj,1);
                e->eventState = 1.f;
            } else if ((e->eventState == 1.f)) {
                if (!bbChannelPlaying(e->soundChannels[0])) {
                    e->eventState = 2.f;
                    e->room->doors[0]->locked = false;
                } else {
                    UpdateRangedSoundOrigin(e->soundChannels[0],mainPlayer->cam,e->room->objects[0],100,1.f);
                }
            }
        } else {
            std::cout << "Removed 'room2scps2' event";
            e->room->doors[0]->locked = false;
            de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[0],true), e->room->y+2.f*RoomScale, bbEntityZ(e->room->objects[0],true), 90, bbRand(360), 0);
            de->size = 0.5f;
            bbEntityAlpha(de->obj, 0.8f);
            bbEntityFX(de->obj,1);
            RemoveEvent(e);
        }
    }
    //[End Block]
}

}

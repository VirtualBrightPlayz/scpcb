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

    r->doors[0] = CreateDoor(r->x + 288.0*RoomScale, r->y, r->z + 576.0*RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[0]->open = false;
    r->doors[0]->locked = true;
    d = CreateDoor(r->x + 777.0*RoomScale, r->y, r->z + 671.0*RoomScale, 90, r);
    d = CreateDoor(r->x + 556.0*RoomScale, r->y, r->z + 296.0*RoomScale, 0, r);
    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0],r->x + 576.0*RoomScale,r->y+160.0*RoomScale,r->z+632.0*RoomScale);
    bbEntityParent(r->objects[0],r->obj);

    it = CreateItem("scp1499", r->x + 600.0 * RoomScale, r->y + 176.0 * RoomScale, r->z - 228.0 * RoomScale);
    bbRotateEntity(it->collider, 0, r->angle, 0);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("badge", r->x + 364.0 * RoomScale, r->y + 5.0 * RoomScale, r->z + 716.0 * RoomScale);
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
            e->eventState = 2.0;
        }
        if (Curr106->state < 0) {
            e->eventState = 2.0;
        }

        if (e->eventState < 2.0) {
            if (e->eventState == 0.0) {
                LoadEventSound(e,"SFX/Character/Scientist/EmilyScream.ogg");
                e->soundChannels[0] = PlayRangedSound(e->sounds[0], mainPlayer->cam, e->room->objects[0], 100, 1.0);
                de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[0],true), e->room->y+2.0*RoomScale, bbEntityZ(e->room->objects[0],true), 90, bbRand(360), 0);
                de->size = 0.5;
                bbEntityAlpha(de->obj, 0.8);
                bbEntityFX(de->obj,1);
                e->eventState = 1.0;
            } else if ((e->eventState == 1.0)) {
                if (!bbChannelPlaying(e->soundChannels[0])) {
                    e->eventState = 2.0;
                    e->room->doors[0]->locked = false;
                } else {
                    UpdateRangedSoundOrigin(e->soundChannels[0],mainPlayer->cam,e->room->objects[0],100,1.0);
                }
            }
        } else {
            std::cout << "Removed 'room2scps2' event";
            e->room->doors[0]->locked = false;
            de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[0],true), e->room->y+2.0*RoomScale, bbEntityZ(e->room->objects[0],true), 90, bbRand(360), 0);
            de->size = 0.5;
            bbEntityAlpha(de->obj, 0.8);
            bbEntityFX(de->obj,1);
            RemoveEvent(e);
        }
    }
    //[End Block]
}

}

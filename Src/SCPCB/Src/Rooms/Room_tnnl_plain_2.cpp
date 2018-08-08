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
#include "Room_tnnl_plain_2.h"

namespace CBN {

// Functions.
void UpdateEventTunnel106(Event* e) {
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
    Decal* d;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    if (e->eventState == 0) {
        if (e->room->dist < 5.0 & e->room->dist > 0) {
            if (Curr106->state >= 0) {
                e->eventState = 1;
            }
        } else if ((Contained106)) {
            RemoveEvent(e);
        }
    } else if (e->eventState == 1) {

        if (e->room->dist < 3.0 | bbRand(7000)==1) {
            e->eventState = 2;
            d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->obj), 445.0*RoomScale, bbEntityZ(e->room->obj), -90, bbRand(360), 0);
            d->size = bbRnd(0.5, 0.7);
            bbEntityAlpha(d->obj, 0.7);
            d->id = 1;
            bbScaleSprite(d->obj, d->size, d->size);
            bbEntityAlpha(d->obj, bbRnd(0.7, 0.85));

            //PlaySound2(HorrorSFX(10))
        } else if ((e->room->dist > 8.0)) {
            if (bbRand(5) == 1) {
                Curr106->idle = false;
                RemoveEvent(e);
            } else {
                Curr106->idle = false;
                Curr106->state = -10000;
                RemoveEvent(e);
            }
        }
    } else {
        e->eventState = e->eventState+timing->tickDuration;

        bbPositionEntity(Curr106->collider, bbEntityX(e->room->obj, true) - bbSin(TimeInPosMilliSecs() / 150.0) / 4.0, bbEntityY(mainPlayer->collider) + 1.0 - Min(bbSin(e->eventState)*1.5,1.1), bbEntityZ(e->room->obj, true) - bbSin(TimeInPosMilliSecs() / 190.0) / 4.0);

        //TranslateEntity(Curr106\collider, 0, -Max((3.0-dist),0), 0, True)
        bbPointEntity(Curr106->collider, mainPlayer->cam);
        Curr106->state = -11;
        AnimateNPC(Curr106, 55, 104, 0.1);
        Curr106->idle = true;

        if (e->eventState > 180) {
            Curr106->idle = false;
            bbPositionEntity(Curr106->collider, bbEntityX(Curr106->collider), -3.0, bbEntityZ(Curr106->collider), true);

            RemoveEvent(e);
        }

    }
    //[End Block]

}

void UpdateEvent682roar(Event* e) {
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
    if (e->eventState == 0) {
        if (mainPlayer->currRoom == e->room) {
            e->eventState = 70 * bbRand(300,1000);
        }
    } else if ((mainPlayer->currRoom->roomTemplate->name != "pocketdimension" & mainPlayer->currRoom->roomTemplate->name != "room860" & mainPlayer->currRoom->roomTemplate->name != "room1123" & mainPlayer->currRoom->roomTemplate->name != "dimension1499")) {
        e->eventState = e->eventState-timing->tickDuration;

        if (e->eventState < 17*70) {
            if (e->eventState+timing->tickDuration >= 17*70) {
                //e\sounds[0] = LoadSound("SFX/SCP/682/Roar.ogg")
                LoadEventSound(e,"SFX/SCP/682/Roar.ogg");
                e->soundChannels[0] = bbPlaySound(e->sounds[0]);
            }
            if (e->eventState > 17*70 - 3*70) {
                mainPlayer->camShake = 0.5;
            }
            if (e->eventState < 17*70 - 7.5*70 & e->eventState > 17*70 - 11*70) {
                mainPlayer->camShake = 2.0;
            }
            if (e->eventState < 70) {
                if (e->sounds[0]!=0) {
                    bbFreeSound(e->sounds[0]);
                }
                RemoveEvent(e);
            }
        }
    }
    //[End Block]

}

}

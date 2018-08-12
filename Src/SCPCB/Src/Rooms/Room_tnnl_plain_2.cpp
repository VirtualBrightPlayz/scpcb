#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
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
        if (e->room->dist < 5.f && e->room->dist > 0) {
            if (Curr106->state >= 0) {
                e->eventState = 1;
            }
        } else if ((Contained106)) {
            RemoveEvent(e);
        }
    } else if (e->eventState == 1) {

        if (e->room->dist < 3.f || bbRand(7000)==1) {
            e->eventState = 2;
            d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->obj), 445.f*RoomScale, bbEntityZ(e->room->obj), -90, bbRand(360), 0);
            d->size = bbRnd(0.5f, 0.7f);
            bbEntityAlpha(d->obj, 0.7f);
            d->id = 1;
            bbScaleSprite(d->obj, d->size, d->size);
            bbEntityAlpha(d->obj, bbRnd(0.7f, 0.85f));

            //PlaySound2(HorrorSFX(10))
        } else if ((e->room->dist > 8.f)) {
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

        bbPositionEntity(Curr106->collider, bbEntityX(e->room->obj, true) - bbSin(TimeInPosMilliSecs() / 150.f) / 4.f, bbEntityY(mainPlayer->collider) + 1.f - Min(bbSin(e->eventState)*1.5f,1.1f), bbEntityZ(e->room->obj, true) - bbSin(TimeInPosMilliSecs() / 190.f) / 4.f);

        //TranslateEntity(Curr106\collider, 0, -Max((3.f-dist),0), 0, True)
        bbPointEntity(Curr106->collider, mainPlayer->cam);
        Curr106->state = -11;
        AnimateNPC(Curr106, 55, 104, 0.1f);
        Curr106->idle = true;

        if (e->eventState > 180) {
            Curr106->idle = false;
            bbPositionEntity(Curr106->collider, bbEntityX(Curr106->collider), -3.f, bbEntityZ(Curr106->collider), true);

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
    } else if (!mainPlayer->currRoom->roomTemplate->name.equals("pocketdimension") && !mainPlayer->currRoom->roomTemplate->name.equals("room860") && !mainPlayer->currRoom->roomTemplate->name.equals("room1123") && !mainPlayer->currRoom->roomTemplate->name.equals("dimension1499")) {
        e->eventState = e->eventState-timing->tickDuration;

        if (e->eventState < 17*70) {
            if (e->eventState+timing->tickDuration >= 17*70) {
                //e\sounds[0] = LoadSound("SFX/SCP/682/Roar.ogg")
                LoadEventSound(e,"SFX/SCP/682/Roar.ogg");
                e->soundChannels[0] = bbPlaySound(e->sounds[0]);
            }
            if (e->eventState > 17*70 - 3*70) {
                mainPlayer->camShake = 0.5f;
            }
            if (e->eventState < 17*70 - 7.5f*70 && e->eventState > 17*70 - 11*70) {
                mainPlayer->camShake = 2.f;
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

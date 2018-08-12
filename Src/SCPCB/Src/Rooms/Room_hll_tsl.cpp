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
#include "Room_hll_tsl.h"

namespace CBN {

// Functions.
void FillRoom_hll_tsl(Room* r) {
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

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x - 114.f * RoomScale, 0.f, r->z);
    bbEntityParent(r->objects[0], r->obj);

    r->objects[1] = bbCreatePivot();
    bbPositionEntity(r->objects[1], r->x + 114.f * RoomScale, 0.f, r->z);
    bbEntityParent(r->objects[1], r->obj);

    r->objects[2] = bbCreatePivot();
    bbPositionEntity(r->objects[2], r->x, 0.f, r->z);
    bbEntityParent(r->objects[2], r->obj);

    r->objects[3] = bbCreateSprite();
    bbEntityTexture((Sprite*)r->objects[3], TeslaTexture);
    bbSpriteViewMode((Sprite*)r->objects[3],2);
    //ScaleSprite(r\objects[3],((512.f * RoomScale)/2.f),((512.f * RoomScale)/2.f))
    bbEntityBlend((Sprite*)r->objects[3], 3);
    bbEntityFX((Sprite*)r->objects[3], 1 + 8 + 16);

    bbPositionEntity(r->objects[3], r->x, 0.8f, r->z);

    bbHideEntity(r->objects[3]);
    bbEntityParent(r->objects[3], r->obj);

    //TODO: fix?
    //w.waypoints = CreateWaypoint(r\x, r\y + 66.f * RoomScale, r\z + 292.f * RoomScale, Null, r)
    //w2.waypoints = CreateWaypoint(r\x, r\y + 66.f * RoomScale, r\z - 284.f * RoomScale, Null, r)
    //w\connected[0] = w2
    // w->dist[0] = bbEntityDistance(w->obj, w2->obj);
    //w2\connected[0] = w
    // w2->dist[0] = w->dist[0];

    Texture* lightSpriteRed = bbLoadTexture("GFX/Sprites/light_flare_red.jpg", 1);
    r->objects[4] = bbCreateSprite();
    bbPositionEntity(r->objects[4], r->x - 32 * RoomScale, 568 * RoomScale, r->z);
    bbScaleSprite((Sprite*)r->objects[4], 0.03f, 0.03f);
    bbEntityTexture((Sprite*)r->objects[4], lightSpriteRed);
    bbEntityBlend((Sprite*)r->objects[4], 3);
    bbEntityParent(r->objects[4], r->obj);
    bbHideEntity(r->objects[4]);
    bbFreeTexture(lightSpriteRed);

    r->objects[5] = bbCreatePivot();
    bbPositionEntity(r->objects[5],r->x,0,r->z-800*RoomScale);
    bbEntityParent(r->objects[5],r->obj);

    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6],r->x,0,r->z+800*RoomScale);
    bbEntityParent(r->objects[6],r->obj);
}

void UpdateEvent_tesla(Event* e) {
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
    temp = true;
    if (e->eventState2 > 70*3.5f && e->eventState2 < 70*90) {
        temp = false;
    }

    if (temp & bbEntityY(mainPlayer->collider, true) > bbEntityY(e->room->obj,true) && bbEntityY(mainPlayer->collider, true) < 4.f) {
        if (e->eventState == 0) {
            if ((TimeInPosMilliSecs() % 1500) < 800) {
                bbShowEntity(e->room->objects[4]);
            } else {
                bbHideEntity(e->room->objects[4]);
            }

            if (e->room->dist < 8) {
                if (!bbChannelPlaying(e->soundChannels[0])) {
                    e->soundChannels[0] = PlayRangedSound_SM(sndManager->teslaIdle, mainPlayer->cam, e->room->objects[3],4.f,0.5f);
                }
            }

            for (i = 0; i <= 2; i++) {
                if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true)) < 300.f*RoomScale) {
                    //play the activation sound
                    if (!mainPlayer->dead) {
                        mainPlayer->loudness = Max(8.f,mainPlayer->loudness);
                        bbStopChannel(e->soundChannels[0]);
                        e->soundChannels[0] = PlayRangedSound_SM(sndManager->teslaActive, mainPlayer->cam, e->room->objects[3],4.f,0.5f);
                        e->eventState = 1;
                        break;
                    }
                }
            }

            if (Curr106->state < -10 && e->eventState == 0) {
                for (i = 0; i <= 2; i++) {
                    if (Distance(bbEntityX(Curr106->collider),bbEntityZ(Curr106->collider),bbEntityX(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true)) < 300.f*RoomScale) {
                        //play the activation sound
                        if (!mainPlayer->dead) {
                            bbStopChannel(e->soundChannels[0]);
                            e->soundChannels[0] = PlayRangedSound_SM(sndManager->teslaActive, mainPlayer->cam, e->room->objects[3],4.f,0.5f);
                            bbHideEntity(e->room->objects[4]);
                            e->eventState = 1;
                            Curr106->state = 70 * 60 * bbRand(10,13);
                            break;
                        }
                    }
                }
            }
        } else {
            e->eventState = e->eventState+timing->tickDuration;
            if (e->eventState <= 40) {
                if ((TimeInPosMilliSecs() % 100) < 50) {
                    bbShowEntity(e->room->objects[4]);
                } else {
                    bbHideEntity(e->room->objects[4]);
                }
            } else {
                if (e->room->dist < 2) {
                    if (e->eventState-timing->tickDuration <= 40) {
                        PlaySound_SM(sndManager->teslaShock);
                    }
                } else {
                    if (e->eventState-timing->tickDuration <= 40) {
                        PlayRangedSound_SM(sndManager->teslaShock,mainPlayer->cam,e->room->objects[2]);
                    }
                }
                if (e->eventState < 70) {

                    if (!mainPlayer->dead) {
                        for (i = 0; i <= 2; i++) {
                            if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true)) < 250.f*RoomScale) {
                                bbShowEntity(mainPlayer->overlays[OVERLAY_WHITE]);
                                mainPlayer->lightFlash = 0.4f;
                                mainPlayer->camShake = 1.f;
                                Kill(mainPlayer);
                                DeathMSG = "Subject D-9341 killed by the Tesla gate at [REDACTED].";
                            }
                        }
                    }

                    if (Curr106->state < -10) {
                        for (i = 0; i <= 2; i++) {
                            if (Distance(bbEntityX(Curr106->collider),bbEntityZ(Curr106->collider),bbEntityX(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true)) < 250.f*RoomScale) {
                                bbShowEntity(mainPlayer->overlays[OVERLAY_WHITE]);
                                mainPlayer->lightFlash = 0.3f;
                                for (i = 0; i <= 10; i++) {
                                    p = CreateParticle(bbEntityX(Curr106->collider, true), bbEntityY(Curr106->collider, true), bbEntityZ(Curr106->collider, true), PARTICLE_SMOKE_BLACK, 0.015f, -0.2f, 250);
                                    p->size = 0.03f;
                                    p->gravity = -0.2f;
                                    p->lifetime = 200;
                                    p->sizeChange = 0.005f;
                                    p->speed = 0.001f;
                                    bbRotateEntity(p->pvt, bbRnd(360), bbRnd(360), 0, true);
                                }
                                Curr106->state = -20000;
                                bbTranslateEntity(Curr106->collider,0,-50.f,0,true);
                            }
                        }
                    }

                    bbHideEntity(e->room->objects[3]);
                    bbHideEntity(e->room->objects[4]);

                    if (bbRand(5)<5) {
                        bbPositionTexture(TeslaTexture,0.f,bbRnd(0,1.f));
                        bbShowEntity(e->room->objects[3]);
                    }
                } else {
                    if (e->eventState-timing->tickDuration < 70) {
                        bbStopChannel(e->soundChannels[0]);
                        e->soundChannels[0] = PlayRangedSound_SM(sndManager->teslaPowerUp, mainPlayer->cam, e->room->objects[3],4.f,0.5f);
                    }
                    bbHideEntity(e->room->objects[3]);

                    if (e->eventState > 150) {
                        e->eventState = 0;
                    }
                }
            }
        }
    }

    if (!mainPlayer->currRoom->roomTemplate->name.equals("pocketdimension") && !mainPlayer->currRoom->roomTemplate->name.equals("room860")) {
        if (e->eventState2 == 0) {
            if (e->eventState3 <=0) {
                temp = false;
                for (int iterator186 = 0; iterator186 < NPC::getListSize(); iterator186++) {
                    n = NPC::getObject(iterator186);

                    if (n->npcType == NPCtypeMTF) {
                        if (abs(bbEntityX(n->collider)-bbEntityX(e->room->obj,true))<4.f) {
                            if (abs(bbEntityZ(n->collider)-bbEntityZ(e->room->obj,true))<4.f) {
                                temp = true;
                                if (e->eventState2 == 0) {
                                    n->sounds[0] = bbLoadSound("SFX/Character/MTF/Tesla0.ogg");
                                    PlayMTFSound(n->sounds[0],n);

                                    LoadEventSound(e,"SFX/Character/MTF/Tesla1.ogg");
                                    e->soundChannels[0] = bbPlaySound(e->sounds[0]);
                                    n->idle = 70*10;
                                    e->eventState2 = 70*100;
                                }
                            }
                        }
                    }
                }
                if (temp == false) {
                    e->eventState2 = 70*3.5f;
                }
                e->eventState3 = e->eventState3+140;
            } else {
                e->eventState3 = e->eventState3-timing->tickDuration;
            }
        } else {
            e->eventState2 = Max(e->eventState2-timing->tickDuration,0);
        }
    }

    //[End Block]
}

}

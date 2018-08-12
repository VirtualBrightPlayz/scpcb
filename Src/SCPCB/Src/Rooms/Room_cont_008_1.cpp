#include <bbblitz3d.h>
#include <bbmath.h>

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
#include "Room_cont_008_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_008_1(Room* r) {
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

    //the container
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 292.f * RoomScale, 130.f*RoomScale, r->z + 516.f * RoomScale, true);

    //the lid of the container
    r->objects[1] = bbLoadMesh("GFX/Map/008_2.b3d");
    bbScaleEntity(r->objects[1], RoomScale, RoomScale, RoomScale);
    bbPositionEntity(r->objects[1], r->x + 292 * RoomScale, 151 * RoomScale, r->z + 576.f * RoomScale, 0);
    bbEntityParent(r->objects[1], r->obj);

    bbRotateEntity(r->objects[1],89,0,0,true);

    Texture* glassTex = bbLoadTexture("GFX/Map/Textures/glass.png",1+2);
    r->objects[2] = bbCreateSprite();
    bbEntityTexture((Sprite*)r->objects[2],glassTex);
    bbSpriteViewMode((Sprite*)r->objects[2],2);
    bbScaleSprite((Sprite*)r->objects[2],256.f*RoomScale*0.5f, 194.f*RoomScale*0.5f);
    bbPositionEntity(r->objects[2], r->x - 176.f * RoomScale, 224.f*RoomScale, r->z + 448.f * RoomScale);
    bbTurnEntity(r->objects[2],0,90,0);
    bbEntityParent(r->objects[2], r->obj);

    bbFreeTexture(glassTex);

    //scp-173 spawnpoint
    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x - 445.f * RoomScale, 120.f*RoomScale, r->z + 544.f * RoomScale, true);

    //scp-173 attack point
    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x + 67.f * RoomScale, 120.f*RoomScale, r->z + 464.f * RoomScale, true);

    Texture* lightSpriteRed = bbLoadTexture("GFX/Sprites/light_flare_red.jpg", 1);
    r->objects[5] = bbCreateSprite();
    bbPositionEntity(r->objects[5], r->x - 158 * RoomScale, 368 * RoomScale, r->z + 298.f * RoomScale);
    bbScaleSprite((Sprite*)r->objects[5], 0.02f, 0.02f);
    bbEntityTexture((Sprite*)r->objects[5], lightSpriteRed);
    bbEntityBlend((Sprite*)r->objects[5], 3);
    bbEntityParent(r->objects[5], r->obj);
    bbHideEntity(r->objects[5]);
    bbFreeTexture(lightSpriteRed);

    d = CreateDoor(r->x + 296.f * RoomScale, 0, r->z - 672.f * RoomScale, 180, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    bbPositionEntity(d->buttons[1], r->x + 164.f * RoomScale, bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true), true);
    bbFreeEntity(d->buttons[0]);
    d->buttons[0] = 0;
    bbFreeEntity(d->obj2);
    d->obj2 = 0;
    r->doors[0] = d;

    d2 = CreateDoor(r->x + 296.f * RoomScale, 0, r->z - 144.f * RoomScale, 0, r, false);
    d2->autoClose = false;
    bbPositionEntity(d2->buttons[0], r->x + 432.f * RoomScale, bbEntityY(d2->buttons[0],true), r->z - 480.f * RoomScale, true);
    bbRotateEntity(d2->buttons[0], 0, -90, 0, true);
    bbPositionEntity(d2->buttons[1], r->x + 164.f * RoomScale, bbEntityY(d2->buttons[0],true), r->z - 128.f * RoomScale, true);
    bbFreeEntity(d2->obj2);
    d2->obj2 = 0;
    r->doors[1] = d2;

    d->linkedDoor = d2;
    d2->linkedDoor = d;

    d = CreateDoor(r->x - 384.f * RoomScale, 0, r->z - 672.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->locked = true;
    r->doors[2] = d;


    it = CreateItem("hazmatsuit", r->x - 76.f * RoomScale, 0.5f, r->z - 396.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
    bbRotateEntity(it->collider, 0, 90, 0);

    it = CreatePaper("doc008", r->x - 245.f * RoomScale, r->y + 192.f * RoomScale, r->z + 368.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    //spawnpoint for the scientist used in the "008 zombie scene"
    r->objects[6] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[6], r->x + 160 * RoomScale, 672 * RoomScale, r->z - 384.f * RoomScale, true);
    //spawnpoint for the player
    r->objects[7] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[7], r->x, 672 * RoomScale, r->z + 352.f * RoomScale, true);

    sc = CreateSecurityCam(r->x+578.956f*RoomScale, r->y+444.956f*RoomScale, r->z+772.f*RoomScale, r);
    sc->angle = 135;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    sc->id = 7;
}

void UpdateEvent_cont_008_1(Event* e) {
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


    if (mainPlayer->currRoom == e->room) {
        if (Curr173->idle<2) {
            //container open
            if (e->eventState == 0) {

                bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[3],true),0.5f,bbEntityZ(e->room->objects[3],true),true);
                bbResetEntity(Curr173->collider);

                e->eventState = 1;
            } else if ((e->eventState == 1)) {
                e->soundChannels[0] = LoopRangedSound_SM(sndManager->alarm, e->soundChannels[0], mainPlayer->cam, e->room->objects[0], 5.f);

                if ((TimeInPosMilliSecs() % 1000)<500) {
                    bbShowEntity(e->room->objects[5]);
                } else {
                    bbHideEntity(e->room->objects[5]);
                }

                dist = bbEntityDistance(mainPlayer->collider, e->room->objects[0]);
                if (dist<2.f) {
                    e->room->doors[0]->locked = true;
                    e->room->doors[1]->locked = true;

                    if (e->eventState2==0) {
                        bbShowEntity(e->room->objects[2]);
                        if (mainPlayer->blinkTimer<-10 && Curr173->idle == 0) {
                            bbPositionEntity(Curr173->collider, bbEntityX(e->room->objects[4],true),0.5f,bbEntityZ(e->room->objects[4],true),true);
                            bbResetEntity(Curr173->collider);

                            bbHideEntity(e->room->objects[2]);

                            if (!IsPlayerWearingItem(mainPlayer,"hazmatsuit")) {
                                mainPlayer->injuries = mainPlayer->injuries+0.1f;
                                mainPlayer->infect008 = 1;
                                Msg = "The window shattered and a piece of glass cut your arm.";
                                MsgTimer = 70*8;
                            }

                            PlayRangedSound(LoadTempSound("SFX/General/GlassBreak.ogg"), mainPlayer->cam, e->room->objects[0]);

                            e->eventState2 = 1;
                        }
                    }

                    if (dist<1.f) {
                        if (bbEntityInView(e->room->objects[0], mainPlayer->cam)) {
                            DrawHandIcon = true;

                            if (MouseDown1) {
                                bbRotateEntity(e->room->objects[1], Max(Min(bbEntityPitch(e->room->objects[1])+Max(Min(-mouse_y_speed_1,10.f),-10), 89), 35), bbEntityYaw(e->room->objects[1]), 0);
                            }
                        }
                    }
                }

                if (bbEntityPitch(e->room->objects[1],true)<40) {
                    e->eventState = 2;
                    PlaySound_SM(sndManager->lever);
                } else {
                    p = CreateParticle(bbEntityX(e->room->objects[0],true),bbEntityY(e->room->objects[0],true),bbEntityZ(e->room->objects[0],true), PARTICLE_SMOKE_WHITE, 0.02f, -0.12f);
                    bbRotateEntity(p->pvt,-90,0,0,true);
                    bbTurnEntity(p->pvt, bbRnd(-26,26), bbRnd(-26,26), bbRnd(360));

                    p->sizeChange = 0.012f;
                    p->aChange = -0.015f;
                }
            } else {
                bbHideEntity(e->room->objects[5]);
                e->room->doors[0]->locked = false;
                e->room->doors[1]->locked = false;
                e->room->doors[2]->locked = false;

                bbRotateEntity(e->room->objects[1],CurveAngle(1,bbEntityPitch(e->room->objects[1],true),15.f),bbEntityYaw(e->room->objects[1],true),0,true);

                if (bbEntityPitch(e->room->objects[1],true)<=1.f) {
                    RemoveEvent(e);
                }
            }
        } else {
            p = CreateParticle(bbEntityX(e->room->objects[0],true),bbEntityY(e->room->objects[0],true),bbEntityZ(e->room->objects[0],true), PARTICLE_SMOKE_WHITE, 0.02f, -0.12f);
            bbRotateEntity(p->pvt,-90,0,0,true);
            bbTurnEntity(p->pvt, bbRnd(-26,26), bbRnd(-26,26), bbRnd(360));
        }

    }

}

}

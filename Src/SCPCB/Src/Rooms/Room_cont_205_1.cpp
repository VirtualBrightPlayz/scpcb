#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "Room_cont_205_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_205_1(Room* r) {
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

    d = CreateDoor(r->x + 128.f * RoomScale, 0, r->z + 640.f *RoomScale, 90, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    //PositionEntity(d\buttons[0], r\x + 320.f * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    //PositionEntity(d\buttons[1], r\x + 224.f * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    r->doors[0] = CreateDoor(r->x - 1392.f * RoomScale, -128.f * RoomScale, r->z - 384*RoomScale, 0, r, true, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = false;
    bbFreeEntity(r->doors[0]->buttons[0]);
    r->doors[0]->buttons[0] = 0;
    bbFreeEntity(r->doors[0]->buttons[1]);
    r->doors[0]->buttons[1] = 0;

    sc = CreateSecurityCam(r->x - 1152.f * RoomScale, r->y + 900.f * RoomScale, r->z + 176.f * RoomScale, r, true);
    sc->angle = 90;
    sc->turn = 0;
    bbEntityParent(sc->obj, r->obj);

    sc->allowSaving = false;
    sc->renderInterval = 0;

    bbEntityParent(sc->scrObj, 0);
    bbPositionEntity(sc->scrObj, r->x - 1716.f * RoomScale, r->y + 160.f * RoomScale, r->z + 176.f * RoomScale, true);
    bbTurnEntity(sc->scrObj, 0, 90, 0);
    bbScaleSprite(sc->scrObj, 896.f*0.5f*RoomScale, 896.f*0.5f*RoomScale);

    bbEntityParent(sc->scrObj, r->obj);
    //EntityBlend(sc\scrObj, 2)

    bbCameraZoom(sc->cam, 1.5f);

    bbHideEntity(sc->scrOverlay);
    bbHideEntity(sc->monitorObj);

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x - 1536.f * RoomScale, r->y + 730.f * RoomScale, r->z + 192.f * RoomScale, true);
    bbRotateEntity(r->objects[0], 0,-90,0,true);

    r->objects[1] = sc->scrObj;
}

void UpdateEvent_cont_205_1(Event* e) {
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
        e->overwriteMusic = true;
        if (e->eventState==0 || e->room->objects[0]==0) {
            if (!e->loaded) {

                e->room->objects[3] = bbLoadAnimMesh("GFX/npcs/205_demon1.b3d");
                e->room->objects[4] = bbLoadAnimMesh("GFX/npcs/205_demon2.b3d");
                e->room->objects[5] = bbLoadAnimMesh("GFX/npcs/205_demon3.b3d");
                e->room->objects[6] = bbLoadAnimMesh("GFX/npcs/205_woman.b3d");

                for (i = 3; i <= 6; i++) {
                    //EntityColor(e\room\objects[i], 30,30,30)
                    //EntityShininess(e\room\objects[i],0.f)
                    //vertex color + flatshaded
                    //EntityFX(e\room\objects[i], 2)

                    bbPositionEntity(e->room->objects[i], bbEntityX(e->room->objects[0],true), bbEntityY(e->room->objects[0],true), bbEntityZ(e->room->objects[0],true), true);
                    bbRotateEntity(e->room->objects[i], -90, bbEntityYaw(e->room->objects[0],true), 0, true);
                    bbScaleEntity(e->room->objects[i], 0.05f, 0.05f, 0.05f, true);
                }

                bbHideEntity(e->room->objects[3]);
                bbHideEntity(e->room->objects[4]);
                bbHideEntity(e->room->objects[5]);

                e->musicTrack = MUS_205;
                e->loaded = true;
                e->eventState = 1;
            }

        } else {

            if (e->eventState<65) {
                if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), bbEntityX(e->room->objects[0],true), bbEntityZ(e->room->objects[0],true))<2.f) {
                    PlaySound2(LoadTempSound("SFX/SCP/205/Enter.ogg"));

                    e->eventState = Max(e->eventState, 65);

                    bbShowEntity(e->room->objects[3]);
                    bbShowEntity(e->room->objects[4]);
                    bbShowEntity(e->room->objects[5]);
                    bbHideEntity(e->room->objects[6]);

                    bbSetAnimTime(e->room->objects[3], 492);
                    bbSetAnimTime(e->room->objects[4], 434);
                    bbSetAnimTime(e->room->objects[5], 434);

                    e->room->doors[0]->open = false;
                }

                if (e->eventState>7) {
                    if (bbRand(0,300)==1) {
                        e->room->doors[0]->open = !e->room->doors[0]->open;
                    }
                }

                e->eventState2 = e->eventState2 + timing->tickDuration;
            }


            switch ((int)e->eventState) {
                case 1: {
                    //sitting
                    Animate2((MeshModel*)e->room->objects[6], bbAnimTime(e->room->objects[6]), 526, 530, 0.2f);
                    if (e->eventState2 > 20*70) {
                        e->eventState = e->eventState+1;
                    }
                } break;
                case 3: {
                    //laying down
                    Animate2((MeshModel*)e->room->objects[6], bbAnimTime(e->room->objects[6]), 377, 525, 0.2f);
                    if (e->eventState2 > 30*70) {
                        e->eventState = e->eventState+1;
                    }
                } break;
                case 5: {
                    //standing
                    Animate2((MeshModel*)e->room->objects[6], bbAnimTime(e->room->objects[6]), 228, 376, 0.2f);
                    if (e->eventState2 > 40*70) {
                        e->eventState = e->eventState+1;
                        PlayRangedSound(LoadTempSound("SFX/SCP/205/Horror.ogg"), mainPlayer->cam, e->room->objects[6], 10, 0.3f);
                    }
                } break;
                case 7: {
                    //first demon appears
                    bbShowEntity(e->room->objects[5]);
                    //le sexy demon pose
                    Animate2((MeshModel*)e->room->objects[5], bbAnimTime(e->room->objects[5]), 500, 648, 0.2f);
                    //TurnEntity(e\room\objects[6], 0.f, DeltaYaw(e\room\objects[6], e\room\objects[5])*0.01f, 0.f, True)
                    if (e->eventState2 > 60*70) {
                        e->eventState = e->eventState+1;
                        PlayRangedSound(LoadTempSound("SFX/SCP/205/Horror.ogg"), mainPlayer->cam, e->room->objects[6], 10, 0.5f);
                    }
                } break;
                case 9: {
                    //second demon appears
                    bbShowEntity(e->room->objects[4]);
                    //idle
                    Animate2((MeshModel*)e->room->objects[4], bbAnimTime(e->room->objects[4]), 2, 200, 0.2f);
                    Animate2((MeshModel*)e->room->objects[5], bbAnimTime(e->room->objects[5]), 4, 125, 0.2f);

                    //TurnEntity(e\room\objects[6], 0.f, DeltaYaw(e\room\objects[6], e\room\objects[4])*0.01f, 0.f, True)

                    if (e->eventState2 > 80*70) {
                        e->eventState = e->eventState+1;
                        PlaySound2(LoadTempSound("SFX/SCP/205/Horror.ogg"));
                    }
                } break;
                case 11: {
                    //third demon
                    bbShowEntity(e->room->objects[3]);
                    //idle
                    Animate2((MeshModel*)e->room->objects[3], bbAnimTime(e->room->objects[3]), 2, 226, 0.2f);
                    Animate2((MeshModel*)e->room->objects[4], bbAnimTime(e->room->objects[4]), 2, 200, 0.2f);
                    Animate2((MeshModel*)e->room->objects[5], bbAnimTime(e->room->objects[5]), 4, 125, 0.2f);

                    //TurnEntity(e\room\objects[6], 0.f, DeltaYaw(e\room\objects[5], e\room\objects[4])*0.01f, 0.f, True)

                    if (e->eventState2 > 85*70) {
                        e->eventState = e->eventState+1;
                    }
                } break;
                case 13: {
                    if (bbAnimTime(e->room->objects[6])!=227) {
                        bbSetAnimTime(e->room->objects[6], 227);
                    }

                    Animate2((MeshModel*)e->room->objects[3], bbAnimTime(e->room->objects[3]), 2, 491, 0.05f);
                    Animate2((MeshModel*)e->room->objects[4], bbAnimTime(e->room->objects[4]), 197, 433, 0.05f);
                    Animate2((MeshModel*)e->room->objects[5], bbAnimTime(e->room->objects[5]), 2, 433, 0.05f);
                } break;
                case 66: {
                    Animate2((MeshModel*)e->room->objects[3], bbAnimTime(e->room->objects[3]), 492, 534, 0.1f, false);
                    Animate2((MeshModel*)e->room->objects[4], bbAnimTime(e->room->objects[4]), 434, 466, 0.1f, false);
                    Animate2((MeshModel*)e->room->objects[5], bbAnimTime(e->room->objects[5]), 434, 494, 0.1f, false);

                    if (bbAnimTime(e->room->objects[3])>515) {
                        //MoveEntity(e\room\objects[3], 0,-timing\tickDuration*0.001f,0)
                        //MoveEntity(e\room\objects[4], 0,-timing\tickDuration*0.001f,0)
                        //MoveEntity(e\room\objects[5], 0,-timing\tickDuration*0.001f,0)
                        if (bbAnimTime(e->room->objects[3])>533) {
                            e->eventState = 67;
                            e->eventState2 = 0;
                            e->eventState3 = 0;
                            bbHideEntity(e->room->objects[1]);
                        }
                    }
                } break;
                case 67: {
                    if (bbRand(150)==1) {
                        DeathMSG = "The SCP-205 cycle seems to have resumed its normal course after the anomalies observed during ";
                        DeathMSG = DeathMSG + "[REDACTED]. The body of subject D-9341 was discovered inside the chamber. ";
                        DeathMSG = DeathMSG + "The subject exhibits signs of blunt force trauma typical for personnel who have ";
                        DeathMSG = DeathMSG + "entered the chamber when the lights are off.";

                        mainPlayer->injuries = mainPlayer->injuries+bbRnd(0.4f,0.8f);
                        PlaySound2(mainPlayer->damageSFX[bbRand(2,3)]);
                        mainPlayer->camShake = 0.5f;

                        e->eventState2 = bbRnd(-0.1f, 0.1f);
                        e->eventState3 = bbRnd(-0.1f, 0.1f);

                        if (mainPlayer->injuries>5.f) {
                            Kill(mainPlayer);
                        }
                    }

                    bbTranslateEntity(mainPlayer->collider, e->eventState2,0,e->eventState3);
                    e->eventState2 = CurveValue(e->eventState2, 0, 10.f);
                    e->eventState3 = CurveValue(e->eventState3, 0, 10.f);
                } break;
                default: {
                    if (bbRand(3)==1) {
                        bbHideEntity(e->room->objects[1]);
                    } else {
                        bbShowEntity(e->room->objects[1]);
                    }

                    e->eventState3 = e->eventState3 + timing->tickDuration;
                    if (e->eventState3>50) {
                        bbShowEntity(e->room->objects[1]);
                        e->eventState = e->eventState+1;
                        e->eventState3 = 0;
                    }
                } break;
            }
        }
    } else {
        e->eventState = 0;
        e->overwriteMusic = false;
    }
    //[End block]
}

}

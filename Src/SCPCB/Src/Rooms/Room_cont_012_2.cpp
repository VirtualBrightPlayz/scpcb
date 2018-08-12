#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_cont_012_2.h"

namespace CBN {

// Functions.
void FillRoom_cont_012_2(Room* r) {
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

    d = CreateDoor(r->x + 264.f * RoomScale, 0.f, r->z + 672.f * RoomScale, 270, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    bbPositionEntity(d->buttons[0], r->x + 224.f * RoomScale, bbEntityY(d->buttons[0],true), r->z + 880.f * RoomScale, true);
    bbPositionEntity(d->buttons[1], r->x + 304.f * RoomScale, bbEntityY(d->buttons[1],true), r->z + 840.f * RoomScale, true);
    bbTurnEntity(d->buttons[1],0,0,0,true);

    r->doors[0] = CreateDoor(r->x -512.f * RoomScale, -768.f*RoomScale, r->z -336.f * RoomScale, 0, r, false);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = false;
    r->doors[0]->locked = true;
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 176.f * RoomScale, -512.f*RoomScale, r->z - 364.f * RoomScale, true);
    bbFreeEntity(r->doors[0]->buttons[1]);
    r->doors[0]->buttons[1] = 0;

    r->levers[0] = CreateLever();

    bbScaleEntity(r->levers[0]->baseObj, 0.04f, 0.04f, 0.04f);
    bbScaleEntity(r->levers[0]->obj, 0.04f, 0.04f, 0.04f);
    bbPositionEntity(r->levers[0]->baseObj, r->x + 240.f * RoomScale, r->y - 512.f * RoomScale, r->z - 364 * RoomScale, true);
    bbPositionEntity(r->levers[0]->obj, r->x + 240.f * RoomScale, r->y - 512.f * RoomScale, r->z - 364 * RoomScale, true);

    bbEntityParent(r->levers[0]->baseObj, r->obj);
    bbEntityParent(r->levers[0]->obj, r->obj);

    //RotateEntity(r\levers[0]\baseObj, 0, 0, 0)
    bbRotateEntity(r->levers[0]->obj, 10, -180, 0);

    bbEntityPickMode(r->levers[0]->obj, 1, false);
    bbEntityRadius(r->levers[0]->obj, 0.1f);

    r->objects[2] = bbLoadMesh("GFX/Map/room012_2.b3d");
    bbScaleEntity(r->objects[2], RoomScale, RoomScale, RoomScale);
    bbPositionEntity(r->objects[2], r->x - 360 * RoomScale, - 130 * RoomScale, r->z + 456.f * RoomScale, 0);
    bbEntityParent(r->objects[2], r->obj);

    Texture* lightSpriteRed = bbLoadTexture("GFX/Sprites/light_flare_red.jpg", 1);
    r->objects[3] = bbCreateSprite();
    bbPositionEntity(r->objects[3], r->x - 43.5f * RoomScale, - 574 * RoomScale, r->z - 362.f * RoomScale);
    bbScaleSprite((Sprite*)r->objects[3], 0.015f, 0.015f);
    bbEntityTexture((Sprite*)r->objects[3], lightSpriteRed);
    bbEntityBlend((Sprite*)r->objects[3], 3);
    bbEntityParent(r->objects[3], r->obj);
    bbHideEntity(r->objects[3]);
    bbFreeTexture(lightSpriteRed);

    r->objects[4] = bbLoadMesh("GFX/Map/room012_3.b3d");
    Texture* tex = bbLoadTexture("GFX/Map/Rooms/cont_012_2/scp-012_0.jpg");
    bbEntityTexture((MeshModel*)r->objects[4],tex, 0,1);
    bbScaleEntity(r->objects[4], RoomScale, RoomScale, RoomScale);
    bbPositionEntity(r->objects[4], r->x - 360 * RoomScale, - 130 * RoomScale, r->z + 456.f * RoomScale, 0);
    bbEntityParent(r->objects[4], r->objects[2]);

    it = CreatePaper("doc012", r->x - 56.f * RoomScale, r->y - 576.f * RoomScale, r->z - 408.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    de = CreateDecal(DECAL_BLOOD_SPLATTER,  r->x - 784*RoomScale, -768*RoomScale+0.01f, r->z+640*RoomScale,90,bbRnd(360),0);
    de->size = 0.5f;
    bbScaleSprite(de->obj, de->size,de->size);
    bbEntityParent(de->obj, r->obj);
}

void UpdateEvent_cont_012_2(Event* e) {
    float dist;
    int i;
    int temp;
    Pivot* pvt;
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


    if (mainPlayer->currRoom == e->room) {

        if (e->eventState==0) {
            if (bbEntityDistance(mainPlayer->collider, e->room->doors[0]->obj)<2.5f && RemoteDoorOn) {
                //PlaySound2(HorrorSFX(7))
                PlayRangedSound_SM(sndManager->lever,mainPlayer->cam,e->room->doors[0]->obj);
                e->eventState = 1;
                e->room->doors[0]->locked = false;
                UseDoor(e->room->doors[0],false);
                e->room->doors[0]->locked = true;
            }
        } else {

            if (e->sounds[0] == 0) {
                LoadEventSound(e,"SFX/Music/012Golgotha.ogg");
            }
            e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->objects[3], 5.f);

            // TODO: Move to musicmanager.
            //If (e\sounds[1] = 0) Then LoadEventSound(e, "SFX/Music/012.ogg", 1)

            if (e->eventState<90) {
                e->eventState = CurveValue(90,e->eventState,500);
            }
            bbPositionEntity(e->room->objects[2], bbEntityX(e->room->objects[2],true),(-130-448*bbSin(e->eventState))*RoomScale,bbEntityZ(e->room->objects[2],true),true);

            if (e->eventState2 > 0 && e->eventState2 < 200) {
                e->eventState2 = e->eventState2 + timing->tickDuration;
                bbRotateEntity(e->room->levers[0]->obj, CurveValue(85, bbEntityPitch(e->room->levers[0]->obj), 5), bbEntityYaw(e->room->levers[0]->obj), 0);
            } else {
                e->eventState2 = e->eventState2 + timing->tickDuration;
                if (e->eventState2<250) {
                    bbShowEntity(e->room->objects[3]);
                } else {
                    bbHideEntity(e->room->objects[3]);
                    if (e->eventState2>300) {
                        e->eventState2 = 200;
                    }
                }
            }

            if (!IsPlayerWearingItem(mainPlayer,"hazmatsuit3") && !IsPlayerWearingItem(mainPlayer,"gasmask3") && !IsPlayerWearingItem(mainPlayer,"nvgoggles")) {
                temp = false;
                if (bbEntityVisible(e->room->objects[2],mainPlayer->cam)) {
                    temp = true;
                }

                //012 not visible, walk to the door
                if (temp==false) {
                    if (bbEntityVisible(e->room->doors[0]->frameobj,mainPlayer->cam)) {
                        pvt = bbCreatePivot();
                        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->cam));
                        bbPointEntity(pvt, e->room->doors[0]->frameobj);
                        //TurnEntity(pvt, 90, 0, 0)
                        mainPlayer->headPitch = CurveAngle(90, mainPlayer->headPitch+90, 100);
                        mainPlayer->headPitch = mainPlayer->headPitch-90;
                        bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), 150), 0);

                        angle = WrapAngle(bbEntityYaw(pvt)-bbEntityYaw(mainPlayer->collider));
                        if (angle<40.f) {
                            mainPlayer->forceMove = (40.f-angle)*0.008f;
                        } else if ((angle > 310.f)) {
                            mainPlayer->forceMove = (40.f-abs(360.f-angle))*0.008f;
                        }

                        bbFreeEntity(pvt);
                    }
                } else {
                    e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, e->room->objects[3], 10, e->eventState3/(86.f*70.f));

                    pvt = bbCreatePivot();
                    bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(e->room->objects[2],true)-0.05f, bbEntityZ(mainPlayer->cam));
                    bbPointEntity(pvt, e->room->objects[2]);
                    bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), 80-(e->eventState3/200.f)), 0);

                    bbTurnEntity(pvt, 90, 0, 0);
                    mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt)+25, mainPlayer->headPitch + 90.f, 80-(e->eventState3/200.f));
                    mainPlayer->headPitch = mainPlayer->headPitch-90;

                    dist = Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[2],true),bbEntityZ(e->room->objects[2],true));

                    mainPlayer->heartbeatIntensity = 150;
                    //HeartBeatVolume = Max(3.f-dist,0.f)/3.f
                    mainPlayer->blurTimer = Max((2.f-dist)*(e->eventState3/800.f)*(bbSin((float)(TimeInPosMilliSecs()) / 20.f + 1.f)),mainPlayer->blurTimer);
                    mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs()) / 20.f)+1.f)*8.f*Max((3.f-dist),0.f));

                    if (mainPlayer->breathChn != 0) {
                        if (bbChannelPlaying(mainPlayer->breathChn)) {
                            bbStopChannel(mainPlayer->breathChn);
                        }
                    }

                    if (dist < 0.6f) {
                        e->eventState3 = Min(e->eventState3+timing->tickDuration,86*70);
                        if (e->eventState3>70 && e->eventState3-timing->tickDuration<=70) {
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech1.ogg"));
                        } else if ((e->eventState3>13*70 && e->eventState3-timing->tickDuration<=13*70)) {
                            Msg = "You start pushing your nails into your wrist, drawing blood.";
                            MsgTimer = 7*70;
                            mainPlayer->injuries = mainPlayer->injuries+0.5f;
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech2.ogg"));
                        } else if ((e->eventState3>31*70 && e->eventState3-timing->tickDuration<=31*70)) {
                            tex = bbLoadTexture("GFX/Map/Textures/scp-012_1.jpg");
                            bbEntityTexture((MeshModel*)e->room->objects[4], tex,0,1);
                            bbFreeTexture(tex);

                            Msg = "You tear open your left wrist and start writing on the composition with your blood.";
                            MsgTimer = 7*70;
                            mainPlayer->injuries = Max(mainPlayer->injuries,1.5f);
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech"+String(bbRand(3,4))+".ogg"));
                        } else if ((e->eventState3>49*70 && e->eventState3-timing->tickDuration<=49*70)) {
                            Msg = "You push your fingers deeper into the wound.";
                            MsgTimer = 8*70;
                            mainPlayer->injuries = mainPlayer->injuries+0.3f;
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech5.ogg"));
                        } else if ((e->eventState3>63*70 && e->eventState3-timing->tickDuration<=63*70)) {
                            tex = bbLoadTexture("GFX/Map/Textures/scp-012_2.jpg");
                            bbEntityTexture((MeshModel*)e->room->objects[4], tex,0,1);
                            bbFreeTexture(tex);

                            mainPlayer->injuries = mainPlayer->injuries+0.5f;
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech6.ogg"));
                        } else if ((e->eventState3>74*70 && e->eventState3-timing->tickDuration<=74*70)) {
                            tex = bbLoadTexture("GFX/Map/Textures/scp-012_3.jpg");
                            bbEntityTexture((MeshModel*)e->room->objects[4], tex,0,1);
                            bbFreeTexture(tex);

                            Msg = "You rip the wound wide open. Grabbing scoops of blood pouring out.";
                            MsgTimer = 7*70;
                            mainPlayer->injuries = mainPlayer->injuries+0.8f;
                            PlaySound2(LoadTempSound("SFX/SCP/012/Speech7.ogg"));
                            mainPlayer->crouching = true;

                            de = CreateDecal(DECAL_BLOOD_POOL,  bbEntityX(mainPlayer->collider), -768*RoomScale+0.01f, bbEntityZ(mainPlayer->collider),90,bbRnd(360),0);
                            de->size = 0.1f;
                            de->maxSize = 0.45f;
                            de->sizeChange = 0.0002f;
                            UpdateDecals();
                        } else if ((e->eventState3>85*70 && e->eventState3-timing->tickDuration<=85*70)) {
                            DeathMSG = "Subject D-9341 found in a pool of blood next to SCP-012. Subject seems to have ripped open his wrists and written three extra ";
                            DeathMSG = DeathMSG + "lines to the composition before dying of blood loss.";
                            Kill(mainPlayer);
                        }

                        bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(mainPlayer->collider)+bbSin(e->eventState3*(e->eventState3/2000))*(e->eventState3/300), bbEntityYaw(mainPlayer->collider), 80), 0);

                    } else {
                        angle = WrapAngle(bbEntityYaw(pvt)-bbEntityYaw(mainPlayer->collider));
                        if (angle<40.f) {
                            mainPlayer->forceMove = (40.f-angle)*0.02f;
                        } else if ((angle > 310.f)) {
                            mainPlayer->forceMove = (40.f-abs(360.f-angle))*0.02f;
                        }
                    }

                    bbFreeEntity(pvt);
                }

            }

        }
    }

}

}

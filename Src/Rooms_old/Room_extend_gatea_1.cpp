#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Skybox.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Menus/LoadingScreen.h"
#include "Room_extend_gatea_1.h"

namespace CBN {

// Functions.
void FillRoom_extend_gatea_1(Room* r) {
    r->doors[2] = CreateDoor(r->x - 4064.f * RoomScale, (-1280.f+12000.f)*RoomScale, r->z + 3952.f * RoomScale, 0, r, false);
    r->doors[2]->autoClose = false;
    r->doors[2]->open = false;

    Door* d2 = CreateDoor(r->x, 12000.f*RoomScale, r->z - 1024.f * RoomScale, 0, r, false);
    d2->autoClose = false;
    d2->open = false;
    d2->locked = true;

    d2 = CreateDoor(r->x-1440*RoomScale, (12000.f-480.f)*RoomScale, r->z + 2328.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    //TODO: fix
    //If (SelectedEnding = "A2") Then
    //    d2\autoClose = False
    d2->open = true;
    d2->locked = true;
    //Else
    //    d2\autoClose = False
    d2->open = false;
    d2->locked = false;
    //EndIf
    bbPositionEntity(d2->buttons[0], r->x-1320.f*RoomScale, bbEntityY(d2->buttons[0],true), r->z + 2288.f*RoomScale, true);
    bbPositionEntity(d2->buttons[1], r->x-1584*RoomScale, bbEntityY(d2->buttons[0],true), r->z + 2488.f*RoomScale, true);
    bbRotateEntity(d2->buttons[1], 0, 90, 0, true);

    d2 = CreateDoor(r->x-1440*RoomScale, (12000.f-480.f)*RoomScale, r->z + 4352.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    //TODO: fix
    //If (SelectedEnding = "A2") Then
    //    d2\autoClose = False
    d2->open = true;
    d2->locked = true;
    //Else
    //    d2\autoClose = False
    d2->open = false;
    d2->locked = false;
    //EndIf
    bbPositionEntity(d2->buttons[0], r->x-1320.f*RoomScale, bbEntityY(d2->buttons[0],true), r->z + 4384.f*RoomScale, true);
    bbRotateEntity(d2->buttons[0], 0, 180, 0, true);
    bbPositionEntity(d2->buttons[1], r->x-1584.f*RoomScale, bbEntityY(d2->buttons[0],true), r->z + 4232.f*RoomScale, true);
    bbRotateEntity(d2->buttons[1], 0, 90, 0, true);

    for (int iterator179 = 0; iterator179 < Room::getListSize(); iterator179++) {
        Room* r2 = Room::getObject(iterator179);

        if (r2->roomTemplate->name.equals("exit1")) {
            r->objects[1] = r2->objects[1];
            r->objects[2] = r2->objects[2];
        } else if ((r2->roomTemplate->name.equals("gateaentrance"))) {
            //ylempi hissi
            r->doors[1] = CreateDoor(r->x+1544.f*RoomScale,12000.f*RoomScale, r->z-64.f*RoomScale, 90, r, false);
            r->doors[1]->autoClose = false;
            r->doors[1]->open = false;
            bbPositionEntity(r->doors[1]->buttons[0],r->x+1584*RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z+80*RoomScale, true);
            bbPositionEntity(r->doors[1]->buttons[1],r->x+1456*RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z-208*RoomScale, true);
            r2->objects[1] = bbCreatePivot();
            bbPositionEntity(r2->objects[1], r->x+1848.f*RoomScale, 240.f*RoomScale, r->z-64.f*RoomScale, true);
            bbEntityParent(r2->objects[1], r->obj);
        }
    }

    //106:n spawnpoint
    r->objects[3] = bbCreatePivot();
    bbPositionEntity(r->objects[3], r->x+1216.f*RoomScale, 0, r->z+2112.f*RoomScale, true);
    bbEntityParent(r->objects[3], r->obj);

    //sillan loppup��
    r->objects[4] = bbCreatePivot();
    bbPositionEntity(r->objects[4], r->x, 96.f*RoomScale, r->z+6400.f*RoomScale, true);
    bbEntityParent(r->objects[4], r->obj);

    //vartiotorni 1
    r->objects[5] = bbCreatePivot();
    bbPositionEntity(r->objects[5], r->x+1784.f*RoomScale, 2124.f*RoomScale, r->z+4512.f*RoomScale, true);
    bbEntityParent(r->objects[5], r->obj);

    //vartiotorni 2
    r->objects[6] = bbCreatePivot();
    bbPositionEntity(r->objects[6], r->x-5048.f*RoomScale, 1912.f*RoomScale, r->z+4656.f*RoomScale, true);
    bbEntityParent(r->objects[6], r->obj);

    //sillan takareuna
    r->objects[7] = bbCreatePivot();
    bbPositionEntity(r->objects[7], r->x+1824.f*RoomScale, 224.f*RoomScale, r->z+7056.f*RoomScale, true);
    bbEntityParent(r->objects[7], r->obj);

    //sillan takareuna2
    r->objects[8] = bbCreatePivot();
    bbPositionEntity(r->objects[8], r->x-1824.f*RoomScale, 224.f*RoomScale, r->z+7056.f*RoomScale, true);
    bbEntityParent(r->objects[8], r->obj);

    //"valopyssy"
    r->objects[9] = bbCreatePivot();
    bbPositionEntity(r->objects[9], r->x+2624.f*RoomScale, 992.f*RoomScale, r->z+6157.f*RoomScale, true);
    bbEntityParent(r->objects[9], r->obj);
    //objects[10] = valopyssyn yl�osa

    //tunnelin loppu
    r->objects[11] = bbCreatePivot();
    bbPositionEntity(r->objects[11], r->x-4064.f*RoomScale, -1248.f*RoomScale, r->z-1696.f*RoomScale, true);
    bbEntityParent(r->objects[11], r->obj);

    r->objects[13] = bbLoadMesh("GFX/Map/gateawall1.b3d",r->obj);
    bbPositionEntity(r->objects[13], r->x-4308.f*RoomScale, -1045.f*RoomScale, r->z+544.f*RoomScale, true);
    bbEntityColor((MeshModel*)r->objects[13], 25,25,25);
    //EntityFX(r\objects[13],1)

    r->objects[14] = bbLoadMesh("GFX/Map/gateawall2.b3d",r->obj);
    bbPositionEntity(r->objects[14], r->x-3820.f*RoomScale, -1045.f*RoomScale, r->z+544.f*RoomScale, true);
    bbEntityColor((MeshModel*)r->objects[14], 25,25,25);
    //EntityFX(r\objects[14],1)

    r->objects[15] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[15], r->x-3568.f*RoomScale, -1089.f*RoomScale, r->z+4944.f*RoomScale, true);
}

void UpdateEvent_extend_gatea_1(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        for (int iterator180 = 0; iterator180 < Room::getListSize(); iterator180++) {
            Room* r = Room::getObject(iterator180);

            bbHideEntity(r->obj);
        }
        bbShowEntity(e->room->obj);

        if (e->eventState == 0) {
            DrawLoading(0);
            e->room->objects[0] = bbLoadMesh("GFX/MAP/gateatunnel.b3d");
            bbPositionEntity(e->room->objects[0], bbEntityX(e->room->obj,true),bbEntityY(e->room->obj,true),bbEntityZ(e->room->obj,true));
            bbScaleEntity(e->room->objects[0],RoomScale,RoomScale,RoomScale);
            bbEntityType(e->room->objects[0], HIT_MAP);
            bbEntityPickMode(e->room->objects[0], 3);
            bbEntityParent(e->room->objects[0],e->room->obj);

            DrawLoading(30);

            // TODO: Re-implement whatever the fuck this is.
            //For i = 0 To e\room\maxLights
            //	If (e\room\lightSprites[i]<>0) Then
            //		EntityFX(e\room\lightSprites[i], 1+8)
            //	EndIf
            //Next

            bbCameraFogMode(mainPlayer->cam, 0);
            SecondaryLightOn = true;

            HideDistance = 35.f;

            for (int i = 2; i <= 4; i++) {
                e->room->npc[i] = CreateNPC(NPCtypeApache, e->room->x, 100.f, e->room->z);
                e->room->npc[i]->state = (!Contained106);
            }

            //Music(5) = LoadSound("SFX/Music/GateA.ogg") ;TODO: fix

            bbTranslateEntity(e->room->obj, 0,12000.f*RoomScale,0);
            bbTranslateEntity(mainPlayer->collider, 0,12000.f*RoomScale,0);

            Sky = sky_CreateSky("GFX/Map/sky.jpg");
            bbRotateEntity(Sky,0,e->room->angle,0);

            DrawLoading(60);

            for (int iterator181 = 0; iterator181 < NPC::getListSize(); iterator181++) {
                NPC* n = NPC::getObject(iterator181);

                if (n->npcType == NPCtypeMTF) {
                    delete n;
                }
            }

            for (int i = 0; i < 2; i++) {
                e->room->npc[i] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->objects[i+5],true),bbEntityY(e->room->objects[i+5],true),bbEntityZ(e->room->objects[i+5],true));
                e->room->npc[i]->state = 0;
                bbPointEntity(e->room->npc[i]->collider, e->room->objects[3]);
            }

            for (int i = 7; i <= 8; i++) {
                e->room->npc[i] = CreateNPC(NPCtypeMTF, bbEntityX(e->room->objects[i],true)+0.8f,bbEntityY(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true)+0.8f);
                e->room->npc[i]->state = 5;
                e->room->npc[i]->prevState = 1;
                bbPointEntity(e->room->npc[i]->collider, e->room->objects[3]);
            }

            for (int i = 5; i <= 6; i++) {
                e->room->npc[i] = CreateNPC(NPCtypeMTF, bbEntityX(e->room->objects[i+2],true),bbEntityY(e->room->objects[i+2],true),bbEntityZ(e->room->objects[i+2],true));
                e->room->npc[i]->state = 5;
                e->room->npc[i]->prevState = 1;
                bbPointEntity(e->room->npc[i]->collider, e->room->objects[3]);
            }

            if (Contained106) {
                e->room->doors[2]->locked = true;

                bbPositionEntity(e->room->npc[5]->collider, bbEntityX(e->room->objects[15],true),bbEntityY(e->room->objects[15],true),bbEntityZ(e->room->objects[15],true), true);
                bbResetEntity(e->room->npc[5]->collider);

            }

            float xtemp = bbEntityX(e->room->objects[9],true);
            float ztemp = bbEntityZ(e->room->objects[9],true);
            bbFreeEntity(e->room->objects[9]);

            e->room->objects[9] = bbLoadMesh("GFX/Map/lightgunbase.b3d");
            bbScaleEntity(e->room->objects[9], RoomScale,RoomScale,RoomScale);
            bbEntityFX((MeshModel*)e->room->objects[9],0);
            bbPositionEntity(e->room->objects[9], xtemp, (992.f+12000.f)*RoomScale, ztemp);
            e->room->objects[10] = bbLoadMesh("GFX/Map/lightgun.b3d");
            bbEntityFX((MeshModel*)e->room->objects[10],0);
            bbScaleEntity(e->room->objects[10], RoomScale,RoomScale,RoomScale);
            bbPositionEntity(e->room->objects[10], xtemp, (992.f+12000.f+288.f)*RoomScale, ztemp-176.f*RoomScale,true);
            bbEntityParent(e->room->objects[10],e->room->objects[9]);
            bbRotateEntity(e->room->objects[9], 0, 48, 0);
            bbRotateEntity(e->room->objects[10], 40, 0, 0);

            for (int temp = 0; temp <= 20; temp++) {
                for (int i = 0; i < 2; i++) {
                    bbTranslateEntity(e->room->npc[i]->collider, 0, -0.04f, 0);
                }
                for (int i = 5; i <= 8; i++) {
                    bbTranslateEntity(e->room->npc[i]->collider, 0, -0.04f, 0);
                }
            }

            bbResetEntity(mainPlayer->collider);
            e->eventState = 1.f;

            if (!Contained106) {
                PlaySound2(LoadTempSound("SFX/Ending/GateA/106Escape.ogg"));
            }

            DrawLoading(100);
        } else {
            //TODO
            //ShouldPlay = 5

            e->eventState = e->eventState+timing->tickDuration;
            bbHideEntity(mainPlayer->overlays[OVERLAY_FOG]);
            bbCameraFogRange(mainPlayer->cam, 5,30);

            float angle = Max(bbSin(bbEntityYaw(mainPlayer->collider)+90),0.f);
            //240,220,200
            bbCameraFogColor(mainPlayer->cam,200+(angle*40),200+(angle*20),200);
            bbCameraClsColor(mainPlayer->cam,200+(angle*40),200+(angle*20),200);
            bbCameraRange(mainPlayer->cam, 0.05f, 30);

            bbAmbientLight(140, 140, 140);

            for (int i = 2; i <= 4; i++) {
                if (e->room->npc[i]!=nullptr) {
                    if (e->room->npc[i]->state < 2) {
                        bbPositionEntity(e->room->npc[i]->collider, bbEntityX(e->room->objects[3],true)+bbCos(e->eventState/10+(120*i))*6000.f*RoomScale,15000*RoomScale,bbEntityZ(e->room->objects[3],true)+bbSin(e->eventState/10+(120*i))*6000.f*RoomScale);
                        bbRotateEntity(e->room->npc[i]->collider,7.f,(e->eventState/10+(120*i)),20.f);
                    }
                }
            }

            UpdateSky();

            if (e->eventState>=350) {
                if (Contained106==false) {
                    if (e->eventState-timing->tickDuration < 350) {
                        Curr106->state = -0.1f;
                        //Curr106\idle = True
                        SetNPCFrame(Curr106, 110.f);
                        bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[3],true),bbEntityY(mainPlayer->collider)-50.f,bbEntityZ(e->room->objects[3],true),true);
                        bbPositionEntity(Curr106->obj, bbEntityX(e->room->objects[3],true),bbEntityY(mainPlayer->collider)-50.f,bbEntityZ(e->room->objects[3],true),true);
                        Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[3],true),bbEntityY(e->room->objects[3],true)+0.01f,bbEntityZ(e->room->objects[3],true), 90, bbRand(360), 0);
                        de->size = 0.05f;
                        de->sizeChange = 0.001f;
                        bbEntityAlpha(de->obj, 0.8f);
                        UpdateDecals();
                        //TODO: fix audio
                        //PlaySound2((HorrorSFX(5)))
                        //PlaySound2(DecaySFX(0))
                    } else if ((Curr106->state < 0)) {
                        bbHideEntity(Curr106->obj2);
                        Curr106->pathTimer = 70*100;

                        if (Curr106->state3 == 0) {
                            if (Curr106->pathStatus != 1) {
                                bbPositionEntity(Curr106->collider,bbEntityX(e->room->objects[3],true),bbEntityY(Curr106->collider),bbEntityZ(e->room->objects[3],true),true);
                                if (Curr106->state <= -10) {
                                    float dist = bbEntityY(Curr106->collider);
                                    bbPositionEntity(Curr106->collider,bbEntityX(Curr106->collider),bbEntityY(e->room->objects[3],true),bbEntityZ(Curr106->collider),true);
                                    //Curr106\pathStatus = FindPath(Curr106, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True),EntityZ(e\room\objects[4],True))
                                    Curr106->pathStatus = FindPath(Curr106,bbEntityX(e->room->npc[5]->collider,true),bbEntityY(e->room->npc[5]->collider,true),bbEntityZ(e->room->npc[5]->collider,true));
                                    Curr106->pathTimer = 70*200;
                                    bbPositionEntity(Curr106->collider,bbEntityX(Curr106->collider),dist,bbEntityZ(Curr106->collider),true);
                                    bbResetEntity(Curr106->collider);
                                    Curr106->pathLocation = 1;
                                    //Curr106\idle = False
                                    //Else
                                    //PositionEntity(Curr106\collider, EntityX(e\room\objects[3],True),EntityY(e\room\objects[3],True),EntityZ(e\room\objects[3],True),True)
                                    //Curr106\idle = True
                                    //Animate2(Curr106\obj, AnimTime(Curr106\obj), 110, 259, 0.15f, False)
                                    //If (AnimTime(Curr106\obj)=>259) Then Curr106\idle = False

                                }
                            } else {
                                Curr106->pathTimer = 70*200;
                                //helicopters start attacking 106
                                for (int i = 2; i <= 4; i++) {
                                    e->room->npc[i]->state = 3;
                                    e->room->npc[i]->enemyX = bbEntityX(Curr106->obj,true);
                                    e->room->npc[i]->enemyY = bbEntityY(Curr106->obj,true)+5.f;
                                    e->room->npc[i]->enemyZ = bbEntityZ(Curr106->obj,true);
                                }

                                for (int i = 5; i <= 8; i++) {
                                    e->room->npc[i]->state = 5;
                                    e->room->npc[i]->enemyX = bbEntityX(Curr106->obj,true);
                                    e->room->npc[i]->enemyY = bbEntityY(Curr106->obj,true)+0.4f;
                                    e->room->npc[i]->enemyZ = bbEntityZ(Curr106->obj,true);
                                }

                                Pivot* pvt = bbCreatePivot();
                                bbPositionEntity(pvt, bbEntityX(e->room->objects[10],true),bbEntityY(e->room->objects[10],true),bbEntityZ(e->room->objects[10],true));
                                bbPointEntity(pvt, Curr106->collider);
                                bbRotateEntity(e->room->objects[9],0,CurveAngle(bbEntityYaw(pvt),bbEntityYaw(e->room->objects[9],true),150.f),0,true);
                                bbRotateEntity(e->room->objects[10],CurveAngle(bbEntityPitch(pvt),bbEntityPitch(e->room->objects[10],true),200.f),bbEntityYaw(e->room->objects[9],true),0, true);

                                bbFreeEntity(pvt);

                                //decals under 106
                                if (timing->tickDuration > 0) {
                                    if (modFloat(e->eventState-timing->tickDuration, 100.f)<=50.f && modFloat(e->eventState, 100.f)>50.f) {
                                        Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(Curr106->collider,true),bbEntityY(e->room->objects[3],true)+0.01f,bbEntityZ(Curr106->collider,true), 90, bbRand(360), 0);
                                        de->size = 0.2f;
                                        de->sizeChange = 0.004f;
                                        de->timer = 90000;
                                        bbEntityAlpha(de->obj, 0.8f);
                                        UpdateDecals();
                                    }
                                }
                            }
                        }

                        float dist = Distance(bbEntityX(Curr106->collider),bbEntityZ(Curr106->collider),bbEntityX(e->room->objects[4],true),bbEntityZ(e->room->objects[4],true));

                        Curr106->currSpeed = CurveValue(0, Curr106->currSpeed, Max(5*dist,2.f));
                        if (dist < 15.f) {
                            if (e->soundChannels[1] == 0) {
                                e->soundChannels[1] = bbPlaySound(LoadTempSound("SFX/Ending/GateA/Franklin.ogg"));
                            }

                            if (dist<0.4f) {
                                Curr106->pathStatus = 0;
                                Curr106->pathTimer = 70*200;
                                if (Curr106->state3==0) {
                                    SetNPCFrame(Curr106, 259.f);
                                    if (e->sounds[0] != 0) {
                                        bbFreeSound(e->sounds[0]);
                                        e->sounds[0] = 0;
                                    }
                                    LoadEventSound(e,"SFX/Ending/GateA/106Retreat.ogg");
                                    e->soundChannels[0] = PlayRangedSound(e->sounds[0], mainPlayer->cam, Curr106->collider, 35.f);
                                }

                                //106:Decals are displayed below
                                if (timing->tickDuration > 0) {
                                    if (modFloat(e->eventState-timing->tickDuration, 160.f)<=50.f && modFloat(e->eventState, 160.f)>50.f) {
                                        Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(Curr106->collider,true),bbEntityY(e->room->objects[3],true)+0.01f,bbEntityZ(Curr106->collider,true), 90, bbRand(360), 0);
                                        de->size = 0.05f;
                                        de->sizeChange = 0.004f;
                                        de->timer = 90000;
                                        bbEntityAlpha(de->obj, 0.8f);
                                        UpdateDecals();
                                    }
                                }

                                AnimateNPC(Curr106, 259, 110, -0.1f, false);

                                Curr106->state3 = Curr106->state3+timing->tickDuration;
                                bbPositionEntity(Curr106->collider, bbEntityX(Curr106->collider,true),CurveValue(bbEntityY(e->room->objects[3],true)-(Curr106->state3/4500.f),bbEntityY(Curr106->collider,true),100.f),bbEntityZ(Curr106->collider,true));
                                if (Curr106->state3>700.f) {
                                    Curr106->state = 100000;
                                    e->eventState2 = 0;
                                    for (int i = 5; i <= 8; i++) {
                                        e->room->npc[i]->state = 1;
                                    }
                                    //helicopters attack the player
                                    for (int i = 2; i <= 4; i++) {
                                        e->room->npc[i]->state = 2;
                                    }
                                    bbHideEntity(Curr106->obj);
                                }
                            } else {
                                if (dist < 8.5f) {
                                    //bbChannelPlaying(e\soundChannels[1]) = 0) Then
                                    if (e->eventState2==0) {
                                        e->soundChannels[1] = bbPlaySound(LoadTempSound("SFX/Ending/GateA/HIDTurret.ogg"));
                                        e->eventState2 = 1;
                                    } else if ((e->eventState2>0)) {
                                        e->eventState2 = e->eventState2+timing->tickDuration;
                                        if (e->eventState2>= 7.5f*70) {
                                            if (e->eventState2-timing->tickDuration < 7.5f*70) {
                                                //TODO: Not use the sun sprite here.
                                                Particle* p = CreateParticle(bbEntityX(Curr106->obj,true),bbEntityY(Curr106->obj,true)+0.4f, bbEntityZ(Curr106->obj,true), PARTICLE_SUN, 7.f, 0, (int)(6.7f*70));
                                                p->speed = 0.f;
                                                p->a = 1.f;
                                                bbEntityParent(p->pvt, Curr106->collider, true);

                                                p = CreateParticle(bbEntityX(e->room->objects[10],true),bbEntityY(e->room->objects[10],true),bbEntityZ(e->room->objects[10],true), PARTICLE_SUN, 2.f, 0, (int)(6.7f*70));
                                                bbRotateEntity(p->pvt, bbEntityPitch(e->room->objects[10],true),bbEntityYaw(e->room->objects[10],true),0,true);
                                                bbMoveEntity(p->pvt, 0, 92.f*RoomScale, 512.f*RoomScale);
                                                p->speed = 0.f;
                                                p->a = 1.f;
                                                bbEntityParent(p->pvt, e->room->objects[10], true);
                                            } else if (e->eventState2 < 14.3f*70) {
                                                mainPlayer->camShake = 0.5f;
                                                mainPlayer->lightFlash = 0.3f+bbEntityInView(e->room->objects[10],mainPlayer->cam)*0.5f;
                                            }
                                        }
                                    }

                                    for (int i = 0; i <= bbRand(2,8)-(int)(dist); i++) {
                                        Particle* p = CreateParticle(bbEntityX(Curr106->obj,true),bbEntityY(Curr106->obj,true)+bbRnd(0.4f,0.9f), bbEntityZ(Curr106->obj), PARTICLE_SMOKE_BLACK, 0.006f, -0.002f, 40);
                                        p->speed = 0.005f;
                                        p->a = 0.8f;
                                        p->aChange = -0.01f;
                                        bbRotateEntity(p->pvt, -bbRnd(70,110), bbRnd(360),0);
                                    }
                                }


                            }
                        }
                    }

                    if (e->eventState3 == 0.f) {
                        if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(e->room->objects[11],true))<1.f) {
                            if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[11],true),bbEntityZ(e->room->objects[11],true)) < 12.f) {
                                Curr106->state = 100000;
                                bbHideEntity(Curr106->obj);

                                //MTF spawns at the tunnel entrance
                                for (int i = 5; i <= 8; i++) {
                                    e->room->npc[i]->state = 3;
                                    bbPositionEntity(e->room->npc[i]->collider, bbEntityX(e->room->objects[15],true)+(i-6)*0.3f,bbEntityY(e->room->objects[15],true),bbEntityZ(e->room->objects[15],true)+(i-6)*0.3f, true);
                                    bbResetEntity(e->room->npc[i]->collider);

                                    e->room->npc[i]->pathStatus = FindPath(e->room->npc[i], bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
                                    e->room->npc[i]->pathTimer = 70*2;
                                    e->room->npc[i]->lastSeen = 70*100;
                                }
                                e->room->npc[5]->sounds[0] = bbLoadSound("SFX/Character/MTF/ThereHeIs1.ogg");
                                PlayRangedSound(e->room->npc[5]->sounds[0], mainPlayer->cam, e->room->npc[5]->collider, 25.f);

                                e->room->doors[2]->open = true;

                                for (int i = 2; i <= 4; i++) {
                                    delete e->room->npc[i];
                                    e->room->npc[i] = nullptr;
                                }

                                e->eventState3 = 1.f;
                            }
                        }
                    } else if (e->eventState3 == 1.f) {

                        for (int i = 5; i <= 8; i++) {
                            if (bbEntityDistance(e->room->npc[i]->collider,mainPlayer->collider)> 4.f) {
                                e->room->npc[i]->state = 3;
                            }
                        }

                        if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(e->room->objects[11],true))<1.f) {
                            if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[11],true),bbEntityZ(e->room->objects[11],true)) < 7.f) {
                                e->room->objects[12] = bbLoadMesh("GFX/npcs/CI/CI.b3d");
                                bbEntityColor((MeshModel*)e->room->objects[12], 0,0,0);
                                bbScaleMesh((MeshModel*)e->room->objects[12], 0.32f/21.3f, 0.32f/21.3f, 0.32f/21.3f);
                                bbPositionEntity(e->room->objects[12], bbEntityX(e->room->objects[11],true), bbEntityY(e->room->objects[11],true), bbEntityZ(e->room->objects[11],true));

                                // TODO: Isn't this a memory leak?
                                MeshModel* obj = bbCopyMeshModelEntity((MeshModel*)e->room->objects[12]);
                                bbPositionEntity(obj, bbEntityX(e->room->obj,true)-3968*RoomScale, bbEntityY(e->room->objects[11],true), bbEntityZ(e->room->obj,true)-1920*RoomScale);

                                obj = bbCopyMeshModelEntity((MeshModel*)e->room->objects[12]);
                                bbPositionEntity(obj, bbEntityX(e->room->obj,true)-4160*RoomScale, bbEntityY(e->room->objects[11],true), bbEntityZ(e->room->obj,true)-1920*RoomScale);

                                obj = bbCopyMeshModelEntity((MeshModel*)e->room->objects[12]);
                                bbPositionEntity(obj, bbEntityX(e->room->obj,true)-4064*RoomScale, bbEntityY(e->room->objects[11],true), bbEntityZ(e->room->obj,true)-2112*RoomScale);

                                e->soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/Ending/GateA/Bell1.ogg"), mainPlayer->cam, e->room->objects[12]);

                                //TODO: Not the sun sprite.
                                Particle* p = CreateParticle(bbEntityX(e->room->objects[11],true),bbEntityY(mainPlayer->cam,true), bbEntityZ(e->room->objects[11],true), PARTICLE_SUN, 8.f, 0, 50);
                                p->speed = 0.15f;
                                p->a = 0.5f;
                                p = CreateParticle(bbEntityX(e->room->objects[11],true),bbEntityY(mainPlayer->cam,true), bbEntityZ(e->room->objects[11],true), PARTICLE_SUN, 8.f, 0, 50);
                                p->speed = 0.25f;
                                p->a = 0.5f;
                                bbPointEntity(p->pvt, mainPlayer->collider);

                                mainPlayer->camShake = 1.f;
                                mainPlayer->lightFlash = 1.f;

                                e->eventState3 = 2.f;
                            }
                        }
                    } else {
                        e->eventState3 = e->eventState3+timing->tickDuration;
                        bbPointEntity(e->room->objects[12], mainPlayer->collider);
                        bbRotateEntity(e->room->objects[12], 0, bbEntityYaw(e->room->objects[12]), 0);

                        mainPlayer->stamina = -5.f;

                        mainPlayer->blurTimer = bbSin(e->eventState3*0.7f)*1000.f;

                        if (!mainPlayer->dead) {
                            bbCameraZoom(mainPlayer->cam, 1.f+bbSin(e->eventState3*0.8f)*0.2f);

                            float dist = bbEntityDistance(mainPlayer->collider,e->room->objects[11]);
                            if (dist < 6.5f) {
                                bbPositionEntity(mainPlayer->collider, CurveValue(bbEntityX(e->room->objects[11],true),bbEntityX(mainPlayer->collider),dist*80),bbEntityY(mainPlayer->collider),CurveValue(bbEntityZ(e->room->objects[0],true),bbEntityZ(mainPlayer->collider),dist*80));
                            }
                        }

                        //tunneli menee umpeen
                        if (e->eventState3>50 && e->eventState3<230) {
                            mainPlayer->camShake = bbSin(e->eventState3-50)*3;
                            bbTurnEntity(e->room->objects[13], 0, bbSin(e->eventState3-50)*-0.85f, 0, true);
                            bbTurnEntity(e->room->objects[14], 0, bbSin(e->eventState3-50)*0.85f, 0, true);

                            for (int i = 5; i <= 8; i++) {
                                bbPositionEntity(e->room->npc[i]->collider, CurveValue(bbEntityX(e->room->doors[2]->frameobj,true), bbEntityX(e->room->npc[i]->collider,true),50.f),bbEntityY(e->room->npc[i]->collider,true),CurveValue(bbEntityZ(e->room->doors[2]->frameobj,true), bbEntityZ(e->room->npc[i]->collider,true),50.f),true);
                                bbResetEntity(e->room->npc[i]->collider);
                            }
                        }

                        if (e->eventState3>=230.f) {
                            if (e->eventState3-timing->tickDuration<230.f) {
                                e->soundChannels[0] = bbPlaySound(LoadTempSound("SFX/Ending/GateA/CI.ogg"));
                            }

                            // And SelectedEnding="" Then
                            if (bbChannelPlaying(e->soundChannels[0])==false) {
                                PlaySound2(LoadTempSound("SFX/Ending/GateA/Bell2.ogg"));

                                Particle* p = CreateParticle(bbEntityX(e->room->objects[11],true),bbEntityY(mainPlayer->cam,true), bbEntityZ(e->room->objects[11],true), PARTICLE_SUN, 8.f, 0, 50);
                                p->speed = 0.15f;
                                p->a = 0.5f;
                                p = CreateParticle(bbEntityX(e->room->objects[11],true),bbEntityY(mainPlayer->cam,true), bbEntityZ(e->room->objects[11],true), PARTICLE_SUN, 8.f, 0, 50);
                                p->speed = 0.25f;
                                p->a = 0.5f;

                                //SelectedEnding = "A1"
                                mainPlayer->godMode = 0;
                                mainPlayer->noclip = 0;
                                DeathMSG = "";
                                Kill(mainPlayer);
                            }

                            //If (SelectedEnding <> "") Then
                            mainPlayer->camShake = CurveValue(2.f,mainPlayer->camShake,10.f);
                            mainPlayer->lightFlash = CurveValue(2.f,mainPlayer->lightFlash,8.f);
                            //EndIf

                        }
                    }

                    //contained106 = true
                } else {

                    if (e->eventState2 == 0) {
                        //PositionEntity(e\room\npc[5]\collider, EntityX(e\room\obj,True)-3408*RoomScale, EntityY(e\room\obj,True)-796*RoomScale, EntityZ(e\room\obj,True)+4976, True)
                        //ResetEntity(e\room\npc[5]\collider)
                        e->eventState2 = 1;

                        for (int i = 5; i <= 8; i++) {
                            e->room->npc[i]->state = 3;

                            e->room->npc[i]->pathStatus = FindPath(e->room->npc[i], bbEntityX(e->room->obj)-1.f+2.f*(i % 2),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(e->room->obj)-2.f*(i % 2));
                            e->room->npc[i]->pathTimer = 70*bbRand(15,20);
                            e->room->npc[i]->lastSeen = 70*300;
                        }
                    } else {

                        for (int i = 5; i <= 8; i++) {
                            if (e->room->npc[i]->state == 5) {
                                e->room->npc[i]->enemyX = bbEntityX(mainPlayer->collider);
                                e->room->npc[i]->enemyY = bbEntityY(mainPlayer->collider);
                                e->room->npc[i]->enemyZ = bbEntityZ(mainPlayer->collider);
                            } else {
                                if (bbEntityDistance(e->room->npc[i]->collider,mainPlayer->collider)<6.f) {
                                    e->room->npc[i]->state = 5;
                                    e->room->npc[i]->currSpeed = 0;
                                }
                            }
                        }

                        if (e->eventState2<=1) {
                            for (int i = 5; i <= 8; i++) {
                                if (e->room->npc[i]->state == 5) {
                                    for (int temp = 5; temp <= 8; temp++) {
                                        e->room->npc[temp]->state = 5;
                                        e->room->npc[temp]->enemyX = bbEntityX(mainPlayer->collider);
                                        e->room->npc[temp]->enemyY = bbEntityY(mainPlayer->collider);
                                        e->room->npc[temp]->enemyZ = bbEntityZ(mainPlayer->collider);
                                        e->room->npc[temp]->pathTimer = 70*bbRand(7,10);
                                        e->room->npc[temp]->reload = 2000;
                                        mainPlayer->disableControls = true;
                                    }

                                    if (e->eventState2==1) {
                                        e->soundChannels[0] = bbPlaySound(LoadTempSound("SFX/Ending/GateA/STOPRIGHTTHERE.ogg"));
                                        e->eventState2 = 2;
                                    }
                                } else {
                                    e->room->npc[i]->lastSeen = 70*300;
                                    e->room->npc[i]->reload = 2000;
                                    e->room->npc[i]->state3 = 70*145;
                                }
                            }
                        } else {
                            //TODO?
                            //ShouldPlay = 0
                            mainPlayer->moveSpeed = 0;
                            if (bbChannelPlaying(e->soundChannels[0])==false) {
                                //PlaySound2(IntroSFX(9)) ;Bang3
                                //SelectedEnding = "A2"
                                mainPlayer->godMode = 0;
                                mainPlayer->noclip = 0;
                                //KillTimer = -0.1f
                                DeathMSG = "";
                                Kill(mainPlayer);
                                mainPlayer->blinkTimer = -10;
                                RemoveEvent(e);
                                return;
                            }
                        }

                    }

                }
            }

        }
    } else {
        bbHideEntity(e->room->obj);
    }

}

}

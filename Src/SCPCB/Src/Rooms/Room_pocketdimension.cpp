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
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "Room_pocketdimension.h"

namespace CBN {

// Functions.
void FillRoom_pocketdimension(Room* r) {
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
    float angle;
    int safe;

    //, Bump
    int t1;

    //the tunnels in the first room
    MeshModel* hallway = bbLoadMesh("GFX/Map/pocketdimension2.b3d");
    //the room with the throne, moving pillars etc
    r->objects[8] = bbLoadMesh("GFX/Map/pocketdimension3.b3d");
    //the flying pillar
    r->objects[9] = bbLoadMesh("GFX/Map/pocketdimension4.b3d");
    r->objects[10] = bbCopyMeshModelEntity((MeshModel*)r->objects[9]);

    //the pillar room
    r->objects[11] = bbLoadMesh("GFX/Map/pocketdimension5.b3d");


    MeshModel* terrain = bbLoadMesh("GFX/Map/pocketdimensionterrain.b3d");
    bbScaleEntity(terrain,RoomScale,RoomScale,RoomScale,true);
    //RotateEntity(terrain,0,e\room\angle,0,True)
    bbPositionEntity(terrain, 0, 2944, 0, true);



    CreatePaper("docBurn", bbEntityX(r->obj),0.5f,bbEntityZ(r->obj)+3.5f);

    int n;
    Object* entity;
    //4 ;TODO: wut
    for (n = 0; n <= -1; n++) {
        switch (n) {
            case 0: {
                entity = hallway;
            } break;
            case 1: {
                entity = r->objects[8];
            } break;
            case 2: {
                entity = r->objects[9];
            } break;
            case 3: {
                entity = r->objects[10];
            } break;
            case 4: {
                entity = r->objects[11];
            } break;
        }

        //If (BumpEnabled) Then
        //
        //	For i = 1 To CountSurfaces(entity)
        //		sf = GetSurface(entity,i)
        //		b = GetSurfaceBrush( sf )
        //		t = GetBrushTexture(b,1)
        //		texname$ =  StripPath(TextureName(t))
        //		mat.Material=GetCache(texname)
        //		If (mat<>Null) Then
        //			If (mat\bump<>0) Then
        //				t1 = GetBrushTexture(b,0)
        //
        //				BrushTexture(b, t1, 0, 0)
        //				BrushTexture(b, mat\bump, 0, 1)
        //				BrushTexture(b, t, 0, 2)
        //
        //				PaintSurface(sf,b)
        //
        //				If (t1<>0) Then FreeTexture(t1
        //t1 = 0);
        //			EndIf
        //		EndIf
        //
        //		If (t<>0) Then FreeTexture(t
        //t = 0);
        //		If (b<>0) Then FreeBrush(b
       // b = 0);
        //	Next
        //
        //EndIf

    }

    for (i = 8; i <= 11; i++) {
        bbScaleEntity(r->objects[i],RoomScale,RoomScale,RoomScale);
        bbEntityType(r->objects[i], HIT_MAP);
        bbEntityPickMode(r->objects[i], 3);
        bbPositionEntity(r->objects[i],r->x,r->y,r->z+32.f,true);
    }

    bbScaleEntity(terrain,RoomScale,RoomScale,RoomScale);
    bbEntityType(terrain, HIT_MAP);
    bbEntityPickMode(terrain, 3);
    bbPositionEntity(terrain,r->x,r->y+2944.f*RoomScale,r->z+32.f,true);

    r->doors[0] = CreateDoor(r->x,2048*RoomScale,r->z+32.f-1024*RoomScale,0,r);
    r->doors[1] = CreateDoor(r->x,2048*RoomScale,r->z+32.f+1024*RoomScale,180,r);

    de = CreateDecal(DECAL_PD6, r->x-(1536*RoomScale), 0.02f,r->z+608*RoomScale+32.f, 90,0,0);
    bbEntityParent(de->obj, r->obj);
    de->size = bbRnd(0.8f, 0.8f);
    de->blendmode = 2;
    de->fx = 1+8;
    bbScaleSprite(de->obj, de->size, de->size);
    bbEntityFX(de->obj, 1+8);
    bbEntityBlend(de->obj, 2);

    bbScaleEntity(r->objects[10],RoomScale*1.5f,RoomScale*2.f,RoomScale*1.5f,true);
    bbPositionEntity(r->objects[11],r->x,r->y,r->z+64.f,true);

    for (i = 0; i <= 7; i++) {
        //CopyMesh
        r->objects[i] = bbCopyMeshModelEntity(hallway);
        bbScaleEntity(r->objects[i],RoomScale,RoomScale,RoomScale);
        angle = i * (360.f/8.f);

        bbEntityType(r->objects[i], HIT_MAP);
        bbEntityPickMode(r->objects[i], 3);

        bbRotateEntity(r->objects[i],0,angle-90,0);
        bbPositionEntity(r->objects[i],r->x+bbCos(angle)*(512.f*RoomScale),0.f,r->z+bbSin(angle)*(512.f*RoomScale));
        bbEntityParent(r->objects[i], r->obj);

        if (i < 5) {
            de = CreateDecal(i+DECAL_PD1, r->x+bbCos(angle)*(512.f*RoomScale)*3.f, 0.02f,r->z+bbSin(angle)*(512.f*RoomScale)*3.f, 90,angle-90,0);
            de->size = bbRnd(0.5f, 0.5f);
            de->blendmode = 2;
            de->fx = 1+8;
            bbScaleSprite(de->obj, de->size, de->size);
            bbEntityFX(de->obj, 1+8);
            bbEntityBlend(de->obj, 2);
        }
    }

    for (i = 12; i <= 16; i++) {
        r->objects[i] = bbCreatePivot(r->objects[11]);
        switch (i) {
            case 12: {
                bbPositionEntity(r->objects[i],r->x,r->y+200*RoomScale,r->z+64.f,true);
            } break;
            case 13: {
                bbPositionEntity(r->objects[i],r->x+390*RoomScale,r->y+200*RoomScale,r->z+64.f+272*RoomScale,true);
            } break;
            case 14: {
                bbPositionEntity(r->objects[i],r->x+838*RoomScale,r->y+200*RoomScale,r->z+64.f-551*RoomScale,true);
            } break;
            case 15: {
                bbPositionEntity(r->objects[i],r->x-139*RoomScale,r->y+200*RoomScale,r->z+64.f+1201*RoomScale,true);
            } break;
            case 16: {
                bbPositionEntity(r->objects[i],r->x-1238*RoomScale,r->y-1664*RoomScale,r->z+64.f+381*RoomScale,true);
            } break;
        }

    }

    // TODO: Fix.
    //Texture* OldManEyes = bbLoadTexture("GFX/npcs/oldmaneyes.jpg");
    //r->objects[17] = bbCreateSprite();
    //bbScaleSprite(r->objects[17], 0.03f, 0.03f);
    //bbEntityTexture(r->objects[17], OldManEyes);
    //bbEntityBlend(r->objects[17], 3);
    //bbEntityFX(r->objects[17], 1 + 8);
    //bbSpriteViewMode(r->objects[17], 2);

    //r->objects[18] = bbLoadTexture("GFX/npcs/pdplane.png", 1+2);
    //r->objects[19] = bbLoadTexture("GFX/npcs/pdplaneeye.png", 1+2);

    //r->objects[20] = bbCreateSprite();
    //bbScaleSprite(r->objects[20], 8.f, 8.f);
    //bbEntityTexture(r->objects[20], r->objects[18]);
    //bbEntityOrder(r->objects[20], 100);
    //bbEntityBlend(r->objects[20], 2);
    //bbEntityFX(r->objects[20], 1 + 8);
    //bbSpriteViewMode(r->objects[20], 2);

    //FreeTexture(t)
    bbFreeEntity(hallway);
}

void UpdateEvent_pocketdimension(Event* e) {
    float dist;
    int i;
    int temp;
    Pivot* pvt;
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

    int safe;

    //[Block]

    //eventstate: a timer for scaling the tunnels in the starting room
    //eventstate2:
    //0 if the player is in the starting room
    //1 if in the room with the throne, moving pillars, plane etc
    //12-15 if player is in the room with the tall pillars
    //(goes down from 15 to 12 And 106 teleports from pillar to another, pillars being room\objects[12 to 15])
    //eventstate3:
    //1 when appearing in the tunnel that looks like the tunnels in hcz
    //2 after opening the door in the tunnel
    //otherwise 0

    if (mainPlayer->currRoom == e->room) {
        bbShowEntity(e->room->obj);

        mainPlayer->injuries = mainPlayer->injuries+timing->tickDuration*0.00005f;

        if (bbEntityY(mainPlayer->collider)<2000*RoomScale || bbEntityY(mainPlayer->collider)>2608*RoomScale) {
            mainPlayer->footstepOverride = 1;
        }

        if (e->sounds[0] == 0) {
            LoadEventSound(e,"SFX/Room/PocketDimension/Rumble.ogg");
        }
        if (e->sounds[1] == 0) {
            e->sounds[1] = LoadEventSound(e,"SFX/Room/PocketDimension/PrisonVoices.ogg",1);
        }

        if (e->eventState == 0) {
            bbCameraFogColor(mainPlayer->cam, 0,0,0);
            bbCameraClsColor(mainPlayer->cam, 0,0,0);
            e->eventState = 0.1f;
        }

        //If (Music(3)=0) Then Music(3) = LoadSound("SFX/Music/PD.ogg") ;TODO: fix
        //If (EntityY(mainPlayer\collider)<2000*RoomScale Or e\eventState3=0 Or EntityY(mainPlayer\collider)>2608*RoomScale) Then
        //		ShouldPlay = 3
        //	Else
        //		ShouldPlay = 0
        //	EndIf

        bbScaleEntity(e->room->obj,RoomScale, RoomScale*(1.f + bbSin(e->eventState/14.f)*0.2f), RoomScale);
        for (i = 0; i <= 7; i++) {
            bbScaleEntity(e->room->objects[i],RoomScale*(1.f + abs(bbSin(e->eventState/21.f+i*45.f)*0.1f)),RoomScale*(1.f + bbSin(e->eventState/14.f+i*20.f)*0.1f), RoomScale,true);
        }
        bbScaleEntity(e->room->objects[9],RoomScale*(1.5f + abs(bbSin(e->eventState/21.f+i*45.f)*0.1f)),RoomScale*(1.f + bbSin(e->eventState/14.f+i*20.f)*0.1f), RoomScale,true);

        e->eventState = e->eventState + timing->tickDuration;

        if (e->eventState2 == 0) {
            e->room->doors[0]->open = false;
            e->room->doors[1]->open = false;

            if (e->eventState > 65*70) {
                if (bbRand(800)==1 && Curr106->state >=0) {
                    //PlaySound2(HorrorSFX(8))
                    Curr106->state = -0.1f;
                    e->eventState = 601;
                }
                //106 circles around the starting room
            } else if ((Curr106->state > 0)) {
                angle = modFloat(e->eventState/10, 360);
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->obj), 0.2f+0.35f+bbSin(e->eventState/14.f+i*20.f)*0.4f, bbEntityX(e->room->obj));
                bbRotateEntity(Curr106->collider, 0,angle,0);
                bbMoveEntity(Curr106->collider,0,0,6.f-bbSin(e->eventState/10.f));
                AnimateNPC(Curr106, 55, 104, 0.5f);
                bbRotateEntity(Curr106->collider, 0,angle+90,0);
                Curr106->idle = true;
            }
        }

        //106 attacks if close enough to player
        if (bbEntityDistance(mainPlayer->collider, Curr106->collider) < 0.3f) {
            Curr106->idle = false;
            Curr106->state = -11;
        }

        //in the second room
        if (e->eventState2 == 1) {

            bbPositionEntity(e->room->objects[9], bbEntityX(e->room->objects[8],true)+3384*RoomScale, 0.f, bbEntityZ(e->room->objects[8],true));

            bbTranslateEntity(e->room->objects[9], bbCos(e->eventState*0.8f)*5, 0, bbSin(e->eventState*1.6f)*4, true);
            bbRotateEntity(e->room->objects[9],0,e->eventState * 2,0);

            bbPositionEntity(e->room->objects[10], bbEntityX(e->room->objects[8],true), 0.f, bbEntityZ(e->room->objects[8],true)+3384*RoomScale);

            bbTranslateEntity(e->room->objects[10], bbSin(e->eventState*1.6f)*4, 0, bbCos(e->eventState*0.8f)*5, true);
            bbRotateEntity(e->room->objects[10],0,e->eventState * 2,0);

            //the "trick room"
            if (e->eventState3 == 1 || e->eventState3 == 2) {
                if (e->eventState3 == 1 && (e->room->doors[0]->openstate>150 || e->room->doors[1]->openstate>150)) {
                    PlaySound2(LoadTempSound("SFX/Horror/Horror16.ogg"));
                    mainPlayer->blurTimer = 800;
                    e->eventState3 = 2;
                }

                if (bbEntityY(mainPlayer->collider)<5.f) {
                    e->eventState3 = 0;
                }
            } else {
                //the trenches
                if (bbEntityY(mainPlayer->collider)>6.f) {
                    //ShouldPlay = 15
                    //If (Music(15)=0) Then Music(15) = LoadSound("SFX/Music/PDTrench.ogg") ;TODO: fix

                    bbCameraFogColor(mainPlayer->cam, 38, 55, 47);
                    bbCameraClsColor(mainPlayer->cam, 38, 55, 47);

                    if (bbEntityX(e->room->objects[20],true)<bbEntityX(e->room->objects[8],true)-4000*RoomScale) {
                        e->soundChannels[1] = bbPlaySound(e->sounds[1]);

                        bbPositionEntity(e->room->objects[20], bbEntityX(mainPlayer->collider,true)+4000*RoomScale, 12.f, bbEntityZ(mainPlayer->collider,true));
                    }

                    bbMoveEntity(mainPlayer->collider, 0, Min((12.f - bbEntityY(mainPlayer->collider)),0.f)*timing->tickDuration, 0);

                    x = -timing->tickDuration*RoomScale*4.f;
                    y = (17.f-abs(bbEntityX(mainPlayer->collider)-bbEntityX(e->room->objects[20]))*0.5f)-bbEntityY(e->room->objects[20]);
                    z = bbEntityZ(mainPlayer->collider,true)-bbEntityZ(e->room->objects[20]);
                    bbTranslateEntity(e->room->objects[20], x, y, z,true);
                    bbRotateEntity(e->room->objects[20], -90-(bbEntityX(mainPlayer->collider)-bbEntityX(e->room->objects[20]))*1.5f, -90.f, 0.f, true);


                    //check if the plane can see the player
                    safe = false;
                    for (i = 0; i <= 2; i++) {
                        switch (i) {
                            case 0: {
                                x = -1452*RoomScale;
                                z = -37*RoomScale;
                            } break;
                            case 1: {
                                x = -121*RoomScale;
                                z = 188*RoomScale;
                            } break;
                            case 2: {
                                x = 1223*RoomScale;
                                z = -196*RoomScale;
                            } break;
                        }

                        x = x + bbEntityX(e->room->objects[8],true);
                        z = z + bbEntityZ(e->room->objects[8],true);

                        if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), x, z) < 200*RoomScale) {
                            safe = true;
                            break;
                        }
                    }

                    dist = bbEntityDistance(mainPlayer->collider, e->room->objects[20]);

                    if (e->soundChannels[1]!=0 && bbChannelPlaying(e->soundChannels[1])) {
                        e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, mainPlayer->cam, 10.f, 0.3f+(!safe)*0.6f);
                    }
                    // TODO: Fix.
                    //if (safe) {
                    //    bbEntityTexture(e->room->objects[20], e->room->objects[18]);
                    //} else if (dist < 8.f) {
                    //    e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->objects[20], 8.f);
                    //    bbEntityTexture(e->room->objects[20], e->room->objects[19]);
                    //    mainPlayer->injuries = mainPlayer->injuries+(8.f-dist)*timing->tickDuration*0.001f;

                    //    if (dist<7.f) {
                    //        pvt = bbCreatePivot();
                    //        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                    //        bbPointEntity(pvt, e->room->objects[20]);
                    //        bbTurnEntity(pvt, 90, 0, 0);
                    //        mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt), mainPlayer->headPitch + 90.f, 10.f);
                    //        mainPlayer->headPitch = mainPlayer->headPitch-90;
                    //        bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), 10), 0);
                    //        bbFreeEntity(pvt);
                    //    }
                    //}

                    mainPlayer->camShake = Max(4.f+((!safe) * 4.f) - dist, 0.f);

                    //check if player is at the sinkhole (the exit from the trench room)
                    if (bbEntityY(mainPlayer->collider)<8.5f) {
                        LoadEventSound(e,"SFX/Room/PocketDimension/Rumble.ogg");
                        LoadEventSound(e,"SFX/Room/PocketDimension/PrisonVoices.ogg",1);

                        //move to the "exit room"
                        mainPlayer->blurTimer = 1500;
                        e->eventState2 = 1;
                        mainPlayer->blinkTimer = -10;

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true)-400*RoomScale, -304*RoomScale, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);

                    }

                } else {
                    e->eventState3 = 0;

                    for (i = 9; i <= 10; i++) {
                        dist = Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true));
                        if (dist<6.f) {
                            if (dist<100.f*RoomScale) {
                                pvt = bbCreatePivot();
                                bbPositionEntity(pvt, bbEntityX(e->room->objects[i],true),bbEntityY(mainPlayer->collider),bbEntityZ(e->room->objects[i],true));

                                bbPointEntity(pvt, mainPlayer->collider);
                                bbRotateEntity(pvt, 0, (int)(bbEntityYaw(pvt)/90)*90,0,true);
                                bbMoveEntity(pvt, 0,0,100*RoomScale);
                                bbPositionEntity(mainPlayer->collider, bbEntityX(pvt),bbEntityY(mainPlayer->collider),bbEntityZ(pvt));

                                bbFreeEntity(pvt);

                                if (!mainPlayer->dead) {
                                    DeathMSG = "In addition to the decomposed appearance typical of SCP-106's victims, the body exhibits injuries that have not been observed before: ";
                                    DeathMSG = DeathMSG + "massive skull fracture, three broken ribs, fractured shoulder and multiple heavy lacerations.";

                                    PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Impact.ogg"));
                                    Kill(mainPlayer);
                                }
                            }
                            e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->objects[i], 6.f);
                        }
                    }

                    pvt = bbCreatePivot();
                    bbPositionEntity(pvt, bbEntityX(e->room->objects[8],true)-1536*RoomScale,500*RoomScale,bbEntityZ(e->room->objects[8],true)+608*RoomScale);
                    if (bbEntityDistance(pvt, mainPlayer->collider)<5.f) {
                        e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, pvt, 3.f);
                    }
                    bbFreeEntity(pvt);

                    //106's eyes
                    bbShowEntity(e->room->objects[17]);
                    bbPositionEntity(e->room->objects[17], bbEntityX(e->room->objects[8],true),1376*RoomScale,bbEntityZ(e->room->objects[8],true)-2848*RoomScale);
                    bbPointEntity(e->room->objects[17], mainPlayer->collider);
                    bbTurnEntity(e->room->objects[17], 0, 180, 0);

                    temp = (int)(bbEntityDistance(mainPlayer->collider, e->room->objects[17]));
                    if (temp < 2000*RoomScale) {
                        mainPlayer->injuries = mainPlayer->injuries + (timing->tickDuration/4000);

                        if (mainPlayer->injuries > 1.f) {
                            if (mainPlayer->injuries - (timing->tickDuration/4000)<= 1.f) {
                                PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Kneel.ogg"));
                            }
                        }

                        mainPlayer->sanity895 = Max(mainPlayer->sanity895 - timing->tickDuration / temp / 8,-1000);

                        //TODO: fix
                        //e\soundChannels[0] = LoopRangedSound(OldManSFX(4), e\soundChannels[0], mainPlayer\cam, e\room\objects[17], 5.f, 0.6f)

                        mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs()) / 20.f)+1.f)*15.f*Max((6.f-temp)/6.f,0.f));

                        pvt = bbCreatePivot();
                        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                        bbPointEntity(pvt, e->room->objects[17]);
                        bbTurnEntity(pvt, 90, 0, 0);
                        mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt), mainPlayer->headPitch + 90.f, Min(Max(15000.f / (-mainPlayer->sanity895), 15.f), 500.f));
                        mainPlayer->headPitch = mainPlayer->headPitch-90;
                        bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), Min(Max(15000.f / (-mainPlayer->sanity895), 15.f), 500.f)), 0);
                        bbFreeEntity(pvt);

                        //teleport the player to the trenches
                        if (mainPlayer->crouching) {
                            mainPlayer->blinkTimer = -10;
                            bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true)-1344*RoomScale,2944*RoomScale,bbEntityZ(e->room->objects[8],true)-1184*RoomScale);
                            bbResetEntity(mainPlayer->collider);
                            mainPlayer->crouching = false;

                            LoadEventSound(e,"SFX/Room/PocketDimension/Explosion.ogg");
                            LoadEventSound(e,"SFX/Room/PocketDimension/TrenchPlane.ogg",1);
                            bbPositionEntity(e->room->objects[20], bbEntityX(e->room->objects[8],true)-1000,0,0,true);

                        }
                        //the "exit room"
                    } else if ((bbEntityY(mainPlayer->collider)<-180*RoomScale)) {
                        temp = (int)(Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(e->room->objects[8],true)+1024*RoomScale,bbEntityZ(e->room->objects[8],true)));
                        if (temp<640*RoomScale) {
                            mainPlayer->blurTimer = (640*RoomScale-temp)*3000;

                            //TODO: fix
                            //e\soundChannels[1] = LoopRangedSound(DecaySFX(Rand(1, 3)), e\soundChannels[1], mainPlayer\cam, mainPlayer\collider, 2.f, (640*RoomScale-temp)*abs(mainPlayer\moveSpeed)*100)
                            mainPlayer->moveSpeed = CurveValue(0.f, mainPlayer->moveSpeed, temp*10);

                            if (temp < 130*RoomScale) {

                                for (int iterator189 = 0; iterator189 < Room::getListSize(); iterator189++) {
                                    r = Room::getObject(iterator189);

                                    if (r->roomTemplate->name.equals("room2_3")) {
                                        e->eventState = 0;
                                        e->eventState2 = 0;
                                        //FreeSound(Music(3) Music(3) = 0 ;TODO: fix);

                                        mainPlayer->blinkTimer = -10;
                                        //LightBlink = 5

                                        PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Exit.ogg"));

                                        de = CreateDecal(DECAL_CORROSION, bbEntityX(r->obj), 381*RoomScale, bbEntityZ(r->obj), 270, bbRand(360), 0);

                                        bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 0.4f, bbEntityZ(r->obj));
                                        bbResetEntity(mainPlayer->collider);
                                        Curr106->idle = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (bbEntityY(mainPlayer->collider) < -1600*RoomScale) {
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[8]) > 4750*RoomScale) {
                    bbCameraFogColor(mainPlayer->cam, 0,0,0);
                    bbCameraClsColor(mainPlayer->cam, 0,0,0);

                    mainPlayer->dropSpeed = 0;
                    mainPlayer->blurTimer = 500;
                    mainPlayer->blurTimer = 1500;
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj,true), 0.4f, bbEntityX(e->room->obj,true));
                    for (int iterator190 = 0; iterator190 < Room::getListSize(); iterator190++) {
                        r = Room::getObject(iterator190);

                        if (r->roomTemplate->name.equals("room106")) {
                            e->eventState = 0;
                            e->eventState2 = 0;
                            //FreeSound(Music(3) Music(3) = 0 ;TODO: fix);
                            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj,true), 0.4f, bbEntityX(r->obj,true));

                            Curr106->state = 10000;
                            Curr106->idle = false;
                            break;
                        }
                    }
                    bbResetEntity(mainPlayer->collider);

                    e->eventState2 = 0;
                    UpdateDoorsTimer = 0;
                    UpdateDoors();
                    UpdateRooms();
                    //the player is not at the exit, must've fallen down
                } else {

                    if (!mainPlayer->dead) {
                        //PlaySound2(HorrorSFX(8))
                        DeathMSG = "In addition to the decomposed appearance typical of the victims of SCP-106, the subject seems to have suffered multiple heavy fractures to both of his legs.";

                    }
                    Kill(mainPlayer);
                    mainPlayer->blurTimer = 3000;
                }
            }

            UpdateDoorsTimer = 0;
            UpdateDoors();
            UpdateRooms();

        } else if ((e->eventState2 == 0)) {
            dist = bbEntityDistance(mainPlayer->collider, e->room->obj);

            if (dist > 1700*RoomScale) {
                mainPlayer->blinkTimer = -10;

                switch (bbRand(25)) {
                    case 1:
                    case 2:
                    case 3:
                    case 4: {
                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        pvt = bbCreatePivot();
                        bbPositionEntity(pvt, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));

                        bbPointEntity(pvt, e->room->obj);
                        bbMoveEntity(pvt, 0,0,dist*1.9f);
                        bbPositionEntity(mainPlayer->collider, bbEntityX(pvt), bbEntityY(mainPlayer->collider), bbEntityZ(pvt));
                        bbResetEntity(mainPlayer->collider);

                        bbMoveEntity(pvt, 0,0,0.8f);
                        bbPositionEntity(e->room->objects[10], bbEntityX(pvt), 0.f, bbEntityZ(pvt));
                        bbRotateEntity(e->room->objects[10], 0, bbEntityYaw(pvt), 0, true);

                        bbFreeEntity(pvt);
                    } break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10: {
                        e->eventState2 = 1;
                        mainPlayer->blinkTimer = -10;
                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true), 0.5f, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                        //middle of the large starting room
                    } break;
                    case 11:
                    case 12: {
                        mainPlayer->blurTimer = 500;
                        bbPositionEntity(mainPlayer->collider,bbEntityX(e->room->obj), 0.5f, bbEntityZ(e->room->obj));
                        //"exit room"
                    } break;
                    case 13:
                    case 14:
                    case 15: {
                        mainPlayer->blurTimer = 1500;
                        e->eventState2 = 1;
                        mainPlayer->blinkTimer = -10;

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true)-400*RoomScale, -304*RoomScale, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                    } break;
                    case 16:
                    case 17:
                    case 18:
                    case 19: {
                        mainPlayer->blurTimer = 1500;
                        for (int iterator191 = 0; iterator191 < Room::getListSize(); iterator191++) {
                            r = Room::getObject(iterator191);

                            if (r->roomTemplate->name.equals("tunnel")) {
                                e->eventState = 0;
                                e->eventState2 = 0;
                                //FreeSound(Music(3) Music(3) = 0 ;TODO: fix);
                                bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 0.4f, bbEntityZ(r->obj));
                                bbResetEntity(mainPlayer->collider);
                                Curr106->idle = false;
                                break;
                            }
                        }
                        //the tower room
                    } break;
                    case 20:
                    case 21:
                    case 22: {
                        mainPlayer->blinkTimer = -10;
                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[12],true), 0.6f, bbEntityZ(e->room->objects[12],true));
                        bbResetEntity(mainPlayer->collider);
                        e->eventState2 = 15;
                    } break;
                    case 23:
                    case 24:
                    case 25: {
                        mainPlayer->blurTimer = 1500;
                        e->eventState2 = 1;
                        e->eventState3 = 1;
                        mainPlayer->blinkTimer = -10;

                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true), 2288*RoomScale, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                    } break;
                }

                UpdateDoorsTimer = 0;
                UpdateDoors();
                UpdateRooms();
            }
            //pillar room
        } else {
            bbCameraFogColor(mainPlayer->cam, 38*0.5f, 55*0.5f, 47*0.5f);
            bbCameraClsColor(mainPlayer->cam, 38*0.5f, 55*0.5f, 47*0.5f);

            if (bbRand(800)==1) {
                //TODO: Not a particle.
                angle = bbEntityYaw(mainPlayer->cam,true)+bbRnd(150,210);
                p = CreateParticle(bbEntityX(mainPlayer->collider)+bbCos(angle)*7.5f, 0.f, bbEntityZ(mainPlayer->collider)+bbSin(angle)*7.5f, PARTICLE_HG, 4.f, 0.f, 2500);
                bbEntityBlend(p->sprite, 2);
                //EntityFX(p\obj, 1)
                p->speed = 0.01f;
                p->sizeChange = 0;
                bbPointEntity(p->pvt, mainPlayer->cam);
                bbTurnEntity(p->pvt, 0, 145, 0, true);
                bbTurnEntity(p->pvt, bbRand(10,20), 0, 0, true);
            }

            if (e->eventState2 > 12) {
                Curr106->idle = true;
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[(int)(e->eventState2)],true),0.27f, bbEntityZ(e->room->objects[(int)(e->eventState2)],true));

                bbPointEntity(Curr106->collider, mainPlayer->cam);
                bbTurnEntity(Curr106->collider, 0, bbSin(TimeInPosMilliSecs() / 20) * 6.f, 0, true);
                bbMoveEntity(Curr106->collider, 0, 0, bbSin(TimeInPosMilliSecs() / 15) * 0.06f);

                if (bbRand(750)==1 && e->eventState2 > 12) {
                    mainPlayer->blinkTimer = -10;
                    e->eventState2 = e->eventState2-1;
                    //PlaySound2(HorrorSFX(8))
                }

                if (e->eventState2 == 12) {
                    mainPlayer->camShake = 1.f;
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[(int)(e->eventState2)],true),-1.f, bbEntityZ(e->room->objects[(int)(e->eventState2)],true));
                    Curr106->state = -11;
                    bbResetEntity(Curr106->collider);
                }

            } else {
                Curr106->state = -11;
                Curr106->idle = false;
            }

            if (bbEntityY(mainPlayer->collider) < -1600*RoomScale) {
                //player is at the exit
                if (Distance(bbEntityX(e->room->objects[16],true),bbEntityZ(e->room->objects[16],true),bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider))<144*RoomScale) {

                    bbCameraFogColor(mainPlayer->cam, 0,0,0);
                    bbCameraClsColor(mainPlayer->cam, 0,0,0);

                    mainPlayer->dropSpeed = 0;
                    mainPlayer->blurTimer = 500;
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj), 0.5f, bbEntityZ(e->room->obj));
                    bbResetEntity(mainPlayer->collider);
                    e->eventState2 = 0;
                    UpdateDoorsTimer = 0;
                    UpdateDoors();
                    UpdateRooms();
                    //somewhere else -> must've fallen down
                } else {
                    //If (KillTimer => 0) Then PlaySound2(HorrorSFX(8))
                    Kill(mainPlayer);
                    mainPlayer->blurTimer = 3000;
                }
            }

        }

    } else {
        bbHideEntity(e->room->obj);

        bbCameraClsColor(mainPlayer->cam, 0,0,0);
        e->eventState = 0;
        e->eventState2 = 0;
        e->eventState3 = 0;
    }
    //[End Block]
}

}

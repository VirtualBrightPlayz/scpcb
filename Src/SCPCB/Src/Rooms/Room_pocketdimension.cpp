#include "Room_pocketdimension.h"
#include "include.h"

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
    int hallway = bbLoadMesh("GFX/Map/pocketdimension2.b3d");
    //the room with the throne, moving pillars etc
    r->objects[8] = bbLoadMesh("GFX/Map/pocketdimension3.b3d");
    //the flying pillar
    r->objects[9] = bbLoadMesh("GFX/Map/pocketdimension4.b3d");
    r->objects[10] = bbCopyMeshModelEntity(r->objects[9]);

    //the pillar room
    r->objects[11] = bbLoadMesh("GFX/Map/pocketdimension5.b3d");


    int terrain = bbLoadMesh("GFX/Map/pocketdimensionterrain.b3d");
    bbScaleEntity(terrain,RoomScale,RoomScale,RoomScale,true);
    //RotateEntity(terrain,0,e\room\angle,0,True)
    bbPositionEntity(terrain, 0, 2944, 0, true);



    CreatePaper("docBurn", bbEntityX(r->obj),0.5,bbEntityZ(r->obj)+3.5);

    int n;
    int entity;
    //4 ;TODO: wut
    for (n = 0; n <= -1; n++) {
        switch (n) {
            case 0: {
                entity = hallway;
            }
            case 1: {
                entity = r->objects[8];
            }
            case 2: {
                entity = r->objects[9];
            }
            case 3: {
                entity = r->objects[10];
            }
            case 4: {
                entity = r->objects[11];
            }
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
        t1 = 0);
        //			EndIf
        //		EndIf
        //
        //		If (t<>0) Then FreeTexture(t
        t = 0);
        //		If (b<>0) Then FreeBrush(b
        b = 0);
        //	Next
        //
        //EndIf

    }

    for (i = 8; i <= 11; i++) {
        bbScaleEntity(r->objects[i],RoomScale,RoomScale,RoomScale);
        bbEntityType(r->objects[i], HIT_MAP);
        bbEntityPickMode(r->objects[i], 3);
        bbPositionEntity(r->objects[i],r->x,r->y,r->z+32.0,true);
    }

    bbScaleEntity(terrain,RoomScale,RoomScale,RoomScale);
    bbEntityType(terrain, HIT_MAP);
    bbEntityPickMode(terrain, 3);
    bbPositionEntity(terrain,r->x,r->y+2944.0*RoomScale,r->z+32.0,true);

    r->doors[0] = CreateDoor(r->x,2048*RoomScale,r->z+32.0-1024*RoomScale,0,r);
    r->doors[1] = CreateDoor(r->x,2048*RoomScale,r->z+32.0+1024*RoomScale,180,r);

    de = CreateDecal(DECAL_PD6, r->x-(1536*RoomScale), 0.02,r->z+608*RoomScale+32.0, 90,0,0);
    bbEntityParent(de->obj, r->obj);
    de->size = bbRnd(0.8, 0.8);
    de->blendmode = 2;
    de->fx = 1+8;
    bbScaleSprite(de->obj, de->size, de->size);
    bbEntityFX(de->obj, 1+8);
    bbEntityBlend(de->obj, 2);

    bbScaleEntity(r->objects[10],RoomScale*1.5,RoomScale*2.0,RoomScale*1.5,true);
    bbPositionEntity(r->objects[11],r->x,r->y,r->z+64.0,true);

    for (i = 0; i <= 7; i++) {
        //CopyMesh
        r->objects[i] = bbCopyMeshModelEntity(hallway);
        bbScaleEntity(r->objects[i],RoomScale,RoomScale,RoomScale);
        angle = i * (360.0/8.0);

        bbEntityType(r->objects[i], HIT_MAP);
        bbEntityPickMode(r->objects[i], 3);

        bbRotateEntity(r->objects[i],0,angle-90,0);
        bbPositionEntity(r->objects[i],r->x+bbCos(angle)*(512.0*RoomScale),0.0,r->z+bbSin(angle)*(512.0*RoomScale));
        bbEntityParent(r->objects[i], r->obj);

        if (i < 5) {
            de = CreateDecal(i+DECAL_PD1, r->x+bbCos(angle)*(512.0*RoomScale)*3.0, 0.02,r->z+bbSin(angle)*(512.0*RoomScale)*3.0, 90,angle-90,0);
            de->size = bbRnd(0.5, 0.5);
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
                bbPositionEntity(r->objects[i],r->x,r->y+200*RoomScale,r->z+64.0,true);
            }
            case 13: {
                bbPositionEntity(r->objects[i],r->x+390*RoomScale,r->y+200*RoomScale,r->z+64.0+272*RoomScale,true);
            }
            case 14: {
                bbPositionEntity(r->objects[i],r->x+838*RoomScale,r->y+200*RoomScale,r->z+64.0-551*RoomScale,true);
            }
            case 15: {
                bbPositionEntity(r->objects[i],r->x-139*RoomScale,r->y+200*RoomScale,r->z+64.0+1201*RoomScale,true);
            }
            case 16: {
                bbPositionEntity(r->objects[i],r->x-1238*RoomScale,r->y-1664*RoomScale,r->z+64.0+381*RoomScale,true);
            }
        }

    }

    int OldManEyes = bbLoadTexture("GFX/npcs/oldmaneyes.jpg");
    r->objects[17] = bbCreateSprite();
    bbScaleSprite(r->objects[17], 0.03, 0.03);
    bbEntityTexture(r->objects[17], OldManEyes);
    bbEntityBlend(r->objects[17], 3);
    bbEntityFX(r->objects[17], 1 + 8);
    bbSpriteViewMode(r->objects[17], 2);

    r->objects[18] = bbLoadTexture("GFX/npcs/pdplane.png", 1+2);
    r->objects[19] = bbLoadTexture("GFX/npcs/pdplaneeye.png", 1+2);

    r->objects[20] = bbCreateSprite();
    bbScaleSprite(r->objects[20], 8.0, 8.0);
    bbEntityTexture(r->objects[20], r->objects[18]);
    bbEntityOrder(r->objects[20], 100);
    bbEntityBlend(r->objects[20], 2);
    bbEntityFX(r->objects[20], 1 + 8);
    bbSpriteViewMode(r->objects[20], 2);

    //FreeTexture(t)
    bbFreeEntity(hallway);
}

void UpdateEvent_pocketdimension(Event* e) {
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

        mainPlayer->injuries = mainPlayer->injuries+timing->tickDuration*0.00005;

        if (bbEntityY(mainPlayer->collider)<2000*RoomScale | bbEntityY(mainPlayer->collider)>2608*RoomScale) {
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
            e->eventState = 0.1;
        }

        //If (Music(3)=0) Then Music(3) = LoadSound("SFX/Music/PD.ogg") ;TODO: fix
        //If (EntityY(mainPlayer\collider)<2000*RoomScale Or e\eventState3=0 Or EntityY(mainPlayer\collider)>2608*RoomScale) Then
        //		ShouldPlay = 3
        //	Else
        //		ShouldPlay = 0
        //	EndIf

        bbScaleEntity(e->room->obj,RoomScale, RoomScale*(1.0 + bbSin(e->eventState/14.0)*0.2), RoomScale);
        for (i = 0; i <= 7; i++) {
            bbScaleEntity(e->room->objects[i],RoomScale*(1.0 + abs(bbSin(e->eventState/21.0+i*45.0)*0.1)),RoomScale*(1.0 + bbSin(e->eventState/14.0+i*20.0)*0.1), RoomScale,true);
        }
        bbScaleEntity(e->room->objects[9],RoomScale*(1.5 + abs(bbSin(e->eventState/21.0+i*45.0)*0.1)),RoomScale*(1.0 + bbSin(e->eventState/14.0+i*20.0)*0.1), RoomScale,true);

        e->eventState = e->eventState + timing->tickDuration;

        if (e->eventState2 == 0) {
            e->room->doors[0]->open = false;
            e->room->doors[1]->open = false;

            if (e->eventState > 65*70) {
                if (bbRand(800)==1 & Curr106->state >=0) {
                    //PlaySound2(HorrorSFX(8))
                    Curr106->state = -0.1;
                    e->eventState = 601;
                }
                //106 circles around the starting room
            } else if ((Curr106->state > 0)) {
                angle = (e->eventState/10 % 360);
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->obj), 0.2+0.35+bbSin(e->eventState/14.0+i*20.0)*0.4, bbEntityX(e->room->obj));
                bbRotateEntity(Curr106->collider, 0,angle,0);
                bbMoveEntity(Curr106->collider,0,0,6.0-bbSin(e->eventState/10.0));
                AnimateNPC(Curr106, 55, 104, 0.5);
                bbRotateEntity(Curr106->collider, 0,angle+90,0);
                Curr106->idle = true;
            }
        }

        //106 attacks if close enough to player
        if (bbEntityDistance(mainPlayer->collider, Curr106->collider) < 0.3) {
            Curr106->idle = false;
            Curr106->state = -11;
        }

        //in the second room
        if (e->eventState2 == 1) {

            bbPositionEntity(e->room->objects[9], bbEntityX(e->room->objects[8],true)+3384*RoomScale, 0.0, bbEntityZ(e->room->objects[8],true));

            bbTranslateEntity(e->room->objects[9], bbCos(e->eventState*0.8)*5, 0, bbSin(e->eventState*1.6)*4, true);
            bbRotateEntity(e->room->objects[9],0,e->eventState * 2,0);

            bbPositionEntity(e->room->objects[10], bbEntityX(e->room->objects[8],true), 0.0, bbEntityZ(e->room->objects[8],true)+3384*RoomScale);

            bbTranslateEntity(e->room->objects[10], bbSin(e->eventState*1.6)*4, 0, bbCos(e->eventState*0.8)*5, true);
            bbRotateEntity(e->room->objects[10],0,e->eventState * 2,0);

            //the "trick room"
            if (e->eventState3 == 1 | e->eventState3 == 2) {
                if (e->eventState3 == 1 & (e->room->doors[0]->openstate>150 | e->room->doors[1]->openstate>150)) {
                    PlaySound2(LoadTempSound("SFX/Horror/Horror16.ogg"));
                    mainPlayer->blurTimer = 800;
                    e->eventState3 = 2;
                }

                if (bbEntityY(mainPlayer->collider)<5.0) {
                    e->eventState3 = 0;
                }
            } else {
                //the trenches
                if (bbEntityY(mainPlayer->collider)>6.0) {
                    //ShouldPlay = 15
                    //If (Music(15)=0) Then Music(15) = LoadSound("SFX/Music/PDTrench.ogg") ;TODO: fix

                    bbCameraFogColor(mainPlayer->cam, 38, 55, 47);
                    bbCameraClsColor(mainPlayer->cam, 38, 55, 47);

                    if (bbEntityX(e->room->objects[20],true)<bbEntityX(e->room->objects[8],true)-4000*RoomScale) {
                        e->soundChannels[1] = bbPlaySound(e->sounds[1]);

                        bbPositionEntity(e->room->objects[20], bbEntityX(mainPlayer->collider,true)+4000*RoomScale, 12.0, bbEntityZ(mainPlayer->collider,true));
                    }

                    bbMoveEntity(mainPlayer->collider, 0, Min((12.0 - bbEntityY(mainPlayer->collider)),0.0)*timing->tickDuration, 0);

                    x = -timing->tickDuration*RoomScale*4.0;
                    y = (17.0-abs(bbEntityX(mainPlayer->collider)-bbEntityX(e->room->objects[20]))*0.5)-bbEntityY(e->room->objects[20]);
                    z = bbEntityZ(mainPlayer->collider,true)-bbEntityZ(e->room->objects[20]);
                    bbTranslateEntity(e->room->objects[20], x, y, z,true);
                    bbRotateEntity(e->room->objects[20], -90-(bbEntityX(mainPlayer->collider)-bbEntityX(e->room->objects[20]))*1.5, -90.0, 0.0, true);


                    //check if the plane can see the player
                    safe = false;
                    for (i = 0; i <= 2; i++) {
                        switch (i) {
                            case 0: {
                                x = -1452*RoomScale;
                                z = -37*RoomScale;
                            }
                            case 1: {
                                x = -121*RoomScale;
                                z = 188*RoomScale;
                            }
                            case 2: {
                                x = 1223*RoomScale;
                                z = -196*RoomScale;
                            }
                        }

                        x = x + bbEntityX(e->room->objects[8],true);
                        z = z + bbEntityZ(e->room->objects[8],true);

                        if (Distance(bbEntityX(mainPlayer->collider), bbEntityZ(mainPlayer->collider), x, z) < 200*RoomScale) {
                            safe = true;
                            break;
                        }
                    }

                    dist = bbEntityDistance(mainPlayer->collider, e->room->objects[20]);

                    if (e->soundChannels[1]!=0 & bbChannelPlaying(e->soundChannels[1])) {
                        e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, mainPlayer->cam, 10.0, 0.3+(!safe)*0.6);
                    }

                    if (safe) {
                        bbEntityTexture(e->room->objects[20], e->room->objects[18]);
                    } else if ((dist < 8.0)) {
                        e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->objects[20], 8.0);
                        bbEntityTexture(e->room->objects[20], e->room->objects[19]);
                        mainPlayer->injuries = mainPlayer->injuries+(8.0-dist)*timing->tickDuration*0.001;

                        if (dist<7.0) {
                            pvt = bbCreatePivot();
                            bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                            bbPointEntity(pvt, e->room->objects[20]);
                            bbTurnEntity(pvt, 90, 0, 0);
                            mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt), mainPlayer->headPitch + 90.0, 10.0);
                            mainPlayer->headPitch = mainPlayer->headPitch-90;
                            bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), 10), 0);
                            bbFreeEntity(pvt);
                        }
                    }

                    mainPlayer->camShake = Max(4.0+((!safe) * 4.0) - dist, 0.0);

                    //check if player is at the sinkhole (the exit from the trench room)
                    if (bbEntityY(mainPlayer->collider)<8.5) {
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
                        if (dist<6.0) {
                            if (dist<100.0*RoomScale) {
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
                            e->soundChannels[0] = LoopRangedSound(e->sounds[0], e->soundChannels[0], mainPlayer->cam, e->room->objects[i], 6.0);
                        }
                    }

                    pvt = bbCreatePivot();
                    bbPositionEntity(pvt, bbEntityX(e->room->objects[8],true)-1536*RoomScale,500*RoomScale,bbEntityZ(e->room->objects[8],true)+608*RoomScale);
                    if (bbEntityDistance(pvt, mainPlayer->collider)<5.0) {
                        e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, pvt, 3.0);
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

                        if (mainPlayer->injuries > 1.0) {
                            if (mainPlayer->injuries - (timing->tickDuration/4000)<= 1.0) {
                                PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Kneel.ogg"));
                            }
                        }

                        mainPlayer->sanity895 = Max(mainPlayer->sanity895 - timing->tickDuration / temp / 8,-1000);

                        //TODO: fix
                        //e\soundChannels[0] = LoopRangedSound(OldManSFX(4), e\soundChannels[0], mainPlayer\cam, e\room\objects[17], 5.0, 0.6)

                        mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs()) / 20.0)+1.0)*15.0*Max((6.0-temp)/6.0,0.0));

                        pvt = bbCreatePivot();
                        bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                        bbPointEntity(pvt, e->room->objects[17]);
                        bbTurnEntity(pvt, 90, 0, 0);
                        mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt), mainPlayer->headPitch + 90.0, Min(Max(15000.0 / (-mainPlayer->sanity895), 15.0), 500.0));
                        mainPlayer->headPitch = mainPlayer->headPitch-90;
                        bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), Min(Max(15000.0 / (-mainPlayer->sanity895), 15.0), 500.0)), 0);
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
                            //e\soundChannels[1] = LoopRangedSound(DecaySFX(Rand(1, 3)), e\soundChannels[1], mainPlayer\cam, mainPlayer\collider, 2.0, (640*RoomScale-temp)*abs(mainPlayer\moveSpeed)*100)
                            mainPlayer->moveSpeed = CurveValue(0.0, mainPlayer->moveSpeed, temp*10);

                            if (temp < 130*RoomScale) {

                                for (int iterator189 = 0; iterator189 < Room::getListSize(); iterator189++) {
                                    r = Room::getObject(iterator189);

                                    if (r->roomTemplate->name == "room2_3") {
                                        e->eventState = 0;
                                        e->eventState2 = 0;
                                        //FreeSound(Music(3)
                                        Music(3) = 0 ;TODO: fix);

                                        mainPlayer->blinkTimer = -10;
                                        //LightBlink = 5

                                        PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Exit.ogg"));

                                        de = CreateDecal(DECAL_CORROSION, bbEntityX(r->obj), 381*RoomScale, bbEntityZ(r->obj), 270, bbRand(360), 0);

                                        bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 0.4, bbEntityZ(r->obj));
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
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj,true), 0.4, bbEntityX(e->room->obj,true));
                    for (int iterator190 = 0; iterator190 < Room::getListSize(); iterator190++) {
                        r = Room::getObject(iterator190);

                        if (r->roomTemplate->name == "room106") {
                            e->eventState = 0;
                            e->eventState2 = 0;
                            //FreeSound(Music(3)
                            Music(3) = 0 ;TODO: fix);
                            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj,true), 0.4, bbEntityX(r->obj,true));

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
                    case 1,2,3,4: {
                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        pvt = bbCreatePivot();
                        bbPositionEntity(pvt, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));

                        bbPointEntity(pvt, e->room->obj);
                        bbMoveEntity(pvt, 0,0,dist*1.9);
                        bbPositionEntity(mainPlayer->collider, bbEntityX(pvt), bbEntityY(mainPlayer->collider), bbEntityZ(pvt));
                        bbResetEntity(mainPlayer->collider);

                        bbMoveEntity(pvt, 0,0,0.8);
                        bbPositionEntity(e->room->objects[10], bbEntityX(pvt), 0.0, bbEntityZ(pvt));
                        bbRotateEntity(e->room->objects[10], 0, bbEntityYaw(pvt), 0, true);

                        bbFreeEntity(pvt);
                    }
                    case 5,6,7,8,9,10: {
                        e->eventState2 = 1;
                        mainPlayer->blinkTimer = -10;
                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true), 0.5, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                        //middle of the large starting room
                    }
                    case 11,12: {
                        mainPlayer->blurTimer = 500;
                        bbPositionEntity(mainPlayer->collider,bbEntityX(e->room->obj), 0.5, bbEntityZ(e->room->obj));
                        //"exit room"
                    }
                    case 13,14,15: {
                        mainPlayer->blurTimer = 1500;
                        e->eventState2 = 1;
                        mainPlayer->blinkTimer = -10;

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true)-400*RoomScale, -304*RoomScale, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                    }
                    case 16,17,18,19: {
                        mainPlayer->blurTimer = 1500;
                        for (int iterator191 = 0; iterator191 < Room::getListSize(); iterator191++) {
                            r = Room::getObject(iterator191);

                            if (r->roomTemplate->name == "tunnel") {
                                e->eventState = 0;
                                e->eventState2 = 0;
                                //FreeSound(Music(3)
                                Music(3) = 0 ;TODO: fix);
                                bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 0.4, bbEntityZ(r->obj));
                                bbResetEntity(mainPlayer->collider);
                                Curr106->idle = false;
                                break;
                            }
                        }
                        //the tower room
                    }
                    case 20,21,22: {
                        mainPlayer->blinkTimer = -10;
                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[12],true), 0.6, bbEntityZ(e->room->objects[12],true));
                        bbResetEntity(mainPlayer->collider);
                        e->eventState2 = 15;
                    }
                    case 23,24,25: {
                        mainPlayer->blurTimer = 1500;
                        e->eventState2 = 1;
                        e->eventState3 = 1;
                        mainPlayer->blinkTimer = -10;

                        //TODO: fix
                        //PlaySound2(OldManSFX(3))

                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[8],true), 2288*RoomScale, bbEntityZ(e->room->objects[8],true));
                        bbResetEntity(mainPlayer->collider);
                    }
                }

                UpdateDoorsTimer = 0;
                UpdateDoors();
                UpdateRooms();
            }
            //pillar room
        } else {
            bbCameraFogColor(mainPlayer->cam, 38*0.5, 55*0.5, 47*0.5);
            bbCameraClsColor(mainPlayer->cam, 38*0.5, 55*0.5, 47*0.5);

            if (bbRand(800)==1) {
                //TODO: Not a particle.
                angle = bbEntityYaw(mainPlayer->cam,true)+bbRnd(150,210);
                p = CreateParticle(bbEntityX(mainPlayer->collider)+bbCos(angle)*7.5, 0.0, bbEntityZ(mainPlayer->collider)+bbSin(angle)*7.5, PARTICLE_HG, 4.0, 0.0, 2500);
                bbEntityBlend(p->obj, 2);
                //EntityFX(p\obj, 1)
                p->speed = 0.01;
                p->sizeChange = 0;
                bbPointEntity(p->pvt, mainPlayer->cam);
                bbTurnEntity(p->pvt, 0, 145, 0, true);
                bbTurnEntity(p->pvt, bbRand(10,20), 0, 0, true);
            }

            if (e->eventState2 > 12) {
                Curr106->idle = true;
                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[(int)(e->eventState2)],true),0.27, bbEntityZ(e->room->objects[(int)(e->eventState2)],true));

                bbPointEntity(Curr106->collider, mainPlayer->cam);
                bbTurnEntity(Curr106->collider, 0, bbSin(TimeInPosMilliSecs() / 20) * 6.0, 0, true);
                bbMoveEntity(Curr106->collider, 0, 0, bbSin(TimeInPosMilliSecs() / 15) * 0.06);

                if (bbRand(750)==1 & e->eventState2 > 12) {
                    mainPlayer->blinkTimer = -10;
                    e->eventState2 = e->eventState2-1;
                    //PlaySound2(HorrorSFX(8))
                }

                if (e->eventState2 == 12) {
                    mainPlayer->camShake = 1.0;
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[(int)(e->eventState2)],true),-1.0, bbEntityZ(e->room->objects[(int)(e->eventState2)],true));
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
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj), 0.5, bbEntityZ(e->room->obj));
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

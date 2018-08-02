#include "NPCtype066.h"
#include "include.h"

namespace CBN {

// Functions.
void InitializeNPCtype066(NPC* n) {
    n->nvName = "SCP-066";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.2);
    bbEntityType(n->collider, HIT_PLAYER);

    n->obj = bbLoadAnimMesh("GFX/NPCs/scp066/scp-066.b3d");
    float temp = GetINIFloat("Data/NPCs.ini", "SCP-066", "scale")/2.5;
    bbScaleEntity(n->obj, temp, temp, temp);

    n->speed = (GetINIFloat("Data/NPCs.ini", "SCP-066", "speed") / 100.0);
}

void UpdateNPCtype066(NPC* n) {
    WayPoint* w;
    Decal* de;
    Door* d;
    float angle;

    switch (n->state) {
        case 0: {
            //idle: moves around randomly from waypoint to another if the player is far enough
            //starts staring at the player when the player is close enough

            if (n->playerDistance > 20.0) {
                AnimateNPC(n, 451, 612, 0.2, true);
                //Animate2(n\obj, AnimTime(n\obj), 451, 612, 0.2, True)

                if (n->state2 < TimeInPosMilliSecs()) {
                    for (int iterator123 = 0; iterator123 < WayPoint::getListSize(); iterator123++) {
                        w = WayPoint::getObject(iterator123);

                        //If (w\door = Null) Then ;TODO: fix?
                        if (Abs(bbEntityX(w->obj,true)-bbEntityX(n->collider))<4.0) {
                            if (Abs(bbEntityZ(w->obj,true)-bbEntityZ(n->collider))<4.0) {
                                bbPositionEntity(n->collider, bbEntityX(w->obj,true), bbEntityY(w->obj,true)+0.3, bbEntityZ(w->obj,true));
                                bbResetEntity(n->collider);
                                Exit;
                            }
                        }
                        //EndIf
                    }
                    n->state2 = TimeInPosMilliSecs()+5000;
                }
            } else if ((n->playerDistance < 8.0)) {
                n->lastDist = bbRnd(1.0, 2.5);
                n->state = 1;
            }
            //staring at the player
        }
        case 1: {

            if (n->frame<451) {
                angle = WrapAngle(CurveAngle(bbDeltaYaw(n->collider, mainPlayer->collider)-180, (bbAnimTime(n->obj)-2.0)/1.2445, 15.0));
                //0->360 = 2->450
                SetNPCFrame(n,angle*1.2445+2.0);

                //SetAnimTime(n\obj, angle*1.2445+2.0)
            } else {
                AnimateNPC(n, 636, 646, 0.4, false);
                if (n->frame == 646) {
                    SetNPCFrame(n,2);
                }
                //Animate2(n\obj, AnimTime(n\obj), 636, 646, 0.4, False)
                //If (AnimTime(n\obj)=646) Then SetAnimTime(n\obj, 2)
            }

            if (bbRand(700)==1) {
                PlayRangedSound(LoadTempSound("SFX/SCP/066/Eric"+Str(bbRand(1,3))+".ogg"),mainPlayer->cam, n->collider, 8.0);
            }

            if (n->playerDistance < 1.0+n->lastDist) {
                n->state = bbRand(2,3);
            }
            //roll towards the player and make a sound, and then escape
        }
        case 2: {
            if (n->frame < 647) {
                angle = CurveAngle(0, (bbAnimTime(n->obj)-2.0)/1.2445, 5.0);

                if (angle < 5 | angle > 355) {
                    SetNPCFrame(n,647);
                } else {
                    SetNPCFrame(n,angle*1.2445+2.0);
                }
                //SetAnimTime(n\obj, angle*1.2445+2.0)
                //If (angle < 5 Or angle > 355) Then SetAnimTime(n\obj, 647)
            } else {
                if (n->frame==683) {
                    if (n->state2 == 0) {
                        if (bbRand(2)==1) {
                            PlayRangedSound(LoadTempSound("SFX/SCP/066/Eric"+Str(bbRand(1,3))+".ogg"),mainPlayer->cam, n->collider, 8.0);
                        } else {
                            PlayRangedSound(LoadTempSound("SFX/SCP/066/Notes"+Str(bbRand(1,6))+".ogg"), mainPlayer->cam, n->collider, 8.0);
                        }

                        switch (bbRand(1,6)) {
                            case 1: {
                                if (n->sounds[1]==0) {
                                    n->sounds[1] = bbLoadSound("SFX/SCP/066/Beethoven.ogg");
                                }
                                n->soundChannels[1] = PlayRangedSound(n->sounds[1], mainPlayer->cam, n->collider);
                                mainPlayer->camShake = 10.0;
                            }
                            case 2: {
                                n->state3 = bbRand(700,1400);
                            }
                            case 3: {
                                for (int iterator124 = 0; iterator124 < Door::getListSize(); iterator124++) {
                                    d = Door::getObject(iterator124);

                                    if (d->locked == false & d->tag == "" & d->code == "") {
                                        if (Abs(bbEntityX(d->frameobj)-bbEntityX(n->collider))<16.0) {
                                            if (Abs(bbEntityZ(d->frameobj)-bbEntityZ(n->collider))<16.0) {
                                                UseDoor(d, false);
                                            }
                                        }
                                    }
                                }
                            }
                            case 4: {
                                if (mainPlayer->currRoom->roomTemplate->disableDecals == false) {
                                    mainPlayer->camShake = 5.0;
                                    de = CreateDecal(DECAL_CRACKS, bbEntityX(n->collider), 0.01, bbEntityZ(n->collider), 90, bbRand(360), 0);
                                    de->size = 0.3;
                                    UpdateDecals();
                                    PlaySound2(LoadTempSound("SFX/General/BodyFall.ogg"));
                                    if (Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(n->collider),bbEntityZ(n->collider))<0.8) {
                                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.3,0.5);
                                    }
                                }
                            }
                        }
                    }

                    n->state2 = n->state2+timing->tickDuration;
                    if (n->state2>70) {
                        n->state = 3;
                        n->state2 = 0;
                    }
                } else {
                    n->currSpeed = CurveValue(n->speed*1.5, n->currSpeed, 10.0);
                    bbPointEntity(n->obj,mainPlayer->collider);
                    //angle = CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10);1.0/Max(n\currSpeed,0.0001))
                    bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj)-180, bbEntityYaw(n->collider), 10), 0);

                    AnimateNPC(n, 647, 683, n->currSpeed*25, false);
                    //Animate2(n\obj, AnimTime(n\obj), 647, 683, n\currSpeed*25, False)

                    bbMoveEntity(n->collider, 0,0,-n->currSpeed*timing->tickDuration);

                }
            }
        }
        case 3: {
            bbPointEntity(n->obj,mainPlayer->collider);
            //1.0/Max(n\currSpeed,0.0001))
            angle = CurveAngle(bbEntityYaw(n->obj)+n->angle-180, bbEntityYaw(n->collider), 10);
            bbRotateEntity(n->collider, 0, angle, 0);

            n->currSpeed = CurveValue(n->speed, n->currSpeed, 10.0);
            bbMoveEntity(n->collider, 0,0,n->currSpeed*timing->tickDuration);

            //Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25)

            if (bbRand(100)==1) {
                n->angle = bbRnd(-20,20);
            }

            n->state2 = n->state2 + timing->tickDuration;
            if (n->state2>250) {
                AnimateNPC(n, 684, 647, -n->currSpeed*25, false);
                //Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25, False)
                if (n->frame==647) {
                    n->state = 0;
                    n->state2 = 0;
                }
            } else {
                AnimateNPC(n, 684, 647, -n->currSpeed*25);

                //Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25)
            }

        }
    }

    if (n->state > 1) {
        if (n->sounds[0] == 0) {
            n->sounds[0] = bbLoadSound("SFX/SCP/066/Rolling.ogg");
        }
        if (n->soundChannels[0]!=0) {
            if (IsChannelPlaying(n->soundChannels[0])) {
                n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 20);
            }
        } else {
            n->soundChannels[0] = PlayRangedSound(n->sounds[0], mainPlayer->cam, n->collider, 20);
        }
    }

    //If (n\soundChannels[1]<>0) Then
    //	If (IsChannelPlaying(n\soundChannels[1])) Then
    //		n\soundChannels[1] = LoopRangedSound(n\sounds[1], n\soundChannels[1], mainPlayer\cam, n\collider, 20)
    //		mainPlayer\blurTimer = Max((5.0-dist)*300,0)
    //	EndIf
    //EndIf


    if (n->state3 > 0) {
        n->state3 = n->state3-timing->tickDuration;
        //LightVolume = TempLightVolume-TempLightVolume*Min(Max(n\state3/500,0.01),0.6)
        mainPlayer->heartbeatIntensity = Max(mainPlayer->heartbeatIntensity, 130);
        //HeartBeatVolume = Max(HeartBeatVolume,Min(n\state3/1000,1.0))
    }

    if (IsChannelPlaying(n->soundChannels[1])) {
        mainPlayer->blurTimer = Max((5.0-n->playerDistance)*300,0);
    }

    bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider) - 0.2, bbEntityZ(n->collider));

    bbRotateEntity(n->obj, bbEntityPitch(n->collider)-90, bbEntityYaw(n->collider), 0);
}

}

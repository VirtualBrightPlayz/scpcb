#include "NPCtypeApache.h"
#include "include.h"

namespace CBN {

// Functions.
void InitializeNPCtypeApache(NPC* n) {
    n->nvName = "Human";
    n->gravityMult = 0.0;
    n->maxGravity = 0.0;
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.2);

    NPC* n2;
    for (int iterator136 = 0; iterator136 < NPC::getListSize(); iterator136++) {
        n2 = NPC::getObject(iterator136);

        if (n->npcType == n2->npcType) & (n != n2) {
            n->obj = bbCopyEntity(n2->obj);
            n->obj2 = bbCopyEntity(n2->obj2);
            n->obj3 = bbCopyEntity(n2->obj3);
            break;
        }
    }

    if (n->obj == 0) {
        n->obj = bbLoadAnimMesh("GFX/NPCs/apache/apache.b3d");
        n->obj2 = bbLoadAnimMesh("GFX/NPCs/apache/apacherotor.b3d");
        n->obj3 = bbLoadAnimMesh("GFX/NPCs/apache/apacherotor2.b3d");
    }

    bbEntityParent(n->obj2, n->obj);
    bbEntityParent(n->obj3, n->obj);

    n->sounds[0] = bbLoadSound("SFX/Character/Apache/Propeller.ogg");
    n->sounds[1] = bbLoadSound("SFX/Character/Apache/Alarm.ogg");

    int i;
    int rotor2;
    for (i = -1; i <= 1; i += 2) {
        rotor2 = bbCopyEntity(n->obj2,n->obj2);
        bbRotateEntity(rotor2,0,4.0*i,0);
        bbEntityAlpha(rotor2, 0.5);
    }

    bbPositionEntity(n->obj3, 0.0, 2.15, -5.48);

    bbEntityType(n->collider, HIT_APACHE);
    bbEntityRadius(n->collider, 3.0);

    float temp = 0.6;
    bbScaleEntity(n->obj, temp, temp, temp);
}

void UpdateNPCtypeApache(NPC* n) {
    float dist2;
    float dist;
    int target;
    int pvt;

    if (n->playerDistance<60.0) {
        if (mainPlayer->currRoom->roomTemplate->name == "exit1") {
            dist2 = Max(Min(bbEntityDistance(n->collider, mainPlayer->currRoom->objects[3])/(8000.0*RoomScale),1.0),0.0);
        } else {
            dist2 = 1.0;
        }

        n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 25.0, dist2);
    }

    n->dropSpeed = 0;

    switch (n->state) {
        case 0,1: {
            bbTurnEntity(n->obj2,0,20.0*timing->tickDuration,0);
            bbTurnEntity(n->obj3,20.0*timing->tickDuration,0,0);

            if (n->state==1 & (!NoTarget)) {
                if (Abs(bbEntityX(mainPlayer->collider)-bbEntityX(n->collider))< 30.0) {
                    if (Abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(n->collider))<30.0) {
                        if (Abs(bbEntityY(mainPlayer->collider)-bbEntityY(n->collider))<20.0) {
                            if (bbRand(20)==1) {
                                if (bbEntityVisible(mainPlayer->collider, n->collider)) {
                                    n->state = 2;
                                    PlayRangedSound(n->sounds[1], mainPlayer->cam, n->collider, 50, 1.0);
                                }
                            }
                        }
                    }
                }
            }
            //player located -> attack
        }
        case 2,3: {

            if (n->state == 2) {
                target = mainPlayer->collider;
            } else if ((n->state == 3)) {
                target = bbCreatePivot();
                bbPositionEntity(target, n->enemyX, n->enemyY, n->enemyZ, true);
            }

            if (NoTarget & n->state == 2) {
                n->state = 1;
            }

            bbTurnEntity(n->obj2,0,20.0*timing->tickDuration,0);
            bbTurnEntity(n->obj3,20.0*timing->tickDuration,0,0);

            if (Abs(bbEntityX(target)-bbEntityX(n->collider)) < 55.0) {
                if (Abs(bbEntityZ(target)-bbEntityZ(n->collider)) < 55.0) {
                    if (Abs(bbEntityY(target)-bbEntityY(n->collider))< 20.0) {
                        bbPointEntity(n->obj, target);
                        bbRotateEntity(n->collider, CurveAngle(Min(WrapAngle(bbEntityPitch(n->obj)),40.0),bbEntityPitch(n->collider),40.0), CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),90.0), bbEntityRoll(n->collider), true);
                        bbPositionEntity(n->collider, bbEntityX(n->collider), CurveValue(bbEntityY(target)+8.0,bbEntityY(n->collider),70.0), bbEntityZ(n->collider));

                        dist = Distance(bbEntityX(target),bbEntityZ(target),bbEntityX(n->collider),bbEntityZ(n->collider));

                        n->currSpeed = CurveValue(Min(dist-6.5,6.5)*0.008, n->currSpeed, 50.0);

                        //If (Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(n\collider),EntityZ(n\collider)) > 6.5) Then
                        //	n\currspeed = CurveValue(0.08,n\currspeed,50.0)
                        //Else
                        //	n\currspeed = CurveValue(0.0,n\currspeed,30.0)
                        //EndIf
                        bbMoveEntity(n->collider, 0,0,n->currSpeed*timing->tickDuration);


                        if (n->pathTimer == 0) {
                            n->pathStatus = bbEntityVisible(n->collider,target);
                            n->pathTimer = bbRand(100,200);
                        } else {
                            n->pathTimer = Min(n->pathTimer-timing->tickDuration,0.0);
                        }

                        //player visible
                        if (n->pathStatus == 1) {
                            bbRotateEntity(n->collider, bbEntityPitch(n->collider), bbEntityYaw(n->collider), CurveAngle(0, bbEntityRoll(n->collider),40), true);

                            if (n->reload ==< 0) {
                                if (dist<20.0) {
                                    pvt = bbCreatePivot();

                                    bbPositionEntity(pvt, bbEntityX(n->collider),bbEntityY(n->collider), bbEntityZ(n->collider));
                                    bbRotateEntity(pvt, bbEntityPitch(n->collider), bbEntityYaw(n->collider),bbEntityRoll(n->collider));
                                    //2.3
                                    bbMoveEntity(pvt, 0, 8.87*(0.21/9.0), 8.87*(1.7/9.0));
                                    bbPointEntity(pvt, target);

                                    if (WrapAngle(bbEntityYaw(pvt)-bbEntityYaw(n->collider))<10) {
                                        PlayRangedSound_SM(sndManager->gunshot[1], mainPlayer->cam, n->collider, 20);

                                        DeathMSG = bbChr(34)+"CH-2 to control. Shot down a runaway Class D at Gate B."+bbChr(34);

                                        Shoot( EntityX(pvt),EntityY(pvt), EntityZ(pvt),((10/dist)*(1/dist))*(n->state = 2),(n->state==2));

                                        n->reload = 5;
                                    }

                                    bbFreeEntity(pvt);
                                }
                            }
                        } else {
                            bbRotateEntity(n->collider, bbEntityPitch(n->collider), bbEntityYaw(n->collider), CurveAngle(-20, bbEntityRoll(n->collider),40), true);
                        }
                        bbMoveEntity(n->collider, -bbEntityRoll(n->collider)*0.002,0,0);

                        n->reload = n->reload-timing->tickDuration;


                    }
                }
            }

            if (n->state == 3) {
                bbFreeEntity(target);
            }
            //crash
        }
        case 4: {
            if (n->state2 < 300) {

                bbTurnEntity(n->obj2,0,20.0*timing->tickDuration,0);
                bbTurnEntity(n->obj3,20.0*timing->tickDuration,0,0);

                //Sin(TimeInPosMilliSecs()/40)*timing\tickDuration)
                bbTurnEntity(n->collider,0,-timing->tickDuration*7,0);
                n->state2 = n->state2+timing->tickDuration*0.3;

                target = bbCreatePivot();
                bbPositionEntity(target, n->enemyX, n->enemyY, n->enemyZ, true);

                bbPointEntity(n->obj, target);
                bbMoveEntity(n->obj, 0,0,timing->tickDuration*0.001*n->state2);
                bbPositionEntity(n->collider, bbEntityX(n->obj), bbEntityY(n->obj), bbEntityZ(n->obj));

                if (bbEntityDistance(n->obj, target) <0.3) {
                    //TODO:
                    //                    If (TempSound2 <> 0) Then FreeSound(TempSound2
                    TempSound2 = 0);
                    //                    TempSound2 = LoadSound("SFX/Character/Apache/Crash"+Rand(1,2)+".ogg")
                    //                    mainPlayer\camShake = Max(mainPlayer\camShake, 3.0)
                    //                    PlaySound2(TempSound2)
                    //                    n\state = 5
                }

                bbFreeEntity(target);
            }
        }
    }

    bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider), bbEntityZ(n->collider));
    bbRotateEntity(n->obj, bbEntityPitch(n->collider), bbEntityYaw(n->collider), bbEntityRoll(n->collider), true);
}

}

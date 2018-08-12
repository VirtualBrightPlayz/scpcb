#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "NPCs.h"
#include "../INI.h"
#include "../GameMain.h"
#include "../Events.h"
#include "../Menus/Menu.h"
#include "../Audio.h"
#include "../MapSystem.h"
#include "../Player.h"
#include "../MathUtils/MathUtils.h"
#include "../Difficulty.h"
#include "../Objects.h"
#include "../Doors.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Options.h"
#include "NPCtype096.h"

namespace CBN {

// Functions.
void InitializeNPCtype096(NPC* n) {
    n->nvName = "SCP-096";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.3f);
    bbEntityType(n->collider, HIT_PLAYER);
    n->obj = bbLoadAnimMesh("GFX/NPCs/scp096/scp096.b3d");
    n->sounds[2] = bbLoadSound("SFX/Door/DoorOpenFast.ogg");

    n->speed = (GetINIFloat("Data/NPCs.ini", "SCP-096", "speed") / 100.f);

    float temp = (GetINIFloat("Data/NPCs.ini", "SCP-096", "scale") / 3.f);
    bbScaleEntity(n->obj, temp, temp, temp);

    bbMeshCullBox(n->obj, -bbMeshWidth(n->obj)*2, -bbMeshHeight(n->obj)*2, -bbMeshDepth(n->obj)*2, bbMeshWidth(n->obj)*2, bbMeshHeight(n->obj)*4, bbMeshDepth(n->obj)*4);
}

void UpdateNPCtype096(NPC* n) {
    float angle;
    int i;
    Pivot* pvt;
    Decal* de;
    float dist2;

    switch ((int)n->state) {
        case 0: {
            if (n->playerDistance < 8.f) {
                if (n->sounds[0] == 0) {
                    n->sounds[0] = bbLoadSound("SFX/Music/096.ogg");
                } else {
                    n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 8.f, 1.f);
                }

                //sitting
                AnimateNPC(n, 1085,1412, 0.1f);
                //Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.1f) ;sitting

                //-EntityYaw(n\collider,True))
                angle = WrapAngle(bbDeltaYaw(n->collider, mainPlayer->collider));

                if (!NoTarget) {
                    if (angle<90 | angle>270) {
                        bbCameraProject(mainPlayer->cam,bbEntityX(n->collider), bbEntityY(n->collider)+0.25f, bbEntityZ(n->collider));

                        if (bbProjectedX()>0 && bbProjectedX()<userOptions->screenWidth) {
                            if (bbProjectedY()>0 && bbProjectedY()<userOptions->screenHeight) {
                                if (bbEntityVisible(mainPlayer->collider, n->collider)) {
                                    if (mainPlayer->blinkTimer < - 16 | mainPlayer->blinkTimer > - 6) {
                                        PlaySound2(LoadTempSound("SFX/SCP/096/Triggered.ogg"));

                                        mainPlayer->camZoom = 10;

                                        n->frame = 307;
                                        //SetAnimTime(n\obj, 307)
                                        bbStopChannel(n->soundChannels[0]);
                                        bbFreeSound(n->sounds[0]);
                                        n->sounds[0] = 0;
                                        n->state = 1;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
        case 4: {
            mainPlayer->camZoom = CurveValue(Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.f)+1.f) * 10.f),mainPlayer->camZoom,8.f);

            if (n->target == nullptr) {
                if (n->sounds[0] == 0) {
                    n->sounds[0] = bbLoadSound("SFX/SCP/096/Scream.ogg");
                } else {
                    n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 7.5f, 1.f);
                }

                if (n->sounds[1] == 0) {
                    n->sounds[1] = bbLoadSound("SFX/Music/096Chase.ogg");
                } else {
                    if (n->soundChannels[1] == 0) {
                        n->soundChannels[1] = bbPlaySound(n->sounds[1]);
                    } else {
                        if (!bbChannelPlaying(n->soundChannels[1])) {
                            n->soundChannels[1] = bbPlaySound(n->sounds[1]);
                        }
                        bbChannelVolume(n->soundChannels[1], Min(Max(8.f-n->playerDistance,0.6f),1.f)*userOptions->sndVolume);
                    }
                }
            }

            if (NoTarget && n->target == nullptr) {
                n->state = 5;
            }

            if (!mainPlayer->dead) {

                if (TimeInPosMilliSecs() > n->state3) {
                    n->lastSeen = 0;
                    if (n->target==nullptr) {
                        if (bbEntityVisible(mainPlayer->collider, n->collider)) {
                            n->lastSeen = 1;
                        }
                    } else {
                        if (bbEntityVisible(n->target->collider, n->collider)) {
                            n->lastSeen = 1;
                        }
                    }
                    n->state3 = TimeInPosMilliSecs()+3000;
                }

                if (n->lastSeen==1) {
                    n->pathTimer = Max(70*3, n->pathTimer);
                    n->pathStatus = 0;

                    if (n->playerDistance < 2.8f | n->frame<150) {
                        //go to the start of the jump animation
                        if (n->frame>193) {
                            n->frame = 2.f;
                        }

                        AnimateNPC(n, 2, 193, 0.7f);
                        //Animate2(n\obj, AnimTime(n\obj), 2, 193, 0.7f)

                        if (n->playerDistance > 1.f) {
                            n->currSpeed = CurveValue(n->speed*2.f,n->currSpeed,15.f);
                        } else {
                            n->currSpeed = 0;

                            if (n->target==nullptr) {
                                if (!mainPlayer->godMode) {
                                    PlaySound2(mainPlayer->damageSFX[4]);

                                    pvt = bbCreatePivot();
                                    mainPlayer->camShake = 30;
                                    mainPlayer->blurTimer = 2000;
                                    DeathMSG = "A large amount of blood found in [DATA REDACTED]. DNA indentified as Subject D-9341. Most likely [DATA REDACTED] by SCP-096.";
                                    Kill(mainPlayer);
                                    //KillAnim = 1 ;TODO: idk
                                    for (i = 0; i <= 6; i++) {
                                        bbPositionEntity(pvt, bbEntityX(mainPlayer->collider)+bbRnd(-0.1f,0.1f),bbEntityY(mainPlayer->collider)-0.05f,bbEntityZ(mainPlayer->collider)+bbRnd(-0.1f,0.1f));
                                        bbTurnEntity(pvt, 90, 0, 0);
                                        bbEntityPick(pvt,0.3f);

                                        de = CreateDecal(bbRand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), bbPickedX(), bbPickedY()+0.005f, bbPickedZ(), 90, bbRand(360), 0);
                                        de->size = bbRnd(0.2f,0.6f);
                                        bbEntityAlpha(de->obj, 1.f);
                                        bbScaleSprite(de->obj, de->size, de->size);
                                    }
                                    bbFreeEntity(pvt);
                                }
                            }
                        }

                        if (n->target==nullptr) {
                            bbPointEntity(n->collider, mainPlayer->collider);
                        } else {
                            bbPointEntity(n->collider, n->target->collider);
                        }

                    } else {
                        if (n->target==nullptr) {
                            bbPointEntity(n->obj, mainPlayer->collider);
                        } else {
                            bbPointEntity(n->obj, n->target->collider);
                        }

                        bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), 5.f), 0);

                        if (n->frame>1000) {
                            n->currSpeed = CurveValue(n->speed,n->currSpeed,20.f);
                        }

                        if (n->frame<1058) {
                            AnimateNPC(n, 892, 1058, n->speed*5, false);
                            //Animate2(n\obj, AnimTime(n\obj),892,1058, n\speed*5, False)
                        } else {
                            AnimateNPC(n, 1059, 1074, n->currSpeed*5);
                            //Animate2(n\obj, AnimTime(n\obj),1059,1074, n\currSpeed*5)
                        }
                    }

                    bbRotateEntity(n->collider, 0, bbEntityYaw(n->collider), 0, true);
                    bbMoveEntity(n->collider, 0,0,n->currSpeed);

                } else {
                    if (n->pathStatus == 1) {

                        if (n->path[n->pathLocation]==nullptr) {
                            if (n->pathLocation > 19) {
                                n->pathLocation = 0;
                                n->pathStatus = 0;
                            } else {
                                n->pathLocation = n->pathLocation + 1;
                            }
                        } else {
                            bbPointEntity(n->obj, n->path[n->pathLocation]->obj);

                            bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), 5.f), 0);

                            if (n->frame>1000) {
                                n->currSpeed = CurveValue(n->speed*1.5f,n->currSpeed,15.f);
                            }
                            bbMoveEntity(n->collider, 0,0,n->currSpeed);

                            if (n->frame<1058) {
                                AnimateNPC(n, 892,1058, n->speed*8, false);
                                //Animate2(n\obj, AnimTime(n\obj),892,1058, n\speed*8, False)
                            } else {
                                AnimateNPC(n, 1059,1084, n->currSpeed*8);
                                //Animate2(n\obj, AnimTime(n\obj),1059,1084, n\currSpeed*8)
                            }

                            dist2 = bbEntityDistance(n->collider,n->path[n->pathLocation]->obj);
                            if (dist2 < 0.4f) {
                                //TODO: fix
                                //                                If (n\path[n\pathLocation]\door <> Null) Then
                                //									If (n\path[n\pathLocation]\door\open = False) Then
                                //										n\path[n\pathLocation]\door\open = True
                                //                                        n\path[n\pathLocation]\door\fastopen = 1
                                //                                        PlayRangedSound(n\sounds[2], mainPlayer\cam, n\path[n\pathLocation]\door\obj)
                                //                                    EndIf
                                //                                EndIf
                                if (dist2 < 0.2f) {
                                    n->pathLocation = n->pathLocation + 1;
                                }

                            }
                        }

                    } else {
                        AnimateNPC(n, 892,972, 0.2f);
                        //Animate2(n\obj, AnimTime(n\obj),892,972, 0.2f)

                        n->pathTimer = Max(0, n->pathTimer-timing->tickDuration);
                        if (n->pathTimer<=0) {
                            if (n->target!=nullptr) {
                                n->pathStatus = FindPath(n, bbEntityX(n->target->collider),bbEntityY(n->target->collider)+0.2f,bbEntityZ(n->target->collider));
                            } else {
                                n->pathStatus = FindPath(n, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
                            }
                            n->pathTimer = 70*5;
                        }
                    }
                }

                if (n->playerDistance > 32.f | bbEntityY(n->collider)<-50) {
                    if (bbRand(50)==1) {
                        TeleportCloser(n);
                    }
                }
                //play the eating animation if killtimer < 0
            } else {
                AnimateNPC(n, Min(27,bbAnimTime(n->obj)), 193, 0.5f);

                //Animate2(n\obj, AnimTime(n\obj), Min(27,AnimTime(n\obj)), 193, 0.5f)
            }


        }
        case 1:
        case 2:
        case 3: {
            if (n->sounds[0] == 0) {
                n->sounds[0] = bbLoadSound("SFX/Music/096Angered.ogg");
            } else {
                n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 10.f, 1.f);
            }

            // get up
            if (n->state==1) {
                if (n->frame>1085) {

                    AnimateNPC(n, 1085, 1412, 0.3f,false);
                    if (n->frame> 1411.9f) {
                        n->frame = 307;
                    }

                    //Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.3f,False)
                    //If (AnimTime(n\obj)=1412) Then SetAnimTime(n\obj, 307)
                } else {
                    AnimateNPC(n, 307, 424, 0.3f, false);
                    if (n->frame > 423.9f) {
                        n->state = 2;
                        n->frame = 892;
                    }

                    //Animate2(n\obj, AnimTime(n\obj),307,424, 0.3f, False)
                    //If (AnimTime(n\obj)=424) Then n\state = 2
                    bbSetAnimTime(n->obj, 892);
                }
            } else if ((n->state==2)) {
                AnimateNPC(n, 833, 972, 0.3f, false);

                //Animate2(n\obj, AnimTime(n\obj),833,972, 0.3f, False)
                if (n->frame>=972) {
                    n->state = 3;
                    n->state2 = 0;
                }
            } else if ((n->state==3)) {
                n->state2 = n->state2+timing->tickDuration;
                if (n->state2 > 70*18) {
                    AnimateNPC(n, 973, 1001, 0.5f, false);
                    //Animate2(n\obj, AnimTime(n\obj),973,1001, 0.5f, False)
                    if (n->frame>1000.9f) {
                        n->state = 4;
                        bbStopChannel(n->soundChannels[0]);
                        bbFreeSound(n->sounds[0]);
                        n->sounds[0] = 0;
                    }
                } else {
                    AnimateNPC(n, 892,978, 0.3f);
                    //Animate2(n\obj, AnimTime(n\obj),892,978, 0.3f)
                }
            }
        }
        case 5: {
            if (n->playerDistance < 16.f) {

                if (n->sounds[0] == 0) {
                    n->sounds[0] = bbLoadSound("SFX/Music/096.ogg");
                } else {
                    n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 14.f, 1.f);
                }

                n->state2 = n->state2+timing->tickDuration;
                //walking around
                if (n->state2>1000) {
                    if (n->state2>1600) {
                        //: SetAnimTime(n\obj, 1652)
                        n->state2 = bbRand(0,500);
                        n->frame = 1652;
                    }

                    //idle to walk
                    if (n->frame<1652) {
                        n->currSpeed = CurveValue(n->speed*0.1f,n->currSpeed,5.f);
                        AnimateNPC(n, 1638,1652, n->currSpeed*45,false);
                        //Animate2(n\obj, AnimTime(n\obj),1638,1652, n\currSpeed*45,False)
                    } else {
                        n->currSpeed = CurveValue(n->speed*0.1f,n->currSpeed,5.f);
                        //walk
                        AnimateNPC(n, 1653,1724, n->currSpeed*45);
                        //Animate2(n\obj, AnimTime(n\obj),1653,1724, n\currSpeed*45) ;walk
                    }

                    if (TimeInPosMilliSecs() > n->state3) {
                        n->lastSeen = 0;
                        if (bbEntityVisible(mainPlayer->collider, n->collider)) {
                            n->lastSeen = 1;
                        } else {
                            bbHideEntity(n->collider);
                            bbEntityPick(n->collider, 1.5f);
                            if (bbPickedEntity() != 0) {
                                n->angle = bbEntityYaw(n->collider)+bbRnd(80,110);
                            }
                            bbShowEntity(n->collider);
                        }
                        n->state3 = TimeInPosMilliSecs()+3000;
                    }

                    if (n->lastSeen) {
                        bbPointEntity(n->obj, mainPlayer->collider);
                        bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),130.f),0);
                        if (n->playerDistance < 1.5f) {
                            n->state2 = 0;
                        }
                    } else {
                        bbRotateEntity(n->collider, 0, CurveAngle(n->angle,bbEntityYaw(n->collider),50.f),0);
                    }
                } else {
                    //walk to idle
                    if (n->frame>1638) {
                        n->currSpeed = CurveValue(n->speed*0.05f,n->currSpeed,8.f);
                        AnimateNPC(n, 1652, 1638, -n->currSpeed*45,false);
                        //Animate2(n\obj, AnimTime(n\obj),1652,1638, -n\currSpeed*45,False)

                        //idle
                    } else {
                        n->currSpeed = CurveValue(0,n->currSpeed,4.f);
                        //idle
                        AnimateNPC(n, 585, 633, 0.2f);
                        //Animate2(n\obj, AnimTime(n\obj),585,633, 0.2f) ;idle
                    }
                }

                //-EntityYaw(n\collider))
                angle = WrapAngle(bbDeltaYaw(n->collider, mainPlayer->cam));
                if (!NoTarget) {
                    if (angle<55 | angle>360-55) {
                        bbCameraProject(mainPlayer->cam,bbEntityX(n->collider), bbEntityY(mainPlayer->collider)+5.8f*0.2f-0.25f, bbEntityZ(n->collider));

                        if (bbProjectedX()>0 && bbProjectedX()<userOptions->screenWidth) {
                            if (bbProjectedY()>0 && bbProjectedY()<userOptions->screenHeight) {
                                if (bbEntityVisible(mainPlayer->collider, n->collider)) {
                                    if (mainPlayer->blinkTimer < - 16 | mainPlayer->blinkTimer > - 6) {
                                        PlaySound2(LoadTempSound("SFX/SCP/096/Triggered.ogg"));

                                        mainPlayer->camZoom = 10;

                                        n->frame = 833;
                                        //SetAnimTime(n\obj, 833)
                                        bbStopChannel(n->soundChannels[0]);
                                        bbFreeSound(n->sounds[0]);
                                        n->sounds[0] = 0;
                                        n->state = 2;
                                    }
                                }
                            }
                        }

                    }
                }

                bbMoveEntity(n->collider, 0,0,n->currSpeed);
            }
        }
    }

    //ResetEntity(n\collider)
    bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider)-0.07f, bbEntityZ(n->collider));

    bbRotateEntity(n->obj, bbEntityPitch(n->collider), bbEntityYaw(n->collider), 0);
}

}

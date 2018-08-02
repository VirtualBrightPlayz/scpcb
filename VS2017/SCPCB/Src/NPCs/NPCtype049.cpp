#include "NPCtype049.h"
#include "include.h"

namespace CBN {

// Constants.
const int STATE049_IDLE = 0;
const int STATE049_ATTACK = 1;
const int STATE049_ROAMING = 2;

// Functions.
void InitializeNPCtype049(NPC* n) {
    n->nvName = "SCP-049";
    n->collider = bbCreatePivot();

    bbEntityRadius(n->collider, 0.2);
    bbEntityType(n->collider, HIT_PLAYER);

    n->obj = bbLoadAnimMesh("GFX/NPCs/scp049/scp-049.b3d");

    n->speed = GetINIFloat("Data/NPCs.ini", "SCP-049", "speed") / 100.0;

    float temp = GetINIFloat("Data/NPCs.ini", "SCP-049", "scale");
    bbScaleEntity(n->obj, temp, temp, temp);

    n->sounds[0] = bbLoadSound("SFX/SCP/049/Catch.ogg");
    n->sounds[1] = bbLoadSound("SFX/SCP/049/Spotted1.ogg");
    n->sounds[2] = bbLoadSound("SFX/SCP/049/Spotted2.ogg");
    n->sounds[3] = bbLoadSound("SFX/SCP/049/Detected1.ogg");
    n->sounds[4] = bbLoadSound("SFX/SCP/049/Detected2.ogg");
    n->sounds[5] = bbLoadSound("SFX/SCP/049/Detected3.ogg");
    n->sounds[6] = bbLoadSound("SFX/SCP/049/Detected4.ogg");

    //If (HorrorSFX(13)=0) Then HorrorSFX(13)=LoadSound("SFX/Music/049Chase.ogg") ;TODO: implement
}

void UpdateNPCtype049(NPC* n) {
    float prevFrame = n->frame;
    Room* r;
    Event* e;
    int npcDetectsPlayer;
    float dist2;
    int temp;

    if (n->state != STATE049_IDLE) {
        //Depending on whether 049 has detected the player, choose the state it's in.
        npcDetectsPlayer = MeNPCSeesPlayer(n);

        if (npcDetectsPlayer == 1) {
            n->state = STATE049_ATTACK;
        } else {
            n->state = STATE049_ROAMING;
        }
    }

    switch (n->state) {
        case STATE049_IDLE: {
            //Idle animation.
            AnimateNPC(n, 231, 344, 0.31, true);

        }
        case STATE049_ATTACK: {
            //[Block]
            //Playing a sound after detecting the player
            if (n->prevState == STATE049_ROAMING & (!IsChannelPlaying(n->soundChannels[0]))) {
                n->soundChannels[0] = LoopRangedSound(n->sounds[bbRand(1,2)], n->soundChannels[0], mainPlayer->cam, n->obj);
            }

            n->pathStatus = 0;
            n->pathTimer = 0.0;
            n->pathLocation = 0;

            bbPointEntity(n->obj, mainPlayer->collider);
            bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), 10.0), 0);

            //Now I must only T O U C H...
            if (n->playerDistance < 0.5) {
                mainPlayer->camZoom = 20.0;
                mainPlayer->blurTimer = 500.0;

                if (!mainPlayer->godMode) {
                    if (mainPlayer->currRoom->roomTemplate->name == "room049") {
                        DeathMSG = "Three (3) active instances of SCP-049-2 discovered in the tunnel outside SCP-049's containment chamber. Terminated by Nine-Tailed Fox.";

                        for (int iterator121 = 0; iterator121 < Event::getListSize(); iterator121++) {
                            e = Event::getObject(iterator121);

                            if (e->name == "room049") {
                                e->eventState = -1;
                                Exit;
                            }
                        }
                    } else {
                        DeathMSG = "An active instance of SCP-049-2 was discovered in [REDACTED]. Terminated by Nine-Tailed Fox.";
                        Kill(mainPlayer);
                    }

                    NPCStopAllChannels(n);
                    PlaySound2(n->sounds[0]);

                    n->state = STATE049_IDLE;
                }
                // S T O P P ;chasing
            } else {
                n->currSpeed = CurveValue(n->speed, n->currSpeed, 20.0);
                bbMoveEntity(n->collider, 0, 0, n->currSpeed * timing->tickDuration);

                //extends arm
                if (n->playerDistance < 3.0) {
                    AnimateNPC(n, Max(Min(bbAnimTime(n->obj),428.0),387), 463.0, n->currSpeed*38);
                } else {
                    if (n->frame>428.0) {
                        AnimateNPC(n, Min(bbAnimTime(n->obj),463.0), 498.0, n->currSpeed*38,false);
                        if (n->frame>497.9) {
                            n->frame = 358;
                        }
                    } else {
                        AnimateNPC(n, Max(Min(bbAnimTime(n->obj),358.0),346), 393.0, n->currSpeed*38);
                    }
                }
            }
            //[End Block]

        }
        case STATE049_ROAMING: {
            //[Block]
            //Finding a path to the player
            if (npcDetectsPlayer == 2) {
                n->lastSeen = 70*15;
            }

            if (n->lastSeen > 0) {
                //Path to player found
                if (n->pathStatus == 1) {
                    if (n->path[n->pathLocation]==nullptr) {
                        if (n->pathLocation > 19) {
                            n->pathLocation = 0;
                            n->pathStatus = 0;
                        } else {
                            n->pathLocation = n->pathLocation + 1;
                        }
                    } else {
                        //closes doors behind him
                        if (n->pathLocation>0) {
                            if (n->path[n->pathLocation-1] != nullptr) {
                                //TODO: fix
                                //If (n\path[n\pathLocation-1]\door <> Null) Then
                                //	If (Not n\path[n\pathLocation-1]\door\isElevatorDoor)
                                //		If EntityDistance(n\path[n\pathLocation-1]\obj,n\collider)>0.3
                                //			If (n\path[n\pathLocation-1]\door\open) Then UseDoor(n\path[n\pathLocation-1]\door, False)
                                //		EndIf
                                //	EndIf
                                //EndIf
                            }
                        }

                        n->currSpeed = CurveValue(n->speed, n->currSpeed, 20.0);
                        bbPointEntity(n->obj,n->path[n->pathLocation]->obj);
                        bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),10.0),0);
                        bbMoveEntity(n->collider,0,0,n->currSpeed*timing->tickDuration);

                        //opens doors in front of him
                        dist2 = bbEntityDistance(n->collider,n->path[n->pathLocation]->obj);
                        if ((dist2 < 0.6)) {
                            temp = true;
                            //TODO: fix
                            //If (n\path[n\pathLocation]\door <> Null) Then
                            //	If (Not n\path[n\pathLocation]\door\isElevatorDoor)
                            //		If (n\path[n\pathLocation]\door\locked Or n\path[n\pathLocation]\door\keyCard>0 Or n\path[n\pathLocation]\door\code<>"") Then
                            //			temp = False
                            //			;n\currSpeed = 0
                            //		Else
                            //			If (n\path[n\pathLocation]\door\open = False) Then UseDoor(n\path[n\pathLocation]\door, False)
                            //		EndIf
                            //	EndIf
                            //EndIf
                            if (dist2<0.2 & temp) {
                                n->pathLocation = n->pathLocation + 1;
                            } else if ((dist2<0.5 & (!temp))) {
                                //Breaking up the path because the door cannot be operated by SCP-049
                                n->pathStatus = 0;
                                n->pathTimer = 0.0;
                            }
                        }

                        AnimateNPC(n, Max(Min(bbAnimTime(n->obj),358.0),346), 393.0, n->currSpeed*38);

                        //Playing a sound if he hears the player
                        if (n->soundTimer < 0 & (!IsChannelPlaying(n->soundChannels[0]))) {
                            n->soundTimer = bbRand(10, 20) * 70;

                            if (bbRand(8)==3) {
                                n->soundChannels[0] = LoopRangedSound(n->sounds[6], n->soundChannels[0], mainPlayer->cam, n->obj);
                            } else {
                                n->soundChannels[0] = LoopRangedSound(n->sounds[bbRand(3, 5)], n->soundChannels[0], mainPlayer->cam, n->obj);
                            }
                        }

                        //Updating the path every 7 seconds
                        if (n->timer > 70*7) {
                            n->pathStatus = FindPath(n, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
                            n->timer = 0.0;
                        }
                    }

                    if (n->currElevator != nullptr) {
                        dist2 = bbEntityDistance(n->collider,n->currElevator->door->frameobj);
                        if (dist2 < 0.7) {
                            n->pathStatus = 0;
                            n->pathLocation = 0;
                            n->pathTimer = 0.0;
                        }
                    }
                    //No Path to the player found - stands still and tries to find a path
                } else {
                    if (n->currElevator == nullptr) {
                        //Updating path faster
                        if (n->pathStatus == 2) {
                            n->pathTimer = n->pathTimer + 2*timing->tickDuration;
                        } else {
                            n->pathTimer = n->pathTimer + timing->tickDuration;
                        }
                        if (n->pathTimer > 70*5-(2*SelectedDifficulty->aggressiveNPCs)) {
                            if (n->inFacility == InFacility) {
                                n->pathStatus = FindPath(n, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
                            } else {
                                FindNextElevator(n);
                            }
                            n->pathTimer = 0.0;
                        }
                        AnimateNPC(n, 269, 345, 0.2);
                        //n\prevState = 0
                    } else {
                        //n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
                        n->currSpeed = n->speed;
                        GoToElevator(n);

                        if (n->currSpeed > 0.0) {
                            bbMoveEntity(n->collider,0,0,n->currSpeed*timing->tickDuration);
                            AnimateNPC(n, Max(Min(bbAnimTime(n->obj),358.0),346), 393.0, n->currSpeed*38);
                        } else {
                            AnimateNPC(n, 269, 345, 0.2);
                        }
                    }
                }
                //Player hasn't been detected, just being idle
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
                        //closes doors behind him
                        if (n->pathLocation>0) {
                            if (n->path[n->pathLocation-1] != nullptr) {
                                //TODO: ffs
                                //If (n\path[n\pathLocation-1]\door <> Null) Then
                                //	If (Not n\path[n\pathLocation-1]\door\isElevatorDoor)
                                //		If EntityDistance(n\path[n\pathLocation-1]\obj,n\collider)>0.3
                                //			If (n\path[n\pathLocation-1]\door\open) Then UseDoor(n\path[n\pathLocation-1]\door, False)
                                //		EndIf
                                //	EndIf
                                //endIf
                            }
                        }

                        n->currSpeed = CurveValue(n->speed, n->currSpeed, 20.0);
                        bbPointEntity(n->obj,n->path[n->pathLocation]->obj);
                        bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),10.0),0);
                        bbMoveEntity(n->collider,0,0,n->currSpeed*timing->tickDuration);

                        //opens doors in front of him
                        dist2 = bbEntityDistance(n->collider,n->path[n->pathLocation]->obj);
                        if (dist2 < 0.6) {
                            temp = true;
                            //TODO: AAAAAHHHHH
                            //If (n\path[n\pathLocation]\door <> Null) Then
                            //	If (Not n\path[n\pathLocation]\door\isElevatorDoor)
                            //		If (n\path[n\pathLocation]\door\locked Or n\path[n\pathLocation]\door\keyCard>0 Or n\path[n\pathLocation]\door\code<>"") Then
                            //			temp = False
                            //			;n\currSpeed = 0
                            //		Else
                            //			If (n\path[n\pathLocation]\door\open = False) Then UseDoor(n\path[n\pathLocation]\door, False)
                            //		EndIf
                            //	EndIf
                            //EndIf
                            if (dist2<0.2 & temp) {
                                n->pathLocation = n->pathLocation + 1;
                            } else if ((dist2<0.5 & (!temp))) {
                                //Breaking up the path because the door cannot be operated by SCP-049
                                n->pathStatus = 0;
                                n->pathTimer = 0.0;
                            }
                        }

                        AnimateNPC(n, Max(Min(bbAnimTime(n->obj),358.0),346), 393.0, n->currSpeed*38);

                        n->timer = n->timer + timing->tickDuration;

                        if (n->inFacility == true) {
                            //Breaking the path after 14 seconds
                            if (n->state3 > 70*14) {
                                n->pathStatus = 0;
                                n->pathLocation = 0;
                                n->pathTimer = 0.0;
                                n->state3 = 0.0;
                            }
                        }
                    }

                    if (n->currElevator != nullptr) {
                        dist2 = bbEntityDistance(n->collider,n->currElevator->door->frameobj);
                        if (dist2 < 0.7) {
                            n->pathStatus = 0;
                            n->pathLocation = 0;
                            n->pathTimer = 0.0;
                        }
                    }
                } else {
                    if (n->currElevator == nullptr) {
                        n->currSpeed = 0;
                        AnimateNPC(n, 269, 345, 0.2);
                        if (n->pathTimer < 300+bbRand(100,300)) {
                            n->pathTimer = n->pathTimer + timing->tickDuration;
                        } else {
                            if (n->inFacility == true) {
                                if (bbRand(2)==1) {
                                    for (int iterator122 = 0; iterator122 < Room::getListSize(); iterator122++) {
                                        r = Room::getObject(iterator122);

                                        if (bbEntityDistance(r->obj,n->collider)<12.0 & bbEntityDistance(r->obj,n->collider)>4.0) {
                                            n->pathStatus = FindPath(n,bbEntityX(r->obj),bbEntityY(r->obj),bbEntityZ(r->obj));
                                            Exit;
                                        }
                                    }
                                }
                            } else {
                                FindNextElevator(n);
                            }
                            n->pathTimer = 0.0;
                        }
                    } else {
                        //n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
                        n->currSpeed = n->speed;
                        GoToElevator(n);

                        if (n->currSpeed > 0.0) {
                            bbMoveEntity(n->collider, 0, 0, n->currSpeed*timing->tickDuration);
                            AnimateNPC(n, Max(Min(bbAnimTime(n->obj),358.0),346), 393.0, n->currSpeed*38);
                        } else {
                            AnimateNPC(n, 269, 345, 0.2);
                        }
                    }
                }
            }
            //[End Block]
        }
    }

    bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider)-0.22, bbEntityZ(n->collider));

    bbRotateEntity(n->obj, 0, bbEntityYaw(n->collider), 0);

    n->lastSeen = Int(Max(n->lastSeen-timing->tickDuration,0));

    if (n->soundTimer >= 0) {
        n->soundTimer = n->soundTimer - timing->tickDuration;
    }

    n->prevState = Int(n->state);
}

}

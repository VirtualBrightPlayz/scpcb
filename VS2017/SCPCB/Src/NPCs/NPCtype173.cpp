#include "NPCtype173.h"
#include "include.h"

namespace CBN {

// Constants.
const int STATE173_IDLE = 0;
const int STATE173_ATTACK = 1;
const int STATE173_MOVE_TO_TARGET = 2;
const int STATE173_BEING_CONTAINED = 3;
const int STATE173_CONTAINED = 4;

// Functions.
void InitializeNPCtype173(NPC* n) {
    n->nvName = "SCP-173";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.32);
    bbEntityType(n->collider, HIT_PLAYER);

    //TODO: Remove this?
    n->gravity = true;

    n->obj = bbLoadMesh("GFX/NPCs/scp173/173.b3d");

    //On Halloween set jack-o-latern texture.
    int texFestive;
    if (bbCurrentDate().substr(0, 6).equals("31 Oct")) {
        HalloweenTex = true;
        texFestive = bbLoadTexture("GFX/NPCs/scp173/173h.png", 1);
        bbEntityTexture(n->obj, texFestive, 0, 0);
        bbFreeTexture(texFestive);
    }

    float temp = (GetINIFloat("Data/NPCs.ini", "SCP-173", "scale") / bbMeshDepth(n->obj));
    bbScaleEntity(n->obj, temp, temp, temp);

    n->speed = (GetINIFloat("Data/NPCs.ini", "SCP-173", "speed") / 100.0);

    n->obj2 = bbLoadMesh("GFX/NPCs/scp173/173box.b3d");
    bbScaleEntity(n->obj2, RoomScale, RoomScale, RoomScale);
    bbHideEntity(n->obj2);

    n->sounds[0] = bbLoadSound("SFX/SCP/173/StoneDrag.ogg");

    n->sounds[1] = bbLoadSound("SFX/SCP/173/Spotted1.ogg");
    n->sounds[2] = bbLoadSound("SFX/SCP/173/Spotted2.ogg");
    n->sounds[3] = bbLoadSound("SFX/SCP/173/Spotted3.ogg");

    n->sounds[4] = bbLoadSound("SFX/SCP/173/BigReveal1.ogg");
    n->sounds[5] = bbLoadSound("SFX/SCP/173/BigReveal2.ogg");
    n->sounds[6] = bbLoadSound("SFX/SCP/173/BigReveal3.ogg");
    n->sounds[7] = bbLoadSound("SFX/SCP/173/BigReveal4.ogg");
    n->sounds[8] = bbLoadSound("SFX/SCP/173/BigReveal5.ogg");

    n->sounds[9] = bbLoadSound("SFX/SCP/173/NeckSnap1.ogg");
    n->sounds[10] = bbLoadSound("SFX/SCP/173/NeckSnap2.ogg");
    n->sounds[11] = bbLoadSound("SFX/SCP/173/NeckSnap3.ogg");

    n->state = STATE173_ATTACK;
}

void UpdateNPCtype173(NPC* n) {
    float x;
    float z;
    int i;
    int tmp;
    WayPoint* w;
    Door* d;
    int playerVisible;
    int canMove;

    if (n->state == STATE173_IDLE | n->state == STATE173_CONTAINED) {
        return;
    }

    float dist;
    switch (n->state) {
        case STATE173_ATTACK: {
            dist = bbEntityDistance(n->collider, mainPlayer->collider);
            bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider) - 0.32, bbEntityZ(n->collider));
            bbRotateEntity(n->obj, 0, bbEntityYaw(n->collider) - 180, 0);

            playerVisible = false;
            canMove = true;
            if (dist < 15) {
                if (dist < 10.0) {
                    if (bbEntityVisible(n->collider, mainPlayer->collider)) {
                        playerVisible = true;
                        n->targetX = bbEntityX(mainPlayer->collider, true);
                        n->targetY = bbEntityY(mainPlayer->collider, true);
                        n->targetZ = bbEntityZ(mainPlayer->collider, true);
                    }
                }

                n->prevX = bbEntityX(n->collider);
                n->prevZ = bbEntityZ(n->collider);

                if (mainPlayer->blinkTimer < - 16 | mainPlayer->blinkTimer > - 6) {
                    if (bbEntityInView(n->obj, mainPlayer->cam)) {
                        canMove = false;
                    }
                }
            }

            if (NoTarget) {
                canMove = true;
            }

            //player is looking at it -> doesn't move
            if (!canMove) {
                //Blur and zoom camera slightly when looking at 173.
                mainPlayer->blurTimer = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), mainPlayer->blurTimer);
                mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0));

                //If it's close spoopy horror sound.
                if (dist < 3.5 && TimeInPosMilliSecs() - n->lastSeen > 60000 && playerVisible) {
                    PlaySound2(n->sounds[bbRand(1,3)]);

                    n->lastSeen = TimeInPosMilliSecs();
                }

                if (dist < 1.5 && n->lastDist > 2.0 && playerVisible) {
                    mainPlayer->camZoom = 40.0;
                    mainPlayer->heartbeatIntensity = Max(mainPlayer->heartbeatIntensity, 140);
                    //HeartBeatVolume = 0.5

                    //Jumpscare.
                    PlaySound2(n->sounds[bbRand(4,8)]);
                }

                n->lastDist = dist;
            } else {
                //Stonedrag.
                n->soundChannels[0] = LoopRangedSound(n->sounds[0], n->soundChannels[0], mainPlayer->cam, n->collider, 10.0, n->state);

                //more than 6 room lengths away from the player -> teleport to a room closer to the player
                if (dist > 50) {
                    if (bbRand(70)==1) {
                        if (!mainPlayer->currRoom->roomTemplate->name.equals("exit1") && !mainPlayer->currRoom->roomTemplate->name.equals("gatea") && !mainPlayer->currRoom->roomTemplate->name.equals("pocketdimension")) {
                            for (int iterator128 = 0; iterator128 < WayPoint::getListSize(); iterator128++) {
                                w = WayPoint::getObject(iterator128);

                                //w\door=Null And (TODO: fix?)
                                if (bbRand(5)==1) {
                                    x = Abs(bbEntityX(mainPlayer->collider) - bbEntityX(w->obj, true));
                                    if (x < 25.0 && x > 15.0) {
                                        z = Abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(w->obj,true));
                                        if (z < 25 && z > 15.0) {
                                            bbDebugLog("MOVING 173 TO " + w->room->roomTemplate->name);
                                            bbPositionEntity(n->collider, bbEntityX(w->obj,true), bbEntityY(w->obj,true)+0.25,bbEntityZ(w->obj,true));
                                            bbResetEntity(n->collider);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //3-6 rooms away from the player -> move randomly from waypoint to another
                } else if ((dist > HideDistance*0.8)) {
                    if (bbRand(70)==1) {
                        TeleportCloser(n);
                    }
                    //less than 3 rooms away -> actively move towards the player
                } else {

                    //try to open doors
                    if (bbRand(20) == 1) {
                        for (int iterator129 = 0; iterator129 < Door::getListSize(); iterator129++) {
                            d = Door::getObject(iterator129);

                            if ((!d->locked) && d->open == false && d->code.isEmpty() && d->tag.isEmpty()) {
                                for (i = 0; i <= 1; i++) {
                                    if (d->buttons[i] != 0) {
                                        if (Abs(bbEntityX(n->collider) - bbEntityX(d->buttons[i])) < 0.5) {
                                            if (Abs(bbEntityZ(n->collider) - bbEntityZ(d->buttons[i])) < 0.5) {
                                                if (d->openstate >= 180 | d->openstate <= 0) {
                                                    if (bbDeltaYaw(n->collider, d->buttons[i]) < 60 && bbDeltaYaw(d->buttons[i], n->collider) < 60) {
                                                        PlaySound2(LoadTempSound("SFX/Door/DoorOpen173.ogg"));
                                                        UseDoor(d,false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (NoTarget) {
                        playerVisible = false;
                        n->targetX = 0;
                        n->targetY = 0;
                        n->targetZ = 0;
                    }

                    //player is not looking and is visible from 173's position -> attack
                    if (playerVisible) {
                        if (dist < 0.65) {
                            if ((!mainPlayer->dead) && (!mainPlayer->godMode)) {

                                switch (mainPlayer->currRoom->roomTemplate->name) {
                                    case "lockroom", "room2closets", "coffin": {
                                        DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. The surveillance tapes confirm that the subject was killed by SCP-173.";
                                    }
                                    case "173": {
                                        DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. According to Security Chief Franklin who was present at SCP-173's containment ";
                                        DeathMSG = DeathMSG + "chamber during the breach, the subject was killed by SCP-173 as soon as the disruptions in the electrical network started.";
                                    }
                                    case "room2doors": {
                                        DeathMSG = "\"If I'm not mistaken, one of the main purposes of these rooms was to stop SCP-173 from moving further in the event of a containment breach. ";
                                        DeathMSG = DeathMSG + "So, who's brilliant idea was it to put A GODDAMN MAN-SIZED VENTILATION DUCT in there?\"";
                                    }
                                    default: {
                                        DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. Assumed to be attacked by SCP-173.";
                                    }
                                }

                                n->state = STATE173_IDLE;

                                //Necksnap.
                                PlaySound2(n->sounds[bbRand(9,11)]);

                                //TODO: Remove?
                                if (bbRand(2) == 1) {
                                    bbTurnEntity(mainPlayer->cam, 0, bbRand(80,100), 0);
                                } else {
                                    bbTurnEntity(mainPlayer->cam, 0, bbRand(-100,-80), 0);
                                }

                                Kill(mainPlayer);
                            }
                        } else {
                            bbPointEntity(n->collider, mainPlayer->collider);
                            bbRotateEntity(n->collider, 0, bbEntityYaw(n->collider), bbEntityRoll(n->collider));
                            bbTranslateEntity(n->collider,bbCos(bbEntityYaw(n->collider)+90.0)*n->speed*timing->tickDuration,0.0,bbSin(bbEntityYaw(n->collider)+90.0)*n->speed*timing->tickDuration);
                        }

                        //player is not visible -> move to the location where he was last seen
                    } else {
                        if (n->targetX != 0) {
                            if (Distance(bbEntityX(n->collider), bbEntityZ(n->collider), n->targetX, n->targetZ) > 0.5) {
                                bbAlignToVector(n->collider, n->targetX-bbEntityX(n->collider), 0, n->targetZ-bbEntityZ(n->collider), 3);
                                bbMoveEntity(n->collider, 0, 0, n->speed * timing->tickDuration);
                                if (bbRand(500) == 1) {
                                    n->targetX = 0;
                                    n->targetY = 0;
                                    n->targetZ = 0;
                                }
                            } else {
                                n->targetX = 0;
                                n->targetY = 0;
                                n->targetZ = 0;
                            }
                        } else {
                            if (bbRand(400)==1) {
                                bbRotateEntity(n->collider, 0, bbRnd(360), 10);
                            }
                            bbTranslateEntity(n->collider,bbCos(bbEntityYaw(n->collider)+90.0)*n->speed*timing->tickDuration,0.0,bbSin(bbEntityYaw(n->collider)+90.0)*n->speed*timing->tickDuration);

                        }
                    }

                    // less than 2 rooms away from the player
                }

                //TODO: Why random Y?
                //PositionEntity(n\collider, EntityX(n\collider), Min(EntityY(n\collider),0.35), EntityZ(n\collider))
            }
        }
        case STATE173_MOVE_TO_TARGET: {
            //If 173 was given a target then use its position.
            if (n->target != nullptr) {
                n->targetX = bbEntityX(n->target->collider);
                n->targetY = bbEntityY(n->target->collider);
                n->targetZ = bbEntityZ(n->target->collider);
            }

            if (n->targetX != 0) {
                if (Distance(bbEntityX(n->collider), bbEntityZ(n->collider), n->targetX, n->targetZ) > 0.5) {
                    bbAlignToVector(n->collider, n->targetX-bbEntityX(n->collider), 0, n->targetZ-bbEntityZ(n->collider), 3);
                    bbMoveEntity(n->collider, 0, 0, n->speed * timing->tickDuration);
                } else {
                    n->targetX = 0;
                    n->targetY = 0;
                    n->targetZ = 0;
                    n->state = STATE173_IDLE;
                }
            } else {
                n->state = STATE173_IDLE;
            }
        }
        case STATE173_BEING_CONTAINED: {
            if (n->target != nullptr) {
                tmp = false;
                if (dist > HideDistance*0.7) {
                    if (bbEntityVisible(n->obj,mainPlayer->collider)==false) {
                        tmp = true;
                    }
                }
                if (!tmp) {
                    bbPointEntity(n->obj, n->target->collider);
                    bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),10.0), 0, true);
                    dist = bbEntityDistance(n->collider, n->target->collider);
                    bbMoveEntity(n->collider, 0, 0, 0.016*timing->tickDuration*Max(Min((dist*2-1.0)*0.5,1.0),-0.5));
                } else {
                    bbPositionEntity(n->collider,bbEntityX(n->target->collider),bbEntityY(n->target->collider)+0.3,bbEntityZ(n->target->collider));
                    bbResetEntity(n->collider);
                    //PointEntity(n\collider, n\target\collider)
                    //RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),10.0), 0, True)
                    //dist = EntityDistance(n\collider, n\target\collider)
                    //MoveEntity(n\collider, 0, 0, dist-0.6)
                }
            }

            bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider) + 0.05 + bbSin(TimeInPosMilliSecs()*0.08)*0.02, bbEntityZ(n->collider));
            bbRotateEntity(n->obj, 0, bbEntityYaw(n->collider)-180, 0);

            bbShowEntity(n->obj2);

            bbPositionEntity(n->obj2, bbEntityX(n->collider), bbEntityY(n->collider) - 0.05 + bbSin(TimeInPosMilliSecs()*0.08)*0.02, bbEntityZ(n->collider));
            bbRotateEntity(n->obj2, 0, bbEntityYaw(n->collider)-180, 0);
        }
    }
}

}

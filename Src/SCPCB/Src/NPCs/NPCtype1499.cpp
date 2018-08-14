#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "NPCs.h"
#include "../Config/INI.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../AssetMgmt/Audio.h"
#include "../Map/MapSystem.h"
#include "../Player/Player.h"
#include "../MathUtils/MathUtils.h"
#include "NPCtype1499.h"

namespace CBN {

// Functions.
void InitializeNPCtype1499(NPC* n) {
    n->nvName = "Unidentified";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.2f);
    bbEntityType(n->collider, HIT_PLAYER);

    NPC* n2;
    for (int iterator125 = 0; iterator125 < NPC::getListSize(); iterator125++) {
        n2 = NPC::getObject(iterator125);

        if ((n->npcType == n2->npcType) && (n!=n2)) {
            n->obj = bbCopyMeshModelEntity(n2->obj);
            break;
        }
    }

    if (n->obj == 0) {
        n->obj = bbLoadAnimMesh("GFX/NPCs/scp1499/scp1499.b3d");
    }

    n->speed = (GetINIFloat("Data/NPCs.ini", "SCP-1499-1", "speed") / 100.f) * bbRnd(0.9f,1.1f);
    float temp = (GetINIFloat("Data/NPCs.ini", "SCP-1499-1", "scale") / 6.f) * bbRnd(0.8f,1.f);

    bbScaleEntity(n->obj, temp, temp, temp);

    bbEntityFX(n->obj,1);
}

void UpdateNPCtype1499(NPC* n) {
    //n\state: Current State of the NPC
    //n\state2: A second state variable (dependend on the current NPC's n\state)

    NPC* n2;
    int prevFrame = (int)(n->frame);

    if ((!n->idle) && bbEntityDistance(n->collider,mainPlayer->collider)<HideDistance*2) {
        if (n->state == 0 || n->state == 2) {
            for (int iterator126 = 0; iterator126 < NPC::getListSize(); iterator126++) {
                n2 = NPC::getObject(iterator126);

                if (n2->npcType == n->npcType && n2 != n) {
                    if (n2->state != 0 && n2->state != 2) {
                        n->state = 1;
                        n->state2 = 0;
                        break;
                    }
                }
            }
        }

        switch ((int)n->state) {
            case 0: {
                if (n->currSpeed == 0.f) {
                    if (n->state2 < 500.f*bbRnd(1,3)) {
                        n->currSpeed = 0.f;
                        n->state2 = n->state2 + timing->tickDuration;
                    } else {
                        if (n->currSpeed == 0.f) {
                            n->currSpeed = n->currSpeed + 0.0001f;
                        }
                    }
                } else {
                    if (n->state2 < 10000.f*bbRnd(1,3)) {
                        n->currSpeed = CurveValue(n->speed,n->currSpeed,10.f);
                        n->state2 = n->state2 + timing->tickDuration;
                    } else {
                        n->currSpeed = CurveValue(0.f,n->currSpeed,50.f);
                    }

                    bbRotateEntity(n->collider,0,CurveAngle(n->angle,bbEntityYaw(n->collider),10.f),0);

                    if (bbRand(200) == 1) {
                        n->angle = n->angle + bbRnd(-45,45);
                    }

                    bbHideEntity(n->collider);
                    bbEntityPick(n->collider, 1.5f);
                    if (bbPickedEntity() != 0) {
                        n->angle = bbEntityYaw(n->collider)+bbRnd(80,110);
                    }
                    bbShowEntity(n->collider);
                }

                if (n->currSpeed == 0.f) {
                    AnimateNPC(n,296,317,0.2f);
                } else {
                    if (n->id % 2 == 0) {
                        AnimateNPC(n,1,62,(n->currSpeed*28));
                    } else {
                        AnimateNPC(n,100,167,(n->currSpeed*28));
                    }
                }

                //randomly play the "screaming animation" and revert back to state 0
                if (bbRand(5000)==1) {
                    n->state = 2;
                    n->state2 = 0;

                    if (!bbChannelPlaying(n->soundChannels[0])) {
                        if (n->playerDistance < 20.f) {
                            if (n->sounds[0] != 0) {
                                bbFreeSound(n->sounds[0]);
                                n->sounds[0] = 0;
                            }
                            n->sounds[0] = bbLoadSound("SFX/SCP/1499/Idle"+String(bbRand(1,4))+".ogg");
                            n->soundChannels[0] = PlayRangedSound(n->sounds[0], mainPlayer->cam, n->collider, 20.f);
                        }
                    }
                }

                if (n->id % 2 == 0 && !NoTarget) {
                    if (n->playerDistance < 10.f) {
                        if (bbEntityVisible(n->collider,mainPlayer->collider)) {
                            //play the "screaming animation"
                            n->state = 2;
                            if (n->playerDistance < 5.f) {
                                if (n->sounds[0] != 0) {
                                    bbFreeSound(n->sounds[0]);
                                    n->sounds[0] = 0;
                                }
                                n->sounds[0] = bbLoadSound("SFX/SCP/1499/Triggered.ogg");
                                n->soundChannels[0] = PlayRangedSound(n->sounds[0], mainPlayer->cam, n->collider,20.f);

                                //if player is too close, switch to attack after screaming
                                n->state2 = 1;

                                for (int iterator127 = 0; iterator127 < NPC::getListSize(); iterator127++) {
                                    n2 = NPC::getObject(iterator127);

                                    //If (n2\npctype = n\npcType And n2 <> n And (n\id Mod 2 = 0)) Then
                                    if (n2->npcType == n->npcType && n2 != n) {
                                        n2->state = 1;
                                        n2->state2 = 0;
                                    }
                                }
                            } else {
                                //otherwise keep idling
                                n->state2 = 0;
                            }

                            n->frame = 203;
                        }
                    }
                }
                //attacking the player
            }
            case 1: {
                if (NoTarget) {
                    n->state = 0;
                }

                if (mainPlayer->currRoom->roomTemplate->name.equals("dimension1499")) {
                    //If (Music(19)=0) Then Music(19) = LoadSound("SFX/Music/1499Danger.ogg") ;TODO: fix
                    //ShouldPlay = 19
                }

                bbPointEntity(n->obj,mainPlayer->collider);
                bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),20.f),0);

                if (n->state2 == 0.f) {
                    n->currSpeed = CurveValue(n->speed*1.75f,n->currSpeed,10.f);

                    if (n->id % 2 == 0) {
                        AnimateNPC(n,1,62,(n->currSpeed*28));
                    } else {
                        AnimateNPC(n,100,167,(n->currSpeed*28));
                    }
                }

                if (n->playerDistance < 0.75f) {
                    if ((n->id % 2 == 0) || n->state3 == 1) {
                        n->state2 = (float)bbRand(1,2);
                        n->state = 3;
                        if (n->state2 == 1) {
                            SetNPCFrame(n,63);
                        } else {
                            SetNPCFrame(n,168);
                        }
                    } else {
                        n->state = 4;
                    }
                }
                //play the "screaming animation" and switch to n\state2 after it's finished
            }
            case 2: {
                n->currSpeed = 0.f;
                AnimateNPC(n,203,295,0.1f,false);

                if (n->frame > 294.f) {
                    n->state = n->state2;
                }
                //slashing at the player
            }
            case 3: {
                n->currSpeed = CurveValue(0.f,n->currSpeed,5.f);
                if (n->state2 == 1) {
                    AnimateNPC(n,63,100,0.6f,false);
                    if (prevFrame < 89 && n->frame>=89) {
                        if (n->playerDistance > 0.85f || abs(bbDeltaYaw(n->collider,mainPlayer->collider))>60.f) {
                            //Miss
                        } else {
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.75f,1.5f);
                            PlayRangedSound(LoadTempSound("SFX/General/Slash"+String(bbRand(1,2))+".ogg"), mainPlayer->cam, n->collider);
                            if (mainPlayer->injuries > 10.f) {
                                Kill(mainPlayer);
                                if (mainPlayer->currRoom->roomTemplate->name.equals("dimension1499")) {
                                    DeathMSG = "All personnel situated within Evacuation Shelter LC-2 during the breach have been administered ";
                                    DeathMSG = DeathMSG + "Class-B amnestics due to Incident 1499-E. The Class D subject involved in the event ";
                                    DeathMSG = DeathMSG + "died shortly after being shot by Agent [REDACTED].";
                                } else {
                                    DeathMSG = "An unidentified male and a deceased Class D subject were discovered in [REDACTED] by the Nine-Tailed Fox. ";
                                    DeathMSG = DeathMSG + "The man was described as highly agitated and seemed to only speak Russian. ";
                                    DeathMSG = DeathMSG + "He's been taken into a temporary holding area at [REDACTED] while waiting for a translator to arrive.";
                                }
                            }
                        }
                    } else if ((n->frame >= 99)) {
                        n->state2 = 0.f;
                        n->state = 1;
                    }
                } else {
                    AnimateNPC(n,168,202,0.6f,false);
                    if (prevFrame < 189 && n->frame>=189) {
                        if (n->playerDistance > 0.85f || abs(bbDeltaYaw(n->collider,mainPlayer->collider))>60.f) {
                            //Miss
                        } else {
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.75f,1.5f);
                            PlayRangedSound(LoadTempSound("SFX/General/Slash"+String(bbRand(1,2))+".ogg"), mainPlayer->cam, n->collider);
                            if (mainPlayer->injuries > 10.f) {
                                Kill(mainPlayer);
                                if (mainPlayer->currRoom->roomTemplate->name.equals("dimension1499")) {
                                    DeathMSG = "All personnel situated within Evacuation Shelter LC-2 during the breach have been administered ";
                                    DeathMSG = DeathMSG + "Class-B amnestics due to Incident 1499-E. The Class D subject involved in the event ";
                                    DeathMSG = DeathMSG + "died shortly after being shot by Agent [REDACTED].";
                                } else {
                                    DeathMSG = "An unidentified male and a deceased Class D subject were discovered in [REDACTED] by the Nine-Tailed Fox. ";
                                    DeathMSG = DeathMSG + "The man was described as highly agitated and seemed to only speak Russian. ";
                                    DeathMSG = DeathMSG + "He's been taken into a temporary holding area at [REDACTED] while waiting for a translator to arrive.";
                                }
                            }
                        }
                    } else if ((n->frame >= 201)) {
                        n->state2 = 0.f;
                        n->state = 1;
                    }
                }
                //standing in front of the player
            }
            case 4: {
                n->currSpeed = CurveValue(0.f,n->currSpeed,5.f);
                AnimateNPC(n,296,317,0.2f);

                bbPointEntity(n->obj,mainPlayer->collider);
                bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),20.f),0);

                if (n->playerDistance > 0.85f) {
                    n->state = 1;
                }
            }
        }

        if (n->soundChannels[0] != 0 && bbChannelPlaying(n->soundChannels[0])) {
            UpdateRangedSoundOrigin(n->soundChannels[0],mainPlayer->cam,n->collider,20.f);
        }

        bbMoveEntity(n->collider,0,0,n->currSpeed*timing->tickDuration);

        bbRotateEntity(n->obj,0,bbEntityYaw(n->collider)-180,0);
        bbPositionEntity(n->obj,bbEntityX(n->collider),bbEntityY(n->collider)-0.2f,bbEntityZ(n->collider));

        bbShowEntity(n->obj);
    } else {
        bbHideEntity(n->obj);
    }
}

}

#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "NPCs.h"
#include "../Config/INI.h"
#include "../GameMain.h"
#include "../AssetMgmt/Audio.h"
#include "../Map/MapSystem.h"
#include "../Player/Player.h"
#include "../MathUtils/MathUtils.h"
#include "../Config/Difficulty.h"
#include "../Map/Decals.h"
#include "NPCtype106.h"

namespace CBN {

// Structs.
std::vector<Data106*> Data106::list;
Data106::Data106() {
    list.push_back(this);
}
Data106::~Data106() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Data106::getListSize() {
    return list.size();
}
Data106* Data106::getObject(int index) {
    return list[index];
}

// Constants.
const int STATE106_IDLE = 0;
const int STATE106_RISE = 1;
const int STATE106_ATTACK = 2;
const int STATE106_PD = 3;
const int STATE106_MOVE_TO_TARGET = 4;
const int STATE106_CONTAINED = 5;

// Functions.
void InitializeNPCtype106(NPC* n) {
    n->nvName = "SCP-106";
    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.2f);
    bbEntityType(n->collider, HIT_PLAYER);
    n->obj = bbLoadAnimMesh("GFX/NPCs/scp106/106.b3d");

    n->gravity = false;

    float temp = (GetINIFloat("Data/NPCs.ini", "SCP-106", "scale") / 2.2f);
    bbScaleEntity(n->obj, temp, temp, temp);

    // TODO: Re-implement.
    // Texture* oldManEyes = bbLoadTexture("GFX/NPCs/scp106/oldmaneyes.jpg");

    // n->speed = (GetINIFloat("Data/NPCs.ini", "SCP-106", "speed") / 100.f);

    // n->obj2 = bbCreateSprite();
    // bbScaleSprite(n->obj2, 0.03f, 0.03f);
    // bbEntityTexture(n->obj2, oldManEyes);
    // bbEntityBlend(n->obj2, BLEND_ADD);
    // //Full-bright + no fog.
    // bbEntityFX(n->obj2, 1 + 8);
    // bbSpriteViewMode(n->obj2, 2);

    // bbFreeTexture(oldManEyes);

    n->sounds[0] = bbLoadSound("SFX/SCP/106/Spawn.ogg");
    n->sounds[1] = bbLoadSound("SFX/SCP/106/Breathing.ogg");
    n->sounds[2] = bbLoadSound("SFX/SCP/106/Seen.ogg");
    n->sounds[3] = bbLoadSound("SFX/SCP/106/Corrosion1.ogg");
    n->sounds[4] = bbLoadSound("SFX/SCP/106/Corrosion2.ogg");
    n->sounds[5] = bbLoadSound("SFX/SCP/106/Corrosion3.ogg");
    n->sounds[6] = bbLoadSound("SFX/SCP/106/Laugh.ogg");
    n->sounds[7] = bbLoadSound("SFX/SCP/106/Catch.ogg");

    n->timer = bbRand(22000, 27000);
}

void UpdateNPCtype106(NPC* n) {
    float dist = bbEntityDistance(n->collider, mainPlayer->collider);
    float dist2;
    bool visible = false;
    float prevFrame;

    Decal* de;

    switch ((int)n->state) {
        case STATE106_RISE: {
            if (mainPlayer->currRoom->roomTemplate->name.equals("dimension1499")) {
                return;
            }

            n->timer = n->timer - (timing->tickDuration * (SelectedDifficulty->aggressiveNPCs * 2));

            //Check if 106's timer is below 0, if not decrease it.
            if (n->timer >= 0) {
                n->currSpeed = 0;
                bbMoveEntity(n->collider, 0, ((bbEntityY(mainPlayer->collider) - 30) - bbEntityY(n->collider)) / 200.f, 0);
                n->frame = 110;
                //Otherwise begin spawning 106.
            } else if ((n->timer >= -10)) {
                if (bbEntityY(n->collider) < bbEntityY(mainPlayer->collider) - 20.f - 0.55f) {
                    if (!mainPlayer->currRoom->roomTemplate->disableDecals) {
                        de = CreateDecal(DECAL_CORROSION, bbEntityX(mainPlayer->collider), 0.01f, bbEntityZ(mainPlayer->collider), 90, bbRand(360), 0);
                        de->size = 0.05f;
                        de->sizeChange = 0.001f;
                        bbEntityAlpha(de->obj, 0.8f);
                        UpdateDecals();
                    }

                    n->prevY = bbEntityY(mainPlayer->collider);

                    bbSetAnimTime(n->obj, 110);

                    PlaySound2(n->sounds[0]);
                }

                //Corrosion.
                if (bbRand(500) == 1) {
                    PlayRangedSound(n->sounds[bbRand(3, 5)], mainPlayer->cam, n->collider);
                }
                //Breathing
                n->soundChannels[0] = LoopRangedSound(n->sounds[1], n->soundChannels[0], mainPlayer->cam, n->collider, 8.f, 0.8f);

                //Rising.
                if (n->timer >= - 10) {
                    //ShouldPlay = 66 ;TODO
                    if (n->frame < 259) {
                        bbPositionEntity(n->collider, bbEntityX(n->collider), n->prevY-0.15f, bbEntityZ(n->collider));
                        bbPointEntity(n->obj, mainPlayer->collider);
                        bbRotateEntity(n->collider, 0, CurveValue(bbEntityYaw(n->obj),bbEntityYaw(n->collider),100.f), 0, true);

                        AnimateNPC(n, 110, 259, 0.15f, false);
                    }
                }
                //Switch to attacking state.
            } else {
                n->timer = 0;
                n->state = STATE106_ATTACK;
            }

        }
        case STATE106_ATTACK: {
            //TODO: Set music to play 106 theme?
            if (dist < 8.f && (!NoTarget)) {
                visible = bbEntityVisible(n->collider, mainPlayer->collider);
            }

            //Show glowing eyes.
            //TODO: fix
            //If (dist < CameraFogFar*LightVolume*0.6f) Then
            //    HideEntity(n\obj2)
            //Else
            //    ShowEntity(n\obj2)
            //    EntityAlpha(n\obj2, Min(dist-CameraFogFar*LightVolume*0.6f,1.f))
            //EndIf

            if (visible) {
                if (bbEntityInView(n->collider, mainPlayer->cam)) {
                    mainPlayer->blurTimer = Max(Max(Min((4.f - dist) / 6.f, 0.9f), 0.1f), mainPlayer->blurTimer);
                    mainPlayer->camZoom = Max(mainPlayer->camZoom, (bbSin((float)(TimeInPosMilliSecs())/20.f)+1.f) * 20.f * Max((4.f-dist)/4.f,0));

                    if (TimeInPosMilliSecs() - n->lastSeen > 60000) {
                        mainPlayer->camZoom = 40;
                        PlaySound2(n->sounds[2]);
                        n->lastSeen = TimeInPosMilliSecs();
                    }
                }
            }

            n->currSpeed = CurveValue(n->speed, n->currSpeed, 10.f);

            if (dist > 0.8f) {
                prevFrame = n->frame;

                //Walking animation.
                AnimateNPC(n, 284, 333, n->currSpeed * 43);

                //Footstep sounds.
                if (prevFrame <= 286 && n->frame > 286 || prevFrame<=311 && n->frame > 311.f) {
                    PlayRangedSound(sndManager->footstepPD[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 6.f, bbRnd(0.8f,1.f));
                }

                if (dist > 25.f || visible || n->pathStatus == 2) {

                    bbPointEntity(n->obj, mainPlayer->collider);
                    bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), 10.f), 0);

                    n->pathTimer = Max(n->pathTimer - timing->tickDuration, 0);
                    if (n->pathTimer <= 0) {
                        n->pathStatus = 0;
                    }
                    //Between 0.8f and 25 units.
                } else {
                    //Pathfind to the player.
                    if (n->pathTimer <= 0) {
                        n->pathStatus = FindPath (n, bbEntityX(mainPlayer->collider,true), bbEntityY(mainPlayer->collider,true), bbEntityZ(mainPlayer->collider,true));
                        n->pathTimer = 70*10;
                        n->currSpeed = 0;
                    } else {
                        n->pathTimer = Max(n->pathTimer-timing->tickDuration,0);

                        if (n->pathStatus == 2) {
                            n->currSpeed = 0;
                        } else if ((n->pathStatus == 1)) {
                            if (n->path[n->pathLocation]==nullptr) {
                                if (n->pathLocation > 19) {
                                    n->pathLocation = 0;
                                    n->pathStatus = 0;
                                } else {
                                    n->pathLocation = n->pathLocation + 1;
                                }
                            } else {
                                bbTranslateEntity(n->collider, 0, ((bbEntityY(n->path[n->pathLocation]->obj,true) - 0.11f) - bbEntityY(n->collider)) / 50.f, 0);

                                bbPointEntity(n->obj, n->path[n->pathLocation]->obj);

                                dist2 = bbEntityDistance(n->collider,n->path[n->pathLocation]->obj);

                                bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), Min(20.f,dist2*10.f)), 0);

                                if (dist2 < 0.2f) {
                                    n->pathLocation = n->pathLocation + 1;
                                }
                            }
                        } else if ((n->pathStatus == 0)) {
                            if (n->state3==0) {
                                AnimateNPC(n, 334, 494, 0.3f);
                            }
                        }
                    }
                }
            } else {
                //Caught.
                if (dist > 0.5f) {
                    n->currSpeed = CurveValue(n->speed * 2.5f, n->currSpeed, 10.f);
                } else {
                    n->currSpeed = 0;
                }
                AnimateNPC(n, 105, 110, 0.15f, false);

                if ((!mainPlayer->dead) && mainPlayer->fallTimer >= 0) {
                    bbPointEntity(n->obj, mainPlayer->collider);
                    bbRotateEntity(n->collider, 0, CurveAngle(bbEntityYaw(n->obj), bbEntityYaw(n->collider), 10.f), 0);

                    //TODO: Teleport to pocket dimension.
                    if (bbCeil(n->frame) == 110 && (!mainPlayer->godMode)) {
                        PlaySound2(mainPlayer->damageSFX[1]);
                        PlaySound2(n->sounds[7]);

                        PlaySound2(n->sounds[6]);
                        mainPlayer->fallTimer = Min(-1, mainPlayer->fallTimer);
                        bbPositionEntity(mainPlayer->head, bbEntityX(mainPlayer->cam, true), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true), true);
                        bbResetEntity(mainPlayer->head);
                        bbRotateEntity(mainPlayer->head, 0, bbEntityYaw(mainPlayer->cam) + bbRand(-45, 45), 0);
                    }
                }
            }

            bbMoveEntity(n->collider, 0, 0, n->currSpeed * timing->tickDuration);

            if (dist > 48) {
                //Reset state.
                if (!bbEntityInView(n->obj,mainPlayer->cam) && bbRand(5)==1) {
                    n->timer = bbRand(22000, 27000);
                    n->state = STATE106_RISE;
                    //Flank.
                } else {
                    //TODO
                }
            }
        }
    }
}

}

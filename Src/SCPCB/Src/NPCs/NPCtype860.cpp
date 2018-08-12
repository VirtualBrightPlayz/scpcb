#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "NPCs.h"
#include "../INI.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../Audio.h"
#include "../MapSystem.h"
#include "../Player.h"
#include "../MathUtils/MathUtils.h"
#include "../Assets.h"
#include "../Rooms/Room_test_860_2.h"
#include "NPCtype860.h"

namespace CBN {

// Functions.
void InitializeNPCtype860(NPC* n) {
    n->nvName = "Unidentified";

    n->collider = bbCreatePivot();
    bbEntityRadius(n->collider, 0.25f);
    bbEntityType(n->collider, HIT_PLAYER);
    n->obj = bbLoadAnimMesh("GFX/NPCs/scp860/forestmonster.b3d");

    bbEntityFX(n->obj, 1);

    // TODO: Re-implement.
    // Texture* tex = bbLoadTexture("GFX/NPCs/scp860/860_eyes.png",1+2);

    // n->obj2 = bbCreateSprite();
    // bbScaleSprite(n->obj2, 0.1f, 0.1f);
    // bbEntityTexture(n->obj2, tex);
    // bbFreeTexture(tex);

    bbEntityFX(n->obj2, 1 + 8);
    bbEntityBlend(n->obj2, BLEND_ADD);
    //bbSpriteViewMode(n->obj2, 2);

    n->speed = (GetINIFloat("Data/NPCs.ini", "forestmonster", "speed") / 100.f);

    float temp = (GetINIFloat("Data/NPCs.ini", "forestmonster", "scale") / 20.f);
    bbScaleEntity(n->obj, temp, temp, temp);

    bbMeshCullBox(n->obj, -bbMeshWidth(n->obj)*2, -bbMeshHeight(n->obj)*2, -bbMeshDepth(n->obj)*2, bbMeshWidth(n->obj)*2, bbMeshHeight(n->obj)*4, bbMeshDepth(n->obj)*4);
}

void UpdateNPCtype860(NPC* n) {
    int x;
    int z;
    int x2;
    int z2;
    Pivot* pvt;
    int prevFrame;
    float angle;
    int temp;
    Forest* fr;

    if (mainPlayer->currRoom->roomTemplate->name.equals("room860")) {
        //Object.Forest(e\room\objects[1])
        fr = mainPlayer->currRoom->fr;

        switch ((int)n->state) {
            case 0: {

                bbHideEntity(n->collider);
                bbHideEntity(n->obj);
                bbHideEntity(n->obj2);

                n->state2 = 0;
                bbPositionEntity(n->collider, 0, -100, 0);
                //appears briefly behind the trees
            }
            case 1: {
                n->dropSpeed = 0;

                if (bbEntityY(n->collider)<= -100) {
                    bbTFormPoint(bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider),0,mainPlayer->currRoom->obj);

                    x = (int)(bbFloor((bbTFormedX()*RoomScale+6.f)/12.f));
                    z = (int)(bbFloor((bbTFormedZ()*RoomScale+6.f)/12.f));

                    bbTFormPoint(x/RoomScale*12.f,0,z/RoomScale*12.f,fr->forest_Pivot,0);

                    for (x2 = (int)(Max(x-1,0)); x2 <= (int)(Min(x+1,gridsize)); x2 += 2) {
                        for (z2 = (int)(Max(z-1,0)); z2 <= (int)(Min(z+1,gridsize)); z2 += 2) {
                            if (fr->grid[(z2*gridsize)+x2]==0) {

                                bbTFormPoint((x*12 + (x2-x)*6)/RoomScale,0,(z*12 + (z2-z)*6)/RoomScale,mainPlayer->currRoom->obj,0);

                                bbPositionEntity(n->collider, bbTFormedX(), bbEntityY(fr->forest_Pivot,true)+2.3f, bbTFormedZ());

                                if (bbEntityInView(n->collider, mainPlayer->cam)) {
                                    bbPositionEntity(n->collider, 0, -110, 0);
                                    //only spawn the monster outside the player's field of view
                                } else {
                                    x2 = (int)(Min(x+1,gridsize));
                                    break;
                                }

                            }
                        }
                    }

                    if (bbEntityY(n->collider)> -100) {
                        PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 15.f, 0.5f);

                        switch (bbRand(3)) {
                            case 1: {
                                bbPointEntity(n->collider,mainPlayer->collider);
                                n->frame = 2;
                                //SetAnimTime(n\obj, 2)
                            }
                            case 2: {
                                bbPointEntity(n->collider,mainPlayer->collider);
                                n->frame = 201;
                                //SetAnimTime(n\obj, 201)
                            }
                            case 3: {
                                bbPointEntity(n->collider,mainPlayer->collider);
                                bbTurnEntity(n->collider, 0, 90, 0);
                                n->frame = 299;
                                //SetAnimTime(n\obj, 299)
                            }
                        }

                        n->state2 = 0;
                    }
                } else {

                    bbShowEntity(n->obj);
                    bbShowEntity(n->collider);

                    bbPositionEntity(n->collider, bbEntityX(n->collider), bbEntityY(fr->forest_Pivot,true)+2.3f, bbEntityZ(n->collider));

                    //don't start moving until the player is looking
                    if (n->state2 == 0) {
                        if (bbEntityInView(n->collider, mainPlayer->cam)) {
                            n->state2 = 1;
                            if (bbRand(8)==1) {
                                PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+String(bbRand(0,2))+".ogg"), mainPlayer->cam, n->collider, 20.f);
                            }
                        }
                    } else {
                        if (n->frame<=199) {
                            AnimateNPC(n, 2, 199, 0.5f,false);
                            if (n->frame==199) {
                                //PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.f)
                                n->frame = 298;

                            }

                            //Animate2(n\obj, AnimTime(n\obj), 2, 199, 0.5f,False)
                            //If (AnimTime(n\obj)=199) Then SetAnimTime(n\obj,298)
                            PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 15.f);
                        } else if ((n->frame <= 297)) {
                            bbPointEntity(n->collider,mainPlayer->collider);

                            AnimateNPC(n, 200, 297, 0.5f, false);
                            if (n->frame==297) {
                                n->frame = 298;
                                PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 15.f);
                            }

                            //Animate2(n\obj, AnimTime(n\obj), 200, 297, 0.5f,False)
                            //If (AnimTime(n\obj)=297) Then SetAnimTime(n\obj,298)
                            PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 15.f);
                        } else {
                            angle = CurveAngle(GetAngle(bbEntityX(n->collider),bbEntityZ(n->collider),bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider)),bbEntityYaw(n->collider)+90,20.f);

                            bbRotateEntity(n->collider, 0, angle-90, 0, true);

                            AnimateNPC(n, 298, 316, n->currSpeed*10);

                            //Animate2(n\obj, AnimTime(n\obj), 298, 316, n\currSpeed*10)

                            n->currSpeed = CurveValue(n->speed, n->currSpeed, 10.f);
                            bbMoveEntity(n->collider, 0,0,n->currSpeed*timing->tickDuration);

                            if (n->playerDistance>15.f) {
                                bbPositionEntity(n->collider, 0,-110,0);
                                n->state = 0;
                                n->state2 = 0;
                            }
                        }
                    }

                }

                bbResetEntity(n->collider);
                //appears on the path and starts to walk towards the player
            }
            case 2: {
                bbShowEntity(n->obj);
                bbShowEntity(n->collider);

                prevFrame = (int)(n->frame);

                if (bbEntityY(n->collider)<= -100) {
                    bbTFormPoint(bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider),0,mainPlayer->currRoom->obj);
                    x = (int)(bbFloor((bbTFormedX()*RoomScale+6.f)/12.f));
                    z = (int)(bbFloor((bbTFormedZ()*RoomScale+6.f)/12.f));

                    for (x2 = (int)(Max(x-1,0)); x2 <= (int)(Min(x+1,gridsize)); x2++) {
                        for (z2 = (int)(Max(z-1,0)); z2 <= (int)(Min(z+1,gridsize)); z2++) {
                            if (fr->grid[(z2*gridsize)+x2]>0 && (x2!=x | z2!=z) && (x2==x || z2==z)) {

                                bbTFormPoint((x2*12)/RoomScale,0,(z2*12)/RoomScale,mainPlayer->currRoom->obj,0);

                                //PositionEntity(n\collider, TFormedX(), EntityY(fr\forest_Pivot,True)+0.5f, TFormedZ())
                                bbPositionEntity(n->collider, bbTFormedX(), bbEntityY(fr->forest_Pivot,true)+1.f, bbTFormedZ());
                                std::cout << String(bbEntityY(fr->forest_Pivot,true));

                                if (bbEntityInView(n->collider, mainPlayer->cam)) {
                                    mainPlayer->blinkTimer = -10;
                                } else {
                                    x2 = (int)(Min(x+1,gridsize));
                                    break;
                                }
                            }
                        }
                    }
                } else {

                    angle = CurveAngle(Find860Angle(n, fr),bbEntityYaw(n->collider)+90,80.f);

                    bbRotateEntity(n->collider, 0, angle-90, 0, true);

                    n->currSpeed = CurveValue(n->speed*0.3f, n->currSpeed, 50.f);
                    bbMoveEntity(n->collider, 0,0,n->currSpeed*timing->tickDuration);

                    AnimateNPC(n, 494, 569, n->currSpeed*25);

                    //Animate2(n\obj, AnimTime(n\obj), 494, 569, n\currSpeed*25)

                    if (n->state2 == 0) {
                        if (n->playerDistance<8.f) {
                            if (bbEntityInView(n->collider,mainPlayer->cam)) {
                                PlaySound2(LoadTempSound("SFX/SCP/860/Chase"+String(bbRand(1,2))+".ogg"));

                                PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+String(bbRand(0,2))+".ogg"), mainPlayer->cam, n->collider);
                                n->state2 = 1;
                            }
                        }
                    }

                    //the player is running
                    if (mainPlayer->moveSpeed > 0.03f) {
                        n->state3 = n->state3 + timing->tickDuration;
                        if (bbRnd(5000)<n->state3) {
                            temp = true;
                            if (n->soundChannels[0] != 0) {
                                if (bbChannelPlaying(n->soundChannels[0])) {
                                    temp = false;
                                }
                            }
                            if (temp) {
                                n->soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+String(bbRand(0,2))+".ogg"), mainPlayer->cam, n->collider);
                            }
                        }
                    } else {
                        n->state3 = Max(n->state3 - timing->tickDuration,0);
                    }

                    if (n->playerDistance<4.5f || n->state3 > bbRnd(200,250)) {
                        n->soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+String(bbRand(3,5))+".ogg"), mainPlayer->cam, n->collider);
                        n->state = 3;
                    }

                    if (n->playerDistance > 16.f) {
                        n->state = 0;
                        n->state2 = 0;
                        bbPositionEntity(n->collider, 0,-110,0);
                    }
                }

                //535, 568
                if (prevFrame < 533 && n->frame>=533 || prevFrame > 568 && n->frame<2) {
                    PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 15.f, 0.6f);
                }

                //runs towards the player and attacks
            }
            case 3: {
                bbShowEntity(n->obj);
                bbShowEntity(n->collider);

                prevFrame = (int)(n->frame);

                angle = CurveAngle(Find860Angle(n, fr),bbEntityYaw(n->collider)+90,40.f);

                bbRotateEntity(n->collider, 0, angle-90, 0, true);

                if (n->sounds[0] == 0) {
                    n->sounds[0] = bbLoadSound("SFX/General/Slash1.ogg");
                }
                if (n->sounds[1] == 0) {
                    n->sounds[1] = bbLoadSound("SFX/General/Slash2.ogg");
                }

                if (n->playerDistance>1.1f && (!mainPlayer->dead)) {
                    n->currSpeed = CurveValue(n->speed*0.8f, n->currSpeed, 10.f);

                    AnimateNPC(n, 298, 316, n->currSpeed*10);
                    //Animate2(n\obj, AnimTime(n\obj), 298, 316, n\currSpeed*10)

                    if (prevFrame < 307 && n->frame>=307) {
                        PlayRangedSound(sndManager->footstep8601[bbRand(0, 2)]->internal, mainPlayer->cam, n->collider, 10.f);
                    }
                } else {
                    //461, 476

                    DeathMSG = "";

                    n->currSpeed = CurveValue(0.f, n->currSpeed, 5.f);

                    AnimateNPC(n, 451,493, 0.5f, false);

                    //Animate2(n\obj, AnimTime(n\obj), 451,493, 0.5f, False)
                    if (prevFrame < 461 && n->frame>=461) {
                        if (!mainPlayer->dead) {
                            Kill(mainPlayer);
                        }
                        PlaySound2(n->sounds[0]);
                    }
                    if (prevFrame < 476 && n->frame>=476) {
                        PlaySound2(n->sounds[1]);
                    }
                    if (prevFrame < 486 && n->frame>=486) {
                        PlaySound2(n->sounds[1]);
                    }

                }

                bbMoveEntity(n->collider, 0,0,n->currSpeed*timing->tickDuration);
            }
        }

        if (n->state != 0) {
            bbRotateEntity(n->collider, 0, bbEntityYaw(n->collider), 0, true);

            bbPositionEntity(n->obj, bbEntityX(n->collider), bbEntityY(n->collider)-0.1f, bbEntityZ(n->collider));
            bbRotateEntity(n->obj, bbEntityPitch(n->collider)-90, bbEntityYaw(n->collider), bbEntityRoll(n->collider), true);

            if (n->playerDistance > 8.f) {
                bbShowEntity(n->obj2);
                bbEntityAlpha(n->obj2, Min(n->playerDistance-8.f,1.f));

                bbPositionEntity(n->obj2, bbEntityX(n->obj), bbEntityY(n->obj) , bbEntityZ(n->obj));
                bbRotateEntity(n->obj2, 0, bbEntityYaw(n->collider) - 180, 0);
                bbMoveEntity(n->obj2, 0, 30.f*0.025f, -33.f*0.025f);

                //render distance is set to 8.5f inside the forest,
                //so we need to cheat a bit to make the eyes visible if they're further than that
                pvt = bbCreatePivot();
                bbPositionEntity(pvt, bbEntityX(mainPlayer->cam),bbEntityY(mainPlayer->cam),bbEntityZ(mainPlayer->cam));
                bbPointEntity(pvt, n->obj2);
                bbMoveEntity(pvt, 0,0,8.f);
                bbPositionEntity(n->obj2, bbEntityX(pvt),bbEntityY(pvt),bbEntityZ(pvt));
                bbFreeEntity(pvt);
            } else {
                bbHideEntity(n->obj2);
            }
        }
    }
}

float Find860Angle(NPC* n, Forest* fr) {
    bbTFormPoint(bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider),0,mainPlayer->currRoom->obj);
    int playerx = (int)(bbFloor((bbTFormedX()*RoomScale+6.f)/12.f));
    int playerz = (int)(bbFloor((bbTFormedZ()*RoomScale+6.f)/12.f));

    bbTFormPoint(bbEntityX(n->collider),bbEntityY(n->collider),bbEntityZ(n->collider),0,mainPlayer->currRoom->obj);
    float x = (bbTFormedX()*RoomScale+6.f)/12.f;
    float z = (bbTFormedZ()*RoomScale+6.f)/12.f;

    int xt = (int)(bbFloor(x));
    int zt = (int)(bbFloor(z));

    int x2;
    int z2;
    //the monster is not on the same tile as the player
    if (xt!=playerx || zt!=playerz) {
        for (x2 = (int)(Max(xt-1,0)); x2 <= (int)(Min(xt+1,gridsize-1)); x2++) {
            for (z2 = (int)(Max(zt-1,0)); z2 <= (int)(Min(zt+1,gridsize-1)); z2++) {
                if (fr->grid[(z2*gridsize)+x2]>0 && (x2!=xt | z2!=zt) && (x2==xt || z2==zt)) {

                    //tile (x2,z2) is closer to the player than the monsters current tile
                    if ((abs(playerx-x2)+abs(playerz-z2))<(abs(playerx-xt)+abs(playerz-zt))) {
                        return GetAngle(x-0.5f,z-0.5f,x2,z2)+bbEntityYaw(mainPlayer->currRoom->obj)+180;
                    }

                }
            }
        }
    } else {
        return GetAngle(bbEntityX(n->collider),bbEntityZ(n->collider),bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider))+180;
    }
}

}

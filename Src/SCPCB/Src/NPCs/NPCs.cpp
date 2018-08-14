#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "NPCs.h"
#include "NPCtype049.h"
#include "NPCtype066.h"
#include "NPCtype096.h"
#include "NPCtype106.h"
#include "NPCtype1499.h"
#include "NPCtype173.h"
#include "NPCtype860.h"
#include "NPCtype939.h"
#include "NPCtype966.h"
#include "NPCtypeApache.h"
#include "NPCtypeD.h"
#include "NPCtypeGuard.h"
#include "NPCtypeMTF.h"
#include "NPCtypeTentacle.h"
#include "NPCtypeZombie.h"
#include "../Player/Player.h"
#include "../GameMain.h"
#include "../Map/MapSystem.h"
#include "../AssetMgmt/Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Config/Difficulty.h"
#include "../Map/Decals.h"
#include "../Map/Particles.h"
#include "../Items/Items.h"
#include "../Menus/Menu.h"
#include "../Map/Doors.h"
#include "../Config/INI.h"
#include "../Map/Events/Events.h"
#include "../Map/Objects.h"

namespace CBN {

// Structs.
std::vector<NPC*> NPC::list;
NPC::NPC() {
    memset(this,0,sizeof(NPC));

    list.push_back(this);
}
NPC::~NPC() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int NPC::getListSize() {
    return list.size();
}
NPC* NPC::getObject(int index) {
    return list[index];
}


// Globals.
NPC* Curr173;
NPC* Curr106;
NPC* Curr096;

// Functions.
NPC* CreateNPC(int NPCtype, float x, float y, float z) {
    NPC* n = new NPC();

    n->obj = nullptr;
    n->obj2 = nullptr;
    n->obj3 = nullptr;
    n->obj4 = nullptr;
    n->target = nullptr;

    n->npcType = NPCtype;
    n->gravityMult = 1.f;
    n->maxGravity = 0.2f;
    switch (NPCtype) {
        case NPCtype173: {
            InitializeNPCtype173(n);
        } break;
        case NPCtype106: {
            InitializeNPCtype106(n);
        } break;
        case NPCtypeGuard: {
            InitializeNPCtypeGuard(n);
        } break;
        case NPCtypeMTF: {
            InitializeNPCtypeMTF(n);
        } break;
        case NPCtypeD: {
            InitializeNPCtypeD(n);
        } break;
        case NPCtype096: {
            InitializeNPCtype096(n);
        } break;
        case NPCtype049: {
            InitializeNPCtype049(n);
        } break;
        case NPCtypeZombie: {
            InitializeNPCtypeZombie(n);
        } break;
        case NPCtypeApache: {
            InitializeNPCtypeApache(n);
        } break;
        case NPCtypeTentacle: {
            InitializeNPCtypeTentacle(n);
        } break;
        case NPCtype860: {
            InitializeNPCtype860(n);
        } break;
        case NPCtype939: {
            InitializeNPCtype939(n);
        } break;
        case NPCtype066: {
            InitializeNPCtype066(n);
        } break;
        case NPCtype966: {
            InitializeNPCtype966(n);
        } break;
        case NPCtype1499: {
            InitializeNPCtype1499(n);
        } break;
    }

    bbPositionEntity(n->collider, x, y, z, true);
    bbPositionEntity(n->obj, x, y, z, true);

    bbResetEntity(n->collider);

    n->id = 0;
    n->id = FindFreeNPCID();

    std::cout << "Created NPC "+String(n->nvName)+" (ID: "+String(n->id)+")\n";

    NPCSpeedChange(n);

    return n;
}

void LoadOrCopyMesh(NPC* n, const String& filePath) {
    NPC* n2;
    for (int iterator110 = 0; iterator110 < NPC::getListSize(); iterator110++) {
        n2 = NPC::getObject(iterator110);

        if (n->npcType == n2->npcType && n != n2) {
            if ((n2->obj != 0)) {
                n->obj = bbCopyMeshModelEntity(n2->obj);
                return;
            }
        }
    }

    n->obj = bbLoadAnimMesh(filePath);
}

void RemoveNPC(NPC* n) {
    if (n == nullptr) {
        return;
    }

    if (n->obj2 != 0) {
        bbFreeEntity(n->obj2);
        n->obj2 = 0;
    }
    if (n->obj3 != 0) {
        bbFreeEntity(n->obj3);
        n->obj3 = 0;
    }
    if (n->obj4 != 0) {
        bbFreeEntity(n->obj4);
        n->obj4 = 0;
    }

    NPCStopAllChannels(n);

    int i;
    for (i = 0; i <= NPC_SOUND_COUNT-1; i++) {
        if (n->sounds[i] != 0) {
            bbFreeSound(n->sounds[i]);
        }
    }

    bbFreeEntity(n->obj);
    n->obj = 0;
    bbFreeEntity(n->collider);
    n->collider = 0;

    delete(n);
}

void UpdateNPCs() {
    NPC* n;
    int i;
    float gravityDist;
    int collidedFloor;

    for (int iterator111 = 0; iterator111 < NPC::getListSize(); iterator111++) {
        n = NPC::getObject(iterator111);

        //A variable to determine if the NPC is in the facility or not
        //TODO: remove because this is practically useless
        n->inFacility = CheckForNPCInFacility(n);

        n->playerDistance = bbEntityDistance(mainPlayer->collider, n->collider);

        //If the npc was given a target then use its position.
        if (n->target != nullptr) {
            n->targetX = bbEntityX(n->target->collider);
            n->targetY = bbEntityY(n->target->collider);
            n->targetZ = bbEntityZ(n->target->collider);
        }

        //If the player is too far away then don't run the update code.
        if (n->playerDistance >= HideDistance*2) {
            //TODO: Implement roaming code.
            return;
        }

        switch (n->npcType) {
            case NPCtype173: {
                UpdateNPCtype173(n);
            } break;
            case NPCtype106: {
                UpdateNPCtype106(n);
            } break;
            case NPCtype096: {
                UpdateNPCtype096(n);
            } break;
            case NPCtype049: {
                UpdateNPCtype049(n);
            } break;
            case NPCtypeZombie: {
                UpdateNPCtypeZombie(n);
            } break;
            case NPCtypeGuard: {
                UpdateNPCtypeGuard(n);
            } break;
            case NPCtypeMTF: {
                UpdateNPCtypeMTF(n);
            } break;
            case NPCtypeD: {
                UpdateNPCtypeD(n);
            } break;
            case NPCtypeApache: {
                UpdateNPCtypeApache(n);
            } break;
            case NPCtypeTentacle: {
                UpdateNPCtypeTentacle(n);
            } break;
            case NPCtype860: {
                UpdateNPCtype860(n);
            } break;
            case NPCtype939: {
                UpdateNPCtype939(n);
            } break;
            case NPCtype066: {
                UpdateNPCtype066(n);
            } break;
            case NPCtype966: {
                UpdateNPCtype966(n);
            } break;
            case NPCtype1499: {
                UpdateNPCtype1499(n);
            } break;
        }

        if (n->isDead) {
            bbEntityType(n->collider, HIT_DEAD);
        }

        //Update sound locations.
        for (i = 0; i <= 2; i++) {
            if (bbChannelPlaying(n->soundChannels[i])) {
                UpdateRangedSoundOrigin(n->soundChannels[i], mainPlayer->cam, n->obj);
            }
        }

        //TODO: Rework.
        gravityDist = Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(n->collider),bbEntityZ(n->collider));

        if (gravityDist<HideDistance*0.7f || n->npcType == NPCtype1499) {
            if (n->inFacility == InFacility) {
                bbTranslateEntity(n->collider, 0, n->dropSpeed, 0);

                collidedFloor = false;
                for (i = 1; i <= bbCountCollisions(n->collider); i++) {
                    if (bbCollisionY(n->collider, i) < bbEntityY(n->collider) - 0.01f) {
                        collidedFloor = true;
                        break;
                    }
                }

                if (collidedFloor == true) {
                    n->dropSpeed = 0;
                } else {
                    n->dropSpeed = Max(n->dropSpeed - 0.005f*timing->tickDuration*n->gravityMult,-n->maxGravity);
                }
            } else {
                n->dropSpeed = 0;
            }
        } else {
            n->dropSpeed = 0;
        }
    }

}

void NPCStopAllChannels(NPC* n) {
    int i;
    for (i = 0; i <= NPC_CHANNEL_COUNT-1; i++) {
        if (bbChannelPlaying(n->soundChannels[i])) {
            bbStopChannel(n->soundChannels[i]);
        }
    }
}

void TeleportCloser(NPC* n) {
    float closestDist = 0;
    WayPoint* closestWaypoint = nullptr;
    WayPoint* w;

    float xtemp;
    float ztemp;
    float newDist;

    for (int iterator112 = 0; iterator112 < WayPoint::getListSize(); iterator112++) {
        w = WayPoint::getObject(iterator112);

        //If (w\door = Null) Then ;TODO: fix?
        xtemp = abs(bbEntityX(w->obj,true)-bbEntityX(n->collider,true));
        if (xtemp < 10.f && xtemp > 1.f) {
            ztemp = abs(bbEntityZ(w->obj,true)-bbEntityZ(n->collider,true));
            if (ztemp < 10.f && ztemp > 1.f) {
                if (bbEntityDistanceSquared(mainPlayer->collider, w->obj)>64) {
                    if (SelectedDifficulty->aggressiveNPCs) {
                        //teleports to the nearby waypoint that takes it closest to the player
                        newDist = bbEntityDistanceSquared(mainPlayer->collider, w->obj);
                        if (newDist < closestDist || closestWaypoint == nullptr) {
                            closestDist = newDist;
                            closestWaypoint = w;
                        }
                    } else {
                        //just teleports to the first nearby waypoint it finds
                        closestWaypoint = w;
                        break;
                    }
                }
            }
        }
        //EndIf
    }

    if (closestWaypoint!=nullptr) {
        bbPositionEntity(n->collider, bbEntityX(closestWaypoint->obj,true), bbEntityY(closestWaypoint->obj,true)+0.15f, bbEntityZ(closestWaypoint->obj,true), true);
        bbResetEntity(n->collider);
    }

}

int OtherNPCSeesMeNPC(NPC* me, NPC* other) {
    if (other->blinkTimer<=0.f) {
        return false;
    }

    if (bbEntityDistanceSquared(other->collider,me->collider)<36.f) {
        if (abs(bbDeltaYaw(other->collider,me->collider))<60.f) {
            return true;
        }
    }
    return false;
}

int MeNPCSeesPlayer(NPC* me, int disableSoundOnCrouch) {
    //Return values:
    //False (=0): Player is not detected anyhow
    //True (=1): Player is detected by vision
    //2: Player is detected by emitting a sound
    //3: Player is detected by a camera (only for MTF Units!)
    //4: Player is detected through glass

    if (NoTarget) {
        return false;
    }

    if ((!PlayerDetected) || me->npcType != NPCtypeMTF) {
        if (me->blinkTimer<=0.f) {
            return false;
        }
        float detectDist = (8.f - mainPlayer->crouchState + mainPlayer->loudness);
        if (bbEntityDistanceSquared(mainPlayer->collider,me->collider)>detectDist*detectDist) {
            return false;
        }

        //spots the player if he's either in view or making a loud sound
        if (mainPlayer->loudness>1.f) {
            if (abs(bbDeltaYaw(me->collider,mainPlayer->collider))>60.f && bbEntityVisible(me->collider,mainPlayer->collider)) {
                return 1;
            } else if ((!bbEntityVisible(me->collider,mainPlayer->collider))) {
                if (disableSoundOnCrouch && mainPlayer->crouching) {
                    return false;
                } else {
                    return 2;
                }
            }
        } else {
            if (abs(bbDeltaYaw(me->collider,mainPlayer->collider))>60.f) {
                return false;
            }
        }
        return bbEntityVisible(me->collider,mainPlayer->collider);
    } else {
        float detectDist = (8.f - mainPlayer->crouchState + mainPlayer->loudness);
        if (bbEntityDistanceSquared(mainPlayer->collider,me->collider)>detectDist*detectDist) {
            return 3;
        }
        if (bbEntityVisible(me->collider, mainPlayer->cam)) {
            return true;
        }

        //spots the player if he's either in view or making a loud sound
        if (mainPlayer->loudness>1.f) {
            return 2;
        }
        return 3;
    }

}

void TeleportMTFGroup(NPC* n) {
    NPC* n2;

    if (n->mtfLeader != nullptr) {
        return;
    }

    TeleportCloser(n);

    for (int iterator113 = 0; iterator113 < NPC::getListSize(); iterator113++) {
        n2 = NPC::getObject(iterator113);

        if (n2->npcType == NPCtypeMTF) {
            if (n2->mtfLeader != nullptr) {
                bbPositionEntity(n2->collider,bbEntityX(n2->mtfLeader->collider),bbEntityY(n2->mtfLeader->collider)+0.1f,bbEntityZ(n2->mtfLeader->collider));
            }
        }
    }

    std::cout << "Teleported MTF Group (dist:"+String(bbEntityDistance(n->collider,mainPlayer->collider))+")";

}

void Shoot(float x, float y, float z, float hitProb, int particles, int instaKill) {
    String shotMessageUpdate;
    int wearingVest;
    Pivot* pvt;
    int i;
    Decal* de;

    //muzzle flash
    Particle* p = CreateParticle(x,y,z, PARTICLE_FLASH, bbRnd(0.08f,0.1f), 0.f, 5);
    bbTurnEntity(p->sprite, 0,0,bbRnd(360));
    p->aChange = -0.15f;

    //LightVolume = TempLightVolume*1.2f

    if (!mainPlayer->godMode) {

        if (instaKill) {
            Kill(mainPlayer);
            PlaySound_SM(sndManager->bulletHit);
            return;
        }

        if (bbRnd(1.f) <= hitProb) {
            bbTurnEntity(mainPlayer->cam, bbRnd(-3,3), bbRnd(-3,3), 0);

            wearingVest = false;
            wearingVest |= IsPlayerWearingItem(mainPlayer,"vest");
            wearingVest |= IsPlayerWearingItem(mainPlayer,"finevest");
            wearingVest |= IsPlayerWearingItem(mainPlayer,"veryfinevest");
            if (wearingVest) {
                if (IsPlayerWearingItem(mainPlayer,"vest")) {
                    switch (bbRand(8)) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5: {
                            mainPlayer->blurTimer = 500;
                            mainPlayer->stamina = 0;
                            shotMessageUpdate = "A bullet penetrated your vest, making you gasp.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.1f,0.5f);
                        } break;
                        case 6: {
                            mainPlayer->blurTimer = 500;
                            shotMessageUpdate = "A bullet hit your left leg.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                        } break;
                        case 7: {
                            mainPlayer->blurTimer = 500;
                            shotMessageUpdate = "A bullet hit your right leg.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                        } break;
                        case 8: {
                            mainPlayer->blurTimer = 500;
                            mainPlayer->stamina = 0;
                            shotMessageUpdate = "A bullet struck your neck, making you gasp.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(1.2f,1.6f);
                        } break;
                    }
                } else {
                    if (bbRand(10)==1) {
                        mainPlayer->blurTimer = 500;
                        mainPlayer->stamina = mainPlayer->stamina - 1;
                        shotMessageUpdate = "A bullet hit your chest. The vest absorbed some of the damage.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.1f);
                    } else {
                        shotMessageUpdate = "A bullet hit your chest. The vest absorbed most of the damage.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.1f,0.5f);
                    }
                }

                if (mainPlayer->injuries >= 3) {
                    if (bbRand(3) == 1) {
                        Kill(mainPlayer);
                    }
                }
            } else {
                switch (bbRand(6)) {
                    case 1: {
                        Kill(mainPlayer);
                    } break;
                    case 2: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your left leg.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                    } break;
                    case 3: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right leg.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                    } break;
                    case 4: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                    } break;
                    case 5: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your left shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8f,1.2f);
                    } break;
                    case 6: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(2.5f,4.f);
                    } break;
                }
            }

            //Only updates the message if it's been more than two seconds.
            if (MsgTimer < 64*4) {
                Msg = shotMessageUpdate;
                MsgTimer = 70*6;
            }

            mainPlayer->injuries = Min(mainPlayer->injuries, 4.f);

            //Kill(mainPlayer)
            PlaySound_SM(sndManager->bulletHit);
        } else if ((particles)) {
            pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),(bbEntityY(mainPlayer->collider)+bbEntityY(mainPlayer->cam))/2,bbEntityZ(mainPlayer->collider));
            bbPointEntity(pvt, p->sprite);
            bbTurnEntity(pvt, 0, 180, 0);

            bbEntityPick(pvt, 2.5f);

            if (bbPickedEntity() != 0) {
                PlayRangedSound_SM(sndManager->bulletMiss, mainPlayer->cam, pvt, 0.4f, bbRnd(0.8f,1.f));

                if (particles) {
                    //dust/smoke particles
                    p = CreateParticle(bbPickedX(),bbPickedY(),bbPickedZ(), PARTICLE_SMOKE_BLACK, 0.03f, 0, 80);
                    p->speed = 0.001f;
                    p->sizeChange = 0.003f;
                    p->a = 0.8f;
                    p->aChange = -0.01f;
                    bbRotateEntity(p->pvt, bbEntityPitch(pvt)-180, bbEntityYaw(pvt),0);

                    for (i = 0; i <= bbRand(2,3); i++) {
                        p = CreateParticle(bbPickedX(),bbPickedY(),bbPickedZ(), PARTICLE_SMOKE_BLACK, 0.006f, 0.003f, 80);
                        p->speed = 0.02f;
                        p->a = 0.8f;
                        p->aChange = -0.01f;
                        bbRotateEntity(p->pvt, bbEntityPitch(pvt)+bbRnd(170,190), bbEntityYaw(pvt)+bbRnd(-10,10),0);
                    }

                    //bullet hole decal
                    de = CreateDecal(bbRand(DECAL_BULLET_HOLE1, DECAL_BULLET_HOLE2), bbPickedX(),bbPickedY(),bbPickedZ(), 0,0,0);
                    bbAlignToVector(de->obj,-bbPickedNX(),-bbPickedNY(),-bbPickedNZ(),3);
                    bbMoveEntity(de->obj, 0,0,-0.001f);
                    bbEntityFX(de->obj, 1);
                    de->lifetime = 70*20;
                    bbEntityBlend(de->obj, 2);
                    de->size = bbRnd(0.028f,0.034f);
                    bbScaleSprite(de->obj, de->size, de->size);
                }
            }
            bbFreeEntity(pvt);

        }

    }

}

void PlayMTFSound(gxSound* sound, NPC* n) {
    if (n != nullptr) {
        n->soundChannels[0] = PlayRangedSound(sound, mainPlayer->cam, n->collider, 8.f);
    }

    //TODO: Re-implement.
    //If (mainPlayer\selectedItem <> Null) Then
    //	If (mainPlayer\selectedItem\state2 = 3 And mainPlayer\selectedItem\state > 0) Then
    //		Select (mainPlayer\selectedItem\template\name)
    //			Case "radio","fineradio","18vradio"
    //				If (RadioCHN(3)<> 0) Then StopChannel(RadioCHN(3))
    //				RadioCHN(3) = PlaySound(sound)
    //		End Select
    //	EndIf
    //EndIf
}

// TODO: Move somewhere relevant.
void MoveToPocketDimension() {
    Room* r;

    for (int iterator114 = 0; iterator114 < Room::getListSize(); iterator114++) {
        r = Room::getObject(iterator114);

        if (r->roomTemplate->name.equals("pocketdimension")) {
            mainPlayer->fallTimer = 0;
            UpdateDoors();
            UpdateRooms();
            bbShowEntity(mainPlayer->collider);
            PlaySound2(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"));
            //PlaySound2(OldManSFX(5)) ;TODO: fix
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj),0.8f,bbEntityZ(r->obj));
            mainPlayer->dropSpeed = 0;
            bbResetEntity(mainPlayer->collider);

            mainPlayer->blinkTimer = -10;

            mainPlayer->injuries = mainPlayer->injuries+0.5f;

            mainPlayer->currRoom = r;

            return;
        }
    }
}

int FindFreeNPCID() {
    int taken;
    NPC* n2;
    int id = 1;

    while ((true)) {
        taken = false;
        for (int iterator115 = 0; iterator115 < NPC::getListSize(); iterator115++) {
            n2 = NPC::getObject(iterator115);

            if (n2->id == id) {
                taken = true;
                break;
            }
        }
        if (!taken) {
            return id;
        }
        id = id + 1;
    }
}

void ForceSetNPCID(NPC* n, int newID) {
    n->id = newID;

    NPC* n2;
    for (int iterator116 = 0; iterator116 < NPC::getListSize(); iterator116++) {
        n2 = NPC::getObject(iterator116);

        if (n2 != n && n2->id == newID) {
            n2->id = FindFreeNPCID();
        }
    }
}

void Console_SpawnNPC(const String& npcName, int state) {
    //NPC* n;

    // switch (c_input) {
    //     case "mtf": {
    //         n = CreateNPC(NPCtypeMTF, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "173","scp173","scp-173": {
    //         n = CreateNPC(NPCtype173, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "106","scp106","scp-106","larry": {
    //         n = CreateNPC(NPCtype106, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "guard": {
    //         n = CreateNPC(NPCtypeGuard, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "096","scp096","scp-096": {
    //         n = CreateNPC(NPCtype096, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //         if (Curr096 == nullptr) {
    //             Curr096 = n;
    //         }
    //     }
    //     case "049","scp049","scp-049": {
    //         n = CreateNPC(NPCtype049, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //         if (state==-9999) {
    //             n->state = 2;
    //         }
    //     }
    //     case "zombie","scp-049-2": {
    //         n = CreateNPC(NPCtypeZombie, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //         if (state==-9999) {
    //             n->state = 1;
    //         }
    //     }
    //     case "966", "scp966", "scp-966": {
    //         n = CreateNPC(NPCtype966, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "class-d","classd","d": {
    //         n = CreateNPC(NPCtypeD, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "apache": {
    //         n = CreateNPC(NPCtypeApache, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "513-1","scp513-1","scp-513-1": {
    //         n = CreateNPC(NPCtype5131, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "tentacle": {
    //         n = CreateNPC(NPCtypeTentacle, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider));
    //     }
    //     case "860-2","scp860-2","scp-860-2": {
    //         n = CreateNPC(NPCtype860, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "939","scp939","scp-939": {
    //         n = CreateNPC(NPCtype939, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //         if (state==-9999) {
    //             n->state = 1;
    //         }
    //     }
    //     case "066","scp066","scp-066": {
    //         n = CreateNPC(NPCtype066, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "pdplane": {
    //         n = CreateNPC(NPCtypePdPlane, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     case "scp-1499-1","scp1499-1","1499-1": {
    //         n = CreateNPC(NPCtype1499, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2f,bbEntityZ(mainPlayer->collider));
    //     }
    //     default: {
    //         ConsoleMsg::create("NPC type not found.");
    //     }
    // }

    // if (n != nullptr) {
    //     if (state!=-9999) {
    //         n->state = state;
    //     }
    // }

}

// TODO: Remove all this.
void ManipulateNPCBones() {
    NPC* n;
    Object* bone = nullptr;
    Object* bone2 = nullptr;
    Pivot* pvt;
    String bonename;
    String bonename2;
    float pitchvalue;
    float yawvalue;
    float rollvalue;
    float pitchoffset;
    float yawoffset;
    float rolloffset;

    for (int iterator117 = 0; iterator117 < NPC::getListSize(); iterator117++) {
        n = NPC::getObject(iterator117);

        if (n->manipulateBone) {
            pitchvalue = 0;
            yawvalue = 0;
            rollvalue = 0;
            pitchoffset = TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"pitchoffset");
            yawoffset = TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yawoffset");
            rolloffset = TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"rolloffset");
            pvt = bbCreatePivot();
            bonename = GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"bonename",0);
            bone = bbFindChild(n->obj,bonename);
            if (bone == 0) {
                throw ("ERROR: NPC bone \""+bonename+"\" does not exist.");
            }
            if (!n->boneToManipulate2.isEmpty()) {
                bonename2 = GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"navbone",0);
                bone2 = bbFindChild(n->obj,n->boneToManipulate2);
                if (bone2 == 0) {
                    throw ("ERROR: NPC bone \""+bonename2+"\" does not exist.");
                }
            }
            bbPositionEntity(pvt,bbEntityX(bone,true),bbEntityY(bone,true),bbEntityZ(bone,true));
            switch (n->manipulationType) {
                case 0: {
                    bbPointEntity(bone,mainPlayer->cam);
                    bbPointEntity(pvt,mainPlayer->cam);
                    n->bonePitch = CurveAngle(bbEntityPitch(pvt),n->bonePitch,10.f);
                    switch ((int)TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            n->boneYaw = CurveAngle(bbEntityPitch(bone),n->boneYaw,10.f);
                            pitchvalue = n->boneYaw;
                        } break;
                        case 1: {
                            n->boneYaw = CurveAngle(bbEntityYaw(bone),n->boneYaw,10.f);
                            yawvalue = n->boneYaw;
                        } break;
                        case 2: {
                            n->boneYaw = CurveAngle(bbEntityRoll(bone),n->boneYaw,10.f);
                            rollvalue = n->boneYaw;
                        } break;
                    }
                    switch ((int)TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"pitch")) {
                        case 0: {
                            pitchvalue = n->bonePitch;
                        } break;
                        case 1: {
                            yawvalue = n->bonePitch;
                        } break;
                        case 2: {
                            rollvalue = n->bonePitch;
                        } break;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3).toInt()==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3).toInt()==true) {
                        yawvalue = -yawvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3).toInt()==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<--- looking at player #2
                } break;
                case 1: {
                    n->bonePitch = CurveAngle(bbDeltaPitch(bone2,mainPlayer->cam),n->bonePitch,10.f);
                    switch ((int)TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"pitch")) {
                        case 0: {
                            pitchvalue = n->bonePitch;
                        }
                        case 1: {
                            yawvalue = n->bonePitch;
                        }
                        case 2: {
                            rollvalue = n->bonePitch;
                        }
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3).toInt()==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3).toInt()==true) {
                        yawvalue = -yawvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3).toInt()==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<--- looking away from SCP-096
                } break;
                case 2: {
                    bbPointEntity(bone,Curr096->obj);
                    switch ((int)TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            n->boneYaw = CurveAngle(bbEntityPitch(bone),n->boneYaw,10.f);
                            pitchvalue = -n->boneYaw;
                        }
                        case 1: {
                            n->boneYaw = CurveAngle(bbEntityYaw(bone),n->boneYaw,10.f);
                            yawvalue = -n->boneYaw;
                        }
                        case 2: {
                            n->boneYaw = CurveAngle(bbEntityRoll(bone),n->boneYaw,10.f);
                            rollvalue = -n->boneYaw;
                        }
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3).toInt()==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3).toInt()==true) {
                        yawvalue = -yawvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3).toInt()==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<-- looking and pitching towards the player
                } break;
                case 3: {
                    bbPointEntity(pvt,mainPlayer->cam);
                    n->boneYaw = CurveAngle(bbEntityPitch(pvt),n->boneYaw,10.f);
                    switch ((int)TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            pitchvalue = n->boneYaw;
                        } break;
                        case 1: {
                            yawvalue = n->boneYaw;
                        } break;
                        case 2: {
                            rollvalue = n->boneYaw;
                        } break;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3).toInt()==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3).toInt()==true) {
                        yawvalue = -yawvalue;
                    }
                    if (GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3).toInt()==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                }
            }
            bbFreeEntity(pvt);
        } break;
    }

}

String GetNPCManipulationValue(const String& NPC, const String& bone, const String& section, int valuetype) {
    //valuetype determines what type of variable should the Output be returned
    //0 - String
    //1 - Int
    //2 - Float
    //3 - Boolean

    String value = GetINIString("Data/NPCBones.ini",NPC,bone+"_"+section);
    switch (valuetype) {
        case 0:
        case 1:
        case 2: {
            return value;
        } break;
        case 3: {
            if (value.equals("true") || value.equals("1")) {
                return "1";
            } else {
                return "0";
            }
        } break;
        default: {
            return "0";
        }
    }

}

// TODO: This is the biggest load of shit ever. Why would you create a function that sometimes returns a float and sometimes returns an integer?
float TransformNPCManipulationData(const String& NPC, const String& bone, const String& section) {
    //If "section$" = "pitch","yaw" or "roll":
    //	- 0 means "realpitch" value has detected
    //	- 1 means "realyaw" value has detected
    //	- 2 means "realroll" value has detected
    //If "section$" = "pitchoffset","yawoffset","rolloffset":
    //	- simply return the offset degree value using a "return Float"

    // String value = GetNPCManipulationValue(NPC,bone,section);
    // switch (section) {
    //     case "pitch","yaw","roll": {
    //         switch (value) {
    //             case "realpitch": {
    //                 return 0;
    //             }
    //             case "realyaw": {
    //                 return 1;
    //             }
    //             case "realroll": {
    //                 return 2;
    //             }
    //         }
    //     }
    //     case "pitchoffset","yawoffset","rolloffset": {
    //         return (float)(value);
    //     }
    // }
    return 0.f;
}

void NPCSpeedChange(NPC* n) {

    switch (n->npcType) {
        case NPCtype173:
        case NPCtype106:
        case NPCtype096:
        case NPCtype049:
        case NPCtype939:
        case NPCtypeMTF: {
            switch (SelectedDifficulty->otherFactors) {
                case NORMAL: {
                    n->speed = n->speed * 1.1f;
                } break;
                case HARD: {
                    n->speed = n->speed * 1.2f;
                } break;
            }
        } break;
    }

}

int PlayerInReachableRoom() {
    String RN = mainPlayer->currRoom->roomTemplate->name;
    int temp;

    //Player is in these rooms, returning false
    if (RN.equals("pocketdimension") | RN.equals("gatea") | RN.equals("dimension1499") || RN.equals("173")) {
        return false;
    }
    //Player is at GateB and is at the surface, returning false
    if (RN.equals("exit1") && bbEntityY(mainPlayer->collider)>1040.f*RoomScale) {
        return false;
    }
    //Player is in 860's test room and inside the forest, returning false
    temp = false;
    /*for (int iterator118 = 0; iterator118 < Event::getListSize(); iterator118++) {
        Event* e = Event::getObject(iterator118);

        if (e->name.equals("room860") && e->eventState == 1.f) {
            temp = true;
            break;
        }
    }
    if (RN.equals("room860") && temp) {
        return false;
    }TODO: this is ridiculous*/
    //Return true, this means player is in reachable room
    return true;

}

int CheckForNPCInFacility(NPC* n) {
    //False (=0): NPC is not in facility (mostly meant for "dimension1499")
    //True (=1): NPC is in facility
    //2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)

    if (bbEntityY(n->collider)>100.f) {
        return false;
    }
    if (bbEntityY(n->collider)< -10.f) {
        return 2;
    }
    if (bbEntityY(n->collider)> 7.f && bbEntityY(n->collider)<=100.f) {
        return 2;
    }

    return true;
}

void FindNextElevator(NPC* n) {
    ElevatorObj* eo;
    ElevatorObj* eo2;

    for (int iterator119 = 0; iterator119 < ElevatorObj::getListSize(); iterator119++) {
        eo = ElevatorObj::getObject(iterator119);

        if (eo->inFacility == n->inFacility) {
            if (abs(bbEntityY(eo->obj,true)-bbEntityY(n->collider))<10.f) {
                for (int iterator120 = 0; iterator120 < ElevatorObj::getListSize(); iterator120++) {
                    eo2 = ElevatorObj::getObject(iterator120);

                    if (eo2 != eo) {
                        if (eo2->inFacility == n->inFacility) {
                            if (abs(bbEntityY(eo2->obj,true)-bbEntityY(n->collider))<10.f) {
                                if (bbEntityDistanceSquared(eo2->obj,n->collider)<bbEntityDistanceSquared(eo->obj,n->collider)) {
                                    n->pathStatus = FindPath(n, bbEntityX(eo2->obj,true),bbEntityY(eo2->obj,true),bbEntityZ(eo2->obj,true));
                                    n->currElevator = eo2;
                                    std::cout << "eo2 found for "+String(n->npcType);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (n->currElevator == nullptr) {
                    n->pathStatus = FindPath(n, bbEntityX(eo->obj,true),bbEntityY(eo->obj,true),bbEntityZ(eo->obj,true));
                    n->currElevator = eo;
                    std::cout << "eo found for "+String(n->npcType);
                }
                if (n->pathStatus != 1) {
                    n->currElevator = nullptr;
                    std::cout << "Unable to find elevator path: Resetting CurrElevator";
                }
                break;
            }
        }
    }

}

void GoToElevator(NPC* n) {
    float dist;
    int inside;

    if (n->pathStatus != 1) {
        bbPointEntity(n->obj,n->currElevator->obj);
        bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),20.f),0);

        inside = false;
        if (abs(bbEntityX(n->collider)-bbEntityX(n->currElevator->obj,true))<280.f*RoomScale) {
            if (abs(bbEntityZ(n->collider)-bbEntityZ(n->currElevator->obj,true))<280.f*RoomScale) {
                if (abs(bbEntityY(n->collider)-bbEntityY(n->currElevator->obj,true))<280.f*RoomScale) {
                    inside = true;
                }
            }
        }

        dist = bbEntityDistanceSquared(n->collider,n->currElevator->door->frameobj);
        if (n->currElevator->door->open) {
            if ((dist > 0.4f*0.4f && dist < 0.7f*0.7f) && inside) {
                UseDoor(n->currElevator->door,false);
                std::cout << String(n->npcType)+" used elevator";
            }
        } else {
            if (dist < 0.7f*0.7f) {
                n->currSpeed = 0.f;
                if (n->currElevator->door->npcCalledElevator==false) {
                    n->currElevator->door->npcCalledElevator = true;
                    std::cout << String(n->npcType)+" called elevator";
                }
            }
        }
    }

}

void FinishWalking(NPC* n, float startframe, float endframe, float speed) {
    float centerframe;

    if (n!=nullptr) {
        centerframe = (endframe-startframe)/2;
        if (n->frame >= centerframe) {
            AnimateNPC(n,startframe,endframe,speed,false);
        } else {
            AnimateNPC(n,endframe,startframe,-speed,false);
        }
    }

}

void RotateToDirection(NPC* n) {
    int turnToSide;

    bbHideEntity(n->collider);
    bbEntityPick(n->collider, 1.f);
    if (bbPickedEntity() != 0) {
        turnToSide = 0;
        bbTurnEntity(n->collider,0,90,0);
        bbEntityPick(n->collider,1.f);
        if (bbPickedEntity()==0) {
            turnToSide = 1;
        }
        bbTurnEntity(n->collider,0,270,0);
        if (turnToSide == 1) {
            bbTurnEntity(n->collider,0.f,45,0.f,true);
        } else {
            bbTurnEntity(n->collider,0.f,-45,0.f,true);
        }
    }
    bbShowEntity(n->collider);

}

void AnimateNPC(NPC* n, float start, float quit, float speed, int loop) {
    float newTime;
    float temp;

    if (speed > 0.f) {
        newTime = Max(Min(n->frame + speed * timing->tickDuration, quit), start);

        if (loop && newTime >= quit) {
            newTime = start;
        }
    } else {
        if (start < quit) {
            temp = start;
            start = quit;
            quit = temp;
        }

        if (loop) {
            newTime = n->frame + speed * timing->tickDuration;

            if (newTime < quit) {
                newTime = start;
            } else if ((newTime > start)) {
                newTime = quit;
            }
        } else {
            newTime = Max(Min(n->frame + speed * timing->tickDuration, start), quit);
        }
    }
    SetNPCFrame(n, newTime);

}

void SetNPCFrame(NPC* n, float frame) {
    if (abs(n->frame-frame)<0.001f) {
        return;
    }

    bbSetAnimTime(n->obj, frame);

    n->frame = frame;
}

}

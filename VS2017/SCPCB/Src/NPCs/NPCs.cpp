#include "NPCs.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<NPC*> NPC::list;
NPC::NPC() {
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

// Constants.
const int NPCtype173 = 1;
const int NPCtype106 = 2;
const int NPCtype049 = 3;
const int NPCtype096 = 4;
const int NPCtype966 = 5;
const int NPCtype860 = 6;
const int NPCtype939 = 7;
const int NPCtype5131 = 8;
const int NPCtype066 = 10;
const int NPCtypeD = 11;
const int NPCtypeGuard = 12;
const int NPCtypeMTF = 13;
const int NPCtypeApache = 14;
const int NPCtypeZombie = 15;
const int NPCtypeTentacle = 16;
const int NPCtype1499 = 17;
const int NPCtypePdPlane = 18;
const int NPC_SOUND_COUNT = 12;
const int NPC_CHANNEL_COUNT = 3;

// Globals.
NPC* Curr173;
NPC* Curr106;
NPC* Curr096;

// Functions.
NPC* CreateNPC(int NPCtype, float x, float y, float z) {
    NPC* n = new NPC();
    NPC* n2;
    float temp;
    int i;
    int diff1;
    int bump1;
    int spec1;
    int sf;
    int b;
    int t1;

    n->npcType = NPCtype;
    n->gravityMult = 1.0;
    n->maxGravity = 0.2;
    switch (NPCtype) {
        case NPCtype173: {
            InitializeNPCtype173(n);
        }
        case NPCtype106: {
            InitializeNPCtype106(n);
        }
        case NPCtypeGuard: {
            InitializeNPCtypeGuard(n);
        }
        case NPCtypeMTF: {
            InitializeNPCtypeMTF(n);
        }
        case NPCtypeD: {
            InitializeNPCtypeD(n);
        }
        case NPCtype096: {
            InitializeNPCtype096(n);
        }
        case NPCtype049: {
            InitializeNPCtype049(n);
        }
        case NPCtypeZombie: {
            InitializeNPCtypeZombie(n);
        }
        case NPCtypeApache: {
            InitializeNPCtypeApache(n);
        }
        case NPCtypeTentacle: {
            InitializeNPCtypeTentacle(n);
        }
        case NPCtype860: {
            InitializeNPCtype860(n);
        }
        case NPCtype939: {
            InitializeNPCtype939(n);
        }
        case NPCtype066: {
            InitializeNPCtype066(n);
        }
        case NPCtype966: {
            InitializeNPCtype966(n);
        }
        case NPCtype1499: {
            InitializeNPCtype1499(n);
        }
    }

    bbPositionEntity(n->collider, x, y, z, true);
    bbPositionEntity(n->obj, x, y, z, true);

    bbResetEntity(n->collider);

    n->id = 0;
    n->id = FindFreeNPCID();

    bbDebugLog("Created NPC "+String(n->nvName)+" (ID: "+String(n->id)+")");

    NPCSpeedChange(n);

    return n;
}

void LoadOrCopyMesh(NPC* n, String filePath) {
    NPC* n2;
    for (int iterator110 = 0; iterator110 < NPC::getListSize(); iterator110++) {
        n2 = NPC::getObject(iterator110);

        if (n->npcType == n2->npcType & n != n2) {
            if ((n2->obj != 0)) {
                n->obj = bbCopyEntity(n2->obj);
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
            }
            case NPCtype106: {
                UpdateNPCtype106(n);
            }
            case NPCtype096: {
                UpdateNPCtype096(n);
            }
            case NPCtype049: {
                UpdateNPCtype049(n);
            }
            case NPCtypeZombie: {
                UpdateNPCtypeZombie(n);
            }
            case NPCtypeGuard: {
                UpdateNPCtypeGuard(n);
            }
            case NPCtypeMTF: {
                UpdateNPCtypeMTF(n);
            }
            case NPCtypeD: {
                UpdateNPCtypeD(n);
            }
            case NPCtypeApache: {
                UpdateNPCtypeApache(n);
            }
            case NPCtypeTentacle: {
                UpdateNPCtypeTentacle(n);
            }
            case NPCtype860: {
                UpdateNPCtype860(n);
            }
            case NPCtype939: {
                UpdateNPCtype939(n);
            }
            case NPCtype066: {
                UpdateNPCtype066(n);
            }
            case NPCtype966: {
                UpdateNPCtype966(n);
            }
            case NPCtype1499: {
                UpdateNPCtype1499(n);
            }
        }

        if (n->isDead) {
            bbEntityType(n->collider, HIT_DEAD);
        }

        //Update sound locations.
        for (i = 0; i <= 2; i++) {
            if (IsChannelPlaying(n->soundChannels[i])) {
                UpdateRangedSoundOrigin(n->soundChannels[i], mainPlayer->cam, n->obj);
            }
        }

        //TODO: Rework.
        gravityDist = Distance(bbEntityX(mainPlayer->collider),bbEntityZ(mainPlayer->collider),bbEntityX(n->collider),bbEntityZ(n->collider));

        if (gravityDist<HideDistance*0.7 | n->npcType == NPCtype1499) {
            if (n->inFacility == InFacility) {
                bbTranslateEntity(n->collider, 0, n->dropSpeed, 0);

                collidedFloor = false;
                for (i = 1; i <= bbCountCollisions(n->collider); i++) {
                    if (bbCollisionY(n->collider, i) < bbEntityY(n->collider) - 0.01) {
                        collidedFloor = true;
                        break;
                    }
                }

                if (collidedFloor == true) {
                    n->dropSpeed = 0;
                } else {
                    n->dropSpeed = Max(n->dropSpeed - 0.005*timing->tickDuration*n->gravityMult,-n->maxGravity);
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
        if (IsChannelPlaying(n->soundChannels[i])) {
            bbStopChannel(n->soundChannels[i]);
        }
    }
}

void TeleportCloser(NPC* n) {
    float closestDist = 0;
    WayPoint* closestWaypoint;
    WayPoint* w;

    float xtemp;
    float ztemp;
    float newDist;

    for (int iterator112 = 0; iterator112 < WayPoint::getListSize(); iterator112++) {
        w = WayPoint::getObject(iterator112);

        //If (w\door = Null) Then ;TODO: fix?
        xtemp = Abs(bbEntityX(w->obj,true)-bbEntityX(n->collider,true));
        if (xtemp < 10.0 & xtemp > 1.0) {
            ztemp = Abs(bbEntityZ(w->obj,true)-bbEntityZ(n->collider,true));
            if (ztemp < 10.0 & ztemp > 1.0) {
                if (bbEntityDistance(mainPlayer->collider, w->obj)>8) {
                    if (SelectedDifficulty->aggressiveNPCs) {
                        //teleports to the nearby waypoint that takes it closest to the player
                        newDist = bbEntityDistance(mainPlayer->collider, w->obj);
                        if (newDist < closestDist | closestWaypoint == nullptr) {
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
        bbPositionEntity(n->collider, bbEntityX(closestWaypoint->obj,true), bbEntityY(closestWaypoint->obj,true)+0.15, bbEntityZ(closestWaypoint->obj,true), true);
        bbResetEntity(n->collider);
    }

}

int OtherNPCSeesMeNPC(NPC* me, NPC* other) {
    if (other->blinkTimer<=0.0) {
        return false;
    }

    if (bbEntityDistance(other->collider,me->collider)<6.0) {
        if (Abs(bbDeltaYaw(other->collider,me->collider))<60.0) {
            return true;
        }
    }
    return false;
}

int MeNPCSeesPlayer(NPC* me, int disableSoundOnCrouch = false) {
    //Return values:
    //False (=0): Player is not detected anyhow
    //True (=1): Player is detected by vision
    //2: Player is detected by emitting a sound
    //3: Player is detected by a camera (only for MTF Units!)
    //4: Player is detected through glass

    if (NoTarget) {
        return false;
    }

    if ((!PlayerDetected) | me->npcType != NPCtypeMTF) {
        if (me->blinkTimer<=0.0) {
            return false;
        }
        if (bbEntityDistance(mainPlayer->collider,me->collider)>(8.0-mainPlayer->crouchState+mainPlayer->loudness)) {
            return false;
        }

        //spots the player if he's either in view or making a loud sound
        if (mainPlayer->loudness>1.0) {
            if (Abs(bbDeltaYaw(me->collider,mainPlayer->collider))>60.0) & bbEntityVisible(me->collider,mainPlayer->collider) {
                return 1;
            } else if ((!bbEntityVisible(me->collider,mainPlayer->collider))) {
                if (disableSoundOnCrouch & mainPlayer->crouching) {
                    return false;
                } else {
                    return 2;
                }
            }
        } else {
            if (Abs(bbDeltaYaw(me->collider,mainPlayer->collider))>60.0) {
                return false;
            }
        }
        return bbEntityVisible(me->collider,mainPlayer->collider);
    } else {
        if (bbEntityDistance(mainPlayer->collider,me->collider)>(8.0-mainPlayer->crouchState+mainPlayer->loudness)) {
            return 3;
        }
        if (bbEntityVisible(me->collider, mainPlayer->cam)) {
            return true;
        }

        //spots the player if he's either in view or making a loud sound
        if (mainPlayer->loudness>1.0) {
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
                bbPositionEntity(n2->collider,bbEntityX(n2->mtfLeader->collider),bbEntityY(n2->mtfLeader->collider)+0.1,bbEntityZ(n2->mtfLeader->collider));
            }
        }
    }

    bbDebugLog("Teleported MTF Group (dist:"+String(bbEntityDistance(n->collider,mainPlayer->collider))+")");

}

void Shoot(float x, float y, float z, float hitProb = 1.0, int particles = true, int instaKill = false) {
    String shotMessageUpdate;
    int wearingVest;
    int pvt;
    int i;
    Decal* de;

    //muzzle flash
    Particle* p = CreateParticle(x,y,z, PARTICLE_FLASH, bbRnd(0.08,0.1), 0.0, 5);
    bbTurnEntity(p->obj, 0,0,bbRnd(360));
    p->aChange = -0.15;

    //LightVolume = TempLightVolume*1.2

    if (!mainPlayer->godMode) {

        if (instaKill) {
            Kill(mainPlayer);
            PlaySound_SM(sndManager->bulletHit);
            return;
        }

        if (bbRnd(1.0) <= hitProb) {
            bbTurnEntity(mainPlayer->cam, bbRnd(-3,3), bbRnd(-3,3), 0);

            wearingVest = false;
            wearingVest = wearingVest | IsPlayerWearingItem(mainPlayer,"vest");
            wearingVest = wearingVest | IsPlayerWearingItem(mainPlayer,"finevest");
            wearingVest = wearingVest | IsPlayerWearingItem(mainPlayer,"veryfinevest");
            if (wearingVest) {
                if (IsPlayerWearingItem(mainPlayer,"vest")) {
                    switch (bbRand(8)) {
                        case 1,2,3,4,5: {
                            mainPlayer->blurTimer = 500;
                            mainPlayer->stamina = 0;
                            shotMessageUpdate = "A bullet penetrated your vest, making you gasp.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.1,0.5);
                        }
                        case 6: {
                            mainPlayer->blurTimer = 500;
                            shotMessageUpdate = "A bullet hit your left leg.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                        }
                        case 7: {
                            mainPlayer->blurTimer = 500;
                            shotMessageUpdate = "A bullet hit your right leg.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                        }
                        case 8: {
                            mainPlayer->blurTimer = 500;
                            mainPlayer->stamina = 0;
                            shotMessageUpdate = "A bullet struck your neck, making you gasp.";
                            mainPlayer->injuries = mainPlayer->injuries + bbRnd(1.2,1.6);
                        }
                    }
                } else {
                    if (bbRand(10)==1) {
                        mainPlayer->blurTimer = 500;
                        mainPlayer->stamina = mainPlayer->stamina - 1;
                        shotMessageUpdate = "A bullet hit your chest. The vest absorbed some of the damage.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.1);
                    } else {
                        shotMessageUpdate = "A bullet hit your chest. The vest absorbed most of the damage.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.1,0.5);
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
                    }
                    case 2: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your left leg.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                    }
                    case 3: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right leg.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                    }
                    case 4: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                    }
                    case 5: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your left shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(0.8,1.2);
                    }
                    case 6: {
                        mainPlayer->blurTimer = 500;
                        shotMessageUpdate = "A bullet hit your right shoulder.";
                        mainPlayer->injuries = mainPlayer->injuries + bbRnd(2.5,4.0);
                    }
                }
            }

            //Only updates the message if it's been more than two seconds.
            if (MsgTimer < 64*4) {
                Msg = shotMessageUpdate;
                MsgTimer = 70*6;
            }

            mainPlayer->injuries = Min(mainPlayer->injuries, 4.0);

            //Kill(mainPlayer)
            PlaySound_SM(sndManager->bulletHit);
        } else if ((particles)) {
            pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),(bbEntityY(mainPlayer->collider)+bbEntityY(mainPlayer->cam))/2,bbEntityZ(mainPlayer->collider));
            bbPointEntity(pvt, p->obj);
            bbTurnEntity(pvt, 0, 180, 0);

            bbEntityPick(pvt, 2.5);

            if (bbPickedEntity() != 0) {
                PlayRangedSound_SM(sndManager->bulletMiss, mainPlayer->cam, pvt, 0.4, bbRnd(0.8,1.0));

                if (particles) {
                    //dust/smoke particles
                    p = CreateParticle(bbPickedX(),bbPickedY(),bbPickedZ(), PARTICLE_SMOKE_BLACK, 0.03, 0, 80);
                    p->speed = 0.001;
                    p->sizeChange = 0.003;
                    p->a = 0.8;
                    p->aChange = -0.01;
                    bbRotateEntity(p->pvt, bbEntityPitch(pvt)-180, bbEntityYaw(pvt),0);

                    for (i = 0; i <= bbRand(2,3); i++) {
                        p = CreateParticle(bbPickedX(),bbPickedY(),bbPickedZ(), PARTICLE_SMOKE_BLACK, 0.006, 0.003, 80);
                        p->speed = 0.02;
                        p->a = 0.8;
                        p->aChange = -0.01;
                        bbRotateEntity(p->pvt, bbEntityPitch(pvt)+bbRnd(170,190), bbEntityYaw(pvt)+bbRnd(-10,10),0);
                    }

                    //bullet hole decal
                    de = CreateDecal(bbRand(DECAL_BULLET_HOLE1, DECAL_BULLET_HOLE2), bbPickedX(),bbPickedY(),bbPickedZ(), 0,0,0);
                    bbAlignToVector(de->obj,-bbPickedNX(),-bbPickedNY(),-bbPickedNZ(),3);
                    bbMoveEntity(de->obj, 0,0,-0.001);
                    bbEntityFX(de->obj, 1);
                    de->lifetime = 70*20;
                    bbEntityBlend(de->obj, 2);
                    de->size = bbRnd(0.028,0.034);
                    bbScaleSprite(de->obj, de->size, de->size);
                }
            }
            bbFreeEntity(pvt);

        }

    }

}

void PlayMTFSound(int sound, NPC* n) {
    if (n != nullptr) {
        n->soundChannels[0] = PlayRangedSound(sound, mainPlayer->cam, n->collider, 8.0);
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

void MoveToPocketDimension() {
    Room* r;

    for (int iterator114 = 0; iterator114 < Room::getListSize(); iterator114++) {
        r = Room::getObject(iterator114);

        if (r->roomTemplate->name == "pocketdimension") {
            mainPlayer->fallTimer = 0;
            UpdateDoors();
            UpdateRooms();
            bbShowEntity(mainPlayer->collider);
            PlaySound2(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"));
            //PlaySound2(OldManSFX(5)) ;TODO: fix
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj),0.8,bbEntityZ(r->obj));
            mainPlayer->dropSpeed = 0;
            bbResetEntity(mainPlayer->collider);

            mainPlayer->blinkTimer = -10;

            mainPlayer->injuries = mainPlayer->injuries+0.5;

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

        if (n2 != n & n2->id == newID) {
            n2->id = FindFreeNPCID();
        }
    }
}

void Console_SpawnNPC(String c_input, int state = -9999) {
    NPC* n;

    switch (c_input) {
        case "mtf": {
            n = CreateNPC(NPCtypeMTF, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "173","scp173","scp-173": {
            n = CreateNPC(NPCtype173, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "106","scp106","scp-106","larry": {
            n = CreateNPC(NPCtype106, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "guard": {
            n = CreateNPC(NPCtypeGuard, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "096","scp096","scp-096": {
            n = CreateNPC(NPCtype096, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
            if (Curr096 == nullptr) {
                Curr096 = n;
            }
        }
        case "049","scp049","scp-049": {
            n = CreateNPC(NPCtype049, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
            if (state==-9999) {
                n->state = 2;
            }
        }
        case "zombie","scp-049-2": {
            n = CreateNPC(NPCtypeZombie, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
            if (state==-9999) {
                n->state = 1;
            }
        }
        case "966", "scp966", "scp-966": {
            n = CreateNPC(NPCtype966, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "class-d","classd","d": {
            n = CreateNPC(NPCtypeD, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "apache": {
            n = CreateNPC(NPCtypeApache, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "513-1","scp513-1","scp-513-1": {
            n = CreateNPC(NPCtype5131, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "tentacle": {
            n = CreateNPC(NPCtypeTentacle, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider));
        }
        case "860-2","scp860-2","scp-860-2": {
            n = CreateNPC(NPCtype860, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "939","scp939","scp-939": {
            n = CreateNPC(NPCtype939, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
            if (state==-9999) {
                n->state = 1;
            }
        }
        case "066","scp066","scp-066": {
            n = CreateNPC(NPCtype066, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "pdplane": {
            n = CreateNPC(NPCtypePdPlane, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        case "scp-1499-1","scp1499-1","1499-1": {
            n = CreateNPC(NPCtype1499, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
        }
        default: {
            CreateConsoleMsg("NPC type not found.");
        }
    }

    if (n != nullptr) {
        if (state!=-9999) {
            n->state = state;
        }
    }

}

void ManipulateNPCBones() {
    NPC* n;
    int bone;
    int bone2;
    int pvt;
    float pitch;
    float yaw;
    float roll;
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
                bbRuntimeError("ERROR: NPC bone \""+bonename+"\" does not exist.");
            }
            if (n->boneToManipulate2!="") {
                bonename2 = GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"navbone",0);
                bone2 = bbFindChild(n->obj,n->boneToManipulate2);
                if (bone2 == 0) {
                    bbRuntimeError("ERROR: NPC bone \""+bonename2+"\" does not exist.");
                }
            }
            bbPositionEntity(pvt,bbEntityX(bone,true),bbEntityY(bone,true),bbEntityZ(bone,true));
            switch (n->manipulationType) {
                case 0: {
                    bbPointEntity(bone,mainPlayer->cam);
                    bbPointEntity(pvt,mainPlayer->cam);
                    n->bonePitch = CurveAngle(bbEntityPitch(pvt),n->bonePitch,10.0);
                    switch (TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            n->boneYaw = CurveAngle(bbEntityPitch(bone),n->boneYaw,10.0);
                            pitchvalue = n->boneYaw;
                        }
                        case 1: {
                            n->boneYaw = CurveAngle(bbEntityYaw(bone),n->boneYaw,10.0);
                            yawvalue = n->boneYaw;
                        }
                        case 2: {
                            n->boneYaw = CurveAngle(bbEntityRoll(bone),n->boneYaw,10.0);
                            rollvalue = n->boneYaw;
                        }
                    }
                    switch (TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"pitch")) {
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
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3))==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3))==true) {
                        yawvalue = -yawvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3))==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<--- looking at player #2
                }
                case 1: {
                    n->bonePitch = CurveAngle(bbDeltaPitch(bone2,mainPlayer->cam),n->bonePitch,10.0);
                    switch (TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"pitch")) {
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
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3))==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3))==true) {
                        yawvalue = -yawvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3))==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<--- looking away from SCP-096
                }
                case 2: {
                    bbPointEntity(bone,Curr096->obj);
                    switch (TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            n->boneYaw = CurveAngle(bbEntityPitch(bone),n->boneYaw,10.0);
                            pitchvalue = -n->boneYaw;
                        }
                        case 1: {
                            n->boneYaw = CurveAngle(bbEntityYaw(bone),n->boneYaw,10.0);
                            yawvalue = -n->boneYaw;
                        }
                        case 2: {
                            n->boneYaw = CurveAngle(bbEntityRoll(bone),n->boneYaw,10.0);
                            rollvalue = -n->boneYaw;
                        }
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3))==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3))==true) {
                        yawvalue = -yawvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3))==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                    //<-- looking and pitching towards the player
                }
                case 3: {
                    bbPointEntity(pvt,mainPlayer->cam);
                    n->boneYaw = CurveAngle(bbEntityPitch(pvt),n->boneYaw,10.0);
                    switch (TransformNPCManipulationData(n->npcNameInSection,n->boneToManipulate,"yaw")) {
                        case 0: {
                            pitchvalue = n->boneYaw;
                        }
                        case 1: {
                            yawvalue = n->boneYaw;
                        }
                        case 2: {
                            rollvalue = n->boneYaw;
                        }
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"pitchinverse",3))==true) {
                        pitchvalue = -pitchvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"yawinverse",3))==true) {
                        yawvalue = -yawvalue;
                    }
                    if ((int)(GetNPCManipulationValue(n->npcNameInSection,n->boneToManipulate,"rollinverse",3))==true) {
                        rollvalue = -rollvalue;
                    }
                    bbRotateEntity(bone,pitchvalue+pitchoffset,yawvalue+yawoffset,rollvalue+rolloffset);
                }
            }
            bbFreeEntity(pvt);
        }
    }

}

String GetNPCManipulationValue(String NPC, String bone, String section, int valuetype = 0) {
    //valuetype determines what type of variable should the Output be returned
    //0 - String
    //1 - Int
    //2 - Float
    //3 - Boolean

    String value = GetINIString("Data/NPCBones.ini",NPC,bone+"_"+section);
    switch (valuetype) {
        case 0,1,2: {
            return value;
        }
        case 3: {
            if (value == "true" | value == "1") {
                return "1";
            } else {
                return "0";
            }
        }
    }

}

float TransformNPCManipulationData(String NPC, String bone, String section) {
    //If "section$" = "pitch","yaw" or "roll":
    //	- 0 means "realpitch" value has detected
    //	- 1 means "realyaw" value has detected
    //	- 2 means "realroll" value has detected
    //If "section$" = "pitchoffset","yawoffset","rolloffset":
    //	- simply return the offset degree value using a "return Float"

    String value = GetNPCManipulationValue(NPC,bone,section);
    switch (section) {
        case "pitch","yaw","roll": {
            switch (value) {
                case "realpitch": {
                    return 0;
                }
                case "realyaw": {
                    return 1;
                }
                case "realroll": {
                    return 2;
                }
            }
        }
        case "pitchoffset","yawoffset","rolloffset": {
            return (float)(value);
        }
    }

}

void NPCSpeedChange(NPC* n) {

    switch (n->npcType) {
        case NPCtype173,NPCtype106,NPCtype096,NPCtype049,NPCtype939,NPCtypeMTF: {
            switch (SelectedDifficulty->otherFactors) {
                case NORMAL: {
                    n->speed = n->speed * 1.1;
                }
                case HARD: {
                    n->speed = n->speed * 1.2;
                }
            }
        }
    }

}

int PlayerInReachableRoom() {
    String RN = mainPlayer->currRoom->roomTemplate->name;
    Event* e;
    int temp;

    //Player is in these rooms, returning false
    if (RN == "pocketdimension" | RN == "gatea" | RN == "dimension1499" | RN == "173") {
        return false;
    }
    //Player is at GateB and is at the surface, returning false
    if (RN == "exit1" & bbEntityY(mainPlayer->collider)>1040.0*RoomScale) {
        return false;
    }
    //Player is in 860's test room and inside the forest, returning false
    temp = false;
    for (int iterator118 = 0; iterator118 < Event::getListSize(); iterator118++) {
        e = Event::getObject(iterator118);

        if (e->name == "room860" & e->eventState == 1.0) {
            temp = true;
            break;
        }
    }
    if (RN == "room860" & temp) {
        return false;
    }
    //Return true, this means player is in reachable room
    return true;

}

int CheckForNPCInFacility(NPC* n) {
    //False (=0): NPC is not in facility (mostly meant for "dimension1499")
    //True (=1): NPC is in facility
    //2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)

    if (bbEntityY(n->collider)>100.0) {
        return false;
    }
    if (bbEntityY(n->collider)< -10.0) {
        return 2;
    }
    if (bbEntityY(n->collider)> 7.0 & bbEntityY(n->collider)<=100.0) {
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
            if (Abs(bbEntityY(eo->obj,true)-bbEntityY(n->collider))<10.0) {
                for (int iterator120 = 0; iterator120 < ElevatorObj::getListSize(); iterator120++) {
                    eo2 = ElevatorObj::getObject(iterator120);

                    if (eo2 != eo) {
                        if (eo2->inFacility == n->inFacility) {
                            if (Abs(bbEntityY(eo2->obj,true)-bbEntityY(n->collider))<10.0) {
                                if (bbEntityDistance(eo2->obj,n->collider)<bbEntityDistance(eo->obj,n->collider)) {
                                    n->pathStatus = FindPath(n, bbEntityX(eo2->obj,true),bbEntityY(eo2->obj,true),bbEntityZ(eo2->obj,true));
                                    n->currElevator = eo2;
                                    bbDebugLog("eo2 found for "+String(n->npcType));
                                    break;
                                }
                            }
                        }
                    }
                }
                if (n->currElevator == nullptr) {
                    n->pathStatus = FindPath(n, bbEntityX(eo->obj,true),bbEntityY(eo->obj,true),bbEntityZ(eo->obj,true));
                    n->currElevator = eo;
                    bbDebugLog("eo found for "+String(n->npcType));
                }
                if (n->pathStatus != 1) {
                    n->currElevator = nullptr;
                    bbDebugLog("Unable to find elevator path: Resetting CurrElevator");
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
        bbRotateEntity(n->collider,0,CurveAngle(bbEntityYaw(n->obj),bbEntityYaw(n->collider),20.0),0);

        inside = false;
        if (Abs(bbEntityX(n->collider)-bbEntityX(n->currElevator->obj,true))<280.0*RoomScale) {
            if (Abs(bbEntityZ(n->collider)-bbEntityZ(n->currElevator->obj,true))<280.0*RoomScale) {
                if (Abs(bbEntityY(n->collider)-bbEntityY(n->currElevator->obj,true))<280.0*RoomScale) {
                    inside = true;
                }
            }
        }

        dist = bbEntityDistance(n->collider,n->currElevator->door->frameobj);
        if (n->currElevator->door->open) {
            if ((dist > 0.4 & dist < 0.7) & inside) {
                UseDoor(n->currElevator->door,false);
                bbDebugLog(String(n->npcType)+" used elevator");
            }
        } else {
            if (dist < 0.7) {
                n->currSpeed = 0.0;
                if (n->currElevator->door->npcCalledElevator==false) {
                    n->currElevator->door->npcCalledElevator = true;
                    bbDebugLog(String(n->npcType)+" called elevator");
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
    bbEntityPick(n->collider, 1.0);
    if (bbPickedEntity() != 0) {
        turnToSide = 0;
        bbTurnEntity(n->collider,0,90,0);
        bbEntityPick(n->collider,1.0);
        if (bbPickedEntity()==0) {
            turnToSide = 1;
        }
        bbTurnEntity(n->collider,0,270,0);
        if (turnToSide == 1) {
            bbTurnEntity(n->collider,0.0,45,0.0,true);
        } else {
            bbTurnEntity(n->collider,0.0,-45,0.0,true);
        }
    }
    bbShowEntity(n->collider);

}

void AnimateNPC(NPC* n, float start, float quit, float speed, int loop = true) {
    float newTime;
    float temp;

    if (speed > 0.0) {
        newTime = Max(Min(n->frame + speed * timing->tickDuration, quit), start);

        if (loop & newTime >= quit) {
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
    if (Abs(n->frame-frame)<0.001) {
        return;
    }

    bbSetAnimTime(n->obj, frame);

    n->frame = frame;
}

}

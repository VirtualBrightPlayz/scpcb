#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Objects.h"
#include "Room_cont_049_2.h"

namespace CBN {

// Functions.
void FillRoom_cont_049_2(Room* r) {
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 640.f * RoomScale, 240.f * RoomScale, r->z + 656.f * RoomScale, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x - 2032.f * RoomScale, -3280.f * RoomScale, r->z - 656.f * RoomScale, true);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x - 640.f * RoomScale, 240.f * RoomScale, r->z - 656.f * RoomScale, true);

    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x + 2040.f * RoomScale, -3280.f * RoomScale, r->z + 656.f * RoomScale, true);

    //storage room (the spawn point of scp-049)
    //r\objects[5] = CreatePivot(r\obj)
    //PositionEntity(r\objects[5], r\x + 584.f * RoomScale, -3440.f * RoomScale, r\z + 104.f * RoomScale, True)

    //zombie 1 and 049
    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x + 528.f * RoomScale, -3440.f * RoomScale, r->z + 96.f * RoomScale, true);
    //zombie 2
    r->objects[5] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[5], r->x  + 64.f * RoomScale, -3440.f * RoomScale, r->z - 1000.f * RoomScale, true);

    for (int n = 0; n < 2; n++) {
        r->levers[n] = CreateLever();

        bbScaleEntity(r->levers[n]->obj, 0.03f, 0.03f, 0.03f);
        bbScaleEntity(r->levers[n]->baseObj, 0.03f, 0.03f, 0.03f);

        switch (n) {
            case 0: {
                bbPositionEntity(r->levers[n]->obj, r->x - 328.f * RoomScale, r->y - 3374.f * RoomScale, r->z + 916 * RoomScale, true);
                bbPositionEntity(r->levers[n]->baseObj, r->x - 328.f * RoomScale, r->y - 3374.f * RoomScale, r->z + 916 * RoomScale, true);

                //generator
            } break;
            case 1: {
                bbPositionEntity(r->levers[n]->obj, r->x - 370.f * RoomScale, r->y - 3400.f * RoomScale, r->z - 799 * RoomScale, true);
                bbPositionEntity(r->levers[n]->baseObj, r->x - 370.f * RoomScale, r->y - 3400.f * RoomScale, r->z - 799 * RoomScale, true);
            } break;
        }

        bbEntityParent(r->levers[n]->obj, r->obj);
        bbEntityParent(r->levers[n]->baseObj, r->obj);

        bbRotateEntity(r->objects[n*2+6], 0, -180*n, 0);
        bbRotateEntity(r->levers[n]->obj, 81-92*n, -180*(!n), 0);

        bbEntityPickMode(r->levers[n]->obj, 1, false);
        bbEntityRadius(r->levers[n]->obj, 0.1f);
    }


    r->doors[0] = CreateDoor(r->x + 328.f * RoomScale, 0.f, r->z + 656.f * RoomScale, 90, r, true);
    r->doors[0]->autoClose = false;
    r->doors[0]->open = true;
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 288.f * RoomScale, 0.7f, r->z + 512.f * RoomScale, true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 368.f * RoomScale, 0.7f, r->z + 840.f * RoomScale, true);

    r->doors[1] = CreateDoor(r->x - 2328.f * RoomScale, -3520.f * RoomScale, r->z - 656.f * RoomScale, 90, r, false);
    r->doors[1]->autoClose = false;
    r->doors[1]->open = false;
    bbPositionEntity(r->doors[1]->buttons[1], r->x - 2432.f * RoomScale, bbEntityY(r->doors[1]->buttons[1],true), r->z - 816.f * RoomScale, true);
    bbPositionEntity(r->doors[1]->buttons[0], r->x - 2304.f * RoomScale, bbEntityY(r->doors[1]->buttons[0],true), r->z - 472.f * RoomScale, true);

    r->doors[2] = CreateDoor(r->x - 328.f * RoomScale, 0.f, r->z - 656.f * RoomScale, 90, r, true);
    r->doors[2]->autoClose = false;
    r->doors[2]->open = true;
    bbPositionEntity(r->doors[2]->buttons[0], r->x - 288.f * RoomScale, 0.7f, r->z - 512.f * RoomScale, true);
    bbPositionEntity(r->doors[2]->buttons[1], r->x - 368.f * RoomScale, 0.7f, r->z - 840.f * RoomScale, true);

    r->doors[3] = CreateDoor(r->x + 2360.f * RoomScale, -3520.f * RoomScale, r->z + 656.f * RoomScale, 90, r, false);
    r->doors[3]->autoClose = false;
    r->doors[3]->open = false;
    bbPositionEntity(r->doors[3]->buttons[0], r->x + 2432.f * RoomScale, bbEntityY(r->doors[3]->buttons[0],true), r->z + 816.f * RoomScale, true);
    bbPositionEntity(r->doors[3]->buttons[1], r->x + 2312.f * RoomScale, bbEntityY(r->doors[3]->buttons[1],true), r->z + 472.f * RoomScale, true);

    for (int i = 0; i < 4; i++) {
        if ((i % 2) == 1) {
            AssignElevatorObj((MeshModel*)r->objects[i],r->doors[i],2);
        } else {
            AssignElevatorObj((MeshModel*)r->objects[i],r->doors[i],true);
        }
    }

    //storage room door
    r->doors[4] = CreateDoor(r->x + 272.f * RoomScale, -3552.f * RoomScale, r->z + 104.f * RoomScale, 90, r, false);
    r->doors[4]->autoClose = false;
    r->doors[4]->open = false;
    r->doors[4]->locked = true;

    Door* d = CreateDoor(r->x,0,r->z, 0, r, false, DOOR_TYPE_HCZ, r->roomTemplate->name);

    Item* it = CreatePaper("doc049", r->x - 608.f * RoomScale, r->y - 3332.f * RoomScale, r->z + 876.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("firstaid", r->x +385.f * RoomScale, r->y - 3412.f * RoomScale, r->z + 271.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    //r\objects[10] = LoadMesh("GFX/Map/room049_hb.b3d",r\obj)
    //EntityPickMode(r\objects[10],2)
    //EntityType(r\objects[10],HIT_MAP)
    //EntityAlpha(r\objects[10],0.f)
}

void UpdateEvent_cont_049_2(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (bbEntityY(mainPlayer->collider) > -2848*RoomScale) {
            e->eventState2 = UpdateElevators(e->eventState2, e->room->doors[0], e->room->doors[1],e->room->objects[0],e->room->objects[1], e);
            e->eventState3 = UpdateElevators(e->eventState3, e->room->doors[2], e->room->doors[3],e->room->objects[2],e->room->objects[3], e);
            e->overwriteMusic = false;
        } else {
            e->overwriteMusic = true;

            if (e->eventState == 0) {
                if (!e->loaded) {
                    NPC* n = CreateNPC(NPCtypeZombie, bbEntityX(e->room->objects[4],true),bbEntityY(e->room->objects[4],true),bbEntityZ(e->room->objects[4],true));
                    bbPointEntity(n->collider, e->room->obj);
                    bbTurnEntity(n->collider, 0, 190, 0);

                    n = CreateNPC(NPCtypeZombie, bbEntityX(e->room->objects[5],true),bbEntityY(e->room->objects[5],true),bbEntityZ(e->room->objects[5],true));
                    bbPointEntity(n->collider, e->room->obj);
                    bbTurnEntity(n->collider, 0, 20, 0);

                    for (int iterator155 = 0; iterator155 < NPC::getListSize(); iterator155++) {
                        n = NPC::getObject(iterator155);

                        if (n->npcType == NPCtype049) {
                            e->room->npc[0] = n;
                            e->room->npc[0]->state = 2;
                            e->room->npc[0]->idle = 1;
                            bbPositionEntity(e->room->npc[0]->collider,bbEntityX(e->room->objects[4],true),bbEntityY(e->room->objects[4],true)+3,bbEntityZ(e->room->objects[4],true));
                            bbResetEntity(e->room->npc[0]->collider);
                            break;
                        }
                    }
                    if (e->room->npc[0]==nullptr) {
                        n = CreateNPC(NPCtype049, bbEntityX(e->room->objects[4],true), bbEntityY(e->room->objects[4],true)+3, bbEntityZ(e->room->objects[4],true));
                        bbPointEntity(n->collider, e->room->obj);
                        n->state = 2;
                        n->idle = 1;
                        e->room->npc[0] = n;
                    }

                    e->musicTrack = MUS_049;
                    e->loaded = true;
                    e->eventState = 1;
                }
            } else if ((e->eventState > 0)) {

                //power feed
                bool powerOff = !e->room->levers[0]->succ;
                //generator
                bool genOn = e->room->levers[1]->succ;

                e->room->doors[1]->locked = true;
                e->room->doors[3]->locked = true;

                //TODO: really
                if (powerOff || genOn) {
                    //049 appears when either of the levers is turned
                    e->eventState = Max(e->eventState,70*180);

                    if (powerOff && genOn) {
                        e->room->doors[1]->locked = false;
                        e->room->doors[3]->locked = false;
                        e->eventState2 = UpdateElevators(e->eventState2, e->room->doors[0], e->room->doors[1],e->room->objects[0],e->room->objects[1], e);
                        e->eventState3 = UpdateElevators(e->eventState3, e->room->doors[2], e->room->doors[3],e->room->objects[2],e->room->objects[3], e);

                        if (e->sounds[1]==0) {
                            LoadEventSound(e,"SFX/General/GeneratorOn.ogg",1);
                        }
                        e->soundChannels[1] = LoopRangedSound(e->sounds[1], e->soundChannels[1], mainPlayer->cam, e->room->levers[1]->baseObj, 6.f, e->eventState3);

                        if (e->room->npc[0]->idle > 0) {
                            int i = 0;
                            if (bbEntityDistance(mainPlayer->collider,e->room->doors[1]->frameobj)<3.f) {
                                i = 1;
                            } else if ((bbEntityDistance(mainPlayer->collider,e->room->doors[3]->frameobj)<3.f)) {
                                i = 3;
                            }
                            if (i > 0) {
                                //If EntityVisible(mainPlayer\collider,e\room\doors[i]\frameobj)
                                bbPositionEntity(e->room->npc[0]->collider,bbEntityX(e->room->objects[i],true),bbEntityY(e->room->objects[i],true),bbEntityZ(e->room->objects[i],true));
                                bbResetEntity(e->room->npc[0]->collider);
                                PlayRangedSound_SM(sndMgmt->elevatorBeep, mainPlayer->cam, e->room->objects[i], 4.f);
                                UseDoor(e->room->doors[i],false);
                                e->room->doors[i-1]->open = false;
                                e->room->doors[i]->open = true;
                                e->room->npc[0]->pathStatus = FindPath(e->room->npc[0],bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider));
                                PlayRangedSound(LoadTempSound("SFX/SCP/049/Greeting"+String(bbRand(1,2))+".ogg"),mainPlayer->cam, e->room->npc[0]->collider);
                                e->room->npc[0]->idle = 0;
                                //EndIf
                            }
                        }
                    }
                }

                if (e->eventState < 70*190) {
                    e->eventState = Min(e->eventState+timing->tickDuration,70*190);
                    //049 spawns after 3 minutes
                    if (e->eventState > 70*180) {

                        //If (e\room\npc[0]=Null) Then
                        //	For n.NPC = Each NPC
                        //		If (n\npcType=NPCtype049) Then e\room\npc[0]=n
                        //break;
                        //	Next
                        //EndIf
                        //e\room\npc[0]\state = 1

                        e->room->doors[4]->open = true;
                        PlaySound_SM(sndMgmt->teslaPowerUp);
                        PlayRangedSound_SM(sndMgmt->openDoor[bbRand(0,2)], mainPlayer->cam, e->room->doors[4]->obj, 6.f);

                        e->room->doors[1]->open = false;
                        e->room->doors[3]->open = false;
                        e->room->doors[0]->open = true;
                        e->room->doors[2]->open = true;

                        e->eventState = 70*190;
                    }
                } else if ((e->eventState < 70*240)) {

                    //If (e\room\npc[0]=Null) Then
                    //	For n.NPC = Each NPC
                    //		If (n\npcType=NPCtype049) Then e\room\npc[0]=n
                    //break;
                    //	Next
                    //Else
                    //If (EntityDistance(e\room\npc[0]\collider,mainPlayer\collider)<4.f) Then
                    //	e\eventState=e\eventState+timing\tickDuration
                    //	If (e\eventState > 70*195 And e\eventState-timing\tickDuration =< 70*195) Then
                    //		For n.NPC = Each NPC ;awake the zombies
                    //			If (n\npcType = NPCtypeZombie And n\state = 0) Then
                    //				n\state = 1
                    //				SetNPCFrame(n, 155)
                    //			EndIf
                    //		Next
                    //		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Greeting"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
                    //	ElseIf e\eventState > 70*214 And e\eventState-timing\tickDuration =< 70*214
                    //		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Spotted"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
                    //	ElseIf e\eventState > 70*227 And e\eventState-timing\tickDuration =< 70*227
                    //		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Detected"+Rand(1,3)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
                    //		e\eventState=70*241
                    //	EndIf
                    //EndIf
                    //EndIf

                    //awake the zombies
                    for (int iterator156 = 0; iterator156 < NPC::getListSize(); iterator156++) {
                        NPC* n = NPC::getObject(iterator156);

                        if (n->npcType == NPCtypeZombie && n->state == 0) {
                            n->state = 1;
                            SetNPCFrame(n, 155);
                        }
                    }
                    e->eventState = 70*241;
                }
            }
        }
    } else {
        e->eventState2 = UpdateElevators(e->eventState2, e->room->doors[0], e->room->doors[1],e->room->objects[0],e->room->objects[1], e);
        e->eventState3 = UpdateElevators(e->eventState3, e->room->doors[2], e->room->doors[3],e->room->objects[2],e->room->objects[3], e);
        e->overwriteMusic = false;
    }

    if (e->eventState < 0) {
        if (e->eventState > -70*4) {
            if (mainPlayer->fallTimer >= 0) {
                mainPlayer->fallTimer = Min(-1, mainPlayer->fallTimer);
                bbPositionEntity(mainPlayer->head, bbEntityX(mainPlayer->cam, true), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true), true);
                bbResetEntity(mainPlayer->head);
                bbRotateEntity(mainPlayer->head, 0, bbEntityYaw(mainPlayer->cam) + bbRand(-45, 45), 0);
            } else if ((mainPlayer->fallTimer < -230)) {
                mainPlayer->fallTimer = -231;
                mainPlayer->blinkTimer = 0;
                e->eventState = e->eventState-timing->tickDuration;

                if (e->eventState <= -70*4) {
                    UpdateDoorsTimer = 0;
                    UpdateDoors();
                    UpdateRooms();
                    bbShowEntity(mainPlayer->collider);
                    mainPlayer->dropSpeed = 0;
                    mainPlayer->blinkTimer = -10;
                    mainPlayer->fallTimer = 0;
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj,true), bbEntityY(e->room->objects[5],true)+0.2f, bbEntityZ(e->room->obj,true));
                    bbResetEntity(mainPlayer->collider);

                    bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[0],true),bbEntityY(e->room->objects[0],true),bbEntityZ(e->room->objects[0],true),true);
                    bbResetEntity(e->room->npc[0]->collider);

                    for (int iterator157 = 0; iterator157 < NPC::getListSize(); iterator157++) {
                        NPC* n = NPC::getObject(iterator157);

                        if (n->npcType == NPCtypeZombie) {
                            bbPositionEntity(n->collider, bbEntityX(e->room->objects[4],true),bbEntityY(e->room->objects[4],true),bbEntityZ(e->room->objects[4],true),true);
                            bbResetEntity(n->collider);
                            n->state = 4;
                            std::cout << "moving zombie";
                        }
                    }

                    NPC* n = CreateNPC(NPCtypeMTF, bbEntityX(e->room->objects[5],true), bbEntityY(e->room->objects[5],true)+0.2f, bbEntityZ(e->room->objects[5],true));
                    n->state = 6;
                    n->reload = 6*70;
                    bbPointEntity(n->collider,mainPlayer->collider);
                    e->room->npc[1] = n;

                    n = CreateNPC(NPCtypeMTF, bbEntityX(e->room->objects[5],true), bbEntityY(e->room->objects[5],true)+0.2f, bbEntityZ(e->room->objects[5],true));
                    n->state = 6;
                    n->reload = (6*70)+bbRnd(15,30);
                    bbRotateEntity(n->collider,0,bbEntityYaw(e->room->npc[1]->collider),0);
                    bbMoveEntity(n->collider,0.5f,0,0);
                    bbPointEntity(n->collider,mainPlayer->collider);

                    n = CreateNPC(NPCtypeMTF, bbEntityX(e->room->objects[5],true), bbEntityY(e->room->objects[5],true)+0.2f, bbEntityZ(e->room->objects[5],true));
                    n->state = 6;
                    //n\reload = 70*4.75f
                    n->reload = 10000;
                    bbRotateEntity(n->collider,0,bbEntityYaw(e->room->npc[1]->collider),0);
                    n->state2 = bbEntityYaw(n->collider);
                    bbMoveEntity(n->collider,-0.65f,0,0);
                    e->room->npc[2] = n;

                    bbMoveEntity(e->room->npc[1]->collider,0,0,0.1f);
                    bbPointEntity(mainPlayer->collider, e->room->npc[1]->collider);

                    PlaySound2(LoadTempSound("SFX/Character/MTF/049/Player0492_1.ogg"));

                    LoadEventSound(e,"SFX/SCP/049/0492Breath.ogg");

                    IsZombie = true;
                }
            }
        } else {
            mainPlayer->blurTimer = 800;
            mainPlayer->forceMove = 0.5f;
            mainPlayer->injuries = Max(2.f,mainPlayer->injuries);
            mainPlayer->bloodloss = 0;

            //Msg = ""

            if (e->room->npc[2]->state == 7) {
                if (e->room->npc[2]->state3 < 70*1.75f) {
                    e->room->npc[2]->state3 = e->room->npc[2]->state3 + timing->tickDuration;
                } else {
                    e->room->npc[2]->state = 6;
                    e->room->npc[2]->reload = e->room->npc[1]->reload+bbRnd(5,10);
                }
            } else if ((e->room->npc[2]->state == 6 && e->room->npc[2]->reload > 70*4)) {
                if (e->room->npc[2]->state3 > -(70*4)) {
                    e->room->npc[2]->state3 = e->room->npc[2]->state3 - timing->tickDuration;
                } else {
                    e->room->npc[2]->state3 = 0.f;
                    e->room->npc[2]->reload = 45;
                    e->room->npc[2]->state = 7;
                }
            }

            Pivot* pvt = bbCreatePivot();
            bbPositionEntity(pvt,bbEntityX(e->room->npc[1]->collider),bbEntityY(e->room->npc[1]->collider)+0.2f,bbEntityZ(e->room->npc[1]->collider));

            bbPointEntity(mainPlayer->collider, e->room->npc[1]->collider);
            bbPointEntity(mainPlayer->cam, pvt);

            bbFreeEntity(pvt);

            if (mainPlayer->dead == true) {
                if (bbChannelPlaying(e->room->npc[1]->soundChannels[0])) {
                    bbStopChannel(e->room->npc[1]->soundChannels[0]);
                }
                PlaySound2(LoadTempSound("SFX/Character/MTF/049/Player0492_2.ogg"));
                RemoveEvent(e);
            } else {
                if (e->soundChannels[0] == 0) {
                    e->soundChannels[0] = bbPlaySound(e->sounds[0]);
                } else {
                    if (!bbChannelPlaying(e->soundChannels[0])) {
                        e->soundChannels[0] = bbPlaySound(e->sounds[0]);
                    }
                }
            }
        }
    }

}

}

#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "Objects.h"
#include "../AssetMgmt/Assets.h"
#include "../Player/Player.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../MathUtils/MathUtils.h"
#include "../AssetMgmt/Audio.h"
#include "Doors.h"
#include "../NPCs/NPCs.h"
#include "Events/Events.h"
#include "MapSystem.h"

namespace CBN {

// Structs.
std::vector<Lever*> Lever::list;
Lever::Lever() {
    list.push_back(this);
}
Lever::~Lever() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Lever::getListSize() {
    return list.size();
}
Lever* Lever::getObject(int index) {
    return list[index];
}

std::vector<ElevatorObj*> ElevatorObj::list;
ElevatorObj::ElevatorObj() {
    list.push_back(this);
}
ElevatorObj::~ElevatorObj() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int ElevatorObj::getListSize() {
    return list.size();
}
ElevatorObj* ElevatorObj::getObject(int index) {
    return list[index];
}

// Functions.
MeshModel* CreateButton(float x, float y, float z, float pitch, float yaw, float roll) {
    MeshAssetWrap* buttonObj = MeshAssetWrap::grab("GFX/Map/Meshes/button.b3d", false);
    MeshModel* obj = bbDeepCopyMesh(buttonObj->getMesh());
    buttonObj->drop();

    bbScaleEntity(obj, 0.03f, 0.03f, 0.03f);

    bbPositionEntity(obj, x,y,z);
    bbRotateEntity(obj, pitch,yaw,roll);

    bbEntityPickMode(obj, 2);

    return obj;
}

void UpdateButton(MeshModel* obj) {
    //entityDistance(collider, d\buttons[i])
    float dist = bbEntityDistanceSquared(mainPlayer->collider, obj);

    if (dist < 0.8f*0.8f) {
        Pivot* tempPvt = bbCreatePivot();
        bbPositionEntity(tempPvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
        bbPointEntity(tempPvt,obj);

        if (bbEntityPick(tempPvt, 0.65f) == obj) {
            if (mainPlayer->closestButton == 0) {
                mainPlayer->closestButton = obj;
            } else {
                if (dist < bbEntityDistanceSquared(mainPlayer->collider, mainPlayer->closestButton)) {
                    mainPlayer->closestButton = obj;
                }
            }
        }

        bbFreeEntity(tempPvt);
    }
}

Lever* CreateLever() {
    MeshAssetWrap* leverObj = MeshAssetWrap::grab("GFX/Map/Meshes/leverhandle.b3d", false);
    MeshAssetWrap* leverBaseObj = MeshAssetWrap::grab("GFX/Map/Meshes/leverbase.b3d", false);

    Lever* lever = new Lever();
    lever->obj = bbCopyMeshModelEntity(leverObj->getMesh());
    lever->baseObj = bbCopyMeshModelEntity(leverBaseObj->getMesh());
    leverObj->drop();
    leverBaseObj->drop();

    return lever;
}

void UpdateLevers() {
    for (int i = 0; i < Lever::getListSize(); i++) {
        Lever* lever = Lever::getObject(i);

        float dist = bbEntityDistanceSquared(mainPlayer->cam, lever->obj);

        if (dist < 64.f) {
            if (dist < 0.8f*0.8f && (!lever->locked)) {
                if (bbEntityInView(lever->obj, mainPlayer->cam)) {

                    bbEntityPick(mainPlayer->cam, 0.65f);

                    if (bbPickedEntity() == lever->obj) {
                        DrawHandIcon = true;
                        if (MouseHit1) {
                            mainPlayer->grabbedEntity = lever->obj;
                        }
                    }

                    float prevpitch = bbEntityPitch(lever->obj);

                    if (MouseDown1 || MouseHit1) {
                        if (mainPlayer->grabbedEntity != 0) {
                            if (mainPlayer->grabbedEntity == lever->obj) {
                                DrawHandIcon = true;
                                //TurnEntity(lever\obj, , 0, 0)
                                bbRotateEntity(mainPlayer->grabbedEntity, Max(Min(bbEntityPitch(lever->obj)+Max(Min(mouse_y_speed_1 * 8,30.f),-30), 80), -80), bbEntityYaw(lever->obj), 0);

                                mainPlayer->drawDirectionalArrow[0] = true;
                                mainPlayer->drawDirectionalArrow[2] = true;

                            }
                        }
                    }

                    if (bbEntityPitch(lever->obj,true) > 75) {
                        if (prevpitch <= 75) {
                            PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, lever->obj, 1.f);
                        }
                    } else if ((bbEntityPitch(lever->obj,true) < -75)) {
                        if (prevpitch >= -75) {
                            PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, lever->obj, 1.f);
                        }
                    }
                }
            }

            if (MouseDown1==false && MouseHit1==false) {
                if (bbEntityPitch(lever->obj,true) > 0) {
                    bbRotateEntity(lever->obj, CurveValue(80, bbEntityPitch(lever->obj), 10), bbEntityYaw(lever->obj), 0);
                } else {
                    bbRotateEntity(lever->obj, CurveValue(-80, bbEntityPitch(lever->obj), 10), bbEntityYaw(lever->obj), 0);
                }
                mainPlayer->grabbedEntity = 0;
            }

        }

        if (bbEntityPitch(lever->obj, true) > 0) {
            lever->succ = true;
        } else {
            lever->succ = false;
        }
    }
}

float UpdateElevators(float state, Door* door1, Door* door2, class Object* room1, class Object* room2, Event* event) {
    door1->isElevatorDoor = 1;
    door2->isElevatorDoor = 1;
    if (door1->open && door2->open) {
        state = -1;
        if ((mainPlayer->closestButton == door2->buttons[0] || mainPlayer->closestButton == door2->buttons[1]) && MouseHit1) {
            UseDoor(door1,false);
        }
        if (door2->npcCalledElevator == 1) {
            UseDoor(door1,false);
            door2->npcCalledElevator = 2;
        }
    } else if ((door2->open == true && door1->open == false)) {
        state = 1;
        if ((mainPlayer->closestButton == door1->buttons[0] || mainPlayer->closestButton == door1->buttons[1]) && MouseHit1) {
            UseDoor(door2,false);
        }
        if (door1->npcCalledElevator == 1) {
            UseDoor(door2,false);
            door1->npcCalledElevator = 2;
        }
    } else if ((abs(door1->openstate-door2->openstate)<0.2f)) {
        door1->isElevatorDoor = 2;
        door2->isElevatorDoor = 2;
    }

    int inside = false;
    NPC* NPC_inside = nullptr;

    //molemmat ovet kiinni = hissi liikkuu
    if (door1->open == false && door2->open == false) {
        door1->locked = true;
        door2->locked = true;
        door1->npcCalledElevator = 2;
        door2->npcCalledElevator = 2;
        //ylh��lt� alas
        if (state < 0) {
            state = state - timing->tickDuration;
            //pelaaja hissin sis�ll�
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room1,true))<280.f*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true))<280.f*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true))<280.f*RoomScale) {
                        inside = true;

                        /*TODO: AHH
                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }*/

                        mainPlayer->camShake = bbSin(abs(state)/3.f)*0.3f;
                    }
                }
            }

            for (int i = 0; i < NPC::getListSize(); i++) {
                NPC* n = NPC::getObject(i);

                if (n->canUseElevator) {
                    if (abs(bbEntityX(n->collider)-bbEntityX(room1,true))<280.f*RoomScale) {
                        if (abs(bbEntityZ(n->collider)-bbEntityZ(room1,true))<280.f*RoomScale) {
                            if (abs(bbEntityY(n->collider)-bbEntityY(room1,true))<280.f*RoomScale) {
                                NPC_inside = n;
                            }
                        }
                    }
                }
            }

            if (NPC_inside != nullptr && (!inside)) {
                NPC_inside->idle = true;
            } else if ((NPC_inside != nullptr && inside)) {
                NPC_inside->idle = false;
            }

            if (state < -400) {
                door1->locked = false;
                door2->locked = false;
                door1->npcCalledElevator = false;
                door2->npcCalledElevator = false;
                state = 0;

                if (inside) {
                    float x = Max(Min((bbEntityX(mainPlayer->collider)-bbEntityX(room1,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    float z = Max(Min((bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    bbPositionEntity(mainPlayer->collider, bbEntityX(room2,true)+x,0.1f+bbEntityY(room2,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);
                    bbResetEntity(mainPlayer->collider);
                    UpdateDoorsTimer = 0;
                    mainPlayer->dropSpeed = 0;
                    UpdateDoors();
                    UpdateRooms();
                }

                if (NPC_inside != nullptr) {
                    float x = Max(Min((bbEntityX(NPC_inside->collider)-bbEntityX(room1,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    float z = Max(Min((bbEntityZ(NPC_inside->collider)-bbEntityZ(room1,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    bbPositionEntity(NPC_inside->collider, bbEntityX(room2,true)+x,0.1f+bbEntityY(room2,true)+(bbEntityY(NPC_inside->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);
                    bbResetEntity(NPC_inside->collider);
                    UpdateDoorsTimer = 0;
                    NPC_inside->dropSpeed = 0;
                    if ((int)(NPC_inside->idle)) {
                        bbTurnEntity(NPC_inside->obj,0,180,0);
                        bbTurnEntity(NPC_inside->collider,0,180,0);
                        NPC_inside->idle = false;
                    }
                    NPC_inside->currElevator = nullptr;
                }

                UseDoor(door2,false);

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room2, 4.f);
            }
            //alhaalta yl�s
        } else {
            state = state + timing->tickDuration;
            //pelaaja hissin sis�ll�
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room2,true))<280.f*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true))<280.f*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true))<280.f*RoomScale) {
                        inside = true;

                        /*TODO: OHH
                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }*/

                        mainPlayer->camShake = bbSin(abs(state)/3.f)*0.3f;
                    }
                }
            }

            for (int i = 0; i < NPC::getListSize(); i++) {
                NPC* n = NPC::getObject(i);

                if (n->canUseElevator) {
                    if (abs(bbEntityX(n->collider)-bbEntityX(room2,true))<280.f*RoomScale) {
                        if (abs(bbEntityZ(n->collider)-bbEntityZ(room2,true))<280.f*RoomScale) {
                            if (abs(bbEntityY(n->collider)-bbEntityY(room2,true))<280.f*RoomScale) {
                                NPC_inside = n;
                            }
                        }
                    }
                }
            }

            if (NPC_inside != nullptr && (!inside)) {
                NPC_inside->idle = true;
            } else if ((NPC_inside != nullptr && inside)) {
                NPC_inside->idle = false;
            }

            if (state > 400) {
                door1->locked = false;
                door2->locked = false;
                door1->npcCalledElevator = false;
                door2->npcCalledElevator = false;
                state = 0;

                //pelaaja hissin sis�ll�, siirret��n
                if (inside) {
                    float x = Max(Min((bbEntityX(mainPlayer->collider)-bbEntityX(room2,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    float z = Max(Min((bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    bbPositionEntity(mainPlayer->collider, bbEntityX(room1,true)+x,0.1f+bbEntityY(room1,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);
                    bbResetEntity(mainPlayer->collider);
                    UpdateDoorsTimer = 0;
                    mainPlayer->dropSpeed = 0;
                    UpdateDoors();
                    UpdateRooms();
                }

                if (NPC_inside != nullptr) {
                    float x = Max(Min((bbEntityX(NPC_inside->collider)-bbEntityX(room2,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    float z = Max(Min((bbEntityZ(NPC_inside->collider)-bbEntityZ(room2,true)),280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    bbPositionEntity(NPC_inside->collider, bbEntityX(room1,true)+x,0.1f+bbEntityY(room1,true)+(bbEntityY(NPC_inside->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);
                    bbResetEntity(NPC_inside->collider);
                    UpdateDoorsTimer = 0;
                    NPC_inside->dropSpeed = 0;
                    if ((int)(NPC_inside->idle)) {
                        bbTurnEntity(NPC_inside->obj,0,180,0);
                        bbTurnEntity(NPC_inside->collider,0,180,0);
                        NPC_inside->idle = false;
                    }
                    NPC_inside->currElevator = nullptr;
                }

                UseDoor(door1,false);

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room1, 4.f);
            }

        }
    }

    return state;

}

float UpdateElevators2(float State, Door* door1, Door* door2, class Object* room1, class Object* room2, Event* event) {
    float x;
    float z;

    door1->isElevatorDoor = 1;
    door2->isElevatorDoor = 1;
    if (door1->open && !door2->open) {
        State = -1;
        door2->isElevatorDoor = 2;
        if ((mainPlayer->closestButton == door2->buttons[0] || mainPlayer->closestButton == door2->buttons[1]) && MouseHit1) {
            UseDoor(door1,false);
        }
    } else if (door2->open && !door1->open) {
        State = 1;
        door1->isElevatorDoor = 2;
        if ((mainPlayer->closestButton == door1->buttons[0] || mainPlayer->closestButton == door1->buttons[1]) && MouseHit1) {
            UseDoor(door2,false);
        }
    } else if (abs(door1->openstate-door2->openstate)<0.2f) {
        door1->isElevatorDoor = 2;
        door2->isElevatorDoor = 2;
    }

    int inside = false;
    float dist;
    float dir;

    //molemmat ovet kiinni = hissi liikkuu
    if (door1->open == false && door2->open == false) {
        door1->locked = true;
        door2->locked = true;
        //ylh��lt?alas
        if (State < 0) {
            State = State - timing->tickDuration;
            //pelaaja hissin sis�ll?
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room1,true))<280.f*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true))<280.f*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true))<280.f*RoomScale) {
                        inside = true;

                        /*TODO: EHH
                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }*/

                        mainPlayer->camShake = bbSin(abs(State)/3.f)*0.3f;
                    }
                }
            }

            if (State < -400) {
                door1->locked = false;
                door2->locked = false;
                State = 0;

                UseDoor(door2,true);

                if (inside) {

                    dist = Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room1,true),bbEntityZ(room1,true));

                    dir = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room1,true),bbEntityZ(room1,true));
                    //EntityYaw(room2,True)+angleDist(dir,EntityYaw(room1,True))
                    dir = dir+bbEntityYaw(room2,true)-bbEntityYaw(room1,true);
                    //dir=dir-90.f

                    //					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room1,True)+EntityYaw(room2,True)

                    dir = WrapAngle(dir);

                    x = Max(Min(bbCos(dir)*dist,280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    z = Max(Min(bbSin(dir)*dist,280*RoomScale-0.17f),-280*RoomScale+0.17f);

                    //x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17f),-280*RoomScale+0.17f)
                    //z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17f),-280*RoomScale+0.17f)

                    //dir
                    bbRotateEntity(mainPlayer->collider,bbEntityPitch(mainPlayer->collider,true),bbEntityYaw(room2,true)+angleDist(bbEntityYaw(mainPlayer->collider,true),bbEntityYaw(room1,true)),bbEntityRoll(mainPlayer->collider,true),true);

                    bbPositionEntity(mainPlayer->collider, bbEntityX(room2,true)+x,0.05f+bbEntityY(room2,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);

                    bbResetEntity(mainPlayer->collider);
                    UpdateDoors();
                    UpdateRooms();
                }

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room2, 4.f);
                //PlaySound(ElevatorBeepSFX)
            }
            //alhaalta yl�s
        } else {
            State = State + timing->tickDuration;
            //pelaaja hissin sis�ll?
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room2,true))<280.f*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true))<280.f*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true))<280.f*RoomScale) {
                        inside = true;

                        /*TODO: UHH
                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }*/

                        mainPlayer->camShake = bbSin(abs(State)/3.f)*0.3f;
                    }
                }
            }

            if (State > 400) {
                door1->locked = false;
                door2->locked = false;
                State = 0;

                UseDoor(door1,true);

                //pelaaja hissin sis�ll? siirret��n
                if (inside) {

                    dist = Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room2,true),bbEntityZ(room2,true));

                    dir = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room2,true),bbEntityZ(room2,true));
                    //EntityYaw(room1,True)+angleDist(dir,EntityYaw(room2,True))
                    dir = dir+bbEntityYaw(room1,true)-bbEntityYaw(room2,true);
                    //dir=dir-90.f

                    //					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room2,True)+EntityYaw(room1,True)

                    //dir=WrapAngle(dir)

                    x = Max(Min(bbCos(dir)*dist,280*RoomScale-0.17f),-280*RoomScale+0.17f);
                    z = Max(Min(bbSin(dir)*dist,280*RoomScale-0.17f),-280*RoomScale+0.17f);

                    //x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17f),-280*RoomScale+0.17f)
                    //z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17f),-280*RoomScale+0.17f)

                    //dir
                    bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider,true),bbEntityYaw(room2,true)+angleDist(bbEntityYaw(mainPlayer->collider,true),bbEntityYaw(room1,true)),bbEntityRoll(mainPlayer->collider,true),true);

                    bbPositionEntity(mainPlayer->collider, bbEntityX(room1,true)+x,0.05f+bbEntityY(room1,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);

                    bbResetEntity(mainPlayer->collider);
                    UpdateDoors();
                    UpdateRooms();
                }

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room1, 4.f);
            }

        }
    }

    return State;

}

ElevatorObj* AssignElevatorObj(MeshModel* obj, Door* door, int in_facility) {
    ElevatorObj* eo = new ElevatorObj();

    eo->obj = obj;
    eo->door = door;
    eo->inFacility = in_facility;

    return eo;
}

}

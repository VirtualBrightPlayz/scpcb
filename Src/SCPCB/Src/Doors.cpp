#include <StringType.h>
#include <bbblitz3d.h>
#include <bbmath.h>

#include "Doors.h"
#include "MapSystem.h"
#include "Assets.h"
#include "GameMain.h"
#include "MathUtils/MathUtils.h"
#include "Items/Items.h"
#include "Particles.h"
#include "Player.h"
#include "Audio.h"
#include "Menus/Menu.h"

namespace CBN {

// Structs.
std::vector<Door*> Door::list;
Door::Door() {
    memset(this,0,sizeof(Door));

    list.push_back(this);
}
Door::~Door() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Door::getListSize() {
    return list.size();
}
Door* Door::getObject(int index) {
    return list[index];
}

// Globals.
float UpdateDoorsTimer;
int DoorTempID;

// Functions.
Door* CreateDoor(float x, float y, float z, float angle, Room* room, int open, int doorType, const String& tag, const String& code) {
    Pivot* parent = nullptr;
    if (room != nullptr) {
        parent = room->obj;
    }

    MeshAssetWrap* doorObj = MeshAssetWrap::grab("GFX/Map/Meshes/door.b3d", false);
    MeshAssetWrap* doorFrameObj = MeshAssetWrap::grab("GFX/Map/Meshes/doorframe.b3d", false);
    MeshAssetWrap* doorColl = MeshAssetWrap::grab("GFX/Map/Meshes/doorcoll.b3d", false);
    MeshAssetWrap* buttonObj = MeshAssetWrap::grab("GFX/Map/Meshes/button.b3d", false);

    MeshAssetWrap* contDoorLeft = MeshAssetWrap::grab("GFX/Map/Meshes/ContDoorLeft.b3d", false);
    MeshAssetWrap* contDoorRight = MeshAssetWrap::grab("GFX/Map/Meshes/ContDoorRight.b3d", false);

    MeshAssetWrap* hczDoorObj[2];
    for (int i = 0; i < 2; i++) {
        hczDoorObj[i] = MeshAssetWrap::grab("GFX/Map/Meshes/heavydoor" + String(i + 1) + ".b3d", false);
    }

    Door* d = new Door();
    d->typ = doorType;
    switch (d->typ) {
        case DOOR_TYPE_CONT: {
            d->obj = bbCopyMeshModelEntity(contDoorRight->getMesh());
            bbScaleEntity(d->obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale);
            d->obj2 = bbCopyMeshModelEntity(contDoorLeft->getMesh());
            bbScaleEntity(d->obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale);

            d->frameobj = bbCopyMeshModelEntity(doorColl->getMesh());
            bbScaleEntity(d->frameobj, RoomScale, RoomScale, RoomScale);
            bbEntityType(d->frameobj, HIT_MAP);
            bbEntityAlpha(d->frameobj, 0.f);
        } break;
        case DOOR_TYPE_HCZ: {
            d->obj = bbCopyMeshModelEntity(hczDoorObj[0]->getMesh());
            bbScaleEntity(d->obj, RoomScale, RoomScale, RoomScale);
            d->obj2 = bbCopyMeshModelEntity(hczDoorObj[1]->getMesh());
            bbScaleEntity(d->obj2, RoomScale, RoomScale, RoomScale);

            d->frameobj = bbCopyMeshModelEntity(doorFrameObj->getMesh());
        } break;
        default: {
            d->obj = bbCopyMeshModelEntity(doorObj->getMesh());
            bbScaleEntity(d->obj, (204.f * RoomScale) / bbMeshWidth(d->obj), 312.f * RoomScale / bbMeshHeight(d->obj), 16.f * RoomScale / bbMeshDepth(d->obj));

            d->frameobj = bbCopyMeshModelEntity(doorFrameObj->getMesh());
            d->obj2 = bbCopyMeshModelEntity(doorObj->getMesh());

            bbScaleEntity(d->obj2, (204.f * RoomScale) / bbMeshWidth(d->obj), 312.f * RoomScale / bbMeshHeight(d->obj), 16.f * RoomScale / bbMeshDepth(d->obj));
            //entityType d\obj2, HIT_MAP
        } break;
    }

    bbPositionEntity(d->frameobj, x, y, z);
    bbScaleEntity(d->frameobj, RoomScale, RoomScale, RoomScale);
    bbEntityType(d->obj, HIT_MAP);
    bbEntityType(d->obj2, HIT_MAP);

    d->id = DoorTempID;
    DoorTempID = DoorTempID+1;

    d->tag = tag;
    d->code = code;

    for (int i = 0; i < 2; i++) {
        if (!tag.isEmpty()) {
            MeshAssetWrap* buttonKeyObj = MeshAssetWrap::grab("GFX/Map/Meshes/ButtonKeycard.b3d", false);
            d->buttons[i] = bbCopyMeshModelEntity(buttonKeyObj->getMesh());
            buttonKeyObj->drop();
        } else if (!code.isEmpty()) {
            MeshAssetWrap* buttonCodeObj = MeshAssetWrap::grab("GFX/Map/Meshes/ButtonCode.b3d", false);
            d->buttons[i] = bbCopyMeshModelEntity(buttonCodeObj->getMesh());
            bbEntityFX(d->buttons[i], 1);
            buttonCodeObj->drop();
        } else {
            d->buttons[i] = bbCopyMeshModelEntity(buttonObj->getMesh());
        }

        bbScaleEntity(d->buttons[i], 0.03f, 0.03f, 0.03f);
    }

    if (d->typ == DOOR_TYPE_CONT) {
        bbPositionEntity(d->buttons[0], x - 432.f * RoomScale, y + 0.7f, z + 192.f * RoomScale);
        bbPositionEntity(d->buttons[1], x + 432.f * RoomScale, y + 0.7f, z - 192.f * RoomScale);
        bbRotateEntity(d->buttons[0], 0, 90, 0);
        bbRotateEntity(d->buttons[1], 0, 270, 0);
    } else {
        bbPositionEntity(d->buttons[0], x + 0.6f, y + 0.7f, z - 0.1f);
        bbPositionEntity(d->buttons[1], x - 0.6f, y + 0.7f, z + 0.1f);
        bbRotateEntity(d->buttons[1], 0, 180, 0);
    }
    bbEntityParent(d->buttons[0], d->frameobj);
    bbEntityParent(d->buttons[1], d->frameobj);
    bbEntityPickMode(d->buttons[0], 2);
    bbEntityPickMode(d->buttons[1], 2);

    bbPositionEntity(d->obj, x, y, z);

    bbRotateEntity(d->obj, 0, angle, 0);
    bbRotateEntity(d->frameobj, 0, angle, 0);

    if (d->obj2 != 0) {
        bbPositionEntity(d->obj2, x, y, z);
        if (d->typ == DOOR_TYPE_CONT) {
            bbRotateEntity(d->obj2, 0, angle, 0);
        } else {
            bbRotateEntity(d->obj2, 0, angle + 180, 0);
        }
        bbEntityParent(d->obj2, parent);
    }

    bbEntityParent(d->frameobj, parent);
    bbEntityParent(d->obj, parent);

    d->angle = angle;
    d->open = open;

    bbEntityPickMode(d->obj, 3);
    MakeCollBox(d->obj);
    if (d->obj2 != 0) {
        bbEntityPickMode(d->obj2, 3);
        MakeCollBox(d->obj2);
    }

    bbEntityPickMode(d->frameobj,2);

    d->room = room;

    d->mtfClose = true;

    //Bust his nut.
    doorObj->drop();
    //Bust his nut!!!
    doorFrameObj->drop();
    //BUST HIS NUT!!!
    doorColl->drop();
    //B U S T  H I S  N U T  ! ! !
    buttonObj->drop();

    contDoorLeft->drop();
    contDoorRight->drop();

    for (int i = 0; i <= 1; i++) {
        hczDoorObj[i]->drop();
    }

    return d;
}

void UpdateDoors() {
    if (UpdateDoorsTimer <= 0) {
        for (int i = 0; i < Door::getListSize(); i++) {
            Door* d = Door::getObject(i);

            float xdist = abs(bbEntityX(mainPlayer->collider)-bbEntityX(d->obj,true));
            float zdist = abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->obj,true));

            d->dist = xdist+zdist;

            if (d->dist > HideDistance*2) {
                if (d->obj != 0) {
                    bbHideEntity(d->obj);
                }
                if (d->frameobj != 0) {
                    bbHideEntity(d->frameobj);
                }
                if (d->obj2 != 0) {
                    bbHideEntity(d->obj2);
                }
                if (d->buttons[0] != 0) {
                    bbHideEntity(d->buttons[0]);
                }
                if (d->buttons[1] != 0) {
                    bbHideEntity(d->buttons[1]);
                }
            } else {
                if (d->obj != 0) {
                    bbShowEntity(d->obj);
                }
                if (d->frameobj != 0) {
                    bbShowEntity(d->frameobj);
                }
                if (d->obj2 != 0) {
                    bbShowEntity(d->obj2);
                }
                if (d->buttons[0] != 0) {
                    bbShowEntity(d->buttons[0]);
                }
                if (d->buttons[1] != 0) {
                    bbShowEntity(d->buttons[1]);
                }
            }
        }

        UpdateDoorsTimer = 30;
    } else {
        UpdateDoorsTimer = Max(UpdateDoorsTimer-timing->tickDuration,0);
    }

    mainPlayer->closestButton = 0;
    mainPlayer->closestDoor = nullptr;

    for (int i = 0; i < Door::getListSize(); i++) {
        Door* d = Door::getObject(i);

        if (d->dist < HideDistance*2) {

            if ((d->openstate >= 180 || d->openstate <= 0) && mainPlayer->grabbedEntity == 0) {
                for (int j = 0; j < 2; j++) {
                    if (d->buttons[j] != 0) {
                        if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(d->buttons[j],true)) < 1.f) {
                            if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->buttons[j],true)) < 1.f) {
                                //entityDistance(collider, d\buttons[i])
                                float dist = Distance(bbEntityX(mainPlayer->collider, true), bbEntityZ(mainPlayer->collider, true), bbEntityX(d->buttons[j], true), bbEntityZ(d->buttons[j], true));
                                if (dist < 0.7f) {
                                    //TODO: use deltayaw as faster way to determine whether the player can press the button or not
                                    Pivot* tempPvt = bbCreatePivot();
                                    bbPositionEntity(tempPvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                                    bbPointEntity(tempPvt,d->buttons[j]);

                                    if (bbEntityPick(tempPvt, 0.6f) == d->buttons[j]) {
                                        if (mainPlayer->closestButton == 0) {
                                            mainPlayer->closestButton = d->buttons[j];
                                            mainPlayer->closestDoor = d;
                                        } else {
                                            if (dist < bbEntityDistance(mainPlayer->collider, mainPlayer->closestButton)) {
                                                mainPlayer->closestButton = d->buttons[j];
                                                mainPlayer->closestDoor = d;
                                            }
                                        }
                                    }
                                    bbFreeEntity(tempPvt);
                                }
                            }
                        }

                    }
                }
            }

            if (d->open) {
                if (d->openstate < 180) {
                    switch (d->typ) {
                        case DOOR_TYPE_CONT: {
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 0.8f);
                            bbMoveEntity(d->obj, bbSin(d->openstate) * timing->tickDuration / 180.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, -bbSin(d->openstate) * timing->tickDuration / 180.f, 0, 0);
                            }
                        } break;
                        case DOOR_TYPE_HCZ: {
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * (d->fastopen+1) * timing->tickDuration / 85.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen*2+1) * timing->tickDuration / 120.f, 0, 0);
                            }
                        } break;
                        default: {
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * (d->fastopen*2+1) * timing->tickDuration / 80.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * timing->tickDuration / 80.f, 0, 0);
                            }
                        } break;
                    }
                } else {
                    d->fastopen = 0;
                    bbResetEntity(d->obj);
                    if (d->obj2 != nullptr) {
                        bbResetEntity(d->obj2);
                    }
                    if (d->timerstate > 0) {
                        d->timerstate = Max(0, d->timerstate - timing->tickDuration);
                        if (d->timerstate + timing->tickDuration > 110 && d->timerstate <= 110) {
                            PlayRangedSound_SM(sndManager->caution, mainPlayer->cam, d->obj);
                        }

                        if (d->timerstate == 0) {
                            d->open = (!d->open);
                            switch (d->typ) {
                                case DOOR_TYPE_CONT: {
                                    PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
                                } break;
                                case DOOR_TYPE_HCZ: {
                                    PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                } break;
                                default: {
                                    PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                } break;
                            }
                        }
                    }
                    if (d->autoClose && RemoteDoorOn == true) {
                        if (bbEntityDistance(mainPlayer->cam, d->obj) < 2.1f) {
                            //PlaySound2(HorrorSFX(7))) ;TODO: fix
                            d->open = false;
                            switch (d->typ) {
                                case DOOR_TYPE_CONT: {
                                    PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
                                } break;
                                case DOOR_TYPE_HCZ: {
                                    PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                } break;
                                default: {
                                    PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                } break;
                            }
                            d->autoClose = false;
                        }
                    }
                }
            } else {
                if (d->openstate > 0) {
                    switch (d->typ) {
                        case DOOR_TYPE_CONT: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration*0.8f);
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration / 180.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * timing->tickDuration / 180.f, 0, 0);
                            }
                            if (d->openstate < 15 && d->openstate+timing->tickDuration >= 15) {
                                for (int j = 0; j < bbRand(75,99); j++) {
                                    Pivot* tempPvt = bbCreatePivot();
                                    bbPositionEntity(tempPvt, bbEntityX(d->frameobj,true)+bbRnd(-0.2f,0.2f), bbEntityY(d->frameobj,true)+bbRnd(0.f,1.2f), bbEntityZ(d->frameobj,true)+bbRnd(-0.2f,0.2f));
                                    bbRotateEntity(tempPvt, 0, bbRnd(360), 0);

                                    Particle* p = CreateParticle(bbEntityX(tempPvt), bbEntityY(tempPvt), bbEntityZ(tempPvt), PARTICLE_DUST, 0.002f, 0, 300);
                                    p->speed = 0.005f;
                                    bbRotateEntity(p->pvt, bbRnd(-20, 20), bbRnd(360), 0);

                                    p->sizeChange = -0.00001f;
                                    p->size = 0.01f;
                                    bbScaleSprite(p->sprite,p->size,p->size);

                                    p->aChange = -0.01f;

                                    bbEntityOrder(p->sprite,-1);

                                    bbFreeEntity(tempPvt);
                                }
                            }
                        } break;
                        case DOOR_TYPE_HCZ: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration * (d->fastopen+1) / 85.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * -timing->tickDuration / 120.f, 0, 0);
                            }
                        } break;
                        default: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration * (d->fastopen+1) / 80.f, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * -timing->tickDuration / 80.f, 0, 0);
                            }
                        } break;
                    }

                    if (d->angle == 0 || d->angle==180) {
                        if (abs(bbEntityZ(d->frameobj, true)-bbEntityZ(mainPlayer->collider))<0.15f) {
                            if (abs(bbEntityX(d->frameobj, true)-bbEntityX(mainPlayer->collider))<0.7f*(d->typ*2+1)) {
                                float z = CurveValue(bbEntityZ(d->frameobj,true)+0.15f*Sgn(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->frameobj, true)), bbEntityZ(mainPlayer->collider), 5);
                                bbPositionEntity(mainPlayer->collider, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), z);
                            }
                        }
                    } else {
                        if (abs(bbEntityX(d->frameobj, true)-bbEntityX(mainPlayer->collider))<0.15f) {
                            if (abs(bbEntityZ(d->frameobj, true)-bbEntityZ(mainPlayer->collider))<0.7f*(d->typ*2+1)) {
                                float x = CurveValue(bbEntityX(d->frameobj,true)+0.15f*Sgn(bbEntityX(mainPlayer->collider)-bbEntityX(d->frameobj, true)), bbEntityX(mainPlayer->collider), 5);
                                bbPositionEntity(mainPlayer->collider, x, bbEntityY(mainPlayer->collider), bbEntityZ(mainPlayer->collider));
                            }
                        }
                    }

                } else {
                    d->fastopen = 0;
                    bbPositionEntity(d->obj, bbEntityX(d->frameobj, true), bbEntityY(d->frameobj, true), bbEntityZ(d->frameobj, true));
                    if (d->obj2 != 0) {
                        bbPositionEntity(d->obj2, bbEntityX(d->frameobj, true), bbEntityY(d->frameobj, true), bbEntityZ(d->frameobj, true));
                    }
                    if (d->obj2 != 0 && d->typ == DOOR_TYPE_DEF) {
                        bbMoveEntity(d->obj, 0, 0, 8.f * RoomScale);
                        bbMoveEntity(d->obj2, 0, 0, 8.f * RoomScale);
                    }
                }
            }
        }
    }
}

void UseDoor(Door* d, int showmsg) {
    int i;
    Item* item;
    int playerHasKeycard = false;
    int playerHasRightKeycard = false;

    //Does the door require a keycard?
    if (!d->tag.isEmpty()) {
        //Does the player have the right keycard?
        for (i = 0; i <= mainPlayer->inventory->size-1; i++) {
            item = mainPlayer->inventory->items[i];
            if (item != nullptr) {
                if (item->itemTemplate->name.equals("keycard")) {
                    if (d->locked) {
                        PlaySound_SM(sndManager->keycardErr);
                        Msg = "The keycard was inserted into the slot but nothing happened.";
                        MsgTimer = 70 * 5;
                        return;
                    }

                    playerHasKeycard = true;
                    if (HasTag(item, d->tag) || HasTag(item, ITEM_TAG_OMNI)) {
                        playerHasRightKeycard = true;
                        break;
                    }
                }
            }
        }

        if (playerHasRightKeycard) {
            PlaySound_SM(sndManager->keycardUse);
            if (showmsg) {
                Msg = "The keycard was inserted into the slot.";
                MsgTimer = 70 * 5;
            }
        } else if ((playerHasKeycard)) {
            PlaySound_SM(sndManager->keycardErr);
            Msg = "A keycard with a higher security clearance is required to operate this door.";
            MsgTimer = 70 * 5;
            return;
        } else {
            Msg = "A keycard is required to operate this door.";
            MsgTimer = 70 * 5;
            return;
        }
    } else {
        if (d->locked) {
            if (showmsg == true) {
                if (!(d->isElevatorDoor>0)) {
                    PlaySound_SM(sndManager->buttonErr);
                    Msg = "The door appears to be locked.";
                    MsgTimer = 70 * 5;
                } else {
                    if (d->isElevatorDoor == 1) {
                        Msg = "You called the elevator.";
                        MsgTimer = 70 * 5;
                    } else if (!Msg.equals("You called the elevator.")) {
                        if (Msg.equals("You already called the elevator.") || MsgTimer<70*3) {
                            switch (bbRand(10)) {
                                case 1: {
                                    Msg = "Stop spamming the button.";
                                    MsgTimer = 70 * 7;
                                } break;
                                case 2: {
                                    Msg = "Pressing it harder does not make the elevator come faster.";
                                    MsgTimer = 70 * 7;
                                } break;
                                case 3: {
                                    Msg = "If you continue pressing this button I will generate a Memory Access- oh wait I can't do that anymore.";
                                    MsgTimer = 70 * 7;
                                } break;
                                default: {
                                    Msg = "You already called the elevator.";
                                    MsgTimer = 70 * 7;
                                } break;
                            }
                        }
                    } else {
                        Msg = "You already called the elevator.";
                        MsgTimer = 70 * 7;
                    }
                }

            }
            return;
        }
    }

    d->open = (!d->open);
    if (d->linkedDoor != nullptr) {
        d->linkedDoor->open = (!d->linkedDoor->open);
    }

    if (d->open) {
        if (d->linkedDoor != nullptr) {
            d->linkedDoor->timerstate = d->linkedDoor->timer;
        }
        d->timerstate = d->timer;
        switch (d->typ) {
            case DOOR_TYPE_CONT: {
                PlayRangedSound_SM(sndManager->openBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
            } break;
            case DOOR_TYPE_HCZ: {
                PlayRangedSound_SM(sndManager->openHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            } break;
            default: {
                PlayRangedSound_SM(sndManager->openDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            } break;
        }
    } else {
        switch (d->typ) {
            case DOOR_TYPE_CONT: {
                PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
            } break;
            case DOOR_TYPE_HCZ: {
                PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            } break;
            default: {
                PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            } break;
        }
    }
}

void RemoveDoor(Door* d) {
    for (int i = 0; i < 2; i++) {
        if (d->buttons[i] != nullptr) {
            bbEntityParent(d->buttons[i], 0);
            bbFreeEntity(d->buttons[i]);
        }
    }

    if (d->obj != nullptr) {
        bbFreeEntity(d->obj);
    }
    if (d->obj2 != nullptr) {
        bbFreeEntity(d->obj2);
    }
    if (d->frameobj != nullptr) {
        bbFreeEntity(d->frameobj);
    }

    delete d;
}

}

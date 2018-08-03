#include "Doors.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Door*> Door::list;
Door::Door() {
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

// Constants.
const int DOOR_TYPE_DEF = 0;
const int DOOR_TYPE_HCZ = 1;
const int DOOR_TYPE_CONT = 2;

// Globals.
float UpdateDoorsTimer;
int DoorTempID;

// Functions.
Door* CreateDoor(float x, float y, float z, float angle, Room* room, int open = false, int doorType = DOOR_TYPE_DEF, String tag = "", String code = "") {
    Door* d;
    int parent;
    int i;
    if (room != nullptr) {
        parent = room->obj;
    }

    int doorObj = GrabMesh("GFX/Map/Meshes/door.b3d");
    int doorFrameObj = GrabMesh("GFX/Map/Meshes/doorframe.b3d");
    int doorColl = GrabMesh("GFX/Map/Meshes/doorcoll.b3d");
    int buttonObj = GrabMesh("GFX/Map/Meshes/button.b3d");

    int contDoorLeft = GrabMesh("GFX/Map/Meshes/ContDoorLeft.b3d");
    int contDoorRight = GrabMesh("GFX/Map/Meshes/ContDoorRight.b3d");

    int hczDoorObj[2];
    for (i = 0; i <= 1; i++) {
        hczDoorObj[i] = GrabMesh("GFX/Map/Meshes/heavydoor" + String(i + 1) + ".b3d");
    }

    int buttonCodeObj;
    int buttonKeyObj;
    int buttonScannerOBJ;

    d = new Door();
    d->typ = doorType;
    switch (d->typ) {
        case DOOR_TYPE_CONT: {
            d->obj = bbCopyEntity(contDoorRight);
            bbScaleEntity(d->obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale);
            d->obj2 = bbCopyEntity(contDoorLeft);
            bbScaleEntity(d->obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale);

            d->frameobj = bbCopyEntity(doorColl);
            bbScaleEntity(d->frameobj, RoomScale, RoomScale, RoomScale);
            bbEntityType(d->frameobj, HIT_MAP);
            bbEntityAlpha(d->frameobj, 0.0);
        }
        case DOOR_TYPE_HCZ: {
            d->obj = bbCopyEntity(hczDoorObj[0]);
            bbScaleEntity(d->obj, RoomScale, RoomScale, RoomScale);
            d->obj2 = bbCopyEntity(hczDoorObj[1]);
            bbScaleEntity(d->obj2, RoomScale, RoomScale, RoomScale);

            d->frameobj = bbCopyEntity(doorFrameObj);
        }
        default: {
            d->obj = bbCopyEntity(doorObj);
            bbScaleEntity(d->obj, (204.0 * RoomScale) / bbMeshWidth(d->obj), 312.0 * RoomScale / bbMeshHeight(d->obj), 16.0 * RoomScale / bbMeshDepth(d->obj));

            d->frameobj = bbCopyEntity(doorFrameObj);
            d->obj2 = bbCopyEntity(doorObj);

            bbScaleEntity(d->obj2, (204.0 * RoomScale) / bbMeshWidth(d->obj), 312.0 * RoomScale / bbMeshHeight(d->obj), 16.0 * RoomScale / bbMeshDepth(d->obj));
            //entityType d\obj2, HIT_MAP
        }
    }

    bbPositionEntity(d->frameobj, x, y, z);
    bbScaleEntity(d->frameobj, RoomScale, RoomScale, RoomScale);
    bbEntityType(d->obj, HIT_MAP);
    bbEntityType(d->obj2, HIT_MAP);

    d->id = DoorTempID;
    DoorTempID = DoorTempID+1;

    d->tag = tag;
    d->code = code;

    for (i = 0; i <= 1; i++) {
        if (!tag.isEmpty()) {
            buttonKeyObj = GrabMesh("GFX/Map/Meshes/ButtonKeycard.b3d");
            d->buttons[i] = bbCopyEntity(buttonKeyObj);
            DropAsset(buttonKeyObj);
        } else if (!code.isEmpty()) {
            buttonCodeObj = GrabMesh("GFX/Map/Meshes/ButtonCode.b3d");
            d->buttons[i] = bbCopyEntity(buttonCodeObj);
            bbEntityFX(d->buttons[i], 1);
            DropAsset(buttonCodeObj);
        } else {
            d->buttons[i] = bbCopyEntity(buttonObj);
        }

        bbScaleEntity(d->buttons[i], 0.03, 0.03, 0.03);
    }

    if (d->typ == DOOR_TYPE_CONT) {
        bbPositionEntity(d->buttons[0], x - 432.0 * RoomScale, y + 0.7, z + 192.0 * RoomScale);
        bbPositionEntity(d->buttons[1], x + 432.0 * RoomScale, y + 0.7, z - 192.0 * RoomScale);
        bbRotateEntity(d->buttons[0], 0, 90, 0);
        bbRotateEntity(d->buttons[1], 0, 270, 0);
    } else {
        bbPositionEntity(d->buttons[0], x + 0.6, y + 0.7, z - 0.1);
        bbPositionEntity(d->buttons[1], x - 0.6, y + 0.7, z + 0.1);
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
    DropAsset(doorObj);
    //Bust his nut!!!
    DropAsset(doorFrameObj);
    //BUST HIS NUT!!!
    DropAsset(doorColl);
    //B U S T  H I S  N U T  ! ! !
    DropAsset(buttonObj);

    DropAsset(contDoorLeft);
    DropAsset(contDoorRight);

    for (i = 0; i <= 1; i++) {
        DropAsset(hczDoorObj[i]);
    }

    return d;
}

void UpdateDoors() {
    int i;
    Door* d;
    Particle* p;
    int pvt;
    float x;
    float z;
    int temp;

    float dist;
    float xdist;
    float zdist;

    if (UpdateDoorsTimer ==< 0) {
        for (int iterator55 = 0; iterator55 < Door::getListSize(); iterator55++) {
            d = Door::getObject(iterator55);

            xdist = Abs(bbEntityX(mainPlayer->collider)-bbEntityX(d->obj,true));
            zdist = Abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->obj,true));

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

    for (int iterator56 = 0; iterator56 < Door::getListSize(); iterator56++) {
        d = Door::getObject(iterator56);

        if (d->dist < HideDistance*2) {

            if ((d->openstate >= 180 | d->openstate <= 0) & mainPlayer->grabbedEntity == 0) {
                for (i = 0; i <= 1; i++) {
                    if (d->buttons[i] != 0) {
                        if (Abs(bbEntityX(mainPlayer->collider)-bbEntityX(d->buttons[i],true)) < 1.0) {
                            if (Abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->buttons[i],true)) < 1.0) {
                                //entityDistance(collider, d\buttons[i])
                                dist = Distance(bbEntityX(mainPlayer->collider, true), bbEntityZ(mainPlayer->collider, true), bbEntityX(d->buttons[i], true), bbEntityZ(d->buttons[i], true));
                                if (dist < 0.7) {
                                    //TODO: use deltayaw as faster way to determine whether the player can press the button or not
                                    temp = bbCreatePivot();
                                    bbPositionEntity(temp, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                                    bbPointEntity(temp,d->buttons[i]);

                                    if (bbEntityPick(temp, 0.6) == d->buttons[i]) {
                                        if (mainPlayer->closestButton == 0) {
                                            mainPlayer->closestButton = d->buttons[i];
                                            mainPlayer->closestDoor = d;
                                        } else {
                                            if (dist < bbEntityDistance(mainPlayer->collider, mainPlayer->closestButton)) {
                                                mainPlayer->closestButton = d->buttons[i];
                                                mainPlayer->closestDoor = d;
                                            }
                                        }
                                    }

                                    bbFreeEntity(temp);

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
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 0.8);
                            bbMoveEntity(d->obj, bbSin(d->openstate) * timing->tickDuration / 180.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, -bbSin(d->openstate) * timing->tickDuration / 180.0, 0, 0);
                            }
                        }
                        case DOOR_TYPE_HCZ: {
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * (d->fastopen+1) * timing->tickDuration / 85.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen*2+1) * timing->tickDuration / 120.0, 0, 0);
                            }
                        }
                        default: {
                            d->openstate = Min(180, d->openstate + timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * (d->fastopen*2+1) * timing->tickDuration / 80.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * timing->tickDuration / 80.0, 0, 0);
                            }
                        }
                    }
                } else {
                    d->fastopen = 0;
                    bbResetEntity(d->obj);
                    if (d->obj2 != 0) {
                        bbResetEntity(d->obj2);
                    }
                    if (d->timerstate > 0) {
                        d->timerstate = Max(0, d->timerstate - timing->tickDuration);
                        if (d->timerstate + timing->tickDuration > 110 & d->timerstate <= 110) {
                            PlayRangedSound_SM(sndManager->caution, mainPlayer->cam, d->obj);
                        }

                        if (d->timerstate == 0) {
                            d->open = (!d->open);
                            switch (d->typ) {
                                case DOOR_TYPE_CONT: {
                                    PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
                                }
                                case DOOR_TYPE_HCZ: {
                                    PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                }
                                default: {
                                    PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                }
                            }
                        }
                    }
                    if (d->autoClose & RemoteDoorOn == true) {
                        if (bbEntityDistance(mainPlayer->cam, d->obj) < 2.1) {
                            //PlaySound2(HorrorSFX(7))) ;TODO: fix
                            d->open = false;
                            switch (d->typ) {
                                case DOOR_TYPE_CONT: {
                                    PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
                                }
                                case DOOR_TYPE_HCZ: {
                                    PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                }
                                default: {
                                    PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
                                }
                            }
                            d->autoClose = false;
                        }
                    }
                }
            } else {
                if (d->openstate > 0) {
                    switch (d->typ) {
                        case DOOR_TYPE_CONT: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration*0.8);
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration / 180.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * timing->tickDuration / 180.0, 0, 0);
                            }
                            if (d->openstate < 15 & d->openstate+timing->tickDuration ==> 15) {
                                for (i = 0; i <= bbRand(75,99); i++) {
                                    pvt = bbCreatePivot();
                                    bbPositionEntity(pvt, bbEntityX(d->frameobj,true)+bbRnd(-0.2,0.2), bbEntityY(d->frameobj,true)+bbRnd(0.0,1.2), bbEntityZ(d->frameobj,true)+bbRnd(-0.2,0.2));
                                    bbRotateEntity(pvt, 0, bbRnd(360), 0);

                                    p = CreateParticle(bbEntityX(pvt), bbEntityY(pvt), bbEntityZ(pvt), PARTICLE_DUST, 0.002, 0, 300);
                                    p->speed = 0.005;
                                    bbRotateEntity(p->pvt, bbRnd(-20, 20), bbRnd(360), 0);

                                    p->sizeChange = -0.00001;
                                    p->size = 0.01;
                                    bbScaleSprite(p->obj,p->size,p->size);

                                    p->aChange = -0.01;

                                    bbEntityOrder(p->obj,-1);

                                    bbFreeEntity(pvt);
                                }
                            }
                        }
                        case DOOR_TYPE_HCZ: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration * (d->fastopen+1) / 85.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * -timing->tickDuration / 120.0, 0, 0);
                            }
                        }
                        default: {
                            d->openstate = Max(0, d->openstate - timing->tickDuration * 2 * (d->fastopen+1));
                            bbMoveEntity(d->obj, bbSin(d->openstate) * -timing->tickDuration * (d->fastopen+1) / 80.0, 0, 0);
                            if (d->obj2 != 0) {
                                bbMoveEntity(d->obj2, bbSin(d->openstate) * (d->fastopen+1) * -timing->tickDuration / 80.0, 0, 0);
                            }
                        }
                    }

                    if (d->angle == 0 | d->angle==180) {
                        if (Abs(bbEntityZ(d->frameobj, true)-bbEntityZ(mainPlayer->collider))<0.15) {
                            if (Abs(bbEntityX(d->frameobj, true)-bbEntityX(mainPlayer->collider))<0.7*(d->typ*2+1)) {
                                z = CurveValue(bbEntityZ(d->frameobj,true)+0.15*Sgn(bbEntityZ(mainPlayer->collider)-bbEntityZ(d->frameobj, true)), bbEntityZ(mainPlayer->collider), 5);
                                bbPositionEntity(mainPlayer->collider, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider), z);
                            }
                        }
                    } else {
                        if (Abs(bbEntityX(d->frameobj, true)-bbEntityX(mainPlayer->collider))<0.15) {
                            if (Abs(bbEntityZ(d->frameobj, true)-bbEntityZ(mainPlayer->collider))<0.7*(d->typ*2+1)) {
                                x = CurveValue(bbEntityX(d->frameobj,true)+0.15*Sgn(bbEntityX(mainPlayer->collider)-bbEntityX(d->frameobj, true)), bbEntityX(mainPlayer->collider), 5);
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
                    if (d->obj2 != 0 & d->typ == DOOR_TYPE_DEF) {
                        bbMoveEntity(d->obj, 0, 0, 8.0 * RoomScale);
                        bbMoveEntity(d->obj2, 0, 0, 8.0 * RoomScale);
                    }
                }
            }
        }
    }
}

void UseDoor(Door* d, int showmsg = true) {
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
                if (item->template->name.equals("keycard")) {
                    if (d->locked) {
                        PlaySound_SM(sndManager->keycardErr);
                        Msg = "The keycard was inserted into the slot but nothing happened.";
                        MsgTimer = 70 * 5;
                        return;
                    }

                    playerHasKeycard = true;
                    if (HasTag(item, d->tag) | HasTag(item, ITEM_TAG_OMNI)) {
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
                    } else if ((Msg!="You called the elevator.")) {
                        if (Msg=="You already called the elevator.") | (MsgTimer<70*3) {
                            switch (bbRand(10)) {
                                case 1: {
                                    Msg = "bbStop spamming the button.";
                                    MsgTimer = 70 * 7;
                                }
                                case 2: {
                                    Msg = "Pressing it harder does not make the elevator come faster.";
                                    MsgTimer = 70 * 7;
                                }
                                case 3: {
                                    Msg = "If you continue pressing this button I will generate a Memory Access Violation.";
                                    MsgTimer = 70 * 7;
                                }
                                default: {
                                    Msg = "You already called the elevator.";
                                    MsgTimer = 70 * 7;
                                }
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
            }
            case DOOR_TYPE_HCZ: {
                PlayRangedSound_SM(sndManager->openHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            }
            default: {
                PlayRangedSound_SM(sndManager->openDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            }
        }
    } else {
        switch (d->typ) {
            case DOOR_TYPE_CONT: {
                PlayRangedSound_SM(sndManager->closeBigDoor[bbRand(0, 1)], mainPlayer->cam, d->obj);
            }
            case DOOR_TYPE_HCZ: {
                PlayRangedSound_SM(sndManager->closeHCZDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            }
            default: {
                PlayRangedSound_SM(sndManager->closeDoor[bbRand(0, 2)], mainPlayer->cam, d->obj);
            }
        }
    }
}

void RemoveDoor(Door* d) {
    if (d->buttons[0] != 0) {
        bbEntityParent(d->buttons[0], 0);
    }
    if (d->buttons[1] != 0) {
        bbEntityParent(d->buttons[1], 0);
    }

    if (d->obj != 0) {
        bbFreeEntity(d->obj);
    }
    if (d->obj2 != 0) {
        bbFreeEntity(d->obj2);
    }
    if (d->frameobj != 0) {
        bbFreeEntity(d->frameobj);
    }
    if (d->buttons[0] != 0) {
        bbFreeEntity(d->buttons[0]);
    }
    if (d->buttons[1] != 0) {
        bbFreeEntity(d->buttons[1]);
    }

    delete d;
}

}

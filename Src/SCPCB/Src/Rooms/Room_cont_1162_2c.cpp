#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "Room_cont_1162_2c.h"

namespace CBN {

// Functions.
void FillRoom_cont_1162_2c(Room* r) {
    Door* d = CreateDoor(r->x + 248.f*RoomScale, 0.f, r->z - 736.f*RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0],r->x+1012.f*RoomScale,r->y+128.f*RoomScale,r->z-640.f*RoomScale);
    bbEntityParent(r->objects[0],r->obj);
    bbEntityPickMode(r->objects[0],1);
    Item* it = CreatePaper("doc1162", r->x + 863.227f * RoomScale, r->y + 152.f * RoomScale, r->z - 953.231f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    SecurityCam* sc = CreateSecurityCam(r->x-192.f*RoomScale, r->y+704.f*RoomScale, r->z+192.f*RoomScale, r);
    sc->angle = 225;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    sc->id = 8;
}

void UpdateEvent_cont_1162_2c(Event* e) {
    //e\eventState = A variable to determine the "nostalgia" items
    //- 0.f = No nostalgia item
    //- 1.f = Lost key
    //- 2.f = Disciplinary Hearing DH-S-4137-17092
    //- 3.f = Coin
    //- 4.f = Movie Ticket
    //- 5.f = Old Badge
    //e\eventState2 = Defining which slot from the Inventory should be picked
    //e\eventState3 = A check for if a item should be removed
    //- 0.f = no item "trade" will happen
    //- 1.f = item "trade" will happen
    //- 2.f = the player doesn't has any items in the Inventory, giving him heavily injuries and giving him a random item
    //- 3.f = player got a memorial item (to explain a bit D-9341's background)
    //- 3.1f = player got a memorial item + injuries (because he didn't had any item in his inventory before)
    if (mainPlayer->currRoom == e->room) {

        mainPlayer->grabbedEntity = 0;

        e->eventState = 0;

        bool pick1162 = true;
        Pivot* pp = bbCreatePivot(e->room->obj);
        bbPositionEntity(pp,976,128,-640,false);

        for (int iterator159 = 0; iterator159 < Item::getListSize(); iterator159++) {
            Item* it = Item::getObject(iterator159);

            if (!it->picked) {
                if (bbEntityDistance(it->collider,e->room->objects[0])<0.75f) {
                    pick1162 = false;
                }
            }
        }

        if (bbEntityDistance(e->room->objects[0],mainPlayer->collider)<0.75f && pick1162) {
            DrawHandIcon = true;
            if (MouseHit1) {
                mainPlayer->grabbedEntity = e->room->objects[0];
            }
        }

        if (mainPlayer->grabbedEntity != 0) {
            e->eventState2 = bbRand(0,mainPlayer->inventory->size-1);
            if (mainPlayer->inventory->items[(int)(e->eventState2)]!=nullptr) {
                //randomly picked item slot has an item in it, using this slot
                e->eventState3 = 1.f;
                std::cout << "pick1";
            } else {
                //randomly picked item slot is empty, getting the first available slot
                for (int i = 0; i < mainPlayer->inventory->size; i++) {
                    bool isSlotEmpty = (mainPlayer->inventory->items[(int)(i+e->eventState2) % mainPlayer->inventory->size] == nullptr);

                    if (!isSlotEmpty) {
                        //successful
                        e->eventState2 = modFloat(i+e->eventState2, mainPlayer->inventory->size);
                    }

                    if (bbRand(8)==1) {
                        if (isSlotEmpty) {
                            e->eventState3 = 3.1f;
                        } else {
                            e->eventState3 = 3.f;
                        }

                        e->eventState = bbRand(1,5);

                        //Checking if the selected nostalgia item already exists or not
                        String itemName = "";
                        switch ((int)e->eventState) {
                            case 1: {
                                itemName = "Lost Key";
                            } break;
                            case 2: {
                                itemName = "Disciplinary Hearing DH-S-4137-17092";
                            } break;
                            case 3: {
                                itemName = "Coin";
                            } break;
                            case 4: {
                                itemName = "Movie Ticket";
                            } break;
                            case 5: {
                                itemName = "Old Badge";
                            } break;
                        }

                        bool itemExists = false;
                        for (int iterator160 = 0; iterator160 < Item::getListSize(); iterator160++) {
                            Item* it = Item::getObject(iterator160);

                            if (it->name.equals(itemName)) {
                                itemExists = true;
                                e->eventState3 = 1.f;
                                e->eventState = 0.f;
                                break;
                            }
                        }

                        if ((!itemExists) && (!isSlotEmpty)) {
                            break;
                        }
                    } else {
                        if (isSlotEmpty) {
                            e->eventState3 = 2.f;
                        } else {
                            e->eventState3 = 1.f;
                            break;
                        }
                    }
                }
            }
        }


        //trade successful
        if (e->eventState3 == 1.f) {
            bool shouldCreateItem = false;

            for (int iterator161 = 0; iterator161 < ItemTemplate::getListSize(); iterator161++) {
                ItemTemplate* itt = ItemTemplate::getObject(iterator161);

                if (IsItemGoodFor1162(itt)) {
                    // TODO: No.
                    //switch (mainPlayer->inventory->items[(int)(e->eventState2)]->itemTemplate->name) {
                    //    case "key": {
                    //        if (itt->name.equals("key1") || itt->name.equals("key2") && bbRand(2)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "lostkey";
                    //        }
                    //    }
                    //    case "paper","oldpaper": {
                    //        if (itt->name.equals("paper") && bbRand(12)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "paper";
                    //        }
                    //    }
                    //    case "gasmask","gasmask3","supergasmask","hazmatsuit","hazmatsuit2","hazmatsuit3": {
                    //        if (itt->name.equals("gasmask") | itt->name.equals("gasmask3") | itt->name.equals("supergasmask") | itt->name.equals("hazmatsuit") | itt->name.equals("hazmatsuit2") || itt->name.equals("hazmatsuit3") && bbRand(2)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "gasmask hazmat";
                    //        }
                    //    }
                    //    case "key1","key2","key3": {
                    //        if (itt->name.equals("key1") | itt->name.equals("key2") | itt->name.equals("key3") || itt->name.equals("misc") && bbRand(6)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "key";
                    //        }
                    //    }
                    //    case "vest","finevest": {
                    //        if (itt->name.equals("vest") || itt->name.equals("finevest") && bbRand(1)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "vest";
                    //        }
                    //    }
                    //    default: {
                    //        if (itt->name.equals("misc") && bbRand(6)==1) {
                    //            shouldCreateItem = true;
                    //            std::cout << "default";
                    //        }
                    //    }
                    //}
                }

                if (shouldCreateItem) {
                    //TODO: This was a float-int cast, redo.
                    //RemoveItem(mainPlayer\inventory\items[e\eventState2])
                    Item* it = CreateItem(itt->name,bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                    bbEntityType(it->collider, HIT_ITEM);
                    PlaySound2(LoadTempSound("SFX/SCP/1162/Exchange"+String(bbRand(0,4))+".ogg"));
                    e->eventState3 = 0.f;

                    MouseHit1 = false;
                    break;
                }
            }
            //trade not sucessful (player got in return to injuries a new item)
        } else if ((e->eventState3 == 2.f)) {
            mainPlayer->injuries = mainPlayer->injuries + 5.f;
            Pivot* pvt = bbCreatePivot();
            bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)-0.05f,bbEntityZ(mainPlayer->collider));
            bbTurnEntity(pvt, 90, 0, 0);
            bbEntityPick(pvt,0.3f);
            Decal* de = CreateDecal(DECAL_BLOOD_SPLATTER, bbPickedX(), bbPickedY()+0.005f, bbPickedZ(), 90, bbRand(360), 0);
            de->size = 0.75f;
            bbScaleSprite(de->obj, de->size, de->size);
            bbFreeEntity(pvt);
            for (int iterator162 = 0; iterator162 < ItemTemplate::getListSize(); iterator162++) {
                ItemTemplate* itt = ItemTemplate::getObject(iterator162);

                if (IsItemGoodFor1162(itt) && bbRand(6)==1) {
                    Item* it = CreateItem(itt->name, bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                    MouseHit1 = false;
                    e->eventState3 = 0.f;
                    if (mainPlayer->injuries > 15) {
                        DeathMSG = "A dead Class D subject was discovered within the containment chamber of SCP-1162.";
                        DeathMSG = DeathMSG + " An autopsy revealed that his right lung was missing, which suggests";
                        DeathMSG = DeathMSG + " interaction with SCP-1162.";
                        PlaySound2(LoadTempSound("SFX/SCP/1162/BodyHorrorExchange"+String(bbRand(1,4))+".ogg"));
                        mainPlayer->lightFlash = 5.f;
                        Kill(mainPlayer);
                    } else {
                        PlaySound2(LoadTempSound("SFX/SCP/1162/BodyHorrorExchange"+String(bbRand(1,4))+".ogg"));
                        mainPlayer->lightFlash = 5.f;
                        Msg = "You feel a sudden overwhelming pain in your chest.";
                        MsgTimer = 70*5;
                    }
                    break;
                }
            }
            //trade with nostalgia item
        } else if ((e->eventState3 >= 3.f)) {
            if (e->eventState3 < 3.1f) {
                PlaySound2(LoadTempSound("SFX/SCP/1162/Exchange"+String(bbRand(0,4))+".ogg"));
                RemoveItem(mainPlayer->inventory->items[(int)(e->eventState2)]);
            } else {
                mainPlayer->injuries = mainPlayer->injuries + 5.f;
                Pivot* pvt = bbCreatePivot();
                bbPositionEntity(pvt, bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)-0.05f,bbEntityZ(mainPlayer->collider));
                bbTurnEntity(pvt, 90, 0, 0);
                bbEntityPick(pvt,0.3f);
                Decal* de = CreateDecal(DECAL_BLOOD_SPLATTER, bbPickedX(), bbPickedY()+0.005f, bbPickedZ(), 90, bbRand(360), 0);
                de->size = 0.75f;
                bbScaleSprite(de->obj, de->size, de->size);
                bbFreeEntity(pvt);
                if (mainPlayer->injuries > 15) {
                    DeathMSG = "A dead Class D subject was discovered within the containment chamber of SCP-1162.";
                    DeathMSG = DeathMSG + " An autopsy revealed that his right lung was missing, which suggests";
                    DeathMSG = DeathMSG + " interaction with SCP-1162.";
                    PlaySound2(LoadTempSound("SFX/SCP/1162/BodyHorrorExchange"+String(bbRand(1,4))+".ogg"));
                    mainPlayer->lightFlash = 5.f;
                    Kill(mainPlayer);
                } else {
                    PlaySound2(LoadTempSound("SFX/SCP/1162/BodyHorrorExchange"+String(bbRand(1,4))+".ogg"));
                    mainPlayer->lightFlash = 5.f;
                    Msg = "You notice something moving in your pockets and a sudden pain in your chest.";
                    MsgTimer = 70*5;
                }
                e->eventState2 = 0.f;
            }
            Item* it = nullptr;
            switch ((int)e->eventState) {
                case 1: {
                    it = CreateItem("key",bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                } break;
                case 2: {
                    it = CreateItem("docDH",bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                } break;
                case 3: {
                    it = CreateItem("coin",bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                } break;
                case 4: {
                    it = CreateItem("ticket",bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                } break;
                case 5: {
                    it = CreateItem("badge9341",bbEntityX(pp,true),bbEntityY(pp,true),bbEntityZ(pp,true));
                } break;
            }

            if (it != nullptr) {
                bbEntityType(it->collider, HIT_ITEM);
            }
            MouseHit1 = false;
            e->eventState3 = 0.f;
        }
        bbFreeEntity(pp);
    }

}

int IsItemGoodFor1162(ItemTemplate* itt) {
    String in = itt->name;

    //TODO: remember which items the player has collected instead of just picking random shit
    //switch (itt->name) {
    //    case "key1", "key2", "key3": {
    //        return true;
    //    }
    //    case "misc", "420", "cigarette": {
    //        return true;
    //    }
    //    case "vest", "finevest","gasmask": {
    //        return true;
    //    }
    //    case "radio","18vradio": {
    //        return true;
    //    }
    //    case "clipboard","eyedrops","nvgoggles": {
    //        return true;
    //    }
    //    default: {
    //        if (itt->name != "paper") {
    //            return false;
    //        } else if ((bbInstr(itt->name, "Leaflet"))) {
    //            return false;
    //        } else {
    //            //if the item is a paper, only allow spawning it if the name contains the word "note" or "log"
    //            //(because those are items created recently, which D-9341 has most likely never seen)
    //            return ((!bbInstr(itt->name, "Note")) & (!bbInstr(itt->name, "bbLog")));
    //        }
    //    }
    //}
    return 0;
}

}

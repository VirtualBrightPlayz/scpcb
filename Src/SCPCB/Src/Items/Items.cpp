#include <bbfilesystem.h>
#include <bbblitz3d.h>
#include <bbstring.h>
#include <bbmath.h>
#include <bbgraphics.h>
#include <iostream>
#include <StringType.h>
#include <bbinput.h>

#include "Items.h"
#include "../Assets.h"
#include "../INI.h"
#include "../Player.h"
#include "../MapSystem.h"
#include "../FastResize.h"
#include "../GameMain.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Audio.h"
#include "../Options.h"

namespace CBN {

// Structs.
std::vector<ItemTemplate*> ItemTemplate::list;
ItemTemplate::ItemTemplate(const String& file, const String& section) {
    name = section;
    invName = GetINIString(file, section, "invname");

    //The model and inv image are in the specified directory.
    String dataPath = GetINIString(file, section, "datapath");
    if (!dataPath.isEmpty()) {
        if (bbFileType(dataPath) != 2) {
            throw "Item template directory not found (" + section + ", " + dataPath + ")";
        }

        this->objPath = dataPath + name + ".b3d";
        invImagePath[0] = GetImagePath(dataPath + "inv_" + name);
    }

    //Otherwise the obj, tex and inv paths are specified in the INI.
    String objectPath = GetINIString(file, section, "objpath");
    if (!objectPath.isEmpty()) {
        this->objPath = objectPath;
    }

    String texturePath = GetINIString(file, section, "texpath");
    if (!texturePath.isEmpty()) {
        this->texPath = texturePath;
    }

    String invImgPath = GetINIString(file, section, "invimgpath");
    if (!invImgPath.isEmpty()) {
        this->invImagePath[0] = invImgPath;
    }

    String invImgPath2 = GetINIString(file, section, "invimgpath2");
    if (!invImgPath2.isEmpty()) {
        this->invImagePath[1] = invImgPath2;
    }

    String slot = GetINIString(file, section, "slot").toLower();
    if (slot.equals("head")) {
        this->wornSlot = WORNITEM_SLOT_HEAD;
    }
    else if (slot.equals("body")) {
        this->wornSlot = WORNITEM_SLOT_BODY;
    }
    else {
        this->wornSlot = WORNITEM_SLOT_NONE;
    }

    wornOnly = (GetINIInt(file, section, "wornonly") == 1);

    String sound = GetINIString(file, section, "sound").toLower();
    if (sound.equals("medium")) {
        this->sound = ITEMPICK_SOUND::MEDIUM;
    }
    else if (sound.equals("large")) {
        this->sound = ITEMPICK_SOUND::LARGE;
    }
    else if (sound.equals("small")) {
        this->sound = ITEMPICK_SOUND::SMALL;
    }
    else {
        this->sound = ITEMPICK_SOUND::PAPER;
    }

    //Start loading the assets needed.

    //Does another item already use that model?
    obj = nullptr;
    for (int i = 0; i < ItemTemplate::getListSize(); i++) {
        ItemTemplate* it2 = ItemTemplate::getObject(i);

        if (it2->objPath.equals(this->objPath) && it2->obj != nullptr) {
            obj = bbCopyMeshModelEntity(it2->obj);
            break;
        }
    }

    //Otherwise load the model.
    if (obj == nullptr) {
        if (GetINIInt(file, section, "animated") == 1) {
            obj = bbLoadAnimMesh(objPath);
        }
        else {
            obj = bbLoadMesh(objPath);
        }
    }

    tex = nullptr;
    if (!texPath.isEmpty()) {
        for (int i = 0; i < ItemTemplate::getListSize(); i++) {
            ItemTemplate* it2 = ItemTemplate::getObject(i);

            if (it2->texPath.equals(texPath) && it2->tex != nullptr) {
                tex = it2->tex;
                break;
            }
        }

        if (tex == nullptr) {
            int flags = GetINIInt(file, section, "textureflags", 1 + 8);
            tex = bbLoadTexture(texPath, flags);
        }

        bbEntityTexture(obj, tex);
    }

    for (int i = 0; i < 2; i++) {
        if (!invImagePath[i].isEmpty()) {
            for (int j = 0; j < ItemTemplate::getListSize(); j++) {
                ItemTemplate* it2 = ItemTemplate::getObject(j);

                if (it2->invImagePath[i].equals(invImagePath[i]) && it2->invImage[i] != 0) {
                    invImage[i] = it2->invImage[i];
                    break;
                }
            }

            if (invImage[i] == 0) {
                invImage[i] = bbLoadImage(invImagePath[i]);
                bbMaskImage(invImage[i], 255, 0, 255);
            }
        }
    }

    float scale = GetINIFloat(file, section, "scale", 1.f);
    this->scale = scale;
    bbScaleEntity(this->obj, this->scale * RoomScale, this->scale * RoomScale, this->scale * RoomScale, true);

    bbHideEntity(this->obj);

    list.push_back(this);
}
ItemTemplate::~ItemTemplate() {
    if (obj != nullptr) {
        bbFreeEntity(obj);
    }
    for (int i = 0; i < 2; i++) {
        if (invImage[i] != nullptr) {
            bbFreeImage(invImage[i]);
        }
    }
    if (tex != nullptr) {
        bbFreeTexture(tex);
    }
}
int ItemTemplate::getListSize() {
    return list.size();
}
ItemTemplate* ItemTemplate::getObject(int index) {
    return list[index];
}

void ItemTemplate::LoadTemplates(const String& file) {
    bbFile* f = bbOpenFile(file);
    String section;

    while (!bbEof(f)) {
        section = bbReadLine(f).trim();
        if (section.charAt(0) == '[') {
            section = bbMid(section, 2, section.size() - 2);

            new ItemTemplate(file, section);
        }
    }

    bbCloseFile(f);
}

void ItemTemplate::DeloadTemplates() {
    for (int i = 0; i < getListSize(); i++) {
        ItemTemplate* it = getObject(i);
        delete it;
    }
    list.clear();
}

std::vector<Item*> Item::list;
Item::Item() {
    memset(this,0,sizeof(Item));

    list.push_back(this);
}
Item::~Item() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Item::getListSize() {
    return list.size();
}
Item* Item::getObject(int index) {
    return list[index];
}

std::vector<Inventory*> Inventory::list;
Inventory::Inventory() {
    for (int i=0;i<MAX_ITEM_COUNT;i++) {
        items[i]=nullptr;
    }

    list.push_back(this);
}
Inventory::~Inventory() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Inventory::getListSize() {
    return list.size();
}
Inventory* Inventory::getObject(int index) {
    return list[index];
}

// Globals.
int LastItemID;
int itemDistanceTimer = 0;

Inventory* CreateInventory(int size) {
    Inventory* inv = new Inventory();
    inv->size = size;
    return inv;
}

void DeleteInventory(Inventory* inv) {
    int i;
    for (i = 0; i <= MAX_ITEM_COUNT-1; i++) {
        if (inv->items[i]!=nullptr) {
            RemoveItem(inv->items[i]);
        }
    }
    delete inv;
}

int CountItemsInInventory(Inventory* inv) {
    int retVal = 0;
    int i;
    for (i = 0; i <= inv->size-1; i++) {
        if (inv->items[i]!=nullptr) {
            retVal = retVal+1;
        }
    }
    return retVal;
}

Item* CreateItem(const String& name, float x, float y, float z, int invSlots) {
    Item* i = new Item();
    ItemTemplate* it;

    for (int iterator105 = 0; iterator105 < ItemTemplate::getListSize(); iterator105++) {
        it = ItemTemplate::getObject(iterator105);

        if (it->name.equals(name)) {
            i->itemTemplate = it;
            i->collider = bbCreatePivot();
            bbEntityRadius(i->collider, 0.01f);
            bbEntityPickMode(i->collider, 1, false);
            i->model = bbCopyMeshModelEntity(it->obj, i->collider);
            i->name = it->invName;
            bbShowEntity(i->collider);
            bbShowEntity(i->model);

            break;
        }
    }

    if (i->itemTemplate == nullptr) {
        throw ("Item template not found ("+name+")");
    }

    bbResetEntity(i->collider);
    bbPositionEntity(i->collider, x, y, z, true);
    bbRotateEntity(i->collider, 0, bbRand(360), 0);
    i->dropSpeed = 0.f;

    //TODO: Re-implement.
    //	If (tempname="clipboard") And (invSlots=0) Then
    //		invSlots = 20
    //		SetAnimTime(i\model, 17.f)
    //		i\invimg = i\template\invimg2 ;<-- this Future Mark.
    //	EndIf

    i->subInventory = nullptr;
    if (invSlots>0) {
        i->subInventory = CreateInventory(invSlots);
    }

    i->id = LastItemID+1;
    LastItemID = i->id;

    bbEntityType(i->collider, HIT_ITEM);
    return i;
}

Item* CreatePaper(const String& name, float x, float y, float z) {
    Item* i = CreateItem("paper", x, y, z, 0);
    i->name = GetINIString("Data/Items/paper.ini", name, "name");

    //Load the document image.
    String imgPath = GetImagePath("GFX/Items/Paper/Documents/" + name);
    if (bbFileType(imgPath) != 1) {
        imgPath = GetImagePath("GFX/Items/Paper/Notes/" + name);
    }

    i->img = bbLoadImage(imgPath);
    bbMaskImage(i->img, 255, 255, 0);

    //Make a resized copy to texture the model with.
    int texDim = 256;
    bbImage* img = bbCopyImage(i->img);
    img = ResizeImage2(img, texDim, texDim);

    Texture* tex = bbCreateTexture(texDim, texDim, 1+8);
    bbCopyRect(0, 0, texDim, texDim, 0, 0, bbImageBuffer(img), bbTextureBuffer(tex));
    bbEntityTexture(i->model, tex);
    bbFreeImage(img);
    bbFreeTexture(tex);

    return i;
}

void RemoveItem(Item* i) {
    if (i->subInventory!=nullptr) {
        DeleteInventory(i->subInventory);
    }

    if (i->img != 0) {
        bbFreeImage(i->img);
    }

    bbFreeEntity(i->model);
    bbFreeEntity(i->collider);
    i->collider = 0;

    delete i;
}

void UpdateItems() {
    float hideDist = HideDistance*0.5f;
    bool deletedItem = false;

    mainPlayer->closestItem = nullptr;
    for (int i = 0; i < Item::getListSize(); i++) {
        Item* item = Item::getObject(i);

        item->dropped = 0;

        if (!item->picked) {
            if (itemDistanceTimer < TimeInPosMilliSecs()) {
                item->dist = bbEntityDistance(mainPlayer->collider, item->collider);
            }

            if (item->dist < hideDist) {
                bbShowEntity(item->collider);

                if (item->dist < 1.2f) {
                    if (mainPlayer->closestItem == nullptr) {
                        if (bbEntityInView(item->model, mainPlayer->cam)) {
                            mainPlayer->closestItem = item;
                        }
                    } else if (item->dist < bbEntityDistance(mainPlayer->collider, mainPlayer->closestItem->collider)) {
                        if (bbEntityInView(item->model, mainPlayer->cam)) {
                            mainPlayer->closestItem = item;
                        }
                    }
                }

                if (bbEntityCollided(item->collider, HIT_MAP)) {
                    item->dropSpeed = 0;
                    item->xspeed = 0.f;
                    item->zspeed = 0.f;
                } else {
                    item->dropSpeed = item->dropSpeed - 0.0004f * timing->tickDuration;
                    bbTranslateEntity(item->collider, item->xspeed*timing->tickDuration, item->dropSpeed * timing->tickDuration, item->zspeed*timing->tickDuration);
                    if (item->wontColl) {
                        bbResetEntity(item->collider);
                    }
                }

                if (item->dist < hideDist*0.2f) {
                    for (int j = 0; j < Item::getListSize(); j++) {
                        Item* collItem = Item::getObject(j);

                        if (item != collItem && !collItem->picked && collItem->dist < hideDist*0.2f) {

                            float xtemp = bbEntityX(collItem->collider,true)-bbEntityX(item->collider,true);
                            float ytemp = bbEntityY(collItem->collider,true)-bbEntityY(item->collider,true);
                            float ztemp = bbEntityZ(collItem->collider,true)-bbEntityZ(item->collider,true);

                            float ed = (xtemp*xtemp+ztemp*ztemp);
                            if (ed < 0.07f && abs(ytemp) < 0.25f) {
                                //items are too close together, push away

                                xtemp = xtemp*(0.07f-ed);
                                ztemp = ztemp*(0.07f-ed);

                                while (abs(xtemp)+abs(ztemp)<0.001f) {
                                    xtemp = xtemp+bbRnd(-0.002f,0.002f);
                                    ztemp = ztemp+bbRnd(-0.002f,0.002f);
                                }

                                bbTranslateEntity(collItem->collider,xtemp,0,ztemp);
                                bbTranslateEntity(item->collider,-xtemp,0,-ztemp);
                            }
                        }
                    }
                }

                if (bbEntityY(item->collider) < - 35.0f) {
                    std::cout << "remove: " + item->name << "\n";
                    RemoveItem(item);
                }
            } else {
                bbHideEntity(item->collider);
            }
        }
    }

    bool canSeePlayer = true;
    if (mainPlayer->closestItem != nullptr) {
        //Can the player see this? (TODO: fix)
        //canSeePlayer = EntityVisible(mainPlayer\closestItem\collider,mainPlayer\cam)
        //If (Not canSeePlayer) Then canSeePlayer = EntityVisible(mainPlayer\closestItem\collider,mainPlayer\collider)
        if (canSeePlayer) {
            if (MouseHit1) {
                PickItem(mainPlayer->closestItem);
            }
        }
    }

    if (itemDistanceTimer < TimeInPosMilliSecs()) {
        itemDistanceTimer = TimeInPosMilliSecs() + 800;
    }
}

// TODO: Make all of these instance methods.
void PickItem(Item* item) {
    if (item->itemTemplate->name.equals("battery")) {
        if (HasTag(item, ITEM_TAG_914VF)) {
            bbShowEntity(mainPlayer->overlays[OVERLAY_WHITE]);
            mainPlayer->lightFlash = 1.f;
            //TODO: Light
            //PlaySound2(IntroSFX(11))
            DeathMSG = "Subject D-9341 found dead inside SCP-914's output booth next to what appears to be an ordinary nine-volt battery. The subject is covered in severe ";
            DeathMSG = DeathMSG + "electrical burns, and assumed to be killed via an electrical shock caused by the battery. The battery has been stored for further study.";
            Kill(mainPlayer);

            return;
        }
    } else if (item->itemTemplate->name.equals("vest")) {
        if (HasTag(item, ITEM_TAG_914VF)) {
            Msg = "The vest is too heavy to pick up.";
            MsgTimer = 70*6;

            return;
        }
    }

    if (SpaceInInventory(mainPlayer)) {
        for (int n = WORNITEM_SLOT_COUNT; n <= mainPlayer->inventory->size - 1; n++) {
            if (mainPlayer->inventory->items[n] == nullptr) {
                PlaySound_SM(sndManager->itemPick[(int)item->itemTemplate->sound]);
                item->picked = true;
                item->dropped = -1;

                mainPlayer->inventory->items[n] = item;
                bbHideEntity(item->collider);
                break;
            }
        }
    } else {
        Msg = "You cannot carry any more items.";
        MsgTimer = 70 * 5;
    }
}

void DropItem(Item* item, Inventory* inv) {
    int i;
    for (i = 0; i <= inv->size-1; i++) {
        if (inv->items[i] == item) {
            inv->items[i] = nullptr;
        }
    }

    PlaySound_SM(sndManager->itemPick[(int)item->itemTemplate->sound]);

    item->dropped = 1;

    bbShowEntity(item->collider);
    bbPositionEntity(item->collider, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
    bbRotateEntity(item->collider, bbEntityPitch(mainPlayer->cam), bbEntityYaw(mainPlayer->cam)+bbRnd(-20,20), 0);
    bbMoveEntity(item->collider, 0, -0.1f, 0.1f);
    bbRotateEntity(item->collider, 0, bbEntityYaw(mainPlayer->cam)+bbRnd(-110,110), 0);

    bbResetEntity(item->collider);

    item->picked = false;
}

void AssignTag(Item* item, const String& tag) {
    if (HasTag(item, tag)) {
        return;
    }

    int space = false;
    int i;
    for (i = 0; i <= 4; i++) {
        if (item->tags[i].isEmpty()) {
            space = true;
            item->tags[i] = tag;
            return;
        }
    }

    if (!space) {
        throw ("Assigned tag without space: " + item->name + ", tag: " + tag);
    }
}

void RemoveTag(Item* item, const String& tag) {
    int found = false;
    int i;
    for (i = 0; i <= 4; i++) {
        if (item->tags[i].equals(tag)) {
            found = true;
            item->tags[i] = "";
            return;
        }
    }

    if (!found) {
        throw ("Removed non-existant tag: " + item->name + ", tag: " + tag);
    }
}

int HasTag(Item* item, const String& tag) {
    int i;
    for (i = 0; i <= 4; i++) {
        if (item->tags[i].equals(tag)) {
            return true;
        }
    }

    return false;
}

int IsPlayerWearingItem(Player* player, const String& itemName) {
    for (int i = 0; i <= WORNITEM_SLOT_COUNT-1; i++) {
        if (player->inventory->items[i] != nullptr) {
            if (player->inventory->items[i]->itemTemplate->name.equals(itemName)) {
                return true;
            }
        }
    }

    return false;
}

void UseItem(Inventory* inv, int index) {
    Item* item = inv->items[index];
    PlaySound_SM(sndManager->itemPick[(int)item->itemTemplate->sound]);

    if (item->itemTemplate->wornSlot != WORNITEM_SLOT_NONE) {
        //If the equip slot is already filled then swap the items.
        inv->items[index] = mainPlayer->inventory->items[item->itemTemplate->wornSlot];
        mainPlayer->inventory->items[item->itemTemplate->wornSlot] = item;

        return;
    }

    //TODO: Non-equippable items here.
}

void DeEquipItem(Item* item) {
    DropItem(item, mainPlayer->openInventory);

    //Check if this item can be put back into the inventory.
    if (!item->itemTemplate->wornOnly) {
        PickItem(item);
    }
}

void UpdateInventory(Player* player) {
    //TODO: cleanup
    int prevInvOpen = (CurrGameState==GAMESTATE_INVENTORY);
    int mouseSlot = 66;
    int mouseOnWornItemSlot = false;

    Item* item;

    int x;
    int y;
    int isMouseOn;
    int i;

    int slotIndex;
    if (CurrGameState == GAMESTATE_INVENTORY) {
        mainPlayer->selectedDoor = nullptr;

        x = userOptions->screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2;
        y = userOptions->screenHeight / 2 - (ITEM_CELL_SIZE + ITEM_CELL_SPACING) * (player->openInventory->size / ITEMS_PER_ROW) / 2 + ITEM_CELL_SPACING/2;

        for (slotIndex = 0; slotIndex <= player->openInventory->size - 1; slotIndex++) {
            isMouseOn = false;
            if (bbMouseX() > x && bbMouseX() < x + ITEM_CELL_SIZE) {
                if (bbMouseY() > y && bbMouseY() < y + ITEM_CELL_SIZE) {
                    isMouseOn = true;
                }
            }

            if (isMouseOn) {
                mouseSlot = slotIndex;
                if (slotIndex < WORNITEM_SLOT_COUNT) {
                    mouseOnWornItemSlot = true;
                }

                if (MouseHit1 && player->openInventory->items[slotIndex] != nullptr) {
                    //Selecting an item.
                    if (player->selectedItem == nullptr) {
                        player->selectedItem = player->openInventory->items[slotIndex];
                    }

                    MouseHit1 = false;
                    if (DoubleClick) {
                        if (mouseOnWornItemSlot) {
                            DeEquipItem(player->openInventory->items[slotIndex]);
                        } else {
                            //Using the item.
                            UseItem(player->openInventory, slotIndex);
                        }

                        player->selectedItem = nullptr;
                        DoubleClick = false;
                    }
                } else if ((MouseUp1 && player->selectedItem != nullptr)) {
                    //Item already selected and mouse release.

                    //Hovering over empty slot. Move the item to the empty slot.
                    if (player->openInventory->items[slotIndex] == nullptr) {
                        //If the empty slot is an equip slot then check if the slots match.
                        if (mouseOnWornItemSlot) {
                            if (slotIndex != player->selectedItem->itemTemplate->wornSlot) {
                                player->selectedItem = nullptr;
                                return;
                            } else {
                                PlaySound_SM(sndManager->itemPick[(int)player->selectedItem->itemTemplate->sound]);
                            }
                        }

                        //Remove the item from its previous slot.
                        for (i = 0; i <= player->openInventory->size - 1; i++) {
                            if (player->openInventory->items[i] == player->selectedItem) {
                                if (i < WORNITEM_SLOT_COUNT) {
                                    PlaySound_SM(sndManager->itemPick[(int)player->selectedItem->itemTemplate->sound]);
                                }
                                player->openInventory->items[i] = nullptr;
                                break;
                            }
                        }

                        player->openInventory->items[slotIndex] = player->selectedItem;
                        player->selectedItem = nullptr;
                    } else if ((player->openInventory->items[slotIndex] != player->selectedItem)) {
                        //Hovering over another item. Attempt to combine the items.
                        //CombineItems(player\selectedItem, player\openInventory\items[slotIndex])
                    } else {
                        //Hovering over the item's slot. Stop selecting the item.
                        player->selectedItem = nullptr;
                    }
                }

                //If the mouse was hovering over this slot then don't bother iterating through the rest of the inventory.
                break;
            }

            //Move x and y coords to point to next item.
            x = x + ITEM_CELL_SIZE + ITEM_CELL_SPACING;
            if (slotIndex % ITEMS_PER_ROW == ITEMS_PER_ROW-1) {
                y = y + ITEM_CELL_SIZE + ITEM_CELL_SPACING;
                x = userOptions->screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2;
            }
        }

        if (MouseUp1 && player->selectedItem != nullptr) {
            //Mouse release outside a slot, drop the item.
            if (mouseSlot == 66) {
                DropItem(player->selectedItem, player->openInventory);
                player->selectedItem = nullptr;
            }
        }

        //Update any items that are used outside the inventory (firstaid for example).
    } else {
        if (player->selectedItem != nullptr) {
            if (MouseHit2) {
                //TODO: Move to de-equip function.
                bbEntityAlpha(player->overlays[OVERLAY_BLACK], 0.f);

                PlaySound_SM(sndManager->itemPick[(int)player->selectedItem->itemTemplate->sound]);
                player->selectedItem = nullptr;
            }
        }
    }

    if (prevInvOpen && CurrGameState != GAMESTATE_INVENTORY) {
        bbMoveMouse(viewport_center_x, viewport_center_y);
    }
}

void DrawInventory(Player* player) {
    int MouseSlot = 66;

    int isMouseOn;

    String strtemp;

    int x;
    int y;
    int i;

    int n;

    Camera* tempCamera;
    Light* tempLight;
    Object* tempObj;

    if (CurrGameState == GAMESTATE_INVENTORY) {
        x = userOptions->screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2;
        y = userOptions->screenHeight / 2 - (ITEM_CELL_SIZE + ITEM_CELL_SPACING) * (player->openInventory->size / ITEMS_PER_ROW) / 2 + ITEM_CELL_SPACING/2;

        for (n = 0; n <= player->openInventory->size - 1; n++) {
            isMouseOn = false;
            if (bbMouseX() > x && bbMouseX() < x + ITEM_CELL_SIZE) {
                if (bbMouseY() > y && bbMouseY() < y + ITEM_CELL_SIZE) {
                    isMouseOn = true;
                }
            }

            if (isMouseOn) {
                MouseSlot = n;
                bbColor(255, 0, 0);
                bbRect(x - 1, y - 1, ITEM_CELL_SIZE + 2, ITEM_CELL_SIZE + 2);
            }

            bbColor(255, 255, 255);
            DrawFrame(x, y, ITEM_CELL_SIZE, ITEM_CELL_SIZE, (x % 64), (x % 64));

            if (player->openInventory->items[n] != nullptr) {
                //TODO: Re-implement.
                //				Color(200, 200, 200)
                //				If (IsPlayerWearingItem(player,player\openInventory\items[n])) Then
                //					Rect(x - 3, y - 3, ITEM_CELL_SIZE + 6, ITEM_CELL_SIZE + 6)
                //				EndIf
                bbColor(255, 255, 255);

                //Render icon.
                if (player->openInventory->items[n]->invImage == 0) {
                    player->openInventory->items[n]->invImage = bbCreateImage(64,64);
                    tempCamera = bbCreateCamera();
                    tempObj = player->openInventory->items[n]->collider;
                    bbCameraZoom(tempCamera,1.2f);
                    tempLight = bbCreateLight(1);
                    bbAmbientLight(40,40,40);

                    bbRotateEntity(tempObj,0,0,0,true);

                    bbCameraRange(tempCamera,0.01f,512.f*RoomScale);
                    bbCameraViewport(tempCamera,0,0,64,64);
                    bbCameraClsColor(tempCamera,255,0,255);
                    bbPositionEntity(tempCamera,10000.f+10.f*RoomScale,10000.f+70.f*RoomScale,10000.f+20.f*RoomScale,true);
                    bbPositionEntity(tempLight,10000.f,10000.f+20.f*RoomScale,10000.f,true);
                    bbShowEntity(tempObj);
                    bbPositionEntity(tempObj,10000.f,10000.f,10000.f,true);
                    bbPointEntity(tempCamera,tempObj);
                    bbPointEntity(tempLight,tempObj);
                    bbPositionEntity(tempObj,10000.f,10000.f+12.f*RoomScale,10000.f,true);
                    bbHideEntity(mainPlayer->cam);

                    bbSetBuffer(bbBackBuffer());
                    bbRenderWorld();
                    bbCopyRect(0,0,64,64,0,0,bbBackBuffer(),bbImageBuffer(player->openInventory->items[n]->invImage));
                    bbMaskImage(player->openInventory->items[n]->invImage,255,0,255);

                    bbHideEntity(tempObj);
                    bbShowEntity(mainPlayer->cam);
                    bbFreeEntity(tempCamera);
                    bbFreeEntity(tempLight);
                    bbAmbientLight(Brightness, Brightness, Brightness);
                }

                if (player->selectedItem != player->openInventory->items[n] | isMouseOn) {
                    bbDrawImage(player->openInventory->items[n]->invImage, x + ITEM_CELL_SIZE / 2 - 32, y + ITEM_CELL_SIZE / 2 - 32);
                }
            }

            if (player->openInventory->items[n] != nullptr && player->selectedItem != player->openInventory->items[n]) {
                if (isMouseOn) {
                    if (player->selectedItem == nullptr) {
                        bbSetFont(uiAssets->font[0]);
                        bbColor(0,0,0);
                        bbText(x + ITEM_CELL_SIZE / 2 + 1, y + ITEM_CELL_SIZE + ITEM_CELL_SPACING - 15 + 1, player->openInventory->items[n]->name, true);
                        bbColor(255, 255, 255);
                        bbText(x + ITEM_CELL_SIZE / 2, y + ITEM_CELL_SIZE + ITEM_CELL_SPACING - 15, player->openInventory->items[n]->name, true);
                    }
                }
            }

            x = x + ITEM_CELL_SIZE + ITEM_CELL_SPACING;
            if (n % ITEMS_PER_ROW == ITEMS_PER_ROW-1) {
                y = y + ITEM_CELL_SIZE + ITEM_CELL_SPACING;
                x = userOptions->screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2;
            }
        }

        //Only re-draw the item under the cursor once it has left the item's original slot.
        if (player->selectedItem != nullptr) {
            if (MouseDown1) {
                //TODO: Short-circuit eval in C.
                if (MouseSlot == 66) {
                    bbDrawImage(player->selectedItem->invImage, bbMouseX() - bbImageWidth(player->selectedItem->invImage) / 2, bbMouseY() - bbImageHeight(player->selectedItem->invImage) / 2);
                } else if ((player->selectedItem != player->openInventory->items[MouseSlot])) {
                    bbDrawImage(player->selectedItem->invImage, bbMouseX() - bbImageWidth(player->selectedItem->invImage) / 2, bbMouseY() - bbImageHeight(player->selectedItem->invImage) / 2);
                }
            }
        }
    } else {
        if (player->selectedItem != nullptr) {
            //TODO: Draw firstaid, nav, radio, docs.
        }
    }
}

void ToggleInventory(Player* player) {
    if (CurrGameState == GAMESTATE_INVENTORY) {
        if (mainPlayer->openInventory == mainPlayer->inventory) {
            CurrGameState = GAMESTATE_PLAYING;
            ResumeSounds();
            bbMouseXSpeed();
            bbMouseYSpeed();
            bbMouseZSpeed();
            mouse_x_speed_1 = 0.f;
            mouse_y_speed_1 = 0.f;
        } else {
            mainPlayer->openInventory = mainPlayer->inventory;
        }
    } else {
        CurrGameState = GAMESTATE_INVENTORY;
        mainPlayer->openInventory = mainPlayer->inventory;
        PauseSounds();
    }

    mainPlayer->selectedItem = nullptr;
}

}

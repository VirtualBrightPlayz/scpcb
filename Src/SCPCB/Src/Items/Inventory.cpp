#include <bbinput.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>

#include "Inventory.h"
#include "Item.h"
#include "../GameMain.h"
#include "../Config/Options.h"
#include "../AssetMgmt/Assets.h"
#include "../AssetMgmt/Audio.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../Player/Player.h"
#include "../Menus/Menu.h"
#include "../Map/MapSystem.h"

namespace CBN {

ItemCell::ItemCell(int size) {
    hover = false;
    val = nullptr;
    this->size = size;
}

ItemCell::~ItemCell() {
    if (val != nullptr) {
        delete val;
    }
}

bool ItemCell::isHovering() {
    return hover;
}

bool ItemCell::isEmpty() {
    return val == nullptr;
}

bool ItemCell::contains(Item* it) {
    if (it == nullptr) {
        return false;
    }

    return val == it;
}

Item* ItemCell::getItem() {
    if (isEmpty()) {
        return nullptr;
    }
    return val;
}

void ItemCell::insertItem(Item* it) {
    val = it;
    it->setVisibility(false);
}

void ItemCell::removeItem() {
    val->setVisibility(true);
    val = nullptr;
}

void ItemCell::update(int x, int y) {
    if (bbMouseX() > x && bbMouseX() < x + size) {
        if (bbMouseY() > y && bbMouseY() < y + size) {
            hover = true;
            return;
        }
    }
    hover = false;
}

void ItemCell::draw(int x, int y, int cellSpacing) {
    if (hover) {
        bbColor(255, 0, 0);
        bbRect(x - 1, y - 1, size + 2, size + 2);
        bbColor(255, 255, 255);

        if (mainPlayer->selectedItem == nullptr && val != nullptr) {
            bbSetFont(uiAssets->font[0]);
            bbColor(0,0,0);
            bbText(x + size / 2 + 1, y + size + cellSpacing - 15 + 1, val->getInvName(), true);
            bbColor(255, 255, 255);
            bbText(x + size / 2, y + size + cellSpacing - 15, val->getInvName(), true);
        }
    }
    DrawFrame(x, y, size, size, (x % 64), (x % 64));

    if (val != nullptr) {
        // Render icon.
        val->generateInvImg();

        if (mainPlayer->selectedItem != val || hover) {
            int offset = (int)(43 * MenuScale);
            bbDrawImage(val->invImg, x + size / 2 - offset, y + size / 2 - offset);
        }
    }
}

Inventory::Inventory(int size, int itemsPerRow) {
    items = new ItemCell[size];
    equipSlots = new ItemCell[WORNITEM_SLOT_COUNT];
    this->size = size;

    this->itemsPerRow = itemsPerRow;
    spacing = 35;
    xOffset = 0;
    yOffset = 0;
}

Inventory::~Inventory() {
    if (items != nullptr) {
        delete[] items;
    }
    if (equipSlots != nullptr) {
        delete[] equipSlots;
    }
}

int Inventory::getSize() const {
    return size;
}

bool Inventory::anyRoom() const {
    for (int i = 0; i < size; i++) {
        if (items[i].isEmpty()) {
            return true;
        }
    }
    return false;
}

Item* Inventory::getItem(int index) {
    if (index < 0 || index >= size) {
        throw ("Inventory::getItem() out of range!");
    }

    if (items[index].isEmpty()) {
        return nullptr;
    }
    return items[index].getItem();
}

int Inventory::getIndex(Item* it) const {
    for (int i = 0; i < size; i++) {
        if (items[i].contains(it)) {
            return i;
        }
    }
    return -1;
}

bool Inventory::isEquipped(Item* it) const {
    if (it->wornSlot == WornItemSlot::None) {
        return false;
    }

    return equipSlots[(int)it->wornSlot].contains(it);
}

Item* Inventory::getEquippedItem(int index) {
    if (index < 0 || index >= WORNITEM_SLOT_COUNT || equipSlots[index].isEmpty()) {
        return nullptr;
    }

    return equipSlots[index].getItem();
}

void Inventory::addItem(Item* it) {
    for (int i = 0; i < size; i++) {
        if (items[i].isEmpty()) {
            setItem(it, i);
            return;
        }
    }

    // This shouldn't be called without there being space available.
    throw ("No space in inventory.");
}

void Inventory::setItem(Item* it, int slot) {
    if (slot >= size || slot < 0) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String(size) + "\" are available.");
    }
    items[slot].insertItem(it);
}

void Inventory::moveItem(Item* it, enum class WornItemSlot slot) {
    // Don't do anything if the item doesn't have an equip slot.
    if (it->wornSlot == WornItemSlot::None || it->wornSlot != slot) {
        return;
    }

    // Equipping an item?
    if (equipSlots[(int)slot].isEmpty()) {
        // Get item's inventory index.
        int index = getIndex(it);

        // Remove it from this slot.
        items[index].removeItem();

        // Put it in the equip slot.
        equipSlots[(int)slot].insertItem(it);
    }
    else if (isEquipped(it)) {
        // De-equipping an item?

        // Unequip it.
        equipSlots[(int)slot].removeItem();

        // Add it to the inventory.
        addItem(it);
    }
}

void Inventory::moveItem(Item* it, int destIndex) {
    if (isEquipped(it)) {
        // Unequip it.
        equipSlots[(int)it->wornSlot].removeItem();
    } else {
        // Get item's inventory index.
        int index = getIndex(it);

        // Remove it from this slot.
        items[index].removeItem();
    }

    // Put it in the other slot.
    items[destIndex].insertItem(it);
}

void Inventory::useItem(Item* it) {
    if (it->wornSlot != WornItemSlot::None) {
        if (!(equipSlots[(int)it->wornSlot].contains(it) || equipSlots[(int)it->wornSlot].isEmpty())) {
            txtMgmt->setMsg(txtMgmt->lang["inv_alreadyequip"]);
            return;
        }

        // If this item is an equippable then equip/unequip it.
        moveItem(it, it->wornSlot);
    }

    PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
    it->onUse();
}

void Inventory::dropItem(Item* it) {
    if (it->wornSlot != WornItemSlot::None && equipSlots[(int)it->wornSlot].contains(it)) {
        equipSlots[(int)it->wornSlot].removeItem();
        it->onUse(); // Has the de-equip message.
    }
    else {
        items[getIndex(it)].removeItem();
    }

    PlaySound_SM(sndMgmt->itemPick[(int)it->pickSound]);
}

int Inventory::getInvStartX() {
    return userOptions->screenWidth / 2 - (ItemCell::SIZE * itemsPerRow + spacing * (itemsPerRow - 1) / 2) + (int)(xOffset * MenuScale);
}

int Inventory::getInvStartY() {
    return userOptions->screenHeight / 2 - ((ItemCell::SIZE + spacing) * (size / itemsPerRow) / 2 + spacing / 2) + (int)(yOffset * MenuScale);
}

void Inventory::nextInvSlotPosition(int& cellX, int& cellY, int currCellIndex) {
    // Move x and y coords to point to next item.
    if (currCellIndex % itemsPerRow == itemsPerRow - 1) {
        cellY += ItemCell::SIZE + spacing;
        cellX = getInvStartX();
    }
    else {
        cellX += ItemCell::SIZE + spacing;
    }
}

void Inventory::updateMainInv() {
    int cellX = getInvStartX();
    int cellY = getInvStartY();

    for (int i = 0; i < size; i++) {
        items[i].update(cellX, cellY);

        if (items[i].isHovering()) {
            if (MouseHit1 && !items[i].isEmpty()) {
                // Selecting an item.
                if (mainPlayer->selectedItem == nullptr) {
                    mainPlayer->selectedItem = items[i].getItem();
                }

                MouseHit1 = false;
                if (DoubleClick) {
                    // Using the item.
                    useItem(mainPlayer->selectedItem);

                    mainPlayer->selectedItem = nullptr;
                    DoubleClick = false;
                }
            } else if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
                // Item already selected and mouse release.

                // Hovering over empty slot. Move the item to the empty slot.
                if (items[i].isEmpty()) {
                    moveItem(mainPlayer->selectedItem, i);
                } else if (items[i].getItem() != mainPlayer->selectedItem) {
                    // Hovering over another item. Attempt to combine the items.
                    items[i].getItem()->combineWith(mainPlayer->selectedItem);
                }
                // Otherwise hovering over the item's own slot.

                mainPlayer->selectedItem = nullptr;
            }

            // If the mouse was hovering over this slot then don't bother iterating through the rest of the inventory.
            break;
        }

        nextInvSlotPosition(cellX, cellY, i);
    }

    // if the mouse was released outside a slot, drop the item.
    if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
        dropItem(mainPlayer->selectedItem);
        mainPlayer->selectedItem = nullptr;
    }

        //Update any items that are used outside the inventory (firstaid for example).
    // TODO:
    // } else {
    //     if (player->selectedItem != nullptr) {
    //         if (MouseHit2) {
    //             //TODO: Move to de-equip function.
    //             bbEntityAlpha(player->overlays[OVERLAY_BLACK], 0.f);

    //             PlaySound_SM(sndMgmt->itemPick[(int)player->selectedItem->itemTemplate->sound]);
    //             player->selectedItem = nullptr;
    //         }
    //     }
    // }
}

int Inventory::getEquipStartX() {
    return userOptions->screenWidth / 2 - (ItemCell::SIZE + equipSlotSpacing * (WORNITEM_SLOT_COUNT - 1) / 2) + (int)(equipXOffset * MenuScale);
}

int Inventory::getEquipStartY() {
    return userOptions->screenHeight / 2 - ((ItemCell::SIZE + spacing) / 2 + equipSlotSpacing / 2) + (int)(equipYOffset * MenuScale);
}

void Inventory::setEquipSlotPosition(int x, int y, int spacing) {
    equipXOffset = x;
    equipYOffset = y;
    equipSlotSpacing = spacing;
}

void Inventory::updateEquipInv() {
    // TODO:
}

void Inventory::update() {
    updateMainInv();
    updateEquipInv();
}

// TODO: Render equip slots.
void Inventory::draw() {
    int cellX = getInvStartX();
    int cellY = getInvStartY();

    for (int i = 0; i < size; i++) {
        items[i].draw(cellX, cellY, spacing);

        nextInvSlotPosition(cellX, cellY, i);
    }

    // Draw the selected item under the cursor when it's not over the item's original slot.
    if (mainPlayer->selectedItem != nullptr) {
        Item* item = mainPlayer->selectedItem;
        bool hoveringOverItemsOwnSlot = false;

        if (item->wornSlot != WornItemSlot::None && equipSlots[(int)item->wornSlot].contains(item)) {
            hoveringOverItemsOwnSlot = true;
        }
        else {
            int index = getIndex(item);
            hoveringOverItemsOwnSlot = items[index].isHovering();
        }

        if (!hoveringOverItemsOwnSlot) {
            bbDrawImage(item->invImg, bbMouseX() - bbImageWidth(item->invImg) / 2, bbMouseY() - bbImageHeight(item->invImg) / 2);
        }
    }
}

}
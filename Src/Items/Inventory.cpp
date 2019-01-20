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

void ItemCell::update(int x, int y, Inventory* const inv) {
    if (bbMouseX() > x && bbMouseX() < x + size) {
        if (bbMouseY() > y && bbMouseY() < y + size) {
            hover = true;

            if (MouseHit1 && !isEmpty()) {
                // Selecting an item.
                if (mainPlayer->selectedItem == nullptr) {
                    mainPlayer->selectedItem = getItem();
                }

                MouseHit1 = false;
                if (DoubleClick) {
                    // Using the item.
                    inv->useItem(mainPlayer->selectedItem);

                    mainPlayer->selectedItem = nullptr;
                    DoubleClick = false;
                }
            }

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
    spacing = ItemCell::SIZE / 2;
    xOffset = 0;
    yOffset = 0;
    equipXOffset = 0;
    equipYOffset = 0;
    equipSlotSpacing = 0;
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
    it->onUse();
}

void Inventory::moveItem(Item* it, int destIndex) {
    if (isEquipped(it)) {
        // Unequip it.
        equipSlots[(int)it->wornSlot].removeItem();
        it->onUse();
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
            txtMgmt->setMsg(txtMgmt->getLocalTxt("inv_alreadyequip"));
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
    int length = ItemCell::SIZE * itemsPerRow + (spacing * (itemsPerRow - 1));
    return userOptions->screenWidth / 2 - (int)(length / 2) + (int)(xOffset * MenuScale);
}

int Inventory::getInvStartY() {
    int rowCount = size / itemsPerRow;
    int height = ItemCell::SIZE * rowCount + (spacing * (rowCount - 1));
    return userOptions->screenHeight / 2 - (int)(height / 2) + (int)(yOffset * MenuScale);
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

void Inventory::updateInvSlots() {
    int cellX = getInvStartX();
    int cellY = getInvStartY();

    for (int i = 0; i < size; i++) {
        items[i].update(cellX, cellY, this);

        if (items[i].isHovering()) {
            // Item already selected and mouse release.
            if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
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

void Inventory::drawInvSlots() {
    int cellX = getInvStartX();
    int cellY = getInvStartY();

    for (int i = 0; i < size; i++) {
        items[i].draw(cellX, cellY, spacing);

        nextInvSlotPosition(cellX, cellY, i);
    }
}

int Inventory::getEquipStartX() {
    int length = ItemCell::SIZE;
    return userOptions->screenWidth / 2 - (int)(length / 2) + (int)(equipXOffset * MenuScale);
}

int Inventory::getEquipStartY() {
    int height = ItemCell::SIZE * WORNITEM_SLOT_COUNT + (equipSlotSpacing * (WORNITEM_SLOT_COUNT - 1));
    return userOptions->screenHeight / 2 - (int)(height / 2) + (int)(equipYOffset * MenuScale);
}

void Inventory::nextEquipSlotPosition(int& cellX, int& cellY) {
    cellY += ItemCell::SIZE + equipSlotSpacing;
}

void Inventory::setEquipSlotPosition(int x, int y, int spacing) {
    equipXOffset = x;
    equipYOffset = y;
    equipSlotSpacing = spacing;
}

void Inventory::updateEquipSlots() {
    int cellX = getEquipStartX();
    int cellY = getEquipStartY();

    for (int i = 0; i < WORNITEM_SLOT_COUNT; i++) {
        equipSlots[i].update(cellX, cellY, this);

        if (equipSlots[i].isHovering()) {
            // Item already selected and mouse release.
            if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
                // Hovering over empty slot. Move the item to the empty slot.
                if (equipSlots[i].isEmpty()) {
                    moveItem(mainPlayer->selectedItem, (WornItemSlot)i);
                }
                else if (equipSlots[i].getItem() != mainPlayer->selectedItem) {
                    // Hovering over another item. Attempt to combine the items.
                    equipSlots[i].getItem()->combineWith(mainPlayer->selectedItem);
                }
                // Otherwise hovering over the item's own slot.

                mainPlayer->selectedItem = nullptr;
            }

            // If the mouse was hovering over this slot then don't bother iterating through the rest of the inventory.
            break;
        }

        nextEquipSlotPosition(cellX, cellY);
    }
}

void Inventory::drawEquipSlots() {
    int cellX = getEquipStartX();
    int cellY = getEquipStartY();

    for (int i = 0; i < WORNITEM_SLOT_COUNT; i++) {
        equipSlots[i].draw(cellX, cellY, equipSlotSpacing);

        nextEquipSlotPosition(cellX, cellY);
    }
}

void Inventory::update() {
    updateInvSlots();
    updateEquipSlots();

    // if the mouse was released outside a slot, drop the item.
    if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
        dropItem(mainPlayer->selectedItem);
        mainPlayer->selectedItem = nullptr;
    }
}

void Inventory::draw() {
    drawInvSlots();
    drawEquipSlots();

    // Draw the selected item under the cursor when it's not over the item's original slot.
    if (mainPlayer->selectedItem != nullptr) {
        Item* item = mainPlayer->selectedItem;
        bool hoveringOverItemsOwnSlot = false;

        if (item->wornSlot != WornItemSlot::None && equipSlots[(int)item->wornSlot].contains(item)) {
            hoveringOverItemsOwnSlot = equipSlots[(int)item->wornSlot].isHovering();
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
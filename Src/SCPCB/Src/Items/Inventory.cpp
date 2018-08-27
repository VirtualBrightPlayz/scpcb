#include <bbinput.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>

#include "Inventory.h"
#include "Item.h"
#include "../GameMain.h"
#include "../Config/Options.h"
#include "../AssetMgmt/Assets.h"
#include "../AssetMgmt/Audio.h"
#include "../Player/Player.h"
#include "../Menus/Menu.h"
#include "../Map/MapSystem.h"

namespace CBN {

ItemCell::ItemCell() {
    hover = false;
    val = nullptr;
}

ItemCell::~ItemCell() {
    if (val != nullptr) {
        delete val;
    }
}

void ItemCell::update(int x, int y) {
    if (bbMouseX() > x && bbMouseX() < x + ItemCell::SIZE) {
        if (bbMouseY() > y && bbMouseY() < y + ItemCell::SIZE) {
            hover = true;
        }
    }
}

void ItemCell::draw(int x, int y, int cellSpacing) {
    if (hover) {
        bbColor(255, 0, 0);
        bbRect(x - 1, y - 1, SIZE + 2, SIZE + 2);
        bbColor(255, 255, 255);

        if (mainPlayer->selectedItem == nullptr && val != nullptr) {
            bbSetFont(uiAssets->font[0]);
            bbColor(0,0,0);
            bbText(x + SIZE / 2 + 1, y + SIZE + cellSpacing - 15 + 1, val->getInvName(), true);
            bbColor(255, 255, 255);
            bbText(x + SIZE / 2, y + SIZE + cellSpacing - 15, val->getInvName(), true);
        }
    }
    DrawFrame(x, y, SIZE, SIZE, (x % 64), (x % 64));

    if (val != nullptr) {
        //Render icon.
        if (val->needsInvImg) {
            val->needsInvImg = false;
            val->invImg = bbCreateImage(64,64);
            Camera* tempCamera = bbCreateCamera();
            Pivot* tempObj = val->collider;
            bbCameraZoom(tempCamera,1.2f);
            Light* tempLight = bbCreateLight(1);
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
            bbCopyRect(0,0,64,64,0,0,bbBackBuffer(),bbImageBuffer(val->invImg));
            bbMaskImage(val->invImg,255,0,255);

            bbHideEntity(tempObj);
            bbShowEntity(mainPlayer->cam);
            bbFreeEntity(tempCamera);
            bbFreeEntity(tempLight);
            bbAmbientLight((float)Brightness, (float)Brightness, (float)Brightness);
        }

        if (mainPlayer->selectedItem != val) {
            int offset = (int)(32 * MenuScale);
            bbDrawImage(val->invImg, x + SIZE / 2 - offset, y + SIZE / 2 - offset);
        }
    }
}

Inventory::Inventory(int size, int itemsPerRow) {
    items = new ItemCell[size];
    this->size = size;

    displayVertical = false;
    this->itemsPerRow = itemsPerRow;
    spacing = 35;
    xOffset = ItemCell::SIZE * itemsPerRow + spacing * (itemsPerRow - 1) / 2;
    yOffset = (ItemCell::SIZE + spacing) * (size / itemsPerRow) / 2 + spacing / 2;
}

Inventory::~Inventory() {
    delete[] items;
}

int Inventory::getSize() {
    return size;
}

void Inventory::addItem(Item* it, int slot) {
    if (slot >= size) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String(size) + "\" are available.");
    }
    if (slot >= 0) {
        if (items[slot].val != nullptr) {
            throw ("Attempted to overwrite an item slot in an inventory. Item: " + it->getType());
        }
        items[slot].val = it;

    // First available slot.
    } else {
        bool spaceFound = false;
        for (int i = 0; i < size; i++) {
            if (items[i].val == nullptr) {
                items[i].val = it;
                spaceFound = true;
                break;
            }
        }
        if (!spaceFound) {
            throw ("No space in inventory.");
        }
    }

    it->parentInv = this;
}

Item* Inventory::getItem(int slot) {
    if (slot >= size || slot < 0) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String(size) + "\" are available.");
    }
    if (items[slot].val == nullptr) {
        return nullptr;
    }
    return items[slot].val;
}

void Inventory::removeItem(int slot) {
    if (slot >= size || slot < 0) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String(size) + "\" are available.");
    }
    if (items[slot].val == nullptr) {
        throw ("Attempted to remove non-existant item at slot \"" + String(slot) + ".\"");
    }
    delete items[slot].val;
    items[slot].val = nullptr;
}

bool Inventory::anyRoom() {
    for (int i = 0; i < size; i++) {
        if (items[i].val == nullptr) {
            return true;
        }
    }
    return false;
}

void Inventory::update() {
    int cellX = userOptions->screenWidth / 2 - (int)(xOffset * MenuScale);
    int cellY = userOptions->screenHeight / 2 - (int)(yOffset * MenuScale);

    for (int i = 0; i < size; i++) {
        items[i].update(cellX, cellY);

        if (items[i].isHovering()) {
            mainPlayer->hoveredItemCell = &items[i];
            if (MouseHit1 && items[i].val != nullptr) {
                // Selecting an item.
                if (mainPlayer->selectedItem == nullptr) {
                    mainPlayer->selectedItem = items[i].val;
                }

                MouseHit1 = false;
                if (DoubleClick) {
                    // Using the item.
                    mainPlayer->useItem(it);

                    mainPlayer->selectedItem = nullptr;
                    DoubleClick = false;
                }
            } else if (MouseUp1 && mainPlayer->selectedItem != nullptr) {
                // Item already selected and mouse release.

                // Hovering over empty slot. Move the item to the empty slot.
                if (items[i].val == nullptr) {
                    mainPlayer->moveItem(mainPlayer->selectedItem, mainPlayer->selectedItem->parentInv, this, i);

                } else if (items[i].val != mainPlayer->selectedItem) {
                    // Hovering over another item. Attempt to combine the items.
                    //CombineItems(player\selectedItem, player\openInventory\items[slotIndex])
                } else {
                    // Hovering over the item's own slot. Stop selecting the item.
                    mainPlayer->selectedItem = nullptr;
                }
            }

            // If the mouse was hovering over this slot then don't bother iterating through the rest of the inventory.
            break;
        }

        // Move x and y coords to point to next item.
        if (!displayVertical) {
            cellX += ItemCell::SIZE + spacing;
            if (i % itemsPerRow == itemsPerRow-1) {
                cellY += ItemCell::SIZE + spacing;
                cellX = userOptions->screenWidth / 2 - (int)(xOffset * MenuScale);
            }
        } else {
            cellY += ItemCell::SIZE + spacing;
            if (i % itemsPerRow == itemsPerRow-1) {
                cellX += ItemCell::SIZE + spacing;
                cellY = userOptions->screenWidth / 2 - (int)(yOffset * MenuScale);
            }
        }
    }

    if (MouseUp1 && mainPlayer->selectedItem != nullptr && mainPlayer->hoveredItemCell == nullptr) {
        // Mouse release outside a slot, drop the item.
        mainPlayer->dropItem(mainPlayer->selectedItem, this);
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

void Inventory::draw() {
    int cellX = userOptions->screenWidth / 2 - (int)(xOffset * MenuScale);
    int cellY = userOptions->screenHeight / 2 - (int)(yOffset * MenuScale);

    for (int i = 0; i < size; i++) {
        items[i].draw(cellX, cellY, spacing);

        // Move x and y coords to point to next item.
        if (!displayVertical) {
            cellX += ItemCell::SIZE + spacing;
            if (i % itemsPerRow == itemsPerRow-1) {
                cellY += ItemCell::SIZE + spacing;
                cellX = userOptions->screenWidth / 2 - (int)(xOffset * MenuScale);
            }
        } else {
            cellY += ItemCell::SIZE + spacing;
            if (i % itemsPerRow == itemsPerRow-1) {
                cellX += ItemCell::SIZE + spacing;
                cellY = userOptions->screenWidth / 2 - (int)(yOffset * MenuScale);
            }
        }
    }

    // Draw the selected item under the cursor when it's not hovering over the item's original slot.
    if (mainPlayer->selectedItem != nullptr) {
        if (mainPlayer->hoveredItemCell == nullptr || mainPlayer->selectedItem != mainPlayer->hoveredItemCell->val) {
            bbDrawImage(mainPlayer->selectedItem->invImg, bbMouseX() - bbImageWidth(mainPlayer->selectedItem->invImg) / 2, bbMouseY() - bbImageHeight(mainPlayer->selectedItem->invImg) / 2);
        }
    }
}

}
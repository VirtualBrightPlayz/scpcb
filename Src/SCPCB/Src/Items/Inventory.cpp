#include "Inventory.h"
#include "Item.h"

namespace CBN {

Inventory::Inventory(int size) {
    items.reserve(size);
    for (int i = 0; i < items.size(); i++) {
        items[i] = nullptr;
    }
}

Inventory::~Inventory() {
    for (int i = 0; i < items.size(); i++) {
        delete items[i];
    }
}

int Inventory::getSize() {
    return (int)items.size();
}

void Inventory::addItem(Item* it, int slot) {
    if (slot >= items.size()) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String((int)items.size()) + "\" are available.");
    }
    if (slot >= 0) {
        if (items[slot] != nullptr) {
            throw ("Attempted to overwrite an item slot in an inventory. Item: " + it->getType());
        }
        items[slot] = it;

    // First available slot.
    } else {
        bool spaceFound = false;
        for (int i = 0; i < items.size(); i++) {
            if (items[i] == nullptr) {
                items[i] = it;
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
    if (slot >= items.size() || slot < 0) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String((int)items.size()) + "\" are available.");
    }
    if (items[slot] == nullptr) {
        return nullptr;
    }
    return items[slot];
}

void Inventory::removeItem(int slot) {
    if (slot >= items.size() || slot < 0) {
        throw ("Invalid inventory slot. Slot \"" + String(slot) + "\" when only \"" + String((int)items.size()) + "\" are available.");
    }
    if (items[slot] == nullptr) {
        throw ("Attempted to remove non-existant item at slot \"" + String(slot) + ".\"");
    }
    delete items[slot];
    items[slot] = nullptr;
}

}
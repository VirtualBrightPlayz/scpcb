#include "Inventory.h"

#include "../Utils/MathUtil.h"
#include "InvSlot.h"
#include "../World/Item.h"

// Almost fully transferred
Inventory::Inventory(World* wrld, UIMesh* um, KeyBinds* kb, Config* con, int size) : Menu(wrld, "inventory") {
    const int rows = MathUtil::ceil((float)size / maxItemsPerRow);
    if (size % rows != 0) {
        throw new std::runtime_error("Invalid size! " + size);
    }
    const int itemsPerRow = size / rows;

    this->size = size;

    slots = new InvSlot*[size];

    float screenX; float screenY = -(rows * 2 - 1) * InvSlot::screenSize / 2;
    for (int y = 0; y < rows; y++) {
        screenX = -(itemsPerRow * 2 - 1) * InvSlot::screenSize / 2;
        for (int x = 0; x < itemsPerRow; x++) {
            slots[x + y * itemsPerRow] = new InvSlot(um, kb, con, screenX, screenY);
            screenX += InvSlot::screenSize * 2;
        }
        screenY += InvSlot::screenSize * 2;
    }

    setState(SubState::Main);
}

Inventory::~Inventory() {
    for (int i = 0; i < size; i++) {
        delete slots[i];
    }
    delete[] slots;
}

bool Inventory::addItem(Item* it) {
    for (int i = 0; i < size; i++) {
        if (!slots[i]->hasItem()) {
            slots[i]->addItem(it);
            return true;
        }
    }
    return false;
}

void Inventory::setState(SubState state) {
    currState = state;
}

void Inventory::update(const PGE::Vector2f& mousePos, const PGE::Vector2i& mouseWheelDelta) {
    for (int i = 0; i < size; i++) {
        slots[i]->update(mousePos);
    }
}

void Inventory::render() const {
    for (int i = 0; i < size; i++) {
        slots[i]->render();
    }
}

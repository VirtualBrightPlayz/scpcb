#include <cmath>

#include "Inventory.h"
#include "InvSlot.h"

Inventory::Inventory(UIMesh* um, KeyBinds* kb, Config* con) {
    float spacing = 5.f;
    float totalWidth = (ITEMS_PER_ROW * InvSlot::SIZE) + (ITEMS_PER_ROW - 1) * spacing;
    int itemsPerColumn = (int)std::round((float)MAX_ITEM_COUNT / ITEMS_PER_ROW);
    float totalHeight = (itemsPerColumn * InvSlot::SIZE) + (itemsPerColumn - 1) * spacing;
    
    float x = -totalWidth / 2.f;
    float y = -totalHeight / 2.f;
    
    for (int i = 0; i < MAX_ITEM_COUNT; i++) {
        slots[i] = new InvSlot(um, kb, con, x, y);
        
        x += InvSlot::SIZE + spacing;
        if (i % (ITEMS_PER_ROW - 1) == 0) {
            y += InvSlot::SIZE + spacing;
        }
    }
}

Inventory::~Inventory() {
    for (int i = 0; i < MAX_ITEM_COUNT; i++) {
        delete slots[i];
    }
}

void Inventory::setState(SubState state) {
    currState = state;
}

void Inventory::update(PGE::Vector2f mousePos) {
    for (int i = 0; i < MAX_ITEM_COUNT; i++) {
        slots[i]->update(mousePos);
    }
}

void Inventory::render() const {
    if (currState == SubState::Hidden) { return; }
    
    for (int i = 0; i < MAX_ITEM_COUNT; i++) {
        slots[i]->render();
    }
}

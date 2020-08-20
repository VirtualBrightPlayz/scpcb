#include "InvSlot.h"

#include "GUI/GUIFrame.h"

InvSlot::InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y)
: GUIComponent(um, kb, con, x, y, screenSize, screenSize, Alignment::CenterXY) {
    frame = new GUIFrame(um, kb, con, x, y, screenSize, screenSize, Alignment::CenterXY);

    item = nullptr;

    doubleClicked = false;
}

InvSlot::~InvSlot() {
    delete frame;
}

bool InvSlot::hasItem() const {
    return item != nullptr;
}

void InvSlot::updateInternal(PGE::Vector2f mousePos) {
    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        frame->setHovered(true);
        
        if (keyBinds->mouse1->getClickCount() >= 2 && hasItem()) {
            // TODO: Use/de-use item.
        }
    } else {
        frame->setHovered(false);
    }
}

void InvSlot::renderInternal() {
    frame->render();
}

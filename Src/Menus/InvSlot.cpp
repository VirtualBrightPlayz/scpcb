#include "InvSlot.h"

#include "GUI/GUIFrame.h"
#include "../World/Item.h"

InvSlot::InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y)
: GUIComponent(um, kb, con, x, y, screenSize, screenSize, Alignment::CenterXY) {
    background = PGE::Rectanglef(getX() + um->borderThickness, getY() + um->borderThickness, getX2() - um->borderThickness, getY2() - um->borderThickness);
    updateRectangles(false);

    item = nullptr;

    hovered = false;
    equipped = false;
}

InvSlot::~InvSlot() {
    
}

void InvSlot::updateRectangles(bool doubled) {
    float x = getX(); float y = getY();
    float x2 = getX2(); float y2 = getY2();
    float thickness = uiMesh->borderThickness * (doubled ? 2 : 1);
    top = PGE::Rectanglef(x, y, x2, y + thickness);
    bottom = PGE::Rectanglef(x, y2, x2, y2 - thickness);
    right = PGE::Rectanglef(x, y + thickness, x + thickness, y2 - thickness);
    left = PGE::Rectanglef(x2 - thickness, y, x2, y2);
}

void InvSlot::addItem(Item* it) {
    item = it;
}

bool InvSlot::hasItem() const {
    return item != nullptr;
}

void InvSlot::updateInternal(PGE::Vector2f mousePos) {
    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        hovered = true;
        
        if (keyBinds->mouse1->getClickCount() >= 2 && hasItem()) {
            if (item->onUse() != equipped) {
                equipped = !equipped;
                updateRectangles(equipped);
            }
        }
    } else {
        hovered = false;
    }
}

void InvSlot::renderInternal() {
    if (hasItem()) {
        uiMesh->setTextured(item->getInvIcon(), false);
        uiMesh->addRect(background);
    } else {
        uiMesh->setTextured("SCPCB/GFX/Menu/menublack", true);
        uiMesh->addRect(background);
    }
    if (hovered) {
        uiMesh->setTextureless();
        uiMesh->setColor(PGE::Color(0.5f, 0.5f, 0.5f, 0.5f));
        uiMesh->addRect(background);
        uiMesh->setColor(PGE::Color::White);
    }
    uiMesh->setTextured("SCPCB/GFX/Menu/menuwhite", true);
    uiMesh->addRect(top);
    uiMesh->addRect(bottom);
    uiMesh->addRect(right);
    uiMesh->addRect(left);
}

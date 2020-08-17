#include "InvSlot.h"

InvSlot::InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y)
: GUIComponent(um, kb, con, x, y, SIZE, SIZE, Alignment::CenterXY),
    menuwhite("SCPCB/GFX/Menu/menuwhite"),
    menublack("SCPCB/GFX/Menu/menublack") {
    item = nullptr;

    hoverColor = PGE::Color(70, 70, 150, 200);
}

bool InvSlot::hasItem() const {
    return item != nullptr;
}

void InvSlot::updateInternal(PGE::Vector2f mousePos) {
    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        hovered = true;
        
        if (keyBinds->mouse1->getClickCount() >= 2 && hasItem()) {
            // TODO: Use/de-use item.
        }
    } else {
        hovered = false;
    }
}

void InvSlot::renderInternal() {
    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));

    // TODO: Thicken border when item is in use.
    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(getX() + uiMesh->borderThickness, getY() + uiMesh->borderThickness), PGE::Vector2f(getX2() - uiMesh->borderThickness, getY2() - uiMesh->borderThickness));
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(foreground);

    // TODO: Item image.

    if (hovered) {
        uiMesh->setTextureless();
        uiMesh->setColor(hoverColor);
        uiMesh->addRect(foreground);
        uiMesh->setColor(PGE::Color());
    }
}

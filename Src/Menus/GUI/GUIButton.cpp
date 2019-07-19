#include "GUIButton.h"
#include "../../Graphics/UIMesh.h"

GUIButton::GUIButton(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, x, y, width, height, alignment), clicked(false), locked(false) { }

bool GUIButton::isClicked() const {
    return clicked;
}

void GUIButton::updateInternal(PGE::Vector2f mousePos) { 
    if (mousePos.x >= x && mousePos.y >= y
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        hovered = true;

        if (keyBinds->mouse1->isHit()) {
            active = true;
        } else if (!keyBinds->mouse1->isDown() && active) {
            clicked = true;
            active = false;
        }
    } else {
        active = false;
    }
}

void GUIButton::renderInternal() {
    float borderThickness = 1.f;

    uiMesh->setTextured(PGE::FileName("GFX/Menu/menuwhite.jpg"), true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(x, y), PGE::Vector2f(getX2(), getY2())));

    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(x + borderThickness, y + borderThickness), PGE::Vector2f(getX2() - borderThickness, getY2() - borderThickness));
    uiMesh->setTextured(PGE::FileName("GFX/Menu/menublack.jpg"), true);
    uiMesh->addRect(foreground);

    // TODO: Figure out hovering.
    //if (hovered) {
    //    uiMesh->setTextureless();
    //    uiMesh->setColor(hoverColor);
    //    uiMesh->addRect(foreground);
    //}
}

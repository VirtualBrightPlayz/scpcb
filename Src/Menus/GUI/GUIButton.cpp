#include "GUIButton.h"
#include "../../Graphics/UIMesh.h"

GUIButton::GUIButton(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment), clicked(false), locked(false) {
    menuwhite = "GFX/Menu/menuwhite.jpg";
    menublack = "GFX/Menu/menublack.jpg";
    hoverColor = PGE::Color(70, 70, 70, 255);
    borderThickness = 1.f;
}

bool GUIButton::isClicked() const {
    return clicked;
}

void GUIButton::updateInternal(PGE::Vector2f mousePos) {
    clicked = false;
    
    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        hovered = true;

        if (keyBinds->mouse1->isHit()) {
            active = true;
        } else if (!keyBinds->mouse1->isDown() && active) {
            clicked = true;
            active = false;
        }
    } else {
        hovered = false;
        active = false;
    }
}

void GUIButton::renderInternal() {
    uiMesh->setTextured(menuwhite, false);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));

    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(getX() + borderThickness, getY() + borderThickness), PGE::Vector2f(getX2() - borderThickness, getY2() - borderThickness));
    uiMesh->setTextured(menublack, false);
    uiMesh->addRect(foreground);

    if (hovered) {
        uiMesh->setTextureless();
        uiMesh->setColor(hoverColor);
        uiMesh->addRect(foreground);
        uiMesh->setColor(PGE::Color());
    }
}

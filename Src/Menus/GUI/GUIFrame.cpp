#include "GUIFrame.h"
#include "../../Graphics/UIMesh.h"

static const PGE::Color hoverColor = PGE::Color(70, 70, 150, 200);

// Ported.
GUIFrame::GUIFrame(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    foreground = PGE::Rectanglef(getX() + uiMesh->borderThickness, getY() + uiMesh->borderThickness, getX2() - uiMesh->borderThickness, getY2() - uiMesh->borderThickness);
    background = PGE::Rectanglef(getX(), getY(), getX2(), getY2());

    menuwhite = "SCPCB/GFX/Menu/menuwhite";
    menublack = "SCPCB/GFX/Menu/menublack";

    hovered = false;
}

void GUIFrame::updateInternal(PGE::Vector2f mousePos) {}

void GUIFrame::renderInternal() {
    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(background);
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(foreground);

    if (hovered) {
        uiMesh->setTextureless();
        uiMesh->setColor(hoverColor);
        uiMesh->addRect(foreground);
        uiMesh->setColor(PGE::Color::White);
    }
}

void GUIFrame::setHovered(bool hovered) {
    this->hovered = hovered;
}

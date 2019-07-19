#include "GUIButton.h"
#include "../../Graphics/UIMesh.h"

GUIButton::GUIButton(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, x, y, width, height, alignment), clicked(false), locked(false) {
    float fgOffset = 0.5f;
}

bool GUIButton::isClicked() const {
    return clicked;
}

void GUIButton::updateInternal(PGE::Vector2f mousePos) { }

void GUIButton::renderInternal() {
    uiMesh->setTextured(PGE::FileName("GFX/Menu/menuwhite.jpg"), true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(x, y), PGE::Vector2f(getX2(), getY2())));
    uiMesh->setTextured(PGE::FileName("GFX/Menu/menublack.jpg"), true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(x + 1.0, y + 1.0), PGE::Vector2f(getX2() - 1.0, getY2() - 1.0)));
}

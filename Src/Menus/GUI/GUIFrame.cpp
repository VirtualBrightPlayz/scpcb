#include "GUIFrame.h"
#include "../../Graphics/UIMesh.h"

GUIFrame::GUIFrame(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, x, y, width, height, alignment) {
    float fgOffset = 0.5f;
}

void GUIFrame::updateInternal(PGE::Vector2f mousePos) { }

void GUIFrame::renderInternal() {
    uiMesh->setTextured(PGE::FileName("GFX/Menu/menuwhite.jpg"), true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(x, y), PGE::Vector2f(getX2(), getY2())));
    uiMesh->setTextured(PGE::FileName("GFX/Menu/menublack.jpg"), true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(x+1.0, y+1.0), PGE::Vector2f(getX2()-1.0, getY2()-1.0)));
}

#include "GUIFrame.h"
#include "../../Graphics/UIMesh.h"

GUIFrame::GUIFrame(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    menuwhite = PGE::FilePath::fromStr("SCPCB/GFX/Menu/menuwhite.jpg");
    menublack = PGE::FilePath::fromStr("SCPCB/GFX/Menu/menublack.jpg");
    borderThickness = 1.f;
}

void GUIFrame::updateInternal(PGE::Vector2f mousePos) { }

void GUIFrame::renderInternal() {
    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX() + borderThickness, getY() + borderThickness), PGE::Vector2f(getX2() - borderThickness, getY2() - borderThickness)));
}

#include "GUIFrame.h"
#include "../../Graphics/UIMesh.h"

GUIFrame::GUIFrame(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment),
    menuwhite("SCPCB/GFX/Menu/menuwhite"),
    menublack("SCPCB/GFX/Menu/menublack") {}

void GUIFrame::updateInternal(PGE::Vector2f mousePos) {}

void GUIFrame::renderInternal() {
    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX() + uiMesh->borderThickness, getY() + uiMesh->borderThickness), PGE::Vector2f(getX2() - uiMesh->borderThickness, getY2() - uiMesh->borderThickness)));
}

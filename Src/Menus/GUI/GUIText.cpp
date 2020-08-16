#include "GUIText.h"
#include "../../Graphics/Font.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* font, float x, float y, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) {
    this->font = font;
    setScale(1.f);
}

void GUIText::setScale(float sc) {
    scale = sc;
}

void GUIText::updateInternal(PGE::Vector2f mousePos) { }

void GUIText::renderInternal() {
    uiMesh->setTextureless();
    PGE::Vector2f txtScale = PGE::Vector2f(0.1388f * scale);
    font->draw(rt.text, PGE::Vector2f(getX(), getY()), txtScale, rt.rotation, rt.color);
}

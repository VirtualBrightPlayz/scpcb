#include "GUIText.h"
#include "../../Graphics/Font.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* font, float x, float y, bool center, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) {
    this->font = font;
    this->center = center;
    setScale(1.f);
}

void GUIText::setScale(float sc) {
    scale = sc;
}

void GUIText::updateInternal(PGE::Vector2f mousePos) { }

void GUIText::renderInternal() {
    uiMesh->setTextureless();
    PGE::Vector2f txtScale = PGE::Vector2f(0.1388f * scale);
    PGE::Vector2f pos = PGE::Vector2f(getX(), getY());
    if (center) {
        pos = font->centerTextCoords(rt.text, pos.x, pos.y, width, height, txtScale);
    }
    font->draw(rt.text, pos, txtScale, rt.rotation, rt.color);
}

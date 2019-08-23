#include "GUIText.h"
#include "../../Graphics/Font.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* font, float x, float y, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) {
    this->font = font;
}

void GUIText::updateInternal(PGE::Vector2f mousePos) { }

void GUIText::renderInternal() {
    PGE::Vector2f txtScale = PGE::Vector2f(100.f / 720.f);
    font->draw(text, PGE::Vector2f(getX(), getY()), txtScale);
}

#include "GUIText.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* font, float x, float y, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) { }

void GUIText::updateInternal(PGE::Vector2f mousePos) { }

void GUIText::renderInternal() {

}

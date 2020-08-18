#include "GUIText.h"
#include "../../Graphics/Font.h"
#include "../../Utils/LocalizationManager.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt, LocalizationManager* lm, float x, float y, bool center, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) {
    this->font = fnt;
    this->lm = lm;

    this->center = center;
    text = "";
    color = PGE::Color::White;
    rotation = 0.f;
    scale = 1.f;
}

void GUIText::updateInternal(PGE::Vector2f mousePos) {}

void GUIText::renderInternal() {
    uiMesh->setTextureless();
    PGE::Vector2f txtScale = PGE::Vector2f(0.1388f * scale);
    PGE::Vector2f pos = PGE::Vector2f(getX(), getY());
    if (center) {
        pos = font->centerTextCoords(text, pos.x, pos.y, width, height, txtScale);
    }
    font->draw(text, pos, txtScale, rotation, color);
}

void GUIText::setText(const PGE::String& newText) {
    if (lm == nullptr) {
        text = newText;
    } else {
        text = lm->getLocalTxt(newText);
    }
}

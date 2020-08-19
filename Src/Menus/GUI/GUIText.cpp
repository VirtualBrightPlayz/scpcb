#include "GUIText.h"

#include "../../Graphics/Font.h"
#include "../../Utils/LocalizationManager.h"
#include "../../Save/Config.h"

GUIText::GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt, LocalizationManager* lm, float x, float y, bool centerX, bool centerY, Alignment alignment)
: GUIComponent(um, kb, con, x, y, 0.f, 0.f, alignment) {
    this->font = fnt;
    this->lm = lm;

    this->centerX = centerX;
    this->centerY = centerY;
    text = "";
    color = PGE::Color::White;
    rotation = 0.f;
    scale = 100.f / con->getHeight();

    updatePosition();
}

void GUIText::updateInternal(PGE::Vector2f mousePos) {}

void GUIText::renderInternal() {
    uiMesh->setTextureless();
    font->draw(text, pos, scale, rotation, color);
}

void GUIText::updatePosition() {
    pos = PGE::Vector2f(getX(), getY());
    if (centerX) {
        pos.x = pos.x - font->stringWidth(text, scale) / 2.f;
    }
    if (centerY) {
        pos.y = pos.y - font->getHeight(scale) / 1.5;
    }
}

void GUIText::setX(float x) {
    GUIComponent::setX(x);
    updatePosition();
}

void GUIText::setY(float y) {
    GUIComponent::setY(y);
    updatePosition();
}

const PGE::Vector2f& GUIText::getTextPos() const {
    return pos;
}

float GUIText::getWidth() const {
    return font->stringWidth(text, scale);
}

float GUIText::getWidth(const PGE::String& text) const {
    return font->stringWidth(text, scale);
}

float GUIText::getHeight() const {
    return font->getHeight(scale);
}

void GUIText::setText(const PGE::String& newText) {
    if (lm == nullptr) {
        text = newText;
    } else {
        text = lm->getLocalTxt(newText);
    }
    updatePosition();
}

const PGE::String& GUIText::getText() {
    return text;
}

#include "GUIComponent.h"
#include "../../Input/KeyBinds.h"
#include "../../Save/Config.h"

GUIComponent::GUIComponent(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
    : uiMesh(um), keyBinds(kb), config(con), x(x), y(y), width(width), height(height), alignment(alignment) { 
    mouseDown = false;
}

GUIComponent::~GUIComponent() { }

void GUIComponent::setX(float x) {
    this->x = x;
}

void GUIComponent::setY(float y) {
    this->y = y;
}

float GUIComponent::getX() const {
    float trueX = x;

    if ((alignment & Alignment::Left) != Alignment::CenterXY) {
        trueX -= SCALE_MAGNITUDE * config->getAspectRatio();
    }

    if ((alignment & Alignment::Right) != Alignment::CenterXY) {
        trueX += SCALE_MAGNITUDE * config->getAspectRatio();
    }

    return trueX;
}

float GUIComponent::getY() const {
    float trueY = y;

    if ((alignment & Alignment::Top) != Alignment::CenterXY) {
        trueY -= SCALE_MAGNITUDE;
    }

    if ((alignment & Alignment::Bottom) != Alignment::CenterXY){
        trueY += SCALE_MAGNITUDE;
    }

    return trueY;
}

float GUIComponent::getX2() const {
    return x + width;
}

float GUIComponent::getY2() const {
    return y + height;
}

void GUIComponent::update(PGE::Vector2f mousePos) {
    updateInternal(mousePos);
}

void GUIComponent::render() {
    renderInternal();
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}

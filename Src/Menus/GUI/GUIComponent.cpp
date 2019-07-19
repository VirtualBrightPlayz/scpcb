#include "GUIComponent.h"
#include "../../Input/KeyBinds.h"
#include "../../Save/Config.h"

GUIComponent::GUIComponent(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment)
    : uiMesh(um), keyBinds(kb), config(con), x(x), y(y), width(width), height(height), alignment(alignment) { }

GUIComponent::~GUIComponent() { }

float GUIComponent::getX() const {
    float trueX = x;
    
    if ((alignment & Alignment::Left) != Alignment::CenterXY) {
        trueX += -50.f * config->getAspectRatio();
    }
    
    if ((alignment & Alignment::Right) != Alignment::CenterXY) {
        trueX += 50.f * config->getAspectRatio();
    }
    
    return trueX;
}

float GUIComponent::getY() const {
    float trueY = y;
    
    if ((alignment & Alignment::Top) != Alignment::CenterXY) {
        trueY += -50.f;
    }
    
    if ((alignment & Alignment::Bottom) != Alignment::CenterXY){
        trueY += 50.f;
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

#include "GUIComponent.h"
#include "../../Input/KeyBinds.h"

GUIComponent::GUIComponent(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment)
    : uiMesh(um), keyBinds(kb), x(x), y(y), width(width), height(height), alignment(alignment), visible(true) { }

GUIComponent::~GUIComponent() { }

float GUIComponent::getX2() const {
    return x + width;
}

float GUIComponent::getY2() const {
    return y + height;
}

void GUIComponent::setVisibility(bool vis) {
    visible = vis;
}

void GUIComponent::update(PGE::Vector2f mousePos) {
    if (visible) { updateInternal(mousePos); }
}

void GUIComponent::render() {
    if (visible) { renderInternal(); }
}

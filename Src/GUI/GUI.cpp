#include "GUI.h"
#include "../Input/KeyBinds.h"

GUI* GUI::active = nullptr;
GUI* GUI::hover = nullptr;

void GUI::reset() {
    hover = nullptr;

    // Remove active element when the mouse is up.
    if (active != nullptr && keyBinds.mouse1.isUp()) {
        active = nullptr;
    }
}

GUI::GUI(float x, float y, float width, float height, Alignment alignment)
    : x(x), y(y), width(width), height(height), alignment(alignment), visible(true) { }

GUI::~GUI() {
    if (this == active) {
        active = nullptr;
    }
}

float GUI::getX2() const {
    return x + width;
}

float GUI::getY2() const {
    return y + height;
}

bool GUI::isHovered() const {
    return hovered;
}

void GUI::setVisibility(bool vis) {
    visible = vis;
    updateVisibility();
}

void GUI::update(PGE::Vector2f mousePos) {
    if (!visible) { return; }

    // Reset hover state.
    hovered = false;
    mouseDown = keyBinds.mouse1.isDown();

    if (mousePos.x >= x && mousePos.x <= getX2()) {
        if (mousePos.y >= y && mousePos.y <= getY2()) {
            hovered = true;
            hover = this;
        }
    }

    internalUpdate(mousePos);

    if (active != this || hover != nullptr || !hovered) {
        return;
    }

    // Is the mouse is hit on this element then it becomes the only selected element until mouse1 is up.
    if (keyBinds.mouse1.isHit()) {
        active = this;
    }

    activeUpdate(mousePos);
}
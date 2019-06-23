#include "GUI.h"

GUI* GUI::active = nullptr;
GUI* GUI::hover = nullptr;

void GUI::reset() {
    hover = nullptr;

    // TODO: Check for mouseup to remove active state.
}

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

void GUI::update(PGE::Vector2f mousePos) {
    // Reset hover state.
    hovered = false;

    // Don't waste time if another element is already in use.
    if (active != nullptr || hover != nullptr) {
        return;
    }

    if (mousePos.x >= x && mousePos.x <= getX2()) {
        if (mousePos.y >= y && mousePos.y <= getY2()) {
            hovered = true;
            hover = this;
        }
    }

    if (!hovered) { return; }

    // TODO: Mousedown check for active.

    // TODO: Set mouseDown and mouseUp fields.

    internalUpdate(mousePos);
}
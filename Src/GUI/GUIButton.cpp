#include <iostream>

#include "GUIButton.h"
#include "UIAssets.h"
#include "../Graphics/UIMesh.h"

GUIButton::GUIButton() : GUI(0.f, 0.f, 0.f, 0.f, Alignment::CenterXY), clicked(false) { }

GUIButton::GUIButton(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment)
: GUI(x, y, width, height, alignment), clicked(false) {
    background = assets->menuWhite->createSlice(x, y, width, height);
    foreground = assets->menuBlack->createSlice(x + 0.5f, y + 0.5f, width - 1.f, height - 1.f);
}

void GUIButton::updateVisibility() {
    background->visible = visible;
    foreground->visible = visible;
}

void GUIButton::internalUpdate(PGE::Vector2f mousePos) {
    foreground->visible = isHovered();

    // Reset clicked state.
    if (clicked) {
        clicked = false;
    }
}

void GUIButton::activeUpdate(PGE::Vector2f mousePos) {
    if (!mouseDown) {
        clicked = true;
    }
}

bool GUIButton::isClicked() const {
    return clicked;
}
#include "GUIButton.h"
#include "../UIAssets.h"
#include "../../Graphics/UIMesh.h"

GUIButton::GUIButton() : GUI(0.f, 0.f, 0.f, 0.f, Alignment::CenterXY), clicked(false) { }

GUIButton::GUIButton(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment)
: GUI(x, y, width, height, alignment), clicked(false) {
    background = assets->menuWhite->createSlice(x, y, width, height);

    float fgOffset = 0.5f;
    foreground = assets->menuBlack->createSlice(x + fgOffset, y + fgOffset, width - fgOffset * 2, height - fgOffset * 2);
    hoverColor = assets->hoverLightUp->createSlice(x + fgOffset, y + fgOffset, width - fgOffset * 2, height - fgOffset * 2);
}

void GUIButton::updateVisibility(bool visible) {
    background->visible = visible;
    foreground->visible = visible;
}

void GUIButton::internalUpdate(PGE::Vector2f mousePos) {
    hoverColor->visible = isHovered();

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

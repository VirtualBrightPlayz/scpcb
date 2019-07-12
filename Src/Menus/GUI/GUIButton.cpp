#include "GUIButton.h"
#include "../UIAssets.h"
#include "../../Graphics/UIMesh.h"

GUIButton::GUIButton(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, const UIAssets* assets, Alignment alignment)
: GUIComponent(um, kb, x, y, width, height, alignment), clicked(false), locked(false) {
    float fgOffset = 0.5f;
}

bool GUIButton::isClicked() const {
    return clicked;
}

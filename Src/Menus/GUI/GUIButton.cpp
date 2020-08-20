#include "GUIButton.h"

#include "GUIFrame.h"
#include "GUIText.h"
#include "../../Graphics/UIMesh.h"
#include "../../Utils/LocalizationManager.h"

GUIButton::GUIButton(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, LocalizationManager* lm, float x, float y, float width, float height, const PGE::String& txt, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    frame = new GUIFrame(um, kb, con, x, y, width, height, alignment);
    text = new GUIText(um, kb, con, fnt, lm, x + width / 2, y + height / 2, true, true, alignment);
    text->setText(txt);

    locked = false;
    active = false;
    clicked = false;
}

GUIButton::~GUIButton() {
    delete frame;
    delete text;
}

bool GUIButton::isClicked() const {
    return clicked;
}

void GUIButton::updateInternal(PGE::Vector2f mousePos) {
    clicked = false;

    if (locked) { return; }

    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        frame->setHovered(!locked);

        if (keyBinds->mouse1->isHit()) {
            active = true;
        } else if (!keyBinds->mouse1->isDown() && active) {
            clicked = true;
            active = false;
        }
    } else {
        frame->setHovered(false);
        active = false;
    }
}

void GUIButton::renderInternal() {
    frame->render();
    text->render();
}

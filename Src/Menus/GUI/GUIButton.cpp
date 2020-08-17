#include "GUIButton.h"
#include "../../Graphics/UIMesh.h"
#include "../../Utils/LocalizationManager.h"

GUIButton::GUIButton(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, LocalizationManager* tm, float x, float y, float width, float height, const PGE::String& txt, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment), clicked(false), locked(false),
    menuwhite("SCPCB/GFX/Menu/menuwhite"),
    menublack("SCPCB/GFX/Menu/menublack") {
    hoverColor = PGE::Color(70, 70, 150, 200);

    font = fnt;
    text = txt;
    txtMng = tm;
}

bool GUIButton::isClicked() const {
    return clicked;
}

void GUIButton::updateInternal(PGE::Vector2f mousePos) {
    clicked = false;

    if (locked) { return; }

    if (mousePos.x >= getX() && mousePos.y >= getY()
        && mousePos.x <= getX2() && mousePos.y <= getY2()) {
        hovered = true;

        if (keyBinds->mouse1->isHit()) {
            active = true;
        } else if (!keyBinds->mouse1->isDown() && active) {
            clicked = true;
            active = false;
        }
    } else {
        hovered = false;
        active = false;
    }
}

void GUIButton::renderInternal() {
    // uiMesh->startRender();

    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));

    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(getX() + uiMesh->borderThickness, getY() + uiMesh->borderThickness), PGE::Vector2f(getX2() - uiMesh->borderThickness, getY2() - uiMesh->borderThickness));
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(foreground);

    if (hovered && !locked) {
        uiMesh->setTextureless();
        uiMesh->setColor(hoverColor);
        uiMesh->addRect(foreground);
        uiMesh->setColor(PGE::Color());
    }

    // uiMesh->endRender();

    if (!text.isEmpty()) {
        // Render anything buffered so the text doesn't get overlapped.
        uiMesh->endRender();
        uiMesh->startRender();

        PGE::String local = txtMng->getLocalTxt(text);
        PGE::Vector2f txtScale = PGE::Vector2f(100.f / 720.f);

        font->draw(local, font->centerTextCoords(local, getX(), getY(), width, height, txtScale), txtScale);
    }
}

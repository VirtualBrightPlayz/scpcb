#include "GUIButton.h"
#include "../../Graphics/UIMesh.h"
#include "../../Utils/TextMgmt.h"

GUIButton::GUIButton(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, TxtManager* tm, float x, float y, float width, float height, const PGE::String& txt, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment), clicked(false), locked(false) {
    menuwhite = PGE::FileName::fromStr("GFX/Menu/menuwhite.jpg");
    menublack = PGE::FileName::fromStr("GFX/Menu/menublack.jpg");
    hoverColor = PGE::Color(70, 70, 150, 200);
    borderThickness = 0.33f;

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

    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(getX() + borderThickness, getY() + borderThickness), PGE::Vector2f(getX2() - borderThickness, getY2() - borderThickness));
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
        float txtX; float txtY;
        font->centerTextCoords(txtX, txtY, local, getX(), getY(), width, height, txtScale);

        font->draw(local, PGE::Vector2f(txtX, txtY), txtScale);
    }
}

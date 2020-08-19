#include "MessageManager.h"

#include "LocalizationManager.h"
#include "../Menus/GUI/GUIText.h"
#include "../Graphics/UIMesh.h"
#include "../Input/KeyBinds.h"
#include "../Graphics/Font.h"

MessageManager::MessageManager(LocalizationManager* lm, UIMesh* um, KeyBinds* kb, Config* con, Font* font) {
    this->um = um;
    this->font = font;
    this->text = new GUIText(um, kb, con, font, lm, 0.f, -33.3f, true, true, Alignment::Bottom);
    displayTimer = 0.f;
}

void MessageManager::setMsg(const PGE::String& local, float seconds, const PGE::Color& color) {
    text->setText(local);
    text->color = color;
    displayTimer = seconds;
}

void MessageManager::update(float timeStep) {
    if (displayTimer <= 0) { return; }
    displayTimer -= timeStep;
}

void MessageManager::draw() {
    if (displayTimer > 0) {
        if (displayTimer < 1.f) {
            text->color = PGE::Color(text->color.red, text->color.green, text->color.blue, displayTimer);
        }
        text->render();
    }
}

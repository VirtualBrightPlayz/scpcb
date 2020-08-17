#include "MessageManager.h"

#include "LocalizationManager.h"
#include "../Menus/GUI/GUIText.h"
#include "../Graphics/UIMesh.h"
#include "../Input/KeyBinds.h"
#include "../Graphics/Font.h"

MessageManager::MessageManager(LocalizationManager* tm, UIMesh* um, KeyBinds* kb, Config* con, Font* font) :
    tm(tm),
    um(um),
    font(font),
    text(new GUIText(um, kb, con, font, 0.f, -33.3f, true, Alignment::Bottom)) {
    displayTimer = 0.f;
}

void MessageManager::setMsg(const PGE::String& local, float seconds, const PGE::Color& color) {
    text->rt = { tm->getLocalTxt(local), color };
    displayTimer = seconds;
}

void MessageManager::update(float timeStep) {
    if (displayTimer <= 0) { return; }
    displayTimer -= timeStep;
}

void MessageManager::draw() {
    if (displayTimer > 0) {
        if (displayTimer < 1.f) {
            PGE::Color oldColor = text->rt.color;
            text->rt.color = PGE::Color(oldColor.red, oldColor.green, oldColor.blue, displayTimer);
        }
        text->render();
    }
}

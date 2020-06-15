#include "Menu.h"
#include "../World/World.h"

Menu::Menu(World* wrld, const PGE::String& type) {
    world = wrld;
    this->type = type;
    markedForRemoval = false;
}

bool Menu::isMarkedForDeath() const {
    return markedForRemoval;
}

void Menu::detach() {
    world->deactivateMenu(this);
}

const PGE::String& Menu::getType() const {
    return type;
}

void Menu::onEscapeHit() {
    detach();
}

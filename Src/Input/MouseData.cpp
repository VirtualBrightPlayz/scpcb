#include "MouseData.h"

#include "../Menus/GUI/GUIComponent.h"
#include "../Save/Config.h"

MouseData::MouseData(PGE::IO* io, Config* config) {
    this->io = io;
    this->config = config;
}

void MouseData::update() {
    // Get mouse position and convert it to screen coordinates.

    // Convert it to [0, 100].
    PGE::Vector2f scale = PGE::Vector2f(GUIComponent::SCALE_MAGNITUDE * 2 / config->getWidth(), GUIComponent::SCALE_MAGNITUDE * 2 / config->getHeight());
    mousePosition = PGE::Vector2f(io->getMousePosition().x * scale.x, io->getMousePosition().y * scale.y);
    mousePosition.x *= config->getAspectRatio();

    // Subtract 50 to bring it inline with the [-50, 50] screen coordinates.
    mousePosition.x -= GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio();
    mousePosition.y -= GUIComponent::SCALE_MAGNITUDE;

    mouseWheelDelta = io->getMouseWheelDelta();
}

const PGE::Vector2f& MouseData::getPosition() const {
    return mousePosition;
}

const PGE::Vector2f& MouseData::getWheelDelta() const { // Casting to 2f for AngelScript.
    return mouseWheelDelta;
}

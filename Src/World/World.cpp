#include <iostream>
#include <cmath>
#include <Color/Color.h>

#include "World.h"
#include "Timing.h"
#include "ShaderManager.h"
#include "../Menus/PauseMenu.h"
#include "../Save/Config.h"
#include "../GUI/GUI.h"
#include "../Input/Binding.h"
#include "../Input/KeyBinds.h"

World::World() {
    int width = 1280 * 2;
    int height = 720 * 2;

    Config::initialize("options.ini");
    config.setResolution(width, height);

    graphics = Graphics::create("SCP - Containment Breach", width, height, false);
    graphics->setViewport(PGE::Rectanglei(0, 0, width, height));
    io = IO::create(graphics->getWindow());
    camera = new Camera(graphics, config.getAspectRatio());

    timing = new Timing(60);

    shaderMngt = new ShaderManager(graphics, camera);

    Sprite::initialize(graphics, shaderMngt->getSpriteShader());
    UIMesh::initialize(shaderMngt->getUIShader());
    Binding::initialize(io);
    KeyBinds::initialize();

    poster = Sprite::create(graphics, "GFX/Map/Textures/dirtymetal.jpg");
    poster.setPosition(0.f, 0.f, 2.f);
    poster.setRotation(0.5f);
    poster.setScale(2.f);

    menuwhite = UIMesh(graphics, "GFX/Menu/menuwhite.jpg", true);
    fake = menuwhite.createSlice(20.f, -20.f, 20.f, 20.f);
    fake->setAlignment(Alignment::Top | Alignment::Bottom);

    setGameState(GameState::Playing);
    pauseMenu = new PauseMenu();

    isRoadRollered = false;
}

World::~World() {
    KeyBinds::cleanup();
    Binding::cleanup();
    UIMesh::cleanup();
    Sprite::cleanup();
    Config::cleanup();

    delete camera;
    delete timing;
    delete shaderMngt;
}

void World::setGameState(GameState gs) {
    currState = gs;

    switch (currState) {
        case GameState::Playing: {
            io->setMousePosition(PGE::Vector2i(graphics->getWindow()->getWidth() / 2, graphics->getWindow()->getHeight() / 2));
        } break;
    }

    io->setMouseVisibility(currState != GameState::Playing);
}

bool World::run() {
    if (isRoadRollered) {
        return false;
    }

    // Non tick-based updating.
    SysEvents::update();
    io->update();
    graphics->update();

    graphics->clear(PGE::Color(0.f, 0.71f, 0.76f, 1.f)); // Turquoise.

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    while (timing->tickReady()) {
        runTick(timing->getTimeStep());
        timing->subtractTick();
    }

    draw();

    return graphics->getWindow()->isOpen();
}

void World::runTick(float timeStep) {
    // Get mouse position and convert it to screen coordinates.

    // Convert it to [0, 100].
    PGE::Vector2f scale = PGE::Vector2f(100.f / graphics->getWindow()->getWidth() , 100.f / graphics->getWindow()->getHeight());
    PGE::Vector2f mousePosition = PGE::Vector2f(io->getMousePosition().x * scale.x, io->getMousePosition().y * scale.y);

    // Subtract 50 to bring it inline with the [-50, 50] screen coordinates.
    mousePosition.x -= 50.f;
    mousePosition.y -= 50.f * config.getAspectRatio();

    keyBinds.update();
    if (keyBinds.escape.isHit()) {
        setGameState(currState == GameState::Playing ? GameState::PauseMenu : GameState::Playing);
    }

    switch (currState) {
        case GameState::Playing: {
            updatePlaying(timeStep);
        } break;

        case GameState::PauseMenu: {
            pauseMenu->update();
        } break;
    }

    shaderMngt->update(camera);

    GUI::reset();
}

void World::draw() {
    drawPlaying();
    pauseMenu->draw(currState);

    graphics->swap(config.isVsync());
}

void World::updatePlaying(float timeStep) {
    int centerX = graphics->getWindow()->getWidth() / 2;
    int centerY = graphics->getWindow()->getHeight()  / 2;

    // TODO: Sensitivity from Config class.
    float mouseXDiff = (float)(io->getMousePosition().x - centerX) / 300.f;
    float mouseYDiff = (float)(io->getMousePosition().y - centerY) / 300.f;

    camera->addAngle(mouseXDiff, mouseYDiff);
    camera->update();

    // Update the view matrix for the shaders.
    shaderMngt->update(camera);

    // Reset mouse to center.
    io->setMousePosition(PGE::Vector2i(centerX, centerY));

    poster.addRotation(5.f * timeStep);
    poster.update();

    menuwhite.bake();
}

void World::drawPlaying() {
    menuwhite.render();
//    poster.render();
}

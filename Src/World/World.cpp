#include <iostream>
#include <cmath>
#include <Color/Color.h>

#include "World.h"
#include "Timing.h"
#include "ShaderManager.h"
#include "../Menus/PauseMenu.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUI.h"
#include "../Menus/UIAssets.h"
#include "../Input/Binding.h"
#include "../Input/KeyBinds.h"

World::World() {
    int width = 1280;
    int height = 720;

    Config::initialize("options.ini");
    config.setResolution(width, height);

    graphics = Graphics::create("SCP - Containment Breach", width, height, false);
    graphics->setViewport(PGE::Rectanglei(0, 0, width, height));
    io = IO::create(graphics->getWindow());
    camera = new Camera(graphics, config.getAspectRatio());

    timing = new Timing(60);

    shaderMngt = new ShaderManager(graphics, camera);

    Sprite::initialize(graphics, shaderMngt->getSpriteShader());
    UIMesh::initialize(shaderMngt->getUIShader(), shaderMngt->getUIShaderNoTex());
    Binding::initialize(io);
    KeyBinds::initialize();

    uiAssets = new UIAssets(graphics);

    poster = Sprite::create(graphics, "GFX/Map/Textures/dirtymetal.jpg");
    poster.setPosition(0.f, 0.f, 2.f);
    poster.setRotation(0.5f);
    poster.setScale(1.f);

    setGameState(GameState::Playing);
    pauseMenu = new PauseMenu(uiAssets);

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
    delete uiAssets;
    delete shaderMngt;
}

void World::setGameState(GameState gs) {
    currState = gs;

    switch (currState) {
        case GameState::Playing: {
            io->setMousePosition(PGE::Vector2f(config.getWidth() / 2, config.getHeight() / 2));
        } break;
    }

    io->setMouseVisibility(currState != GameState::Playing);
}

bool World::run() {
    if (isRoadRollered) {
        return false;
    }

    // Non tick-based updating.
    graphics->update();

    graphics->clear(PGE::Color(0.f, 0.71f, 0.76f, 1.f)); // Turquoise.

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    while (timing->tickReady()) {
        runTick((float)timing->getTimeStep());
        timing->subtractTick();
    }

    draw();

    return graphics->getWindow()->isOpen();
}

void World::runTick(float timeStep) {
    SysEvents::update();
    io->update();

    // Get mouse position and convert it to screen coordinates.

    // Convert it to [0, 100].
    PGE::Vector2f scale = PGE::Vector2f(100.f / config.getWidth() , 100.f / config.getHeight());
    PGE::Vector2f mousePosition = PGE::Vector2f(io->getMousePosition().x * scale.x, io->getMousePosition().y * scale.y);
    mousePosition.x *= config.getAspectRatio();

    // Subtract 50 to bring it inline with the [-50, 50] screen coordinates.
    mousePosition.x -= 50.f * config.getAspectRatio();
    mousePosition.y -= 50.f;

    keyBinds.update();
    if (keyBinds.escape.isHit()) {
        setGameState(currState == GameState::Playing ? GameState::PauseMenu : GameState::Playing);
    }
    if (keyBinds.mouse1.isHit()) {
        std::cout << "MouseX: " << mousePosition.x << std::endl;
        std::cout << "MouseY: " << mousePosition.y << std::endl;
    }

    switch (currState) {
        case GameState::Playing: {
            updatePlaying(timeStep);
        } break;

        case GameState::PauseMenu: {
            pauseMenu->update(this, mousePosition);
        } break;
    }

    shaderMngt->update(camera);

    uiAssets->update();

    GUI::reset();
}

void World::draw() {
    drawPlaying();

    uiAssets->draw();

    graphics->swap(config.isVsync());
}

void World::updatePlaying(float timeStep) {
    int centerX = config.getWidth() / 2;
    int centerY = config.getHeight()  / 2;

    // TODO: Sensitivity from Config class.
    float mouseXDiff = (float)(io->getMousePosition().x - centerX) / 300.f;
    float mouseYDiff = (float)(io->getMousePosition().y - centerY) / 300.f;

    camera->addAngle(mouseXDiff, mouseYDiff);
    camera->update();

    // Update the view matrix for the shaders.
    shaderMngt->update(camera);

    // Reset mouse to center.
    io->setMousePosition(PGE::Vector2f(centerX, centerY));

    poster.addRotation(5.f * timeStep);
    poster.update();
}

void World::drawPlaying() {
   poster.render();
}

void World::quit() {
    isRoadRollered = true;
}

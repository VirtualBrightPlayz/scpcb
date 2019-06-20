#include <iostream>
#include <cmath>
#include <Color/Color.h>

#include "World.h"
#include "Timing.h"
#include "ShaderManager.h"

World::World() {
    int width = 1280;
    int height = 720;
    int retinaUpscale = 2;

    graphics = Graphics::create("SCP - Containment Breach", width, height, false);
    graphics->setViewport(PGE::Rectanglei(0, 0, width * retinaUpscale, height * retinaUpscale));
    io = IO::create(graphics->getWindow());
    camera = new Camera(graphics, (float)graphics->getWindow()->getWidth() / graphics->getWindow()->getHeight());

    timing = new Timing(60);

    shaderMngt = new ShaderManager(graphics, camera);

    poster = Sprite::create(graphics, shaderMngt->getSpriteShader(), "GFX/079pics/angery.jpg");
    poster.setPosition(0, 0.0f, 2.0f);
    poster.setRotation(0.5f);

    setGameState(GameState::Playing);

    isRoadRollered = false;
}

World::~World() {
    delete camera;
    delete timing;
    delete shaderMngt;
}

void World::setGameState(GameState gs) {
    currState = gs;

    switch (currState) {
        case GameState::Playing: {
            // TODO: Grab the game's current resolution from the Config class.
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
    switch (currState) {
        case GameState::Playing: {
            updatePlaying();
        } break;
    }

    shaderMngt->update(camera);
}

void World::draw() {
    switch (currState) {
        case GameState::Playing: {
            drawPlaying();
        } break;
    }

    graphics->swap(false);
}

void World::updatePlaying() {
    // TODO: Grab the game's current resolution from the Config class.
    int centerX = graphics->getWindow()->getWidth() / 2;
    int centerY = graphics->getWindow()->getHeight() / 2;

    // TODO: Sensitivity from Config class.
    float mouseXDiff = (float)(io->getMousePosition().x - centerX) / 300.f;
    float mouseYDiff = (float)(io->getMousePosition().y - centerY) / 300.f;

    camera->addAngle(mouseXDiff, mouseYDiff);
    camera->update();

    // Update the view matrix for the shaders.
    shaderMngt->update(camera);

    // Reset mouse to center.
    io->setMousePosition(PGE::Vector2i(centerX, centerY));

    poster.setRotation((float)timing->getTotalElapsedTime());
}

void World::drawPlaying() {
    poster.render();
}

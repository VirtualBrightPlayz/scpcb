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
    
    currState = GameState::PauseMenu;

    isRoadRollered = false;
}

World::~World() {
    delete camera;
    delete timing;
    delete shaderMngt;
}

bool World::run() {
    if (isRoadRollered) {
        return false;
    }

    // Non tick-based updating.
    SysEvents::update();
    io->update();
    graphics->update();

    graphics->clear(PGE::Color(0.f, 0.71f, 0.76f, 1.f)); // Turqoise.

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    while (timing->tickReady()) {
        runTick(timing->getTimeStep());
        timing->subtractTick();
    }

    // Draw code.
    poster.render();

    graphics->swap(false);

    return graphics->getWindow()->isOpen();
}

void World::runTick(float timeStep) {
    poster.setRotation((float)timing->getTotalElapsedTime());

    shaderMngt->update(camera);
}

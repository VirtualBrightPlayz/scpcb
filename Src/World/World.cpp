#include <iostream>
#include <cmath>

#include "World.h"
#include "Timing.h"
#include "ShaderManager.h"

World::World() {
    graphics = Graphics::create("SCP - Containment Breach", 1280, 720, false);
    io = IO::create(graphics->getWindow());
    timing = new Timing(60);
    
    shaderMngt = new ShaderManager(graphics);

    poster = Sprite::create(graphics, shaderMngt->getSpriteShader(), "GFX/079pics/face.jpg");
    poster.setPosition(0, 0, 4.0f);

    ticks = 0;

    isRoadRollered = false;
}

World::~World() {
    delete timing;
    delete shaderMngt;
}

bool World::run() {
    if (isRoadRollered) {
        return false;
    }

    SysEvents::update();
    io->update();
    graphics->update();

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    while (timing->tickReady()) {
        runTick();
        timing->subtractTick();
    }

    poster.render();

    graphics->swap(false);

    return graphics->getWindow()->isOpen();
}

void World::runTick() {
    ticks++;
    if (ticks % 60 == 0) {
        std::cout << "A second has passed." << std::endl;
    }

    if (ticks == 600) {
        std::cout << "Goodbye.";
        isRoadRollered = true;
    }

    poster.setScale(std::sinf((float)timing->getTotalElapsedTime()));
}

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
    camera = new Camera(graphics);

    timing = new Timing(60);

    shaderMngt = new ShaderManager(graphics, camera);

    poster = Sprite::create(graphics, shaderMngt->getSpriteShader(), "GFX/079pics/face.jpg");
//    poster.setPosition(0, 0, 5.0f);
    poster.setScale(0.005f);

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


    graphics->clear(PGE::Color(0.f, 0.f, 0.f, 1.f));

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    while (timing->tickReady()) {
        runTick();
        timing->subtractTick();
    }

    // Draw code.
    poster.render();

    graphics->swap(false);

    return graphics->getWindow()->isOpen();
}

void World::runTick() {
    poster.setPosition(5.0f * std::sinf((float)timing->getTotalElapsedTime()), 5.0f * std::sinf((float)timing->getTotalElapsedTime()), 0.0f);

    shaderMngt->update(camera);
}

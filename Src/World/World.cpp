#include <iostream>

#include "World.h"
#include "Timing.h"

World::World() {
    graphics = CBN::Graphics::create("SCP - Containment Breach", 1280, 720, false);
    io = CBN::IO::create(graphics->getWindow());
    timing = new Timing(60);
    
    ticks = 0;
    
    isRoadRollered = false;
}

World::~World() {
    delete timing;
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
}

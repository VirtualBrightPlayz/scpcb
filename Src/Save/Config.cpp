#include "Config.h"

void Config::initialize() {
    // TODO: Find and detect config file, otherwise use the generated default.
}

Config::Config() {
    windowType = WindowType::Windowed;
    setResolution(800, 600);
}

void Config::setResolution(int width, int height) {
    this->width = width;
    this->height = height;
    aspectRatio = (float)width / height;
}

float Config::getAspectRatio() const {
    return aspectRatio;
}

#include "Config.h"

Config config = Config();

void Config::initialize() {
    // TODO: Find and detect config file, otherwise use the generated default.
}

Config::Config() {
    windowType = WindowType::Windowed;
    setResolution(800, 600);

    vsync = false;
}

void Config::setResolution(int width, int height) {
    this->width = width;
    this->height = height;
    aspectRatio = (float)width / height;
}

int Config::getWidth() const {
    return width;
}

int Config::getHeight() const {
    return height;
}

float Config::getAspectRatio() const {
    return aspectRatio;
}

float Config::isVsync() const {
    return vsync;
}

#include "Config.h"

Config::Config(PGE::FileName optionsFile) {
    windowType = WindowType::Windowed;
    setResolution(1280, 720);

    vsync = false;
}

Config::Config(const Config& cpy) {
    windowType = cpy.windowType;
    setResolution(cpy.width, cpy.height);

    vsync = cpy.vsync;
}

Config& Config::operator=(const Config& other) {
    if (this != &other) {
        windowType = other.windowType;
        setResolution(other.width, other.height);

        vsync = other.vsync;
    }

    return *this;
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

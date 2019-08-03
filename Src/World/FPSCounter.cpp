#include "FPSCounter.h"

#include "../Graphics/Font.h"
#include "../Save/Config.h"

FPSCounter::FPSCounter(Font* font, Config* con) {
    avgFPS = 0.0;
    currFPS = 0.0;
    visible = false;
    this->font = font;
    this->config = con;
}

void FPSCounter::update(double elapsedSeconds) {
    currFPS = 1.0 / elapsedSeconds;
    sampleBuffer.push_back(currFPS);

    if (sampleBuffer.size() > SAMPLE_COUNT) {
        sampleBuffer.pop_front();
    }

    double sum = 0.0;
    for (int i = 0; i < (int)sampleBuffer.size(); i++) {
        sum += sampleBuffer[i];
    }
    avgFPS = sum / sampleBuffer.size();
}

void FPSCounter::draw() {
    if (!visible) { return; }

    float x = -50.f;
}
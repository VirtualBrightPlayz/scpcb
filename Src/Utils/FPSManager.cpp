#include "FPSManager.h"

#include "../Graphics/Font.h"
#include "../Save/Config.h"

FPSManager::FPSManager() {
    avgFPS = 0.0;
    currFPS = 0.0;
}

void FPSManager::update(double elapsedSeconds) {
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

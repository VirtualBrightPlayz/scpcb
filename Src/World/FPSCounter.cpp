#include "FPSCounter.h"

#include "../Graphics/Font.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIText.h"

FPSCounter::FPSCounter(UIMesh* um, KeyBinds* kb, Config* con, Font* font) {
    avgFPS = 0.0;
    currFPS = 0.0;
    visible = false;
    this->config = con;

    display = new GUIText(um, kb, con, font, nullptr, 0.f, 0.f, false, false, Alignment::Left | Alignment::Top);
    display->setText("FPS: -1");
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
    display->setText(PGE::String("FPS: ") + (int)avgFPS);
}

void FPSCounter::draw() {
    if (!visible) { return; }

    display->render();
}

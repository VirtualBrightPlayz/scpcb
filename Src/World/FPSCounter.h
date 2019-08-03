#ifndef FPSCOUNTER_H_INCLUDED
#define FPSCOUNTER_H_INCLUDED

#include <queue>

class Font;
class Config;

class FPSCounter {
    private:
        std::deque<double> sampleBuffer;
        const int SAMPLE_COUNT = 100;

        double avgFPS;
        double currFPS;

        Font* font;
        Config* config;

    public:
        bool visible;

        FPSCounter(Font* font, Config* con);

        void update(double elapsedSeconds);
        void draw();
};

#endif // FPSCOUNTER_H_INCLUDED
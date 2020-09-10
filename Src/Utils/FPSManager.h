#ifndef FPSManager_H_INCLUDED
#define FPSManager_H_INCLUDED

#include <queue>

class FPSManager {
    private:
        std::deque<double> sampleBuffer;
        const int SAMPLE_COUNT = 20;

        double avgFPS;
        double currFPS;

    public:
        FPSManager();

        void update(double elapsedSeconds);
};

#endif // FPSManager_H_INCLUDED

#ifndef Timing_H_INCLUDED
#define Timing_H_INCLUDED

#include <chrono>

class Timing {
private:
    // The maximum amount of time the accumulator can store.
    const double MAX_ACCUMULATED_SECONDS = 3.0;

    double timeStep;
    double accumulatedSeconds;

    // Total time since the object's initialization.
    std::chrono::high_resolution_clock::time_point initialTime;
    // Previous time when the last call to getElapsedSeconds() was made.
    std::chrono::high_resolution_clock::time_point prevTime;

public:
    Timing(int tickrate);
    ~Timing()=default;

    double getTimeStep() const;
    void addSecondsToAccumulator(double seconds);

    // Returns whether enough time is left on the accumulator for another tick.
    bool tickReady();
    // Subtracts one tick from the accumlator.
    void subtractTick();
    // Returns the elapsed seconds since the last call to this function.
    double getElapsedSeconds();
    // Returns the total elapsed time since the object's creation.
    double getTotalElapsedTime();
};

#endif // Timing_H_INCLUDED

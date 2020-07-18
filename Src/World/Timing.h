#ifndef TIMING_H_INCLUDED
#define TIMING_H_INCLUDED

#include <chrono>

class Timing {
private:
    // The maximum amount of time the accumulator can store.
    const double MAX_ACCUMULATED_SECONDS = 3.0;

    double timeStep;
    double accumulatedSeconds;

    double secondToLastFixedTime;
    double lastFixedTime;

    /// <summary>
    /// Total time since the object's initialization.
    /// </summary>
    std::chrono::high_resolution_clock::time_point initialTime;
    /// <summary>
    /// Previous time when the last call to getElapsedSeconds() was made.
    /// </summary>
    std::chrono::high_resolution_clock::time_point prevTime;

public:
    Timing(int tickrate);
    ~Timing()=default;

    double getTimeStep() const;
    void addSecondsToAccumulator(double secondsPassed);

    /// <summary>
    /// Returns whether enough time is left on the accumulator for another tick.
    /// </summary>
    bool tickReady() const;

    /// <summary>
    /// Subtracts one tick from the accumlator.
    /// </summary>
    void subtractTick();

    void updateInterpolationFactor();

    double getInterpolationFactor() const;

    /// <summary>
    /// Returns the total elapsed time since the object's creation.
    /// </summary>
    double getTotalElapsedTime() const;

    /// <summary>
    /// Returns the elapsed seconds since the last call to this function.
    /// </summary>
    double getElapsedSeconds();
};

#endif // TIMING_H_INCLUDED

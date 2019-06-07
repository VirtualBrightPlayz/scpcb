#ifndef Timing_H_INCLUDED
#define Timing_H_INCLUDED

#include <chrono>

class Timing {
private:
    double timeStep;
    double accumulatedSeconds;
    
    std::chrono::high_resolution_clock::time_point initialTime;
    
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
};

#endif // Timing_H_INCLUDED

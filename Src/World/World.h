#ifndef World_H_INCLUDED
#define World_H_INCLUDED

#include "../Wrap/Graphics.h"
#include "../Wrap/IO.h"

class Timing;

class World {
private:
    Graphics graphics;
    IO io;
    Timing* timing;
    int ticks;

    bool isRoadRollered;

public:
    World();
    ~World();

    bool run();
    void runTick();
};

#endif // World_H_INCLUDED

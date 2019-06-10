#ifndef World_H_INCLUDED
#define World_H_INCLUDED

#include "../Wrap/Graphics.h"
#include "../Wrap/IO.h"
#include "../Graphics/Sprite.h"

class Timing;
class ShaderManager;

class World {
private:
    Graphics graphics;
    IO io;
    Timing* timing;
    int ticks;

    // TODO: Remove, this is a test.
    Sprite poster;

    ShaderManager* shaderMngt;

    bool isRoadRollered;

public:
    World();
    ~World();

    bool run();
    void runTick();
};

#endif // World_H_INCLUDED

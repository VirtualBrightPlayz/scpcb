#ifndef World_H_INCLUDED
#define World_H_INCLUDED

#include "../Wrap/Graphics.h"
#include "../Wrap/IO.h"
#include "../Graphics/Sprite.h"

class Camera;
class Timing;
class ShaderManager;

class World {
private:
    Graphics graphics;
    IO io;
    Camera* camera;

    Timing* timing;

    // TODO: Remove, this is a test.
    Sprite poster;

    ShaderManager* shaderMngt;

    bool isRoadRollered;

public:
    World();
    ~World();

    // Goes through the update/draw phrase of a single frame.
    bool run();
    // Goes through the update phase of a single tick.
    void runTick();
};

#endif // World_H_INCLUDED

#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED


#include "../Input/Input.h"

class ScriptManager;
class MathDefinitions;
class RM2Definitions;
class EventDefinition;
class ScriptModule;
class GraphicsResources;
class Config;

class ScriptWorld {
private:
    ScriptManager* manager;

    MathDefinitions* mathDefinitions;
    RM2Definitions* rm2Definitions;
    EventDefinition* perTickEventDefinition;
    EventDefinition* perFrameEventDefinition;

    std::vector<ScriptModule*> modules;

public:
    ScriptWorld(GraphicsResources* gfxRes, const Config* config, float timestep);
    ~ScriptWorld();

    void update(Input input);
    void draw(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

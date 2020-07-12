#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

class ScriptManager;
class GeneralDefinitions;
class MathDefinitions;
class RM2Definitions;
class CollisionDefinitions;
class PlayerControllerDefinitions;
class InputDefinitions;
class EventDefinition;
class ScriptModule;
class GraphicsResources;
class KeyBinds;
class Config;
class Camera;
class RefCounterManager;

class ScriptWorld {
private:
    ScriptManager* manager;

    RefCounterManager* refCounterManager;

    GeneralDefinitions* generalDefinitions;
    InputDefinitions* inputDefinitions;
    MathDefinitions* mathDefinitions;
    RM2Definitions* rm2Definitions;
    CollisionDefinitions* collisionDefinitions;
    PlayerControllerDefinitions* playerControllerDefinitions;

    EventDefinition* perTickEventDefinition;
    EventDefinition* perFrameEventDefinition;

    std::vector<ScriptModule*> modules;

public:
    ScriptWorld(GraphicsResources* gfxRes, Camera* camera, KeyBinds* keyBinds, const Config* config, float timestep);
    ~ScriptWorld();

    void update();
    void draw(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

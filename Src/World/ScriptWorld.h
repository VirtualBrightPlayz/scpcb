#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

class ScriptManager;

class BillboardDefinitions;
class ConsoleDefinition;
class MathDefinitions;
class RM2Definitions;
class CollisionDefinitions;
class PlayerControllerDefinitions;
class InputDefinitions;
class EventDefinition;
class ReflectionDefinitions;

class ScriptModule;
class GraphicsResources;
class KeyBinds;
class Config;
class Camera;
class Console;
class BillboardManager;
class RefCounterManager;

class ScriptWorld {
private:
    ScriptManager* manager;

    RefCounterManager* refCounterManager;

    ConsoleDefinition* consoleDefinition;
    InputDefinitions* inputDefinitions;
    MathDefinitions* mathDefinitions;
    BillboardDefinitions* billboardDefinitions;
    RM2Definitions* rm2Definitions;
    CollisionDefinitions* collisionDefinitions;
    PlayerControllerDefinitions* playerControllerDefinitions;
    ReflectionDefinitions* reflectionDefinitions;

    EventDefinition* perTickEventDefinition;
    EventDefinition* perFrameEventDefinition;

    std::vector<ScriptModule*> modules;

public:
    ScriptWorld(GraphicsResources* gfxRes, Camera* camera, KeyBinds* keyBinds, const Config* config, float timestep, Console* con, BillboardManager* bm);
    ~ScriptWorld();

    void update();
    void draw(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

class ScriptManager;
class MathDefinitions;
class RM2Definitions;
class CollisionDefinitions;
class PlayerControllerDefinitions;
class EventDefinition;
class ScriptModule;
class GraphicsResources;
class Config;
class Camera;

class ScriptWorld {
private:
    ScriptManager* manager;

    MathDefinitions* mathDefinitions;
    RM2Definitions* rm2Definitions;
    CollisionDefinitions* collisionDefinitions;
    PlayerControllerDefinitions* playerControllerDefinitions;

    EventDefinition* perTickEventDefinition;
    EventDefinition* perFrameEventDefinition;

    std::vector<ScriptModule*> modules;

public:
    ScriptWorld(GraphicsResources* gfxRes, Camera* camera, const Config* config, float timestep);
    ~ScriptWorld();

    void update();
    void draw(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

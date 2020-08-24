#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

class ScriptManager;

class WorldDefinitions;
class BillboardDefinitions;
class ConsoleDefinitions;
class ColorDefinitions;
class MathDefinitions;
class UIDefinitions;
class MessageDefinitions;
class LocalizationDefinitions;
class ModelDefinitions;
class RM2Definitions;
class CollisionDefinitions;
class PickableDefinitions;
class PlayerControllerDefinitions;
class InputDefinitions;
class EventDefinition;
class ReflectionDefinitions;

class ScriptModule;
class World;
class GraphicsResources;
class KeyBinds;
class MouseData;
class Config;
class Camera;
class Console;
class UIMesh;
class MessageManager;
class LocalizationManager;
class PickableManager;
class BillboardManager;
class RefCounterManager;

class ScriptWorld {
    private:
        ScriptManager* manager;

        RefCounterManager* refCounterManager;

        WorldDefinitions* worldDefinitions;
        ConsoleDefinitions* consoleDefinitions;
        InputDefinitions* inputDefinitions;
        ColorDefinitions* colorDefinitions;
        MathDefinitions* mathDefinitions;
        UIDefinitions* uiDefinitions;
        MessageDefinitions* messageDefinitions;
        LocalizationDefinitions* localizationDefinitions;
        BillboardDefinitions* billboardDefinitions;
        ModelDefinitions* modelDefinitions;
        RM2Definitions* rm2Definitions;
        CollisionDefinitions* collisionDefinitions;
        PickableDefinitions* pickableDefinitions;
        PlayerControllerDefinitions* playerControllerDefinitions;
        ReflectionDefinitions* reflectionDefinitions;

        EventDefinition* perTickEventDefinition;
        EventDefinition* perFrameGameEventDefinition;
        EventDefinition* perFrameMenuEventDefinition;

        std::vector<ScriptModule*> modules;

    public:
        ScriptWorld(World* world, GraphicsResources* gfxRes, Camera* camera, KeyBinds* keyBinds, MouseData* mouseData, MessageManager* mm, LocalizationManager* lm, PickableManager* pm, UIMesh* um,  Config* config, float timestep, Console* con, BillboardManager* bm);
        ~ScriptWorld();

        void update();
        void drawGame(float interpolation);
        void drawMenu(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

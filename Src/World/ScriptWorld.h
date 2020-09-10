#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

#include <IO/IO.h>

class ScriptManager;

class WorldDefinitions;
class RegexDefinitions;
class MementoDefinitions;
class BillboardDefinitions;
class ConsoleDefinitions;
class ColorDefinitions;
class MathDefinitions;
class TextureDefinitions;
class UIDefinitions;
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
class UIMesh;
class LocalizationManager;
class PickableManager;
class BillboardManager;
class RefCounterManager;

class ScriptWorld {
    private:
        ScriptManager* manager;

        RefCounterManager* refCounterManager;

        WorldDefinitions* worldDefinitions;
        RegexDefinitions* regexDefinitions;
        MementoDefinitions* mementoDefinitions;
        ConsoleDefinitions* consoleDefinitions;
        InputDefinitions* inputDefinitions;
        ColorDefinitions* colorDefinitions;
        MathDefinitions* mathDefinitions;
        TextureDefinitions* textureDefinitions;
        UIDefinitions* uiDefinitions;
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
        ScriptWorld(World* world, GraphicsResources* gfxRes, Camera* camera, KeyBinds* keyBinds, MouseData* mouseData, PGE::IO* io, LocalizationManager* lm, PickableManager* pm, UIMesh* um,  Config* config, float timestep, BillboardManager* bm);
        ~ScriptWorld();

        void update();
        void drawGame(float interpolation);
        void drawMenu(float interpolation);

};

#endif // SCRIPTWORLD_H_INCLUDED

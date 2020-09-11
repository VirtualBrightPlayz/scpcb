#ifndef SCRIPTWORLD_H_INCLUDED
#define SCRIPTWORLD_H_INCLUDED

#include <IO/IO.h>

class ScriptManager;

class EventDefinition;
class NativeDefinition;

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

        std::vector<NativeDefinition*> nativeDefs;

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

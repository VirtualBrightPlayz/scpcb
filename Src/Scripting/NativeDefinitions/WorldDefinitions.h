#ifndef WORLDDEFINITIONS_H_INCLUDED
#define WORLDDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class World;

class WorldDefinitions : NativeDefinition {
    private:
        const int platform;

    public:
        WorldDefinitions(ScriptManager* mgr, World* w);
};

#endif // WORLDDEFINITIONS_H_INCLUDED

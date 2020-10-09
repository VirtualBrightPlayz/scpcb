#ifndef UIDEFINITIONS_H_INCLUDED
#define UIDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class UIMesh;
class Config;
class World;

class UIDefinitions : public NativeDefinition {
    public:
        UIDefinitions(ScriptManager* mgr, UIMesh* uiMesh, Config* config, World* world);
};

#endif // UIDEFINITIONS_H_INCLUDED

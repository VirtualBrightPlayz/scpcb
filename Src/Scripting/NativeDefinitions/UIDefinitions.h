#ifndef UIDEFINITIONS_H_INCLUDED
#define UIDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class UIMesh;

class UIDefinitions : NativeDefinition {
    public:
        UIDefinitions(ScriptManager* mgr, UIMesh* uiMesh);
};

#endif

#ifndef MODELDEFINITIONS_H_INCLUDED
#define MODELDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class GraphicsResources;

class ModelDefinitions : public NativeDefinition {
    public:
        ModelDefinitions(ScriptManager* mgr, GraphicsResources* gr);
};

#endif // MODELDEFINITIONS_H_INCLUDED

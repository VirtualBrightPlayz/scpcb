#ifndef MODELIMAGEGENERATORDEFINITIONS_H_INCLUDED
#define MODELIMAGEGENERATORDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class ModelImageGenerator;

class ModelImageGeneratorDefinitions : public NativeDefinition {
    public:
        ModelImageGeneratorDefinitions(ScriptManager* mgr, ModelImageGenerator* lm);
};

#endif // MODELIMAGEGENERATORDEFINITIONS_H_INCLUDED

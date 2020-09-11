#ifndef LOCALIZATIONDEFINITIONS_H_INCLUDED
#define LOCALIZATIONDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class LocalizationManager;

class LocalizationDefinitions : public NativeDefinition {
    public:
        LocalizationDefinitions(ScriptManager* mgr, LocalizationManager* lm);
};

#endif // LOCALIZATIONSDEFINITIONS_H_INCLUDED

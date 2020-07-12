#ifndef GENERALDEFINITIONS_H_INCLUDED
#define GENERALDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class GeneralDefinitions : public NativeDefinition {
    private:
        void Log(PGE::String content);
        void Err(PGE::String content);
    public:
        GeneralDefinitions(ScriptManager* mgr);
};

#endif

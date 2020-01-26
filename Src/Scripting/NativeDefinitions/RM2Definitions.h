#ifndef RM2DEFINITIONS_H_INCLUDED
#define RM2DEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"
#include <Misc/String.h>
#include "../../Models/RM2.h"

class RM2Definitions : public NativeDefinition {
    private:
        void loadRM2(PGE::String filename);
    public:
        void registerToEngine(ScriptManager* mgr) override;
        void cleanup() override;
};

#endif

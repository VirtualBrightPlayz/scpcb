#ifndef NATIVEDEFINITION_H_INCLUDED
#define NATIVEDEFINITION_H_INCLUDED

class ScriptManager;

class NativeDefinition {
    public:
        virtual void registerToEngine(ScriptManager* mgr) = 0;
        virtual void cleanup() = 0;
};

#endif

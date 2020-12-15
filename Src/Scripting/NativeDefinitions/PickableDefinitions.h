#ifndef PICKABLEDEFINITIONS_H_INCLUDED
#define PICKABLEDEFINITIONS_H_INCLUDED

#include "../RefCounter.h"

class ScriptManager;
class Pickable;
class PickableManager;

class PickableDefinitions : public RefCounter {
    public:
        PickableDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr, PickableManager* pm);

    private:
        std::map<Pickable*, int> refCount;
        Pickable* pickableFactory();
        void addRef(void* ptr) override;
        void release(void* ptr) override;
};

#endif // PICKABLEDEFINITIONS_H_INCLUDED

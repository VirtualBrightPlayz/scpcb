#ifndef RANDOMDEFINITIONS_H_INCLUDED
#define RANDOMDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"
#include "../RefCounter.h"

#include <Math/Random.h>

class ScriptManager;

class RandomDefinitions : public RefCounter {
    public:
        RandomDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr);
        ~RandomDefinitions();

    private:
        PGE::Random* genericRandom;

        std::map<PGE::Random*, int> refCount;
        PGE::Random* randomFactory();
        PGE::Random* randomFactorySeeded(uint64_t seed);
        void addRef(void* ptr) override;
        void release(void* ptr) override;
};

#endif // RANDOMDEFINITIONS_H_INCLUDED

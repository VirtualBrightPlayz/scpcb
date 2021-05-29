#ifndef REGEXDEFINITIONS_H_INCLUDED
#define REGEXDEFINITIONS_H_INCLUDED

#include <regex>

#include <PGE/String/String.h>

#include "../RefCounter.h"
#include "../NativeDefinition.h"

class ScriptManager;

class RegexDefinitions : public RefCounter {
    private:
        std::map<std::regex*,int> refCount;
        std::regex* regexFactory(const PGE::String& regex);
        void addRef(void* ptr) override;
        void release(void* ptr) override;
    
    public:
        RegexDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr);
};

#endif // REGEXDEFINITIONS_H_INCLUDED
#ifndef STRINGFACTORY_H_INCLUDED
#define STRINGFACTORY_H_INCLUDED

#include <angelscript.h>
#include <map>
#include <Misc/String.h>

struct StringPoolEntry {
    PGE::String str;
    int refCount;

    StringPoolEntry() { refCount = 0; }

    StringPoolEntry(const PGE::String& s) {
        str = s;
        refCount = 0;
    }
};

class StringFactory : public asIStringFactory {
    private:
        std::map<long long, StringPoolEntry> strPool;
    public:
        StringFactory(asIScriptEngine* engine);
        const void* GetStringConstant(const char* data, asUINT length);
        int ReleaseStringConstant(const void* str);
        int GetRawStringData(const void* str, char* data, asUINT* length) const;
};

#endif

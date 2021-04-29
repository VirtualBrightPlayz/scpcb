#ifndef STRINGFACTORY_H_INCLUDED
#define STRINGFACTORY_H_INCLUDED

#include <map>

#include <angelscript.h>

#include <String/String.h>

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
        std::map<uint64_t, StringPoolEntry> strPool;

    public:
        StringFactory(asIScriptEngine* engine);
        const void* GetStringConstant(const char* data, asUINT length);
        int ReleaseStringConstant(const void* str);
        int GetRawStringData(const void* str, char* data, asUINT* length) const;
};

#endif // STRINGFACTORY_H_INCLUDED

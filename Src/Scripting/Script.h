#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <angelscript.h>
#include <Misc/FileName.h>
#include <Misc/String.h>
#include <map>
#include <inttypes.h>
#include "Type.h"

class ScriptFunction;
class ScriptClass;

class Script {
    private:
        PGE::String scriptContents;
        PGE::FileName fileName;
    public:
        Script(const PGE::FileName& fn);
        ~Script();

        PGE::FileName getFileName() const;
        PGE::String getScriptContents() const;
};

#endif

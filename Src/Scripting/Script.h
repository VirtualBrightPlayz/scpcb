#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <angelscript.h>
#include <Misc/FilePath.h>
#include <Misc/String.h>
#include <map>
#include <inttypes.h>

class ScriptFunction;
class ScriptClass;

class Script {
    private:
        PGE::String scriptContents;
        PGE::FilePath fileName;
    public:
        Script(const PGE::FilePath& fn);
        ~Script();

        PGE::FilePath getFileName() const;
        PGE::String getScriptContents() const;
};

#endif

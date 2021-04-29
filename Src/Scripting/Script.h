#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <map>

#include <angelscript.h>

#include <Misc/FilePath.h>
#include <String/String.h>

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

#endif // SCRIPT_H_INCLUDED

#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <map>

#include <angelscript.h>

#include <PGE/File/FilePath.h>
#include <PGE/String/String.h>

class ScriptFunction;
class ScriptClass;

// TODO: Probably turn this into a struct.
class Script {
    private:
        PGE::String scriptContents;
        PGE::FilePath fileName;

    public:
        Script(const PGE::FilePath& fn);

        PGE::FilePath getFileName() const;
        PGE::String getScriptContents() const;
};

#endif // SCRIPT_H_INCLUDED

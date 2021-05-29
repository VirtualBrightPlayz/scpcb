#include "Script.h"

#include <PGE/Exception/Exception.h>

#include "ScriptManager.h"
#include "ScriptClass.h"
#include "ScriptFunction.h"
#include "ScriptModule.h"

#include <fstream>

Script::Script(const PGE::FilePath& fn) {
    fileName = fn;
    scriptContents = fileName.read();
}

PGE::FilePath Script::getFileName() const {
    return fileName;
}

PGE::String Script::getScriptContents() const {
    return scriptContents;
}

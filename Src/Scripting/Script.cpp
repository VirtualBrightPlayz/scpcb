#include "Script.h"

#include "ScriptManager.h"
#include "ScriptClass.h"
#include "ScriptFunction.h"
#include "ScriptModule.h"

#include <fstream>

Script::Script(const PGE::FilePath& fn) {
    fileName = fn;

    scriptContents = "";

    char* readBuf = new char[1024];

    std::ifstream inFile;
    inFile.open(fileName.cstr(), std::ios_base::in);

    if (!inFile.good()) {
        throw std::runtime_error((PGE::String("Could not find script: \"") + fileName.str() + "\"").cstr());
    }

    bool reachedEndOfFile = inFile.eof();
    while (!reachedEndOfFile) {
        inFile.read(readBuf,1023);
        readBuf[inFile.gcount()]='\0';
        scriptContents = PGE::String(scriptContents,readBuf);

        reachedEndOfFile = inFile.eof();
    }

    inFile.close();

    delete[] readBuf;
}

PGE::FilePath Script::getFileName() const {
    return fileName;
}

PGE::String Script::getScriptContents() const {
    return scriptContents;
}

Script::~Script() {
    //do nothing
}


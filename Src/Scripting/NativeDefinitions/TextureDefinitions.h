#ifndef TEXTUREDEFINITIONS_H_INCLUDED
#define TEXTUREDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class GraphicsResources;

class TextureDefinitions : public NativeDefinition {
    public:
        TextureDefinitions(ScriptManager* mgr, GraphicsResources* gr);
};

#endif // TEXTUREDEFINITIONS_H_INCLUDED

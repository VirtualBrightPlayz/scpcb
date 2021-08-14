#include "TextureDefinitions.h"

#include "../ScriptManager.h"

#include "../../Graphics/GraphicsResources.h"

TextureDefinitions::TextureDefinitions(ScriptManager* mgr, GraphicsResources* gr) {
    engine = mgr->getAngelScriptEngine();

    engine->RegisterObjectType("Texture", sizeof(PGE::Texture), asOBJ_REF | asOBJ_NOCOUNT);

    engine->SetDefaultNamespace("Texture");
    engine->RegisterGlobalFunction("Texture@ get(const string&in filename)", asMETHOD(GraphicsResources, getTexture), asCALL_THISCALL_ASGLOBAL, gr);
    engine->RegisterGlobalFunction("void drop(Texture@ texture)", asMETHOD(GraphicsResources, dropTexture), asCALL_THISCALL_ASGLOBAL, gr);
}

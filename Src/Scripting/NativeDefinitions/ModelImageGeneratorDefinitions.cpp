#include "ModelImageGeneratorDefinitions.h"

#include "../ScriptManager.h"

#include "../../Graphics/ModelImageGenerator.h"

ModelImageGeneratorDefinitions::ModelImageGeneratorDefinitions(ScriptManager* mgr, ModelImageGenerator* lm) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("ModelImageGenerator");
    engine->RegisterGlobalFunction("void initialize(int texSize)", asMETHOD(ModelImageGenerator, initialize), asCALL_THISCALL_ASGLOBAL, lm);
    engine->RegisterGlobalFunction("void deinitialize()", asMETHOD(ModelImageGenerator, deinitialize), asCALL_THISCALL_ASGLOBAL, lm);
    engine->RegisterGlobalFunction("bool getInitialized()", asMETHOD(ModelImageGenerator, getInitialized), asCALL_THISCALL_ASGLOBAL, lm);
    engine->RegisterGlobalFunction("Texture@ generate(const string&in model, float scale, const Vector3f&in rotation, Vector2f position)", asMETHOD(ModelImageGenerator, generate), asCALL_THISCALL_ASGLOBAL, lm);
}

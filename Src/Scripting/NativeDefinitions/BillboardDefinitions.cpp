#include "BillboardDefinitions.h"

#include "../../Graphics/Billboard.h"

BillboardDefinitions::BillboardDefinitions(ScriptManager* mgr, BillboardManager* bm) {
    engine = mgr->getAngelScriptEngine();
    this->bm = bm;

    engine->RegisterObjectType("Billboard", sizeof(Billboard), asOBJ_REF | asOBJ_NOCOUNT);

    engine->SetDefaultNamespace("Billboard");
    engine->RegisterGlobalFunction("Billboard@ create(string textureName, const Vector3f&in pos, float rotation, const Vector2f&in scale=Vector2f(1.0, 1.0), const Color&in color=Color(1.0, 1.0, 1.0))", asMETHOD(BillboardDefinitions, createBillboardFacingCamera), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("Billboard@ create(string textureName, const Vector3f&in pos, const Vector3f&in rotation, const Vector2f&in scale=Vector2f(1.0, 1.0), const Color&in color=Color(1.0, 1.0, 1.0))", asMETHOD(BillboardDefinitions, createBillboardArbitraryRotation), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void destroy(Billboard@ b)", asMETHOD(BillboardDefinitions, destroyBillboard), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void renderAll()", asMETHOD(BillboardManager, render), asCALL_THISCALL_ASGLOBAL, bm);
}

Billboard* BillboardDefinitions::createBillboardFacingCamera(const PGE::String& textureName, const PGE::Vector3f& pos, float rotation, const PGE::Vector2f& scale, const PGE::Color& color) {
    return new Billboard(bm, textureName, pos, rotation, scale, color);
}

Billboard* BillboardDefinitions::createBillboardArbitraryRotation(const PGE::String& textureName, const PGE::Vector3f& pos, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color) {
    return new Billboard(bm, textureName, pos, rotation, scale, color);
}

void BillboardDefinitions::destroyBillboard(Billboard* b) {
    delete b;
}

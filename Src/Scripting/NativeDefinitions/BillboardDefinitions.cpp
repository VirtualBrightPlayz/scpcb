#include "BillboardDefinitions.h"

#include "../../Graphics/Billboard.h"

BillboardDefinitions::BillboardDefinitions(ScriptManager* mgr, BillboardManager* bm) {
    this->bm = bm;
    engine = mgr->getAngelScriptEngine();

    registerClass<Billboard>("Billboard");
    registerClass<RotatedBillboard>("RotatedBillboard");
    engine->RegisterObjectProperty("RotatedBillboard", "Vector3f rotation", asOFFSET(RotatedBillboard, rotation));

    engine->RegisterObjectInheritance<Billboard, RotatedBillboard>("Billboard", "RotatedBillboard");

    engine->SetDefaultNamespace("Billboard");
    engine->RegisterGlobalFunction("Billboard@ create(const Vector3f&in pos, string textureName, const Vector3f&in scale)", asMETHOD(BillboardDefinitions, createBillboard), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("RotatedBillboard@ createRotated(const Vector3f&in pos, string textureName, const Vector3f&in rotation, const Vector3f&in scale)", asMETHOD(BillboardDefinitions, createBillboardRotated), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void destroy(Billboard@ b)", asMETHOD(BillboardDefinitions, destroyBillboard), asCALL_THISCALL_ASGLOBAL, this);
}

template<class T>
void BillboardDefinitions::registerClass(const char* className) {
    engine->RegisterObjectType(className, sizeof(T), asOBJ_REF | asOBJ_NOCOUNT);

    engine->RegisterObjectProperty(className, "Vector3f pos", asOFFSET(T, pos));
    // TODO: Replace with Vector2f.
    engine->RegisterObjectProperty(className, "Vector3f scale", asOFFSET(T, scale));
    // TODO: Add color.

    // TODO: Move transformation to shader.
    engine->RegisterObjectMethod(className, "void render(const Matrix4x4f&in matrix)", asMETHOD(T, render), asCALL_THISCALL);
}

Billboard* BillboardDefinitions::createBillboard(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& scale) {
    return new Billboard(bm, pos, textureName, PGE::Vector2f(scale.x, scale.y), PGE::Color::White);
}

RotatedBillboard* BillboardDefinitions::createBillboardRotated(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& rotation, const PGE::Vector3f& scale) {
    return new RotatedBillboard(bm, pos, textureName, rotation, PGE::Vector2f(scale.x, scale.y), PGE::Color::White);
}

void BillboardDefinitions::destroyBillboard(Billboard* b) {
    delete b;
}

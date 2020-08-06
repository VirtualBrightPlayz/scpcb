#include "BillboardDefinitions.h"

#include "../../Graphics/Billboard.h"

BillboardDefinitions::BillboardDefinitions(ScriptManager* mgr, BillboardManager* bm) {
    engine = mgr->getAngelScriptEngine();
    this->bm = bm;

    registerClass<Billboard>("Billboard");
    registerClass<RotatedBillboard>("RotatedBillboard");
    engine->RegisterObjectProperty("RotatedBillboard", "Vector3f rotation", asOFFSET(RotatedBillboard, rotation));

    registerInheritance<Billboard, RotatedBillboard>("Billboard", "RotatedBillboard");

    engine->SetDefaultNamespace("Billboard");
    engine->RegisterGlobalFunction("Billboard@ create(const Vector3f&in pos, string textureName, const Vector2f&in scale=Vector2f(1.0, 1.0), const Color&in color=Color(1.0, 1.0, 1.0))", asMETHOD(BillboardDefinitions, createBillboard), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("RotatedBillboard@ createRotated(const Vector3f&in pos, string textureName, const Vector3f&in rotation=Vector3f(), const Vector2f&in scale=Vector2f(1.0, 1.0), const Color&in color=Color(1.0, 1.0, 1.0))", asMETHOD(BillboardDefinitions, createBillboardRotated), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void destroy(Billboard@ b)", asMETHOD(BillboardDefinitions, destroyBillboard), asCALL_THISCALL_ASGLOBAL, this);
}

template<class T>
void BillboardDefinitions::registerClass(const char* className) {
    engine->RegisterObjectType(className, sizeof(T), asOBJ_REF | asOBJ_NOCOUNT);

    engine->RegisterObjectProperty(className, "Vector3f pos", asOFFSET(T, pos));
    engine->RegisterObjectProperty(className, "Vector3f scale", asOFFSET(T, scale));
    engine->RegisterObjectProperty(className, "Color color", asOFFSET(T, color));

    engine->RegisterObjectMethod(className, "void setTexture(string textureName)", asMETHOD(T, setTexture), asCALL_THISCALL);
    // TODO: Move transformation to shader.
    engine->RegisterObjectMethod(className, "void render(const Matrix4x4f&in matrix)", asMETHOD(T, render), asCALL_THISCALL);
}

Billboard* BillboardDefinitions::createBillboard(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector2f& scale, const PGE::Color& color) {
    return new Billboard(bm, pos, textureName, scale, color);
}

RotatedBillboard* BillboardDefinitions::createBillboardRotated(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color) {
    return new RotatedBillboard(bm, pos, textureName, rotation, scale, color);
}

void BillboardDefinitions::destroyBillboard(Billboard* b) {
    delete b;
}

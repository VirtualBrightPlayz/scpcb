#include "MathDefinitions.h"

void vector3fConstructor(void* memory) {
    new(memory) PGE::Vector3f();
}

void vector3fConstructorParametrized(float x, float y, float z, void* memory) {
    new(memory) PGE::Vector3f(x, y, z);
}

void vector3fDestructor(void* memory) {
    ((PGE::Vector3f*)memory)->~Vector3f();
}

void matrixConstructor(void* memory) {
    new(memory) PGE::Matrix4x4f();
}

void matrixConstructorParametrized(float aa, float ab, float ac, float ad,
                                   float ba, float bb, float bc, float bd,
                                   float ca, float cb, float cc, float cd,
                                   float da, float db, float dc, float dd, void* memory) {
    new(memory) PGE::Matrix4x4f(aa, ab, ac, ad,
                                ba, bb, bc, bd,
                                ca, cb, cc, cd,
                                da, db, dc, dd);
}

void matrixDestructor(void* memory) {
    ((PGE::Matrix4x4f*)memory)->~Matrix4x4f();
}

MathDefinitions::MathDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();

    //Vector3f
    engine->RegisterObjectType("Vector3f", sizeof(PGE::Vector3f), asOBJ_VALUE | asOBJ_APP_CLASS_ALLFLOATS | asGetTypeTraits<PGE::Vector3f>());
    engine->RegisterObjectBehaviour("Vector3f", asBEHAVE_CONSTRUCT, "void f()", asFUNCTION(vector3fConstructor), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("Vector3f", asBEHAVE_CONSTRUCT, "void f(float x, float y, float z)",
        asFUNCTION(vector3fConstructorParametrized), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("Vector3f", asBEHAVE_DESTRUCT, "void f()", asFUNCTION(vector3fDestructor), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectProperty("Vector3f", "float x", asOFFSET(PGE::Vector3f, x));
    engine->RegisterObjectProperty("Vector3f", "float y", asOFFSET(PGE::Vector3f, y));
    engine->RegisterObjectProperty("Vector3f", "float z", asOFFSET(PGE::Vector3f, z));

    engine->RegisterObjectMethod("Vector3f", "Vector3f& opAssign(const Vector3f &in other)", asMETHODPR(PGE::Vector3f, operator=, (const PGE::Vector3f&), PGE::Vector3f&), asCALL_THISCALL);

    engine->RegisterObjectMethod("Vector3f", "float lengthSquared() const", asMETHOD(PGE::Vector3f, lengthSquared), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "float length() const", asMETHOD(PGE::Vector3f, length), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "float distanceSquared(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, distanceSquared), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "float distance(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, distance), asCALL_THISCALL);

    engine->RegisterObjectMethod("Vector3f", "Vector3f add(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, add), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "Vector3f subtract(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, subtract), asCALL_THISCALL);

    engine->RegisterObjectMethod("Vector3f", "Vector3f multiply(float s) const", asMETHOD(PGE::Vector3f, multiply), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "Vector3f normalize() const", asMETHOD(PGE::Vector3f, normalize), asCALL_THISCALL);

    engine->RegisterObjectMethod("Vector3f", "Vector3f reflect(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, reflect), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "float dotProduct(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, dotProduct), asCALL_THISCALL);
    engine->RegisterObjectMethod("Vector3f", "Vector3f crossProduct(const Vector3f&in other) const", asMETHOD(PGE::Vector3f, crossProduct), asCALL_THISCALL);

    //Matrix4x4f
    engine->RegisterObjectType("Matrix4x4f", sizeof(PGE::Matrix4x4f), asOBJ_VALUE | asOBJ_APP_CLASS_ALLFLOATS | asGetTypeTraits<PGE::Matrix4x4f>());
    engine->RegisterObjectBehaviour("Matrix4x4f", asBEHAVE_CONSTRUCT, "void f()", asFUNCTION(matrixConstructor), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("Matrix4x4f", asBEHAVE_CONSTRUCT, "void f(float aa, float ab, float ac, float ad,"
                                                                             "float ba, float bb, float bc, float bd,"
                                                                             "float ca, float cb, float cc, float cd,"
                                                                             "float da, float db, float dc, float dd)",
        asFUNCTION(matrixConstructorParametrized), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("Matrix4x4f", asBEHAVE_DESTRUCT, "void f()", asFUNCTION(matrixDestructor), asCALL_CDECL_OBJLAST);

    engine->RegisterObjectMethod("Matrix4x4f", "Matrix4x4f& opAssign(const Matrix4x4f &in other)", asMETHODPR(PGE::Matrix4x4f, operator=, (const PGE::Matrix4x4f&), PGE::Matrix4x4f&), asCALL_THISCALL);

    engine->RegisterObjectMethod("Matrix4x4f", "Matrix4x4f transpose() const", asMETHOD(PGE::Matrix4x4f, transpose), asCALL_THISCALL);
    engine->RegisterObjectMethod("Matrix4x4f", "Matrix4x4f product(const Matrix4x4f&in other) const", asMETHOD(PGE::Matrix4x4f, product), asCALL_THISCALL);
    engine->RegisterObjectMethod("Matrix4x4f", "Vector3f transform(const Vector3f&in vec) const", asMETHODPR(PGE::Matrix4x4f, transform, (const PGE::Vector3f&) const, PGE::Vector3f), asCALL_THISCALL);

    engine->SetDefaultNamespace("Matrix4x4f");
    engine->RegisterGlobalFunction("Matrix4x4f translate(const Vector3f&in position)", asFUNCTION(PGE::Matrix4x4f::translate), asCALL_CDECL, this);
    engine->RegisterGlobalFunction("Matrix4x4f scale(const Vector3f&in scale)", asFUNCTION(PGE::Matrix4x4f::scale), asCALL_CDECL, this);
    engine->RegisterGlobalFunction("Matrix4x4f rotate(const Vector3f&in rotation)", asFUNCTION(PGE::Matrix4x4f::rotate), asCALL_CDECL, this);
    
    engine->RegisterGlobalFunction("Matrix4x4f constructWorldMat(const Vector3f&in position, const Vector3f&in scale, const Vector3f&in rotation)", asFUNCTION(PGE::Matrix4x4f::constructWorldMat), asCALL_CDECL, this);
    engine->RegisterGlobalFunction("Matrix4x4f constructViewMat(const Vector3f&in position, const Vector3f&in target, const Vector3f&in upVector)", asFUNCTION(PGE::Matrix4x4f::constructViewMat), asCALL_CDECL, this);
    engine->RegisterGlobalFunction("Matrix4x4f constructPerspectiveMat(float horizontalfov, float aspectRatio, float nearZ, float farZ)", asFUNCTION(PGE::Matrix4x4f::constructPerspectiveMat), asCALL_CDECL, this);
    engine->RegisterGlobalFunction("Matrix4x4f constructOrthographicMat(float width, float height, float nearZ, float farZ)", asFUNCTION(PGE::Matrix4x4f::constructOrthographicMat), asCALL_CDECL, this);
    engine->SetDefaultNamespace("");
}

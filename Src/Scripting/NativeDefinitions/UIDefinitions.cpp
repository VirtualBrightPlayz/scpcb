#include "UIDefinitions.h"

#include "../ScriptManager.h"

#include "../../World/World.h"
#include "../../Save/Config.h"
#include "../../Graphics/Font.h"
#include "../../Graphics/UIMesh.h"

UIDefinitions::UIDefinitions(ScriptManager* mgr, UIMesh* uiMesh, Config* config, World* world) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("UI");
    engine->RegisterGlobalFunction("void setColor(const Color&in col)", asMETHOD(UIMesh, setColor), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void setTextureless()", asMETHOD(UIMesh, setTextureless), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void setTextured(Texture@ texture, bool tile)", asMETHODPR(UIMesh, setTextured, (PGE::Texture*, bool), void), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void addRect(const Rectanglef&in rect)", asMETHOD(UIMesh, addRect), asCALL_THISCALL_ASGLOBAL, uiMesh);

    engine->RegisterGlobalFunction("int getScreenWidth()", asMETHOD(Config, getWidth), asCALL_THISCALL_ASGLOBAL, config);
    engine->RegisterGlobalFunction("int getScreenHeight()", asMETHOD(Config, getHeight), asCALL_THISCALL_ASGLOBAL, config);
    engine->RegisterGlobalFunction("float getAspectRatio()", asMETHOD(Config, getAspectRatio), asCALL_THISCALL_ASGLOBAL, config);

    engine->SetDefaultNamespace("");
    engine->RegisterObjectType("Font", sizeof(Font), asOBJ_REF | asOBJ_NOCOUNT);
    engine->RegisterObjectMethod("Font", "void draw(const string&in text, const Vector2f&in pos, float scale, float rotation = 0.0, const Color&in color = Color::White) const", asMETHODPR(Font, draw, (const PGE::String & text, const PGE::Vector2f & pos, float scale, float rotation, const PGE::Color & color), void), asCALL_THISCALL);
    engine->RegisterObjectMethod("Font", "float stringWidth(const string&in text, float scale)", asMETHOD(Font, stringWidth), asCALL_THISCALL);
    engine->RegisterObjectMethod("Font", "float getHeight(float scale) const", asMETHOD(Font, getHeight), asCALL_THISCALL);

    engine->SetDefaultNamespace("Font");
    engine->RegisterGlobalFunction("Font@ get_large() property", asMETHOD(World, getFont), asCALL_THISCALL_ASGLOBAL, world);
}

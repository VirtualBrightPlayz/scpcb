#include "InputDefinitions.h"
#include "../../Input/KeyBinds.h"
#include "../../Input/MouseData.h"

InputDefinitions::InputDefinitions(ScriptManager* mgr, KeyBinds* kb, MouseData* md) {
    engine = mgr->getAngelScriptEngine();

    engine->RegisterEnum("Input");

    engine->RegisterEnumValue("Input", "None", (int)Input::None);
    engine->RegisterEnumValue("Input", "Forward", (int)Input::Forward);
    engine->RegisterEnumValue("Input", "Backward", (int)Input::Backward);
    engine->RegisterEnumValue("Input", "Left", (int)Input::Left);
    engine->RegisterEnumValue("Input", "Right", (int)Input::Right);
    engine->RegisterEnumValue("Input", "Crouch", (int)Input::Crouch);
    engine->RegisterEnumValue("Input", "Blink", (int)Input::Blink);
    engine->RegisterEnumValue("Input", "Interact", (int)Input::Interact);

    engine->RegisterEnumValue("Input", "Inventory", (int)Input::Inventory);
    engine->RegisterEnumValue("Input", "ToggleSiteNavigator", (int)Input::ToggleSiteNavigator);
    engine->RegisterEnumValue("Input", "ToggleRadio", (int)Input::ToggleRadio);

    engine->RegisterEnumValue("Input", "ToggleConsole", (int)Input::ToggleConsole);

    engine->SetDefaultNamespace("Input");

    engine->RegisterGlobalFunction("Input getDown()", asMETHOD(KeyBinds, getDownInputs), asCALL_THISCALL_ASGLOBAL, kb);
    engine->RegisterGlobalFunction("Input getHit()", asMETHOD(KeyBinds, getHitInputs), asCALL_THISCALL_ASGLOBAL, kb);

    engine->RegisterGlobalFunction("const Vector2f& getMousePosition()", asMETHOD(MouseData , getPosition), asCALL_THISCALL_ASGLOBAL, md);
    engine->RegisterGlobalFunction("const Vector2f& getMouseWheelDelta()", asMETHOD(MouseData, getWheelDelta), asCALL_THISCALL_ASGLOBAL, md);
}

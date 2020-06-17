#include "ScriptModule.h"
#include "Script.h"
#include "ScriptClass.h"
#include "ScriptObject.h"
#include "ScriptFunction.h"
#include "ScriptGlobal.h"
#include "ScriptManager.h"

#include <stdexcept>
#include <Math/Vector.h>
#include <Math/Matrix.h>
#include "AngelScriptAddons/scriptarray/scriptarray.h"
#include <iostream>

ScriptModule::ScriptModule(ScriptManager* mgr, const PGE::String& nm) {
    scriptManager = mgr;
    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();
    name = nm;
    scriptModule = engine->GetModule(name.cstr(), asGM_ALWAYS_CREATE);
    built = false;
}

ScriptModule::~ScriptModule() {
    scriptModule->Discard();
}

void ScriptModule::addScript(const PGE::String& sectionName, Script* script) {
    if (built) { throw std::runtime_error("Module already built!"); }

    int errorCode = scriptModule->AddScriptSection(sectionName.cstr(), script->getScriptContents().cstr(), script->getScriptContents().size());
    if (errorCode < 0) { throw std::runtime_error("kablooey!"); }

    scripts.push_back(script);
}

void ScriptModule::build() {
    int errorCode = scriptModule->Build();
    if (errorCode < 0) { throw std::runtime_error("whabammy!"); }

    int typeCount = scriptModule->GetObjectTypeCount();
    for (int i = 0; i < typeCount; i++) {
        asITypeInfo* typeInfo = scriptModule->GetObjectTypeByIndex(i);

        ScriptClass* newClass = new ScriptClass(this, typeInfo);
        classes.push_back(newClass);
        if ((typeInfo->GetFlags() & asOBJ_SHARED) != 0) {
            scriptManager->registerSharedClass(newClass);
        }
    }

    for (int i = 0; i < classes.size(); i++) {
        classes[i]->finalizeInitialization();
    }

    int functionCount = scriptModule->GetFunctionCount();
    for (int i = 0; i < functionCount; i++) {
        asIScriptFunction* asFunction = scriptModule->GetFunctionByIndex(i);
        ScriptFunction* newFunction = new ScriptFunction(this, asFunction);

        functions.push_back(newFunction);
    }

    int globalCount = scriptModule->GetGlobalVarCount();
    for (int i = 0; i < globalCount; i++) {
        ScriptGlobal* newGlobal = new ScriptGlobal(this, i);
        globals.push_back(newGlobal);
    }
}

asIScriptModule* ScriptModule::getAngelScriptModule() const {
    return scriptModule;
}

ScriptManager* ScriptModule::getScriptManager() const {
    return scriptManager;
}

ScriptClass* ScriptModule::getClassByName(const PGE::String& name) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getName().equals(name)) { return classes[i]; }
    }
    return nullptr;
}

ScriptClass* ScriptModule::getClassByTypeId(int typeId) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getTypeId() == typeId) { return classes[i]; }
    }
    return nullptr;
}

ScriptFunction* ScriptModule::getFunctionByName(const PGE::String& name) const {
    for (int i = 0; i < functions.size(); i++) {
        if (functions[i]->getSignature().functionName.equals(name)) { return functions[i]; }
    }
    return nullptr;
}

Type* ScriptModule::typeFromTypeId(int typeId) const {
    asIScriptModule* module = scriptModule;
    asIScriptEngine* engine = module->GetEngine();

    int stringFactoryTypeId = engine->GetStringFactoryReturnTypeId();
    int arrayTypeId = engine->GetDefaultArrayTypeId();
    int vector3fTypeID = engine->GetTypeIdByDecl("Vector3f");
    int matrix4x4fTypeID = engine->GetTypeIdByDecl("Matrix4x4f");

    int originalTypeId = typeId;
    bool isRef = (typeId & asTYPEID_OBJHANDLE) != 0;
    bool isTemplate = (typeId & asTYPEID_TEMPLATE) != 0;
    typeId = typeId & (~asTYPEID_OBJHANDLE) & (~asTYPEID_TEMPLATE);

    Type* type = nullptr;
    switch (typeId) {
    case asTYPEID_INT32: {
        type = Type::Int32;
    } break;
    case asTYPEID_UINT32: {
        type = Type::UInt32;
    } break;
    case asTYPEID_FLOAT: {
        type = Type::Float;
    } break;
    case asTYPEID_DOUBLE: {
        type = Type::Double;
    } break;
    case asTYPEID_VOID: {
        type = Type::Void;
    } break;
    default: {
        if (typeId == stringFactoryTypeId) {
            type = Type::String;
        } else if (typeId == vector3fTypeID) {
            type = Type::Vector3f;
        } else if (typeId == matrix4x4fTypeID) {
            type = Type::Matrix4x4f;
        }
        else if (isTemplate) {
            if (scriptManager->isArrayTypeId(originalTypeId)) {
                asITypeInfo* typeInfo = engine->GetTypeInfoById(originalTypeId);
                Type* baseType = typeFromTypeId(typeInfo->GetSubTypeId());
                type = baseType->getArrayType();
            }
            else {
                throw std::runtime_error("Templates are currently not supported for types other than arrays");
            }
        }
        else {
            ScriptClass* clss = getClassByTypeId(typeId);
            if (clss == nullptr) { clss = scriptManager->getSharedClassByTypeId(typeId); }
            type = clss;
        }
    } break;
    }

    if (type == nullptr) {
        return Type::Unsupported;
    }

    if (isRef) {
        type = type->asRef();
    }

    return type;
}

void ScriptModule::save(tinyxml2::XMLDocument& doc) const {
    tinyxml2::XMLElement* moduleElement = doc.NewElement(name);
    for (int i = 0; i < (int)globals.size(); i++) {
        if (globals[i]->isSerializable()) {
            globals[i]->saveXML(moduleElement);
        }
    }

    if (!moduleElement->IsEmpty()) {
        doc.InsertEndChild(moduleElement);
    }
}

void ScriptModule::load(tinyxml2::XMLElement* moduleElement) const {
    for (tinyxml2::XMLElement* element = moduleElement->FirstChildElement(); element != NULL; element = element->NextSiblingElement()) {
        PGE::String name = element->Name();
        const char* namespaceAttrib = element->Attribute("namespace");
        PGE::String varNamespace = namespaceAttrib == nullptr ? "" : PGE::String(namespaceAttrib);

        for (int i = 0; i < (int)globals.size(); i++) {
            if (globals[i]->getName().equals(name) && (globals[i]->getNamespace().isEmpty()|| varNamespace.equals(globals[i]->getNamespace())) && globals[i]->isSerializable()) {
                globals[i]->loadXML(element);
            }
        }
    }
}

void ScriptModule::saveXML(const void* ref, Type* type, tinyxml2::XMLElement* element) const {
    std::cout << "Serializing: " << type->getName() << std::endl;
    if (type->isArrayType()) {
        CScriptArray* arr = (CScriptArray*)ref;
        ArrayType* arrayType = (ArrayType*)type;
        Type* elementType = arrayType->getElementType();

        int arrayLength = arr->GetSize();
        for (int i = 0; i < arrayLength; i++) {
            const void* index = arr->At(i);
            tinyxml2::XMLElement* indexElement = element->GetDocument()->NewElement("arrayElement");
            element->InsertEndChild(indexElement);
                
            saveXML(index, elementType, indexElement);
        }
    } else if (type->isClassType()) {
        asIScriptObject* obj;
        ScriptClass* clss;

        if (type->isRefType()) {
            void** objectRef = (void**)ref;
            obj = (asIScriptObject*)(*objectRef);

            RefType* refType = (RefType*)type;
            clss = (ScriptClass*)refType->getBaseType();
        }
        else {
            obj = (asIScriptObject*)ref;
            clss = (ScriptClass*)type;
        }
        
        if (obj == nullptr) { return; }

        ScriptObject classObject = ScriptObject(clss, obj);
        classObject.saveXML(element, this);
    } else {
        PGE::String strValue;
        if (type == Type::String) {
            PGE::String* str = (PGE::String*)ref;
            strValue = PGE::String(str->cstr());
        }
        else if (type == Type::Float) {
            float* fValue = (float*)ref;
            strValue = PGE::String(*fValue);
        }
        else if (type == Type::Double) {
            double* dValue = (double*)ref;
            strValue = PGE::String(*dValue);
        }
        else if (type == Type::Vector3f) {
            PGE::Vector3f* vectValue = (PGE::Vector3f*)ref;

            strValue =
                PGE::String(vectValue->x) + ","
                + PGE::String(vectValue->y) + ","
                + PGE::String(vectValue->z);
        }
        else if (type == Type::Matrix4x4f) {
            PGE::Matrix4x4f* matValue = (PGE::Matrix4x4f*)ref;

            strValue =
                PGE::String(matValue->elements[0][0]) + ","
                + PGE::String(matValue->elements[0][1]) + ","
                + PGE::String(matValue->elements[0][2]) + ","
                + PGE::String(matValue->elements[0][3]) + ","
                + PGE::String(matValue->elements[1][0]) + ","
                + PGE::String(matValue->elements[1][1]) + ","
                + PGE::String(matValue->elements[1][2]) + ","
                + PGE::String(matValue->elements[1][3]) + ","
                + PGE::String(matValue->elements[2][0]) + ","
                + PGE::String(matValue->elements[2][1]) + ","
                + PGE::String(matValue->elements[2][2]) + ","
                + PGE::String(matValue->elements[2][3]) + ","
                + PGE::String(matValue->elements[3][0]) + ","
                + PGE::String(matValue->elements[3][1]) + ","
                + PGE::String(matValue->elements[3][2]) + ","
                + PGE::String(matValue->elements[3][3]);
        }
        else {
            int iValue = 0;
            memcpy(&iValue, ref, type->getSize());

            strValue = PGE::String(iValue);
        }

        element->SetAttribute("value", strValue);
    }
}

void ScriptModule::loadXML(void* ref, Type* type, tinyxml2::XMLElement* element) const {
    if (type->isArrayType()) {
        ArrayType* arrayType = (ArrayType*)type;
        Type* elementType = arrayType->getElementType();

        int arraySize = 0;
        for (tinyxml2::XMLElement* child = element->FirstChildElement(); child; child = child->NextSiblingElement()) {
            arraySize++;
        }

        CScriptArray* arr;
        if (ref == nullptr) {
            asITypeInfo* asType = getAngelScriptModule()->GetEngine()->GetTypeInfoByName((elementType->getName().cstr()));
            arr = CScriptArray::Create(asType, arraySize);
        }
        else {
            arr = (CScriptArray*)ref;
            arr->Resize(arraySize);
        }

        tinyxml2::XMLElement* indexElement = element->FirstChildElement();
        for (int i = 0; i < arraySize; i++) {
            void* index = arr->At(i);

            loadXML(index, elementType, indexElement);
            indexElement = indexElement->NextSiblingElement();
        }
    }
    else if (type->isClassType()) {
        asIScriptObject* obj;
        ScriptClass* clss;

        if (type->isRefType()) {
            void** objectRef = (void**)ref;
            obj = (asIScriptObject*)(*objectRef);

            RefType* refType = (RefType*)type;
            clss = (ScriptClass*)refType->getBaseType();
        }
        else {
            obj = (asIScriptObject*)ref;
            clss = (ScriptClass*)type;
        }

        if (obj == nullptr) { 
            PGE::String error = PGE::String("Unexpected uninitialized variable: ") + element->Name();
            throw std::runtime_error(error.cstr());
        }

        ScriptObject classObject = ScriptObject(clss, obj);
        classObject.loadXML(element, this);
    }
    else {
        if (type == Type::String) {
            PGE::String* prop = (PGE::String*)ref;
            const char* str = element->Attribute("value");
            *prop = str == nullptr ? "" : str;
        }
        else if (type == Type::Float) {
            float fValue = element->FloatAttribute("value", 0.0f);
            memcpy(ref, &fValue, type->getSize());
        }
        else if (type == Type::Double) {
            double dValue = element->DoubleAttribute("value", 0.0);
            memcpy(ref, &dValue, type->getSize());
        }
        else if (type == Type::Vector3f) {
            //PGE::Vector3f* vectValue = (PGE::Vector3f*)ref;

            //strValue =
            //    PGE::String(vectValue->x) + ","
            //    + PGE::String(vectValue->y) + ","
            //    + PGE::String(vectValue->z);
        }
        else if (type == Type::Matrix4x4f) {
            //PGE::Matrix4x4f* matValue = (PGE::Matrix4x4f*)ref;

            //strValue =
            //    PGE::String(matValue->elements[0][0]) + ","
            //    + PGE::String(matValue->elements[0][1]) + ","
            //    + PGE::String(matValue->elements[0][2]) + ","
            //    + PGE::String(matValue->elements[0][3]) + ","
            //    + PGE::String(matValue->elements[1][0]) + ","
            //    + PGE::String(matValue->elements[1][1]) + ","
            //    + PGE::String(matValue->elements[1][2]) + ","
            //    + PGE::String(matValue->elements[1][3]) + ","
            //    + PGE::String(matValue->elements[2][0]) + ","
            //    + PGE::String(matValue->elements[2][1]) + ","
            //    + PGE::String(matValue->elements[2][2]) + ","
            //    + PGE::String(matValue->elements[2][3]) + ","
            //    + PGE::String(matValue->elements[3][0]) + ","
            //    + PGE::String(matValue->elements[3][1]) + ","
            //    + PGE::String(matValue->elements[3][2]) + ","
            //    + PGE::String(matValue->elements[3][3]);
        }
        else {
            int iValue = element->IntAttribute("value", 0);
            memcpy(ref, &iValue, type->getSize());
        }
    }
}


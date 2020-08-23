#include "ScriptModule.h"
#include "ScriptFunction.h"
#include "ScriptClass.h"
#include "ScriptObject.h"
#include "ScriptModule.h"
#include "ScriptManager.h"

ScriptClass::ScriptClass(ScriptModule* module, asITypeInfo* tInfo) {
    scriptModule = module;

    typeName = tInfo->GetName();

    parentClass = nullptr;
    if (tInfo->GetBaseType() != nullptr) {
        int parentId = tInfo->GetBaseType()->GetTypeId();
        parentClass = module->getScriptManager()->getClassByTypeId(parentId);
        parentClass->registerDerivedClass(this);
    }

    refType = new RefType(this);
    arrayType = nullptr;

    //asIScriptModule* module = scriptModule->getAngelScriptModule();

    angelScriptTypeInfo = tInfo;

    int propertyCount = angelScriptTypeInfo->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        const char* name;
        bool isPrivate;
        bool isProtected;
        bool isSerialize;
        int typeId;
        int offset;
        bool isReference;
        Visibility visibility;
        angelScriptTypeInfo->GetProperty(i, &name, &typeId, &isPrivate, &isProtected, &isSerialize, &offset, &isReference);

        if (isPrivate) {
            visibility = Visibility::Private;
        } else if (isProtected) {
            visibility = Visibility::Protected;
        } else {
            visibility = Visibility::Public;
        }

        Property newProperty = Property(name, offset, typeId, isReference, visibility, isSerialize);

        properties.push_back(newProperty);
    }
}

ScriptClass::~ScriptClass() {
    for (int i=0;i<methods.size();i++) {
        delete methods[i];
    }
    for (int i=0;i<constructors.size();i++) {
        delete constructors[i];
    }
}

bool ScriptClass::isClassType() const {
    return true;
}

const std::vector<ScriptClass::Property>& ScriptClass::getProperties() const {
    return properties;
}

const std::vector<ScriptFunction*>& ScriptClass::getConstructors() const {
    return constructors;
}

ScriptModule* ScriptClass::getScriptModule() const {
    return scriptModule;
}

asITypeInfo* ScriptClass::getAngelScriptTypeInfo() const {
    return angelScriptTypeInfo;
}

int ScriptClass::getTypeId() const {
    return angelScriptTypeInfo->GetTypeId();
}

void ScriptClass::registerDerivedClass(ScriptClass* clss) {
    derivedClasses.push_back(clss);
}

const std::vector<ScriptClass*>& ScriptClass::getDerivedClasses() const {
    return derivedClasses;
}

void ScriptClass::finalizeInitialization() {
    int methodCount = angelScriptTypeInfo->GetMethodCount();
    for (int i = 0; i < methodCount; i++) {
        ScriptFunction* newFunction = new ScriptFunction(scriptModule,
                                                         angelScriptTypeInfo->GetMethodByIndex(i, true),
                                                         angelScriptTypeInfo->GetMethodByIndex(i, false));

        PGE::String decl = newFunction->getSignature().toString();

        methods.push_back(newFunction);
    }

    int factoryCount = angelScriptTypeInfo->GetFactoryCount();

    for (int i = 0; i < factoryCount; i++) {
        ScriptFunction* newFunction = new ScriptFunction(scriptModule, angelScriptTypeInfo->GetFactoryByIndex(i));

        PGE::String decl = newFunction->getSignature().toString();

        constructors.push_back(newFunction);
    }

    for (int i = 0; i < properties.size(); i++) {
        properties[i].determineType(scriptModule);
    }
}

ScriptClass::Property::Property(const PGE::String& n, int off, int tId, bool ref, ScriptClass::Visibility vis, bool isSerial) {
    name = n; offset = off; typeId = tId; isRef = ref; visibility = vis; isSerialize = isSerial;
}

PGE::String ScriptClass::Property::getName() const {
    return name;
}

int ScriptClass::Property::getOffset() const {
    return offset;
}

int ScriptClass::Property::getTypeId() const {
    return typeId;
}

bool ScriptClass::Property::isReference() const {
    return isRef;
}

ScriptClass::Visibility ScriptClass::Property::getVisibility() const {
    return visibility;
}

bool ScriptClass::Property::isSerializable() const {
    return isSerialize;
}

void ScriptClass::Property::determineType(ScriptModule* scriptModule) {
    type = scriptModule->typeFromTypeId(typeId);
}

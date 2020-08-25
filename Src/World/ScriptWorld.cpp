#include "ScriptWorld.h"

#include <Misc/FileUtil.h>

#include "../Save/Config.h"

#include "../Scripting/ScriptManager.h"
#include "../Scripting/ScriptModule.h"
#include "../Scripting/Script.h"
#include "../Scripting/ScriptFunction.h"
#include "../Scripting/ScriptClass.h"

#include "../Scripting/NativeDefinitions/WorldDefinitions.h"
#include "../Scripting/NativeDefinitions/RegexDefinitions.h"
#include "../Scripting/NativeDefinitions/MementoDefinitions.h"
#include "../Scripting/NativeDefinitions/ConsoleDefinitions.h"
#include "../Scripting/NativeDefinitions/InputDefinitions.h"
#include "../Scripting/NativeDefinitions/RefCounter.h"
#include "../Scripting/NativeDefinitions/ColorDefinitions.h"
#include "../Scripting/NativeDefinitions/MathDefinitions.h"
#include "../Scripting/NativeDefinitions/UIDefinitions.h"
#include "../Scripting/NativeDefinitions/TextureDefinitions.h"
#include "../Scripting/NativeDefinitions/MessageDefinitions.h"
#include "../Scripting/NativeDefinitions/LocalizationDefinitions.h"
#include "../Scripting/NativeDefinitions/BillboardDefinitions.h"
#include "../Scripting/NativeDefinitions/ModelDefinitions.h"
#include "../Scripting/NativeDefinitions/RM2Definitions.h"
#include "../Scripting/NativeDefinitions/CollisionDefinitions.h"
#include "../Scripting/NativeDefinitions/PickableDefinitions.h"
#include "../Scripting/NativeDefinitions/PlayerControllerDefinitions.h"
#include "../Scripting/NativeDefinitions/EventDefinition.h"
#include "../Scripting/NativeDefinitions/ReflectionDefinitions.h"

ScriptWorld::ScriptWorld(World* world, GraphicsResources* gfxRes, Camera* camera, KeyBinds* keyBinds, MouseData* mouseData, PGE::IO* io, MessageManager* mm, LocalizationManager* lm, PickableManager* pm, UIMesh* um, Config* config, float timestep, Console* con, BillboardManager* bm) {
    manager = new ScriptManager();

    refCounterManager = new RefCounterManager();

    worldDefinitions = new WorldDefinitions(manager, world);
    regexDefinitions = new RegexDefinitions(manager, refCounterManager);
    mementoDefinitions = new MementoDefinitions(manager);
    consoleDefinitions = new ConsoleDefinitions(manager, con);
    colorDefinitions = new ColorDefinitions(manager);
    mathDefinitions = new MathDefinitions(manager);
    inputDefinitions = new InputDefinitions(manager, keyBinds, mouseData, io);
    textureDefinitions = new TextureDefinitions(manager, gfxRes);
    uiDefinitions = new UIDefinitions(manager, um, config, world);
    messageDefinitions = new MessageDefinitions(manager, mm);
    localizationDefinitions = new LocalizationDefinitions(manager, lm);
    billboardDefinitions = new BillboardDefinitions(manager, bm);
    collisionDefinitions = new CollisionDefinitions(manager, refCounterManager);
    modelDefinitions = new ModelDefinitions(manager, gfxRes);
    rm2Definitions = new RM2Definitions(manager, gfxRes);
    pickableDefinitions = new PickableDefinitions(manager, refCounterManager, pm);
    playerControllerDefinitions = new PlayerControllerDefinitions(manager, refCounterManager, camera);
    reflectionDefinitions = new ReflectionDefinitions(manager);

    ScriptFunction::Signature perTickSignature;
    perTickSignature.functionName = "PerTick";
    perTickSignature.returnType = Type::Void;
    perTickSignature.arguments.push_back(ScriptFunction::Signature::Argument(Type::Float, "deltaTime"));
    perTickEventDefinition = new EventDefinition(manager, "PerTick", perTickSignature);
    perTickEventDefinition->setArgument("deltaTime", timestep); // TODO: Make timestep const global.

    ScriptFunction::Signature perFrameGameSignature;
    perFrameGameSignature.functionName = "PerFrameGame";
    perFrameGameSignature.returnType = Type::Void;
    perFrameGameSignature.arguments.push_back(ScriptFunction::Signature::Argument(Type::Float, "interpolation"));
    perFrameGameEventDefinition = new EventDefinition(manager, "PerFrameGame", perFrameGameSignature);
    perFrameGameEventDefinition->setArgument("interpolation", 1.0f);

    ScriptFunction::Signature perFrameMenuSignature;
    perFrameMenuSignature.functionName = "PerFrameMenu";
    perFrameMenuSignature.returnType = Type::Void;
    perFrameMenuSignature.arguments.push_back(ScriptFunction::Signature::Argument(Type::Float, "interpolation"));
    perFrameMenuEventDefinition = new EventDefinition(manager, "PerFrameMenu", perFrameMenuSignature);
    perFrameMenuEventDefinition->setArgument("interpolation", 1.0f);

    const std::vector<PGE::String>& enabledMods = config->enabledMods->value;

    tinyxml2::XMLDocument doc;

    for (int i = 0; i < enabledMods.size(); i++) {
        PGE::FilePath directory = PGE::FilePath::fromStr(enabledMods[i] + "/");
        PGE::FilePath depsFile = PGE::FilePath(directory, "dependencies.cfg");
        if (PGE::FileUtil::exists(depsFile)) {
            std::vector<PGE::String> depNames = PGE::FileUtil::readLines(depsFile);
            int depsNotEnabled = (int)depNames.size();
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < depNames.size(); k++) {
                    if (enabledMods[j].equals(depNames[k])) {
                        depsNotEnabled--;
                        break;
                    }
                }
            }
            if (depsNotEnabled > 0) {
                throw std::runtime_error((enabledMods[i] + " has dependencies that are not enabled before it").cstr());
            }
        }
        ScriptModule* scriptModule = new ScriptModule(manager, enabledMods[i]);
        std::vector<PGE::FilePath> files = PGE::FileUtil::enumerateFiles(directory);
        for (int j = 0; j < files.size(); j++) {
            if (files[j].getExtension().equals("as")) {
                Script* script = new Script(files[j]);
                scriptModule->addScript(files[j].str()
                    .replace("/", "")
                    .replace(".", ""), script);
            }
        }
        scriptModule->build();
        modules.push_back(scriptModule);
    }

    for (int i=0;i<modules.size();i++) {
        ScriptModule* scriptModule = modules[i];

        ScriptFunction* mainFunction = scriptModule->getFunctionByName("main");
        if (mainFunction != nullptr) {
            mainFunction->prepare();
            mainFunction->execute();
        }

        //scriptModule->save(doc);
        //doc.LoadFile("juanIsntReal.xml");
        //scriptModule->load(doc.FirstChildElement());
        //scriptModule->save(doc);
    }

    doc.SaveFile("juanIsntReal.xml");
}

ScriptWorld::~ScriptWorld() {
    for (int i = 0; i < modules.size(); i++) {
        ScriptModule* scriptModule = modules[i];

        ScriptFunction* exitFunction = scriptModule->getFunctionByName("exit");
        if (exitFunction != nullptr) {
            exitFunction->prepare();
            exitFunction->execute();
        }
    }

    for (int i = (int)modules.size()-1; i >= 0; i--) {
        delete modules[i];
    }

    delete perTickEventDefinition;
    delete perFrameGameEventDefinition;
    delete perFrameMenuEventDefinition;

    delete worldDefinitions;
    delete regexDefinitions;
    delete consoleDefinitions;
    delete inputDefinitions;
    delete colorDefinitions;
    delete mathDefinitions;
    delete uiDefinitions;
    delete messageDefinitions;
    delete localizationDefinitions;
    delete billboardDefinitions;
    delete modelDefinitions;
    delete rm2Definitions;
    delete collisionDefinitions;
    delete pickableDefinitions;
    delete playerControllerDefinitions;
    delete reflectionDefinitions;

    delete refCounterManager;

    delete manager;
}

void ScriptWorld::update() {
    perTickEventDefinition->execute();
}

void ScriptWorld::drawGame(float interpolation) {
    perFrameGameEventDefinition->setArgument("interpolation", interpolation);
    perFrameGameEventDefinition->execute();
}

void ScriptWorld::drawMenu(float interpolation) {
    perFrameMenuEventDefinition->setArgument("interpolation", interpolation);
    perFrameMenuEventDefinition->execute();
}

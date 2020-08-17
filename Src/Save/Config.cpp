#include "Config.h"

#include <exception>
#include <Misc/FileUtil.h>

#include "../Utils/INI.h"
#include "../Graphics/GraphicsResources.h"

const PGE::String corpFolder = "Undertow Games";
const PGE::String gameFolder = "SCP - Containment Breach";

PGE::String getConfigDir() {
    // return ""; // Uncomment this if you want options.ini in the root game folder.
    return PGE::FileUtil::getDataFolder() + corpFolder + '/' + gameFolder + '/';
}

Config::Config(const PGE::String& optionsFile) :
        filename(getConfigDir() + optionsFile),
        vsync(new BoolConfigValue(filename, secGFX, "vsync", true)),
        vr(new BoolConfigValue(filename, secGFX, "vr", false)),
        sensitivity(new IntConfigValue(filename, secGen, "sensitivity", 100)),
        languageCode(new StringConfigValue(filename, secGen, "language", "en")),
        windowType(new IntConfigValue(filename, secGFX, "window", WindowType::Windowed)),
        width(new IntConfigValue(filename, secGFX, "width", 1280)),
        height(new IntConfigValue(filename, secGFX, "height", 720)),
        enabledMods(new ArrayConfigValue(filename, secMod, "enabledmods", "RootScript|SCPCB")),
        resourcePackLocations(new ArrayConfigValue(filename, secMod, "resourcepacklocations", "ResourcePacks")),
        enabledResourcePacks(new ArrayConfigValue(filename, secMod, "enabledresourcepacks", "HIGH|hahahaha")) {
    values.push_back(vsync);
    values.push_back(vr);
    values.push_back(sensitivity);
    values.push_back(languageCode);
    values.push_back(windowType);
    values.push_back(width);
    values.push_back(height);
    values.push_back(enabledMods);
    values.push_back(resourcePackLocations);
    values.push_back(enabledResourcePacks);

    setResolution(width->value, height->value);

    // Generating default keyboard bindings.
    kbBinds.emplace(Input::Forward, PGE::KeyboardInput::KEYCODE::W);
    kbBinds.emplace(Input::Backward, PGE::KeyboardInput::KEYCODE::S);
    kbBinds.emplace(Input::Left, PGE::KeyboardInput::KEYCODE::A);
    kbBinds.emplace(Input::Right, PGE::KeyboardInput::KEYCODE::D);
    kbBinds.emplace(Input::Sprint, PGE::KeyboardInput::KEYCODE::LSHIFT);
    kbBinds.emplace(Input::Crouch, PGE::KeyboardInput::KEYCODE::LCTRL);
    kbBinds.emplace(Input::Blink, PGE::KeyboardInput::KEYCODE::SPACE);
    kbBinds.emplace(Input::Interact, PGE::KeyboardInput::KEYCODE::E);
    kbBinds.emplace(Input::Inventory, PGE::KeyboardInput::KEYCODE::TAB);
    kbBinds.emplace(Input::ToggleConsole, PGE::KeyboardInput::KEYCODE::F3);

    if (PGE::FileUtil::exists(PGE::FilePath::fromStr(filename))) {
        loadFile();
    } else {
        PGE::FileUtil::createDirectoryIfNotExists(PGE::FilePath::fromStr(PGE::FileUtil::getDataFolder() + corpFolder));
        PGE::FileUtil::createDirectoryIfNotExists(PGE::FilePath::fromStr(getConfigDir()));

        saveFile();
    }
}

Config::~Config() {
    // Deleting all entries in the list, as we own them.
    for (int i = 0; i < values.size(); i++) {
        delete values[i];
    }
}

void Config::setGraphicsResources(GraphicsResources* grm) {
    gfxResMgr = grm;
}

void Config::loadFile() {
    for (int i = 0; i < values.size(); i++) {
        values[i]->loadValue();
    }

    setResolution(width->value, height->value);

    loadKeyboardInput(Input::Forward);
    loadKeyboardInput(Input::Backward);
    loadKeyboardInput(Input::Left);
    loadKeyboardInput(Input::Right);
    loadKeyboardInput(Input::Sprint);
    loadKeyboardInput(Input::Crouch);
    loadKeyboardInput(Input::Blink);
    loadKeyboardInput(Input::Interact);
    loadKeyboardInput(Input::Inventory);
}

void Config::loadKeyboardInput(Input input) {
    PGE::String name = getBindingName(input) + "_keyboard";
    PGE::String bindings = getINIString(filename, secCon, name, "");
    if (bindings.isEmpty()) {
        return;
    }

    std::pair<KeyBindsMap::iterator, KeyBindsMap::iterator> bindingsRange = kbBinds.equal_range(input);
    kbBinds.erase(bindingsRange.first, bindingsRange.second);

    std::vector<PGE::String> bindVect = bindings.split(',', true);
    for (int i = 0; i < (int)bindVect.size(); i++) {
        kbBinds.emplace(input, (PGE::KeyboardInput::KEYCODE)bindVect[i].toInt());
    }
}

void Config::saveFile() const {
    for (int i = 0; i < values.size(); i++) {
        values[i]->saveValue();
    }

    std::map<Input, PGE::String> inputValues;
    for (const auto& it : kbBinds) {
        std::map<Input, PGE::String>::iterator valIter = inputValues.find(it.first);
        if (valIter == inputValues.end()) {
            inputValues.emplace(it.first, "");
            valIter = inputValues.find(it.first);
        } else {
            valIter->second += PGE::String(",");
        }
        valIter->second += PGE::String((int)it.second);
    }

    for (const auto& valIter : inputValues) {
        putINIValue(filename, secCon, getBindingName(valIter.first) + "_keyboard", valIter.second);
    }
}

void Config::setResolution(int width, int height) {
    if (width <= 0 || height <= 0) {
        throw std::runtime_error((PGE::String("Invalid display resolution. (width: ") + width + ", height: " + height + ")").cstr());
    }

    this->width->value = width;
    this->height->value = height;
    aspectRatio = (float)width / height;

    if (gfxResMgr != nullptr) { gfxResMgr->updateOrthoMat(aspectRatio); }
}

int Config::getWidth() const {
    return width->value;
}

int Config::getHeight() const {
    return height->value;
}

float Config::getAspectRatio() const {
    return aspectRatio;
}

const Config::KeyBindsMap& Config::getKeyboardBindings() const {
    return kbBinds;
}

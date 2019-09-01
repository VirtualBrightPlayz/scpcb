#include <exception>
#include <Misc/FileUtil.h>

#include "Config.h"
#include "../Utils/INI.h"
#include "../Graphics/GraphicsResources.h"

PGE::String corpFolder = "Undertow Games";
PGE::String gameFolder = "SCP - Containment Breach";

// Defaults.
PGE::String defaultLanguage = "en";

WindowType defaultWindow = WindowType::Windowed;
int defaultWidth = 1280;
int defaultHeight = 720;
bool defaultVsync = false;

void Config::genDefaultKeyboardBindings() {
    kbBinds[Input::Forward] = { PGE::KeyboardInput::SCANCODE::W };
    kbBinds[Input::Backward] = { PGE::KeyboardInput::SCANCODE::S };
    kbBinds[Input::Left] = { PGE::KeyboardInput::SCANCODE::A };
    kbBinds[Input::Right] = { PGE::KeyboardInput::SCANCODE::S };
    kbBinds[Input::Sprint] = { PGE::KeyboardInput::SCANCODE::LSHIFT };
    kbBinds[Input::Crouch] = { PGE::KeyboardInput::SCANCODE::LCTRL };
    kbBinds[Input::Blink] = { PGE::KeyboardInput::SCANCODE::SPACE };
    kbBinds[Input::Interact] = { PGE::KeyboardInput::SCANCODE::E };
    kbBinds[Input::Inventory] = { PGE::KeyboardInput::SCANCODE::TAB };
}

PGE::String getConfigDir() {
    return PGE::FileUtil::getDataFolder() + corpFolder + "/" + gameFolder + "/";
}

Config::Config(const PGE::String& optionsFile) {
    filename = getConfigDir() + optionsFile;

    languageCode = defaultLanguage;

    windowType = defaultWindow;
    setResolution(defaultWidth, defaultHeight);
    vsync = defaultVsync;

    genDefaultKeyboardBindings();

    if (PGE::FileUtil::exists(filename)) {
        loadFile();
    } else {
        if (!PGE::FileUtil::exists(PGE::FileUtil::getDataFolder() + corpFolder)) {
            PGE::FileUtil::createDirectory(PGE::FileUtil::getDataFolder() + corpFolder);
        }

        if (!PGE::FileUtil::exists(getConfigDir())) {
            PGE::FileUtil::createDirectory(getConfigDir());
        }

        saveFile();
    }
}

Config::Config(const Config& cpy) {
    languageCode = cpy.languageCode;

    windowType = cpy.windowType;
    setResolution(cpy.width, cpy.height);
    vsync = cpy.vsync;

    kbBinds = cpy.kbBinds;
    msBinds = cpy.msBinds;
}

Config& Config::operator=(const Config& other) {
    if (this != &other) {
        languageCode = other.languageCode;

        windowType = other.windowType;
        setResolution(other.width, other.height);
        vsync = other.vsync;

        kbBinds = other.kbBinds;
        msBinds = other.msBinds;
    }

    return *this;
}

void Config::setGraphicsResources(GraphicsResources* grm) {
    gfxResMgr = grm;
}

void Config::loadFile() {
    languageCode = getINIString(filename, secGen, "language", defaultLanguage);

    windowType = (WindowType)getINIInt(filename, secGFX, "fullscreen");
    int widthINI = getINIInt(filename, secGFX, "width", defaultWidth);
    int heightINI = getINIInt(filename, secGFX, "height", defaultHeight);
    setResolution(widthINI, heightINI);
    vsync = getINIBool(filename, secGFX, "vsync", defaultVsync);

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

    kbBinds[input] = std::vector<PGE::KeyboardInput::SCANCODE>();

    std::vector<PGE::String> bindVect = bindings.split(",", true);
    for (int i = 0; i < (int)bindVect.size(); i++) {
        kbBinds[input].push_back((PGE::KeyboardInput::SCANCODE)bindVect[i].toInt());
    }
}

void Config::saveFile() const {
    putINIValue(filename, secGen, "language", languageCode);

    putINIValue(filename, secGFX, "width", width);
    putINIValue(filename, secGFX, "height", height);
    putINIValue(filename, secGFX, "vsync", vsync);

    std::map<Input, std::vector<PGE::KeyboardInput::SCANCODE>>::const_iterator it;
    for (it = kbBinds.begin(); it != kbBinds.end(); it++) {
        PGE::String bindName = getBindingName(it->first) + "_keyboard";

        std::vector<PGE::String> strToJoin;
        for (int i = 0; i < (int)it->second.size(); i++) {
            strToJoin.push_back(PGE::String((int)it->second[i]));
        }
        PGE::String finalSave = PGE::String::join(strToJoin, ",");

        putINIValue(filename, secCon, bindName, finalSave);
    }
}

void Config::setResolution(int width, int height) {
    if (width <= 0 || height <= 0) {
        PGE::String errorStr = PGE::String("Invalid display resolution. (width: ") + width + ", height: " + height + ")";
        throw std::runtime_error(errorStr.cstr());
    }

    this->width = width;
    this->height = height;
    aspectRatio = (float)width / height;

    if (gfxResMgr != nullptr) { gfxResMgr->updateOrthoMat(aspectRatio); }
}

PGE::String Config::getLangCode() const {
    return languageCode;
}

int Config::getWidth() const {
    return width;
}

int Config::getHeight() const {
    return height;
}

float Config::getAspectRatio() const {
    return aspectRatio;
}

float Config::isVsync() const {
    return vsync;
}

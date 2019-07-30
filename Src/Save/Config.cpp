#include <exception>
#include <Misc/FileUtil.h>

#include "Config.h"
#include "../Utils/INI.h"
#include "../Graphics/GfxResManager.h"

PGE::String corpFolder = "Undertow Games";
PGE::String gameFolder = "SCP - Containment Breach";

PGE::String getConfigDir() {
    return PGE::FileUtil::getDataFolder() + corpFolder + "/" + gameFolder + "/";
}

Config::Config(const PGE::String& optionsFile) {
    filename = getConfigDir() + optionsFile;
    
    languageCode = "en";

    windowType = WindowType::Windowed;
    setResolution(1280, 720);

    vsync = false;

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
}

Config& Config::operator=(const Config& other) {
    if (this != &other) {
        languageCode = other.languageCode;
        
        windowType = other.windowType;
        setResolution(other.width, other.height);

        vsync = other.vsync;
    }

    return *this;
}

void Config::setGfxResManager(GfxResManager* grm) {
    gfxResMgr = grm;
}

void Config::loadFile() {
    languageCode = getINIString(filename, secGen, "language", "en");
    
    int widthINI = getINIInt(filename, secGFX, "width");
    int heightINI = getINIInt(filename, secGFX, "height");
    setResolution(widthINI, heightINI);
    vsync = getINIBool(filename, secGFX, "vsync");
}

void Config::saveFile() const {
    putINIValue(filename, secGen, "language", languageCode);
    
    putINIValue(filename, secGFX, "width", width);
    putINIValue(filename, secGFX, "height", height);
    putINIValue(filename, secGFX, "vsync", vsync);
}

void Config::setResolution(int width, int height) {
    if (width <= 0 || height <= 0) {
        PGE::String errorStr = PGE::String("Invalid display resolution. (width: ") + width + ", height: " + height + ")";
        throw std::runtime_error(errorStr.cstr());
    }

    this->width = width;
    this->height = height;
    aspectRatio = (float)width / height;
    
    if (shaderMgmt != nullptr) { shaderMgmt->updateOrthoMat(getAspectRatio()); }
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

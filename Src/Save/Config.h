#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <map>
#include <list>

#include <Misc/FilePath.h>
#include <Math/Matrix.h>
 #include <UserInput/UserInput.h>

#include "ConfigValues.h"
#include "../Input/Input.h"

class GraphicsResources;

enum WindowType {
    Windowed = 0,
    Fullscreen = 1
};

class Config {
    public:
        typedef std::multimap<Input, PGE::KeyboardInput::KEYCODE> KeyBindsMap;
        
    private:
        GraphicsResources* gfxResMgr = nullptr;

        const PGE::String filename;
        const PGE::String secGen = "general";
        const PGE::String secGFX = "graphics";
        const PGE::String secCon = "controls";
        const PGE::String secMod = "mods";

        std::vector<ConfigValue*> values;

        IntConfigValue* const windowType;
        IntConfigValue* const width;
        IntConfigValue* const height;
        float aspectRatio;
        
        KeyBindsMap kbBinds;
        void loadKeyboardInput(Input input);
        std::multimap<Input, PGE::MouseInput::BUTTON> msBinds;

        void loadFile();
        void saveFile() const;

    public:
        StringConfigValue* const languageCode;
        
        BoolConfigValue* const vsync;
        BoolConfigValue* const vr;

        IntConfigValue* const sensitivity;

        ArrayConfigValue* enabledMods;
        ArrayConfigValue* resourcePackLocations;
        ArrayConfigValue* enabledResourcePacks;
        
        Config(const PGE::String& optionsFile);
        ~Config();

        void setGraphicsResources(GraphicsResources* grm);

        void setResolution(int width, int height);
        int getWidth() const;
        int getHeight() const;
        float getAspectRatio() const;

        const KeyBindsMap& getKeyboardBindings() const;
};

#endif // CONFIG_H_INCLUDED

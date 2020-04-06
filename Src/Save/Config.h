#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <map>

#include <Misc/FilePath.h>
#include <Math/Matrix.h>
 #include <UserInput/UserInput.h>

#include "../Input/Input.h"

class GraphicsResources;

enum class WindowType {
    Windowed,
    Fullscreen
};

class Config {
    private:
        GraphicsResources* gfxResMgr = nullptr;

        PGE::String filename;
        PGE::String secGen = "general";
        PGE::String secGFX = "graphics";
        PGE::String secCon = "controls";
        PGE::String secMod = "mods";

        PGE::String languageCode;

        WindowType windowType;
        int width;
        int height;
        float aspectRatio;

        bool vsync;

        std::map<Input, std::vector<PGE::KeyboardInput::SCANCODE>> kbBinds;
        void loadKeyboardInput(Input input);
        std::map<Input, std::vector<PGE::MouseInput::BUTTON>> msBinds;

        std::vector<PGE::String> enabledMods;

        void genDefaultKeyboardBindings();

        void loadFile();
        void saveFile() const;
        void saveBindings(Input input) const;

    public:
        Config(const PGE::String& optionsFile);
        Config(const Config& cpy);

        Config& operator=(const Config& other);

        void setGraphicsResources(GraphicsResources* grm);

        PGE::String getLangCode() const;

        void setResolution(int width, int height);
        int getWidth() const;
        int getHeight() const;
        float getAspectRatio() const;

        const std::vector<PGE::String>& getEnabledMods() const;

        float isVsync() const;
};

#endif // CONFIG_H_INCLUDED

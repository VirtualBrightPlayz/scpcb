#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <Misc/FileName.h>
#include <Math/Matrix.h>

class GfxResManager;

enum class WindowType {
    Windowed,
    Fullscreen
};

class Config {
    private:
        GfxResManager* gfxResMgr = nullptr;
    
        PGE::String filename;
        PGE::String secGen = "general";
        PGE::String secGFX = "graphics";
    
        PGE::String languageCode;
    
        WindowType windowType;
        int width;
        int height;
        float aspectRatio;

        bool vsync;

    public:
        Config(const PGE::String& optionsFile);
        Config(const Config& cpy);

        Config& operator=(const Config& other);
    
        void setGfxResManager(GfxResManager* grm);
    
        void loadFile();
        void saveFile() const;
    
        PGE::String getLangCode() const;

        void setResolution(int width, int height);
        int getWidth() const;
        int getHeight() const;
        float getAspectRatio() const;

        float isVsync() const;
};

#endif // CONFIG_H_INCLUDED

#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <Misc/FileName.h>
#include <Math/Matrix.h>

class ShaderManager;

enum class WindowType {
    Windowed,
    Fullscreen
};

class Config {
    private:
        ShaderManager* shaderMgmt = nullptr;
    
        PGE::String filename;
        PGE::String secGFX = "graphics";
    
        WindowType windowType;
        int width;
        int height;
        float aspectRatio;

        bool vsync;

    public:
        Config(const PGE::String& optionsFile);
        Config(const Config& cpy);

        Config& operator=(const Config& other);
    
        void setShaderManager(ShaderManager* sm);
    
        void loadFile();
        void saveFile() const;

        void setResolution(int width, int height);
        int getWidth() const;
        int getHeight() const;
        float getAspectRatio() const;

        float isVsync() const;
};

#endif // CONFIG_H_INCLUDED

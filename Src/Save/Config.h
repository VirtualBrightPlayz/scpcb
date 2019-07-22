#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <Misc/FileName.h>
#include <Math/Matrix.h>

enum class WindowType {
    Windowed,
    Fullscreen
};

class Config {
    private:
        PGE::String filename;
        PGE::String secGFX = "graphics";
    
        WindowType windowType;
        int width;
        int height;
        float aspectRatio;

        bool vsync;

        PGE::Matrix4x4f orthoMat;
        void updateOrthoMat();

    public:
        Config(const PGE::String& optionsFile);
        Config(const Config& cpy);

        Config& operator=(const Config& other);
    
        void loadFile();
        void saveFile() const;

        void setResolution(int width, int height);
        int getWidth() const;
        int getHeight() const;
        float getAspectRatio() const;

        PGE::Matrix4x4f getOrthoMat() const;

        float isVsync() const;
};

#endif // CONFIG_H_INCLUDED

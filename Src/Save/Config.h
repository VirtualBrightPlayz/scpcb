#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <Misc/String.h>

enum class WindowType {
    Windowed,
    Fullscreen
};

class Config {
private:
    WindowType windowType;
    int width;
    int height;
    float aspectRatio;

    bool vsync;

public:
    static void initialize(const PGE::String& fileName);
    static void cleanup();

    Config();
    Config(const Config& cpy);

    Config& operator=(const Config& other);

    void setResolution(int width, int height);
    int getWidth() const;
    int getHeight() const;
    float getAspectRatio() const;

    float isVsync() const;
};

extern Config config;

#endif // CONFIG_H_INCLUDED

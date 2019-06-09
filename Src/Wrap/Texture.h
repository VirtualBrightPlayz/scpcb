#ifndef TEXTURE_H_INCLUDED
#define TEXTURE_H_INCLUDED

#include <Texture/Texture.h>
#include <Misc/String.h>
#include <map>
#include "Graphics.h"
#include "ThreadManager.h"

class Texture {
private:
    static std::map<PGE::Texture*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::Texture* internal = nullptr;

public:
    Texture()=default;

    Texture(const Texture& cpy);
    Texture& operator=(const Texture& other);
    ~Texture();

    static Texture load(Graphics gfx, PGE::String filename);
    static Texture load(Graphics gfx, PGE::String filename, ThreadManager threadManager);
    static Texture create(Graphics gfx, int w, int h, bool renderTarget, const void* buffer, PGE::Texture::FORMAT fmt);

    PGE::Texture* operator->() const;

    PGE::Texture* getInternal();
};

#endif // TEXTURE_H_INCLUDED

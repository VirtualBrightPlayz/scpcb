#include "Texture.h"

std::map<PGE::Texture*, int> Texture::cpyTracker = std::map<PGE::Texture*, int>();

void Texture::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool Texture::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

Texture Texture::load(Graphics gfx, PGE::String filename) {
    Texture tex;
    tex.internal = PGE::Texture::load(gfx.getInternal(), filename.resourcePath());
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture Texture::load(Graphics gfx, PGE::String filename, ThreadManager threadManager) {
    Texture tex;
    tex.internal = PGE::Texture::load(gfx.getInternal(), filename.resourcePath(), threadManager.getInternal());
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture Texture::create(Graphics gfx, int w, int h, bool renderTarget, const void* buffer, PGE::Texture::FORMAT fmt) {
    Texture tex;
    tex.internal = PGE::Texture::create(gfx.getInternal(), w, h, renderTarget, buffer, fmt);
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture::Texture(const Texture& cpy) {
    internal = cpy.internal;
    cpy.increment();
}

Texture& Texture::operator=(const Texture& other) {
    if (&other == this) return *this;

    // Wipe current reference.
    bool remove = decrement();

    if (remove) {
        cpyTracker.erase(internal);
        delete internal;
    }
    internal = nullptr;

    // Add new reference.
    internal = other.internal;
    other.increment();

    return *this;
}

Texture::~Texture() {
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::Texture* Texture::operator->() const {
    return internal;
}

PGE::Texture* Texture::getInternal() const {
    return internal;
}

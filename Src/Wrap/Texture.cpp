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

Texture Texture::load(const Graphics& gfx, const PGE::String& filename) {
    Texture tex;
    tex.internal = PGE::Texture::load(gfx.getInternal(), filename.resourcePath());
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture Texture::load(const Graphics& gfx, const PGE::String& filename, const ThreadManager& threadManager) {
    Texture tex;
    tex.internal = PGE::Texture::load(gfx.getInternal(), filename.resourcePath(), threadManager.getInternal());
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture Texture::create(const Graphics& gfx, int w, int h, bool renderTarget, const void* buffer, PGE::Texture::FORMAT fmt) {
    Texture tex;
    tex.internal = PGE::Texture::create(gfx.getInternal(), w, h, renderTarget, buffer, fmt);
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));

    return tex;
}

Texture Texture::createColorFill(const Graphics& gfx, const PGE::Color& color) {
    Texture tex;
    char cColor[4];
    cColor[0] = color.getRedInt();
    cColor[1] = color.getGreenInt();
    cColor[2] = color.getBlueInt();
    cColor[3] = color.getAlphaInt();
    
    tex.internal = PGE::Texture::create(gfx.getInternal(), 1, 1, false, cColor, PGE::Texture::FORMAT::RGBA32);
    cpyTracker.insert(std::pair<PGE::Texture*, int>(tex.internal, 1));
    
    return tex;
}

Texture::Texture(const Texture& cpy) {
    if (cpy.internal != nullptr) {
        internal = cpy.internal;
        increment();
    }
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
    if (other.internal != nullptr) {
        internal = other.internal;
        increment();
    }

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

bool Texture::isTracking() const {
    return internal != nullptr;
}

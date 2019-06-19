#include "Graphics.h"

std::map<PGE::Graphics*, int> Graphics::cpyTracker = std::map<PGE::Graphics*, int>();

void Graphics::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool Graphics::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

Graphics Graphics::create(const PGE::String& name, int w, int h, bool fs) {
    Graphics gfx;
    gfx.internal = PGE::Graphics::create(name, w, h, fs);
    cpyTracker.insert(std::pair<PGE::Graphics*, int>(gfx.internal, 1));

    return gfx;
}

Graphics::Graphics(const Graphics& cpy) {
    internal = cpy.internal;
    increment();
}

Graphics& Graphics::operator=(const Graphics& other) {
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
    increment();

    return *this;
}

Graphics::~Graphics() {
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::Graphics* Graphics::operator->() const {
    return internal;
}

PGE::Graphics* Graphics::getInternal() const {
    return internal;
}

bool Graphics::isTracking() const {
    return internal != nullptr;
}

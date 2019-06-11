#include "Shader.h"

std::map<PGE::Shader*, int> Shader::cpyTracker = std::map<PGE::Shader*, int>();

void Shader::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool Shader::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

Shader Shader::load(Graphics& gfx, const PGE::String& path) {
    Shader shd;
    shd.internal = PGE::Shader::load(gfx.getInternal(), path.resourcePath());
    cpyTracker.insert(std::pair<PGE::Shader*, int>(shd.internal, 1));

    return shd;
}

Shader::Shader(const Shader& cpy) {
    internal = cpy.internal;
    cpy.increment();
}

Shader& Shader::operator=(const Shader& other) {
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

Shader::~Shader() {
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::Shader* Shader::operator->() const {
    return internal;
}

PGE::Shader* Shader::getInternal() {
    return internal;
}

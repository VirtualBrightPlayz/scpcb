#include "Mesh.h"

std::map<PGE::Mesh*, int> Mesh::cpyTracker = std::map<PGE::Mesh*, int>();

void Mesh::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool Mesh::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

Mesh Mesh::create(const Graphics& gfx, PGE::Primitive::TYPE pt) {
    Mesh mesh;
    mesh.internal = PGE::Mesh::create(gfx.getInternal(), pt);
    cpyTracker.insert(std::pair<PGE::Mesh*, int>(mesh.internal, 1));

    return mesh;
}

Mesh::Mesh(const Mesh& cpy) {
    internal = cpy.internal;
    increment();
}

Mesh& Mesh::operator=(const Mesh& other) {
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

Mesh::~Mesh() {
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::Mesh* Mesh::operator->() const {
    return internal;
}

PGE::Mesh* Mesh::getInternal() const {
    return internal;
}

bool Mesh::isTracking() const {
    return internal != nullptr;
}

#include "IO.h"

using namespace CBN;

std::map<PGE::IO*, int> IO::cpyTracker = std::map<PGE::IO*, int>();

void IO::increment() const {
    cpyTracker[internal] = cpyTracker[internal] + 1;
}

bool IO::decrement() const {
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

IO IO::create(PGE::Window* window) {
    IO gfx;
    gfx.internal = PGE::IO::create(window);
    cpyTracker.insert(std::pair<PGE::IO*, int>(gfx.internal, 1));

    return gfx;
}

IO::IO(const IO& cpy) {
    internal = cpy.internal;
    cpy.increment();
}

IO& IO::operator=(const IO& other) {
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

IO::~IO() {
    bool remove = decrement();

    if (remove) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::IO* IO::operator->() const {
    return internal;
}

PGE::IO* IO::getInternal() {
    return internal;
}

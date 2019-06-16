#include "IO.h"

std::map<PGE::IO*, int> IO::cpyTracker = std::map<PGE::IO*, int>();

void IO::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool IO::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

IO IO::create(PGE::Window* window) {
    IO io;
    io.internal = PGE::IO::create(window);
    cpyTracker.insert(std::pair<PGE::IO*, int>(io.internal, 1));

    return io;
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

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::IO* IO::operator->() const {
    return internal;
}

PGE::IO* IO::getInternal() const {
    return internal;
}

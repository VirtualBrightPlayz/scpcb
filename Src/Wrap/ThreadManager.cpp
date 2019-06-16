#include "ThreadManager.h"

std::map<PGE::ThreadManager*, int> ThreadManager::cpyTracker = std::map<PGE::ThreadManager*, int>();

void ThreadManager::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool ThreadManager::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

ThreadManager ThreadManager::create() {
    ThreadManager tm;
    tm.internal = new PGE::ThreadManager();
    cpyTracker.insert(std::pair<PGE::ThreadManager*, int>(tm.internal, 1));

    return tm;
}

ThreadManager::ThreadManager(const ThreadManager& cpy) {
    internal = cpy.internal;
    cpy.increment();
}

ThreadManager& ThreadManager::operator=(const ThreadManager& other) {
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

ThreadManager::~ThreadManager() {
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::ThreadManager* ThreadManager::operator->() const {
    return internal;
}

PGE::ThreadManager* ThreadManager::getInternal() const {
    return internal;
}

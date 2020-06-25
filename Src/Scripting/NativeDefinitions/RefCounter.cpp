#include "RefCounter.h"

void RefCounterManager::linkPtrToCounter(void* ptr, RefCounter* counter) {
    links.emplace(ptr, counter);
}

void RefCounterManager::unlinkPtr(void* ptr) {
    links.erase(ptr);
}

void RefCounterManager::addRef(void* ptr) {
    if (links.find(ptr) == links.end()) { return; }
    links[ptr]->addRef(ptr);
}

void RefCounterManager::release(void* ptr) {
    if (links.find(ptr) == links.end()) { return; }
    links[ptr]->release(ptr);
}

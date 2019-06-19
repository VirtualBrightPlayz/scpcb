#ifndef THREADMANAGER_H_INCLUDED
#define THREADMANAGER_H_INCLUDED

#include <Threading/ThreadManager.h>
#include <Misc/String.h>
#include <map>

// TODO: This wrapper probably isn't enough.
class ThreadManager {
private:
    static std::map<PGE::ThreadManager*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::ThreadManager* internal = nullptr;

public:
    ThreadManager()=default;

    ThreadManager(const ThreadManager& cpy);
    ThreadManager& operator=(const ThreadManager& other);
    ~ThreadManager();

    static ThreadManager create();

    PGE::ThreadManager* operator->() const;

    PGE::ThreadManager* getInternal() const;

    bool isTracking() const;
};

#endif // THREADMANAGER_H_INCLUDED

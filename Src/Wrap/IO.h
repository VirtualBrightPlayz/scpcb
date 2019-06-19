#ifndef IO_H_INCLUDED
#define IO_H_INCLUDED

#include <IO/IO.h>
#include <Window/Window.h>
#include <Misc/String.h>
#include <map>

class IO {
private:
    static std::map<PGE::IO*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::IO* internal = nullptr;

public:
    IO()=default;

    IO(const IO& cpy);
    IO& operator=(const IO& other);
    ~IO();

    static IO create(PGE::Window* window);

    PGE::IO* operator->() const;

    PGE::IO* getInternal() const;

    bool isTracking() const;
};

#endif // IO_H_INCLUDED

#ifndef GRAPHICS_H_INCLUDED
#define GRAPHICS_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Misc/String.h>
#include <map>

class Graphics {
private:
    static std::map<PGE::Graphics*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::Graphics* internal = nullptr;

public:
    Graphics()=default;

    Graphics(const Graphics& cpy);
    Graphics& operator=(const Graphics& other);
    ~Graphics();

    static Graphics create(PGE::String name,int w, int h, bool fs);

    PGE::Graphics* operator->() const;

    PGE::Graphics* getInternal();
};

#endif // GRAPHICS_H_INCLUDED

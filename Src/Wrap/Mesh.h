#ifndef MESH_H_INCLUDED
#define MESH_H_INCLUDED

#include <Mesh/Mesh.h>
#include <Misc/String.h>
#include <map>
#include "Graphics.h"

class Mesh {
private:
    static std::map<PGE::Mesh*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::Mesh* internal = nullptr;

public:
    Mesh()=default;

    Mesh(const Mesh& cpy);
    Mesh& operator=(const Mesh& other);
    ~Mesh();

    static Mesh create(const Graphics& gfx, PGE::Primitive::TYPE pt);

    PGE::Mesh* operator->() const;

    PGE::Mesh* getInternal() const;
};

#endif // MESH_H_INCLUDED

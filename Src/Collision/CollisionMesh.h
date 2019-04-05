#ifndef CBN_COLLISIONMESH_H_INCLUDED
#define CBN_COLLISIONMESH_H_INCLUDED

#include <vector>
#include <Math/Vector.h>
#include "Collision.h"

namespace CBN {

class CollisionMesh {
    public:
        CollisionMesh(std::vector<PGE::Vector3f> verts,std::vector<int> inds);
        Collision checkCollision(PGE::Line3f line,float radius,int& outTriangleIndex);
        Collision checkCollision(PGE::Line3f line,float radius);
    private:
        std::vector<PGE::Vector3f> vertices;
        std::vector<int> indices;
};

}

#endif

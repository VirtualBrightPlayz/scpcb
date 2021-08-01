#ifndef COLLISIONMESH_H_INCLUDED
#define COLLISIONMESH_H_INCLUDED

#include <vector>
#include <PGE/Math/Vector.h>
#include <PGE/Math/Matrix.h>

#include "Collision.h"

class CollisionMesh {
    public:
        CollisionMesh(const std::vector<PGE::Vector3f>& verts,const std::vector<int>& inds);
        Collision checkCollision(const PGE::Matrix4x4f& matrix, const PGE::Line3f& line,float height,float radius,int& outTriangleIndex) const;
        Collision checkCollision(const PGE::Matrix4x4f& matrix, const PGE::Line3f& line,float height,float radius) const;
        const std::vector<PGE::Vector3f>& getVertices() const;

    private:
        std::vector<PGE::Vector3f> vertices;
        std::vector<int> indices;
};

#endif // COLLISIONMESH_H_INCLUDED

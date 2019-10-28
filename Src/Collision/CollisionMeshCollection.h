#ifndef CBN_COLLISIONMESHCOLLECTION_H_INCLUDED
#define CBN_COLLISIONMESHCOLLECTION_H_INCLUDED

#include <vector>

#include <Math/Line.h>
#include <Math/Matrix.h>
#include <Math/AABBox.h>
#include "CollisionMesh.h"
#include "Collision.h"

class CollisionMeshCollection {
    public:
        class Instance {
            public:
                Instance(CollisionMesh* msh, PGE::Matrix4x4f mtrx);
                Collision checkCollision(PGE::Line3f line,float height,float radius) const;
                PGE::AABBox getBBox() const;
            private:
                Instance() {};
                CollisionMesh* mesh;
                PGE::Matrix4x4f matrix;
                PGE::AABBox bbox;
        };

        Instance* addInstance(CollisionMesh* mesh, PGE::Matrix4x4f matrix);
        void removeInstance(Instance* instance);

        Collision checkCollision(PGE::Line3f line,float height,float radius) const;
    private:
        std::vector<Instance*> instances;
};

#endif

#include "CollisionMeshCollection.h"

using namespace PGE;

CollisionMeshCollection::Instance::Instance(CollisionMesh* msh,Matrix4x4f mtrx) {
    mesh = msh; matrix = mtrx;
}

Collision CollisionMeshCollection::Instance::checkCollision(Line3f line, float radius) const {
    return mesh->checkCollision(matrix, line, radius);
}

CollisionMeshCollection::Instance* CollisionMeshCollection::addInstance(CollisionMesh* mesh,Matrix4x4f matrix) {
    Instance* instance = new Instance(mesh, matrix);
    instances.push_back(instance);
    return instance;
}

void CollisionMeshCollection::removeInstance(Instance* instance) {
    for (int i=0; i<instances.size(); i++) {
        if (instances[i] == instance) {
            instances.erase(instances.begin()+i);
            return;
        }
    }
}

Collision CollisionMeshCollection::checkCollision(Line3f line, float radius) const {
    Collision retVal; retVal.hit = false;
    for (int i=0;i<instances.size();i++) {
        Collision coll = instances[i]->checkCollision(line,radius);
        if (coll.hit) {
            if (!retVal.hit || retVal.coveredAmount>coll.coveredAmount) {
                retVal = coll;
            }
        }
    }
    return retVal;
}


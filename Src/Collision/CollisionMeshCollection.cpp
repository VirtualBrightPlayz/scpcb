#include "CollisionMeshCollection.h"

using namespace PGE;

CollisionMeshCollection::Instance::Instance(CollisionMesh* msh,Matrix4x4f mtrx) {
    mesh = msh; matrix = mtrx;

    const std::vector<Vector3f>& verts = mesh->getVertices();

    bbox = AABBox(matrix.transform(verts[0]), matrix.transform(verts[1]));

    for (int i=2;i<verts.size();i++) {
        bbox.addPoint(matrix.transform(verts[i]));
    }
}

Collision CollisionMeshCollection::Instance::checkCollision(Line3f line, float height, float radius) const {
    return mesh->checkCollision(matrix, line, height, radius);
}

AABBox CollisionMeshCollection::Instance::getBBox() const {
    return bbox;
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

Collision CollisionMeshCollection::checkCollision(Line3f line, float height, float radius) const {
    Collision retVal; retVal.hit = false;

    AABBox lineBox(line.pointA,line.pointB);
    lineBox.addPoint(lineBox.getMin().add(Vector3f(-radius,-height*0.5f,-radius)));
    lineBox.addPoint(lineBox.getMax().add(Vector3f(radius,height*0.5f,radius)));

    for (int i=0;i<instances.size();i++) {
        AABBox bbox = instances[i]->getBBox();
        if (!bbox.intersects(lineBox)) { continue; }
        Collision coll = instances[i]->checkCollision(line,height,radius);
        if (coll.hit) {
            if (!retVal.hit || retVal.coveredAmount>coll.coveredAmount) {
                retVal = coll;
            }
        }
    }
    return retVal;
}


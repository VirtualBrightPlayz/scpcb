#include "CollisionMeshCollection.h"

using namespace PGE;

CollisionMeshCollection::Instance::Instance(CollisionMeshCollection* coll,CollisionMesh* msh,Matrix4x4f mtrx, int id) {
    collection = coll;
    
    mesh = msh; matrix = mtrx;

    identifier = id;

    const std::vector<Vector3f>& verts = mesh->getVertices();

    bbox = AABBox(matrix.transform(verts[0]), matrix.transform(verts[1]));

    for (int i=2;i<verts.size();i++) {
        bbox.addPoint(matrix.transform(verts[i]));
    }
}

Collision CollisionMeshCollection::Instance::checkCollision(Line3f line, float height, float radius) const {
    return mesh->checkCollision(matrix, line, height, radius);
}

CollisionMeshCollection* CollisionMeshCollection::Instance::getCollection() const {
    return collection;
}

CollisionMesh* CollisionMeshCollection::Instance::getMesh() const {
    return mesh;
}

AABBox CollisionMeshCollection::Instance::getBBox() const {
    return bbox;
}

int CollisionMeshCollection::Instance::getId() const {
    return identifier;
}

int CollisionMeshCollection::addInstance(CollisionMesh* mesh, Matrix4x4f matrix) {
    lastInstanceId++;
    Instance instance(this, mesh, matrix, lastInstanceId);
    instances.emplace(lastInstanceId, instance);

    return instance.getId();
}

void CollisionMeshCollection::removeInstance(int instance) {
    if (instances.find(instance) != instances.end()) {
        instances.erase(instance);
    }
}

Collision CollisionMeshCollection::checkCollision(Line3f line, float height, float radius) const {
    Collision retVal; retVal.hit = false;

    AABBox lineBox(line.pointA,line.pointB);
    lineBox.addPoint(lineBox.getMin().add(Vector3f(-radius,-height*0.5f,-radius)));
    lineBox.addPoint(lineBox.getMax().add(Vector3f(radius,height*0.5f,radius)));

    for (std::map<int, Instance>::const_iterator it=instances.begin();it!=instances.end();it++) {
        AABBox bbox = it->second.getBBox();
        if (!bbox.intersects(lineBox)) { continue; }
        Collision coll = it->second.checkCollision(line,height,radius);
        if (coll.hit) {
            if (!retVal.hit || retVal.coveredAmount>coll.coveredAmount) {
                retVal = coll;
            }
        }
    }
    return retVal;
}


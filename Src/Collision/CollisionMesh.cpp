#include "CollisionMesh.h"
#include <Math/AABBox.h>
#include <Math/Plane.h>

using namespace PGE;

CollisionMesh::CollisionMesh(std::vector<Vector3f> verts,std::vector<int> inds) {
    vertices = verts; indices = inds;
}

Collision CollisionMesh::checkCollision(Line3f line,float radius,int& outTriangleIndex) {
    Collision retVal;
    retVal.hit = false;
    outTriangleIndex = -1;
    AABBox lineBox(line.pointA,line.pointB);
    lineBox.addPoint(lineBox.getMin().add(Vector3f(-radius,-radius,-radius)));
    lineBox.addPoint(lineBox.getMax().add(Vector3f(radius,radius,radius)));
    for (int i=0;i<indices.size()/3;i++) {
        AABBox triBox(vertices[indices[(i*3)+0]],vertices[indices[(i*3)+1]]);
        triBox.addPoint(vertices[indices[(i*3)+2]]);
        triBox.addPoint(triBox.getMin().add(Vector3f(-0.1f,-0.1f,-0.1f)));
        triBox.addPoint(triBox.getMax().add(Vector3f(0.1f,0.1f,0.1f)));
        if (!triBox.intersects(lineBox)) { continue; }
        Collision coll; coll.hit = false;
        coll = Collision::triangleCollide(line,radius,vertices[indices[(i*3)+0]],vertices[indices[(i*3)+1]],vertices[indices[(i*3)+2]]);
        if (coll.hit) {
            if (!retVal.hit || retVal.coveredAmount>coll.coveredAmount) {
                retVal = coll;
                outTriangleIndex = i;
            }
        }
    }
    return retVal;
}

Collision CollisionMesh::checkCollision(Line3f line,float radius) {
    int outTriangleIndex;
    return checkCollision(line,radius,outTriangleIndex);
}

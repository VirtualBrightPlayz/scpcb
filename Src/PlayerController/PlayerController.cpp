#include "PlayerController.h"

#include <cmath>
#include <Math/Plane.h>

#include "../Collision/CollisionMeshCollection.h"
#include "../World/Pickable.h"

using namespace PGE;

PlayerController::PlayerController(float r, float camHeight) {
    radius = r; cameraHeight = camHeight;

    position = Vector3f::zero;
    camAnimState = 0.f;
    currWalkSpeed = 0.f;
    blinkTimer = 0.f;
    stamina = 1.f;
    fallSpeed = 0.f;

    collisionMeshCollection = nullptr;
    
    noclip = false;
}

void PlayerController::setCollisionMeshCollection(CollisionMeshCollection* collection) {
    collisionMeshCollection = collection;
    PickableManager::cmc = collection;
}

CollisionMeshCollection* PlayerController::getCollisionMeshCollection() const {
    return collisionMeshCollection;
}

Vector3f PlayerController::getPosition() const {
    return position;
}

void PlayerController::setPosition(const Vector3f& pos) {
    position = pos;
}

void PlayerController::update(float yaw, float pitch, Input input) {
    updateMovement(yaw, pitch, input);
}

void PlayerController::updateMovement(float yaw, float pitch, Input input) {
    if ((input&(Input::Forward|Input::Backward|Input::Left|Input::Right)) == Input::None) {
        stand(); //not pressing any movement keys: we're standing still
    } else {
        float sinAngle = std::sin(yaw);
        float cosAngle = std::cos(yaw);
        float targetSpeed = WALK_SPEED_MAX;
        if ((input&Input::Sprint) != Input::None) {
            targetSpeed = SPRINT_SPEED_MAX*getClampedStamina() + WALK_SPEED_MAX*(1.f-getClampedStamina());
        }
        currWalkSpeed = currWalkSpeed*WALK_SPEED_SMOOTHING_FACTOR + targetSpeed*(1.f-WALK_SPEED_SMOOTHING_FACTOR);
        
        Vector2f targetDir = Vector2f::zero;
        if ((input&Input::Forward) != Input::None) {
            targetDir = targetDir.add(Vector2f(sinAngle,cosAngle));
        }
        if ((input&Input::Backward) != Input::None) {
            targetDir = targetDir.add(Vector2f(-sinAngle,-cosAngle));
        }
        if ((input&Input::Left) != Input::None) {
            targetDir = targetDir.add(Vector2f(-cosAngle,sinAngle));
        }
        if ((input&Input::Right) != Input::None) {
            targetDir = targetDir.add(Vector2f(cosAngle,-sinAngle));
        }
        if (targetDir.lengthSquared() < 0.01f) {
            //TODO: remove
            position = position.add(Vector3f(0.f,1.f,0.f));
            fallSpeed = 0.f;
            // -------

            stand(); //inputs cancel each other out: we're standing still
        } else {
            targetDir = targetDir.normalize();
            walk(targetDir);
            camAnimState += sqrt(currWalkSpeed/WALK_SPEED_MAX);
            while (camAnimState>=2.f*3.141592f/0.08f) {
                camAnimState-=2.f*3.141592f/0.08f;
            }
        }
    }
    applyGravity();
}

float PlayerController::getCamAnimState() const {
    return camAnimState;
}

float PlayerController::getClampedStamina() const {
    if (stamina < 0.f) { return 0.f; }
    if (stamina > 1.f) { return 1.f; }
    return stamina;
}

void PlayerController::applyGravity() {
    int iterations = 0;
    fallSpeed += GRAVITY;
    Line3f line = Line3f(position,position.add(Vector3f(0.f,-fallSpeed-(currWalkSpeed*2.f),0.f)));
    while (true) {
        Collision coll = collisionMeshCollection->checkCollision(line, cameraHeight, radius);
        if (coll.hit) {
            Vector3f resultPos = line.pointA.add(line.pointB.subtract(line.pointA).multiply(coll.coveredAmount*0.995f));
            if (iterations == 0) {
                line = Line3f(position,position.add(Vector3f(0.f,-fallSpeed,0.f)));
            }
            if (resultPos.distanceSquared(position)<0.0001f) {
                resultPos = position;
                coll.coveredAmount = 0.f;
            }
            position = resultPos;
            if (abs(coll.normal.y)>0.71f && abs(coll.normal.y-coll.surfaceNormal.y)<0.05f) {
                //hit a floor: stand still
                fallSpeed = 0.f < fallSpeed ? 0.f : fallSpeed;
                break;
            } else {
                if (iterations >= 5) { break; }
                iterations++;
                //hit a steep surface: slide down
                float remainingDist = line.pointB.subtract(position).length()*0.995f;
                Vector3f reflectedDir = line.pointB.subtract(line.pointA).reflect(coll.normal).multiply(-remainingDist);
                Plane p = Plane(coll.normal,position);
                Vector3f tempPos = position;
                tempPos = tempPos.add(reflectedDir);
                tempPos = tempPos.subtract(p.normal.multiply((tempPos.subtract(position)).dotProduct(p.normal)*0.995f));
                if (tempPos.y-position.y>=0.f) { break; } //don't allow movement in the opposite direction
                if (position.distanceSquared(tempPos)<0.0001f) {break; }
                line = Line3f(position,tempPos);
            }
        } else {
            //didn't hit anything: keep falling
            if (iterations == 0) {
                line = Line3f(position,position.add(Vector3f(0.f,-fallSpeed,0.f)));
            }
            position = line.pointB;
            break;
        }
    }
}

void PlayerController::stand() {
    currWalkSpeed = 0.f;
    stamina += STAMINA_RECOVERY_RATE;
    if (stamina > 1.f) { stamina = 1.f; }
}

void PlayerController::walk(Vector2f dir) {
    int iterations = 0;
    Vector3f startPos = position;
    Line3f line = Line3f(position,position.add(Vector3f(dir.x*currWalkSpeed,0.f,dir.y*currWalkSpeed)));
    Vector3f targetDir = line.pointB.subtract(line.pointA).normalize();
    Vector3f currDir = targetDir;
    while (true) {
        Collision coll = collisionMeshCollection->checkCollision(line, cameraHeight, radius);
        if (coll.hit) {
            Vector3f resultPos = line.pointA.add(line.pointB.subtract(line.pointA).multiply(coll.coveredAmount*0.995f));
            if (resultPos.distanceSquared(position)<0.0001f) {
                resultPos = position;
                coll.coveredAmount = 0.f;
            }
            position = resultPos;
            if (iterations >= 5) { break; }
            iterations++;
            float remainingDist = line.pointB.subtract(position).length();
            if (coll.normal.y<=0.71f) {
                //surface is too steep to climb up or pushes you down
                coll.normal.y = 0.f;
                coll.normal = coll.normal.normalize();
            }
            Vector3f reflectedDir = currDir.reflect(coll.normal).multiply(-remainingDist);
            Plane p = Plane(coll.normal,position);
            Vector3f tempPos = position;
            tempPos = tempPos.add(reflectedDir);
            tempPos = tempPos.subtract(p.normal.multiply((tempPos.subtract(position)).dotProduct(p.normal)*0.995f));
            currDir = tempPos.subtract(position).normalize();
            if (currDir.dotProduct(targetDir)<0.f && iterations>1) { break; }
            line = Line3f(position,tempPos);
        } else {
            position = line.pointB;
            break;
        }
    }
}

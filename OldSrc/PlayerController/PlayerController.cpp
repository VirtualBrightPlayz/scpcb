#include "PlayerController.h"
#include <Math/Plane.h>
#include <SDL.h>

using namespace PGE;
using namespace CBN;

PlayerController::PlayerController(float r,float camHeight) {
    radius = r; cameraHeight = camHeight;

    position = Vector3f::zero;
    camAnimState = 0.f;
    currWalkSpeed = 0.f;
    blinkTimer = 0.f;
    stamina = 1.f;
}

void PlayerController::setCollisionMeshes(std::vector<CollisionMesh*> meshes) {
    collisionMeshes = meshes;
}

Vector3f PlayerController::getPosition() const {
    return position;
}

void PlayerController::setPosition(Vector3f pos) {
    position = pos;
}

void PlayerController::update(float angle,INPUT input) {
    if ((input&(INPUT::FORWARD|INPUT::BACKWARD|INPUT::LEFT|INPUT::RIGHT)) == INPUT::NONE) {
        stand(); //not pressing any movement keys: we're standing still
    } else {
        float sinAngle = sin(angle);
        float cosAngle = cos(angle);
        float targetSpeed = WALK_SPEED_MAX;
        if ((input&INPUT::SPRINT) != INPUT::NONE) {
            targetSpeed = SPRINT_SPEED_MAX*getClampedStamina() + WALK_SPEED_MAX*(1.f-getClampedStamina());
        }
        currWalkSpeed = currWalkSpeed*WALK_SPEED_SMOOTHING_FACTOR + targetSpeed*(1.f-WALK_SPEED_SMOOTHING_FACTOR);
        
        Vector2f targetDir = Vector2f::zero;
        if ((input&INPUT::FORWARD) != INPUT::NONE) {
            targetDir = targetDir.add(Vector2f(sinAngle,cosAngle));
        }
        if ((input&INPUT::BACKWARD) != INPUT::NONE) {
            targetDir = targetDir.add(Vector2f(-sinAngle,-cosAngle));
        }
        if ((input&INPUT::LEFT) != INPUT::NONE) {
            targetDir = targetDir.add(Vector2f(-cosAngle,sinAngle));
        }
        if ((input&INPUT::RIGHT) != INPUT::NONE) {
            targetDir = targetDir.add(Vector2f(cosAngle,-sinAngle));
        }
        if (targetDir.lengthSquared() < 0.01f) {
            position = position.add(Vector3f(0.f,1.f,0.f));
            fallSpeed = 0.f;
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

Collision PlayerController::checkCollision(Line3f line) {
    Collision retVal; retVal.hit = false;
    for (int i=0;i<collisionMeshes.size();i++) {
        Collision coll = collisionMeshes[i]->checkCollision(line,radius);
        if (coll.hit) {
            if (!retVal.hit || retVal.coveredAmount>coll.coveredAmount) {
                retVal = coll;
            }
        }
    }
    return retVal;
}

void PlayerController::applyGravity() {
    int iterations = 0;
    fallSpeed += GRAVITY;
    Line3f line = Line3f(position,position.add(Vector3f(0.f,-fallSpeed-(currWalkSpeed*2.f),0.f)));
    while (true) {
        Collision coll = checkCollision(line);
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
                fallSpeed = fmin(0.f,fallSpeed);
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
        Collision coll = checkCollision(line);
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

const PlayerController::INPUT CBN::operator&(const PlayerController::INPUT& a,const PlayerController::INPUT& b) {
    return (PlayerController::INPUT)((int)a & (int)b);
}

const PlayerController::INPUT CBN::operator|(const PlayerController::INPUT& a,const PlayerController::INPUT& b) {
    return (PlayerController::INPUT)((int)a | (int)b);
}

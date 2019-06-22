#ifndef CBN_PLAYERCONTROLLER_H_INCLUDED
#define CBN_PLAYERCONTROLLER_H_INCLUDED

#include <Math/Vector.h>
#include "../Collision/Collision.h"
#include "../Collision/CollisionMesh.h"

class PlayerController {
    public:
        PlayerController(float r,float camHeight);
        void setCollisionMeshes(std::vector<CollisionMesh*> meshes);

        enum class INPUT {
            NONE = 0x0,
            FORWARD = 0x1,
            BACKWARD = 0x2,
            LEFT = 0x4,
            RIGHT = 0x8,
            SPRINT = 0x10,
            CROUCH = 0x20,
            BLINK = 0x40,
            INTERACT = 0x80
        };
        void update(float angle,INPUT input);

        PGE::Vector3f getPosition() const;
        void setPosition(PGE::Vector3f pos);

        float getCamAnimState() const;
    private:
        std::vector<CollisionMesh*> collisionMeshes;
        Collision checkCollision(PGE::Line3f line);

        float radius;
        float cameraHeight;
        PGE::Vector3f position;

        float camAnimState;
        float currWalkSpeed;
        float fallSpeed;
        float blinkTimer;
        float stamina;

        float getClampedStamina() const;

        void applyGravity();
        void stand();
        void walk(PGE::Vector2f dir);

        const float WALK_SPEED_MAX = 0.3f;
        const float SPRINT_SPEED_MAX = 0.7f;
        const float WALK_SPEED_SMOOTHING_FACTOR = 0.9f;
        const float STAMINA_RECOVERY_RATE = 0.2f / 60.f;
        const float GRAVITY = 0.01f;
};

const PlayerController::INPUT operator&(const PlayerController::INPUT& a,const PlayerController::INPUT& b);
const PlayerController::INPUT operator|(const PlayerController::INPUT& a,const PlayerController::INPUT& b);

#endif

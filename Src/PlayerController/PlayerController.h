#ifndef CBN_PLAYERCONTROLLER_H_INCLUDED
#define CBN_PLAYERCONTROLLER_H_INCLUDED

#include <Math/Vector.h>
#include "../Input/Input.h"

class CollisionMeshCollection;

class PlayerController {
    public:
        PlayerController(float r, float camHeight);
    
        bool noclip;
    
        void setCollisionMeshCollection(CollisionMeshCollection* collection);
        CollisionMeshCollection* getCollisionMeshCollection() const;
        void update(float angle, float angleY, Input input);

        PGE::Vector3f getPosition() const;
        void setPosition(const PGE::Vector3f& pos);

        float getCamAnimState() const;
    private:
        CollisionMeshCollection* collisionMeshCollection;

        float radius;
        float cameraHeight;
        PGE::Vector3f position;

        float camAnimState;
        float currWalkSpeed;
        float fallSpeed;
        float blinkTimer;
        float stamina;

        float getClampedStamina() const;
    
        void updateMovement(float angle, float angleY, Input input);

        void applyGravity();
        void stand();
        void walk(PGE::Vector2f dir);

        const float WALK_SPEED_MAX = 0.3f;
        const float SPRINT_SPEED_MAX = 0.7f;
        const float WALK_SPEED_SMOOTHING_FACTOR = 0.9f;
        const float STAMINA_RECOVERY_RATE = 0.2f / 60.f;
        const float GRAVITY = 0.01f;
};

#endif

#ifndef PLAYERCONTROLLER_H_INCLUDED
#define PLAYERCONTROLLER_H_INCLUDED

#include <Math/Vector.h>

#include "../Input/Input.h"

class CollisionMeshCollection;
class Collider;

class PlayerController {
    public:
        PlayerController(float r, float camHeight);
        ~PlayerController();
        
        PGE::Vector3f position;
        bool noclip;
    
        void setCollisionMeshCollection(CollisionMeshCollection* collection);
        void update(float angle, float angleY, Input input);

        float getCamAnimState() const;

    private:
        Collider* collider;

        float camAnimState;
        float currWalkSpeed;
        float stamina;

        float getClampedStamina() const;
    
        void updateMovement(float angle, float angleY, Input input);

        void stand();
        void walk(PGE::Vector2f dir);

        const float WALK_SPEED_MAX = 0.3f;
        const float SPRINT_SPEED_MAX = 0.7f;
        const float WALK_SPEED_SMOOTHING_FACTOR = 0.9f;
        const float STAMINA_RECOVERY_RATE = 0.2f / 60.f;
};

#endif // PLAYERCONTROLLER_H_INCLUDED

#ifndef PICKABLE_H_INCLUDED
#define PICKABLE_H_INCLUDED

#include <Math/Vector.h>

class Camera;
class CollisionMeshCollection;
class KeyBinds;
class UIMesh;

class Pickable {
    public:
        PGE::Vector3f position;
        bool picked;
        float cachedDistance;

        Pickable();

        bool getPicked();
};

class PickableManager {
    private:
        KeyBinds* keyBinds;
        UIMesh* uiMesh;

        std::vector<Pickable*> activePickables;
        std::vector<Pickable*> validPickables;
        Pickable* currentPickable;
        Camera* camera; // TOOD: Replace this with PlayerController.
        
        PickableManager();

    public:
        static CollisionMeshCollection* cmc; // TODO: TEMPORARILY STATIC, need to move CollisionMeshCollection to C++.

        PickableManager(Camera* cam, UIMesh* um, KeyBinds* kb);

        void activatePickable(Pickable* p);
        void deactivatePickable(Pickable* p);

        void update();
        void render();
};

#endif // PICKABLE_H_INCLUDED

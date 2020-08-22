#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <Mesh/Mesh.h>

#include "../World/Pickable.h"

class ModelInstance;
class GraphicsResources;
class Inventory;

// TODO: Move all of this to Angelscript.
class Item : public Pickable {
    private:
        GraphicsResources* gfxRes;
        PickableManager* pickMan;
        Inventory* inventory;

        ModelInstance* model;
        PGE::Texture* invIcon;

        bool picked;
        
    public:
        Item(const PGE::String& filename, const PGE::String& icon, int x, GraphicsResources* gr, PickableManager* pm, Inventory* inv);
        ~Item();

        //PGE::Vector3f getPos() const override;
        //void onPicked() override;

        PGE::Texture* getInvIcon() const;

        // Should return whether or not this item was equipped.
        virtual bool onUse() { return true; }; // This will be purely virtual
        virtual void onDrop() {}; // This will be purely virtual

        virtual void update() {}; // This will be purely virtual
        void render() const;
};

#endif // ITEM_H_INCLUDED

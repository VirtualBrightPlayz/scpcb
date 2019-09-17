#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <Math/Vector.h>
#include <Misc/String.h>
#include <Texture/Texture.h>

class GraphicsResources;
class TxtManager;

enum class ItemPickSound {
    Tiny = 0,
    Metallic = 1,
    Heafty = 2,
    Paper = 3
};

class Item {
    private:
        const float hideDistance = 16.f;

        PGE::Vector3f position;
        PGE::Vector3f rotation;
        float scale;

        // Whether the item was selected in the inventory.
        bool selected;
        float dropSpeed;
        bool needsInventoryIcon;

        ItemPickSound pickSound;
    
        PGE::Texture* inventoryIcon;
    
        GraphicsResources* gfxRes;
    
        void generateInventoryIcon();
    
    protected:
        Item(GraphicsResources* gfx, TxtManager* tm, const PGE::FileName& path, float scale, ItemPickSound ips);
        virtual ~Item();
    
        TxtManager* txtM;

    public:
        bool markedForRemoval;
    
        virtual PGE::String getType() const=0;
        virtual PGE::String getInvName() const=0;
    
        void setPosition(float x, float y, float z);

        // Called when the player picks up the item.
        virtual void onPick();
        // Called when the player selects the item in the inventory.
        virtual void onUse();

        void update();
        virtual void updateInternal();
        virtual void render() const;
};

#endif // ITEM_H_INCLUDED

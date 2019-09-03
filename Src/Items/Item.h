#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <Math/Vector.h>
#include <Misc/String.h>

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

        // Whether the item was selected in the inventory.
        bool selected;

        ItemPickSound pickSound;

    public:
        virtual PGE::String getInvName()=0;

        // Called when the player picks up the item.
        virtual void onPick();
        // Called when the player selects the item in the inventory.
        virtual void onUse();

        // Called while the item is selected in the inventory.
        virtual void updateUse();
        virtual void renderUse();

        void update();
        virtual void updateInternal();
        virtual void render()=0;
};

#endif // ITEM_H_INCLUDED

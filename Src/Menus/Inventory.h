#ifndef INVENTORY_H_INCLUDED
#define INVENTORY_H_INCLUDED

#include <Math/Vector.h>

#include "Menu.h"

class Item;
class InvSlot;
class World;
class UIMesh;
class KeyBinds;
class Config;

// Almost fully transferred
class Inventory : public Menu {
    private:
        static constexpr int maxItemsPerRow = 6;

        enum class SubState {
            Main,
            DocumentViewer,
            ObjectivesMenu
        };

        SubState currState;
        void setState(SubState state);

        int size;
        InvSlot** slots;
    
    public:
        Inventory(World* wrld, UIMesh* um, KeyBinds* kb, Config* con, int size);
        ~Inventory();

        bool addItem(Item* it);
    
        void update(const PGE::Vector2f& mousePosition, const PGE::Vector2i& mouseWheelDelta) override;
        void render() const override;
};

#endif // INVENTORY_H_INCLUDED

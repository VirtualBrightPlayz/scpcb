#ifndef INVENTORY_H_INCLUDED
#define INVENTORY_H_INCLUDED

#include <Math/Vector.h>

class InvSlot;
class World;
class UIMesh;
class KeyBinds;
class Config;

class Inventory {
private:
    constexpr static int MAX_ITEM_COUNT = 6;
    constexpr static int ITEMS_PER_ROW = MAX_ITEM_COUNT / 2;
    
    InvSlot* slots[MAX_ITEM_COUNT];

    enum class SubState {
        Main,
        DocumentViewer,
        ObjectivesMenu,
        Hidden
    };
    SubState currState;
    void setState(SubState state);
    
public:
    Inventory(UIMesh* um, KeyBinds* kb, Config* con);
    ~Inventory();
    
    void update(PGE::Vector2f mousePos);
    void render() const;
};

#endif // INVENTORY_H_INCLUDED

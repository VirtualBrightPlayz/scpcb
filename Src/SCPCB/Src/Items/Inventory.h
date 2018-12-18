#ifndef INVENTORY_H_INCLUDED
#define INVENTORY_H_INCLUDED
#include <StringType.h>
#include <vector>

class MeshModel;
class bbImage;
class Texture;
class Pivot;

namespace CBN {

class Item;

class ItemCell {
private:
    bool hover;
    Item* val;
    int size;

public:
    static const int SIZE = 70;
    ItemCell(int size = SIZE);
    ~ItemCell();

    bool isHovering();
    bool isEmpty();

    bool contains(Item* it);
    Item* getItem();
    void insertItem(Item* it);
    void removeItem();

    void update(int x, int y);
    void draw(int x, int y, int cellSpacing);
};

class Inventory {
private:
    ItemCell* items;
    ItemCell* equipSlots;
    int size;

    void setItem(Item* it, int slot);
    int getIndex(Item* it) const;

    void updateMainInv();
    void updateEquipInv();

public:
    Inventory(int size, int itemsPerRow = 5);
    ~Inventory();
    
    // Main inventory.
    int itemsPerRow;
    int xOffset;
    int yOffset;
    int spacing;

    int getSize() const;
    void addItem(Item* it);
    bool anyRoom() const;

    // Equip Slot <-> Inventory
    void moveItem(Item* it, WornItemSlot slot);
    // Move item (from some index in the inv) to another slot in the inv.
    void moveItem(Item* it, int destIndex);
    void useItem(Item* it);
    void dropItem(Item* it);

    void update();
    void draw();
};

}
#endif // INVENTORY_H_INCLUDED

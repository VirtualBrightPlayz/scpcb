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

struct ItemCell {
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
    void insertItem(Item* it);
    Item* removeItem();

    void update(int x, int y);
    void draw(int x, int y, int cellSpacing);
};

class Inventory {
private:
    ItemCell* items;
    ItemCell* equipSlots;
    int size;

    bool hoveringOverSlot;

    void setItem(Item* it, int slot);

    void updateMainInv();
    void updateEquipInv();

public:
    Inventory(int size, int itemsPerRow = 5);
    ~Inventory();

    int itemsPerRow;
    int xOffset;
    int yOffset;
    int spacing;

    int getSize() const;
    void addItem(Item* it);
    bool anyRoom() const;

    void useItem(Item* it);
    void dropItem(Item* it);

    void update();
    void draw();
};

}
#endif // INVENTORY_H_INCLUDED

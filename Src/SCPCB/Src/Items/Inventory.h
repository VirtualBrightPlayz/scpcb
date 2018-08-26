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
public:
    ItemCell();
    ~ItemCell();

    Item* val;
    static const int SIZE = 70;
    void update(int x, int y);
    void draw(int x, int y, int cellSpacing);
    bool isHovering();
};

class Inventory {
private:
    ItemCell* items;
    int size;

public:
    Inventory(int size, int itemsPerRow = 5);
    ~Inventory();

    bool displayVertical;
    int itemsPerRow;
    int xOffset;
    int yOffset;
    int spacing;

    int getSize();
    void addItem(Item* it, int slot = -1);
    Item* getItem(int slot);
    void removeItem(int slot);
    bool anyRoom();

    void update();

    void saveXML();

    static void toggleInventory();
};

}
#endif // INVENTORY_H_INCLUDED

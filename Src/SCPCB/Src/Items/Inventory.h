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
public:
    static const int SIZE = 70;
    ItemCell();
    ~ItemCell();

    Item* val;
    bool isHovering();
    void update(int x, int y);
    void draw(int x, int y, int cellSpacing);
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
    void addItem(Item* it);
    void setItem(Item* it, int slot);
    Item* getItem(int slot);
    int getIndex(Item* it);
    void removeItem(Item* it);
    bool anyRoom();

    void update();
    void draw();

    void saveXML();

    static void toggleInventory();
};

}
#endif // INVENTORY_H_INCLUDED

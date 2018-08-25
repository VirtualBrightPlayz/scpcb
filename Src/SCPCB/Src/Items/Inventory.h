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

class Inventory {
private:
    std::vector<Item*> items;

public:
    Inventory(int size);
    ~Inventory();

    int getSize();
    void addItem(Item* it, int slot = -1);
    Item* getItem(int slot);
    void removeItem(int slot);

    void saveXML();
};

}
#endif // INVENTORY_H_INCLUDED

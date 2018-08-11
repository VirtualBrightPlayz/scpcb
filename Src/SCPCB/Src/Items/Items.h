#ifndef ITEMS_H_INCLUDED
#define ITEMS_H_INCLUDED
#include <StringType.h>
#include <vector>
#include <StringType.h>

class MeshModel;
class bbImage;
class Texture;
class Pivot;

namespace CBN {

// Constants.
const String ITEM_TAG_914F("914_fine");
const String ITEM_TAG_914VF("914_veryfine");
const String ITEM_TAG_OMNI("omni");

// TODO: Rename these.
enum class ITEMPICK_SOUND {
    PAPER = 0,
    SMALL = 2,
    MEDIUM = 1,
    LARGE = 2
};

const int MAX_ITEM_COUNT = 20;
const int ITEM_CELL_SIZE = 70;
const int ITEM_CELL_SPACING = 35;
const int ITEMS_PER_ROW = 3;


// Structs.
struct ItemTemplate {
private:
    static std::vector<ItemTemplate*> list;

public:
    ItemTemplate();
    ~ItemTemplate();
    static int getListSize();
    static ItemTemplate* getObject(int index);

    String name;
    String invName;

    MeshModel* obj;
    String objPath;

    bbImage* invImage[2];
    String invImagePath[2];

    Texture* tex;
    String texPath;

    ITEMPICK_SOUND sound;

    int wornSlot;
    bool wornOnly;

    float scale;
};

struct Item {
private:
    static std::vector<Item*> list;

public:
    Item();
    ~Item();
    static int getListSize();
    static Item* getObject(int index);

    String name;
    Pivot* collider;
    MeshModel* model;
    struct ItemTemplate* itemTemplate;
    bbImage* img;
    int id;

    int r;
    int g;
    int b;
    float a;

    float dist;

    float state;
    //TODO: Deprecate
    float state2;

    int picked;
    int dropped;

    bbImage* invImage;

    int wontColl = false;
    float xspeed;
    float zspeed;
    float dropSpeed;

    String tags[5];
    struct Inventory* subInventory = nullptr;
};

struct Inventory {
private:
    static std::vector<Inventory*> list;

public:
    Inventory();
    ~Inventory();
    static int getListSize();
    static Inventory* getObject(int index);

    struct Item* items[MAX_ITEM_COUNT];
    int size = 10;
    Inventory* parent = nullptr;
};

// Globals.
extern int LastItemID;
extern int itemDistanceTimer;

// Functions.
void CreateItemTemplate(const String& file, const String& section);

void LoadItemTemplates(const String& file);

Inventory* CreateInventory(int size);

void DeleteInventory(Inventory* inv);

int CountItemsInInventory(Inventory* inv);

Item* CreateItem(const String& name, float x, float y, float z, int invSlots = 0);

Item* CreatePaper(const String& name, float x, float y, float z);

void RemoveItem(Item* i);

void UpdateItems();

void PickItem(Item* item);

void DropItem(Item* item, Inventory* inv);

void AssignTag(Item* item, const String& tag);

void RemoveTag(Item* item, const String& tag);

int HasTag(Item* item, const String& tag);

int IsPlayerWearingItem(struct Player* player, const String& itemName);

void UseItem(Inventory* inv, int index);

void DeEquipItem(Item* item);

void UpdateInventory(Player* player);

void DrawInventory(Player* player);

void ToggleInventory(Player* player);

}
#endif // ITEMS_H_INCLUDED

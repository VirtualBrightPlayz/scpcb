#ifndef ITEMS_H_INCLUDED
#define ITEMS_H_INCLUDED
#include <vector>

namespace CBN {

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

    int obj;
    String objPath;

    int invImage[2];
    String invImagePath[2];

    int tex;
    String texPath;

    int sound;

    int wornSlot;
    int wornOnly;

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
    int collider;
    int model;
    ItemTemplate* template;
    int img;
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

    int invImage;

    int wontColl = false;
    float xspeed;
    float zspeed;
    float dropSpeed;

    String tags[5];
    Inventory* subInventory = nullptr;
};

struct Inventory {
private:
    static std::vector<Inventory*> list;

public:
    Inventory();
    ~Inventory();
    static int getListSize();
    static Inventory* getObject(int index);

    Item* items[MAX_ITEM_COUNT];
    int size = 10;
    Inventory* parent = nullptr;
};

// Constants.
extern const String ITEM_TAG_914F;
extern const String ITEM_TAG_914VF;
extern const String ITEM_TAG_OMNI;
extern const int ITEMPICK_SOUND_PAPER;
extern const int ITEMPICK_SOUND_MEDIUM;
extern const int ITEMPICK_SOUND_LARGE;
extern const int ITEMPICK_SOUND_SMALL;
extern const int MAX_ITEM_COUNT;
extern const int ITEM_CELL_SIZE;
extern const int ITEM_CELL_SPACING;
extern const int ITEMS_PER_ROW;

// Globals.
extern int LastItemID;
extern int itemDistanceTimer;

// Functions.
void CreateItemTemplate(String file, String section);

void LoadItemTemplates(String file);

Inventory* CreateInventory(int size);

void DeleteInventory(Inventory* inv);

int CountItemsInInventory(Inventory* inv);

Item* CreateItem(String name, float x, float y, float z, int invSlots = 0);

Item* CreatePaper(String name, float x, float y, float z);

void RemoveItem(Item* i);

void UpdateItems();

void PickItem(Item* item);

void DropItem(Item* item, Inventory* inv);

void AssignTag(Item* item, String tag);

void RemoveTag(Item* item, String tag);

int HasTag(Item* item, String tag);

int IsPlayerWearingItem(Player* player, String itemName);

void UseItem(Inventory* inv, int index);

void DeEquipItem(Item* item);

void UpdateInventory(Player* player);

void DrawInventory(Player* player);

void ToggleInventory(Player* player);

}
#endif // ITEMS_H_INCLUDED

#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <vector>
#include <StringType.h>

class bbImage;
class MeshModel;
class Texture;
class Pivot;

namespace CBN {

class Inventory;

// FIXME: MOVE SOMEWHERE RELEVANT
enum class Setting914 {
    Rough,
    Coarse,
    OneToOne,
    Fine,
    VeryFine
};

const int WORNITEM_SLOT_COUNT = 3;
enum class WornItemSlot {
    None,
    Hand = 0,
    Body = 1,
    Head = 2
};

class Item {
private:
    Item(const String& meshPath, enum ItemPickSound sound, WornItemSlot slot = WornItemSlot::None);

    static std::vector<Item*> list;

    static int ID;
    void addID();
    void setID(int id);

protected:
    enum class ItemPickSound {
        Tiny,
        Metallic,
        Heafty,
        Paper
    };

    int id;
    bool markedForRemoval;

    ItemPickSound pickSound;
    WornItemSlot wornSlot;
    bool wornOnly;

    std::vector<String> tags;

    String meshPath;
    MeshModel* mesh;
    bool needsInvImg;
    bbImage* invImg;
    float scale;
    float dist;

    Pivot* collider;
    bool wontColl;
    float xSpeed;
    float zSpeed;
    float dropSpeed;

public:
    ~Item();
    static int itemDistanceTimer;
    Inventory* parentInv;

    virtual String getType()=0;
    virtual String getInvName()=0;

    void assignTag(const String& tag);
    bool hasTag(const String& tag);
    void removeTag(const String& tag);

    virtual void OnPick();
    virtual void OnUse();
    virtual void On914Use(Setting914 setting);

    virtual void update();
    virtual void draw();
    // Functions to call when the item's selected.
    // (I.E. the first aid kit.)
    virtual void updateUse()=0;
    virtual void drawUse()=0;

    // TODO: tinyxml2
    virtual void saveXML();

    static void updateAll();
    static void drawAll();
};

const String ITEM_TAG_OMNI("omni"); // TODO: Move to keycards.

}
#endif // ITEM_H_INCLUDED

#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <vector>
#include <StringType.h>

class bbImage;
class MeshModel;
class Texture;
class Pivot;

namespace CBN {

// FIXME: MOVE SOMEWHERE RELEVANT
enum class Setting914 {
    Rough,
    Coarse,
    OneToOne,
    Fine,
    VeryFine
};

enum class WornItemSlot {
    None,
    Hand,
    Body,
    Head
};

class Item {
private:
    static std::vector<Item*> list;
protected:
    enum class ItemPickSound {
        Tiny,
        Metallic,
        Heafty,
        Paper
    };
    Item(const String& meshPath, ItemPickSound sound, WornItemSlot slot = WornItemSlot::None);
    ~Item();

    int id; // TODO:

    ItemPickSound pickSound;
    WornItemSlot wornSlot;
    bool wornOnly;

    std::vector<String> tags;

    String meshPath;
    MeshModel* mesh;
    bool needInvImg;
    bbImage* invImg;
    float scale;
    float dist;
    bool picked;
    bool dropped;

    Pivot* collider;
    bool wontColl;
    float xSpeed;
    float zSpeed;
    float dropSpeed;

public:
    virtual String getType()=0;
    virtual String getInvName()=0;

    void assignTag(const String& tag);
    bool hasTag(const String& tag);
    void removeTag(const String& tag);

    virtual void OnPick();
    virtual void OnUse();
    virtual void On914Use(Setting914 setting);

    virtual void update();
    // Functions to call when the item's selected.
    // (I.E. the first aid kit.)
    virtual void updateUse()=0;
    virtual void drawUse()=0;

    // TODO: tinyxml2
    virtual void saveXML();
    virtual void loadXML();

    static void updateAll();
    static void drawAll();
};

const String ITEM_TAG_OMNI("omni"); // TODO: Move to keycards.

}
#endif // ITEM_H_INCLUDED

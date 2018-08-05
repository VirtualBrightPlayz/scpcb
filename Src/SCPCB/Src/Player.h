#ifndef PLAYER_H_INCLUDED
#define PLAYER_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Player {
private:
    static std::vector<Player*> list;

public:
    Player();
    ~Player();
    static int getListSize();
    static Player* getObject(int index);

    //entities
    class Object* collider;

    int head;
    float headPitch;
    float headYaw;

    int cam;
    float camShake;
    float camAnimState;
    float camZoom;

    float camFogNear;
    float camFogFar;

    int overlays[OVERLAY_COUNT];

    int grabbedEntity;

    struct Item* closestItem;
    struct Item* selectedItem;

    int closestButton;
    struct Door* closestDoor;
    struct Door* selectedDoor;

    int drawDirectionalArrow[4];
    //------------

    //movement states
    int crouching;
    float crouchState;

    float moveSpeed;
    float dropSpeed;

    float blinkFreq;
    float blinkTimer;
    float stamina;

    int footstepOverride;
    //------------

    //ailments
    int dead;
    float fallTimer;

    float injuries;
    float bloodloss;

    float heartbeatIntensity;

    float superMan;

    float infect008;

    float sanity895;

    float forceMove;
    float forceAngle;

    int disableControls;

    float blinkEffect;
    float blinkEffectTimer;
    float staminaEffect;
    float staminaEffectTimer;

    float lightFlash;
    float blurTimer;

    float loudness;
    //------

    //items
    struct Inventory* inventory;
    struct Inventory* openInventory;
    //------

    //sounds
    struct IntArray* breathingSFX;
    int bloodDripSFX[4];
    int damageSFX[9];
    int coughSFX[3];
    int heartbeatSFX;

    int breathChn;
    //------

    //other states
    struct Room* currRoom;

    int godMode;
    int noclip;
    //------
};

// Constants.
extern const int WORNITEM_SLOT_COUNT;
extern const int WORNITEM_SLOT_NONE;
extern const int WORNITEM_SLOT_HEAD;
extern const int WORNITEM_SLOT_BODY;
extern const int WORNITEM_SLOT_HAND;
extern const int PLAYER_INV_COUNT;
extern const int OVERLAY_COUNT;
extern const int OVERLAY_BLACK;
extern const int OVERLAY_WHITE;
extern const int OVERLAY_FOG;
extern const int OVERLAY_GASMASK;
extern const int OVERLAY_NIGHTVISION;
extern const int OVERLAY_008;

// Globals.
extern Player* mainPlayer;
extern int viewport_center_x;
extern int viewport_center_y;
extern float mouselook_x_inc;
extern float mouselook_y_inc;
extern int mouse_left_limit;
extern int mouse_right_limit;
extern int mouse_top_limit;
extern int mouse_bottom_limit;
extern float mouse_x_speed_1;
extern float mouse_y_speed_1;

// Functions.
Player* CreatePlayer();

void DeletePlayer(Player* player);

void UpdatePlayer();

void MouseLook();

int SpaceInInventory(Player* player);

void Kill(Player* player);

}
#endif // PLAYER_H_INCLUDED

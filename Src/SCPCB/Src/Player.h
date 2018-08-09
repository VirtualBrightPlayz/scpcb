#ifndef PLAYER_H_INCLUDED
#define PLAYER_H_INCLUDED
#include <vector>

class Pivot;
class Camera;
class gxSound;
class gxChannel;
class MeshModel;
class Texture;
class Object;
class Sprite;

namespace CBN {

// Constants.
const int WORNITEM_SLOT_COUNT = 3;
const int WORNITEM_SLOT_NONE = WORNITEM_SLOT_COUNT;
const int WORNITEM_SLOT_HEAD = 0;
const int WORNITEM_SLOT_BODY = 1;
const int WORNITEM_SLOT_HAND = 2;
const int PLAYER_INV_COUNT = 6;
const int OVERLAY_COUNT = 6;
const int OVERLAY_BLACK = 0;
const int OVERLAY_WHITE = 1;
const int OVERLAY_FOG = 2;
const int OVERLAY_GASMASK = 3;
const int OVERLAY_NIGHTVISION = 4;
const int OVERLAY_008 = 5;


// Structs.
struct Player {
public:
    Player();
    ~Player();

    //entities
    Pivot* collider;

    Pivot* head;
    float headPitch;
    float headYaw;

    Camera* cam;
    float camShake;
    float camAnimState;
    float camZoom;

    float camFogNear;
    float camFogFar;

    Sprite* overlays[OVERLAY_COUNT];

    Object* grabbedEntity;

    struct Item* closestItem;
    struct Item* selectedItem;

    MeshModel* closestButton;
    struct Door* closestDoor;
    struct Door* selectedDoor;

    int drawDirectionalArrow[4];
    //------------

    //movement states
    bool crouching;
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

    bool disableControls;

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
    gxSound* breathingSFX[2][5];
    gxSound* bloodDripSFX[4];
    gxSound* damageSFX[9];
    gxSound* coughSFX[3];
    gxSound* heartbeatSFX;

    gxChannel* breathChn;
    //------

    //other states
    struct Room* currRoom;

    int godMode;
    int noclip;
    //------
};

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

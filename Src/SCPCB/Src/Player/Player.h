#ifndef PLAYER_H_INCLUDED
#define PLAYER_H_INCLUDED
#include <vector>
#include <StringType.h>

class Pivot;
class Camera;
class gxSound;
class gxChannel;
class MeshModel;
class Texture;
class Object;
class Sprite;

namespace CBN {

class Item;
struct ItemCell;
class Inventory;
struct Door;
struct Room;

// Constants.
const int PLAYER_INV_COUNT = 6;
const int OVERLAY_COUNT = 6;
const int OVERLAY_BLACK = 0;
const int OVERLAY_WHITE = 1;
const int OVERLAY_FOG = 2;
const int OVERLAY_GASMASK = 3;
//const int OVERLAY_NIGHTVISION = 4;
const int OVERLAY_008 = 5;

// Structs.
struct Player {
private:
    bool disableCameraControls;

    float moveAngle;
    // Any movement happening. For acceleration.
    bool isMoving;
    bool isSprinting;
    bool crouched;
    float speed;
    const float DEFAULT_SPEED = 0.018f;
    // Multiplied with speed. Used for sprint, crouching, ect.
    float speedMultiplier;

    float overlayBlackAlpha;

    void updateInput();
    void updateMouseInput();
    void updateMove();
    void updateNoClip();
    void mouseLook();

    void updateBlink();
    void updateOverlays();

    void updateItemUse();
    void updateInjuries();
    void update895Sanity();
    void updateDeathAnim();
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

    Item* closestItem;
    Item* selectedItem;
    MeshModel* closestButton;
    Door* closestDoor;
    Door* selectedDoor;

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

    // TODO: Enum.
    int footstepOverride;
    //------------

    //ailments
    int dead;
    float fallTimer;

    float injuries;
    float bloodloss;

    float heartbeatIntensity;

    float infect008;

    float sanity895;

    float forceWalk;
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

    Inventory* inventory;

    //sounds
    gxSound* breathingSFX[2][5];
    gxSound* bloodDripSFX[4];
    gxSound* damageSFX[9];
    gxSound* coughSFX[3];
    gxSound* heartbeatSFX;

    gxChannel* breathChn;
    //------

    //other states
    Room* currRoom;

    bool godMode;
    bool noclip;
    //------

    void update();
    void kill();

    void toggleInventory();

    void pickItem(Item* it);
    void useItem(Item* it);
    void dropItem(Item* it);
    bool isEquipped(const String& itType);
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

}
#endif // PLAYER_H_INCLUDED

#ifndef ASSETS_H_INCLUDED
#define ASSETS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct AssetWrap {
private:
    static std::vector<AssetWrap*> list;

public:
    AssetWrap();
    ~AssetWrap();
    static int getListSize();
    static AssetWrap* getObject(int index);

    int asType;
    int grabCount;
    String file;
    float decayTimer;
    int intVal;
};

struct UIAssets {
private:
    static std::vector<UIAssets*> list;

public:
    UIAssets();
    ~UIAssets();
    static int getListSize();
    static UIAssets* getObject(int index);

    //Misc. Interface
    int back;
    int scpText;
    int tileWhite;
    int tileBlack;
    int scp173;

    int pauseMenuBG;

    int cursorIMG;
    int arrow[4];

    int font[4];
    int consoleFont;

    //HUD
    int sprintIcon;
    int blinkIcon;
    int crouchIcon;
    int handIcon[2];
    int blinkBar;
    int staminaBar;
    int keypadHUD;

};

// Constants.
extern const int ASSET_NONE;
extern const int ASSET_TEXTURE;
extern const int ASSET_IMAGE;
extern const int ASSET_MESH;
extern const int ASSET_ANIM_MESH;
extern const int ASSET_DECAY_TIMER;
extern const int BLEND_ADD;
extern const int GORE_PIC_COUNT;
extern const int HAND_ICON_TOUCH;
extern const int HAND_ICON_GRAB;

// Globals.
extern UIAssets* uiAssets;

// Functions.
AssetWrap* CreateAsset(String filePath, int asType, int flag = 1);

void FreeAsset(AssetWrap* as);

int GrabAsset(String filePath, int asType, int flag = 1);

int GrabTexture(String filePath, int flag = 1);

int GrabImage(String filePath);

int GrabMesh(String filePath);

void DropAsset(int obj);

void UpdateAssets();

void InitializeUIAssets();

void ReleaseUIAssets();

void LoadEntities();

void InitNewGame();

void InitLoadGame();

void NullGame();

String GetImagePath(String path);

}
#endif // ASSETS_H_INCLUDED

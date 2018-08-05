#ifndef ASSETS_H_INCLUDED
#define ASSETS_H_INCLUDED
#include <StringType.h>
#include <vector>

namespace CBN {

// Structs.
class AssetWrap {
public:
    enum class TYPE {
        ASSET_NONE,
        ASSET_TEXTURE,
        ASSET_IMAGE,
        ASSET_MESH,
        ASSET_ANIM_MESH
    };

    virtual TYPE getType() =0;

    static void update();
protected:
    static std::vector<AssetWrap*> list;

    int grabCount;

    String file;

    float decayTimer;

    static AssetWrap* grab(String filePath, TYPE asType, int flag = 1);
    static AssetWrap* create(String filePath, TYPE asType, int flag = 1);
    static void drop(AssetWrap* asset);
};

class TextureAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();

    class Texture* getTexture();

    static TextureAssetWrap* grab(String filePath, int flag = 1);
private:
    int flags;
    class Texture* texture;

    TextureAssetWrap(String filePath, int flag = 1);
    ~TextureAssetWrap();
};

class ImageAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();

    class bbImage* getImage();

    static ImageAssetWrap* grab(String filePath);
private:
    class bbImage* image;

    ImageAssetWrap(String filePath);
    ~ImageAssetWrap();
};

class MeshAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();

    class Entity* getMesh();

    static ImageAssetWrap* grab(String filePath, bool isAnimated = true);
private:
    bool animated;
    class Entity* mesh;

    MeshAssetWrap(String filePath);
    ~MeshAssetWrap();
};

struct UIAssets {
public:
	UIAssets();
	~UIAssets();

    //Misc. Interface
    class bbImage* back;
	class bbImage* scpText;
	class bbImage* tileWhite;
	class bbImage* tileBlack;
	class bbImage* scp173;

	class bbImage* pauseMenuBG;

	class bbImage* cursorIMG;
	class bbImage* arrow[4];

	class gxFont* font[4];
	class gxFont* consoleFont;

    //HUD
	class bbImage* sprintIcon;
	class bbImage* blinkIcon;
	class bbImage* crouchIcon;
	class bbImage* handIcon[2];
	class bbImage* blinkBar;
	class bbImage* staminaBar;
	class bbImage* keypadHUD;

};

// Constants.
extern const int ASSET_DECAY_TIMER;
extern const int BLEND_ADD;
extern const int GORE_PIC_COUNT;
extern const int HAND_ICON_TOUCH;
extern const int HAND_ICON_GRAB;

// Globals.
extern UIAssets* uiAssets;

// Functions.
void LoadEntities();

void InitNewGame();

void InitLoadGame();

void NullGame();

String GetImagePath(String path);

}
#endif // ASSETS_H_INCLUDED

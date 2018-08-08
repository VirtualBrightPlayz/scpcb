#ifndef ASSETS_H_INCLUDED
#define ASSETS_H_INCLUDED
#include <StringType.h>
#include <vector>

namespace CBN {

// Constants.
const int ASSET_DECAY_TIMER = 10 * 70;
const int BLEND_ADD = 3;
const int GORE_PIC_COUNT = 6;
const int HAND_ICON_TOUCH = 0;
const int HAND_ICON_GRAB = 1;

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
    virtual void drop() =0;

    static void update();
protected:
    int grabCount;

    String file;

    float decayTimer;
};

class TextureAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();
    virtual void drop();

    class Texture* getTexture();

    static TextureAssetWrap* grab(String filePath, int flag = 1);

    static void update();
private:
    static std::vector<TextureAssetWrap*> list;
    
    int flags;
    class Texture* texture;

    TextureAssetWrap(String filePath, int flag = 1);
    ~TextureAssetWrap();
};

class ImageAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();
    virtual void drop();

    class bbImage* getImage();

    static ImageAssetWrap* grab(String filePath);

    static void update();
private:
    static std::vector<ImageAssetWrap*> list;
    
    class bbImage* image;

    ImageAssetWrap(String filePath);
    ~ImageAssetWrap();
};

class MeshAssetWrap : public AssetWrap {
public:
    virtual TYPE getType();
    virtual void drop();

    class MeshModel* getMesh();

    static MeshAssetWrap* grab(String filePath, bool isAnimated = true);

    static void update();
private:
    static std::vector<MeshAssetWrap*> list;
    
    bool animated;
    class Object* mesh;

    MeshAssetWrap(String filePath, bool isAnimated = true);
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

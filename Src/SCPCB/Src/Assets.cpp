//library includes
#include <StringType.h>
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <bbinput.h>
#include <bbaudio.h>
#include <bbfilesystem.h>
#include <bbmath.h>
#include <iostream>

//project includes
#include "Assets.h"
#include "GameMain.h"
#include "Player.h"
#include "Dreamfilter.h"
#include "Items/Items.h"
#include "MapSystem.h"
#include "Materials.h"
#include "Doors.h"
#include "Decals.h"
#include "Audio.h"
#include "NPCs/NPCs.h"
#include "Menus/Menu.h"
#include "Menus/LoadingScreen.h"
#include "Options.h"
#include "Events.h"
#include "Skybox.h"
#include "NPCs/NPCtypeMTF.h"
#include "Console.h"
#include "FastResize.h"

#include "Rooms/Room_dimension1499.h" //TODO: AAAAAA

namespace CBN {

// Structs.
std::vector<TextureAssetWrap*> TextureAssetWrap::list;
std::vector<ImageAssetWrap*> ImageAssetWrap::list;
std::vector<MeshAssetWrap*> MeshAssetWrap::list;

UIAssets::UIAssets() {
	back = bbLoadImage("GFX/menu/back.jpg");
	scpText = bbLoadImage("GFX/menu/scptext.jpg");
	scp173 = bbLoadImage("GFX/menu/173back.jpg");
	tileWhite = bbLoadImage("GFX/menu/menuwhite.jpg");
	tileBlack = bbLoadImage("GFX/menu/menublack.jpg");
	bbMaskImage(tileBlack, 255, 255, 0);

	bbResizeImage(back, bbImageWidth(back) * MenuScale, bbImageHeight(back) * MenuScale);
	bbResizeImage(scpText, bbImageWidth(scpText) * MenuScale, bbImageHeight(scpText) * MenuScale);
	bbResizeImage(scp173, bbImageWidth(scp173) * MenuScale, bbImageHeight(scp173) * MenuScale);

	pauseMenuBG = bbLoadImage("GFX/menu/pausemenu.jpg");
	bbMaskImage(pauseMenuBG, 255, 255, 0);
	bbScaleImage(pauseMenuBG, MenuScale, MenuScale);

	cursorIMG = bbLoadImage("GFX/cursor.png");

	int i;
	for (i = 0; i <= 3; i++) {
		arrow[i] = bbLoadImage("GFX/menu/arrow.png");
		bbRotateImage(arrow[i], 90 * i);
		bbHandleImage(arrow[i], 0, 0);
	}

	// TODO: Change this once we move to FreeType
	//For some reason, Blitz3D doesn't load fonts that have filenames that
	//don't match their "internal name" (i.e. their display name in applications
	//like Word and such). As a workaround, I moved the files and renamed them so they
	//can load without FastText.
	font[0] = bbLoadFont("GFX/font/cour/Courier New.ttf", (int)(18 * MenuScale), 0, 0, 0);
	font[1] = bbLoadFont("GFX/font/courbd/Courier New.ttf", (int)(58 * MenuScale), 0, 0, 0);
	font[2] = bbLoadFont("GFX/font/DS-DIGI/DS-Digital.ttf", (int)(22 * MenuScale), 0, 0, 0);
	font[3] = bbLoadFont("GFX/font/DS-DIGI/DS-Digital.ttf", (int)(60 * MenuScale), 0, 0, 0);
	consoleFont = bbLoadFont("Blitz", (int)(20 * MenuScale), 0, 0, 0);

	sprintIcon = bbLoadImage("GFX/HUD/sprinticon.png");
	blinkIcon = bbLoadImage("GFX/HUD/blinkicon.png");
	crouchIcon = bbLoadImage("GFX/HUD/sneakicon.png");
	handIcon[HAND_ICON_TOUCH] = bbLoadImage("GFX/HUD/handsymbol.png");
	handIcon[HAND_ICON_GRAB] = bbLoadImage("GFX/HUD/handsymbol2.png");
	blinkBar = bbLoadImage("GFX/HUD/blinkmeter.jpg");
	staminaBar = bbLoadImage("GFX/HUD/staminameter.jpg");
	keypadHUD = bbLoadImage("GFX/HUD/keypadhud.jpg");
	bbMaskImage(keypadHUD, 255, 0, 255);
}

UIAssets::~UIAssets() {
	bbFreeImage(back);
	bbFreeImage(scpText);
	bbFreeImage(scp173);
	bbFreeImage(tileWhite);
	bbFreeImage(tileBlack);

	bbFreeImage(pauseMenuBG);

	bbFreeImage(cursorIMG);

	for (int i = 0; i < 4; i++) {
		bbFreeImage(arrow[i]);
	}

	for (int i = 0; i < 4; i++) {
		bbFreeFont(font[i]);
	}
	bbFreeFont(consoleFont);

	bbFreeImage(sprintIcon);
	bbFreeImage(blinkIcon);
	bbFreeImage(crouchIcon);
	for (int i = 0; i < 2; i++) {
		bbFreeImage(handIcon[i]);
	}
	bbFreeImage(blinkBar);
	bbFreeImage(staminaBar);
	bbFreeImage(keypadHUD);
}

// Globals.
UIAssets* uiAssets;

// Functions.
TextureAssetWrap::TextureAssetWrap(const String& filePath, int flag) {
    texture = bbLoadTexture(filePath, flag);
    grabCount = 1;

    list.push_back(this);
}

TextureAssetWrap::~TextureAssetWrap() {
    for (int i=0;i<list.size();i++) {
        if (list[i]==this) {
            list.erase(list.begin()+i);
            break;
        }
    }
    bbFreeTexture(texture);
}

TextureAssetWrap* TextureAssetWrap::grab(const String& filePath, int flag) {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->file.equals(filePath)) {
            list[i]->grabCount++;
            return list[i];
        }
    }

    return new TextureAssetWrap(filePath, flag);
}

void TextureAssetWrap::drop() {
    grabCount--;
    if (grabCount<0) {
        grabCount = 0;
    }
}

Texture* TextureAssetWrap::getTexture() {
    return texture;
}

void TextureAssetWrap::update() {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->grabCount <= 0) {
            delete list[i];
            i--;
        }
    }
}

ImageAssetWrap::ImageAssetWrap(const String& filePath) {
    image = bbLoadImage(filePath);
    grabCount = 1;

    list.push_back(this);
}

ImageAssetWrap::~ImageAssetWrap() {
    for (int i = 0; i<list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
    bbFreeImage(image);
}

ImageAssetWrap* ImageAssetWrap::grab(const String& filePath) {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->file.equals(filePath)) {
            list[i]->grabCount++;
            return list[i];
        }
    }

    return new ImageAssetWrap(filePath);
}

void ImageAssetWrap::drop() {
    grabCount--;
    if (grabCount<0) {
        grabCount = 0;
    }
}

bbImage* ImageAssetWrap::getImage() {
    return image;
}

void ImageAssetWrap::update() {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->grabCount <= 0) {
            delete list[i];
            i--;
        }
    }
}

MeshAssetWrap::MeshAssetWrap(const String& filePath, bool isAnimated) {
    if (isAnimated) {
        mesh = bbLoadAnimMesh(filePath);
    }
    else {
        mesh = bbLoadMesh(filePath);
    }
    grabCount = 1;

    list.push_back(this);
}

MeshAssetWrap::~MeshAssetWrap() {
    for (int i = 0; i<list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
    bbFreeEntity(mesh);
}

MeshAssetWrap* MeshAssetWrap::grab(const String& filePath, bool isAnimated) {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->file.equals(filePath) && list[i]->animated == isAnimated) {
            list[i]->grabCount++;
            return list[i];
        }
    }

    return new MeshAssetWrap(filePath, isAnimated);
}

void MeshAssetWrap::drop() {
    grabCount--;
    if (grabCount<0) {
        grabCount = 0;
    }
}

MeshModel* MeshAssetWrap::getMesh() {
    return mesh;
}

void MeshAssetWrap::update() {
    for (int i = 0; i<list.size(); i++) {
        if (list[i]->grabCount <= 0) {
            delete list[i];
            i--;
        }
    }
}

void AssetWrap::update() {
    TextureAssetWrap::update();
    ImageAssetWrap::update();
    MeshAssetWrap::update();
}

void LoadEntities() {
    DrawLoading(0);

    //TODO: there may be a memory leak here,
    //probably gonna have to rework the tempsound system
    for (int i = 0; i < 10; i++) {
        TempSounds[i] = 0;
    }

    //TextureLodBias

    AmbientLightRoomTex = bbCreateTexture(2,2,257);
    bbTextureBlend(AmbientLightRoomTex,5);
    bbSetBuffer(bbTextureBuffer(AmbientLightRoomTex));
    bbClsColor(0,0,0);
    bbCls();
    bbSetBuffer(bbBackBuffer());
    AmbientLightRoomVal = 0;

    SoundEmitter = bbCreatePivot();

    CreateBlurImage();
    bbCameraProjMode(ark_blur_cam,0);

    mainPlayer = new Player();

    bbAmbientLight(Brightness, Brightness, Brightness);

    ScreenTexs[0] = bbCreateTexture(512, 512, 1+256);
    ScreenTexs[1] = bbCreateTexture(512, 512, 1+256);

    //Listener = CreateListener(mainPlayer\cam)

    DrawLoading(5);
    TeslaTexture = bbLoadTexture("GFX/Map/Textures/tesla.jpg", 1+2);

    DrawLoading(25);

    Monitor = bbLoadMesh("GFX/Map/monitor.b3d");
    bbHideEntity(Monitor);
    MonitorTexture = bbLoadTexture("GFX/General/monitortexture.jpg");

    CamBaseOBJ = bbLoadMesh("GFX/Map/cambase.b3d");
    bbHideEntity(CamBaseOBJ);
    CamOBJ = bbLoadMesh("GFX/Map/CamHead.b3d");
    bbHideEntity(CamOBJ);

    LoadItemTemplates("Data/Items/templates.ini");

    LoadMaterials("Data/materials.ini");

    //TextureLodBias TextureFloat#

    DrawLoading(30);

    LoadRoomTemplates("Data/rooms.ini");

    //LoadRoomMeshes()

}

void InitNewGame() {
    DrawLoading(45);

    DebugHUD = true;

    LoadInGameSounds(sndManager);

    HideDistance = 15.0;

    mainPlayer->heartbeatIntensity = 70;
    //HeartBeatRate = 70

    AccessCode = 0;
    for (int i = 0; i < 4; i++) {
        AccessCode = AccessCode + (int)(bbRand(1,9)*(10^i));
    }

    //TODO:
    //If (SelectedMap = "") Then
    CreateMap();
    //Else
    //	LoadMap("Map Creator/Maps/"+SelectedMap)
    //EndIf
    InitWayPoints();

    DrawLoading(79);

    Curr173 = CreateNPC(NPCtype173, 0, -30.0, 0);
    Curr106 = CreateNPC(NPCtype106, 0, -30.0, 0);
    Curr106->state = 70 * 60 * bbRand(12,17);

    for (int i = 0; i < Door::getListSize(); i++) {
        Door* d = Door::getObject(i);

        bbEntityParent(d->obj, 0);
        if (d->obj2 > 0) {
            bbEntityParent(d->obj2, 0);
        }
        if (d->frameobj > 0) {
            bbEntityParent(d->frameobj, 0);
        }
        if (d->buttons[0] > 0) {
            bbEntityParent(d->buttons[0], 0);
        }
        if (d->buttons[1] > 0) {
            bbEntityParent(d->buttons[1], 0);
        }

        if (d->obj2 != 0 & d->typ == DOOR_TYPE_DEF) {
            bbMoveEntity(d->obj, 0, 0, 8.0 * RoomScale);
            bbMoveEntity(d->obj2, 0, 0, 8.0 * RoomScale);
        }
    }

    for (int i = 0; i < Item::getListSize(); i++) {
        Item* it = Item::getObject(i);

        bbEntityType(it->collider, HIT_ITEM);
        bbEntityParent(it->collider, 0);
    }

    DrawLoading(80);
    for (int i = 0; i < SecurityCam::getListSize(); i++) {
		SecurityCam* sc = SecurityCam::getObject(i);

        sc->angle = bbEntityYaw(sc->obj) + sc->angle;
        bbEntityParent(sc->obj, 0);
    }

    std::cout<<"ROOM COUNT: "<<Room::getListSize()<<"\n";
    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);
        std::cout << "NAME: " << r->roomTemplate->name << "\n";

        for (int j = 0; j < MaxRoomLights; j++) {
            if (r->lights[j]!=0) {
                bbEntityParent(r->lights[j],0);
            }
        }

        if (!r->roomTemplate->disableDecals) {
            if (bbRand(4) == 1) {
                Decal* de = CreateDecal(bbRand(DECAL_BLOOD_SPREAD, DECAL_BLOOD_SPLATTER), bbEntityX(r->obj)+bbRnd(- 2,2), 0.003, bbEntityZ(r->obj)+bbRnd(-2,2), 90, bbRand(360), 0);
                de->size = bbRnd(0.1, 0.4);
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.85, 0.95));
            }

            if (bbRand(4) == 1) {
                Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(r->obj)+bbRnd(- 2,2), 0.003, bbEntityZ(r->obj)+bbRnd(-2,2), 90, bbRand(360), 0);
                de->size = bbRnd(0.5, 0.7);
                bbEntityAlpha(de->obj, 0.7);
                de->id = 1;
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.7, 0.85));
            }
        }

        if (r->roomTemplate->name.equals("cont_914_1") && !userOptions->introEnabled) {
            std::cout<<"914_TELEPORT\n";
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 1.0f, bbEntityZ(r->obj));
            bbResetEntity(mainPlayer->collider);
            mainPlayer->currRoom = r;
        } else if (r->roomTemplate->name.equals("intro") && userOptions->introEnabled) {
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 1.0, bbEntityZ(r->obj));
            bbResetEntity(mainPlayer->collider);
            mainPlayer->currRoom = r;
        }

    }

    for (int i = 0; i < RoomTemplate::getListSize(); i++) {
		RoomTemplate* rt = RoomTemplate::getObject(i);

        for (int j = 0; j < rt->collisionObjs.size(); j++) {
            bbFreeEntity(rt->collisionObjs[j]);
        }
        rt->collisionObjs.clear();

        if (rt->opaqueMesh!=nullptr) {
            bbFreeEntity(rt->opaqueMesh);
        }
        if (rt->alphaMesh!=nullptr) {
            bbFreeEntity(rt->alphaMesh);
        }

        for (int j = 0; j < rt->props.size(); j++) {
            Prop* prop = rt->props[j];
            bbFreeEntity(prop->obj);
            delete prop;
        }
        rt->props.clear();
    }

    for (int i = 0; i < TempWayPoint::getListSize(); i++) {
		TempWayPoint* tw = TempWayPoint::getObject(i);

        delete tw;
    }

    bbTurnEntity(mainPlayer->collider, 0, bbRand(160, 200), 0);

    bbResetEntity(mainPlayer->collider);

    InitEvents();

    //TODO: fix
    //	For e = Each Event
    //		If e\name = "room2nuke"
    //			e\eventState = 1
    //			DebugLog("room2nuke")
    //		EndIf
    //		If e\name = "room106"
    //			e\eventState2 = 1
    //			DebugLog("room106")
    //		EndIf
    //		If e\name = "room2sl"
    //			e\eventState3 = 1
    //			DebugLog("room2sl")
    //		EndIf
    //	Next

    //320, 240)
    bbMoveMouse(viewport_center_x,viewport_center_y);

    bbSetFont(uiAssets->font[0]);

    bbHidePointer();

    mainPlayer->blinkTimer = -10;
    mainPlayer->blurTimer = 100;
    mainPlayer->stamina = 100;

    for (int i = 0; i < 70; i++) {
        bbFlushKeys();
        UpdatePlayer();
        UpdateRooms();
        UpdateDoors();
        UpdateNPCs();
        bbUpdateWorld();
        //Cls()
        if ((int)((float)(i)*0.27)!=(int)((float)(i-1)*0.27)) {
            DrawLoading(80+(int)((float)(i)*0.27));
        }
    }

    FreeTextureCache();
    DrawLoading(100);

    bbFlushKeys();
    bbFlushMouse();

    mainPlayer->dropSpeed = 0;
}

void InitLoadGame() {
    SecurityCam* sc;
    RoomTemplate* rt;
    Event* e;
    Prop* prop;
    Texture* planetex;
    Chunk* ch;

    DrawLoading(80);

    for (int i = 0; i < Door::getListSize(); i++) {
        Door* d = Door::getObject(i);

        bbEntityParent(d->obj, 0);
        if (d->obj2 > 0) {
            bbEntityParent(d->obj2, 0);
        }
        if (d->frameobj > 0) {
            bbEntityParent(d->frameobj, 0);
        }
        if (d->buttons[0] > 0) {
            bbEntityParent(d->buttons[0], 0);
        }
        if (d->buttons[1] > 0) {
            bbEntityParent(d->buttons[1], 0);
        }

    }

    for (int iterator10 = 0; iterator10 < SecurityCam::getListSize(); iterator10++) {
        sc = SecurityCam::getObject(iterator10);

        sc->angle = bbEntityYaw(sc->obj) + sc->angle;
        bbEntityParent(sc->obj, 0);
    }

    bbResetEntity(mainPlayer->collider);

    //InitEvents()

    DrawLoading(90);

    bbMoveMouse(viewport_center_x,viewport_center_y);

    bbSetFont(uiAssets->font[0]);

    bbHidePointer();

    mainPlayer->blinkTimer = mainPlayer->blinkFreq;
    mainPlayer->stamina = 100;

    int i;
    float x;
    float z;
    for (int iterator11 = 0; iterator11 < RoomTemplate::getListSize(); iterator11++) {
        rt = RoomTemplate::getObject(iterator11);

        for (i=0;i<rt->collisionObjs.size();i++) {
            bbFreeEntity(rt->collisionObjs[i]);
        }
        rt->collisionObjs.clear();

        bbFreeEntity(rt->opaqueMesh);
        if (rt->alphaMesh!=0) {
            bbFreeEntity(rt->alphaMesh);
        }

        for (i=0;i<rt->props.size();i++) {
            bbFreeEntity(rt->props[i]->obj);
            delete rt->props[i];
        }
        rt->props.clear();
    }

    mainPlayer->dropSpeed = 0.0;

    FreeTextureCache();

    DrawLoading(100);

    bbWireFrame(true);
}

void NullGame() {
    int i;
    int x;
    int y;
    int lvl;
    ItemTemplate* itt;
    Screen* s;
    LightTemplate* lt;
    Door* d;
    Material* m;
    WayPoint* wp;
    TempWayPoint* twp;
    Room* r;
    Item* it;

    DeloadInGameSounds(sndManager);

    ClearTextureCache();

    delete mainPlayer;
    mainPlayer = nullptr;

    DeathMSG = "";

    DoorTempID = 0;

    GameSaved = 0;

    HideDistance = 15.0;

    Contained106 = false;
    Curr173->idle = false;

    MTFtimer = 0;
    for (i = 0; i <= 9; i++) {
        MTFrooms[i] = nullptr;
        MTFroomState[i] = 0;
    }

    for (int iterator13 = 0; iterator13 < Screen::getListSize(); iterator13++) {
        s = Screen::getObject(iterator13);

        if (s->img != 0) {
            bbFreeImage(s->img);
            s->img = 0;
        }
        delete s;
    }

    ConsoleInput = "";

    Msg = "";
    MsgTimer = 0;

    for (int iterator14 = 0; iterator14 < Room::getListSize(); iterator14++) {
        r = Room::getObject(iterator14);

        r->collisionObjs.clear();
        r->props.clear();
        delete r;
    }
    for (int i=0;i<MAP_SIZE;i++) {
        delete[] MapRooms[i];
    }
    delete[] MapRooms;

    for (int iterator15 = 0; iterator15 < ItemTemplate::getListSize(); iterator15++) {
        itt = ItemTemplate::getObject(iterator15);

        delete itt;
    }

    for (int iterator16 = 0; iterator16 < Item::getListSize(); iterator16++) {
        it = Item::getObject(iterator16);

        delete it;
    }

    NPC* n;
    for (int iterator17 = 0; iterator17 < NPC::getListSize(); iterator17++) {
        n = NPC::getObject(iterator17);

        RemoveNPC(n);
    }
    Curr173 = nullptr;
    Curr106 = nullptr;
    Curr096 = nullptr;
    for (i = 0; i <= 6; i++) {
        MTFrooms[i] = nullptr;
    }

    Event* e;
    for (int iterator18 = 0; iterator18 < Event::getListSize(); iterator18++) {
        e = Event::getObject(iterator18);

        RemoveEvent(e);
    }

    NTF_1499PrevX = 0.0;
    NTF_1499PrevY = 0.0;
    NTF_1499PrevZ = 0.0;
    NTF_1499PrevRoom = nullptr;
    NTF_1499X = 0.0;
    NTF_1499Y = 0.0;
    NTF_1499Z = 0.0;

    NoTarget = false;

    //OptionsMenu% = -1
    //QuitMSG% = -1

    IsZombie = false;

    //DeInitExt

    bbClearWorld();
    ark_blur_cam = 0;
    InitFastResize();

    for (i = 0; i <= 9; i++) {
        if (TempSounds[i]!=0) {
            bbFreeSound(TempSounds[i]);
            TempSounds[i] = 0;
        }
    }

}

String GetImagePath(const String& path) {
    if (bbFileType(path + ".png") == 1) {
        return path + ".png";
    }
    return path + ".jpg";
}

}

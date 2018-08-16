//library includes
#include <StringType.h>
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <bbinput.h>
#include <bbaudio.h>
#include <bbfilesystem.h>
#include <bbmath.h>

//project includes
#include "Assets.h"
#include "../GameMain.h"
#include "../Player/Player.h"
#include "../MiscGFX/Dreamfilter.h"
#include "../Items/Items.h"
#include "../Map/MapSystem.h"
#include "../Map/RM2/Materials.h"
#include "../Map/Doors.h"
#include "../Map/Decals.h"
#include "Audio.h"
#include "../NPCs/NPCs.h"
#include "../Menus/Menu.h"
#include "../Menus/Console/Console.h"
#include "../Menus/Console/ConsoleCommands.h"
#include "../Menus/LoadingScreen.h"
#include "../Config/Options.h"
#include "../Map/Events/Events.h"
#include "../NPCs/NPCtypeMTF.h"
#include "../Menus/Console/Console.h"
#include "../MiscGFX/FastResize.h"


namespace CBN {

// Structs.
std::vector<TextureAssetWrap*> TextureAssetWrap::list;
std::vector<ImageAssetWrap*> ImageAssetWrap::list;
std::vector<MeshAssetWrap*> MeshAssetWrap::list;

UIAssets::UIAssets() {
	back = bbLoadImage("GFX/menu/back.jpg");
	scpText = bbLoadImage("GFX/menu/scptext.jpg");
    // TODO: Remove this and add support for multiple SCPS.
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

	cursorIMG = bbLoadImage("GFX/Menu/cursor.png");

	for (int i = 0; i < 4; i++) {
		arrow[i] = bbLoadImage("GFX/menu/arrow.png");
		bbRotateImage(arrow[i], 90.f * i);
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
	consoleFont = bbLoadFont("Arial", (int)(18 * MenuScale), 0, 0, 0);

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
AssetWrap::AssetWrap() {
    grabCount = 1;
    decayTimer = ASSET_DECAY_TIMER;
}

TextureAssetWrap::TextureAssetWrap(const String& filePath, int flag) : AssetWrap() {
    texture = bbLoadTexture(filePath, flag);
    file = filePath;
    flags = flag;

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
            list[i]->decayTimer = ASSET_DECAY_TIMER;
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
            list[i]->decayTimer -= timing->tickDuration;
            if (list[i]->decayTimer <= 0.0f) {
                delete list[i];
                i--;
            }
        }
    }
}

ImageAssetWrap::ImageAssetWrap(const String& filePath) : AssetWrap() {
    image = bbLoadImage(filePath);
    file = filePath;

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
            list[i]->decayTimer = ASSET_DECAY_TIMER;
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
            list[i]->decayTimer -= timing->tickDuration;
            if (list[i]->decayTimer <= 0.0f) {
                delete list[i];
                i--;
            }
        }
    }
}

MeshAssetWrap::MeshAssetWrap(const String& filePath, bool isAnimated) : AssetWrap() {
    if (isAnimated) {
        mesh = bbLoadAnimMesh(filePath);
    }
    else {
        mesh = bbLoadMesh(filePath);
    }
    bbHideEntity(mesh);
    file = filePath;
    animated = isAnimated;

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
            list[i]->decayTimer = ASSET_DECAY_TIMER;
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
            list[i]->decayTimer -= timing->tickDuration;
            if (list[i]->decayTimer <= 0.0f) {
                delete list[i];
                i--;
            }
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

    bbAmbientLight((float)Brightness, (float)Brightness, (float)Brightness);

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

    ItemTemplate::LoadTemplates("Data/Items/templates.ini");

    LoadMaterials("Data/materials.ini");

    //TextureLodBias TextureFloat#

    DrawLoading(30);

    LoadRoomTemplates("Data/rooms.ini");

    //LoadRoomMeshes()

}

void InitNewGame() {
    DrawLoading(45);

    sndMgmt->loadInGameSounds();

    console = new Console();

    HideDistance = 15.f;

    mainPlayer->heartbeatIntensity = 70.f;
    //HeartBeatRate = 70

    AccessCode = 0;
    for (int i = 0; i < 4; i++) {
        AccessCode = AccessCode + bbRand(1,9)*(10^i);
    }

    //TODO:
    //If (SelectedMap = "") Then
    CreateMap();
    //Else
    //	LoadMap("Map Creator/Maps/"+SelectedMap)
    //EndIf
    InitWayPoints();

    DrawLoading(79);

    Curr173 = CreateNPC(NPCtype173, 0, -30.f, 0);
    Curr106 = CreateNPC(NPCtype106, 0, -30.f, 0);
    Curr106->state = 70.f * 60.f * bbRand(12,17);

    for (int i = 0; i < Door::getListSize(); i++) {
        Door* d = Door::getObject(i);

        bbEntityParent(d->obj, 0);
        if (d->obj2 != nullptr) {
            bbEntityParent(d->obj2, 0);
        }
        if (d->frameobj != nullptr) {
            bbEntityParent(d->frameobj, 0);
        }
        if (d->buttons[0] != nullptr) {
            bbEntityParent(d->buttons[0], 0);
        }
        if (d->buttons[1] != nullptr) {
            bbEntityParent(d->buttons[1], 0);
        }

        if (d->obj2 != nullptr && d->typ == DOOR_TYPE_DEF) {
            bbMoveEntity(d->obj, 0.f, 0.f, 8.f * RoomScale);
            bbMoveEntity(d->obj2, 0.f, 0.f, 8.f * RoomScale);
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
            if (r->lights[j]!=nullptr) {
                bbEntityParent(r->lights[j],0);
            }
        }

        if (!r->roomTemplate->disableDecals) {
            if (bbRand(4) == 1) {
                Decal* de = CreateDecal(bbRand(DECAL_BLOOD_SPREAD, DECAL_BLOOD_SPLATTER), bbEntityX(r->obj)+bbRnd(- 2,2), 0.003f, bbEntityZ(r->obj)+bbRnd(-2,2), 90.f, (float)bbRand(360), 0.f);
                de->size = bbRnd(0.1f, 0.4f);
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.85f, 0.95f));
            }

            if (bbRand(4) == 1) {
                Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(r->obj)+bbRnd(- 2,2), 0.003f, bbEntityZ(r->obj)+bbRnd(-2,2), 90.f, (float)bbRand(360), 0.f);
                de->size = bbRnd(0.5f, 0.7f);
                bbEntityAlpha(de->obj, 0.7f);
                de->id = 1;
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.7f, 0.85f));
            }
        }

        if (r->roomTemplate->name.equals("lck_cam_2c") && !userOptions->introEnabled) {
            std::cout<<"LOCKROOM_TELEPORT\n";
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj)+3.0f, 1.0f, bbEntityZ(r->obj));
            bbResetEntity(mainPlayer->collider);
            mainPlayer->currRoom = r;
        } else if (r->roomTemplate->name.equals("intro") && userOptions->introEnabled) {
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 1.f, bbEntityZ(r->obj));
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

    bbTurnEntity(mainPlayer->collider, 0.f, (float)bbRand(160, 200), 0.f);

    bbResetEntity(mainPlayer->collider);

    //InitEvents();

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
        if ((int)((float)(i)*0.27f)!=(int)((float)(i-1)*0.27f)) {
            DrawLoading(80+(int)((float)(i)*0.27f));
        }
    }

    FreeTextureCache();
    DrawLoading(100);

    bbFlushKeys();
    bbFlushMouse();

    mainPlayer->dropSpeed = 0;
}

void InitLoadGame() {
    DrawLoading(80);

    for (int i = 0; i < Door::getListSize(); i++) {
        Door* d = Door::getObject(i);

        bbEntityParent(d->obj, 0);
        if (d->obj2 != nullptr) {
            bbEntityParent(d->obj2, 0);
        }
        if (d->frameobj != nullptr) {
            bbEntityParent(d->frameobj, 0);
        }
        if (d->buttons[0] != nullptr) {
            bbEntityParent(d->buttons[0], 0);
        }
        if (d->buttons[1] != nullptr) {
            bbEntityParent(d->buttons[1], 0);
        }

    }

    for (int i = 0; i < SecurityCam::getListSize(); i++) {
        SecurityCam* sc = SecurityCam::getObject(i);

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
    mainPlayer->stamina = 100.f;

    for (int i = 0; i < RoomTemplate::getListSize(); i++) {
        RoomTemplate* rt = RoomTemplate::getObject(i);

        for (int j = 0; j < rt->collisionObjs.size(); j++) {
            bbFreeEntity(rt->collisionObjs[j]);
        }
        rt->collisionObjs.clear();

        bbFreeEntity(rt->opaqueMesh);
        if (rt->alphaMesh!=0) {
            bbFreeEntity(rt->alphaMesh);
        }

        for (int j = 0; j < rt->props.size(); j++) {
            bbFreeEntity(rt->props[j]->obj);
            delete rt->props[j];
        }
        rt->props.clear();
    }

    mainPlayer->dropSpeed = 0.f;

    FreeTextureCache();

    DrawLoading(100);

    bbWireFrame(console->wireframeState);
}

void NullGame() {
    sndMgmt->deloadInGameSounds();

    ItemTemplate::DeloadTemplates();

    delete console;

    ClearTextureCache();

    delete mainPlayer;
    mainPlayer = nullptr;

    DeathMSG = "";

    DoorTempID = 0;

    GameSaved = 0;

    HideDistance = 15.f;

    Contained106 = false;
    Curr173->idle = false;

    MTFtimer = 0;
    for (int i = 0; i < 10; i++) {
        MTFrooms[i] = nullptr;
        MTFroomState[i] = 0;
    }

    for (int i = 0; i < Screen::getListSize(); i++) {
        Screen* s = Screen::getObject(i);

        if (s->img != nullptr) {
            bbFreeImage(s->img);
            s->img = nullptr;
        }
        delete s;
    }

    Msg = "";
    MsgTimer = 0;

    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);

        r->collisionObjs.clear();
        r->props.clear();
        delete r;
    }
    for (int i=0;i<MAP_SIZE;i++) {
        delete[] MapRooms[i];
    }
    delete[] MapRooms;

    for (int i = 0; i < Item::getListSize(); i++) {
        Item* it = Item::getObject(i);

        delete it;
    }

    for (int i = 0; i < NPC::getListSize(); i++) {
        NPC* n = NPC::getObject(i);

        RemoveNPC(n);
    }
    Curr173 = nullptr;
    Curr106 = nullptr;
    Curr096 = nullptr;
    for (int i = 0; i < 7; i++) {
        MTFrooms[i] = nullptr;
    }

    /*TODO: aaaa
    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        RemoveEvent(e);
    }*/

    NTF_1499PrevX = 0.f;
    NTF_1499PrevY = 0.f;
    NTF_1499PrevZ = 0.f;
    NTF_1499PrevRoom = nullptr;
    NTF_1499X = 0.f;
    NTF_1499Y = 0.f;
    NTF_1499Z = 0.f;

    console->noTarget = false;

    //OptionsMenu% = -1
    //QuitMSG% = -1

    IsZombie = false;

    //DeInitExt

    bbPrintWorldAssetList(); //TODO: MASSIVE MEMORY LEAK BECAUSE THIS DOESN'T DO CLEANUP ANYMORE

    ark_blur_cam = 0;
    InitFastResize();

    for (int i = 0; i < 10; i++) {
        if (TempSounds[i]!=nullptr) {
            bbFreeSound(TempSounds[i]);
            TempSounds[i] = nullptr;
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

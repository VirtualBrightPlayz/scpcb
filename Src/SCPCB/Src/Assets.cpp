//library includes
#include <StringType.h>
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <iostream>

//project includes
#include "Assets.h"
#include "GameMain.h"
#include "Player.h"
#include "Dreamfilter.h"
#include "Menus/Menu.h"
#include "Menus/LoadingScreen.h"

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

	// TODO: Change this now?
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

	int i;
	for (i = 0; i < 4; i++) {
		bbFreeImage(arrow[i]);
	}

	for (i = 0; i < 4; i++) {
		bbFreeFont(font[i]);
	}
	bbFreeFont(consoleFont);

	bbFreeImage(sprintIcon);
	bbFreeImage(blinkIcon);
	bbFreeImage(crouchIcon);
	for (i = 0; i < 2; i++) {
		bbFreeImage(handIcon[i]);
	}
	bbFreeImage(blinkBar);
	bbFreeImage(staminaBar);
	bbFreeImage(keypadHUD);
}

// Constants.
const int ASSET_DECAY_TIMER = 10 * 70;
const int BLEND_ADD = 3;
const int GORE_PIC_COUNT = 6;
const int HAND_ICON_TOUCH = 0;
const int HAND_ICON_GRAB = 1;

// Globals.
UIAssets* uiAssets;

// Functions.
TextureAssetWrap::TextureAssetWrap(String filePath, int flag) {
    texture = bbLoadTexture(filePath,flag);

    list.push_back(this);
}

TextureAssetWrap* TextureAssetWrap::grab(String filePath, int flag) {
    for (int i=0;i<list.size();i++) {
        if (list[i]->file.equals(filePath)) {
            return list[i];
        }
    }

    return new TextureAssetWrap(filePath,flag);
}

int GrabImage(String filePath) {
    return GrabAsset(filePath, ASSET_IMAGE);
}

int GrabMesh(String filePath) {
    return GrabAsset(filePath, ASSET_MESH);
}

void DropAsset(int obj) {
    AssetWrap* as;
    for (int iterator1 = 0; iterator1 < AssetWrap::getListSize(); iterator1++) {
        as = AssetWrap::getObject(iterator1);

        if (obj == as->intVal) {
            as->grabCount = as->grabCount - 1;
            //DebugLog("DROPPED ASSET: " + as\file + ", " + String(as\grabCount))
            return;
        }
    }

    //TODO: Maybe not make this crash the game later?
    throw Exception("Attempted to drop non-existant asset.");
}

void UpdateAssets() {
    AssetWrap* as;
    for (int iterator2 = 0; iterator2 < AssetWrap::getListSize(); iterator2++) {
        as = AssetWrap::getObject(iterator2);

        if (as->grabCount < 1) {
            as->decayTimer = as->decayTimer - timing->tickDuration;
            //DebugLog("ASSET DECAYING: " + as\file + ", " + String(as\decayTimer))
            if (as->decayTimer < 0) {
                FreeAsset(as);
            }
        }
    }
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

    mainPlayer = CreatePlayer();

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
    int i;
    Decal* de;
    Door* d;
    Item* it;
    Room* r;
    SecurityCam* sc;
    Event* e;
    Prop* prop;

    DrawLoading(45);

    LoadInGameSounds(sndManager);

    HideDistance = 15.0;

    mainPlayer->heartbeatIntensity = 70;
    //HeartBeatRate = 70

    AccessCode = 0;
    for (i = 0; i <= 3; i++) {
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

    for (int iterator3 = 0; iterator3 < Door::getListSize(); iterator3++) {
        d = Door::getObject(iterator3);

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

    for (int iterator4 = 0; iterator4 < Item::getListSize(); iterator4++) {
        it = Item::getObject(iterator4);

        bbEntityType(it->collider, HIT_ITEM);
        bbEntityParent(it->collider, 0);
    }

    DrawLoading(80);
    for (int iterator5 = 0; iterator5 < SecurityCam::getListSize(); iterator5++) {
        sc = SecurityCam::getObject(iterator5);

        sc->angle = bbEntityYaw(sc->obj) + sc->angle;
        bbEntityParent(sc->obj, 0);
    }

    for (int iterator6 = 0; iterator6 < Room::getListSize(); iterator6++) {
        r = Room::getObject(iterator6);

        for (i = 0; i <= MaxRoomLights; i++) {
            if (r->lights[i]!=0) {
                bbEntityParent(r->lights[i],0);
            }
        }

        if (!r->roomTemplate->disableDecals) {
            if (bbRand(4) == 1) {
                de = CreateDecal(bbRand(DECAL_BLOOD_SPREAD, DECAL_BLOOD_SPLATTER), bbEntityX(r->obj)+bbRnd(- 2,2), 0.003, bbEntityZ(r->obj)+bbRnd(-2,2), 90, bbRand(360), 0);
                de->size = bbRnd(0.1, 0.4);
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.85, 0.95));
            }

            if (bbRand(4) == 1) {
                de = CreateDecal(DECAL_CORROSION, bbEntityX(r->obj)+bbRnd(- 2,2), 0.003, bbEntityZ(r->obj)+bbRnd(-2,2), 90, bbRand(360), 0);
                de->size = bbRnd(0.5, 0.7);
                bbEntityAlpha(de->obj, 0.7);
                de->id = 1;
                bbScaleSprite(de->obj, de->size, de->size);
                bbEntityAlpha(de->obj, bbRnd(0.7, 0.85));
            }
        }

        if (r->roomTemplate->name == "cont_173_1" & (userOptions->introEnabled == false)) {
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj)+3584*RoomScale, 714*RoomScale, bbEntityZ(r->obj)+1024*RoomScale);
            bbResetEntity(mainPlayer->collider);
            mainPlayer->currRoom = r;
        } else if ((r->roomTemplate->name == "intro" & userOptions->introEnabled)) {
            bbPositionEntity(mainPlayer->collider, bbEntityX(r->obj), 1.0, bbEntityZ(r->obj));
            bbResetEntity(mainPlayer->collider);
            mainPlayer->currRoom = r;
        }

    }

    RoomTemplate* rt;
    for (int iterator7 = 0; iterator7 < RoomTemplate::getListSize(); iterator7++) {
        rt = RoomTemplate::getObject(iterator7);

        if (rt->collisionObjs!=nullptr) {
            for (i = 0; i <= rt->collisionObjs->size-1; i++) {
                bbFreeEntity(GetIntArrayListElem(rt->collisionObjs,i));
            }
            DeleteIntArrayList(rt->collisionObjs);
            rt->collisionObjs = nullptr;
        }

        bbFreeEntity(rt->opaqueMesh);
        if (rt->alphaMesh!=0) {
            bbFreeEntity(rt->alphaMesh);
        }

        if (rt->props!=nullptr) {
            for (i = 0; i <= rt->props->size-1; i++) {
                prop = Object.Prop(GetIntArrayListElem(rt->props,i));
                bbFreeEntity(prop->obj);
                delete prop;
            }
            DeleteIntArrayList(rt->props);
            rt->props = nullptr;
        }
    }

    TempWayPoint* tw;
    for (int iterator8 = 0; iterator8 < TempWayPoint::getListSize(); iterator8++) {
        tw = TempWayPoint::getObject(iterator8);

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

    for (i = 0; i <= 70; i++) {
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
    Door* d;
    SecurityCam* sc;
    RoomTemplate* rt;
    Event* e;
    Prop* prop;
    int planetex;
    Chunk* ch;

    DrawLoading(80);

    for (int iterator9 = 0; iterator9 < Door::getListSize(); iterator9++) {
        d = Door::getObject(iterator9);

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

        if (rt->collisionObjs!=nullptr) {
            for (i = 0; i <= rt->collisionObjs->size-1; i++) {
                bbFreeEntity(GetIntArrayListElem(rt->collisionObjs,i));
            }
            DeleteIntArrayList(rt->collisionObjs);
            rt->collisionObjs = nullptr;
        }

        bbFreeEntity(rt->opaqueMesh);
        if (rt->alphaMesh!=0) {
            bbFreeEntity(rt->alphaMesh);
        }

        if (rt->props!=nullptr) {
            for (i = 0; i <= rt->props->size-1; i++) {
                prop = Object.Prop(GetIntArrayListElem(rt->props,i));
                bbFreeEntity(prop->obj);
                delete prop;
            }
            DeleteIntArrayList(rt->props);
            rt->props = nullptr;
        }
    }

    mainPlayer->dropSpeed = 0.0;

    //TODO: Not load this at the start of the game jesus.
    for (int iterator12 = 0; iterator12 < Event::getListSize(); iterator12++) {
        e = Event::getObject(iterator12);

        //Loading the necessary stuff for dimension1499, but this will only be done if the player is in this dimension already
        if (e->name == "dimension1499") {
            if (e->eventState == 2) {
                //[Block]
                DrawLoading(91);
                e->room->objects[0] = bbCreatePlane();
                planetex = bbLoadTexture("GFX/Map/Rooms/dimension1499/grit3.jpg");
                bbEntityTexture(e->room->objects[0],planetex);
                bbFreeTexture(planetex);
                bbPositionEntity(e->room->objects[0],0,bbEntityY(e->room->obj),0);
                bbEntityType(e->room->objects[0],HIT_MAP);
                //EntityParent(e\room\objects[0],e\room\obj)
                DrawLoading(92);
                NTF_1499Sky = sky_CreateSky("GFX/Map/sky/1499sky");
                DrawLoading(93);
                for (i = 1; i <= 15; i++) {
                    e->room->objects[i] = bbLoadMesh("GFX/Map/Rooms/dimension1499/1499object" + String(i) + ".b3d");
                    bbHideEntity(e->room->objects[i]);
                }
                DrawLoading(96);
                CreateChunkParts(e->room);
                DrawLoading(97);
                x = bbEntityX(e->room->obj);
                z = bbEntityZ(e->room->obj);
                for (i = -2; i <= 2; i += 2) {
                    ch = CreateChunk(-1, x * (i * 2.5), bbEntityY(e->room->obj),z);
                }
                //If (Music(18)=0) Then Music(18) = LoadSound("SFX/Music/1499.ogg") ;TODO: fix
                DrawLoading(98);
                UpdateChunks(e->room,15,false);
                //MoveEntity(mainPlayer\collider,0,10,0)
                //ResetEntity(mainPlayer\collider)

                std::cout << "Loaded dimension1499 successful";

                break;
                //[End Block]
            }
        }
    }

    FreeTextureCache();

    DrawLoading(100);
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

    DeletePlayer(mainPlayer);
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

        DeleteIntArray(r->collisionObjs);
        if (r->props!=nullptr) {
            DeleteIntArray(r->props);
        }
        delete r;
    }
    DeleteIntArray(MapRooms);

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

String GetImagePath(String path) {
    if (bbFileType(path + ".png") == 1) {
        return path + ".png";
    }
    return path + ".jpg";
}

}

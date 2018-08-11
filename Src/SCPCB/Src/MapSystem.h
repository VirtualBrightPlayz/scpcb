#ifndef MAPSYSTEM_H_INCLUDED
#define MAPSYSTEM_H_INCLUDED

#include <StringType.h>
#include <vector>

class Object;
class MeshModel;
class Pivot;
class Texture;
class Camera;
class Sprite;
class Light;
class bbImage;
class gxChannel;

namespace CBN {

struct Prop;

// Constants.
const int MaxRoomLights = 32;
const int MaxRoomEmitters = 8;
const int MaxRoomObjects = 30;

//TODO: use enum classes
const int ROOM0 = 0;
const int ROOM1 = 1;
const int ROOM2 = 2;
const int ROOM2C = 3;
const int ROOM3 = 4;
const int ROOM4 = 5;

const int ZONE_LCZ = 1;
const int ZONE_HCZ = 2;
const int ZONE_EZ = 4;

const int MAP_SIZE = 19;

const int LIGHTTYPE_POINT = 2;
const int LIGHTTYPE_SPOT = 3;

// Structs.
struct RoomTemplate {
private:
    static std::vector<RoomTemplate*> list;

public:
    RoomTemplate();
    ~RoomTemplate();
    static int getListSize();
    static RoomTemplate* getObject(int index);

    String name;
    int shape;
    //TODO: might not be needed?
    int large;

    String objPath;
    int loaded;

    MeshModel* opaqueMesh;
    MeshModel* alphaMesh;
    std::vector<MeshModel*> collisionObjs;
	std::vector<Prop*> props;

    int zones;

    static const int MaxRoomLights = 32;
    static const int MaxRoomEmitters = 8;
    static const int MaxRoomObjects = 30;

    //TODO: cleanup?
    int tempSoundEmitter[MaxRoomEmitters];
    float tempSoundEmitterX[MaxRoomEmitters];
    float tempSoundEmitterY[MaxRoomEmitters];
    float tempSoundEmitterZ[MaxRoomEmitters];
    float tempSoundEmitterRange[MaxRoomEmitters];

    float commonness;
    int minAmount;
    int maxAmount;
    float xRangeStart;
    float xRangeEnd;
    float yRangeStart;
    float yrangeEnd;
    int disableDecals;

    //TODO: remove
    int tempTriggerboxAmount;
    int tempTriggerbox[128];
    String tempTriggerboxName[128];
};

struct Room {
private:
    static std::vector<Room*> list;

public:
    Room();
    ~Room();
    static int getListSize();
    static Room* getObject(int index);

    int zone;

    int found;

    float x;
    float y;
    float z;
    int angle;
    struct RoomTemplate* roomTemplate;

    //TODO: rename
    Pivot* obj;
	MeshModel* opaqueMesh;
	MeshModel* alphaMesh;
    std::vector<MeshModel*> collisionObjs;
    std::vector<MeshModel*> props;

    float dist;

    gxChannel* soundCHN;

    struct Forest* fr;

    //TODO: remove duplicates and rename
    static const int MaxRoomLights = 32;
    static const int MaxRoomEmitters = 8;
    static const int MaxRoomObjects = 30;

    //TODO: use arraylists for all this stuff?
    int soundEmitter[MaxRoomEmitters];
    Pivot* soundEmitterObj[MaxRoomEmitters];
    float soundEmitterRange[MaxRoomEmitters];
    gxChannel* soundEmitterCHN[MaxRoomEmitters];

    Light* lights[MaxRoomLights];
    float lightIntensity[MaxRoomLights];

    Sprite* lightSprites[MaxRoomLights];

    Object* objects[MaxRoomObjects];
    struct Lever* levers[11];
    struct Door* doors[7];
    struct NPC* npc[12];

    struct Room* adjacent[4];
    struct Door* adjDoor[4];

    //TODO: what the fuck
    Pivot* lightSpritesPivot[MaxRoomLights];
    int triggerboxAmount;
    int triggerbox[128];
    String triggerboxName[128];
};

struct LightTemplate {
private:
    static std::vector<LightTemplate*> list;

public:
    LightTemplate();
    ~LightTemplate();
    static int getListSize();
    static LightTemplate* getObject(int index);

    struct RoomTemplate* roomtemplate;
    int ltype;
    float x;
    float y;
    float z;
    float range;
    int r;
    int g;
    int b;

    float pitch;
    float yaw;
    float innerconeangle;
    float outerconeangle;
};

struct TempWayPoint {
private:
    static std::vector<TempWayPoint*> list;

public:
    TempWayPoint();
    ~TempWayPoint();
    static int getListSize();
    static TempWayPoint* getObject(int index);

    float x;
    float y;
    float z;
    int connectedTo[32];
    struct RoomTemplate* roomtemplate;
};

struct WayPoint {
private:
    static std::vector<WayPoint*> list;

public:
    WayPoint();
    ~WayPoint();
    static int getListSize();
    static WayPoint* getObject(int index);

    Pivot* obj;
    struct Room* room;
    int state;
    //Field tempDist#
    //Field tempSteps%
    struct WayPoint* connected[16];
    float dist[16];

    float fCost;
    float gCost;
    float hCost;

    struct WayPoint* parent;
};

struct Screen {
private:
    static std::vector<Screen*> list;

public:
    Screen();
    ~Screen();
    static int getListSize();
    static Screen* getObject(int index);

    Pivot* obj;
    String imgpath;
    bbImage* img;
    struct Room* room;
};

struct TempScreen {
private:
    static std::vector<TempScreen*> list;

public:
    TempScreen();
    ~TempScreen();
    static int getListSize();
    static TempScreen* getObject(int index);

    String imgpath;
    float x;
    float y;
    float z;
    struct RoomTemplate* roomtemplate;
};

struct SecurityCam {
private:
    static std::vector<SecurityCam*> list;

public:
    SecurityCam();
    ~SecurityCam();
    static int getListSize();
    static SecurityCam* getObject(int index);

    MeshModel* obj;
    MeshModel* monitorObj;

    //MeshModel* baseObj;
    MeshModel* cameraObj;

    Sprite* scrObj;
    float scrWidth;
    float scrHeight;
    int screen;
    Camera* cam;
    int scrTexture; //TODO: rename
    Sprite* scrOverlay;
    float angle;
    float turn;
    float currAngle;
    float state;
    int playerState;

    gxChannel* soundCHN;

    int inSight;

    float renderInterval;

    struct Room* room;

    int followPlayer;
    int coffinEffect;

    int allowSaving;

    float minAngle;
    float maxAngle;
    int dir;

    int isRoom2slCam = false;
    Texture* room2slTexs[2]; //TODO: kill
    int specialCam = false;
    int id = -1;
};

struct Prop {
private:
    static std::vector<Prop*> list;

public:
    Prop();
    ~Prop();
    static int getListSize();
    static Prop* getObject(int index);

    String file;
    MeshModel* obj;

    float x;
    float y;
    float z;
    float pitch;
    float yaw;
    float roll;
    float xScale;
    float yScale;
    float zScale;
};

// Globals.
extern float RoomScale;
extern gxSound* RoomAmbience[20];
extern MeshModel* Sky;
extern float HideDistance;
extern float SecondaryLightOn;
extern int RemoteDoorOn;
extern int Contained106;
extern Screen* SelectedScreen;
extern SecurityCam* SelectedMonitor;
extern SecurityCam* CoffinCam;
extern Texture* ScreenTexs[2];
extern Room*** MapRooms;

// Functions.
void LoadMaterials(const String& file);

String StripPath(const String& file);

RoomTemplate* CreateRoomTemplate(const String& meshpath);

void LoadRoomTemplates(const String& file);

void LoadRoomMesh(RoomTemplate* rt);

RoomTemplate* GetRoomTemplate(const String& name);

int CountRooms(RoomTemplate* rt);

Room* CreateRoom(RoomTemplate* rt, float x, float y, float z);

void FillRoom(Room* r);

void SetRoomVisibility(Room* r, int on);

void UpdateRooms();

int IsRoomAdjacent(Room* ths, Room* that);

Light* AddLight(Room* room, float x, float y, float z, int ltype, float range, int r, int g, int b);

LightTemplate* AddTempLight(RoomTemplate* rt, float x, float y, float z, int ltype, float range, int r, int g, int b);

WayPoint* CreateWaypoint(float x, float y, float z, Room* room);

void InitWayPoints(int loadingstart = 45);

void RemoveWaypoint(WayPoint* w);

int FindPath(struct NPC* n, float x, float y, float z); //TODO: move to NPCs.h?

MeshModel* CreateLine(float x1, float y1, float z1, float x2, float y2, float z2, MeshModel* mesh = nullptr);

Screen* CreateScreen(float x, float y, float z, const String& imgpath, Room* r);

void UpdateScreens();

SecurityCam* CreateSecurityCam(float x, float y, float z, Room* r, int screen = false);

void UpdateSecurityCams();

Prop* LoadProp(const String& file, float x, float y, float z, float pitch, float yaw, float roll, float xScale, float yScale, float zScale);

void CreateMap();

void DetermineRoomTypes(int** layout, int mapDim);

int DetermineRotation(int** layout, int layoutDims, int x, int y);

int CheckRoomOverlap(const String& roomname, int x, int y);

int GetZone(int y);

void AmbientLightRooms(int value = 0);

void FindAndDeleteFakeMonitor(Room* r, float x, float y, float z, int Amount);

}
#endif // MAPSYSTEM_H_INCLUDED

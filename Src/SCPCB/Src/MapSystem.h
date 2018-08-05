#ifndef MAPSYSTEM_H_INCLUDED
#define MAPSYSTEM_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Material {
private:
    static std::vector<Material*> list;

public:
    Material();
    ~Material();
    static int getListSize();
    static Material* getObject(int index);

    String name;
    int diff;
    //Field Bump

    int stepSound;
};

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

    class Object* opaqueMesh;
    class Object* alphaMesh;
    std::vector<class Object*> collisionObjs;
	std::vector<class Prop*> props;

    int zones;

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
    class Object* obj;
	class Object* opaqueMesh;
	class Object* alphaMesh;
    struct IntArray* collisionObjs;
    struct IntArray* props;

    float dist;

    int soundCHN;

    struct Forest* fr;

    //TODO: use arraylists for all this stuff?
    int soundEmitter[MaxRoomEmitters];
    int soundEmitterObj[MaxRoomEmitters];
    float soundEmitterRange[MaxRoomEmitters];
    int soundEmitterCHN[MaxRoomEmitters];

    int lights[MaxRoomLights];
    float lightIntensity[MaxRoomLights];

    int lightSprites[MaxRoomLights];

    int objects[MaxRoomObjects];
    struct Lever* levers[11];
    struct Door* doors[7];
    struct NPC* npc[12];
    struct Grid* grid;

    struct Room* adjacent[4];
    struct Door* adjDoor[4];

    //TODO: what the fuck
    int lightSpritesPivot[MaxRoomLights];
    int triggerboxAmount;
    int triggerbox[128];
    String triggerboxName[128];
};

struct Grid {
private:
    static std::vector<Grid*> list;

public:
    Grid();
    ~Grid();
    static int getListSize();
    static Grid* getObject(int index);

    int grid[gridsz*gridsz];
    int angles[gridsz*gridsz];
    int meshes[7];
    int entities[gridsz*gridsz];
    struct WayPoint* waypoints[gridsz*gridsz];
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

    int obj;
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

    int obj;
    String imgpath;
    int img;
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

    class Object* obj;
    int monitorObj;

    int baseObj;
    int cameraObj;

    int scrObj;
    float scrWidth;
    float scrHeight;
    int screen;
    int cam;
    int scrTexture;
    int scrOverlay;
    float angle;
    float turn;
    float currAngle;
    float state;
    int playerState;

    int soundCHN;

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
    int room2slTexs[2];
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
    class Object* obj;

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

// Constants.
extern const int MaxRoomLights;
extern const int MaxRoomEmitters;
extern const int MaxRoomObjects;
extern const int ROOM0;
extern const int ROOM1;
extern const int ROOM2;
extern const int ROOM2C;
extern const int ROOM3;
extern const int ROOM4;
extern const int ZONE_LCZ;
extern const int ZONE_HCZ;
extern const int ZONE_EZ;
extern const int MAP_SIZE;
extern const int gridsz;
extern const int LIGHTTYPE_POINT;
extern const int LIGHTTYPE_SPOT;

// Globals.
extern float RoomScale;
extern int RoomAmbience[20];
extern int Sky;
extern float HideDistance;
extern float SecondaryLightOn;
extern int RemoteDoorOn;
extern int Contained106;
extern Screen* SelectedScreen;
extern SecurityCam* SelectedMonitor;
extern SecurityCam* CoffinCam;
extern class Texture* ScreenTexs[2];
extern IntArray* MapRooms;

// Functions.
void LoadMaterials(String file);

String StripPath(String file);

RoomTemplate* CreateRoomTemplate(String meshpath);

void LoadRoomTemplates(String file);

void LoadRoomMesh(RoomTemplate* rt);

void UpdateGrid(Grid* grid);

RoomTemplate* GetRoomTemplate(String name);

int CountRooms(RoomTemplate* rt);

Room* CreateRoom(RoomTemplate* rt, float x, float y, float z);

void FillRoom(Room* r);

void SetRoomVisibility(Room* r, int on);

void UpdateRooms();

int IsRoomAdjacent(Room* ths, Room* that);

int AddLight(Room* room, float x, float y, float z, int ltype, float range, int r, int g, int b);

LightTemplate* AddTempLight(RoomTemplate* rt, float x, float y, float z, int ltype, float range, int r, int g, int b);

WayPoint* CreateWaypoint(float x, float y, float z, Room* room);

void InitWayPoints(int loadingstart = 45);

void RemoveWaypoint(WayPoint* w);

int FindPath(NPC* n, float x, float y, float z);

int CreateLine(float x1, float y1, float z1, float x2, float y2, float z2, int mesh = 0);

Screen* CreateScreen(float x, float y, float z, String imgpath, Room* r);

void UpdateScreens();

SecurityCam* CreateSecurityCam(float x, float y, float z, Room* r, int screen = false);

void UpdateSecurityCams();

Prop* LoadProp(String file, float x, float y, float z, float pitch, float yaw, float roll, float xScale, float yScale, float zScale);

void CreateMap();

void DetermineRoomTypes(IntArray* layout, int mapDim);

int DetermineRotation(IntArray* layout, int x, int y);

int CheckRoomOverlap(String roomname, int x, int y);

int GetZone(int y);

void AmbientLightRooms(int value = 0);

void FindAndDeleteFakeMonitor(Room* r, float x, float y, float z, int Amount);

}
#endif // MAPSYSTEM_H_INCLUDED

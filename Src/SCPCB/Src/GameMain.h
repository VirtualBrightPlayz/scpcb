#ifndef GAMEMAIN_H_INCLUDED
#define GAMEMAIN_H_INCLUDED

#include <StringType.h>
#include <vector>

class Object;
class gxSound;
class Texture;
class MeshModel;

namespace CBN {

struct Room;

// Structs.
struct Timing {
private:
    static std::vector<Timing*> list;

public:
    Timing();
    ~Timing();
    static int getListSize();
    static Timing* getObject(int index);

    //tick duration (1 = 1/70th of a second) TODO: change this value to be relative to a second
    float tickDuration;
    //accumulated steps
    float accumulator;

    int prevTime;
    int currTime;

    float fps;
};

struct Version {
    int major;
    int minor;
    int build;
    int revision;

    String getVersionString();
};

// Constants.
extern const int HIT_MAP;
extern const int HIT_PLAYER;
extern const int HIT_ITEM;
extern const int HIT_APACHE;
extern const int HIT_DEAD;

// Globals.
extern Timing* timing;
extern float CurrFrameLimit;
extern int GameSaved;
extern int CanSave;
extern bool MouseHit1;
extern bool MouseDown1;
extern bool MouseHit2;
extern bool DoubleClick;
extern int LastMouseHit1;
extern bool MouseUp1;
extern float CoffinDistance;
extern float ExplosionTimer;
extern int LightsOn;
extern int SoundTransmission;
extern int Brightness;
extern Object* SoundEmitter;
extern gxSound* TempSounds[10];
extern int TempSoundIndex;
extern gxSound* RadioSquelch;
extern gxSound* RadioStatic;
extern gxSound* RadioBuzz;
extern int PlayerDetected;
extern float PrevInjuries;
extern float PrevBloodloss;
extern Texture* AmbientLightRoomTex;
extern int AmbientLightRoomVal;
extern float NTF_1499PrevX;
extern float NTF_1499PrevY;
extern float NTF_1499PrevZ;
extern Room* NTF_1499PrevRoom;
extern float NTF_1499X;
extern float NTF_1499Y;
extern float NTF_1499Z;
extern MeshModel* NTF_1499Sky;
extern int InFacility;
extern int IsZombie;
extern int room2gw_brokendoor;
extern float room2gw_x;
extern float room2gw_z;
extern String Input294;
extern Texture* TeslaTexture;
extern MeshModel* Monitor;
extern Texture* MonitorTexture;
extern MeshModel* CamBaseOBJ;
extern MeshModel* CamOBJ;

// Functions.
void SetTickrate(int tickrate);

void AddToTimingAccumulator(int milliseconds);

void ResetTimingAccumulator();

int EntryPoint();

void InitializeMainGame();

void UpdateGame();

void updateGameState();

void UpdateGUI();

void DrawGUI();

String f2s(float n, int count);

float Animate2(MeshModel* entity, float curr, int start, int quit, float speed, int loop = true);

void UpdateInfect();

void Graphics3DExt(int width, int height, int depth = 32, int mode = 2);

void RenderWorld2();

int CheckForPlayerInFacility();

String CheckTriggers();

}
#endif // GAMEMAIN_H_INCLUDED

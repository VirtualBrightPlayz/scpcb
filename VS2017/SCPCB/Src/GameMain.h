#ifndef GAMEMAIN_H_INCLUDED
#define GAMEMAIN_H_INCLUDED
#include <vector>

namespace CBN {

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

// Constants.
extern const String VERSION;
extern const int HIT_MAP;
extern const int HIT_PLAYER;
extern const int HIT_ITEM;
extern const int HIT_APACHE;
extern const int HIT_DEAD;

// Globals.
extern int WireframeState;
extern int HalloweenTex;
extern Timing* timing;
extern float CurrFrameLimit;
extern int GameSaved;
extern int CanSave;
extern int MouseHit1;
extern int MouseDown1;
extern int MouseHit2;
extern int DoubleClick;
extern int LastMouseHit1;
extern int MouseUp1;
extern float CoffinDistance;
extern float ExplosionTimer;
extern int LightsOn;
extern int SoundTransmission;
extern int Brightness;
extern int SoundEmitter;
extern int TempSounds[10];
extern int TempSoundIndex;
extern int RadioSquelch;
extern int RadioStatic;
extern int RadioBuzz;
extern int PlayerDetected;
extern float PrevInjuries;
extern float PrevBloodloss;
extern int NoTarget;
extern int AmbientLightRoomTex;
extern int AmbientLightRoomVal;
extern float NTF_1499PrevX;
extern float NTF_1499PrevY;
extern float NTF_1499PrevZ;
extern Room* NTF_1499PrevRoom;
extern float NTF_1499X;
extern float NTF_1499Y;
extern float NTF_1499Z;
extern int NTF_1499Sky;
extern int InFacility;
extern int IsZombie;
extern int room2gw_brokendoor;
extern float room2gw_x;
extern float room2gw_z;
extern String Input294;
extern int TeslaTexture;
extern int Monitor;
extern int MonitorTexture;
extern int CamBaseOBJ;
extern int CamOBJ;

// Functions.
int VerifyResolution();

void SetTickrate(int tickrate);

void AddToTimingAccumulator(int milliseconds);

void ResetTimingAccumulator();

int EntryPoint();

void InitializeMainGame();

void UpdateGame();

void UpdateGUI();

void DrawGUI();

void DrawPauseMenu();

void UpdatePauseMenu();

String f2s(float n, int count);

float Animate2(int entity, float curr, int start, int quit, float speed, int loop = true);

void UpdateInfect();

int Graphics3DExt(int width, int height, int depth = 32, int mode = 2);

void RenderWorld2();

int CheckForPlayerInFacility();

String CheckTriggers();

}
#endif // GAMEMAIN_H_INCLUDED

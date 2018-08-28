#ifndef AUDIO_H_INCLUDED
#define AUDIO_H_INCLUDED

#include <StringType.h>
#include <vector>
#include <map>

class gxSound;
class gxChannel;
class Camera;
class Pivot;
class Object;

namespace CBN {

enum class ItemPickSound;

// Structs.
struct SoundWrapper {
private:
    SoundWrapper();

public:
    static std::vector<SoundWrapper*> list;

    ~SoundWrapper();

    gxSound* internal;
    String file;

    static SoundWrapper* Initialize(const String& fileName);

    static SoundWrapper* Load(const String& fileName);
};

class SoundManager {
public:
    SoundManager();

    void loadInGameSounds();
    void deloadInGameSounds();

    // Buttons
    SoundWrapper* button;
    SoundWrapper* buttonErr;

    // Footsteps
    SoundWrapper* footstep[8];
    SoundWrapper* footstepRun[8];
    SoundWrapper* footstepMetal[8];
    SoundWrapper* footstepMetalRun[8];
    SoundWrapper* footstepPD[3];
    SoundWrapper* footstep8601[3];

    // Doors
    SoundWrapper* openDoor[3];
    SoundWrapper* openBigDoor[2];
    SoundWrapper* openHCZDoor[3];

    SoundWrapper* closeDoor[3];
    SoundWrapper* closeBigDoor[2];
    SoundWrapper* closeHCZDoor[3];

    // Keycards
    SoundWrapper* keycardUse;
    SoundWrapper* keycardErr;
    SoundWrapper* scannerUse;
    SoundWrapper* scannerErr;

    // Pickups
    std::map<ItemPickSound, SoundWrapper*> itemPick;

    // Elevator
    SoundWrapper* elevatorBeep;
    SoundWrapper* elevatorMove;

    // Tesla
    SoundWrapper* teslaIdle;
    SoundWrapper* teslaActive;
    SoundWrapper* teslaPowerUp;
    SoundWrapper* teslaShock;

    // Bullets
    SoundWrapper* gunshot[2];
    SoundWrapper* bulletHit;
    SoundWrapper* bulletMiss;

    SoundWrapper* alarm;
    SoundWrapper* caution;
    SoundWrapper* hiss;
    SoundWrapper* lightSwitch;
    SoundWrapper* lever;
    SoundWrapper* burst;
    SoundWrapper* camera;
    SoundWrapper* heartbeat;
};

struct SoundChannel {
private:
    static std::vector<SoundChannel*> list;

public:
    SoundChannel();
    ~SoundChannel();
    static int getListSize();
    static SoundChannel* getObject(int index);

    gxChannel* internal;

    Camera* camera;
    Pivot* point;

    float range;
    float volume;
};

// TODO: Add the ability to have no music playing.
// Make it possible to play a track then swap to default when said track is done (breath.ogg)
class MusicManager {
public:
    MusicManager();
    ~MusicManager();

    gxSound* currMusic;
    gxChannel* channel;

    // When set to true the music manager just plays whichever track
    // is appropriate to the current zone/menu.
    bool useDefault;

    String nowPlaying;
    String shouldPlay;

    // Used for fading out tracks.
    bool fadeOut;
    float currMusicVolume;

    void restoreDefaultMusic();
    void setNextMusicTrack(const String& trackName, int fadeOut = true);
    void freeMusic();
    void update();
};

// Constants.
extern const int STEPSOUND_DEFAULT;
extern const int STEPSOUND_METAL;
extern const String MUS_MENU;
extern const String MUS_INTRO;
extern const String MUS_LCZ;
extern const String MUS_EZ;
extern const String MUS_HCZ;
extern const String MUS_049;
extern const String MUS_079;
extern const String MUS_106;
extern const String MUS_205;
extern const String MUS_939;
extern const String MUS_8601;
extern const String MUS_8602;
extern const String MUS_14991;
extern const String MUS_14992;
extern const String MUS_PD;
extern const String MUS_TRENCH;
extern const String MUS_GATEA;
extern const String MUS_GATEB1;
extern const String MUS_GATEB2;
extern const String MUS_END;
extern const String MUS_BREATH;

// Globals.
extern SoundManager* sndMgmt;
extern MusicManager* musicMgmt;

// Functions.
void AddPositionalChannel(gxChannel* ref, Camera* cam, Object* ent, float range = 10.f, float vol = 1.f);

void UpdateChannelList();

gxChannel* PlaySound2(gxSound* snd);

gxChannel* PlaySound_SM(SoundWrapper* snd);

gxChannel* PlayRangedSound(gxSound* soundHandle, Camera* cam, Object* entity, float range = 10.f, float volume = 1.f);

gxChannel* PlayRangedSound_SM(SoundWrapper* snd, Camera* cam, Object* entity, float range = 10, float volume = 1.f);

gxChannel* LoopRangedSound(gxSound* soundHandle, gxChannel* chn, Camera* cam, Object* entity, float range = 10.f, float volume = 1.f);

gxChannel* LoopRangedSound_SM(SoundWrapper* snd, gxChannel* chn, Camera* cam, Object* entity, float range = 10.f, float volume = 1.f);

void UpdateRangedSoundOrigin(gxChannel* chn, Camera* cam, Object* entity, float range = 10.f, float volume = 1.f);

void UpdateRangedSoundOrigin_SM(SoundChannel* chn);

gxSound* LoadTempSound(const String& file);

gxSound* LoadEventSound(class Event* e, const String& file, int i = 0);

void PauseSounds();

void ResumeSounds();

void StopSounds();

int GetMaterialStepSound(Object* entity);

}
#endif // AUDIO_H_INCLUDED

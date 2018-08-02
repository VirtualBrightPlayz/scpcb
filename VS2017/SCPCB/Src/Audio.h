#ifndef AUDIO_H_INCLUDED
#define AUDIO_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Sound {
private:
    static std::vector<Sound*> list;

public:
    Sound();
    ~Sound();
    static int getListSize();
    static Sound* getObject(int index);

    int internal;
    String file;
};

struct SoundManager {
private:
    static std::vector<SoundManager*> list;

public:
    SoundManager();
    ~SoundManager();
    static int getListSize();
    static SoundManager* getObject(int index);

    // Buttons
    Sound* button;
    Sound* buttonErr;

    // Footsteps
    Sound* footstep[8];
    Sound* footstepRun[8];
    Sound* footstepMetal[8];
    Sound* footstepMetalRun[8];
    Sound* footstepPD[3];
    Sound* footstep8601[3];

    // Doors
    Sound* openDoor[3];
    Sound* openBigDoor[2];
    Sound* openHCZDoor[3];

    Sound* closeDoor[3];
    Sound* closeBigDoor[2];
    Sound* closeHCZDoor[3];

    // Keycards
    Sound* keycardUse;
    Sound* keycardErr;
    Sound* scannerUse;
    Sound* scannerErr;

    // Pickups
    Sound* itemPick[4];

    // Elevator
    Sound* elevatorBeep;
    Sound* elevatorMove;

    // Tesla
    Sound* teslaIdle;
    Sound* teslaActive;
    Sound* teslaPowerUp;
    Sound* teslaShock;

    // Bullets
    Sound* gunshot[2];
    Sound* bulletHit;
    Sound* bulletMiss;

    Sound* alarm;
    Sound* caution;
    Sound* hiss;
    Sound* lightSwitch;
    Sound* lever;
    Sound* burst;
    Sound* camera;
    Sound* heartbeat;
};

struct SoundChannel {
private:
    static std::vector<SoundChannel*> list;

public:
    SoundChannel();
    ~SoundChannel();
    static int getListSize();
    static SoundChannel* getObject(int index);

    int internal;

    int camera;
    int point;

    float range;
    float volume;
};

struct MusicManager {
private:
    static std::vector<MusicManager*> list;

public:
    MusicManager();
    ~MusicManager();
    static int getListSize();
    static MusicManager* getObject(int index);

    int currMusic;
    int channel;

    // When set to true the music manager just plays whichever track
    // is appropriate to the current zone/menu.
    int useDefault;

    String nowPlaying;
    String shouldPlay;

    // Used for fading out tracks.
    int fadeOut;
    float currMusicVolume;
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
extern SoundManager* sndManager;
extern MusicManager* musicManager;

// Functions.
SoundManager* CreateSoundManager();

void LoadInGameSounds(SoundManager* sndMan);

void DeloadInGameSounds(SoundManager* sndMan);

void AddPositionalChannel(int ref, int cam, int ent, float range = 10, float vol = 1.0);

void UpdateChannelList();

Sound* InitializeSound_SM(String fileName);

Sound* LoadSound_SM(String fileName);

int PlaySound2(int snd);

int PlaySound_SM(Sound* snd);

void FreeSound_SM(Sound* snd);

int IsChannelPlaying(int chn);

int PlayRangedSound(int soundHandle, int cam, int entity, float range = 10, float volume = 1.0);

int PlayRangedSound_SM(Sound* snd, int cam, int entity, float range = 10, float volume = 1.0);

int LoopRangedSound(int soundHandle, int chn, int cam, int entity, float range = 10, float volume = 1.0);

int LoopRangedSound_SM(Sound* snd, int chn, int cam, int entity, float range = 10, float volume = 1.0);

void UpdateRangedSoundOrigin(int chn, int cam, int entity, float range = 10, float volume = 1.0);

void UpdateRangedSoundOrigin_SM(SoundChannel* chn);

int LoadTempSound(String file);

int LoadEventSound(Event* e, String file, int i = 0);

void PauseSounds();

void ResumeSounds();

void StopSounds();

int GetMaterialStepSound(int entity);

MusicManager* CreateMusicManager();

void RestoreDefaultMusic();

void SetNextMusicTrack(String trackName, int fadeOut = true);

void FreeMusic();

void UpdateMusic();

}
#endif // AUDIO_H_INCLUDED

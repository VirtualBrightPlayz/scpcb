#include "Audio.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Sound*> Sound::list;
Sound::Sound() {
    list.push_back(this);
}
Sound::~Sound() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Sound::getListSize() {
    return list.size();
}
Sound* Sound::getObject(int index) {
    return list[index];
}

std::vector<SoundManager*> SoundManager::list;
SoundManager::SoundManager() {
    list.push_back(this);
}
SoundManager::~SoundManager() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int SoundManager::getListSize() {
    return list.size();
}
SoundManager* SoundManager::getObject(int index) {
    return list[index];
}

std::vector<SoundChannel*> SoundChannel::list;
SoundChannel::SoundChannel() {
    list.push_back(this);
}
SoundChannel::~SoundChannel() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int SoundChannel::getListSize() {
    return list.size();
}
SoundChannel* SoundChannel::getObject(int index) {
    return list[index];
}

std::vector<MusicManager*> MusicManager::list;
MusicManager::MusicManager() {
    list.push_back(this);
}
MusicManager::~MusicManager() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int MusicManager::getListSize() {
    return list.size();
}
MusicManager* MusicManager::getObject(int index) {
    return list[index];
}

// Constants.
const int STEPSOUND_DEFAULT = 0;
const int STEPSOUND_METAL = 1;
const String MUS_MENU = "SFX/Music/Menu.ogg";
const String MUS_INTRO = "SFX/Music/Intro.ogg";
const String MUS_LCZ = "SFX/Music/The Dread.ogg";
const String MUS_EZ = "SFX/Music/EntranceZone.ogg";
const String MUS_HCZ = "SFX/Music/HeavyContainment.ogg";
const String MUS_049 = "SFX/Music/Room049.ogg";
const String MUS_079 = "SFX/Music/079.ogg";
const String MUS_106 = "SFX/Music/106.ogg";
const String MUS_205 = "SFX/Music/205.ogg";
const String MUS_939 = "SFX/Music/Room939.ogg";
const String MUS_8601 = "SFX/Music/8601.ogg";
const String MUS_8602 = "SFX/Music/8601Cancer.ogg";
const String MUS_14991 = "SFX/Music/1499.ogg";
const String MUS_14992 = "SFX/Music/1499Danger.ogg";
const String MUS_PD = "SFX/Music/PD.ogg";
const String MUS_TRENCH = "SFX/Music/PDTrench.ogg";
const String MUS_GATEA = "SFX/Music/GateA.ogg";
const String MUS_GATEB1 = "SFX/Music/GateB1.ogg";
const String MUS_GATEB2 = "SFX/Music/GateB2.ogg";
const String MUS_END = "SFX/Music/Ending.ogg";
const String MUS_BREATH = "SFX/Music/Breath.ogg";

// Globals.
SoundManager* sndManager;
MusicManager* musicManager;

// Functions.
SoundManager* CreateSoundManager() {
    SoundManager* sndMan = new SoundManager();
    sndMan->button = LoadSound_SM("SFX/Interact/Button.ogg");
    return sndMan;
}

void LoadInGameSounds(SoundManager* sndMan) {
    int i;

    sndMan->buttonErr = InitializeSound_SM("SFX/Interact/Button2.ogg");

    for (i = 0; i <= 7; i++) {
        sndMan->footstep[i] = LoadSound_SM("SFX/Step/Step" + String(i + 1) + ".ogg");
        sndMan->footstepRun[i] = LoadSound_SM("SFX/Step/Run" + String(i + 1) + ".ogg");
        sndMan->footstepMetal[i] = LoadSound_SM("SFX/Step/StepMetal" + String(i + 1) + ".ogg");
        sndMan->footstepMetalRun[i] = LoadSound_SM("SFX/Step/RunMetal" + String(i + 1) + ".ogg");
    }

    for (i = 0; i <= 2; i++) {
        sndMan->footstepPD[i] = InitializeSound_SM("SFX/Step/StepPD" + String(i + 1) + ".ogg");
        sndMan->footstep8601[i] = InitializeSound_SM("SFX/Step/StepForest" + String(i + 1) + ".ogg");
    }

    for (i = 0; i <= 2; i++) {
        sndMan->openDoor[i] = LoadSound_SM("SFX/Door/DoorOpen" + String(i + 1) + ".ogg");
        sndMan->closeDoor[i] = LoadSound_SM("SFX/Door/DoorClose" + String(i + 1) + ".ogg");
        sndMan->openHCZDoor[i] = LoadSound_SM("SFX/Door/Door2Open" + String(i + 1) + ".ogg");
        sndMan->closeHCZDoor[i] = LoadSound_SM("SFX/Door/Door2Close" + String(i + 1) + ".ogg");
    }

    for (i = 0; i <= 1; i++) {
        sndMan->openBigDoor[i] = InitializeSound_SM("SFX/Door/BigDoorOpen" + String(i + 1) + ".ogg");
        sndMan->closeBigDoor[i] = InitializeSound_SM("SFX/Door/BigDoorClose" + String(i + 1) + ".ogg");
    }

    sndMan->keycardUse = InitializeSound_SM("SFX/Interact/KeyCardUse1.ogg");
    sndMan->keycardErr = InitializeSound_SM("SFX/Interact/KeyCardUse2.ogg");
    sndMan->scannerUse = InitializeSound_SM("SFX/Interact/ScannerUse1.ogg");
    sndMan->scannerErr = InitializeSound_SM("SFX/Interact/ScannerUse2.ogg");

    for (i = 0; i <= 3; i++) {
        sndMan->itemPick[i] = LoadSound_SM("SFX/Interact/PickItem" + String(i) + ".ogg");
    }

    sndMan->elevatorBeep = InitializeSound_SM("SFX/General/Elevator/Beep.ogg");
    sndMan->elevatorMove = InitializeSound_SM("SFX/General/Elevator/Moving.ogg");

    sndMan->teslaIdle = InitializeSound_SM("SFX/Room/Tesla/Idle.ogg");
    sndMan->teslaActive = InitializeSound_SM("SFX/Room/Tesla/WindUp.ogg");
    sndMan->teslaPowerUp = InitializeSound_SM("SFX/Room/Tesla/PowerUp.ogg");
    sndMan->teslaShock = InitializeSound_SM("SFX/Room/Tesla/Shock.ogg");

    sndMan->gunshot[0] = InitializeSound_SM("SFX/General/Gunshot.ogg");
    sndMan->gunshot[1] = InitializeSound_SM("SFX/General/Gunshot2.ogg");
    sndMan->bulletHit = InitializeSound_SM("SFX/General/BulletHit.ogg");
    sndMan->bulletMiss = InitializeSound_SM("SFX/General/BulletMiss.ogg");

    sndMan->alarm = InitializeSound_SM("SFX/General/Alarm.ogg");
    sndMan->caution = InitializeSound_SM("SFX/Room/LockroomSiren.ogg");
    sndMan->hiss = InitializeSound_SM("SFX/General/Hiss.ogg");
    sndMan->lightSwitch = InitializeSound_SM("SFX/General/LightSwitch.ogg");
    sndMan->lever = InitializeSound_SM("SFX/Interact/LeverFlip.ogg");
    sndMan->burst = InitializeSound_SM("SFX/Room/TunnelBurst.ogg");
    sndMan->camera = InitializeSound_SM("SFX/General/Camera.ogg");
    sndMan->heartbeat = InitializeSound_SM("SFX/Character/D9341/Heartbeat.ogg");
}

void DeloadInGameSounds(SoundManager* sndMan) {
    int i;
    for (i = 0; i <= 7; i++) {
        FreeSound_SM(sndMan->footstep[i]);
        FreeSound_SM(sndMan->footstepRun[i]);
        FreeSound_SM(sndMan->footstepMetal[i]);
        FreeSound_SM(sndMan->footstepMetalRun[i]);
    }

    for (i = 0; i <= 2; i++) {
        FreeSound_SM(sndMan->footstepPD[i]);
        FreeSound_SM(sndMan->footstep8601[i]);
    }

    for (i = 0; i <= 2; i++) {
        FreeSound_SM(sndMan->openDoor[i]);
        FreeSound_SM(sndMan->closeDoor[i]);
        FreeSound_SM(sndMan->openHCZDoor[i]);
        FreeSound_SM(sndMan->closeHCZDoor[i]);
    }

    for (i = 0; i <= 1; i++) {
        FreeSound_SM(sndMan->openBigDoor[i]);
        FreeSound_SM(sndMan->closeBigDoor[i]);
    }

    FreeSound_SM(sndMan->keycardErr);
    FreeSound_SM(sndMan->keycardUse);
    FreeSound_SM(sndMan->scannerUse);
    FreeSound_SM(sndMan->scannerErr);

    for (i = 0; i <= 3; i++) {
        FreeSound_SM(sndMan->itemPick[i]);
    }

    FreeSound_SM(sndMan->elevatorBeep);
    FreeSound_SM(sndMan->elevatorMove);

    FreeSound_SM(sndMan->teslaIdle);
    FreeSound_SM(sndMan->teslaActive);
    FreeSound_SM(sndMan->teslaPowerUp);
    FreeSound_SM(sndMan->teslaShock);

    for (i = 0; i <= 1; i++) {
        FreeSound_SM(sndMan->gunshot[i]);
    }
    FreeSound_SM(sndMan->bulletHit);
    FreeSound_SM(sndMan->bulletMiss);

    FreeSound_SM(sndMan->alarm);
    FreeSound_SM(sndMan->caution);
    FreeSound_SM(sndMan->hiss);
    FreeSound_SM(sndMan->lightSwitch);
    FreeSound_SM(sndMan->lever);
    FreeSound_SM(sndMan->burst);
    FreeSound_SM(sndMan->camera);
    FreeSound_SM(sndMan->heartbeat);
}

void AddPositionalChannel(int ref, int cam, int ent, float range = 10, float vol = 1.0) {
    if (ref == 0) {
        return;
    }

    SoundChannel* chn = new SoundChannel();
    chn->internal = ref;
    chn->camera = cam;
    chn->point = bbCreatePivot();
    bbPositionEntity(chn->point, bbEntityX(ent), bbEntityY(ent), bbEntityZ(ent));
    chn->range = range;
    chn->volume = vol;
}

void UpdateChannelList() {
    SoundChannel* chn;
    for (int iterator19 = 0; iterator19 < SoundChannel::getListSize(); iterator19++) {
        chn = SoundChannel::getObject(iterator19);

        if (!IsChannelPlaying(chn->internal)) {
            bbFreeEntity(chn->point);
            delete chn;
        } else if ((chn->camera != 0)) {
            UpdateRangedSoundOrigin_SM(chn);
        }
    }
}

Sound* InitializeSound_SM(String fileName) {
    Sound* snd = new Sound();
    snd->file = fileName;

    return snd;
}

Sound* LoadSound_SM(String fileName) {
    Sound* snd = InitializeSound_SM(fileName);
    snd->internal = bbLoadSound(fileName);

    return snd;
}

int PlaySound2(int snd) {
    int chn = bbPlaySound(snd);

    SoundChannel* sc = new SoundChannel();
    sc->internal = chn;

    return chn;
}

int PlaySound_SM(Sound* snd) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == 0) {
        snd->internal = bbLoadSound(snd->file);
    }

    //Play the sound.
    return PlaySound2(snd->internal);
}

void FreeSound_SM(Sound* snd) {
    if (snd->internal != 0) {
        bbFreeSound(snd->internal);
        snd->internal = 0;
    }

    delete snd;
}

int IsChannelPlaying(int chn) {
    if (chn == 0) {
        return false;
    }

    return bbChannelPlaying(chn);
}

int PlayRangedSound(int soundHandle, int cam, int entity, float range = 10, float volume = 1.0) {
    float dist;
    float panvalue;

    range = Max(range, 1.0);
    int soundChn = 0;

    if (volume > 0) {
        dist = bbEntityDistance(cam, entity) / range;
        if (1 - dist > 0 & 1 - dist < 1) {
            panvalue = bbSin(-bbDeltaYaw(cam, entity));
            soundChn = bbPlaySound(soundHandle);

            bbChannelVolume(soundChn, volume * (1 - dist) * userOptions->sndVolume);
            bbChannelPan(soundChn, panvalue);
        }
    }

    AddPositionalChannel(soundChn, cam, entity, range, volume);

    return soundChn;
}

int PlayRangedSound_SM(Sound* snd, int cam, int entity, float range = 10, float volume = 1.0) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == 0) {
        snd->internal = bbLoadSound(snd->file);
    }

    return PlayRangedSound(snd->internal, cam, entity, range, volume);
}

int LoopRangedSound(int soundHandle, int chn, int cam, int entity, float range = 10, float volume = 1.0) {
    range = Max(range,1.0);

    if (!IsChannelPlaying(chn)) {
        chn = bbPlaySound(soundHandle);
    }

    UpdateRangedSoundOrigin(chn, cam, entity, range, volume);

    return chn;
}

int LoopRangedSound_SM(Sound* snd, int chn, int cam, int entity, float range = 10, float volume = 1.0) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == 0) {
        snd->internal = bbLoadSound(snd->file);
    }

    return LoopRangedSound(snd->internal, chn, cam, entity, range, volume);
}

void UpdateRangedSoundOrigin(int chn, int cam, int entity, float range = 10, float volume = 1.0) {
    float dist;
    float panvalue;

    range = Max(range, 1.0);

    if (volume > 0) {

        dist = bbEntityDistance(cam, entity) / range;
        if (1 - dist > 0 & 1 - dist < 1) {
            panvalue = bbSin(-bbDeltaYaw(cam,entity));

            bbChannelVolume(chn, volume * (1 - dist) * userOptions->sndVolume);
            bbChannelPan(chn, panvalue);
        }
    } else {
        if (chn != 0) {
            bbChannelVolume(chn, 0);
        }
    }
}

void UpdateRangedSoundOrigin_SM(SoundChannel* chn) {
    float dist;
    float panvalue;

    if (chn->volume > 0) {
        dist = bbEntityDistance(chn->camera, chn->point) / chn->range;
        if (1 - dist > 0 & 1 - dist < 1) {
            panvalue = bbSin(-bbDeltaYaw(chn->camera, chn->point));

            bbChannelVolume(chn->internal, chn->volume * (1 - dist) * userOptions->sndVolume);
            bbChannelPan(chn->internal, panvalue);
        }
    } else {
        bbChannelVolume(chn->internal, 0);
    }
}

int LoadTempSound(String file) {
    if (TempSounds[TempSoundIndex] != 0) {
        bbFreeSound(TempSounds[TempSoundIndex]);
    }

    int TempSound = bbLoadSound(file);
    TempSounds[TempSoundIndex] = TempSound;

    TempSoundIndex = (TempSoundIndex + 1) % 10;

    return TempSound;
}

int LoadEventSound(Event* e, String file, int i = 0) {
    if (e->sounds[i] != 0) {
        bbFreeSound(e->sounds[i]);
        e->sounds[i] = 0;
    }
    e->sounds[i] = bbLoadSound(file);

    return e->sounds[i];
}

void PauseSounds() {
    SoundChannel* chn;
    SecurityCam* sc;
    Room* r;
    Event* e;
    NPC* n;
    Emitter* em;
    int i;

    for (int iterator20 = 0; iterator20 < SoundChannel::getListSize(); iterator20++) {
        chn = SoundChannel::getObject(iterator20);

        if (IsChannelPlaying(chn->internal)) {
            bbPauseChannel(chn->internal);
        }
    }

    for (int iterator21 = 0; iterator21 < SecurityCam::getListSize(); iterator21++) {
        sc = SecurityCam::getObject(iterator21);

        if (IsChannelPlaying(sc->soundCHN)) {
            bbPauseChannel(sc->soundCHN);
        }
    }

    for (int iterator22 = 0; iterator22 < Room::getListSize(); iterator22++) {
        r = Room::getObject(iterator22);

        if (IsChannelPlaying(r->soundCHN)) {
            bbPauseChannel(r->soundCHN);
        }

        for (i = 0; i <= MaxRoomEmitters-1; i++) {
            if (IsChannelPlaying(r->soundEmitterCHN[i])) {
                bbPauseChannel(r->soundEmitterCHN[i]);
            }
        }
    }

    for (int iterator23 = 0; iterator23 < Event::getListSize(); iterator23++) {
        e = Event::getObject(iterator23);

        for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(e->soundChannels[i])) {
                bbPauseChannel(e->soundChannels[i]);
            }
        }
    }

    for (int iterator24 = 0; iterator24 < NPC::getListSize(); iterator24++) {
        n = NPC::getObject(iterator24);

        for (i = 0; i <= NPC_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(n->soundChannels[i])) {
                bbPauseChannel(n->soundChannels[i]);
            }
        }
    }

    for (int iterator25 = 0; iterator25 < Emitter::getListSize(); iterator25++) {
        em = Emitter::getObject(iterator25);

        if (IsChannelPlaying(em->soundCHN)) {
            bbPauseChannel(em->soundCHN);
        }
    }

    if (IsChannelPlaying(mainPlayer->breathChn)) {
        bbPauseChannel(mainPlayer->breathChn);
    }
}

void ResumeSounds() {
    SoundChannel* chn;
    SecurityCam* sc;
    Room* r;
    Event* e;
    NPC* n;
    Emitter* em;
    int i;

    for (int iterator26 = 0; iterator26 < SoundChannel::getListSize(); iterator26++) {
        chn = SoundChannel::getObject(iterator26);

        if (IsChannelPlaying(chn->internal)) {
            bbResumeChannel(chn->internal);
        }
    }

    for (int iterator27 = 0; iterator27 < SecurityCam::getListSize(); iterator27++) {
        sc = SecurityCam::getObject(iterator27);

        if (IsChannelPlaying(sc->soundCHN)) {
            bbResumeChannel(sc->soundCHN);
        }
    }

    for (int iterator28 = 0; iterator28 < Room::getListSize(); iterator28++) {
        r = Room::getObject(iterator28);

        if (IsChannelPlaying(r->soundCHN)) {
            bbResumeChannel(r->soundCHN);
        }

        for (i = 0; i <= MaxRoomEmitters-1; i++) {
            if (IsChannelPlaying(r->soundEmitterCHN[i])) {
                bbResumeChannel(r->soundEmitterCHN[i]);
            }
        }
    }

    for (int iterator29 = 0; iterator29 < Event::getListSize(); iterator29++) {
        e = Event::getObject(iterator29);

        for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(e->soundChannels[i])) {
                bbResumeChannel(e->soundChannels[i]);
            }
        }
    }

    for (int iterator30 = 0; iterator30 < NPC::getListSize(); iterator30++) {
        n = NPC::getObject(iterator30);

        for (i = 0; i <= NPC_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(n->soundChannels[i])) {
                bbResumeChannel(n->soundChannels[i]);
            }
        }
    }

    for (int iterator31 = 0; iterator31 < Emitter::getListSize(); iterator31++) {
        em = Emitter::getObject(iterator31);

        if (IsChannelPlaying(em->soundCHN)) {
            bbResumeChannel(em->soundCHN);
        }
    }

    if (IsChannelPlaying(mainPlayer->breathChn)) {
        bbResumeChannel(mainPlayer->breathChn);
    }
}

void StopSounds() {
    SoundChannel* chn;
    SecurityCam* sc;
    Room* r;
    Event* e;
    NPC* n;
    Emitter* em;
    int i;

    for (int iterator32 = 0; iterator32 < SoundChannel::getListSize(); iterator32++) {
        chn = SoundChannel::getObject(iterator32);

        if (IsChannelPlaying(chn->internal)) {
            bbStopChannel(chn->internal);
        }
    }

    for (int iterator33 = 0; iterator33 < SecurityCam::getListSize(); iterator33++) {
        sc = SecurityCam::getObject(iterator33);

        if (IsChannelPlaying(sc->soundCHN)) {
            bbStopChannel(sc->soundCHN);
        }
    }

    for (int iterator34 = 0; iterator34 < Room::getListSize(); iterator34++) {
        r = Room::getObject(iterator34);

        if (IsChannelPlaying(r->soundCHN)) {
            bbStopChannel(r->soundCHN);
        }

        for (i = 0; i <= MaxRoomEmitters-1; i++) {
            if (IsChannelPlaying(r->soundEmitterCHN[i])) {
                bbStopChannel(r->soundEmitterCHN[i]);
            }
        }
    }

    for (int iterator35 = 0; iterator35 < Event::getListSize(); iterator35++) {
        e = Event::getObject(iterator35);

        for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(e->soundChannels[i])) {
                bbStopChannel(e->soundChannels[i]);
            }
        }
    }

    for (int iterator36 = 0; iterator36 < NPC::getListSize(); iterator36++) {
        n = NPC::getObject(iterator36);

        for (i = 0; i <= NPC_CHANNEL_COUNT-1; i++) {
            if (IsChannelPlaying(n->soundChannels[i])) {
                bbStopChannel(n->soundChannels[i]);
            }
        }
    }

    for (int iterator37 = 0; iterator37 < Emitter::getListSize(); iterator37++) {
        em = Emitter::getObject(iterator37);

        if (IsChannelPlaying(em->soundCHN)) {
            bbStopChannel(em->soundCHN);
        }
    }

    if (IsChannelPlaying(mainPlayer->breathChn)) {
        bbStopChannel(mainPlayer->breathChn);
    }
}

int GetMaterialStepSound(int entity) {
    int picker = bbLinePick(bbEntityX(entity),bbEntityY(entity),bbEntityZ(entity),0,-1,0);
    int brush;
    int texture;
    String name;
    Material* mat;

    if (picker != 0) {
        if (bbGetEntityType(picker) != HIT_MAP) {
            return 0;
        }
        brush = bbGetSurfaceBrush(bbGetSurface(picker,bbCountSurfaces(picker)));
        if (brush != 0) {
            texture = bbGetBrushTexture(brush,2);

            if (texture != 0) {
                name = StripPath(bbTextureName(texture));
                if (name != "") {
                    bbFreeTexture(texture);
                }
                for (int iterator38 = 0; iterator38 < Material::getListSize(); iterator38++) {
                    mat = Material::getObject(iterator38);

                    if (mat->name == name) {
                        if (mat->stepSound > 0) {
                            bbFreeBrush(brush);
                            return mat->stepSound;
                        }
                        break;
                    }
                }
            }
            texture = bbGetBrushTexture(brush,1);
            if (texture != 0) {
                name = StripPath(bbTextureName(texture));
                if (name != "") {
                    bbFreeTexture(texture);
                }
                bbFreeBrush(brush);
                for (int iterator39 = 0; iterator39 < Material::getListSize(); iterator39++) {
                    mat = Material::getObject(iterator39);

                    if (mat->name == name) {
                        if (mat->stepSound > 0) {
                            return mat->stepSound;
                        }
                        break;
                    }
                }
            }
        }
    }

    return 0;
}

MusicManager* CreateMusicManager() {
    MusicManager* musMan = new MusicManager();
    musMan->useDefault = true;

    return musMan;
}

void RestoreDefaultMusic() {
    musicManager->fadeOut = true;
    musicManager->useDefault = true;
}

void SetNextMusicTrack(String trackName, int fadeOut = true) {
    if (musicManager->shouldPlay == trackName) {
        return;
    }

    //If the track's already being overwritten then don't let anything else change it.
    if (!musicManager->useDefault) {
        return;
    }

    musicManager->useDefault = false;
    musicManager->shouldPlay = trackName;
    musicManager->fadeOut = fadeOut;
}

void FreeMusic() {
    if (musicManager->currMusic != 0) {
        bbFreeSound(musicManager->currMusic);
        musicManager->currMusic = 0;
    }
}

void UpdateMusic() {
    if (userOptions->musicVolume <= 0) {
        return;
    }

    if (musicManager->useDefault) {
        if (CurrGameState == GAMESTATE_MAINMENU) {
            musicManager->shouldPlay = MUS_MENU;
        } else {
            musicManager->shouldPlay = MUS_LCZ;
        }
        //TODO: Play zone track here depending on game's current state.
    }

    if (musicManager->nowPlaying != musicManager->shouldPlay) {
        if (musicManager->fadeOut & musicManager->currMusicVolume > 0) {
            musicManager->currMusicVolume = musicManager->currMusicVolume - (timing->tickDuration / 250.0);
        } else {
            if (musicManager->channel != 0) {
                bbStopChannel(musicManager->channel);
                musicManager->channel = 0;
            }

            FreeMusic();
            musicManager->nowPlaying = musicManager->shouldPlay;
            musicManager->currMusic = bbLoadSound(musicManager->nowPlaying);

            musicManager->fadeOut = false;
            musicManager->currMusicVolume = userOptions->musicVolume;
        }
    } else if ((musicManager->currMusicVolume != userOptions->musicVolume)) {
        musicManager->currMusicVolume = userOptions->musicVolume;
    }

    if (!IsChannelPlaying(musicManager->channel)) {
        musicManager->channel = bbPlaySound(musicManager->currMusic);
    }

    bbChannelVolume(musicManager->channel, musicManager->currMusicVolume);
}

}

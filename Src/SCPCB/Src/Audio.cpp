#include <bbaudio.h>
#include <bbblitz3d.h>
#include <bbmath.h>

#include "Audio.h"
#include "MathUtils/MathUtils.h"
#include "Menus/Menu.h"
#include "Options.h"
#include "NPCs/NPCs.h"
#include "Player.h"
#include "GameMain.h"
#include "Events.h"
#include "MapSystem.h"
#include "Particles.h"
#include "Materials.h"

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

// Constants.
const int STEPSOUND_DEFAULT = 0;
const int STEPSOUND_METAL = 1;
const String MUS_MENU("SFX/Music/Menu.ogg");
const String MUS_INTRO("SFX/Music/Intro.ogg");
const String MUS_LCZ("SFX/Music/The Dread.ogg");
const String MUS_EZ("SFX/Music/EntranceZone.ogg");
const String MUS_HCZ("SFX/Music/HeavyContainment.ogg");
const String MUS_049("SFX/Music/Room049.ogg");
const String MUS_079("SFX/Music/079.ogg");
const String MUS_106("SFX/Music/106.ogg");
const String MUS_205("SFX/Music/205.ogg");
const String MUS_939("SFX/Music/Room939.ogg");
const String MUS_8601("SFX/Music/8601.ogg");
const String MUS_8602("SFX/Music/8601Cancer.ogg");
const String MUS_14991("SFX/Music/1499.ogg");
const String MUS_14992("SFX/Music/1499Danger.ogg");
const String MUS_PD("SFX/Music/PD.ogg");
const String MUS_TRENCH("SFX/Music/PDTrench.ogg");
const String MUS_GATEA("SFX/Music/GateA.ogg");
const String MUS_GATEB1("SFX/Music/GateB1.ogg");
const String MUS_GATEB2("SFX/Music/GateB2.ogg");
const String MUS_END("SFX/Music/Ending.ogg");
const String MUS_BREATH("SFX/Music/Breath.ogg");

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

void AddPositionalChannel(gxChannel* ref, Camera* cam, Object* ent, float range, float vol) {
    if (ref == nullptr) {
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

        if (!bbChannelPlaying(chn->internal)) {
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

gxChannel* PlaySound2(gxSound* snd) {
    gxChannel* chn = bbPlaySound(snd);

    SoundChannel* sc = new SoundChannel();
    sc->internal = chn;

    return chn;
}

gxChannel* PlaySound_SM(Sound* snd) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == nullptr) {
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

gxChannel* PlayRangedSound(gxSound* soundHandle, Camera* cam, Object* entity, float range, float volume) {
    float dist;
    float panvalue;

    range = Max(range, 1.0);
    gxChannel* soundChn = nullptr;

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

gxChannel* PlayRangedSound_SM(Sound* snd, Camera* cam, Object* entity, float range, float volume) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == nullptr) {
        snd->internal = bbLoadSound(snd->file);
    }

    return PlayRangedSound(snd->internal, cam, entity, range, volume);
}

gxChannel* LoopRangedSound(gxSound* soundHandle, gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
    range = Max(range,1.0);

    if (!bbChannelPlaying(chn)) {
        chn = bbPlaySound(soundHandle);
    }

    UpdateRangedSoundOrigin(chn, cam, entity, range, volume);

    return chn;
}

gxChannel* LoopRangedSound_SM(Sound* snd, gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == 0) {
        snd->internal = bbLoadSound(snd->file);
    }

    return LoopRangedSound(snd->internal, chn, cam, entity, range, volume);
}

void UpdateRangedSoundOrigin(gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
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

gxSound* LoadTempSound(String file) {
    if (TempSounds[TempSoundIndex] != 0) {
        bbFreeSound(TempSounds[TempSoundIndex]);
    }

    gxSound* TempSound = bbLoadSound(file);
    TempSounds[TempSoundIndex] = TempSound;

    TempSoundIndex = (TempSoundIndex + 1) % 10;

    return TempSound;
}

gxSound* LoadEventSound(Event* e, String file, int i) {
    if (e->sounds[i] != 0) {
        bbFreeSound(e->sounds[i]);
        e->sounds[i] = 0;
    }
    e->sounds[i] = bbLoadSound(file);

    return e->sounds[i];
}

void PauseSounds() {
    for (int i = 0; i < SoundChannel::getListSize(); i++) {
        SoundChannel* chn = SoundChannel::getObject(i);

        if (bbChannelPlaying(chn->internal)) {
            bbPauseChannel(chn->internal);
        }
    }

    for (int i = 0; i < SecurityCam::getListSize(); i++) {
        SecurityCam* sc = SecurityCam::getObject(i);

        if (bbChannelPlaying(sc->soundCHN)) {
            bbPauseChannel(sc->soundCHN);
        }
    }

    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);

        if (bbChannelPlaying(r->soundCHN)) {
            bbPauseChannel(r->soundCHN);
        }

        for (int j = 0; j < MaxRoomEmitters; j++) {
            if (bbChannelPlaying(r->soundEmitterCHN[j])) {
                bbPauseChannel(r->soundEmitterCHN[j]);
            }
        }
    }

    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        for (int j = 0; j < EVENT_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(e->soundChannels[j])) {
                bbPauseChannel(e->soundChannels[j]);
            }
        }
    }

    for (int i = 0; i < NPC::getListSize(); i++) {
        NPC* n = NPC::getObject(i);

        for (int j = 0; j < NPC_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(n->soundChannels[j])) {
                bbPauseChannel(n->soundChannels[j]);
            }
        }
    }

    for (int iterator25 = 0; iterator25 < Emitter::getListSize(); iterator25++) {
        Emitter* em = Emitter::getObject(iterator25);

        if (bbChannelPlaying(em->soundCHN)) {
            bbPauseChannel(em->soundCHN);
        }
    }

    if (bbChannelPlaying(mainPlayer->breathChn)) {
        bbPauseChannel(mainPlayer->breathChn);
    }
}

void ResumeSounds() {
    for (int i = 0; i < SoundChannel::getListSize(); i++) {
        SoundChannel* chn = SoundChannel::getObject(i);

        if (bbChannelPlaying(chn->internal)) {
            bbResumeChannel(chn->internal);
        }
    }

    for (int i = 0; i < SecurityCam::getListSize(); i++) {
        SecurityCam* sc = SecurityCam::getObject(i);

        if (bbChannelPlaying(sc->soundCHN)) {
            bbResumeChannel(sc->soundCHN);
        }
    }

    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);

        if (bbChannelPlaying(r->soundCHN)) {
            bbResumeChannel(r->soundCHN);
        }

        for (int j = 0; j < MaxRoomEmitters; j++) {
            if (bbChannelPlaying(r->soundEmitterCHN[j])) {
                bbResumeChannel(r->soundEmitterCHN[j]);
            }
        }
    }

    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        for (int j = 0; j < EVENT_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(e->soundChannels[j])) {
                bbResumeChannel(e->soundChannels[j]);
            }
        }
    }

    for (int i = 0; i < NPC::getListSize(); i++) {
        NPC* n = NPC::getObject(i);

        for (int j = 0; j < NPC_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(n->soundChannels[j])) {
                bbResumeChannel(n->soundChannels[j]);
            }
        }
    }

    for (int i = 0; i < Emitter::getListSize(); i++) {
        Emitter* em = Emitter::getObject(i);

        if (bbChannelPlaying(em->soundCHN)) {
            bbResumeChannel(em->soundCHN);
        }
    }

    if (bbChannelPlaying(mainPlayer->breathChn)) {
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

        if (bbChannelPlaying(chn->internal)) {
            bbStopChannel(chn->internal);
        }
    }

    for (int iterator33 = 0; iterator33 < SecurityCam::getListSize(); iterator33++) {
        sc = SecurityCam::getObject(iterator33);

        if (bbChannelPlaying(sc->soundCHN)) {
            bbStopChannel(sc->soundCHN);
        }
    }

    for (int iterator34 = 0; iterator34 < Room::getListSize(); iterator34++) {
        r = Room::getObject(iterator34);

        if (bbChannelPlaying(r->soundCHN)) {
            bbStopChannel(r->soundCHN);
        }

        for (i = 0; i <= MaxRoomEmitters-1; i++) {
            if (bbChannelPlaying(r->soundEmitterCHN[i])) {
                bbStopChannel(r->soundEmitterCHN[i]);
            }
        }
    }

    for (int iterator35 = 0; iterator35 < Event::getListSize(); iterator35++) {
        e = Event::getObject(iterator35);

        for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
            if (bbChannelPlaying(e->soundChannels[i])) {
                bbStopChannel(e->soundChannels[i]);
            }
        }
    }

    for (int iterator36 = 0; iterator36 < NPC::getListSize(); iterator36++) {
        n = NPC::getObject(iterator36);

        for (i = 0; i <= NPC_CHANNEL_COUNT-1; i++) {
            if (bbChannelPlaying(n->soundChannels[i])) {
                bbStopChannel(n->soundChannels[i]);
            }
        }
    }

    for (int iterator37 = 0; iterator37 < Emitter::getListSize(); iterator37++) {
        em = Emitter::getObject(iterator37);

        if (bbChannelPlaying(em->soundCHN)) {
            bbStopChannel(em->soundCHN);
        }
    }

    if (bbChannelPlaying(mainPlayer->breathChn)) {
        bbStopChannel(mainPlayer->breathChn);
    }
}

int GetMaterialStepSound(Object* entity) {
    Object* picker = bbLinePick(bbEntityX(entity), bbEntityY(entity), bbEntityZ(entity), 0, -1, 0);

    if (picker != nullptr) {
        if (bbGetEntityType(picker) != HIT_MAP) {
            return 0;
        }
        Brush* brush = bbGetSurfaceBrush(bbGetSurface((MeshModel*)picker, bbCountSurfaces((MeshModel*)picker)));
        if (brush != nullptr) {
            Texture* texture = bbGetBrushTexture(brush, 2);

            if (texture != nullptr) {
                String name = StripPath(bbTextureName(texture));
                if (!name.isEmpty()) {
                    bbFreeTexture(texture);
                }
                for (int i = 0; i < Material::getListSize(); i++) {
                    Material* mat = Material::getObject(i);

                    if (mat->name.equals(name)) {
                        if (mat->stepSound > 0) {
                            bbFreeBrush(brush);
                            return mat->stepSound;
                        }
                        break;
                    }
                }
            }
            texture = bbGetBrushTexture(brush, 1);
            if (texture != nullptr) {
                String name = StripPath(bbTextureName(texture));
                if (!name.isEmpty()) {
                    bbFreeTexture(texture);
                }
                bbFreeBrush(brush);
                for (int i = 0; i < Material::getListSize(); i++) {
                    Material* mat = Material::getObject(i);

                    if (mat->name.equals(name)) {
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

MusicManager::MusicManager() {
    this->useDefault = true;
}

MusicManager::~MusicManager() {
    bbStopChannel(this->channel);
    this->freeMusic();
}

void MusicManager::restoreDefaultMusic() {
    this->fadeOut = true;
    this->useDefault = true;
}

void MusicManager::setNextMusicTrack(String trackName, int fadeOut) {
    if (this->shouldPlay.equals(trackName)) {
        return;
    }

    //If the track's already being overwritten then don't let anything else change it.
    if (!this->useDefault) {
        return;
    }

    this->useDefault = false;
    this->shouldPlay = trackName;
    this->fadeOut = fadeOut;
}

void MusicManager::freeMusic() {
    if (this->currMusic != nullptr) {
        bbFreeSound(this->currMusic);
        this->currMusic = 0;
    }
}

void MusicManager::update() {
    if (userOptions->musicVolume <= 0) {
        return;
    }

    if (this->useDefault) {
        if (CurrGameState == GAMESTATE_MAINMENU) {
            this->shouldPlay = MUS_MENU;
        } else {
            this->shouldPlay = MUS_LCZ;
        }
        //TODO: Play zone track here depending on game's current state.
    }

    if (!this->nowPlaying.equals(this->shouldPlay)) {
        if (this->fadeOut && this->currMusicVolume > 0) {
            this->currMusicVolume -= timing->tickDuration / 250.0;
        } else {
            if (this->channel != 0) {
                bbStopChannel(this->channel);
                this->channel = nullptr;
            }

            this->freeMusic();
            this->nowPlaying = this->shouldPlay;
            this->currMusic = bbLoadSound(this->nowPlaying);

            this->fadeOut = false;
            this->currMusicVolume = userOptions->musicVolume;
        }
    } else if (this->currMusicVolume != userOptions->musicVolume) {
        this->currMusicVolume = userOptions->musicVolume;
    }

    if (!bbChannelPlaying(this->channel)) {
        this->channel = bbPlaySound(this->currMusic);
    }

    bbChannelVolume(this->channel, this->currMusicVolume);
}

}

#include <bbaudio.h>
#include <bbblitz3d.h>
#include <bbmath.h>

#include "Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Config/Options.h"
#include "../NPCs/NPCs.h"
#include "../Player/Player.h"
#include "../GameMain.h"
#include "../Items/Item.h"
#include "../Map/Events/Events.h"
#include "../Map/MapSystem.h"
#include "../Map/Particles.h"
#include "../Map/RM2/Materials.h"

namespace CBN {

// Structs.
std::vector<SoundWrapper*> SoundWrapper::list;
SoundWrapper::SoundWrapper() {
    list.push_back(this);
}
SoundWrapper::~SoundWrapper() {
    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            if (internal != nullptr) {
                bbFreeSound(internal);
            }

            list.erase(list.begin() + i);
            break;
        }
    }
}

SoundWrapper* SoundWrapper::Initialize(const String& fileName) {
    SoundWrapper* snd = new SoundWrapper();
    snd->file = fileName;

    return snd;
}

SoundWrapper* SoundWrapper::Load(const String& fileName) {
    SoundWrapper* snd = SoundWrapper::Initialize(fileName);
    snd->internal = bbLoadSound(fileName);

    return snd;
}

SoundManager::SoundManager() {
    memset(this, 0, sizeof(SoundManager));
    button = SoundWrapper::Load("SFX/Interact/Button.ogg");
}

std::vector<SoundChannel*> SoundChannel::list;
SoundChannel::SoundChannel() {
    list.push_back(this);
}
SoundChannel::~SoundChannel() {
    for (int i = 0; i < (int)list.size(); i++) {
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

void SoundManager::loadInGameSounds() {
    buttonErr = SoundWrapper::Initialize("SFX/Interact/Button2.ogg");

    for (int i = 0; i < 8; i++) {
        footstep[i] = SoundWrapper::Load("SFX/Step/Step" + String(i + 1) + ".ogg");
        footstepRun[i] = SoundWrapper::Load("SFX/Step/Run" + String(i + 1) + ".ogg");
        footstepMetal[i] = SoundWrapper::Load("SFX/Step/StepMetal" + String(i + 1) + ".ogg");
        footstepMetalRun[i] = SoundWrapper::Load("SFX/Step/RunMetal" + String(i + 1) + ".ogg");
    }

    for (int i = 0; i < 3; i++) {
        footstepPD[i] = SoundWrapper::Initialize("SFX/Step/StepPD" + String(i + 1) + ".ogg");
        footstep8601[i] = SoundWrapper::Initialize("SFX/Step/StepForest" + String(i + 1) + ".ogg");
    }

    for (int i = 0; i < 3; i++) {
        openDoor[i] = SoundWrapper::Load("SFX/Door/DoorOpen" + String(i + 1) + ".ogg");
        closeDoor[i] = SoundWrapper::Load("SFX/Door/DoorClose" + String(i + 1) + ".ogg");
        openHCZDoor[i] = SoundWrapper::Load("SFX/Door/Door2Open" + String(i + 1) + ".ogg");
        closeHCZDoor[i] = SoundWrapper::Load("SFX/Door/Door2Close" + String(i + 1) + ".ogg");
    }

    for (int i = 0; i < 2; i++) {
        openBigDoor[i] = SoundWrapper::Initialize("SFX/Door/BigDoorOpen" + String(i + 1) + ".ogg");
        closeBigDoor[i] = SoundWrapper::Initialize("SFX/Door/BigDoorClose" + String(i + 1) + ".ogg");
    }

    keycardUse = SoundWrapper::Initialize("SFX/Interact/KeyCardUse1.ogg");
    keycardErr = SoundWrapper::Initialize("SFX/Interact/KeyCardUse2.ogg");
    scannerUse = SoundWrapper::Initialize("SFX/Interact/ScannerUse1.ogg");
    scannerErr = SoundWrapper::Initialize("SFX/Interact/ScannerUse2.ogg");

    itemPick[ItemPickSound::Tiny] = SoundWrapper::Load("SFX/Interact/PickItem_Tiny.ogg");
    itemPick[ItemPickSound::Metallic] = SoundWrapper::Load("SFX/Interact/PickItem_Metallic.ogg");
    itemPick[ItemPickSound::Heafty] = SoundWrapper::Load("SFX/Interact/PickItem_Heafty.ogg");
    itemPick[ItemPickSound::Paper] = SoundWrapper::Load("SFX/Interact/PickItem_Paper.ogg");

    elevatorBeep = SoundWrapper::Initialize("SFX/General/Elevator/Beep.ogg");
    elevatorMove = SoundWrapper::Initialize("SFX/General/Elevator/Moving.ogg");

    teslaIdle = SoundWrapper::Initialize("SFX/Room/Tesla/Idle.ogg");
    teslaActive = SoundWrapper::Initialize("SFX/Room/Tesla/WindUp.ogg");
    teslaPowerUp = SoundWrapper::Initialize("SFX/Room/Tesla/PowerUp.ogg");
    teslaShock = SoundWrapper::Initialize("SFX/Room/Tesla/Shock.ogg");

    gunshot[0] = SoundWrapper::Initialize("SFX/General/Gunshot.ogg");
    gunshot[1] = SoundWrapper::Initialize("SFX/General/Gunshot2.ogg");
    bulletHit = SoundWrapper::Initialize("SFX/General/BulletHit.ogg");
    bulletMiss = SoundWrapper::Initialize("SFX/General/BulletMiss.ogg");

    alarm = SoundWrapper::Initialize("SFX/General/Alarm.ogg");
    caution = SoundWrapper::Initialize("SFX/Room/LockroomSiren.ogg");
    hiss = SoundWrapper::Initialize("SFX/General/Hiss.ogg");
    lightSwitch = SoundWrapper::Initialize("SFX/General/LightSwitch.ogg");
    lever = SoundWrapper::Initialize("SFX/Interact/LeverFlip.ogg");
    burst = SoundWrapper::Initialize("SFX/Room/TunnelBurst.ogg");
    camera = SoundWrapper::Initialize("SFX/General/Camera.ogg");
    heartbeat = SoundWrapper::Initialize("SFX/Character/D9341/Heartbeat.ogg");
}

void SoundManager::deloadInGameSounds() {
    for (int i = 0; i < (int)SoundWrapper::list.size(); i++)
    {
        if (SoundWrapper::list[i] == this->button) { continue; }
        delete SoundWrapper::list[i];
        i--;
    }
    itemPick.clear();
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
SoundManager* sndMgmt;
MusicManager* musicMgmt;

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
    for (int i = 0; i < SoundChannel::getListSize(); i++) {
        SoundChannel* chn = SoundChannel::getObject(i);

        if (!bbChannelPlaying(chn->internal)) {
            bbFreeEntity(chn->point);
            delete chn;
        } else if (chn->camera != nullptr) {
            UpdateRangedSoundOrigin_SM(chn);
        }
    }
}

gxChannel* PlaySound2(gxSound* snd) {
    gxChannel* chn = bbPlaySound(snd);

    SoundChannel* sc = new SoundChannel();
    sc->internal = chn;

    return chn;
}

gxChannel* PlaySound_SM(SoundWrapper* snd) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == nullptr) {
        snd->internal = bbLoadSound(snd->file);
    }

    //Play the sound.
    return PlaySound2(snd->internal);
}

gxChannel* PlayRangedSound(gxSound* soundHandle, Camera* cam, Object* entity, float range, float volume) {
    float dist;
    float panvalue;

    range = Max(range, 1.f);
    gxChannel* soundChn = nullptr;

    if (volume > 0) {
        dist = bbEntityDistance(cam, entity) / range;
        if (1 - dist > 0 && 1 - dist < 1) {
            panvalue = bbSin(-bbDeltaYaw(cam, entity));
            soundChn = bbPlaySound(soundHandle);

            bbChannelVolume(soundChn, volume * (1 - dist) * userOptions->sndVolume);
            bbChannelPan(soundChn, panvalue);
        }
    }

    AddPositionalChannel(soundChn, cam, entity, range, volume);

    return soundChn;
}

gxChannel* PlayRangedSound_SM(SoundWrapper* snd, Camera* cam, Object* entity, float range, float volume) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == nullptr) {
        snd->internal = bbLoadSound(snd->file);
    }

    return PlayRangedSound(snd->internal, cam, entity, range, volume);
}

gxChannel* LoopRangedSound(gxSound* soundHandle, gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
    range = Max(range,1.f);

    if (!bbChannelPlaying(chn)) {
        chn = bbPlaySound(soundHandle);
    }

    UpdateRangedSoundOrigin(chn, cam, entity, range, volume);

    return chn;
}

gxChannel* LoopRangedSound_SM(SoundWrapper* snd, gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
    //If the sound hasn't been loaded yet then do that.
    if (snd->internal == 0) {
        snd->internal = bbLoadSound(snd->file);
    }

    return LoopRangedSound(snd->internal, chn, cam, entity, range, volume);
}

void UpdateRangedSoundOrigin(gxChannel* chn, Camera* cam, Object* entity, float range, float volume) {
    float dist;
    float panvalue;

    range = Max(range, 1.f);

    if (volume > 0) {

        dist = bbEntityDistance(cam, entity) / range;
        if (1 - dist > 0 && 1 - dist < 1) {
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
        if (1 - dist > 0 && 1 - dist < 1) {
            panvalue = bbSin(-bbDeltaYaw(chn->camera, chn->point));

            bbChannelVolume(chn->internal, chn->volume * (1 - dist) * userOptions->sndVolume);
            bbChannelPan(chn->internal, panvalue);
        }
    } else {
        bbChannelVolume(chn->internal, 0);
    }
}

gxSound* LoadTempSound(const String& file) {
    if (TempSounds[TempSoundIndex] != 0) {
        bbFreeSound(TempSounds[TempSoundIndex]);
    }

    gxSound* TempSound = bbLoadSound(file);
    TempSounds[TempSoundIndex] = TempSound;

    TempSoundIndex = (TempSoundIndex + 1) % 10;

    return TempSound;
}

gxSound* LoadEventSound(Event* e, const String& file, int i) {
    //TODO: redo

    return nullptr;
    /*if (e->sounds[i] != 0) {
        bbFreeSound(e->sounds[i]);
        e->sounds[i] = 0;
    }
    e->sounds[i] = bbLoadSound(file);

    return e->sounds[i];*/
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

    /*TODO: asdasdasdasd

    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        for (int j = 0; j < EVENT_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(e->soundChannels[j])) {
                bbPauseChannel(e->soundChannels[j]);
            }
        }
    }*/

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

    /*TODO: everything is broken

    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        for (int j = 0; j < EVENT_CHANNEL_COUNT; j++) {
            if (bbChannelPlaying(e->soundChannels[j])) {
                bbResumeChannel(e->soundChannels[j]);
            }
        }
    }*/

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

    /*TODO: a

    for (int iterator35 = 0; iterator35 < Event::getListSize(); iterator35++) {
        e = Event::getObject(iterator35);

        for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
            if (bbChannelPlaying(e->soundChannels[i])) {
                bbStopChannel(e->soundChannels[i]);
            }
        }
    }*/

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

void MusicManager::setNextMusicTrack(const String& trackName, int fadeOut) {
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
            this->currMusicVolume -= timing->tickDuration / 250.f;
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

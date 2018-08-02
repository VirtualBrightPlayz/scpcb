#include "Events.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Event*> Event::list;
Event::Event() {
    list.push_back(this);
}
Event::~Event() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Event::getListSize() {
    return list.size();
}
Event* Event::getObject(int index) {
    return list[index];
}

// Constants.
const int EVENT_MAX_STATE_COUNT = 5;
const int EVENT_SOUND_COUNT = 5;
const int EVENT_CHANNEL_COUNT = 2;

// Functions.
Event* CreateEvent(String eventname, String roomname, int id, float prob = 0.0) {
    //roomname = the name of the room(s) you want the event to be assigned to

    //the id-variable determines which of the rooms the event is assigned to,
    //0 will assign it to the first generated room, 1 to the second, etc

    //the prob-variable can be used to randomly assign events into some rooms
    //0.5 means that there's a 50% chance that event is assigned to the rooms
    //1.0 means that the event is assigned to every room
    //the id-variable is ignored if prob <> 0.0

    int i = 0;
    int temp;
    Event* e;
    Event* e2;
    Room* r;

    if (prob == 0.0) {
        for (int iterator57 = 0; iterator57 < Room::getListSize(); iterator57++) {
            r = Room::getObject(iterator57);

            if (roomname == "" | roomname == r->roomTemplate->name) {
                temp = false;
                for (int iterator58 = 0; iterator58 < Event::getListSize(); iterator58++) {
                    e2 = Event::getObject(iterator58);

                    if (e2->room == r) {
                        temp = true;
                        Exit;
                    }
                }

                i = i+1;
                if (i >= id & temp == false) {
                    e = new Event();
                    e->name = eventname;
                    e->room = r;
                    return e;
                }
            }
        }
    } else {
        for (int iterator59 = 0; iterator59 < Room::getListSize(); iterator59++) {
            r = Room::getObject(iterator59);

            if (roomname == "" | roomname == r->roomTemplate->name) {
                temp = false;
                for (int iterator60 = 0; iterator60 < Event::getListSize(); iterator60++) {
                    e2 = Event::getObject(iterator60);

                    if (e2->room == r) {
                        temp = true;
                        Exit;
                    }
                }

                if (bbRnd(0.0, 1.0) < prob & temp == false) {
                    e = new Event();
                    e->name = eventname;
                    e->room = r;
                }
            }
        }
    }

    return nullptr;
}

void RemoveEvent(Event* e) {
    int i;
    for (i = 0; i <= EVENT_SOUND_COUNT-1; i++) {
        if (e->sounds[i] != 0) {
            bbFreeSound(e->sounds[i]);
        }
    }

    for (i = 0; i <= EVENT_CHANNEL_COUNT-1; i++) {
        if (e->soundChannels[i] != 0) {
            if (IsChannelPlaying(e->soundChannels[i])) {
                bbStopChannel(e->soundChannels[i]);
            }
        }
    }

    //If (e\img<>0) Then FreeImage(e\img)

    delete e;
}

void InitEvents() {
    Event* e;

    CreateEvent("evt_intro", "roomintro", 0);
    CreateEvent("evt_cont_173", "cont_173_1", 0);

    CreateEvent("evt_pd", "pocketdimension", 0);

    //there's a 7% chance that 106 appears in the rooms named "tnnl_plain_2"
    CreateEvent("evt_tunnl_106", "tnnl_plain_2", 0, 0.07 + (0.1*SelectedDifficulty->aggressiveNPCs));

    //the chance for 173 appearing in the first lockroom is about 66%
    //there's a 30% chance that it appears in the later lockrooms
    if (bbRand(3)<3) {
        CreateEvent("evt_lck_173", "lck_cam_2c", 0);
    }
    CreateEvent("evt_lck_173", "lck_cam_2c", 0, 0.3 + (0.5*SelectedDifficulty->aggressiveNPCs));

    CreateEvent("evt_hll_trick", "hll_plain_2", 0, 0.15);

    CreateEvent("evt_hll_970", "scp_970_2", 0);

    CreateEvent("evt_end_106", "end_1", bbRand(0,1));

    CreateEvent("evt_off_l_conf", "off_l_conf_2", 0);

    CreateEvent("evt_hll_fan", "hll_fan_2", 0, 1.0);

    CreateEvent("evt_ele_guard", "hll_ele_2", 0);
    CreateEvent("evt_ele_gonzales", "hll_ele_2", bbRand(1,2));

    CreateEvent("evt_strg_939", "strg_939_3", 0, 0);

    CreateEvent("evt_tnnl_smoke", "tnnl_elec_2", 0, 0.2);
    CreateEvent("evt_tnnl_blackout", "tnnl_elec_2", bbRand(0,2), 0);
    CreateEvent("evt_tnnl_blackout", "tnnl_elec_2", 0, (0.2*SelectedDifficulty->aggressiveNPCs));

    //173 appears in half of the "lck_tshape_2" -rooms
    CreateEvent("evt_lck_tshape_173", "lck_tshape_2", 0, 0.5 + (0.4*SelectedDifficulty->aggressiveNPCs));

    //the anomalous duck in room2offices2-rooms
    CreateEvent("evt_off_lower", "off_lower_2", 0, 0.7);

    CreateEvent("evt_closets_173", "closets_2", 0);

    CreateEvent("evt_hll_caf", "hll_caf_2", 0);

    CreateEvent("evt_gas_duck", "hll_gas_3", 0);
    CreateEvent("evt_gas_1048", "hll_gas_3", 1);

    //the event that causes the door to open by itself in room2offices3
    CreateEvent("evt_off_2level", "off_2level_2", 0, 1.0);

    CreateEvent("evt_srvr_096", "srvr_096_2", 0);

    CreateEvent("evt_srvr", "srvr_farm_3", 0);
    CreateEvent("evt_srvr", "srvr_lshape_3", 0);

    //the dead guard
    CreateEvent("evt_tnnl_plain_3", "tnnl_plain_3", 0, 0.08);

    CreateEvent("evt_hll_plain_4","hll_plain_4", 0);

    if (bbRand(5)<5) {
        switch (bbRand(3)) {
            case 1: {
                CreateEvent("evnt_roar_682", "tnnl_plain_2", bbRand(0,2), 0);
            }
            case 2: {
                CreateEvent("evnt_roar_682", "hll_gas_3", bbRand(0,2), 0);
            }
            case 3: {
                CreateEvent("evnt_roar_682", "off_plain_2", 0, 0);
            }
        }
    }

    CreateEvent("evt_test_173", "test_smallwindow_2", 0, 1.0);

    CreateEvent("evt_tesla", "tsl_ez_2", 0, 0.9);
    CreateEvent("evt_tesla", "tsl_lcz_2", 0, 0.9);
    CreateEvent("evt_tesla", "tsl_hcz_2", 0, 0.9);

    CreateEvent("evt_tnnl_nuke", "tnnl_nuke_2", 0, 0);

    if (bbRand(5) < 5) {
        CreateEvent("evt_cont_895_106", "coffin", 0, 0);
    } else {
        CreateEvent("evt_cont_895", "coffin", 0, 0);
    }

    CreateEvent("evt_chck", "chck_lcz_hcz_2", 0, 1.0);
    CreateEvent("evt_chck", "chck_hcz_ez_2", 0, 1.0);

    if (bbRand(2)==1) {
        CreateEvent("evt_hll_106_victim", "hll_plain_3", bbRand(1,2));
        CreateEvent("evt_hll_sinkhole", "hll_plain_3", bbRand(2,3));
    } else {
        CreateEvent("evt_hll_106_victim", "hll_pipes_3", bbRand(1,2));
        CreateEvent("evt_hll_sinkhole", "hll_pipes_3", bbRand(2,3));
    }
    CreateEvent("evt_hll_sinkhole", "hll_plain_4", bbRand(1,2));

    CreateEvent("evt_cont_079", "cont_079_1", 0, 0);

    CreateEvent("evt_cont_049", "cont_049_2", 0, 0);

    CreateEvent("evt_cont_012", "cont_012_2", 0, 0);

    CreateEvent("evt_cont_035", "cont_035_1", 0, 0);

    CreateEvent("evt_cont_008", "cont_008_1", 0, 0);

    CreateEvent("evt_cont_106", "cont_106_1", 0, 0);

    CreateEvent("evt_cont_914", "cont_914_1", 0, 0);

    CreateEvent("evt_toilets_suicide", "hll_toilets_2", 1);
    CreateEvent("evt_toilets_789j", "hll_toilets_2", 0, 0.8);

    CreateEvent("evt_106_pass", "tnnl_pipes_2", bbRand(0, 3));

    CreateEvent("evt_hll_gas", "hll_gas_2", 0, 0.4 + (0.4*SelectedDifficulty->aggressiveNPCs));
    CreateEvent("evt_hll_gas", "hll_lshape_2",0,0.4 + (0.4*SelectedDifficulty->aggressiveNPCs));

    CreateEvent("evt_test_682", "test_682_2", 0);

    CreateEvent("evt_tnnl_maintenance", "tnnl_maintenance_2", 0);

    CreateEvent("evt_strg_elec", "strg_elec_2c", 0);

    CreateEvent("evt_gatea", "exit_gatea_1", 0);
    CreateEvent("evt_extend_gatea", "extend_gatea_1", 0);
    CreateEvent("evt_gateb", "exit_gateb_1", 0);

    CreateEvent("evt_cont_205", "cont_205_1", 0);

    CreateEvent("evt_test_860","test_860_2", 0);

    CreateEvent("evt_cont_966","cont_966_3", 0);

    CreateEvent("cont_1123_2", "cont_1123_2", 0, 0);

    CreateEvent("evt_1499", "dimension1499",0);
    CreateEvent("evt_cont_1162", "cont_1162_2c",0);
    CreateEvent("evt_cont_500_1499", "cont_500_1499_2",0);

    CreateEvent("evt_tnnl_plain_4", "tnnl_plain_4",0);

    CreateEvent("evt_lck_air", "lck_air_2",0,1.0);
    CreateEvent("evt_lck_air", "lck_ez_3",0,1.0);
    CreateEvent("evt_lck_air_broke", "lck_air_broke_2",bbRand(0,1));

    CreateEvent("evt_hll_sl", "hll_sl_2",0);
}

void UpdateEvents() {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e;
    Event* e2;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    mainPlayer->footstepOverride = 0;

    UpdateRooms();

    for (int iterator61 = 0; iterator61 < Event::getListSize(); iterator61++) {
        e = Event::getObject(iterator61);

        // Does the event have music to play?
        if (e->overwriteMusic) {
            SetNextMusicTrack(e->musicTrack);
        } else if ((!musicManager->useDefault)) {
            // If the event was previously playing music then go back to the default.
            if (e->musicTrack == musicManager->nowPlaying) {
                RestoreDefaultMusic();
            }
        }

        switch (e->name) {
            case "evt_intro": {
                UpdateEvent173(e);
            }
            case "evt_cont_173": {
                UpdateEventAlarm(e);
            }
            case "evt_pd": {
                UpdateEvent_pocketdimension(e);
            }
            case "evt_tunnl_106": {
                UpdateEventTunnel106(e);
            }
            case "evt_lck_173": {
                UpdateEventLockroom173(e);
            }
            case "evt_hll_trick": {
                UpdateEventRoom2trick(e);
            }
            case "evt_hll_970": {
                UpdateEvent_scp_970_2(e);
            }
            case "evt_end_106": {
                UpdateEventEndroom106(e);
            }
            case "evt_off_l_conf": {
                UpdateEvent_off_l_conf_2(e);
            }
            case "evt_hll_fan": {
                UpdateEventRoom2fan(e);
            }
            case "evt_ele_guard": {
                UpdateEvent_hll_ele_2(e);
            }
            case "evt_ele_gonzales": {
                UpdateEventRoom2elevator2(e);
            }
            case "evt_strg_939": {
                UpdateEvent_strg_939_3(e);
            }
            case "evt_tnnl_smoke": {
                UpdateEventTunnel2smoke(e);
            }
            case "evt_tnnl_blackout": {
                UpdateEvent_tnnl_elec_2(e);
            }
            case "evt_lck_tshape_173": {
                UpdateEventRoom2doors173(e);
            }
            case "evt_off_lower": {
                UpdateEvent_off_lower_2(e);
            }
            case "evt_closets_173": {
                UpdateEvent_closets_2(e);
            }
            case "evt_hll_caf": {
                UpdateEvent_hll_caf_2(e);
            }
            case "evt_gas_duck": {
                UpdateEventRoom3pitduck(e);
            }
            case "evt_gas_1048": {
                UpdateEventRoom3pit1048(e);
            }
            case "evt_off_2level": {
                UpdateEvent_off_2level_2(e);
            }
            case "evt_srvr_096": {
                UpdateEvent_srvr_096_2(e);
            }
            case "evt_srvr": {
                UpdateEvent_srvr_farm_3(e);
            }
            case "evt_tnnl_plain_3": {
                UpdateEvent_tnnl_plain_3(e);
            }
            case "evt_hll_plain_4": {
                UpdateEvent_hll_plain_4(e);
            }
            case "evt_roar_682": {
                UpdateEvent682roar(e);
            }
            case "evt_test_173": {
                UpdateEventTestroom173(e);
            }
            case "evt_tesla": {
                UpdateEvent_tesla(e);
            }
            case "evt_tnnl_nuke": {
                UpdateEvent_tnnl_nuke_2(e);
            }
            case "evt_cont_895_106": {
                UpdateEventCoffin106(e);
            }
            case "evt_cont_895": {
                UpdateEventCoffin(e);
            }
            case "evt_hll_106_victim": {
                UpdateEvent106victim(e);
            }
            case "evt_hll_sinkhole": {
                UpdateEvent106sinkhole(e);
            }
            case "evt_cont_079": {
                UpdateEvent_cont_079_1(e);
            }
            case "evt_cont_049": {
                UpdateEvent_cont_049_2(e);
            }
            case "evt_cont_012": {
                UpdateEvent_cont_012_2(e);
            }
            case "evt_cont_035": {
                UpdateEvent_cont_035_1(e);
            }
            case "evt_cont_008": {
                UpdateEvent_cont_008_1(e);
            }
            case "evt_cont_106": {
                UpdateEvent_cont_106_1(e);
            }
            case "evt_cont_914": {
                UpdateEvent_cont_914_1(e);
            }
            case "evt_toilets_suicide": {
                UpdateEventToiletguard(e);
            }
            case "evt_toilets_789j": {
                UpdateEventButtghost(e);
            }
            case "evt_106_pass": {
                UpdateEventRoom2pipes106(e);
            }
            case "evt_hll_gas": {
                UpdateEvent_hll_gas_2(e);
            }
            case "evt_test_682": {
                UpdateEvent_test_682_2(e);
            }
            case "evt_tnnl_maintenance": {
                UpdateEvent_tnnl_maintenance_2(e);
            }
            case "evt_strg_elec": {
                UpdateEvent_strg_elec_2c(e);
            }
            case "evt_gatea": {
                UpdateEvent_exit_gatea_1(e);
            }
            case "evt_extend_gatea": {
                UpdateEvent_extend_gatea_1(e);
            }
            case "evt_gateb": {
                UpdateEventExit1(e);
            }
            case "evt_cont_205": {
                UpdateEvent_cont_205_1(e);
            }
            case "evt_test_860": {
                UpdateEvent_test_860_2(e);
            }
            case "evt_cont_966": {
                UpdateEvent_cont_966_3(e);
            }
            case "evt_cont_1123": {
                UpdateEvent_cont_1123_2(e);
            }
            case "evt_1499": {
                UpdateEvent_dimension1499(e);
            }
            case "evt_cont_1162": {
                UpdateEvent_cont_1162_2c(e);
            }
            case "evt_cont_500_1499": {
                UpdateEvent_cont_500_1499_2(e);
            }
            case "evt_tnnl_plain_4": {
                UpdateEvent_tnnl_plain_4(e);
            }
            case "evt_lck_air": {
                UpdateEvent_lck_air_2(e);
            }
            case "evt_lck_air_broke": {
                UpdateEvent_lck_air_broke_2(e);
            }
            case "evt_hll_sl": {
                UpdateEvent_hll_sl_2(e);
            }
        }
    }

    if (ExplosionTimer > 0) {
        ExplosionTimer = ExplosionTimer+timing->tickDuration;

        if (ExplosionTimer < 140.0) {
            if (ExplosionTimer-timing->tickDuration < 5.0) {
                PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke1.ogg"));
                mainPlayer->camShake = 10.0;
                ExplosionTimer = 5.0;
            }

            mainPlayer->camShake = CurveValue(ExplosionTimer/60.0,mainPlayer->camShake, 50.0);
        } else {
            mainPlayer->camShake = Min((ExplosionTimer/20.0),20.0);
            if (ExplosionTimer-timing->tickDuration < 140.0) {
                mainPlayer->blinkTimer = 1.0;
                PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke2.ogg"));
                for (i = 0; i <= 40; i++) {
                    p = CreateParticle(bbEntityX(mainPlayer->collider)+bbRnd(-0.5,0.5),bbEntityY(mainPlayer->collider)-bbRnd(0.2,1.5),bbEntityZ(mainPlayer->collider)+bbRnd(-0.5,0.5), PARTICLE_SMOKE_BLACK, bbRnd(0.2,0.6), 0.0, 350);
                    bbRotateEntity(p->pvt,-90,0,0,true);
                    p->speed = bbRnd(0.05,0.07);
                }
            }
            mainPlayer->lightFlash = Min((ExplosionTimer-160.0)/40.0,2.0);
            // : EndingTimer = Min(mainPlayer\fallTimer,-0.1)
            if (ExplosionTimer > 160) {
                Kill(mainPlayer);
            }
            if (ExplosionTimer > 500) {
                ExplosionTimer = 0;
            }
        }

    }
}

}

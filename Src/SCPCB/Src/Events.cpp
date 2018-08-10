#include <bbmath.h>
#include <bbaudio.h>
#include <string.h>

#include "Events.h"
#include "MapSystem.h"
#include "Player.h"
#include "Audio.h"

#include "Rooms/Room_closets_2.h"
#include "Rooms/Room_cont_008_1.h"
#include "Rooms/Room_cont_012_2.h"
#include "Rooms/Room_cont_035_1.h"
#include "Rooms/Room_cont_049_2.h"
#include "Rooms/Room_cont_079_1.h"
#include "Rooms/Room_cont_106_1.h"
#include "Rooms/Room_cont_1123_2.h"
#include "Rooms/Room_cont_1162_2c.h"
#include "Rooms/Room_cont_173_1.h"
#include "Rooms/Room_cont_205_1.h"
#include "Rooms/Room_cont_500_1499_2.h"
#include "Rooms/Room_cont_513_3.h"
#include "Rooms/Room_cont_714_860_1025_2.h"
#include "Rooms/Room_cont_895_1.h"
#include "Rooms/Room_cont_914_1.h"
#include "Rooms/Room_cont_966_3.h"
#include "Rooms/Room_dimension1499.h"
#include "Rooms/Room_end_1.h"
#include "Rooms/Room_exit_gatea_1.h"
#include "Rooms/Room_exit_gateb_1.h"
#include "Rooms/Room_extend_gatea_1.h"
#include "Rooms/Room_hll_bench_3.h"
#include "Rooms/Room_hll_caf_2.h"
#include "Rooms/Room_hll_dirty_3.h"
#include "Rooms/Room_hll_ele_2.h"
#include "Rooms/Room_hll_fan_2.h"
#include "Rooms/Room_hll_gas_2.h"
#include "Rooms/Room_hll_gas_3.h"
#include "Rooms/Room_hll_lshape_2.h"
#include "Rooms/Room_hll_pipes_3.h"
#include "Rooms/Room_hll_plain_2.h"
#include "Rooms/Room_hll_plain_3.h"
#include "Rooms/Room_hll_plain_4.h"
#include "Rooms/Room_hll_sl_2.h"
#include "Rooms/Room_hll_toilets_2.h"
#include "Rooms/Room_hll_tsl.h"
#include "Rooms/Room_intro.h"
#include "Rooms/Room_lck_096_2c.h"
#include "Rooms/Room_lck_air_2.h"
#include "Rooms/Room_lck_broke_2c.h"
#include "Rooms/Room_lck_cam_2c.h"
#include "Rooms/Room_lck_ez_3.h"
#include "Rooms/Room_lck_tshape_2.h"
#include "Rooms/Room_lifts_1.h"
#include "Rooms/Room_off_2level_2.h"
#include "Rooms/Room_off_bain_2.h"
#include "Rooms/Room_off_gears_may_har_2.h"
#include "Rooms/Room_off_glss_3.h"
#include "Rooms/Room_off_l_conf_2.h"
#include "Rooms/Room_off_lower_2.h"
#include "Rooms/Room_off_plain_2.h"
#include "Rooms/Room_off_rosewood_2.h"
#include "Rooms/Room_pocketdimension.h"
#include "Rooms/Room_scp_970_2.h"
#include "Rooms/Room_srvr_096_2.h"
#include "Rooms/Room_srvr_farm_3.h"
#include "Rooms/Room_srvr_lshape_3.h"
#include "Rooms/Room_srvr_pc_2.h"
#include "Rooms/Room_strg_939_3.h"
#include "Rooms/Room_strg_elec_2c.h"
#include "Rooms/Room_test_682_2.h"
#include "Rooms/Room_test_860_2.h"
#include "Rooms/Room_test_smallwindow_2.h"
#include "Rooms/Room_tnnl_elec_2.h"
#include "Rooms/Room_tnnl_maintenance_2.h"
#include "Rooms/Room_tnnl_nuke_2.h"
#include "Rooms/Room_tnnl_pipes_2.h"
#include "Rooms/Room_tnnl_plain_2.h"
#include "Rooms/Room_tnnl_plain_3.h"
#include "Rooms/Room_tnnl_plain_4.h"

namespace CBN {

// Structs.
std::vector<Event*> Event::list;
Event::Event() {
    memset(this, 0, sizeof(Event));

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

// Functions.
Event* CreateEvent(String eventname, String roomname, int id, float prob) {
    //roomname = the name of the room(s) you want the event to be assigned to

    //the id-variable determines which of the rooms the event is assigned to,
    //0 will assign it to the first generated room, 1 to the second, etc

    //the prob-variable can be used to randomly assign events into some rooms
    //0.5 means that there's a 50% chance that event is assigned to the rooms
    //1.0 means that the event is assigned to every room
    //the id-variable is ignored if prob <> 0.0

    // FIXME
    if (prob == 0.0) {
        for (int i = 0; i < Room::getListSize(); i++) {
            Room* r = Room::getObject(i);

            if (roomname.isEmpty() || roomname.equals(r->roomTemplate->name)) {
                bool temp = false;
                for (int j = 0; j < Event::getListSize(); j++) {
                    Event* e2 = Event::getObject(j);

                    if (e2->room == r) {
                        temp = true;
                        break;
                    }
                }

                i++;
                if (i >= id && !temp) {
                    Event* e = new Event();
                    e->name = eventname;
                    e->room = r;
                    return e;
                }
            }
        }
    } else {
        for (int i = 0; i < Room::getListSize(); i++) {
            Room* r = Room::getObject(i);

            if (roomname.isEmpty() || roomname.equals(r->roomTemplate->name)) {
                bool temp = false;
                for (int j = 0; j < Event::getListSize(); j++) {
                    Event* e2 = Event::getObject(j);

                    if (e2->room == r) {
                        temp = true;
                        break;
                    }
                }

                if (bbRnd(0.0, 1.0) < prob & temp == false) {
                    Event* e = new Event();
                    e->name = eventname;
                    e->room = r;
                }
            }
        }
    }

    return nullptr;
}

void RemoveEvent(Event* e) {
    for (int i = 0; i < EVENT_SOUND_COUNT; i++) {
        if (e->sounds[i] != nullptr) {
            bbFreeSound(e->sounds[i]);
        }
    }

    for (int i = 0; i < EVENT_CHANNEL_COUNT; i++) {
        if (e->soundChannels[i] != nullptr) {
            if (bbChannelPlaying(e->soundChannels[i])) {
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
    CreateEvent("evt_tunnl_106", "tnnl_plain_2", 0, 0.07);

    //the chance for 173 appearing in the first lockroom is about 66%
    //there's a 30% chance that it appears in the later lockrooms
    if (bbRand(3)<3) {
        CreateEvent("evt_lck_173", "lck_cam_2c", 0);
    }
    CreateEvent("evt_lck_173", "lck_cam_2c", 0, 0.3);

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
    CreateEvent("evt_tnnl_blackout", "tnnl_elec_2", 0, 0);

    //173 appears in half of the "lck_tshape_2" -rooms
    CreateEvent("evt_lck_tshape_173", "lck_tshape_2", 0, 0.5);

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
            } break;
            case 2: {
                CreateEvent("evnt_roar_682", "hll_gas_3", bbRand(0,2), 0);
            } break;
            case 3: {
                CreateEvent("evnt_roar_682", "off_plain_2", 0, 0);
            } break;
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

    CreateEvent("evt_hll_gas", "hll_gas_2", 0, 0.4);
    CreateEvent("evt_hll_gas", "hll_lshape_2",0,0.4);

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
    mainPlayer->footstepOverride = 0;

    UpdateRooms();

    for (int i = 0; i < Event::getListSize(); i++) {
        Event* e = Event::getObject(i);

        // Does the event have music to play?
        if (e->overwriteMusic) {
            musicManager->setNextMusicTrack(e->musicTrack);
        } else if (!musicManager->useDefault) {
            // If the event was previously playing music then go back to the default.
            if (e->musicTrack.equals(musicManager->nowPlaying)) {
                musicManager->restoreDefaultMusic();
            }
        }

        if (e->name.equals("evt_intro")) {
            UpdateEvent173(e);
        }
        else if (e->name.equals("evt_cont_173")) {
            UpdateEventAlarm(e);
        }
        else if (e->name.equals("evt_pd")) {
            UpdateEvent_pocketdimension(e);
        }
        else if (e->name.equals("evt_tunnl_106")) {
            UpdateEventTunnel106(e);
        }
        else if (e->name.equals("evt_lck_173")) {
            UpdateEventLockroom173(e);
        }
        else if (e->name.equals("evt_hll_trick")) {
            UpdateEventRoom2trick(e);
        }
        else if (e->name.equals("evt_hll_970")) {
            UpdateEvent_scp_970_2(e);
        }
        else if (e->name.equals("evt_end_106")) {
            UpdateEventEndroom106(e);
        }
        else if (e->name.equals("evt_off_l_conf")) {
            UpdateEvent_off_l_conf_2(e);
        }
        else if (e->name.equals("evt_hll_fan")) {
            UpdateEventRoom2fan(e);
        }
        else if (e->name.equals("evt_ele_guard")) {
            UpdateEvent_hll_ele_2(e);
        }
        else if (e->name.equals("evt_ele_gonzales")) {
            UpdateEventRoom2elevator2(e);
        }
        else if (e->name.equals("evt_strg_939")) {
            UpdateEvent_strg_939_3(e);
        }
        else if (e->name.equals("evt_tnnl_smoke")) {
            UpdateEventTunnel2smoke(e);
        }
        else if (e->name.equals("evt_tnnl_blackout")) {
            UpdateEvent_tnnl_elec_2(e);
        }
        else if (e->name.equals("evt_lck_tshape_173")) {
            UpdateEventRoom2doors173(e);
        }
        else if (e->name.equals("evt_off_lower")) {
            UpdateEvent_off_lower_2(e);
        }
        else if (e->name.equals("evt_closets_173")) {
            UpdateEvent_closets_2(e);
        }
        else if (e->name.equals("evt_hll_caf")) {
            UpdateEvent_hll_caf_2(e);
        }
        else if (e->name.equals("evt_gas_duck")) {
            UpdateEventRoom3pitduck(e);
        }
        else if (e->name.equals("evt_gas_1048")) {
            UpdateEventRoom3pit1048(e);
        }
        else if (e->name.equals("evt_off_2level")) {
            UpdateEvent_off_2level_2(e);
        }
        else if (e->name.equals("evt_srvr_096")) {
            UpdateEvent_srvr_096_2(e);
        }
        else if (e->name.equals("evt_srvr")) {
            UpdateEvent_srvr_farm_3(e);
        }
        else if (e->name.equals("evt_tnnl_plain_3")) {
            UpdateEvent_tnnl_plain_3(e);
        }
        else if (e->name.equals("evt_hll_plain_4")) {
            UpdateEvent_hll_plain_4(e);
        }
        else if (e->name.equals("evt_roar_682")) {
            UpdateEvent682roar(e);
        }
        else if (e->name.equals("evt_test_173")) {
            UpdateEventTestroom173(e);
        }
        else if (e->name.equals("evt_tesla")) {
            UpdateEvent_tesla(e);
        }
        else if (e->name.equals("evt_tnnl_nuke")) {
            UpdateEvent_tnnl_nuke_2(e);
        }
        else if (e->name.equals("evt_cont_895_106")) {
            UpdateEventCoffin106(e);
        }
        else if (e->name.equals("evt_cont_895")) {
            UpdateEventCoffin(e);
        }
        else if (e->name.equals("evt_hll_106_victim")) {
            UpdateEvent106victim(e);
        }
        else if (e->name.equals("evt_hll_sinkhole")) {
            UpdateEvent106sinkhole(e);
        }
        else if (e->name.equals("evt_cont_079")) {
            UpdateEvent_cont_079_1(e);
        }
        else if (e->name.equals("evt_cont_049")) {
            UpdateEvent_cont_049_2(e);
        }
        else if (e->name.equals("evt_cont_012")) {
            UpdateEvent_cont_012_2(e);
        }
        else if (e->name.equals("evt_cont_035")) {
            UpdateEvent_cont_035_1(e);
        }
        else if (e->name.equals("evt_cont_008")) {
            UpdateEvent_cont_008_1(e);
        }
        else if (e->name.equals("evt_cont_106")) {
            UpdateEvent_cont_106_1(e);
        }
        else if (e->name.equals("evt_cont_914")) {
            UpdateEvent_cont_914_1(e);
        }
        else if (e->name.equals("evt_toilets_suicide")) {
            UpdateEventToiletguard(e);
        }
        else if (e->name.equals("evt_toilets_789j")) {
            UpdateEventButtghost(e);
        }
        else if (e->name.equals("evt_106_pass")) {
            UpdateEventRoom2pipes106(e);
        }
        else if (e->name.equals("evt_hll_gas")) {
            UpdateEvent_hll_gas_2(e);
        }
        else if (e->name.equals("evt_test_682")) {
            UpdateEvent_test_682_2(e);
        }
        else if (e->name.equals("evt_tnnl_maintenance")) {
            UpdateEvent_tnnl_maintenance_2(e);
        }
        else if (e->name.equals("evt_strg_elec")) {
            UpdateEvent_strg_elec_2c(e);
        }
        else if (e->name.equals("evt_gatea")) {
            UpdateEvent_exit_gatea_1(e);
        }
        else if (e->name.equals("evt_extend_gatea")) {
            UpdateEvent_extend_gatea_1(e);
        }
        else if (e->name.equals("evt_gateb")) {
            UpdateEventExit1(e);
        }
        else if (e->name.equals("evt_cont_205")) {
            UpdateEvent_cont_205_1(e);
        }
        else if (e->name.equals("evt_test_860")) {
            UpdateEvent_test_860_2(e);
        }
        else if (e->name.equals("evt_cont_966")) {
            UpdateEvent_cont_966_3(e);
        }
        else if (e->name.equals("evt_cont_1123")) {
            UpdateEvent_cont_1123_2(e);
        }
        else if (e->name.equals("evt_1499")) {
            //TODO: fix
        }
        else if (e->name.equals("evt_cont_1162")) {
            UpdateEvent_cont_1162_2c(e);
        }
        else if (e->name.equals("evt_cont_500_1499")) {
            UpdateEvent_cont_500_1499_2(e);
        }
        else if (e->name.equals("evt_tnnl_plain_4")) {
            UpdateEvent_tnnl_plain_4(e);
        }
        else if (e->name.equals("evt_lck_air")) {
            UpdateEvent_lck_air_2(e);
        }
        else if (e->name.equals("evt_lck_air_broke")) {
            UpdateEvent_lck_air_broke_2(e);
        }
        else if (e->name.equals("evt_hll_sl")) {
            UpdateEvent_hll_sl_2(e);
        }
    }

    // TODO: Move somewhere relevant.
    // if (ExplosionTimer > 0) {
    //     ExplosionTimer = ExplosionTimer+timing->tickDuration;

    //     if (ExplosionTimer < 140.0) {
    //         if (ExplosionTimer-timing->tickDuration < 5.0) {
    //             PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke1.ogg"));
    //             mainPlayer->camShake = 10.0;
    //             ExplosionTimer = 5.0;
    //         }

    //         mainPlayer->camShake = CurveValue(ExplosionTimer/60.0,mainPlayer->camShake, 50.0);
    //     } else {
    //         mainPlayer->camShake = Min((ExplosionTimer/20.0),20.0);
    //         if (ExplosionTimer-timing->tickDuration < 140.0) {
    //             mainPlayer->blinkTimer = 1.0;
    //             PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke2.ogg"));
    //             for (i = 0; i <= 40; i++) {
    //                 p = CreateParticle(bbEntityX(mainPlayer->collider)+bbRnd(-0.5,0.5),bbEntityY(mainPlayer->collider)-bbRnd(0.2,1.5),bbEntityZ(mainPlayer->collider)+bbRnd(-0.5,0.5), PARTICLE_SMOKE_BLACK, bbRnd(0.2,0.6), 0.0, 350);
    //                 bbRotateEntity(p->pvt,-90,0,0,true);
    //                 p->speed = bbRnd(0.05,0.07);
    //             }
    //         }
    //         mainPlayer->lightFlash = Min((ExplosionTimer-160.0)/40.0,2.0);
    //         // : EndingTimer = Min(mainPlayer\fallTimer,-0.1)
    //         if (ExplosionTimer > 160) {
    //             Kill(mainPlayer);
    //         }
    //         if (ExplosionTimer > 500) {
    //             ExplosionTimer = 0;
    //         }
    //     }

    // }
}

}

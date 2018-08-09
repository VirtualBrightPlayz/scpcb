#include "Room_cont_895_1.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_cont_895_1(Room* r) {
    Door* d;
    Door* d2;
    SecurityCam* sc;
    Decal* de;
    Room* r2;
    SecurityCam* sc2;
    Item* it;
    int i;
    int xtemp;
    int ytemp;
    int ztemp;

    //, Bump
    int t1;

    d = CreateDoor(r->x, 0, r->z - 448.0 * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], r->x - 384.0 * RoomScale, 0.7, r->z - 280.0 * RoomScale, true);

    sc = CreateSecurityCam(r->x - 320.0 * RoomScale, r->y + 704 * RoomScale, r->z + 288.0 * RoomScale, r, true);
    sc->angle = 45 + 180;
    sc->turn = 45;
    sc->coffinEffect = true;
    bbTurnEntity(sc->cameraObj, 120, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    CoffinCam = sc;

    bbPositionEntity(sc->scrObj, r->x - 800 * RoomScale, 288.0 * RoomScale, r->z - 340.0 * RoomScale);
    bbEntityParent(sc->scrObj, r->obj);
    bbTurnEntity(sc->scrObj, 0, 180, 0);

    r->levers[0] = CreateLever();

    bbScaleEntity(r->levers[0]->baseObj, 0.04, 0.04, 0.04);
    bbScaleEntity(r->levers[0]->obj, 0.04, 0.04, 0.04);
    bbPositionEntity(r->levers[0]->baseObj, r->x - 800.0 * RoomScale, r->y + 180.0 * RoomScale, r->z - 336 * RoomScale, true);
    bbPositionEntity(r->levers[0]->obj, r->x - 800.0 * RoomScale, r->y + 180.0 * RoomScale, r->z - 336 * RoomScale, true);

    bbEntityParent(r->levers[0]->baseObj, r->obj);
    bbEntityParent(r->levers[0]->obj, r->obj);

    bbRotateEntity(r->levers[0]->baseObj, 0, 180, 0);
    bbRotateEntity(r->levers[0]->obj, 10, 0, 0);

    bbEntityPickMode(r->levers[0]->obj, 1, false);
    bbEntityRadius(r->levers[0]->obj, 0.1);

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x, -1320.0 * RoomScale, r->z + 2304.0 * RoomScale);
    bbEntityParent(r->objects[0], r->obj);

    it = CreatePaper("doc895", r->x - 688.0 * RoomScale, r->y + 133.0 * RoomScale, r->z - 304.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("nvgoggles", r->x + 280.0 * RoomScale, r->y -1456.0 * RoomScale, r->z + 2164.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 96.0*RoomScale, -1532.0 * RoomScale, r->z + 2016.0 * RoomScale,true);

    //de.Decal = CreateDecal(DECAL_CORROSION, r\x + 96.0*RoomScale, -1535.0 * RoomScale, r\z + 32.0 * RoomScale, 90, Rand(360), 0)
    //EntityParent(de\obj, r\obj)
}

void UpdateEventCoffin106(Event* e) {
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
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

}

void UpdateEventCoffin(Event* e) {
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
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;
    int hasBatteryFor895;
    float tempF;
    float tempF2;
    float tempF3;

    //[Block]

    if (e->eventState < TimeInPosMilliSecs()) {
        //SCP-079 starts broadcasting 895 camera feed on monitors after leaving the first zone
        //TODO: rewrite this to adjust for separate zone loading
        //camera feed on
        if (bbEntityPitch(e->room->levers[0]->obj, true) > 0) {
            for (int iterator163 = 0; iterator163 < SecurityCam::getListSize(); iterator163++) {
                sc = SecurityCam::getObject(iterator163);

                if (!sc->specialCam) {
                    if (sc->coffinEffect==0 & sc->room->roomTemplate->name!="room106" & sc->room->roomTemplate->name!="room205") {
                        sc->coffinEffect = 2;
                    }
                    if (sc->room == e->room) {
                        sc->screen = true;
                    }
                }
            }
            //camera feed off
        } else {
            for (int iterator164 = 0; iterator164 < SecurityCam::getListSize(); iterator164++) {
                sc = SecurityCam::getObject(iterator164);

                if (!sc->specialCam) {
                    if (sc->coffinEffect!=1) {
                        sc->coffinEffect = 0;
                    }
                    if (sc->room == e->room) {
                        sc->screen = false;
                    }
                }
            }
        }

        e->eventState = TimeInPosMilliSecs()+3000;
    }

    if (mainPlayer->currRoom == e->room) {
        CoffinDistance = bbEntityDistance(mainPlayer->collider, e->room->objects[1]);
        if (CoffinDistance < 1.5) {
            if ((!Contained106) & e->name.equals("coffin106") & e->eventState2 == 0) {
                de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[1],true), -1531.0*RoomScale, bbEntityZ(e->room->objects[1],true), 90, bbRand(360), 0);
                de->size = 0.05;
                de->sizeChange = 0.001;
                bbEntityAlpha(de->obj, 0.8);
                UpdateDecals();

                if (Curr106->state > 0) {
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[1],true), -10240*RoomScale, bbEntityZ(e->room->objects[1],true));
                    Curr106->state = -0.1;
                    bbShowEntity(Curr106->obj);
                    e->eventState2 = 1;
                }
            }
        }

        //TODO: cleanup
        if (IsPlayerWearingItem(mainPlayer,"nvgoggles")) {
            hasBatteryFor895 = 0;
            for (i = 0; i <= mainPlayer->inventory->size - 1; i++) {
                if (mainPlayer->inventory->items[i] != nullptr) {
                    if (mainPlayer->inventory->items[i]->itemTemplate->name.equals("nvgoggles") | mainPlayer->inventory->items[i]->itemTemplate->name.equals("supernv")) {
                        if (mainPlayer->inventory->items[i]->state > 0.0) {
                            hasBatteryFor895 = 1;
                            break;
                        }
                    }
                }
            }
            //If (EntityVisible(mainPlayer\cam,e\room\levers[0]\baseObj)) Then
            //If (EntityInView(e\room\levers[0]\baseObj, mainPlayer\cam)) Then
            //If EntityVisible(mainPlayer\cam,e\room\objects[1])
            if (CoffinDistance < 4.0) & (hasBatteryFor895) {

                ///WearingNightVision)
                mainPlayer->sanity895 = mainPlayer->sanity895-(timing->tickDuration*1.1);
                mainPlayer->blurTimer = bbSin(TimeInPosMilliSecs()/10)*abs(mainPlayer->sanity895);

                tempF = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(e->room->objects[1],true),bbEntityZ(e->room->objects[1],true));
                tempF2 = bbEntityYaw(mainPlayer->collider);
                tempF3 = angleDist(tempF+90+bbSin(WrapAngle(e->eventState3/10)),tempF2);

                bbTurnEntity(mainPlayer->collider, 0,tempF3/4,0,true);

                tempF = abs(Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(e->room->objects[1],true),bbEntityZ(e->room->objects[1],true)));
                tempF2 = -60.0 * Min(Max((2.0-tempF)/2.0,0.0),1.0);

                mainPlayer->headPitch = (mainPlayer->headPitch * 0.8)+(tempF2 * 0.2);

                //TODO: fix
                //If (Rand((int)(Max(tempF*100.0,1.0)))=1) And (e\eventState3<0.0) Then
                //	EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], GorePics(Rand(0, 5)))
                //	;PlaySound2(HorrorSFX(1))
                //	e\eventState3 = 10.0
                //	EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,255,255)
                //EndIf
                if (mainPlayer->sanity895 < (-1000)) {
                    if (IsPlayerWearingItem(mainPlayer,"supernv")) {
                        DeathMSG = "\"Class D viewed SCP-895 through a pair of digital night vision goggles, presumably enhanced by SCP-914. It might be possible that the subject";
                        DeathMSG = DeathMSG + "was able to resist the memetic effects partially through these goggles. The goggles have been stored for further study.\"";
                    } else {
                        DeathMSG = "\"Class D viewed SCP-895 through a pair of digital night vision goggles, killing him.\"";
                    }
                    Kill(mainPlayer);
                }
            }
            //EndIf
        }

        if (e->eventState3>0.0) {
            e->eventState3 = Max(e->eventState3-timing->tickDuration,0.0);
        }
        if (e->eventState3==0.0) {
            e->eventState3 = -1.0;
            //TODO: fix
            //EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], NVTexture)
            if (IsPlayerWearingItem(mainPlayer,"nvgoggles")) {
                bbEntityColor(mainPlayer->overlays[OVERLAY_NIGHTVISION], 0,255,0);
            } else if ((IsPlayerWearingItem(mainPlayer,"supernv"))) {
                bbEntityColor(mainPlayer->overlays[OVERLAY_NIGHTVISION], 0,100,255);
            }
        }

        //TODO
        //ShouldPlay = 66

        if (e->room->levers[0]->succ) {
            for (int iterator165 = 0; iterator165 < SecurityCam::getListSize(); iterator165++) {
                sc = SecurityCam::getObject(iterator165);

                if (!sc->specialCam) {
                    if (sc->coffinEffect==0 & sc->room->roomTemplate->name!="room106") {
                        sc->coffinEffect = 2;
                    }
                    if (sc->coffinEffect == 1) {
                        bbEntityBlend(sc->scrOverlay, 3);
                    }
                    if (sc->room == e->room) {
                        sc->screen = true;
                    }
                }
            }
        } else {
            for (int iterator166 = 0; iterator166 < SecurityCam::getListSize(); iterator166++) {
                sc = SecurityCam::getObject(iterator166);

                if (!sc->specialCam) {
                    if (sc->coffinEffect != 1) {
                        sc->coffinEffect = 0;
                    }
                    if (sc->coffinEffect == 1) {
                        bbEntityBlend(sc->scrOverlay, 0);
                    }
                    if (sc->room == e->room) {
                        sc->screen = false;
                    }
                }
            }
        }
    } else {
        CoffinDistance = e->room->dist;
    }
    //[End Block]
}

}

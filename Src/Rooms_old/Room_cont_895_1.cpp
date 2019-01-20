#include <bbblitz3d.h>
#include <bbmath.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_cont_895_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_895_1(Room* r) {
    Door* d = CreateDoor(r->x, 0, r->z - 448.f * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;
    bbPositionEntity(d->buttons[0], r->x - 384.f * RoomScale, 0.7f, r->z - 280.f * RoomScale, true);

    SecurityCam* sc = CreateSecurityCam(r->x - 320.f * RoomScale, r->y + 704 * RoomScale, r->z + 288.f * RoomScale, r, true);
    sc->angle = 45 + 180;
    sc->turn = 45;
    sc->coffinEffect = true;
    bbTurnEntity(sc->cameraObj, 120, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    CoffinCam = sc;

    bbPositionEntity(sc->scrObj, r->x - 800 * RoomScale, 288.f * RoomScale, r->z - 340.f * RoomScale);
    bbEntityParent(sc->scrObj, r->obj);
    bbTurnEntity(sc->scrObj, 0, 180, 0);

    r->levers[0] = CreateLever();

    bbScaleEntity(r->levers[0]->baseObj, 0.04f, 0.04f, 0.04f);
    bbScaleEntity(r->levers[0]->obj, 0.04f, 0.04f, 0.04f);
    bbPositionEntity(r->levers[0]->baseObj, r->x - 800.f * RoomScale, r->y + 180.f * RoomScale, r->z - 336 * RoomScale, true);
    bbPositionEntity(r->levers[0]->obj, r->x - 800.f * RoomScale, r->y + 180.f * RoomScale, r->z - 336 * RoomScale, true);

    bbEntityParent(r->levers[0]->baseObj, r->obj);
    bbEntityParent(r->levers[0]->obj, r->obj);

    bbRotateEntity(r->levers[0]->baseObj, 0, 180, 0);
    bbRotateEntity(r->levers[0]->obj, 10, 0, 0);

    bbEntityPickMode(r->levers[0]->obj, 1, false);
    bbEntityRadius(r->levers[0]->obj, 0.1f);

    r->objects[0] = bbCreatePivot();
    bbPositionEntity(r->objects[0], r->x, -1320.f * RoomScale, r->z + 2304.f * RoomScale);
    bbEntityParent(r->objects[0], r->obj);

    Item* it = CreatePaper("doc895", r->x - 688.f * RoomScale, r->y + 133.f * RoomScale, r->z - 304.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("nvgoggles", r->x + 280.f * RoomScale, r->y -1456.f * RoomScale, r->z + 2164.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 96.f*RoomScale, -1532.f * RoomScale, r->z + 2016.f * RoomScale,true);

    //de.Decal = CreateDecal(DECAL_CORROSION, r\x + 96.f*RoomScale, -1535.f * RoomScale, r\z + 32.f * RoomScale, 90, Rand(360), 0)
    //EntityParent(de\obj, r\obj)
}

void UpdateEventCoffin106(Event* e) {
    // TODO:
}

void UpdateEventCoffin(Event* e) {
    if (e->eventState < TimeInPosMilliSecs()) {
        //SCP-079 starts broadcasting 895 camera feed on monitors after leaving the first zone
        //TODO: rewrite this to adjust for separate zone loading
        //camera feed on
        if (bbEntityPitch(e->room->levers[0]->obj, true) > 0) {
            for (int iterator163 = 0; iterator163 < SecurityCam::getListSize(); iterator163++) {
                SecurityCam* sc = SecurityCam::getObject(iterator163);

                if (!sc->specialCam) {
                    if (!sc->coffinEffect==0 && sc->room->roomTemplate->name.equals("room106") && !sc->room->roomTemplate->name.equals("room205")) {
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
                SecurityCam* sc = SecurityCam::getObject(iterator164);

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
        if (CoffinDistance < 1.5f) {
            if ((!Contained106) && e->name.equals("coffin106") && e->eventState2 == 0) {
                Decal* de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[1],true), -1531.f*RoomScale, bbEntityZ(e->room->objects[1],true), 90, bbRand(360), 0);
                de->size = 0.05f;
                de->sizeChange = 0.001f;
                bbEntityAlpha(de->obj, 0.8f);
                UpdateDecals();

                if (Curr106->state > 0) {
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[1],true), -10240*RoomScale, bbEntityZ(e->room->objects[1],true));
                    Curr106->state = -0.1f;
                    bbShowEntity(Curr106->obj);
                    e->eventState2 = 1;
                }
            }
        }

        //TODO: cleanup
        if (IsPlayerWearingItem(mainPlayer,"nvgoggles")) {
            bool hasBatteryFor895 = false;
            for (int i = 0; i < mainPlayer->inventory->size; i++) {
                if (mainPlayer->inventory->items[i] != nullptr) {
                    if (mainPlayer->inventory->items[i]->itemTemplate->name.equals("nvgoggles") || mainPlayer->inventory->items[i]->itemTemplate->name.equals("supernv")) {
                        if (mainPlayer->inventory->items[i]->state > 0.f) {
                            hasBatteryFor895 = 1;
                            break;
                        }
                    }
                }
            }
            //If (EntityVisible(mainPlayer\cam,e\room\levers[0]\baseObj)) Then
            //If (EntityInView(e\room\levers[0]\baseObj, mainPlayer\cam)) Then
            //If EntityVisible(mainPlayer\cam,e\room\objects[1])
            if (CoffinDistance < 4.f && hasBatteryFor895) {

                ///WearingNightVision)
                mainPlayer->sanity895 = mainPlayer->sanity895-(timing->tickDuration*1.1f);
                mainPlayer->blurTimer = bbSin(TimeInPosMilliSecs()/10)*abs(mainPlayer->sanity895);

                float tempF = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(e->room->objects[1],true),bbEntityZ(e->room->objects[1],true));
                float tempF2 = bbEntityYaw(mainPlayer->collider);
                float tempF3 = angleDist(tempF+90+bbSin(WrapAngle(e->eventState3/10)),tempF2);

                bbTurnEntity(mainPlayer->collider, 0,tempF3/4,0,true);

                tempF = abs(Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(e->room->objects[1],true),bbEntityZ(e->room->objects[1],true)));
                tempF2 = -60.f * Min(Max((2.f-tempF)/2.f,0.f),1.f);

                mainPlayer->headPitch = (mainPlayer->headPitch * 0.8f)+(tempF2 * 0.2f);

                //TODO: fix
                //If (Rand((int)(Max(tempF*100.f,1.f)))=1) And (e\eventState3<0.f) Then
                //	EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], GorePics(Rand(0, 5)))
                //	;PlaySound2(HorrorSFX(1))
                //	e\eventState3 = 10.f
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

        if (e->eventState3>0.f) {
            e->eventState3 = Max(e->eventState3-timing->tickDuration,0.f);
        }
        if (e->eventState3==0.f) {
            e->eventState3 = -1.f;
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
                SecurityCam* sc = SecurityCam::getObject(iterator165);

                if (!sc->specialCam) {
                    if (sc->coffinEffect==0 && !sc->room->roomTemplate->name.equals("room106")) {
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
                SecurityCam* sc = SecurityCam::getObject(iterator166);

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

}

}

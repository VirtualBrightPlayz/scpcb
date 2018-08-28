#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Item.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Objects.h"
#include "Room_cont_106_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_106_1(Room* r) {
    Item* it = CreatePaper("106_treats", r->x - 416.f * RoomScale, r->y - 576 * RoomScale, r->z + 2492.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("docRecall", r->x + 268.f * RoomScale, r->y - 576 * RoomScale, r->z + 2593.f * RoomScale);
    bbEntityParent(it->collider, r->obj);

    Door* d = CreateDoor(r->x - 968.f * RoomScale, -764.f * RoomScale, r->z + 1392.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x, 0, r->z - 464.f * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x - 624.f * RoomScale, -1280.f * RoomScale, r->z, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    r->objects[6] = bbLoadMesh("GFX/Map/room1062.b3d");

    bbScaleEntity(r->objects[6],RoomScale,RoomScale,RoomScale);
    bbEntityType(r->objects[6], HIT_MAP);
    bbEntityPickMode(r->objects[6], 3);
    bbPositionEntity(r->objects[6],r->x+784.f*RoomScale,-980.f*RoomScale,r->z+720.f*RoomScale,true);

    //If (BumpEnabled) Then
    //
    //	For i = 1 To CountSurfaces(r\objects[6])
    //		sf = GetSurface(r\objects[6],i)
    //		b = GetSurfaceBrush( sf )
    //		t = GetBrushTexture(b,1)
    //		texname$ =  StripPath(TextureName(t))
    //
    //		mat.Material=GetCache(texname)
    //		If (mat<>Null) Then
    //			If (mat\bump<>0) Then
    //				t1 = GetBrushTexture(b,0)
    //
    //				BrushTexture(b, t1, 0, 0)
    //				BrushTexture(b, mat\bump, 0, 1)
    //				BrushTexture(b, t, 0, 2)
    //
    //				PaintSurface(sf,b)
    //
    //				If (t1<>0) Then FreeTexture(t1
    //t1 = 0);
    //			EndIf
    //		EndIf
    //
    //		If (t<>0) Then FreeTexture(t
    //t = 0);
    //		If (b<>0) Then FreeBrush(b
    //b = 0);
    //	Next
    //
    //EndIf

    bbEntityParent(r->objects[6], r->obj);

    for (int n = 0; n < 2; n++) {
        r->levers[n] = CreateLever();

        bbScaleEntity(r->levers[n]->baseObj, 0.04f, 0.04f, 0.04f);
        bbScaleEntity(r->levers[n]->obj, 0.04f, 0.04f, 0.04f);
        bbPositionEntity(r->levers[n]->baseObj, r->x - (555.f - 81.f * (n)) * RoomScale, r->y - 576.f * RoomScale, r->z + 3040.f * RoomScale, true);
        bbPositionEntity(r->levers[n]->obj, r->x - (555.f - 81.f * (n)) * RoomScale, r->y - 576.f * RoomScale, r->z + 3040.f * RoomScale, true);

        bbEntityParent(r->levers[n]->baseObj, r->obj);
        bbEntityParent(r->levers[n]->obj, r->obj);

        bbRotateEntity(r->levers[n]->baseObj, 0, 0, 0);
        bbRotateEntity(r->levers[n]->obj, 10, -180, 0);

        //EntityPickMode(r\objects[n * 2 + 1], 2)
        bbEntityPickMode(r->levers[n]->obj, 1, false);
        bbEntityRadius(r->levers[n]->obj, 0.1f);
        //makecollbox(r\objects[n * 2 + 1])
    }

    bbRotateEntity(r->levers[0]->obj, 81,-180,0);
    bbRotateEntity(r->levers[1]->obj, -81,-180,0);

    r->objects[4] = CreateButton(r->x - 146.f*RoomScale, r->y - 576.f * RoomScale, r->z + 3045.f * RoomScale, 0,0,0);
    bbEntityParent(r->objects[4],r->obj);

    SecurityCam* sc = CreateSecurityCam(r->x + 768.f * RoomScale, r->y + 1392.f * RoomScale, r->z + 1696.f * RoomScale, r, true);
    sc->angle = 45 + 90 + 180;
    sc->turn = 20;
    bbTurnEntity(sc->cameraObj, 45, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    r->objects[7] = sc->cameraObj;
    r->objects[8] = sc->obj;

    bbPositionEntity(sc->scrObj, r->x - 272.f * RoomScale, -544.f * RoomScale, r->z + 3020.f * RoomScale);
    bbTurnEntity(sc->scrObj, 0, -10, 0);
    bbEntityParent(sc->scrObj, r->obj);
    sc->coffinEffect = 0;

    //r\npc[0] = CreateNPC(NPCtypeD, r\x + 1088.f * RoomScale, 1096.f * RoomScale, r\z + 1728.f * RoomScale)
    r->objects[5] = bbCreatePivot();
    bbTurnEntity(r->objects[5], 0,180,0);
    bbPositionEntity(r->objects[5], r->x + 1088.f * RoomScale, 1104.f * RoomScale, r->z + 1888.f * RoomScale);
    bbEntityParent(r->objects[5], r->obj);
    //HideEntity(r\npc[0]\obj)

    r->objects[9] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[9], r->x - 272 * RoomScale, r->y - 672.f * RoomScale, r->z + 2736.f * RoomScale, true);

    sc = CreateSecurityCam(r->x-1216.f*RoomScale, r->y-336.f*RoomScale, r->z+1468.f*RoomScale, r, true);
    sc->angle = 315;
    sc->turn = 45;
    sc->room = r;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    bbEntityParent(sc->obj, r->obj);
    sc->id = 4;
}

void UpdateEvent_cont_106_1(Event* e) {
    //eventstate2 = are the magnets on

    if (SoundTransmission) {
        if (e->eventState == 1) {
            e->eventState3 = Min(e->eventState3+timing->tickDuration,4000);
        }
        if (!bbChannelPlaying(e->soundChannels[0])) {
            e->soundChannels[0] = bbPlaySound(RadioStatic);
        }
    }

    //add the lure subject
    if (e->room->npc[0]==nullptr) {
        bbTFormPoint(1088, 1096, 1728, e->room->obj, 0);
        e->room->npc[0] = CreateNPC(NPCtypeD, bbTFormedX(), bbTFormedY(), bbTFormedZ());
        bbTurnEntity(e->room->npc[0]->collider,0,e->room->angle+90,0,true);
    }

    if (mainPlayer->currRoom == e->room && e->room->npc[0]!=nullptr) {
        if (!e->loaded) {
            e->sounds[2] = bbLoadSound("SFX/Room/106Chamber/MagnetUp.ogg");
            e->sounds[3] = bbLoadSound("SFX/Room/106Chamber/MagnetDown.ogg");
            e->sounds[4] = bbLoadSound("SFX/Room/106Chamber/FemurBreaker.ogg");

            e->loaded = true;
        }

        //ShowEntity(e\room\npc[0]\obj)

        e->room->npc[0]->state = 6;
        if (e->room->npc[0]->idle == 0) {
            AnimateNPC(e->room->npc[0], 17.f, 19.f, 0.01f, false);
            if (e->room->npc[0]->frame == 19.f) {
                e->room->npc[0]->idle = 1;
            }
        } else {
            AnimateNPC(e->room->npc[0], 19.f, 17.f, -0.01f, false);
            if (e->room->npc[0]->frame == 17.f) {
                e->room->npc[0]->idle = 0;
            }
        }

        bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[5],true),bbEntityY(e->room->objects[5],true)+0.1f,bbEntityZ(e->room->objects[5],true),true);
        bbRotateEntity(e->room->npc[0]->collider,bbEntityPitch(e->room->objects[5],true),bbEntityYaw(e->room->objects[5],true),0,true);
        bbResetEntity(e->room->npc[0]->collider);

        int temp = (int)(e->eventState2);

        if ((bbEntityY(e->room->objects[6],true)<-990*RoomScale) && (bbEntityY(e->room->objects[6],true)>-1275.f*RoomScale)) {
            e->room->levers[0]->locked = true;
        }

        bool leverstate = e->room->levers[0]->succ;
        if (mainPlayer->grabbedEntity == e->room->objects[1] && DrawHandIcon == true) {
            e->eventState2 = leverstate;
        }

        if (e->eventState2 != temp) {
            if (e->eventState2 == false) {
                PlaySound2(e->sounds[3]);
            } else {
                PlaySound2(e->sounds[2]);
            }
        }

        if (e->eventState3>3200 || e->eventState3<2500 || e->eventState!=1) {
            SoundTransmission = e->room->levers[1]->succ;
        }
        if (!SoundTransmission) {
            if (bbChannelPlaying(e->soundChannels[1])) {
                bbStopChannel(e->soundChannels[1]);
            }

            if (bbChannelPlaying(e->soundChannels[0])) {
                bbStopChannel(e->soundChannels[0]);
            }
        }

        if (e->eventState == 0) {
            if (SoundTransmission && bbRand(100)==1) {
                if (e->soundChannels[1] == 0) {
                    LoadEventSound(e,"SFX/Character/LureSubject/Idle"+String(bbRand(1,6))+".ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                }
                if (bbChannelPlaying(e->soundChannels[1]) == false) {
                    LoadEventSound(e,"SFX/Character/LureSubject/Idle"+String(bbRand(1,6))+".ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                }
            }

            UpdateButton((MeshModel*)e->room->objects[4]);
            if (mainPlayer->closestButton == e->room->objects[4] && MouseHit1) {
                //start the femur breaker
                e->eventState = 1;
                //only play sounds if transmission is on
                if (SoundTransmission == true) {
                    if (bbChannelPlaying(e->soundChannels[1])) {
                        bbStopChannel(e->soundChannels[1]);
                    }

                    e->soundChannels[1] = bbPlaySound(e->sounds[4]);
                }
            }
            //nut BUSTED
        } else if ((e->eventState == 1)) {
            if (SoundTransmission && e->eventState3 < 2000) {
                if (e->soundChannels[1] == 0) {
                    LoadEventSound(e,"SFX/Character/LureSubject/Sniffling.ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                }
                if (bbChannelPlaying(e->soundChannels[1]) == false) {
                    LoadEventSound(e,"SFX/Character/LureSubject/Sniffling.ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                }
            }

            if (e->eventState3 >= 2500) {

                if (e->eventState2 == 1 && e->eventState3-timing->tickDuration < 2500) {
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[6], true), bbEntityY(e->room->objects[6], true), bbEntityZ(e->room->objects[6], true));
                    Contained106 = false;
                    bbShowEntity(Curr106->obj);
                    Curr106->idle = false;
                    Curr106->state = -11;
                    e->eventState = 2;
                    return;
                }

                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[5], true), (700.f + 108.f*(Min(e->eventState3-2500.f,800)/320.f))*RoomScale , bbEntityZ(e->room->objects[5], true));
                bbHideEntity(Curr106->obj2);

                //PointEntity(Curr106\collider, mainPlayer\cam)
                bbRotateEntity(Curr106->collider,0, bbEntityYaw(e->room->objects[5],true)+180.f, 0, true);
                Curr106->state = -11;
                AnimateNPC(Curr106, 206, 250, 0.1f);
                Curr106->idle = true;

                if (e->eventState3-timing->tickDuration < 2500) {
                    Decal* d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[5], true), 936.f*RoomScale, bbEntityZ(e->room->objects[5], true), 90, 0, bbRnd(360));
                    d->timer = 90000;
                    d->alpha = 0.01f;
                    d->alphaChange = 0.005f;
                    d->size = 0.1f;
                    d->sizeChange = 0.003f;

                    if (bbChannelPlaying(e->soundChannels[1])) {
                        bbStopChannel(e->soundChannels[1]);
                    }

                    LoadEventSound(e,"SFX/Character/LureSubject/106Bait.ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                } else if ((e->eventState3-timing->tickDuration < 2900 && e->eventState3 >= 2900)) {
                    Decal* d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[7], true), bbEntityY(e->room->objects[7], true) , bbEntityZ(e->room->objects[7], true), 0, 0, 0);
                    bbRotateEntity(d->obj, bbEntityPitch(e->room->objects[7], true)+bbRand(10,20), bbEntityYaw(e->room->objects[7], true)+30, bbEntityRoll(d->obj));
                    bbMoveEntity(d->obj, 0,0,0.15f);
                    bbRotateEntity(d->obj, bbEntityPitch(e->room->objects[7], true), bbEntityYaw(e->room->objects[7], true), bbEntityRoll(d->obj));

                    bbEntityParent(d->obj, e->room->objects[7]);
                    //TurnEntity(d\obj, 0, 180, 0)

                    d->timer = 90000;
                    d->alpha = 0.01f;
                    d->alphaChange = 0.005f;
                    d->size = 0.05f;
                    d->sizeChange = 0.002f;
                } else if ((e->eventState3 > 3200)) {
                    bbPositionEntity(e->room->objects[8], 0, 1000.f, 0, true);
                    bbPositionEntity(e->room->objects[7], 0, 1000.f, 0, true);

                    //magnets off -> 106 caught
                    if (e->eventState2 == true) {
                        Contained106 = true;
                        //magnets off -> 106 comes out and attacks
                    } else {
                        bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[6], true), bbEntityY(e->room->objects[6], true), bbEntityZ(e->room->objects[6], true));

                        Contained106 = false;
                        bbShowEntity(Curr106->obj);
                        Curr106->idle = false;
                        Curr106->state = -11;

                        e->eventState = 2;
                        return;
                    }
                }

            }

        }

        if ((int)(e->eventState2)) {
            bbPositionEntity(e->room->objects[6],bbEntityX(e->room->objects[6],true),CurveValue(-980.f*RoomScale + bbSin((float)(TimeInPosMilliSecs())*0.04f)*0.07f,bbEntityY(e->room->objects[6],true),200.f),bbEntityZ(e->room->objects[6],true),true);
            bbRotateEntity(e->room->objects[6], bbSin((float)(TimeInPosMilliSecs())*0.03f), bbEntityYaw(e->room->objects[6],true), -bbSin((float)(TimeInPosMilliSecs())*0.025f), true);
        } else {
            bbPositionEntity(e->room->objects[6],bbEntityX(e->room->objects[6],true),CurveValue(-1280.f*RoomScale,bbEntityY(e->room->objects[6],true),200.f),bbEntityZ(e->room->objects[6],true),true);
            bbRotateEntity(e->room->objects[6], 0, bbEntityYaw(e->room->objects[6],true), 0, true);
        }
    }

}

}

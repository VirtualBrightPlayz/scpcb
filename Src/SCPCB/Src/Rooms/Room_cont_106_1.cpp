#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
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

    it = CreatePaper("106_treats", r->x - 416.0 * RoomScale, r->y - 576 * RoomScale, r->z + 2492.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreatePaper("docRecall", r->x + 268.0 * RoomScale, r->y - 576 * RoomScale, r->z + 2593.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    d = CreateDoor(r->x - 968.0 * RoomScale, -764.0 * RoomScale, r->z + 1392.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x, 0, r->z - 464.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    d = CreateDoor(r->x - 624.0 * RoomScale, -1280.0 * RoomScale, r->z, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    d->autoClose = false;
    d->open = false;

    r->objects[6] = bbLoadMesh("GFX/Map/room1062.b3d");

    bbScaleEntity(r->objects[6],RoomScale,RoomScale,RoomScale);
    bbEntityType(r->objects[6], HIT_MAP);
    bbEntityPickMode(r->objects[6], 3);
    bbPositionEntity(r->objects[6],r->x+784.0*RoomScale,-980.0*RoomScale,r->z+720.0*RoomScale,true);

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

    int n;
    for (n = 0; n <= 1; n++) {
        r->levers[n] = CreateLever();

        bbScaleEntity(r->levers[n]->baseObj, 0.04, 0.04, 0.04);
        bbScaleEntity(r->levers[n]->obj, 0.04, 0.04, 0.04);
        bbPositionEntity(r->levers[n]->baseObj, r->x - (555.0 - 81.0 * (n)) * RoomScale, r->y - 576.0 * RoomScale, r->z + 3040.0 * RoomScale, true);
        bbPositionEntity(r->levers[n]->obj, r->x - (555.0 - 81.0 * (n)) * RoomScale, r->y - 576.0 * RoomScale, r->z + 3040.0 * RoomScale, true);

        bbEntityParent(r->levers[n]->baseObj, r->obj);
        bbEntityParent(r->levers[n]->obj, r->obj);

        bbRotateEntity(r->levers[n]->baseObj, 0, 0, 0);
        bbRotateEntity(r->levers[n]->obj, 10, -180, 0);

        //EntityPickMode(r\objects[n * 2 + 1], 2)
        bbEntityPickMode(r->levers[n]->obj, 1, false);
        bbEntityRadius(r->levers[n]->obj, 0.1);
        //makecollbox(r\objects[n * 2 + 1])
    }

    bbRotateEntity(r->levers[0]->obj, 81,-180,0);
    bbRotateEntity(r->levers[1]->obj, -81,-180,0);

    r->objects[4] = CreateButton(r->x - 146.0*RoomScale, r->y - 576.0 * RoomScale, r->z + 3045.0 * RoomScale, 0,0,0);
    bbEntityParent(r->objects[4],r->obj);

    sc = CreateSecurityCam(r->x + 768.0 * RoomScale, r->y + 1392.0 * RoomScale, r->z + 1696.0 * RoomScale, r, true);
    sc->angle = 45 + 90 + 180;
    sc->turn = 20;
    bbTurnEntity(sc->cameraObj, 45, 0, 0);
    bbEntityParent(sc->obj, r->obj);

    r->objects[7] = sc->cameraObj;
    r->objects[8] = sc->obj;

    bbPositionEntity(sc->scrObj, r->x - 272.0 * RoomScale, -544.0 * RoomScale, r->z + 3020.0 * RoomScale);
    bbTurnEntity(sc->scrObj, 0, -10, 0);
    bbEntityParent(sc->scrObj, r->obj);
    sc->coffinEffect = 0;

    //r\npc[0] = CreateNPC(NPCtypeD, r\x + 1088.0 * RoomScale, 1096.0 * RoomScale, r\z + 1728.0 * RoomScale)
    r->objects[5] = bbCreatePivot();
    bbTurnEntity(r->objects[5], 0,180,0);
    bbPositionEntity(r->objects[5], r->x + 1088.0 * RoomScale, 1104.0 * RoomScale, r->z + 1888.0 * RoomScale);
    bbEntityParent(r->objects[5], r->obj);
    //HideEntity(r\npc[0]\obj)

    r->objects[9] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[9], r->x - 272 * RoomScale, r->y - 672.0 * RoomScale, r->z + 2736.0 * RoomScale, true);

    sc = CreateSecurityCam(r->x-1216.0*RoomScale, r->y-336.0*RoomScale, r->z+1468.0*RoomScale, r, true);
    sc->angle = 315;
    sc->turn = 45;
    sc->room = r;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    bbEntityParent(sc->obj, r->obj);
    sc->id = 4;
}

void UpdateEvent_cont_106_1(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;
    int leverstate;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;
    Decal* d;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]

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

    if (mainPlayer->currRoom == e->room & e->room->npc[0]!=nullptr) {
        if (!e->loaded) {
            e->sounds[2] = bbLoadSound("SFX/Room/106Chamber/MagnetUp.ogg");
            e->sounds[3] = bbLoadSound("SFX/Room/106Chamber/MagnetDown.ogg");
            e->sounds[4] = bbLoadSound("SFX/Room/106Chamber/FemurBreaker.ogg");

            e->loaded = true;
        }

        //ShowEntity(e\room\npc[0]\obj)

        e->room->npc[0]->state = 6;
        if (e->room->npc[0]->idle == 0) {
            AnimateNPC(e->room->npc[0], 17.0, 19.0, 0.01, false);
            if (e->room->npc[0]->frame == 19.0) {
                e->room->npc[0]->idle = 1;
            }
        } else {
            AnimateNPC(e->room->npc[0], 19.0, 17.0, -0.01, false);
            if (e->room->npc[0]->frame == 17.0) {
                e->room->npc[0]->idle = 0;
            }
        }

        bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[5],true),bbEntityY(e->room->objects[5],true)+0.1,bbEntityZ(e->room->objects[5],true),true);
        bbRotateEntity(e->room->npc[0]->collider,bbEntityPitch(e->room->objects[5],true),bbEntityYaw(e->room->objects[5],true),0,true);
        bbResetEntity(e->room->npc[0]->collider);

        temp = (int)(e->eventState2);

        if ((bbEntityY(e->room->objects[6],true)<-990*RoomScale) & (bbEntityY(e->room->objects[6],true)>-1275.0*RoomScale)) {
            e->room->levers[0]->locked = true;
        }

        leverstate = e->room->levers[0]->succ;
        if (mainPlayer->grabbedEntity == e->room->objects[1] & DrawHandIcon == true) {
            e->eventState2 = leverstate;
        }

        if (e->eventState2 != temp) {
            if (e->eventState2 == false) {
                PlaySound2(e->sounds[3]);
            } else {
                PlaySound2(e->sounds[2]);
            }
        }

        if (e->eventState3>3200 | e->eventState3<2500 | e->eventState!=1) {
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
            if (SoundTransmission & bbRand(100)==1) {
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
            if (mainPlayer->closestButton == e->room->objects[4] & MouseHit1) {
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
            if (SoundTransmission & e->eventState3 < 2000) {
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

                if (e->eventState2 == 1 & e->eventState3-timing->tickDuration < 2500) {
                    bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[6], true), bbEntityY(e->room->objects[6], true), bbEntityZ(e->room->objects[6], true));
                    Contained106 = false;
                    bbShowEntity(Curr106->obj);
                    Curr106->idle = false;
                    Curr106->state = -11;
                    e->eventState = 2;
                    return;
                }

                bbPositionEntity(Curr106->collider, bbEntityX(e->room->objects[5], true), (700.0 + 108.0*(Min(e->eventState3-2500.0,800)/320.0))*RoomScale , bbEntityZ(e->room->objects[5], true));
                bbHideEntity(Curr106->obj2);

                //PointEntity(Curr106\collider, mainPlayer\cam)
                bbRotateEntity(Curr106->collider,0, bbEntityYaw(e->room->objects[5],true)+180.0, 0, true);
                Curr106->state = -11;
                AnimateNPC(Curr106, 206, 250, 0.1);
                Curr106->idle = true;

                if (e->eventState3-timing->tickDuration < 2500) {
                    d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[5], true), 936.0*RoomScale, bbEntityZ(e->room->objects[5], true), 90, 0, bbRnd(360));
                    d->timer = 90000;
                    d->alpha = 0.01;
                    d->alphaChange = 0.005;
                    d->size = 0.1;
                    d->sizeChange = 0.003;

                    if (bbChannelPlaying(e->soundChannels[1])) {
                        bbStopChannel(e->soundChannels[1]);
                    }

                    LoadEventSound(e,"SFX/Character/LureSubject/106Bait.ogg",1);
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                } else if ((e->eventState3-timing->tickDuration < 2900 & e->eventState3 >= 2900)) {
                    d = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->objects[7], true), bbEntityY(e->room->objects[7], true) , bbEntityZ(e->room->objects[7], true), 0, 0, 0);
                    bbRotateEntity(d->obj, bbEntityPitch(e->room->objects[7], true)+bbRand(10,20), bbEntityYaw(e->room->objects[7], true)+30, bbEntityRoll(d->obj));
                    bbMoveEntity(d->obj, 0,0,0.15);
                    bbRotateEntity(d->obj, bbEntityPitch(e->room->objects[7], true), bbEntityYaw(e->room->objects[7], true), bbEntityRoll(d->obj));

                    bbEntityParent(d->obj, e->room->objects[7]);
                    //TurnEntity(d\obj, 0, 180, 0)

                    d->timer = 90000;
                    d->alpha = 0.01;
                    d->alphaChange = 0.005;
                    d->size = 0.05;
                    d->sizeChange = 0.002;
                } else if ((e->eventState3 > 3200)) {
                    bbPositionEntity(e->room->objects[8], 0, 1000.0, 0, true);
                    bbPositionEntity(e->room->objects[7], 0, 1000.0, 0, true);

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
            bbPositionEntity(e->room->objects[6],bbEntityX(e->room->objects[6],true),CurveValue(-980.0*RoomScale + bbSin((float)(TimeInPosMilliSecs())*0.04)*0.07,bbEntityY(e->room->objects[6],true),200.0),bbEntityZ(e->room->objects[6],true),true);
            bbRotateEntity(e->room->objects[6], bbSin((float)(TimeInPosMilliSecs())*0.03), bbEntityYaw(e->room->objects[6],true), -bbSin((float)(TimeInPosMilliSecs())*0.025), true);
        } else {
            bbPositionEntity(e->room->objects[6],bbEntityX(e->room->objects[6],true),CurveValue(-1280.0*RoomScale,bbEntityY(e->room->objects[6],true),200.0),bbEntityZ(e->room->objects[6],true),true);
            bbRotateEntity(e->room->objects[6], 0, bbEntityYaw(e->room->objects[6],true), 0, true);
        }
    }
    //[End Block]
}

}

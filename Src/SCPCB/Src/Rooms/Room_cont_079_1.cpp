#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Assets.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../MathUtils/MathUtils.h"
#include "Room_cont_079_1.h"

namespace CBN {

// Functions.
void FillRoom_cont_079_1(Room* r) {
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

    d = CreateDoor(r->x, -448.f*RoomScale, r->z + 1136.f * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(d->buttons[1], r->x + 224.f * RoomScale, -250*RoomScale, r->z + 918.f * RoomScale, true);
    //TurnEntity(d\buttons[0],0,-90,0,True)
    bbPositionEntity(d->buttons[0], r->x - 240.f * RoomScale, -250*RoomScale, r->z + 1366.f * RoomScale, true);
    //TurnEntity(d\buttons[1],0, 90,0,True)

    r->doors[0] = CreateDoor(r->x + 1456.f*RoomScale, -448.f*RoomScale, r->z + 976.f * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 1760.f * RoomScale, -250*RoomScale, r->z + 1236.f * RoomScale, true);
    bbTurnEntity(r->doors[0]->buttons[0],0,-90-90,0,true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 1760.f * RoomScale, -240*RoomScale, r->z + 740.f * RoomScale, true);
    bbTurnEntity(r->doors[0]->buttons[1],0, 90-90,0,true);

    CreateDoor(r->x + 1144.f*RoomScale, -448.f*RoomScale, r->z + 704.f * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    r->objects[0] = bbLoadAnimMesh("GFX/Map/079.b3d");
    bbScaleEntity(r->objects[0], 1.3, 1.3, 1.3, true);
    bbPositionEntity(r->objects[0], r->x + 1856.f*RoomScale, -560.f*RoomScale, r->z-672.f*RoomScale, true);
    bbEntityParent(r->objects[0], r->obj);
    bbTurnEntity(r->objects[0],0,180,0,true);

    TextureAssetWrap* aiPic = TextureAssetWrap::grab("GFX/079pics/face.jpg");
    r->objects[1] = bbCreateSprite(r->objects[0]);
    bbSpriteViewMode((Sprite*)r->objects[1],2);
    bbPositionEntity(r->objects[1], 0.082, 0.119, 0.010);
    bbScaleSprite((Sprite*)r->objects[1],0.18*0.5,0.145*0.5);
    bbTurnEntity((Sprite*)r->objects[1],0,13.f,0);
    bbMoveEntity(r->objects[1], 0,0,-0.022);
    bbEntityTexture((Sprite*)r->objects[1],aiPic->getTexture());
    aiPic->drop();

    bbHideEntity(r->objects[1]);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 1184.f*RoomScale, -448.f*RoomScale, r->z+1792.f*RoomScale, true);

    de = CreateDecal(DECAL_BLOOD_SPLATTER,  r->x + 1184.f*RoomScale, -448.f*RoomScale+0.01, r->z+1792.f*RoomScale,90,bbRnd(360),0);
    de->size = 0.5;
    bbScaleSprite(de->obj, de->size,de->size);
    bbEntityParent(de->obj, r->obj);
}

void UpdateEvent_cont_079_1(Event* e) {
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

    //[Block]
    if (mainPlayer->currRoom == e->room) {

        if (e->eventState == 0) {
            //Music(4) = LoadSound("SFX/Music/079.ogg") ;TODO: fix
            e->room->npc[0] = CreateNPC(NPCtypeGuard, bbEntityX(e->room->objects[2],true), bbEntityY(e->room->objects[2],true)+0.5, bbEntityZ(e->room->objects[2],true));
            bbPointEntity(e->room->npc[0]->collider, e->room->obj);
            bbRotateEntity(e->room->npc[0]->collider, 0, bbEntityYaw(e->room->npc[0]->collider),0, true);
            //SetAnimTime(e\room\npc[0]\obj, 906)
            SetNPCFrame(e->room->npc[0], 906);
            e->room->npc[0]->state = 8;

            e->eventState = 1;
        }

        //TODO
        //ShouldPlay = 4
        if (RemoteDoorOn) {
            if (e->room->doors[0]->open) {
                if (e->room->doors[0]->openstate > 50 | bbEntityDistance(mainPlayer->collider, e->room->doors[0]->frameobj)<0.5) {
                    e->room->doors[0]->openstate = Min(e->room->doors[0]->openstate,50);
                    e->room->doors[0]->open = false;
                    PlaySound2((LoadTempSound("SFX/Door/DoorError.ogg")));
                }
            }
        } else if ((e->eventState < 10000)) {
            if (e->eventState == 1) {
                e->eventState = 2;
            } else if ((e->eventState == 2)) {
                if (bbEntityDistance(e->room->objects[0], mainPlayer->collider)<3.f) {
                    e->eventState = 3;
                    e->eventState2 = 1;
                    //e\sounds[0] = LoadSound("SFX/SCP/079/Speech.ogg")
                    LoadEventSound(e,"SFX/SCP/079/Speech.ogg");
                    e->soundChannels[0] = bbPlaySound(e->sounds[0]);
                }
            } else if ((e->eventState == 3)) {
                if (e->eventState < 3500) {
                    if (bbChannelPlaying(e->soundChannels[0])) {
                        if (bbRand(3) == 1) {
                            // TODO: Make the texture members of the struct.
                            //EntityTexture(e\room\objects[1], OldAiPics(0))
                            bbShowEntity(e->room->objects[1]);
                        } else if ((bbRand(10) == 1)) {
                            bbHideEntity(e->room->objects[1]);
                        }
                    } else {
                        if (e->sounds[0] != 0) {
                            bbFreeSound(e->sounds[0]);
                            e->sounds[0] = 0;
                        }
                        //EntityTexture(e\room\objects[1], OldAiPics(1))
                        bbShowEntity(e->room->objects[1]);
                    }
                } else {
                    if (bbEntityDistance(e->room->objects[0], mainPlayer->collider)<2.5) {
                        e->eventState = 10001;
                        //e\sounds[0] = LoadSound("SFX/SCP/079/Refuse.ogg")
                        LoadEventSound(e,"SFX/SCP/079/Refuse.ogg");
                        e->soundChannels[0] = bbPlaySound(e->sounds[0]);
                        //EntityTexture(e\room\objects[1], OldAiPics(1))
                        bbShowEntity(e->room->objects[1]);
                    }
                }

            }

        }

    }


    if (e->eventState2 == 1) {
        if (RemoteDoorOn) {
            LoadEventSound(e,"SFX/SCP/079/GateB.ogg");
            e->soundChannels[0] = bbPlaySound(e->sounds[0]);
            e->eventState2 = 2;

            for (int iterator158 = 0; iterator158 < Event::getListSize(); iterator158++) {
                e2 = Event::getObject(iterator158);

                if (e2->name.equals("exit1") | e2->name.equals("gateaentrance")) {
                    e2->eventState3 = 1;
                }
            }
        }
    }

    //[End Block]
}

}

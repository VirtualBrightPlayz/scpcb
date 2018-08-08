#include "Room_cont_079_1.h"
#include "include.h"

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

    d = CreateDoor(r->x, -448.0*RoomScale, r->z + 1136.0 * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(d->buttons[1], r->x + 224.0 * RoomScale, -250*RoomScale, r->z + 918.0 * RoomScale, true);
    //TurnEntity(d\buttons[0],0,-90,0,True)
    bbPositionEntity(d->buttons[0], r->x - 240.0 * RoomScale, -250*RoomScale, r->z + 1366.0 * RoomScale, true);
    //TurnEntity(d\buttons[1],0, 90,0,True)

    r->doors[0] = CreateDoor(r->x + 1456.0*RoomScale, -448.0*RoomScale, r->z + 976.0 * RoomScale, 0, r, false, DOOR_TYPE_CONT, r->roomTemplate->name);
    bbPositionEntity(r->doors[0]->buttons[1], r->x + 1760.0 * RoomScale, -250*RoomScale, r->z + 1236.0 * RoomScale, true);
    bbTurnEntity(r->doors[0]->buttons[0],0,-90-90,0,true);
    bbPositionEntity(r->doors[0]->buttons[0], r->x + 1760.0 * RoomScale, -240*RoomScale, r->z + 740.0 * RoomScale, true);
    bbTurnEntity(r->doors[0]->buttons[1],0, 90-90,0,true);

    CreateDoor(r->x + 1144.0*RoomScale, -448.0*RoomScale, r->z + 704.0 * RoomScale, 90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    r->objects[0] = bbLoadAnimMesh("GFX/Map/079.b3d");
    bbScaleEntity(r->objects[0], 1.3, 1.3, 1.3, true);
    bbPositionEntity(r->objects[0], r->x + 1856.0*RoomScale, -560.0*RoomScale, r->z-672.0*RoomScale, true);
    bbEntityParent(r->objects[0], r->obj);
    bbTurnEntity(r->objects[0],0,180,0,true);

    int aiPic = GrabTexture("GFX/079pics/face.jpg");
    r->objects[1] = bbCreateSprite(r->objects[0]);
    bbSpriteViewMode(r->objects[1],2);
    bbPositionEntity(r->objects[1], 0.082, 0.119, 0.010);
    bbScaleSprite(r->objects[1],0.18*0.5,0.145*0.5);
    bbTurnEntity(r->objects[1],0,13.0,0);
    bbMoveEntity(r->objects[1], 0,0,-0.022);
    bbEntityTexture(r->objects[1],aiPic);
    aiPic->drop();

    bbHideEntity(r->objects[1]);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x + 1184.0*RoomScale, -448.0*RoomScale, r->z+1792.0*RoomScale, true);

    de = CreateDecal(DECAL_BLOOD_SPLATTER,  r->x + 1184.0*RoomScale, -448.0*RoomScale+0.01, r->z+1792.0*RoomScale,90,bbRnd(360),0);
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
                if (bbEntityDistance(e->room->objects[0], mainPlayer->collider)<3.0) {
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

                if (e2->name=="exit1" | e2->name=="gateaentrance") {
                    e2->eventState3 = 1;
                }
            }
        }
    }

    //[End Block]
}

}

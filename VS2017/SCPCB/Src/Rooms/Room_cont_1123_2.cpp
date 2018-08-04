#include "Room_cont_1123_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_cont_1123_2(Room* r) {
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

    it = CreatePaper("doc1123", r->x + 511.0 * RoomScale, r->y + 125.0 * RoomScale, r->z - 936.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    it = CreateItem("gasmask", r->x + 457.0 * RoomScale, r->y + 150.0 * RoomScale, r->z + 960.0 * RoomScale);
    bbEntityParent(it->collider, r->obj);

    d = CreateDoor(r->x + 832.0 * RoomScale, 0.0, r->z + 367.0 * RoomScale, 0, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    bbPositionEntity(d->buttons[0], r->x + 956.0 * RoomScale, bbEntityY(d->buttons[0],true), r->z + 352.0 * RoomScale, true);
    bbPositionEntity(d->buttons[1], r->x + 713.0 * RoomScale, bbEntityY(d->buttons[1],true), r->z + 384.0 * RoomScale, true);
    bbFreeEntity(d->obj2);
    d->obj2 = 0;
    d = CreateDoor(r->x + 280.0 * RoomScale, 0.0, r->z - 607.0 * RoomScale, 90, r, false);
    bbPositionEntity(d->buttons[0], bbEntityX(d->buttons[0],true), bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true), true);
    bbPositionEntity(d->buttons[1], bbEntityX(d->buttons[1],true), bbEntityY(d->buttons[1],true), bbEntityZ(d->buttons[1],true), true);

    d = CreateDoor(r->x + 280.0 * RoomScale, 512.0 * RoomScale, r->z - 607.0 * RoomScale, 90, r, false);
    bbPositionEntity(d->buttons[0], bbEntityX(d->buttons[0],true), bbEntityY(d->buttons[0],true), bbEntityZ(d->buttons[0],true), true);
    bbFreeEntity(d->buttons[1]);
    d->buttons[1] = 0;
    r->doors[0] = d;
    //PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x + 832.0 * RoomScale, r->y + 166.0 * RoomScale, r->z + 784.0 * RoomScale, true);
    r->objects[4] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[4], r->x -648.0 * RoomScale, r->y + 592.0 * RoomScale, r->z + 692.0 * RoomScale, true);
    r->objects[5] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[5], r->x + 828.0 * RoomScale, r->y + 592.0 * RoomScale, r->z + 592.0 * RoomScale, true);

    r->objects[6] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[6], r->x - 76.0 * RoomScale, r->y + 620.0 * RoomScale, r->z + 744.0 * RoomScale, true);
    r->objects[7] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[7], r->x - 640.0 * RoomScale, r->y + 620.0 * RoomScale, r->z - 864.0 * RoomScale, true);

    r->objects[8] = bbLoadMesh("GFX/Map/forest/door_frame.b3d");
    bbPositionEntity(r->objects[8], r->x - 272.0 * RoomScale, 512.0 * RoomScale, r->z + 288.0 * RoomScale,true);
    bbRotateEntity(r->objects[8],0,90,0,true);
    bbScaleEntity(r->objects[8],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,true);
    bbEntityParent(r->objects[8],r->obj);

    r->objects[9] = bbLoadMesh("GFX/Map/forest/door.b3d");
    bbPositionEntity(r->objects[9],r->x - 272.0 * RoomScale, 512.0 * RoomScale, r->z + (288.0-70) * RoomScale,true);
    bbRotateEntity(r->objects[9],0,10,0,true);
    bbEntityType(r->objects[9], HIT_MAP);
    bbScaleEntity(r->objects[9],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,true);
    bbEntityParent(r->objects[9],r->obj);

    r->objects[10] = bbCopyEntity(r->objects[8]);
    bbPositionEntity(r->objects[10], r->x - 272.0 * RoomScale, 512.0 * RoomScale, r->z + 736.0 * RoomScale,true);
    bbRotateEntity(r->objects[10],0,90,0,true);
    bbScaleEntity(r->objects[10],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,true);
    bbEntityParent(r->objects[10],r->obj);

    r->objects[11] = bbCopyEntity(r->objects[9]);
    bbPositionEntity(r->objects[11],r->x - 272.0 * RoomScale, 512.0 * RoomScale, r->z + (736.0-70) * RoomScale,true);
    bbRotateEntity(r->objects[11],0,90,0,true);
    bbEntityType(r->objects[11], HIT_MAP);
    bbScaleEntity(r->objects[11],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,true);
    bbEntityParent(r->objects[11],r->obj);

    r->objects[12] = bbCopyEntity(r->objects[8]);
    bbPositionEntity(r->objects[12], r->x - 592.0 * RoomScale, 512.0 * RoomScale, r->z - 704.0 * RoomScale,true);
    bbRotateEntity(r->objects[12],0,0,0,true);
    bbScaleEntity(r->objects[12],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,true);
    bbEntityParent(r->objects[12],r->obj);

    r->objects[13] = bbCopyEntity(r->objects[9]);
    bbPositionEntity(r->objects[13],r->x - (592.0+70.0) * RoomScale, 512.0 * RoomScale, r->z - 704.0 * RoomScale,true);
    bbRotateEntity(r->objects[13],0,0,0,true);
    bbEntityType(r->objects[13], HIT_MAP);
    bbScaleEntity(r->objects[13],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,true);
    bbEntityParent(r->objects[13],r->obj);

    //r\objects[14] = LoadMesh("GFX/Map/1123_hb.b3d",r\obj)
    //EntityPickMode(r\objects[14],2)
    //EntityType(r\objects[14],HIT_MAP)
    //EntityAlpha(r\objects[14],0.0)
}

void UpdateEvent_cont_1123_2(Event* e) {
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
    int nazi;
    float scale;

    //[Block]
    if (mainPlayer->currRoom == e->room) {
        //the event is started when the player picks up SCP-1123 (in Items.bb/UpdateItems())

        if (e->eventState == 1) {
            //If (e\soundChannels[0] = 0) Then
            //	e\soundChannels[0] =
            //Else
            //	If (Not IsChannelPlaying(e\soundChannels[0])) Then e\soundChannels[0] = PlaySound2(moddedambience)
            //EndIf

            //Saving injuries and bloodloss, so that the player won't be healed automatically
            PrevInjuries = mainPlayer->injuries;
            PrevBloodloss = mainPlayer->bloodloss;

            e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->objects[6],true),bbEntityY(e->room->objects[6],true),bbEntityZ(e->room->objects[6],true));
            //e\room\npc[1] = CreateNPC(NPCtypeD, EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True),EntityZ(e\room\objects[7],True))

            nazi = bbLoadAnimMesh("GFX/npcs/naziofficer.b3d");
            scale = 0.5 / bbMeshWidth(nazi);

            bbFreeEntity(e->room->npc[0]->obj);
            e->room->npc[0]->obj = bbCopyEntity(nazi);
            bbScaleEntity(e->room->npc[0]->obj, scale, scale, scale);

            //FreeEntity(e\room\npc[1]\obj)
            //e\room\npc[1]\obj = CopyEntity(nazi)
            //ScaleEntity(e\room\npc[1]\obj, scale, scale, scale)

            bbFreeEntity(nazi);

            bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[4],true),bbEntityY(e->room->objects[4],true),bbEntityZ(e->room->objects[4],true),true);
            bbResetEntity(mainPlayer->collider);
            //PlaySound2(HorrorSFX(9))
            mainPlayer->camShake = 1.0;
            mainPlayer->blurTimer = 1200;
            mainPlayer->injuries = 1.0;
            e->eventState = 2;

        } else if ((e->eventState == 2)) {
            e->eventState2 = e->eventState2 + timing->tickDuration;

            bbPointEntity(e->room->npc[0]->collider, mainPlayer->collider);
            mainPlayer->blurTimer = Max(mainPlayer->blurTimer, 100);

            if (e->eventState2>200 & e->eventState2-timing->tickDuration<=200) {
                //e\sounds[0] = LoadSound("SFX/Music/1123.ogg");TODO: fix
                e->soundChannels[0] = bbPlaySound(e->sounds[0]);
            }


            if (e->eventState2 > 1000) {
                if (e->sounds[1]==0) {
                    e->sounds[1] = bbLoadSound("SFX/Door/1123DoorOpen.ogg");
                    e->soundChannels[1] = bbPlaySound(e->sounds[1]);
                }
                bbRotateEntity(e->room->objects[11], 0, CurveAngle(10, bbEntityYaw(e->room->objects[11],0), 40), 0);

                if (e->eventState2>=1040 & e->eventState2-timing->tickDuration<1040) {
                    PlayRangedSound(LoadTempSound("SFX/SCP/1123/Officer1.ogg"), mainPlayer->cam, e->room->npc[0]->obj);
                } else if ((e->eventState2>=1400 & e->eventState2-timing->tickDuration<1400)) {
                    PlayRangedSound(LoadTempSound("SFX/SCP/1123/Officer2.ogg"), mainPlayer->cam, e->room->npc[0]->obj);
                }

                //Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 0, 8, 0.1, False)
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[4])>392*RoomScale) {
                    mainPlayer->blinkTimer = -10;
                    mainPlayer->blurTimer = 500;
                    bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->objects[5],true),bbEntityY(e->room->objects[5],true),bbEntityZ(e->room->objects[5],true),true);
                    bbRotateEntity(mainPlayer->collider, 0, bbEntityYaw(e->room->obj,true)+180, 0);
                    bbResetEntity(mainPlayer->collider);
                    e->eventState = 3;
                }

            }
        } else if ((e->eventState==3)) {
            if (e->room->doors[0]->openstate>160) {
                if (e->sounds[0]==0) {
                    e->sounds[0] = bbLoadSound("SFX/Music/1123.ogg");
                }
                e->soundChannels[0] = bbPlaySound(e->sounds[0]);

                bbPositionEntity(e->room->npc[0]->collider, bbEntityX(e->room->objects[7],true),bbEntityY(e->room->objects[7],true),bbEntityZ(e->room->objects[7],true));
                bbResetEntity(e->room->npc[0]->collider);

                e->eventState = 4;
            }
        } else if ((e->eventState==4)) {

            //PointEntity(e\room\npc[1]\collider, mainPlayer\collider)

            bbTFormPoint(bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider),bbEntityZ(mainPlayer->collider),0,e->room->obj);

            if (bbTFormedX()<256 & bbTFormedZ()>-480) {
                e->room->doors[0]->open = false;
            }

            if (bbEntityYaw(e->room->objects[13],false)==0) {
                bbDebugLog("aaaaaaaa");
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[12])<1.0) {
                    DrawHandIcon = true;
                    if (MouseHit1) {
                        bbRotateEntity(e->room->objects[13], 0, 1, 0, false);
                        PlaySound2(LoadTempSound("SFX/SCP/1123/Horror.ogg"));
                    }
                }
            } else {
                bbDebugLog("bbbbbbb");
                bbRotateEntity(e->room->objects[13], 0, CurveAngle(90, bbEntityYaw(e->room->objects[13], false), 40), 0);
                if (bbEntityYaw(e->room->objects[13], false)>30) {
                    e->room->npc[0]->state = 3;
                    bbPointEntity(e->room->npc[0]->collider, mainPlayer->collider);
                    AnimateNPC(e->room->npc[0], 570, 596, 0.5, false);
                    if (e->room->npc[0]->frame >= 596) {
                        e->eventState = 5;
                        e->eventState2 = 0;
                        bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj,true),0.3,bbEntityZ(e->room->obj,true),true);
                        bbResetEntity(mainPlayer->collider);
                        mainPlayer->blinkTimer = -10;
                        mainPlayer->blurTimer = 500;
                        mainPlayer->injuries = 1.5;
                        mainPlayer->bloodloss = 70;
                        //PlaySound2(LoadTempSound("SFX/Door/WoodenDoorClose.ogg"))
                    }
                }

            }

            //RemoveNPC(e\room\npc[0])
            //RemoveNPC(e\room\npc[1])
            //RemoveEvent(e)
        } else if ((e->eventState == 5)) {
            e->eventState2 = e->eventState2 + timing->tickDuration;
            if (e->eventState2 > 500) {
                bbRotateEntity(e->room->objects[9],0,90,0,false);
                bbRotateEntity(e->room->objects[13],0,0,0,false);

                x = (bbEntityX(e->room->objects[8], true)+bbEntityX(e->room->objects[12], true))/2;
                y = bbEntityY(e->room->objects[5], true);
                z = (bbEntityZ(e->room->objects[8], true)+bbEntityZ(e->room->objects[12], true))/2;
                bbPositionEntity(mainPlayer->collider, x,y,z, true);
                bbResetEntity(mainPlayer->collider);

                x = (bbEntityX(mainPlayer->collider, true)+bbEntityX(e->room->objects[12], true))/2;
                z = (bbEntityZ(mainPlayer->collider, true)+bbEntityZ(e->room->objects[12], true))/2;

                bbPositionEntity(e->room->npc[0]->collider, x,y+0.2,z);
                bbResetEntity(e->room->npc[0]->collider);

                mainPlayer->injuries = 1.5;
                mainPlayer->bloodloss = 70;

                mainPlayer->blinkTimer = -10;

                de = CreateDecal(DECAL_BLOOD_SPLATTER, bbEntityX(mainPlayer->collider), 512*RoomScale + 0.0005, bbEntityZ(mainPlayer->collider),90,bbRnd(360),0);
                de->size = 0.5;
                bbScaleSprite(de->obj, de->size, de->size);

                e->room->npc[0]->sounds[0] = bbLoadSound("SFX/SCP/1123/Officer3.ogg");

                e->eventState = 6;
            }
        } else if ((e->eventState == 6)) {
            bbPointEntity(e->room->npc[0]->collider, mainPlayer->collider);

            if (e->room->npc[0]->sounds[0]!=0) {
                if (e->room->npc[0]->soundChannels[0]!=0) {
                    if (!IsChannelPlaying(e->room->npc[0]->soundChannels[0])) {
                        PlaySound2(LoadTempSound("SFX/SCP/1123/Gunshot.ogg"));
                        e->eventState = 7;
                        bbFreeSound(e->room->npc[0]->sounds[0]);
                        e->room->npc[0]->sounds[0] = 0;
                    }
                }

                if (e->room->npc[0]->sounds[0]!=0) {
                    e->room->npc[0]->soundChannels[0] = LoopRangedSound(e->room->npc[0]->sounds[0], e->room->npc[0]->soundChannels[0], mainPlayer->cam, e->room->npc[0]->collider, 7.0);
                }
            }
        } else if ((e->eventState==7)) {
            bbPositionEntity(mainPlayer->collider, bbEntityX(e->room->obj,true),0.3,bbEntityZ(e->room->obj,true),true);
            bbResetEntity(mainPlayer->collider);
            bbShowEntity(mainPlayer->overlays[OVERLAY_WHITE]);
            mainPlayer->lightFlash = 6;
            mainPlayer->blurTimer = 500;
            mainPlayer->injuries = PrevInjuries;
            mainPlayer->bloodloss = PrevBloodloss;
            mainPlayer->crouching = false;

            PrevInjuries = 0;
            PrevBloodloss = 0;

            for (i = 0; i <= mainPlayer->inventory->size-1; i++) {
                if (mainPlayer->inventory->items[i] != nullptr) {
                    if (mainPlayer->inventory->items[i]->template->name == "Leaflet") {
                        mainPlayer->inventory->items[i] = nullptr;
                        RemoveItem(mainPlayer->inventory->items[i]);
                        break;
                    }
                }
            }

            RemoveNPC(e->room->npc[0]);
            RemoveEvent(e);
        }
    }
    //[End Block]


}

}

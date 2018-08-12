#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbaudio.h>

#include "../GameMain.h"
#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../Audio.h"
#include "../Menus/Menu.h"
#include "Room_hll_gas_3.h"

namespace CBN {

// Functions.
void FillRoom_hll_gas_3(Room* r) {
    Door* d;
    Door* d2;
    SecurityCam* sc;
    Decal* de;
    Room* r2;
    SecurityCam* sc2;
    Emitter* em;
    Item* it;
    int i;
    int xtemp;
    int ytemp;
    int ztemp;

    //, Bump
    int t1;

    em = CreateEmitter(r->x + 512.f * RoomScale, -76 * RoomScale, r->z - 688 * RoomScale, 0);
    bbTurnEntity(em->obj, -90, 0, 0);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 55;
    em->speed = 0.0005f;
    em->aChange = -0.015f;
    em->sizeChange = 0.007f;

    em = CreateEmitter(r->x - 512.f * RoomScale, -76 * RoomScale, r->z - 688 * RoomScale, 0);
    bbTurnEntity(em->obj, -90, 0, 0);
    bbEntityParent(em->obj, r->obj);
    em->randAngle = 55;
    em->speed = 0.0005f;
    em->aChange = -0.015f;
    em->sizeChange = 0.007f;

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x + 704.f * RoomScale, 112.f*RoomScale, r->z-416.f*RoomScale, true);
}

void UpdateEventRoom3pitduck(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;
    Texture* tex;

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


    if (mainPlayer->currRoom == e->room) {
        if (e->room->objects[2] == 0) {
            e->room->objects[2] = bbLoadMesh("GFX/npcs/duck_low_res.b3d");
            bbScaleEntity(e->room->objects[2], 0.07f, 0.07f, 0.07f);
            tex = bbLoadTexture("GFX/npcs/duck1.png");
            bbEntityTexture((MeshModel*)e->room->objects[2], tex);
            bbFreeTexture(tex);
            bbPositionEntity(e->room->objects[2], bbEntityX(e->room->objects[0],true), bbEntityY(e->room->objects[0],true), bbEntityZ(e->room->objects[0],true));
            bbPointEntity(e->room->objects[2], e->room->obj);
            bbRotateEntity(e->room->objects[2], 0, bbEntityYaw(e->room->objects[2],true),0, true);

            LoadEventSound(e,"SFX/SCP/Joke/Saxophone.ogg");
        } else {
            if (bbEntityInView(e->room->objects[2],mainPlayer->cam)==false) {
                e->eventState = e->eventState + timing->tickDuration;
                if (bbRand(200)==1 && e->eventState > 300) {
                    e->eventState = 0;
                    e->soundChannels[0] = PlayRangedSound(e->sounds[0], mainPlayer->cam, e->room->objects[2],6.f);
                }
            } else {
                if (e->soundChannels[0] != 0) {
                    if (bbChannelPlaying(e->soundChannels[0])) {
                        bbStopChannel(e->soundChannels[0]);
                    }
                }
            }
        }
    }

}

void UpdateEventRoom3pit1048(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;
    int tex;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    ItemTemplate* itt;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;
    String imgPath;
    int sf;
    int b;
    int t;
    String texname;


    if (mainPlayer->currRoom == e->room) {
        if (e->room->objects[2] == 0) {
            e->room->objects[2] = bbLoadAnimMesh("GFX/npcs/scp-1048pp.b3d");
            bbScaleEntity(e->room->objects[2], 0.05f,0.05f,0.05f);
            bbSetAnimTime(e->room->objects[2], 414);

            //TODO: Redo.
            //			imgPath = "GFX/items/1048/1048_"+String(Rand(1,20))+".jpg"
            //
            //			For itt = Each ItemTemplate
            //				If (itt\name = "Drawing") Then
            //					If (itt\img<>0) Then FreeImage(itt\img)
            //					itt\img = LoadImage(imgPath)
            //					MaskImage(itt\img, 255,0,255)
            //					itt\imgpath = imgPath
            //
            //					Exit
            //				EndIf
            //			Next
            //
            //			tex = LoadTexture(imgPath)
            //			brush = LoadBrush(imgPath, 1)
            //
            //			For i = 1 To CountSurfaces(e\room\objects[2])
            //				sf = GetSurface(e\room\objects[2],i)
            //				b = GetSurfaceBrush( sf )
            //				t = GetBrushTexture(b, 0)
            //				texname = StripPath(TextureName(t))
            //				DebugLog("texname: "+texname)
            //				If (Lower(texname) = "1048_1.jpg") Then
            //					PaintSurface(sf, brush)
            //				EndIf
            //				;MAV???
            //				;If (texname<>"") Then FreeTexture(t)
            //				FreeBrush(b)
            //			Next
            //
            //			FreeTexture(tex)
            //			FreeBrush(brush)

            bbPositionEntity(e->room->objects[2], bbEntityX(e->room->objects[0],true), bbEntityY(e->room->objects[0],true), bbEntityZ(e->room->objects[0],true));

            //e\sounds[0] = LoadSound("SFX/SCP/Joke/Saxophone.ogg")
        } else {
            bbPointEntity(e->room->objects[2], mainPlayer->collider);
            bbRotateEntity(e->room->objects[2], -90, bbEntityYaw(e->room->objects[2],true),0, true);

            if (e->eventState==0) {
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[2])<3.f) {
                    if (bbEntityInView(e->room->objects[2],mainPlayer->cam)) {
                        e->eventState = 1;
                    }
                }
            } else if ((e->eventState==1)) {
                Animate2((MeshModel*)e->room->objects[2], bbAnimTime(e->room->objects[2]), 414, 543, 0.5f, false);
                if (bbAnimTime(e->room->objects[2])==543) {
                    e->eventState = 2;
                }
            } else if ((e->eventState == 2)) {
                Animate2((MeshModel*)e->room->objects[2], bbAnimTime(e->room->objects[2]), 543, 692, 1.f);
                if (bbEntityDistance(mainPlayer->collider, e->room->objects[2])<1.5f) {
                    DrawHandIcon = true;

                    if (MouseHit1) {
                        mainPlayer->selectedItem = CreateItem("paper", 0.f, 0.f, 0.f);
                        //TODO: Maybe not have it automatically eject the first item from your inventory?
                        if (!SpaceInInventory(mainPlayer)) {
                            DropItem(mainPlayer->inventory->items[WORNITEM_SLOT_COUNT], mainPlayer->inventory);
                        }

                        PickItem(mainPlayer->selectedItem);

                        bbFreeEntity(e->room->objects[2]);

                        e->eventState = 3;
                        RemoveEvent(e);
                    }
                }
            }
        }
    }

}

}

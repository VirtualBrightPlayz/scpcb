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
#include "Room_hll_plain_3.h"

namespace CBN {

// Functions.
void UpdateEvent106victim(Event* e) {
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
    Decal* de;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;
    float fdir;

    //[Block]
    if (!Contained106) {
        if (mainPlayer->currRoom == e->room) {
            if (e->eventState == 0) {
                de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->obj), 799.f*RoomScale, bbEntityZ(e->room->obj), -90, bbRand(360), 0);
                de->size = 0.05;
                de->sizeChange = 0.0015;
                bbEntityAlpha(de->obj, 0.8);
                UpdateDecals();
                //TODO: fix
                //PlayRangedSound(DecaySFX(3), mainPlayer\cam, de\obj, 15.f)
                e->eventState = 1;
            }
        }

        if (e->eventState > 0) {
            if (e->room->npc[0]==nullptr) {
                e->eventState = e->eventState+timing->tickDuration;
            }
            if (e->eventState>200) {
                if (e->room->npc[0]==nullptr) {
                    e->room->npc[0] = CreateNPC(NPCtypeD, bbEntityX(e->room->obj), 900.f*RoomScale, bbEntityZ(e->room->obj));
                    bbRotateEntity(e->room->npc[0]->collider, 0, bbRnd(360), 0, true);
                    tex = bbLoadTexture("GFX/NPCs/classd/106victim.jpg");
                    e->room->npc[0]->texture = "GFX/NPCs/classd/106victim.jpg";
                    bbEntityTexture(e->room->npc[0]->obj, tex);
                    bbFreeTexture(tex);
                    e->room->npc[0]->state = 6;

                    //PlaySound2(HorrorSFX(0))
                    //TODO: fix
                    //PlayRangedSound(DecaySFX(2), mainPlayer\cam, e\room\npc[0]\collider, 15.f)
                }

                bbEntityType(e->room->npc[0]->collider,HIT_PLAYER);
                if (bbEntityY(e->room->npc[0]->collider)>0.35) {
                    AnimateNPC(e->room->npc[0], 1, 10, 0.12, false);
                    dist = bbEntityDistance(mainPlayer->collider,e->room->npc[0]->collider);
                    //get the player out of the way
                    if (dist<0.8) {
                        fdir = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(e->room->npc[0]->collider,true),bbEntityZ(e->room->npc[0]->collider,true));
                        bbTranslateEntity(mainPlayer->collider,bbCos(-fdir+90)*(dist-0.8)*(dist-0.8),0,bbSin(-fdir+90)*(dist-0.8)*(dist-0.8));
                    }

                    if (bbEntityY(e->room->npc[0]->collider)>0.6) {
                        bbEntityType(e->room->npc[0]->collider,0);
                    }
                } else {
                    e->eventState = e->eventState+timing->tickDuration;
                    AnimateNPC(e->room->npc[0], 11, 19, 0.25, false);
                    if (e->sounds[0]==0) {
                        //e\sounds[0] = LoadSound("SFX/General/BodyFall.ogg")
                        LoadEventSound(e,"SFX/General/BodyFall.ogg");
                        PlaySound2(e->sounds[0]);

                        de = CreateDecal(DECAL_CORROSION, bbEntityX(e->room->obj), 0.001, bbEntityZ(e->room->obj), 90, bbRand(360), 0);
                        de->size = 0.4;
                        bbEntityAlpha(de->obj, 0.8);
                        UpdateDecals();
                    }

                    if (e->eventState>400) {
                        if (e->sounds[0]!=0) {
                            bbFreeSound(e->sounds[0]);
                            e->sounds[0] = 0;
                        }
                        RemoveEvent(e);
                    }
                }

            }
        }

    }

    //[End Block]
}

}

#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbgraphics.h>
#include <bbaudio.h>
#include <bbinput.h>
#include <bbstring.h>

#include "../GameMain.h"
#include "../Assets.h"
#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Particles.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "../Audio.h"
#include "../INI.h"
#include "../MathUtils/MathUtils.h"
#include "../Menus/Menu.h"
#include "../Options.h"
#include "Room_hll_caf_2.h"

namespace CBN {

// Functions.
void FillRoom_hll_caf_2(Room* r) {
    Door* d;
    Door* d2;
    SecurityCam* sc;
    Decal* de;
    Room* r2;
    SecurityCam* sc2;
    Item* it = nullptr;
    int i;
    int xtemp;
    int ytemp;
    int ztemp;

    //, Bump
    int t1;

    //scp-294
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x+1847.f*RoomScale, -240.f*RoomScale, r->z-321*RoomScale, true);
    //"spawnpoint" for the cups
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x+1780.f*RoomScale, -248.f*RoomScale, r->z-276*RoomScale, true);

    //it = CreateItem("cup", "cup", r\x-508.f*RoomScale, -187*RoomScale, r\z+284.f*RoomScale, 240,175,70)
    //EntityParent(it\collider, r\obj)
    // it->name = "Cup of Orange Juice";

    //it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.f * RoomScale, 87,62,45)
    //EntityParent(it\collider, r\obj)
    // it->name = "Cup of Coffee";

    // it = CreateItem("cup", r->x-540*RoomScale, -187*RoomScale, r->z+124.f*RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void Draw294() {
    int x;
    int y;
    int xtemp;
    int ytemp;
    String strtemp;
    int temp;

    ImageAssetWrap* panel294 = ImageAssetWrap::grab("GFX/HUD/294panel.jpg");
    x = userOptions->screenWidth/2 - (bbImageWidth(panel294->getImage())/2);
    y = userOptions->screenHeight/2 - (bbImageHeight(panel294->getImage())/2);

    bbMaskImage(panel294->getImage(), 255, 0, 255);
    bbDrawImage(panel294->getImage(), x, y);
    panel294->drop();

    bbText(x+907, y+185, Input294, true,true);


}

void Update294() {
    float x;
    float y;
    int xtemp;
    int ytemp;
    String strtemp;
    int temp;
    int sep1;
    int sep2;
    int r;
    int g;
    int b;

    float alpha;
    int glow;

    ImageAssetWrap* panel294 = ImageAssetWrap::grab("GFX/HUD/294panel.jpg");
    x = userOptions->screenWidth/2 - (bbImageWidth(panel294->getImage())/2);
    y = userOptions->screenHeight/2 - (bbImageHeight(panel294->getImage())/2);
    panel294->drop();

    temp = true;
    if (mainPlayer->currRoom->soundCHN!=0) {
        temp = false;
    }

    if (temp) {
        if (MouseHit1) {
            xtemp = (int)(bbFloor((bbMouseX()-x-228) / 35.5f));
            ytemp = (int)(bbFloor((bbMouseY()-y-342) / 36.5f));

            if (ytemp >= 0 && ytemp < 5) {
                if (xtemp >= 0 && xtemp < 10) {
                    PlaySound_SM(sndManager->button);
                }
            }

            strtemp = "";

            temp = false;

            switch (ytemp) {
                case 0: {
                    strtemp = String((xtemp + 1) % 10);
                } break;
                case 1: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "Q";
                        } break;
                        case 1: {
                            strtemp = "W";
                        } break;
                        case 2: {
                            strtemp = "E";
                        } break;
                        case 3: {
                            strtemp = "R";
                        } break;
                        case 4: {
                            strtemp = "T";
                        } break;
                        case 5: {
                            strtemp = "Y";
                        } break;
                        case 6: {
                            strtemp = "U";
                        } break;
                        case 7: {
                            strtemp = "I";
                        } break;
                        case 8: {
                            strtemp = "O";
                        } break;
                        case 9: {
                            strtemp = "P";
                        } break;
                    }
                } break;
                case 2: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "A";
                        } break;
                        case 1: {
                            strtemp = "S";
                        } break;
                        case 2: {
                            strtemp = "D";
                        } break;
                        case 3: {
                            strtemp = "F";
                        } break;
                        case 4: {
                            strtemp = "G";
                        } break;
                        case 5: {
                            strtemp = "H";
                        } break;
                        case 6: {
                            strtemp = "J";
                        } break;
                        case 7: {
                            strtemp = "K";
                        } break;
                        case 8: {
                            strtemp = "L";
                            //dispense
                        } break;
                        case 9: {
                            temp = true;
                        } break;
                    }
                } break;
                case 3: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "Z";
                        } break;
                        case 1: {
                            strtemp = "X";
                        } break;
                        case 2: {
                            strtemp = "C";
                        } break;
                        case 3: {
                            strtemp = "V";
                        } break;
                        case 4: {
                            strtemp = "B";
                        } break;
                        case 5: {
                            strtemp = "N";
                        } break;
                        case 6: {
                            strtemp = "M";
                        } break;
                        case 7: {
                            strtemp = "-";
                        } break;
                        case 8: {
                            strtemp = " ";
                        } break;
                        case 9: {
                            Input294 = Input294.substr(0, (int)(Max(Input294.size()-1,0)));
                        } break;
                    }
                } break;
                case 4: {
                    strtemp = " ";
                } break;
            }

            Input294 = Input294 + strtemp;

            Input294 = Input294.substr(0, (int)(Min(Input294.size(),15)));

            //dispense
            if (temp && !Input294.isEmpty()) {
                Input294 = Input294.toLower().trim();
                if (Input294.substr(0, (int)(Min(7,Input294.size()))).equals("cup of ")) {
                    Input294 = bbRight(Input294, Input294.size()-7);
                } else if ((Input294.substr(0, (int)(Min(9,Input294.size()))).equals("a cup of " ))) {
                    Input294 = bbRight(Input294, Input294.size()-9);
                }

                strtemp = GetINIString("Data/SCP-294.ini", Input294, "color", "N/A");

                if (!strtemp.equals("N/A")) {
                    sep1 = bbInstr(strtemp, ",", 1);
                    sep2 = bbInstr(strtemp, ",", sep1 + 1);
                    r = strtemp.substr(0, sep1 - 1).trim().toInt();
                    g = bbMid(strtemp, sep1 + 1, sep2 - sep1 - 1).trim().toInt();
                    b = bbRight(strtemp, strtemp.size() - sep2).trim().toInt();

                    strtemp = GetINIString("Data/SCP-294.ini", Input294, "dispensesound");
                    if (strtemp.isEmpty()) {
                        mainPlayer->currRoom->soundCHN = bbPlaySound(LoadTempSound("SFX/SCP/294/dispense1.ogg"));
                    } else {
                        mainPlayer->currRoom->soundCHN = bbPlaySound(LoadTempSound(strtemp));
                    }

                    if (GetINIInt("Data/SCP-294.ini", Input294, "explosion")==true) {
                        ExplosionTimer = 135;
                        DeathMSG = GetINIString("Data/SCP-294.ini", Input294, "deathmessage");
                    }

                    alpha = GetINIString("Data/SCP-294.ini", Input294, "alpha").toFloat();
                    glow = GetINIInt("Data/SCP-294.ini", Input294, "glow");
                    // FIXME: Float equality.
                    if (alpha == 0) {
                        alpha = 1.f;
                    }
                    if (glow) {
                        alpha = -alpha;
                    }
                    //TODO: Re-implement
                    //it.Item = CreateItem("Cup", "cup", EntityX(mainPlayer\currRoom\objects[1],True),EntityY(mainPlayer\currRoom\objects[1],True),EntityZ(mainPlayer\currRoom\objects[1],True), r,g,b,alpha)
                    //it\name = "Cup of "+Input294
                    //EntityType(it\collider, HIT_ITEM)
                } else {
                    //out of range
                    Input294 = "OUT OF RANGE";
                    mainPlayer->currRoom->soundCHN = bbPlaySound(LoadTempSound("SFX/SCP/294/outofrange.ogg"));
                }

            }

            //if mousehit1
        }

        if (MouseHit2 && CurrGameState==GAMESTATE_SCP294) {
            bbHidePointer();
            CurrGameState = GAMESTATE_PLAYING;
            Input294 = "";
        }

        //playing a dispensing sound
    } else {
        if (!Input294.equals("OUT OF RANGE")) {
            Input294 = "DISPENSING...";
        }

        if (!bbChannelPlaying(mainPlayer->currRoom->soundCHN)) {
            if (!Input294.equals("OUT OF RANGE")) {
                bbHidePointer();
                CurrGameState = GAMESTATE_SCP294;
            }
            Input294 = "";
            mainPlayer->currRoom->soundCHN = 0;
        }
    }

}

void UpdateEvent_hll_caf_2(Event* e) {
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
        if (CurrGameState!=GAMESTATE_SCP294) {
            if (bbEntityDistance(e->room->objects[0], mainPlayer->collider)<1.5f) {
                if (bbEntityInView(e->room->objects[0], mainPlayer->cam)) {
                    DrawHandIcon = true;
                    if (MouseHit1) {
                        temp = true;
                        for (int iterator182 = 0; iterator182 < Item::getListSize(); iterator182++) {
                            it = Item::getObject(iterator182);

                            if (it->picked==false) {
                                if (bbEntityX(it->collider)-bbEntityX(e->room->objects[1],true)==0) {
                                    if (bbEntityZ(it->collider)-bbEntityZ(e->room->objects[1],true)==0) {
                                        temp = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (temp) {
                            CurrGameState = GAMESTATE_SCP294;
                            MouseHit1 = false;
                        }
                    }
                }
            }
        }
    }

    if (e->eventState == 0) {
        CreateNPC(NPCtype066, bbEntityX(e->room->obj), 0.5f, bbEntityZ(e->room->obj));
        e->eventState = 1;
    }
    //[End Block]
}

}

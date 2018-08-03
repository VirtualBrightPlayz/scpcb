#include "Room_hll_caf_2.h"
#include "include.h"

namespace CBN {

// Functions.
void FillRoom_hll_caf_2(Room* r) {
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

    //scp-294
    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x+1847.0*RoomScale, -240.0*RoomScale, r->z-321*RoomScale, true);
    //"spawnpoint" for the cups
    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x+1780.0*RoomScale, -248.0*RoomScale, r->z-276*RoomScale, true);

    //it = CreateItem("cup", "cup", r\x-508.0*RoomScale, -187*RoomScale, r\z+284.0*RoomScale, 240,175,70)
    //EntityParent(it\collider, r\obj)
    it->name = "Cup of Orange Juice";

    //it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.0 * RoomScale, 87,62,45)
    //EntityParent(it\collider, r\obj)
    it->name = "Cup of Coffee";

    it = CreateItem("cup", r->x-540*RoomScale, -187*RoomScale, r->z+124.0*RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void Draw294() {
    int x;
    int y;
    int xtemp;
    int ytemp;
    String strtemp;
    int temp;

    int panel294 = bbGrabImage("GFX/HUD/294panel.jpg");
    x = userOptions->screenWidth/2 - (bbImageWidth(panel294)/2);
    y = userOptions->screenHeight/2 - (bbImageHeight(panel294)/2);

    bbMaskImage(panel294, 255, 0, 255);
    bbDrawImage(panel294, x, y);
    DropAsset(panel294);

    bbText(x+907, y+185, Input294, true,true);


}

void Update294() {
    float x;
    float y;
    int xtemp;
    int ytemp;
    String strtemp;
    int temp;
    int loc;
    int sep1;
    int sep2;
    int r;
    int g;
    int b;

    float alpha;
    int glow;

    int panel294 = bbGrabImage("GFX/HUD/294panel.jpg");
    x = userOptions->screenWidth/2 - (bbImageWidth(panel294)/2);
    y = userOptions->screenHeight/2 - (bbImageHeight(panel294)/2);
    DropAsset(panel294);

    temp = true;
    if (mainPlayer->currRoom->soundCHN!=0) {
        temp = false;
    }

    if (temp) {
        if (MouseHit1) {
            xtemp = (int)(bbFloor((bbMouseX()-x-228) / 35.5));
            ytemp = (int)(bbFloor((bbMouseY()-y-342) / 36.5));

            if (ytemp ==> 0 & ytemp < 5) {
                if (xtemp ==> 0 & xtemp < 10) {
                    PlaySound_SM(sndManager->button);
                }
            }

            strtemp = "";

            temp = false;

            switch (ytemp) {
                case 0: {
                    strtemp = String((xtemp + 1) % 10);
                }
                case 1: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "Q";
                        }
                        case 1: {
                            strtemp = "W";
                        }
                        case 2: {
                            strtemp = "E";
                        }
                        case 3: {
                            strtemp = "R";
                        }
                        case 4: {
                            strtemp = "T";
                        }
                        case 5: {
                            strtemp = "Y";
                        }
                        case 6: {
                            strtemp = "U";
                        }
                        case 7: {
                            strtemp = "I";
                        }
                        case 8: {
                            strtemp = "O";
                        }
                        case 9: {
                            strtemp = "P";
                        }
                    }
                }
                case 2: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "A";
                        }
                        case 1: {
                            strtemp = "S";
                        }
                        case 2: {
                            strtemp = "D";
                        }
                        case 3: {
                            strtemp = "F";
                        }
                        case 4: {
                            strtemp = "G";
                        }
                        case 5: {
                            strtemp = "H";
                        }
                        case 6: {
                            strtemp = "J";
                        }
                        case 7: {
                            strtemp = "K";
                        }
                        case 8: {
                            strtemp = "L";
                            //dispense
                        }
                        case 9: {
                            temp = true;
                        }
                    }
                }
                case 3: {
                    switch (xtemp) {
                        case 0: {
                            strtemp = "Z";
                        }
                        case 1: {
                            strtemp = "X";
                        }
                        case 2: {
                            strtemp = "C";
                        }
                        case 3: {
                            strtemp = "V";
                        }
                        case 4: {
                            strtemp = "B";
                        }
                        case 5: {
                            strtemp = "N";
                        }
                        case 6: {
                            strtemp = "M";
                        }
                        case 7: {
                            strtemp = "-";
                        }
                        case 8: {
                            strtemp = " ";
                        }
                        case 9: {
                            Input294 = bbLeft(Input294, (int)(Max(Input294.size()-1,0)));
                        }
                    }
                }
                case 4: {
                    strtemp = " ";
                }
            }

            Input294 = Input294 + strtemp;

            Input294 = bbLeft(Input294, (int)(Min(Input294.size(),15)));

            //dispense
            if (temp && !Input294.isEmpty()) {
                Input294 = Input294.toLower().trim();
                if (bbLeft(Input294, (int)(Min(7,Input294.size()))) == "cup of ") {
                    Input294 = bbRight(Input294, Input294.size()-7);
                } else if ((bbLeft(Input294, (int)(Min(9,Input294.size()))) == "a cup of " )) {
                    Input294 = bbRight(Input294, Input294.size()-9);
                }

                loc = GetINISectionLocation("Data/SCP-294.ini", Input294);

                if (loc > 0) {
                    strtemp = GetINIString2("Data/SCP-294.ini", loc, "dispensesound");
                    if (strtemp.isEmpty()) {
                        mainPlayer->currRoom->soundCHN = bbPlaySound(LoadTempSound("SFX/SCP/294/dispense1.ogg"));
                    } else {
                        mainPlayer->currRoom->soundCHN = bbPlaySound(LoadTempSound(strtemp));
                    }

                    if (GetINIInt2("Data/SCP-294.ini", loc, "explosion")==true) {
                        ExplosionTimer = 135;
                        DeathMSG = GetINIString2("Data/SCP-294.ini", loc, "deathmessage");
                    }

                    strtemp = GetINIString2("Data/SCP-294.ini", loc, "color");

                    sep1 = bbInstr(strtemp, ",", 1);
                    sep2 = bbInstr(strtemp, ",", sep1+1);
                    r = (int)(bbLeft(strtemp, sep1-1).trim());
                    g = (int)(bbMid(strtemp, sep1+1, sep2-sep1-1).trim());
                    b = (int)(bbRight(strtemp, strtemp.size()-sep2).trim());

                    alpha = (float)(GetINIString2("Data/SCP-294.ini", loc, "alpha"));
                    glow = GetINIInt2("Data/SCP-294.ini", loc, "glow");
                    // FIXME: Float equality.
                    if (alpha == 0) {
                        alpha = 1.0;
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
        if (Input294 != "OUT OF RANGE") {
            Input294 = "DISPENSING...";
        }

        if (!IsChannelPlaying(mainPlayer->currRoom->soundCHN)) {
            if (Input294 != "OUT OF RANGE") {
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
            if (bbEntityDistance(e->room->objects[0], mainPlayer->collider)<1.5) {
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
        CreateNPC(NPCtype066, bbEntityX(e->room->obj), 0.5, bbEntityZ(e->room->obj));
        e->eventState = 1;
    }
    //[End Block]
}

}

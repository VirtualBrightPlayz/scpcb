#include "Console.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<ConsoleMsg*> ConsoleMsg::list;
ConsoleMsg::ConsoleMsg() {
    list.push_back(this);
}
ConsoleMsg::~ConsoleMsg() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int ConsoleMsg::getListSize() {
    return list.size();
}
ConsoleMsg* ConsoleMsg::getObject(int index) {
    return list[index];
}

// Globals.
String ConsoleInput;
float ConsoleScroll;
int ConsoleScrollDragging;
int ConsoleMouseMem;
ConsoleMsg* ConsoleReissue = nullptr;
int ConsoleR = 255;
int ConsoleG = 255;
int ConsoleB = 255;
int ConsoleFlush;
int ConsoleFlushSnd = 0;
int ConsoleMusFlush = 0;
int DebugHUD;

// Functions.
void CreateConsoleMsg(String txt, int r = -1, int g = -1, int b = -1, int isCommand = false) {
    ConsoleMsg* c = new ConsoleMsg();
    //TODO: Re-implement.
    //Insert c Before First ConsoleMsg

    c->txt = txt;
    c->isCommand = isCommand;

    c->r = r;
    c->g = g;
    c->b = b;

    if (c->r<0) {
        c->r = ConsoleR;
    }
    if (c->g<0) {
        c->g = ConsoleG;
    }
    if (c->b<0) {
        c->b = ConsoleB;
    }
}

void DrawConsole() {
    ConsoleMsg* cm;
    int inBar;
    int inBox;

    int x;
    int y;
    int width;
    int height;

    int consoleHeight;
    int scrollbarHeight;

    int TempY;
    int count;
    if (CurrGameState==GAMESTATE_CONSOLE) {
        bbSetFont(uiAssets->consoleFont);

        x = 0;
        y = userOptions->screenHeight-Int(300.0*MenuScale);
        width = userOptions->screenWidth;
        height = Int(300.0*MenuScale-30.0*MenuScale);

        DrawFrame(x,y,width,height+Int(30.0*MenuScale));

        consoleHeight = 0;
        scrollbarHeight = 0;

        for (int iterator40 = 0; iterator40 < ConsoleMsg::getListSize(); iterator40++) {
            cm = ConsoleMsg::getObject(iterator40);

            consoleHeight = consoleHeight + Int(15.0*MenuScale);
        }
        scrollbarHeight = Int((Float(height)/Float(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        bbColor(50,50,50);
        inBar = MouseOn(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height);
        if (inBar) {
            bbColor(70,70,70);
        }
        bbRect(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height,true);


        bbColor(120,120,120);
        inBox = MouseOn(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight);
        if (inBox) {
            bbColor(200,200,200);
        }
        if (ConsoleScrollDragging) {
            bbColor(255,255,255);
        }
        bbRect(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight,true);

        bbColor(255, 255, 255);

        TempY = y + height - Int(25.0*MenuScale) - Int(ConsoleScroll);
        count = 0;
        for (int iterator41 = 0; iterator41 < ConsoleMsg::getListSize(); iterator41++) {
            cm = ConsoleMsg::getObject(iterator41);

            count = count+1;
            if (count>1000) {
                delete cm;
            } else {
                if (TempY >= y & TempY < y + height - Int(20.0*MenuScale)) {
                    if (cm==ConsoleReissue) {
                        bbColor(cm->r/4,cm->g/4,cm->b/4);
                        bbRect(x,TempY-Int(2.0*MenuScale),width-Int(30.0*MenuScale),Int(24.0*MenuScale),true);
                    }
                    bbColor(cm->r,cm->g,cm->b);
                    if (cm->isCommand) {
                        bbText(x + Int(20.0*MenuScale), TempY, "> "+cm->txt);
                    } else {
                        bbText(x + Int(20.0*MenuScale), TempY, cm->txt);
                    }
                }
                TempY = TempY - Int(15.0*MenuScale);
            }

        }

        bbColor(255,255,255);

        DrawInputBox(x, y + height, width, Int(30.0*MenuScale), ConsoleInput, 2);


    }
}

void UpdateConsole() {
    int inBar;
    int inBox;
    int mouseScroll;
    int x;
    int y;
    int c;
    int sf;
    int b;
    int t;
    String texname;
    String StrTemp;
    String StrTemp2;
    String StrTemp3;
    int temp;
    int i;
    Event* ev;
    Room* r;
    Item* it;
    int consoleHeight;
    int scrollbarHeight;
    int width;
    int height;
    ItemTemplate* itt;

    ConsoleMsg* cm;
    NPC* n;
    String args;

    int reissuePos;

    String oldConsoleInput;

    int tex;
    int tex2;
    if (CurrGameState==GAMESTATE_CONSOLE) {
        ConsoleR = 255;
        ConsoleG = 255;
        ConsoleB = 255;

        x = 0;
        y = userOptions->screenHeight-Int(300.0*MenuScale);
        width = userOptions->screenWidth;
        height = Int(300.0*MenuScale-30.0*MenuScale);

        consoleHeight = 0;
        scrollbarHeight = 0;
        for (int iterator42 = 0; iterator42 < ConsoleMsg::getListSize(); iterator42++) {
            cm = ConsoleMsg::getObject(iterator42);

            consoleHeight = consoleHeight + Int(15.0*MenuScale);
        }
        scrollbarHeight = Int((Float(height)/Float(consoleHeight))*height);
        if (scrollbarHeight>height) {
            scrollbarHeight = height;
        }
        if (consoleHeight<height) {
            consoleHeight = height;
        }

        inBar = MouseOn(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height);

        inBox = MouseOn(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight);

        if (!bbMouseDown(1)) {
            ConsoleScrollDragging = false;
        } else if ((ConsoleScrollDragging)) {
            ConsoleScroll = ConsoleScroll+((bbMouseY()-ConsoleMouseMem)*height/scrollbarHeight);
            ConsoleMouseMem = bbMouseY();
        }

        if (!ConsoleScrollDragging) {
            if (MouseHit1) {
                if (inBox) {
                    ConsoleScrollDragging = true;
                    ConsoleMouseMem = bbMouseY();
                } else if ((inBar)) {
                    ConsoleScroll = ConsoleScroll+((bbMouseY()-(y+height))*consoleHeight/height+(height/2));
                    ConsoleScroll = ConsoleScroll/2;
                }
            }
        }

        mouseScroll = bbMouseZSpeed();
        if (mouseScroll==1) {
            ConsoleScroll = ConsoleScroll - 15*MenuScale;
        } else if ((mouseScroll==-1)) {
            ConsoleScroll = ConsoleScroll + 15*MenuScale;
        }

        if (bbKeyHit(200)) {
            reissuePos = 0;
            if (ConsoleReissue==nullptr) {
                ConsoleReissue = First ConsoleMsg;

                while (ConsoleReissue!=nullptr) {
                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos - Int(15.0*MenuScale);
                    ConsoleReissue = After ConsoleReissue;
                }

            } else {
                cm = First ConsoleMsg;
                while (cm != nullptr) {
                    if (cm==ConsoleReissue) {
                        break;
                    }
                    reissuePos = reissuePos-Int(15.0*MenuScale);
                    cm = After cm;
                }
                ConsoleReissue = After ConsoleReissue;
                reissuePos = reissuePos-Int(15.0*MenuScale);

                while (true) {
                    if (ConsoleReissue==nullptr) {
                        ConsoleReissue = First ConsoleMsg;
                        reissuePos = 0;
                    }

                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos - Int(15.0*MenuScale);
                    ConsoleReissue = After ConsoleReissue;
                }
            }

            if (ConsoleReissue!=nullptr) {
                ConsoleInput = ConsoleReissue->txt;
                ConsoleScroll = reissuePos+(height/2);
            }
        }

        if (bbKeyHit(208)) {
            reissuePos = -consoleHeight+Int(15.0*MenuScale);
            if (ConsoleReissue==nullptr) {
                ConsoleReissue = Last ConsoleMsg;

                while (ConsoleReissue!=nullptr) {
                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos + Int(15.0*MenuScale);
                    ConsoleReissue = Before ConsoleReissue;
                }

            } else {
                cm = Last ConsoleMsg;
                while (cm!=nullptr) {
                    if (cm==ConsoleReissue) {
                        break;
                    }
                    reissuePos = reissuePos+Int(15.0*MenuScale);
                    cm = Before cm;
                }
                ConsoleReissue = Before ConsoleReissue;
                reissuePos = reissuePos+Int(15.0*MenuScale);

                while (true) {
                    if (ConsoleReissue==nullptr) {
                        ConsoleReissue = Last ConsoleMsg;
                        reissuePos = -consoleHeight+Int(15.0*MenuScale);
                    }

                    if (ConsoleReissue->isCommand) {
                        break;
                    }
                    reissuePos = reissuePos + Int(15.0*MenuScale);
                    ConsoleReissue = Before ConsoleReissue;
                }
            }

            if (ConsoleReissue!=nullptr) {
                ConsoleInput = ConsoleReissue->txt;
                ConsoleScroll = reissuePos+(height/2);
            }
        }

        if (ConsoleScroll<-consoleHeight+height) {
            ConsoleScroll = -consoleHeight+height;
        }
        if (ConsoleScroll>0) {
            ConsoleScroll = 0;
        }

        SelectedInputBox = 2;
        oldConsoleInput = ConsoleInput;
        ConsoleInput = UpdateInputBox(x, y + height, width, Int(30.0*MenuScale), ConsoleInput, 2);
        if (oldConsoleInput!=ConsoleInput) {
            ConsoleReissue = nullptr;
        }
        ConsoleInput = bbLeft(ConsoleInput, 100);

        if (bbKeyHit(28) & ConsoleInput!= "") {
            ConsoleReissue = nullptr;
            ConsoleScroll = 0;
            CreateConsoleMsg(ConsoleInput,255,255,0,true);
            if (bbInstr(ConsoleInput, " ") > 0) {
                StrTemp = bbLower(bbLeft(ConsoleInput, bbInstr(ConsoleInput, " ") - 1));
            } else {
                StrTemp = bbLower(ConsoleInput);
            }

            //TODO: Overhaul this. Move all of the argument stuff to dedicated functions so this is actually readable/maintainable.
            switch (bbLower(StrTemp)) {
                case "help": {
                    if (bbInstr(ConsoleInput, " ")!=0) {
                        StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    } else {
                        StrTemp = "";
                    }
                    ConsoleR = 0;
                    ConsoleG = 255;
                    ConsoleB = 255;

                    switch (bbLower(StrTemp)) {
                        case "1","": {
                            CreateConsoleMsg("LIST OF COMMANDS - PAGE 1/3");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("- asd");
                            CreateConsoleMsg("- status");
                            CreateConsoleMsg("- camerapick");
                            CreateConsoleMsg("- ending");
                            CreateConsoleMsg("- noclipspeed");
                            CreateConsoleMsg("- noclip");
                            CreateConsoleMsg("- injure [value]");
                            CreateConsoleMsg("- infect [value]");
                            CreateConsoleMsg("- heal");
                            CreateConsoleMsg("- teleport [room name]");
                            CreateConsoleMsg("- spawnitem [item name]");
                            CreateConsoleMsg("- wireframe");
                            CreateConsoleMsg("- 173speed");
                            CreateConsoleMsg("- 106speed");
                            CreateConsoleMsg("- 173state");
                            CreateConsoleMsg("- 106state");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Use "+bbChr(34)+"help 2/3"+bbChr(34)+" to find more commands.");
                            CreateConsoleMsg("Use "+bbChr(34)+"help [command name]"+bbChr(34)+" to get more information about a command.");
                            CreateConsoleMsg("******************************");
                        }
                        case "2": {
                            CreateConsoleMsg("LIST OF COMMANDS - PAGE 2/3");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("- spawn513-1");
                            CreateConsoleMsg("- spawn106");
                            CreateConsoleMsg("- reset096");
                            CreateConsoleMsg("- disable173");
                            CreateConsoleMsg("- enable173");
                            CreateConsoleMsg("- disable106");
                            CreateConsoleMsg("- enable106");
                            CreateConsoleMsg("- halloween");
                            CreateConsoleMsg("- sanic");
                            CreateConsoleMsg("- scp-420-j");
                            CreateConsoleMsg("- godmode");
                            CreateConsoleMsg("- revive");
                            CreateConsoleMsg("- noclip");
                            CreateConsoleMsg("- showfps");
                            CreateConsoleMsg("- 096state");
                            CreateConsoleMsg("- debughud");
                            CreateConsoleMsg("- camerafog [near] [far]");
                            CreateConsoleMsg("- gamma [value]");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Use "+bbChr(34)+"help [command name]"+bbChr(34)+" to get more information about a command.");
                            CreateConsoleMsg("******************************");
                        }
                        case "3": {
                            CreateConsoleMsg("- spawn [npc type]");
                            CreateConsoleMsg("- infinitestamina");
                            CreateConsoleMsg("- notarget");
                            CreateConsoleMsg("- spawnnpcstate [npc type] [state]");
                            CreateConsoleMsg("- unlockexits");
                        }
                        case "asd": {
                            CreateConsoleMsg("HELP - asd");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Actives godmode, noclip, wireframe and");
                            CreateConsoleMsg("sets fog distance to 20 near, 30 far");
                            CreateConsoleMsg("******************************");
                        }
                        case "camerafog": {
                            CreateConsoleMsg("HELP - camerafog");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Sets the draw distance of the fog.");
                            CreateConsoleMsg("The fog begins generating at 'CameraFogNear' units");
                            CreateConsoleMsg("away from the camera and becomes completely opaque");
                            CreateConsoleMsg("at 'CameraFogFar' units away from the camera.");
                            CreateConsoleMsg("Example: camerafog 20 40");
                            CreateConsoleMsg("******************************");
                        }
                        case "gamma": {
                            CreateConsoleMsg("HELP - gamma");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Sets the gamma correction.");
                            CreateConsoleMsg("Should be set to a value between 0.0 and 2.0.");
                            CreateConsoleMsg("Default is 1.0.");
                            CreateConsoleMsg("******************************");
                        }
                        case "noclip","fly": {
                            CreateConsoleMsg("HELP - noclip");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Toggles noclip, unless a valid parameter");
                            CreateConsoleMsg("is specified (on/off).");
                            CreateConsoleMsg("Allows the camera to move in any direction while");
                            CreateConsoleMsg("bypassing collision.");
                            CreateConsoleMsg("******************************");
                        }
                        case "godmode": {
                            CreateConsoleMsg("HELP - godmode");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Toggles godmode, unless a valid parameter");
                            CreateConsoleMsg("is specified (on/off).");
                            CreateConsoleMsg("Prevents player death under normal circumstances.");
                            CreateConsoleMsg("******************************");
                        }
                        case "wireframe": {
                            CreateConsoleMsg("HELP - wireframe");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Toggles wireframe, unless a valid parameter");
                            CreateConsoleMsg("is specified (on/off).");
                            CreateConsoleMsg("Allows only the edges of geometry to be rendered,");
                            CreateConsoleMsg("making everything else transparent.");
                            CreateConsoleMsg("******************************");
                        }
                        case "spawnitem": {
                            CreateConsoleMsg("HELP - spawnitem");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Spawns an item at the player's location.");
                            CreateConsoleMsg("Any name that can appear in your inventory");
                            CreateConsoleMsg("is a valid parameter.");
                            CreateConsoleMsg("Example: spawnitem key5");
                            CreateConsoleMsg("******************************");
                        }
                        case "spawn": {
                            CreateConsoleMsg("HELP - spawn");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Spawns an NPC at the player's location.");
                            CreateConsoleMsg("Valid parameters are:");
                            CreateConsoleMsg("049 / zombie (049-2) / 096 / 106 / 173 / 513-1");
                            CreateConsoleMsg("/ 966 / 1499-1 / guard / mtf");
                            CreateConsoleMsg("******************************");
                        }
                        case "revive","undead","resurrect": {
                            CreateConsoleMsg("HELP - revive");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Resets the player's death timer after the dying");
                            CreateConsoleMsg("animation triggers.");
                            CreateConsoleMsg("Does not affect injury, blood loss");
                            CreateConsoleMsg("or 008 infection values.");
                            CreateConsoleMsg("******************************");
                        }
                        case "teleport": {
                            CreateConsoleMsg("HELP - teleport");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Teleports the player to the first instance");
                            CreateConsoleMsg("of the specified room. Any room that appears");
                            CreateConsoleMsg("in rooms.ini is a valid parameter.");
                            CreateConsoleMsg("******************************");
                        }
                        case "stopsound", "stfu": {
                            CreateConsoleMsg("HELP - stopsound");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Stops all currently playing sounds.");
                            CreateConsoleMsg("******************************");
                        }
                        case "camerapick": {
                            CreateConsoleMsg("HELP - camerapick");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Prints the texture name and coordinates of");
                            CreateConsoleMsg("the model the camera is pointing at.");
                            CreateConsoleMsg("******************************");
                        }
                        case "status": {
                            CreateConsoleMsg("HELP - status");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Prints player, camera, and room information.");
                            CreateConsoleMsg("******************************");
                        }
                        case "weed","scp-420-j","420": {
                            CreateConsoleMsg("HELP - 420");
                            CreateConsoleMsg("******************************");
                            CreateConsoleMsg("Generates dank memes.");
                            CreateConsoleMsg("******************************");

                        }
                        default: {
                            CreateConsoleMsg("There is no help available for that command.",255,150,0);
                        }
                    }

                }
                case "asd": {
                    bbWireFrame(1);
                    WireframeState = 1;
                    mainPlayer->godMode = true;
                    mainPlayer->noclip = true;
                    //CameraFogNear = 15
                    //CameraFogFar = 20
                }
                case "status": {
                    ConsoleR = 0;
                    ConsoleG = 255;
                    ConsoleB = 0;
                    CreateConsoleMsg("******************************");
                    CreateConsoleMsg("Status: ");
                    CreateConsoleMsg("Coordinates: ");
                    CreateConsoleMsg("    - collider: "+String(bbEntityX(mainPlayer->collider))+", "+String(bbEntityY(mainPlayer->collider))+", "+String(bbEntityZ(mainPlayer->collider)));
                    CreateConsoleMsg("    - camera: "+String(bbEntityX(mainPlayer->cam))+", "+String(bbEntityY(mainPlayer->cam))+", "+String(bbEntityZ(mainPlayer->cam)));

                    CreateConsoleMsg("Rotation: ");
                    CreateConsoleMsg("    - collider: "+String(bbEntityPitch(mainPlayer->collider))+", "+String(bbEntityYaw(mainPlayer->collider))+", "+String(bbEntityRoll(mainPlayer->collider)));
                    CreateConsoleMsg("    - camera: "+String(bbEntityPitch(mainPlayer->cam))+", "+String(bbEntityYaw(mainPlayer->cam))+", "+String(bbEntityRoll(mainPlayer->cam)));

                    CreateConsoleMsg("Room: "+mainPlayer->currRoom->roomTemplate->name);
                    for (int iterator43 = 0; iterator43 < Event::getListSize(); iterator43++) {
                        ev = Event::getObject(iterator43);

                        if (ev->room == mainPlayer->currRoom) {
                            CreateConsoleMsg("Room event: "+ev->name);
                            CreateConsoleMsg("-    state: "+String(ev->eventState));
                            CreateConsoleMsg("-    state2: "+String(ev->eventState2));
                            CreateConsoleMsg("-    state3: "+String(ev->eventState3));
                            break;
                        }
                    }

                    CreateConsoleMsg("Room coordinates: "+String(bbFloor(bbEntityX(mainPlayer->currRoom->obj) / 8.0 + 0.5))+", "+String(bbFloor(bbEntityZ(mainPlayer->currRoom->obj) / 8.0 + 0.5)));
                    CreateConsoleMsg("Stamina: "+String(mainPlayer->stamina));
                    //CreateConsoleMsg("Dead: "+mainPlayer\dead)
                    CreateConsoleMsg("Blinktimer: "+String(mainPlayer->blinkTimer));
                    CreateConsoleMsg("Injuries: "+String(mainPlayer->injuries));
                    CreateConsoleMsg("Bloodloss: "+String(mainPlayer->bloodloss));
                    CreateConsoleMsg("******************************");

                }
                case "camerapick": {
                    ConsoleR = 0;
                    ConsoleG = 255;
                    ConsoleB = 0;
                    c = bbCameraPick(mainPlayer->cam, userOptions->screenWidth / 2, userOptions->screenHeight / 2);
                    if (c == 0) {
                        CreateConsoleMsg("******************************");
                        CreateConsoleMsg("No entity picked");
                        CreateConsoleMsg("******************************");
                    } else {
                        CreateConsoleMsg("******************************");
                        CreateConsoleMsg("Picked entity:");
                        sf = bbGetSurface(c,1);
                        b = bbGetSurfaceBrush( sf );
                        t = bbGetBrushTexture(b,0);
                        texname = StripPath(bbTextureName(t));
                        CreateConsoleMsg("Texture name: "+texname);
                        CreateConsoleMsg("Coordinates: "+String(bbEntityX(c))+", "+String(bbEntityY(c))+", "+String(bbEntityZ(c)));
                        CreateConsoleMsg("******************************");
                        bbFreeTexture(t);
                        bbFreeBrush(b);
                    }

                }
                case "hidedistance": {
                    HideDistance = Float(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    CreateConsoleMsg("Hidedistance set to "+String(HideDistance));

                }
                case "ending": {
                    CurrGameState = GAMESTATE_ENDING;
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    Kill(mainPlayer);

                }
                case "noclipspeed": {
                    bbRuntimeError("TODO: reimplement?");
                    //StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

                    //NoClipSpeed = Float(StrTemp)

                }
                case "injure": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    mainPlayer->injuries = Float(StrTemp);

                }
                case "infect": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    mainPlayer->infect008 = Float(StrTemp);

                }
                case "heal": {
                    mainPlayer->injuries = 0;
                    mainPlayer->bloodloss = 0;

                }
                case "teleport": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    for (int iterator44 = 0; iterator44 < Room::getListSize(); iterator44++) {
                        r = Room::getObject(iterator44);

                        if (r->roomTemplate->name == StrTemp) {
                            //PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
                            bbPositionEntity(mainPlayer->collider, r->x, r->y+0.7, r->z);
                            bbResetEntity(mainPlayer->collider);
                            UpdateDoors();
                            UpdateRooms();
                            mainPlayer->currRoom = r;
                            break;
                        }
                    }

                    if (mainPlayer->currRoom->roomTemplate->name != StrTemp) {
                        CreateConsoleMsg("Room not found.",255,150,0);
                    }

                }
                case "guh": {
                    for (int iterator45 = 0; iterator45 < Room::getListSize(); iterator45++) {
                        r = Room::getObject(iterator45);

                        if (r->roomTemplate->name == "cont_914_1") {
                            //PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
                            bbPositionEntity(mainPlayer->collider, r->x, r->y+0.7, r->z);
                            bbResetEntity(mainPlayer->collider);
                            UpdateDoors();
                            UpdateRooms();
                            mainPlayer->currRoom = r;
                            CreateItem("gasmask", r->x, r->y+2, r->z);
                            break;
                        }
                    }

                }
                case "spawnitem": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    temp = false;
                    for (int iterator46 = 0; iterator46 < ItemTemplate::getListSize(); iterator46++) {
                        itt = ItemTemplate::getObject(iterator46);

                        if (bbLower(itt->name) == StrTemp) {
                            temp = true;
                            CreateConsoleMsg(itt->name + " spawned.");
                            it = CreateItem(itt->name, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam,true), bbEntityZ(mainPlayer->collider));
                            break;
                        }
                    }

                    if (temp == false) {
                        CreateConsoleMsg("Item not found.",255,150,0);
                    }

                }
                case "spawndoc": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    CreatePaper(StrTemp, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam,true), bbEntityZ(mainPlayer->collider));

                }
                case "test914key": {
                    it = CreateItem("keycard", bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam,true), bbEntityZ(mainPlayer->collider));
                    AssignTag(it, "cont_914_1");

                }
                case "spawnomni": {
                    it = CreateItem("keycard", bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam,true), bbEntityZ(mainPlayer->collider));
                    AssignTag(it, ITEM_TAG_OMNI);
                    CreateConsoleMsg("Nerd.",255,150,0);

                }
                case "wireframe": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    switch (StrTemp) {
                        case "on", "1", "true": {
                            WireframeState = true;
                        }
                        case "off", "0", "false": {
                            WireframeState = false;
                        }
                        default: {
                            WireframeState = !WireframeState;
                        }
                    }

                    if (WireframeState) {
                        CreateConsoleMsg("WIREFRAME ON");
                    } else {
                        CreateConsoleMsg("WIREFRAME OFF");
                    }

                    bbWireFrame(WireframeState);

                }
                case "173speed": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    Curr173->speed = Float(StrTemp);
                    CreateConsoleMsg("173's speed set to " + StrTemp);

                }
                case "106speed": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    Curr106->speed = Float(StrTemp);
                    CreateConsoleMsg("106's speed set to " + StrTemp);

                }
                case "173state": {
                    CreateConsoleMsg("SCP-173");
                    CreateConsoleMsg("Position: " + String(bbEntityX(Curr173->obj)) + ", " + String(bbEntityY(Curr173->obj)) + ", " + String(bbEntityZ(Curr173->obj)));
                    CreateConsoleMsg("Idle: " + String(Curr173->idle));
                    CreateConsoleMsg("State: " + String(Curr173->state));

                }
                case "106state": {
                    CreateConsoleMsg("SCP-106");
                    CreateConsoleMsg("Position: " + String(bbEntityX(Curr106->obj)) + ", " + String(bbEntityY(Curr106->obj)) + ", " + String(bbEntityZ(Curr106->obj)));
                    CreateConsoleMsg("Idle: " + String(Curr106->idle));
                    CreateConsoleMsg("State: " + String(Curr106->state));

                }
                case "spawn513-1": {
                    CreateNPC(NPCtype5131, 0,0,0);

                }
                case "spawn106": {
                    Curr106->state = -1;
                    bbPositionEntity(Curr106->collider, bbEntityX(mainPlayer->collider), bbEntityY(Curr106->collider), bbEntityZ(mainPlayer->collider));

                }
                case "reset096": {
                    for (int iterator47 = 0; iterator47 < NPC::getListSize(); iterator47++) {
                        n = NPC::getObject(iterator47);

                        if (n->npcType == NPCtype096) {
                            n->state = 0;
                            break;
                        }
                    }

                }
                case "disable173": {
                    //TODO: clean up
                    Curr173->idle = 3;
                    bbHideEntity(Curr173->obj);
                    bbHideEntity(Curr173->collider);

                }
                case "enable173": {
                    Curr173->idle = false;
                    bbShowEntity(Curr173->obj);
                    bbShowEntity(Curr173->collider);

                }
                case "disable106": {
                    Curr106->idle = true;
                    Curr106->state = 200000;
                    Contained106 = true;

                }
                case "enable106": {
                    Curr106->idle = false;
                    Contained106 = false;
                    bbShowEntity(Curr106->collider);
                    bbShowEntity(Curr106->obj);

                }
                case "halloween": {
                    HalloweenTex = !HalloweenTex;
                    if (HalloweenTex) {
                        tex = bbLoadTexture("GFX/npcs/173h.pt", 1);
                        bbEntityTexture(Curr173->obj, tex, 0, 0);
                        bbFreeTexture(tex);
                        CreateConsoleMsg("173 JACK-O-LANTERN ON");
                    } else {
                        tex2 = bbLoadTexture("GFX/npcs/173texture.png", 1);
                        bbEntityTexture(Curr173->obj, tex2, 0, 0);
                        bbFreeTexture(tex2);
                        CreateConsoleMsg("173 JACK-O-LANTERN OFF");
                    }

                }
                case "sanic": {
                    mainPlayer->superMan = !mainPlayer->superMan;
                    if (mainPlayer->superMan == true) {
                        CreateConsoleMsg("GOTTA GO FAST");
                    } else {
                        CreateConsoleMsg("WHOA SLOW DOWN");
                    }
                }
                case "godmode": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    switch (StrTemp) {
                        case "on", "1", "true": {
                            mainPlayer->godMode = true;
                        }
                        case "off", "0", "false": {
                            mainPlayer->godMode = false;
                        }
                        default: {
                            mainPlayer->godMode = !mainPlayer->godMode;
                        }
                    }
                    if (mainPlayer->godMode) {
                        CreateConsoleMsg("GODMODE ON");
                    } else {
                        CreateConsoleMsg("GODMODE OFF");
                    }

                }
                case "revive","undead","resurrect": {
                    mainPlayer->dead = false;

                    mainPlayer->dropSpeed = -0.1;
                    mainPlayer->camShake = 0;
                    mainPlayer->moveSpeed = 0;

                    mainPlayer->heartbeatIntensity = 0;

                    mainPlayer->lightFlash = 0;
                    mainPlayer->blurTimer = 0;

                    mainPlayer->fallTimer = 0;

                    mainPlayer->godMode = 0;
                    mainPlayer->noclip = 0;

                    bbShowEntity(mainPlayer->collider);

                }
                case "noclip","fly": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    switch (StrTemp) {
                        case "on", "1", "true": {
                            mainPlayer->noclip = true;
                            mainPlayer->disableControls = false;
                        }
                        case "off", "0", "false": {
                            mainPlayer->noclip = false;
                            bbRotateEntity(mainPlayer->collider, 0, bbEntityYaw(mainPlayer->collider), 0);
                        }
                        default: {
                            mainPlayer->noclip = !mainPlayer->noclip;
                            if (mainPlayer->noclip == false) {
                                bbRotateEntity(mainPlayer->collider, 0, bbEntityYaw(mainPlayer->collider), 0);
                            } else {
                                mainPlayer->disableControls = false;
                            }
                        }
                    }

                    if (mainPlayer->noclip) {
                        CreateConsoleMsg("NOCLIP ON");
                    } else {
                        CreateConsoleMsg("NOCLIP OFF");
                    }

                    mainPlayer->dropSpeed = 0;

                }
                case "showfps": {
                    userOptions->showFPS = !userOptions->showFPS;
                    CreateConsoleMsg("ShowFPS: " + String(userOptions->showFPS));

                }
                case "096state": {
                    for (int iterator48 = 0; iterator48 < NPC::getListSize(); iterator48++) {
                        n = NPC::getObject(iterator48);

                        if (n->npcType == NPCtype096) {
                            CreateConsoleMsg("SCP-096");
                            CreateConsoleMsg("Position: " + String(bbEntityX(n->obj)) + ", " + String(bbEntityY(n->obj)) + ", " + String(bbEntityZ(n->obj)));
                            CreateConsoleMsg("Idle: " + String(n->idle));
                            CreateConsoleMsg("State: " + String(n->state));
                            break;
                        }
                    }
                    CreateConsoleMsg("SCP-096 has not spawned.");

                }
                case "debughud": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    switch (StrTemp) {
                        case "on", "1", "true": {
                            DebugHUD = true;
                        }
                        case "off", "0", "false": {
                            DebugHUD = false;
                        }
                        default: {
                            DebugHUD = !DebugHUD;
                        }
                    }

                    if (DebugHUD) {
                        CreateConsoleMsg("Debug Mode On");
                    } else {
                        CreateConsoleMsg("Debug Mode Off");
                    }

                }
                case "stopsound", "stfu": {
                    StopSounds();

                }
                case "camerafog": {
                    bbRuntimeError("TODO: reimplement?");
                    //args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
                    //CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
                    //CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
                    //CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

                }
                case "gamma": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    userOptions->screenGamma = Float(StrTemp);
                    CreateConsoleMsg("Gamma set to " + String(userOptions->screenGamma));

                }
                case "spawn": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    Console_SpawnNPC(StrTemp);

                }
                case "infinitestamina","infstam": {
                    bbRuntimeError("TODO: reimplement?");

                    //StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

                    //Select (StrTemp)
                    //	Case "on", "1", "true"
                    //		InfiniteStamina% = True
                    //	Case "off", "0", "false"
                    //		InfiniteStamina% = False
                    //	Default
                    //		InfiniteStamina% = Not InfiniteStamina%
                    //End Select

                    //If InfiniteStamina
                    //	CreateConsoleMsg("INFINITE STAMINA ON")
                    //Else
                    //	CreateConsoleMsg("INFINITE STAMINA OFF")
                    //EndIf

                }
                case "spawnnpcstate": {
                    args = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    StrTemp = Piece$(args,1," ");
                    StrTemp2 = Piece$(args,2," ");
                    Console_SpawnNPC(StrTemp,Int(StrTemp2));

                }
                case "toggle_warhead_lever": {
                    for (int iterator49 = 0; iterator49 < Event::getListSize(); iterator49++) {
                        ev = Event::getObject(iterator49);

                        if (ev->name == "room2nuke") {
                            ev->eventState = (!ev->eventState);
                            break;
                        }
                    }

                }
                case "unlockexits": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    switch (StrTemp) {
                        case "a": {
                            for (int iterator50 = 0; iterator50 < Event::getListSize(); iterator50++) {
                                ev = Event::getObject(iterator50);

                                if (ev->name == "gateaentrance") {
                                    ev->eventState3 = 1;
                                    ev->room->doors[1]->open = true;
                                    break;
                                }
                            }
                            CreateConsoleMsg("Gate A is now unlocked.");
                        }
                        case "b": {
                            for (int iterator51 = 0; iterator51 < Event::getListSize(); iterator51++) {
                                ev = Event::getObject(iterator51);

                                if (ev->name == "exit1") {
                                    ev->eventState3 = 1;
                                    ev->room->doors[4]->open = true;
                                    break;
                                }
                            }
                            CreateConsoleMsg("Gate B is now unlocked.");
                        }
                        default: {
                            for (int iterator52 = 0; iterator52 < Event::getListSize(); iterator52++) {
                                ev = Event::getObject(iterator52);

                                if (ev->name == "gateaentrance") {
                                    ev->eventState3 = 1;
                                    ev->room->doors[1]->open = true;
                                } else if ((ev->name == "exit1")) {
                                    ev->eventState3 = 1;
                                    ev->room->doors[4]->open = true;
                                }
                            }
                            CreateConsoleMsg("Gate A and B are now unlocked.");
                        }
                    }

                    RemoteDoorOn = true;

                }
                case "kill","suicide": {
                    Kill(mainPlayer);

                    switch (bbRand(4)) {
                        case 1: {
                            DeathMSG = "[REDACTED]";
                        }
                        case 2: {
                            DeathMSG = "Subject D-9341 found dead in Sector [REDACTED]. ";
                            DeathMSG = DeathMSG + "The subject appears to have attained no physical damage, and there is no visible indication as to what killed him. ";
                            DeathMSG = DeathMSG + "Body was sent for autopsy.";
                        }
                        case 3: {
                            DeathMSG = "EXCP_ACCESS_VIOLATION";
                        }
                        case 4: {
                            DeathMSG = "Subject D-9341 found dead in Sector [REDACTED]. ";
                            DeathMSG = DeathMSG + "The subject appears to have scribbled the letters "+bbChr(34)+"kys"+bbChr(34)+" in his own blood beside him. ";
                            DeathMSG = DeathMSG + "No other signs of physical trauma or struggle can be observed. Body was sent for autopsy.";
                        }
                    }

                }
                case "tp_to_mtf": {
                    for (int iterator53 = 0; iterator53 < NPC::getListSize(); iterator53++) {
                        n = NPC::getObject(iterator53);

                        if (n->npcType == NPCtypeMTF) {
                            if (n->mtfLeader == nullptr) {
                                bbPositionEntity(mainPlayer->collider,bbEntityX(n->collider),bbEntityY(n->collider)+5,bbEntityZ(n->collider));
                                bbResetEntity(mainPlayer->collider);
                                break;
                            }
                        }
                    }

                }
                case "tele": {
                    args = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));
                    StrTemp = Piece$(args,1," ");
                    StrTemp2 = Piece$(args,2," ");
                    StrTemp3 = Piece$(args,3," ");
                    bbPositionEntity(mainPlayer->collider,Float(StrTemp),Float(StrTemp2),Float(StrTemp3));
                    bbPositionEntity(mainPlayer->cam,Float(StrTemp),Float(StrTemp2),Float(StrTemp3));
                    CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+String(bbEntityX(mainPlayer->collider))+"|"+String(bbEntityY(mainPlayer->collider))+"|"+String(bbEntityZ(mainPlayer->collider)));

                }
                case "notarget": {
                    StrTemp = bbLower(bbRight(ConsoleInput, bbLen(ConsoleInput) - bbInstr(ConsoleInput, " ")));

                    switch (StrTemp) {
                        case "on", "1", "true": {
                            NoTarget = true;
                        }
                        case "off", "0", "false": {
                            NoTarget = false;
                        }
                        default: {
                            NoTarget = !NoTarget;
                        }
                    }

                    if (NoTarget == false) {
                        CreateConsoleMsg("NOTARGET OFF");
                    } else {
                        CreateConsoleMsg("NOTARGET ON");
                    }
                }
                case "teleport173": {
                    bbPositionEntity(Curr173->collider,bbEntityX(mainPlayer->collider),bbEntityY(mainPlayer->collider)+0.2,bbEntityZ(mainPlayer->collider));
                    bbResetEntity(Curr173->collider);
                    //TODO
                }
                case Chr($6A)+Chr($6F)+Chr($72)+Chr($67)+Chr($65): {
                    bbRuntimeError("Implement");
                }
                default: {
                    CreateConsoleMsg("Command not found.",255,0,0);
                }
            }

            ConsoleInput = "";
        }
    }

    bbSetFont(uiAssets->font[0]);

}

}

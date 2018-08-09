#include <bbblitz3d.h>
#include <bbmath.h>

#include "ConsoleCommands.h"
#include "Console.h"
#include "Player.h"
#include "MapSystem.h"

namespace CBN {

std::vector<ConsoleCmd*> commandList;

void executeConsoleCommand(String name, std::vector<String> args) {
    ConsoleCmd* foundCmd;
    for (int i = 0; i < commandList.size(); i++) {
        if (commandList[i]->name.equals(name)) {
            foundCmd = commandList[i];
            break;
        } else {
            for (int j = 0; j < commandList[i]->aliases.size(); j++) {
                if (commandList[i]->aliases[j].equals(name)) {
                    foundCmd = commandList[i];
                    break;
                }
            }
        }
    }

    if (foundCmd != nullptr) {
        foundCmd->execute(args);
    } else {
        CreateConsoleMsg("Command not found.", 255, 0, 0);
    }
}
void generateConsoleCommands() {
    commandList.push_back(new Cmd_Status());
}
void clearConsoleCommands() {
    commandList.clear();
}

// Command execute definitions.
void Cmd_Status::execute(std::vector<String> args) {
    ConsoleR = 0;
    ConsoleG = 255;
    ConsoleB = 0;
    CreateConsoleMsg("******************************");
    CreateConsoleMsg("Status: ");
    CreateConsoleMsg("Coordinates: ");
    CreateConsoleMsg("    - collider: " + String(bbEntityX(mainPlayer->collider)) + ", " + String(bbEntityY(mainPlayer->collider)) + ", " + String(bbEntityZ(mainPlayer->collider)));
    CreateConsoleMsg("    - camera: " + String(bbEntityX(mainPlayer->cam)) + ", " + String(bbEntityY(mainPlayer->cam)) + ", " + String(bbEntityZ(mainPlayer->cam)));

    CreateConsoleMsg("Rotation: ");
    CreateConsoleMsg("    - collider: " + String(bbEntityPitch(mainPlayer->collider)) + ", " + String(bbEntityYaw(mainPlayer->collider)) + ", " + String(bbEntityRoll(mainPlayer->collider)));
    CreateConsoleMsg("    - camera: " + String(bbEntityPitch(mainPlayer->cam)) + ", " + String(bbEntityYaw(mainPlayer->cam)) + ", " + String(bbEntityRoll(mainPlayer->cam)));

    CreateConsoleMsg("Room: " + mainPlayer->currRoom->roomTemplate->name);

    CreateConsoleMsg("Room coordinates: " + String(bbFloor(bbEntityX(mainPlayer->currRoom->obj) / 8.0 + 0.5)) + ", " + String(bbFloor(bbEntityZ(mainPlayer->currRoom->obj) / 8.0 + 0.5)));
    CreateConsoleMsg("Stamina: " + String(mainPlayer->stamina));
    //CreateConsoleMsg("Dead: "+mainPlayer\dead)
    CreateConsoleMsg("Blinktimer: " + String(mainPlayer->blinkTimer));
    CreateConsoleMsg("Injuries: " + String(mainPlayer->injuries));
    CreateConsoleMsg("Bloodloss: " + String(mainPlayer->bloodloss));
    CreateConsoleMsg("******************************");
}

}

//TODO: Overhaul this. Move all of the argument stuff to dedicated functions so this is actually readable/maintainable.
switch (StrTemp.toLower()) {
case "help": {
    if (bbInstr(ConsoleInput, " ") != 0) {
        StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    }
    else {
        StrTemp = "";
    }
    ConsoleR = 0;
    ConsoleG = 255;
    ConsoleB = 255;

    switch (StrTemp.toLower()) {
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
    case "noclip", "fly": {
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
    case "revive", "undead", "resurrect": {
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
    case "weed", "scp-420-j", "420": {
        CreateConsoleMsg("HELP - 420");
        CreateConsoleMsg("******************************");
        CreateConsoleMsg("Generates dank memes.");
        CreateConsoleMsg("******************************");

    }
    default: {
        CreateConsoleMsg("There is no help available for that command.", 255, 150, 0);
    }
    }

}
case "status": {


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
    }
    else {
        CreateConsoleMsg("******************************");
        CreateConsoleMsg("Picked entity:");
        sf = bbGetSurface(c, 1);
        b = bbGetSurfaceBrush(sf);
        t = bbGetBrushTexture(b, 0);
        texname = StripPath(bbTextureName(t));
        CreateConsoleMsg("Texture name: " + texname);
        CreateConsoleMsg("Coordinates: " + String(bbEntityX(c)) + ", " + String(bbEntityY(c)) + ", " + String(bbEntityZ(c)));
        CreateConsoleMsg("******************************");
        bbFreeTexture(t);
        bbFreeBrush(b);
    }

}
case "hidedistance": {
    HideDistance = (float)(bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")));
    CreateConsoleMsg("Hidedistance set to " + String(HideDistance));

}
case "ending": {
    CurrGameState = GAMESTATE_ENDING;
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

    Kill(mainPlayer);

}
case "noclipspeed": {
    throw ("TODO: reimplement?");
    //StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

    //NoClipSpeed = (float)(StrTemp)

}
case "injure": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

    mainPlayer->injuries = (float)(StrTemp);

}
case "infect": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

    mainPlayer->infect008 = (float)(StrTemp);

}
case "heal": {
    mainPlayer->injuries = 0;
    mainPlayer->bloodloss = 0;

}
case "teleport": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

    for (int iterator44 = 0; iterator44 < Room::getListSize(); iterator44++) {
        r = Room::getObject(iterator44);

        if (r->roomTemplate->name == StrTemp) {
            //PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
            bbPositionEntity(mainPlayer->collider, r->x, r->y + 0.7, r->z);
            bbResetEntity(mainPlayer->collider);
            UpdateDoors();
            UpdateRooms();
            mainPlayer->currRoom = r;
            break;
        }
    }

    if (mainPlayer->currRoom->roomTemplate->name != StrTemp) {
        CreateConsoleMsg("Room not found.", 255, 150, 0);
    }

}
case "guh": {
    for (int iterator45 = 0; iterator45 < Room::getListSize(); iterator45++) {
        r = Room::getObject(iterator45);

        if (r->roomTemplate->name == "cont_914_1") {
            //PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
            bbPositionEntity(mainPlayer->collider, r->x, r->y + 0.7, r->z);
            bbResetEntity(mainPlayer->collider);
            UpdateDoors();
            UpdateRooms();
            mainPlayer->currRoom = r;
            CreateItem("gasmask", r->x, r->y + 2, r->z);
            break;
        }
    }

}
case "spawnitem": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    temp = false;
    for (int iterator46 = 0; iterator46 < ItemTemplate::getListSize(); iterator46++) {
        itt = ItemTemplate::getObject(iterator46);

        if (itt->name.toLower().equals(StrTemp)) {
            temp = true;
            CreateConsoleMsg(itt->name + " spawned.");
            it = CreateItem(itt->name, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));
            break;
        }
    }

    if (temp == false) {
        CreateConsoleMsg("Item not found.", 255, 150, 0);
    }

}
case "spawndoc": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    CreatePaper(StrTemp, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));

}
case "test914key": {
    it = CreateItem("keycard", bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));
    AssignTag(it, "cont_914_1");

}
case "spawnomni": {
    it = CreateItem("keycard", bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));
    AssignTag(it, ITEM_TAG_OMNI);
    CreateConsoleMsg("Nerd.", 255, 150, 0);

}
case "wireframe": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

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
    }
    else {
        CreateConsoleMsg("WIREFRAME OFF");
    }

    bbWireFrame(WireframeState);

}
case "173speed": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    Curr173->speed = StrTemp.toFloat();
    CreateConsoleMsg("173's speed set to " + StrTemp);

}
case "106speed": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    Curr106->speed = StrTemp.toFloat();
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
    CreateNPC(NPCtype5131, 0, 0, 0);

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
    }
    else {
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
    }
    else {
        CreateConsoleMsg("WHOA SLOW DOWN");
    }
}
case "godmode": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

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
    }
    else {
        CreateConsoleMsg("GODMODE OFF");
    }

}
case "revive", "undead", "resurrect": {
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
case "noclip", "fly": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

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
        }
        else {
            mainPlayer->disableControls = false;
        }
    }
    }

    if (mainPlayer->noclip) {
        CreateConsoleMsg("NOCLIP ON");
    }
    else {
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
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
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
    }
    else {
        CreateConsoleMsg("Debug Mode Off");
    }

}
case "stopsound", "stfu": {
    StopSounds();

}
case "camerafog": {
    throw ("TODO: reimplement?");
    //args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    //CameraFogNear = (float)(Left(args, Len(args) - Instr(args, " ")))
    //CameraFogFar = (float)(Right(args, Len(args) - Instr(args, " ")))
    //CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

}
case "gamma": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    userOptions->screenGamma = (float)(StrTemp);
    CreateConsoleMsg("Gamma set to " + String(userOptions->screenGamma));

}
case "spawn": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    Console_SpawnNPC(StrTemp);

}
case "infinitestamina", "infstam": {
    throw ("TODO: reimplement?");

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
    args = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    // StrTemp = Piece(args,1," ");
    // StrTemp2 = Piece(args,2," ");
    Console_SpawnNPC(StrTemp, StrTemp2.toInt());

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
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

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
            }
            else if ((ev->name == "exit1")) {
                ev->eventState3 = 1;
                ev->room->doors[4]->open = true;
            }
        }
        CreateConsoleMsg("Gate A and B are now unlocked.");
    }
    }

    RemoteDoorOn = true;

}
case "kill", "suicide": {
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
        DeathMSG = DeathMSG + "The subject appears to have scribbled the letters \"kys\" in his own blood beside him. ";
        DeathMSG = DeathMSG + "No other signs of physical trauma or struggle can be observed. Body was sent for autopsy.";
    }
    }

}
case "tp_to_mtf": {
    for (int iterator53 = 0; iterator53 < NPC::getListSize(); iterator53++) {
        n = NPC::getObject(iterator53);

        if (n->npcType == NPCtypeMTF) {
            if (n->mtfLeader == nullptr) {
                bbPositionEntity(mainPlayer->collider, bbEntityX(n->collider), bbEntityY(n->collider) + 5, bbEntityZ(n->collider));
                bbResetEntity(mainPlayer->collider);
                break;
            }
        }
    }

}
case "tele": {
    args = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    // StrTemp = Piece(args,1," ");
    // StrTemp2 = Piece(args,2," ");
    // StrTemp3 = Piece(args,3," ");
    bbPositionEntity(mainPlayer->collider, (float)(StrTemp), (float)(StrTemp2), (float)(StrTemp3));
    bbPositionEntity(mainPlayer->cam, (float)(StrTemp), (float)(StrTemp2), (float)(StrTemp3));
    CreateConsoleMsg("Teleported to coordinates (X|Y|Z): " + String(bbEntityX(mainPlayer->collider)) + "|" + String(bbEntityY(mainPlayer->collider)) + "|" + String(bbEntityZ(mainPlayer->collider)));

}
case "notarget": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();

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
    }
    else {
        CreateConsoleMsg("NOTARGET ON");
    }
}
case "teleport173": {
    bbPositionEntity(Curr173->collider, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->collider) + 0.2, bbEntityZ(mainPlayer->collider));
    bbResetEntity(Curr173->collider);
    //TODO
}
case Chr($6A) + Chr($6F) + Chr($72) + Chr($67) + Chr($65): {
    throw ("Implement");
}
default: {
    CreateConsoleMsg("Command not found.", 255, 0, 0);
}
}
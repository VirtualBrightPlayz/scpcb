#include <bbblitz3d.h>
#include <bbmath.h>
#include <iostream>

#include "ConsoleCommands.h"
#include "../../Player/Player.h"
#include "../../Map/MapSystem.h"
#include "../../Map/Doors.h"

namespace CBN {

std::vector<ConsoleCmd*> commandList;

void ConsoleCmd::executeCommand(const String& name, std::vector<String> args) {
    ConsoleCmd* foundCmd = nullptr;
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
        ConsoleMsg::create("Command not found.", 255, 0, 0);
    }
}
void ConsoleCmd::generateCommands() {
    commandList.push_back(new Cmd_Help());
    commandList.push_back(new Cmd_Status());
    commandList.push_back(new Cmd_DebugHUD());
    commandList.push_back(new Cmd_Wireframe());
    commandList.push_back(new Cmd_Noclip());
    commandList.push_back(new Cmd_GodMode());
    commandList.push_back(new Cmd_Teleport());
}
void ConsoleCmd::clearCommands() {
    for (int i = 0; i < commandList.size(); i++) {
        delete commandList[i];
    }
    commandList.clear();
}

// TODO: Maybe turn help descriptions into a vector to allow multi-line support.
void Cmd_Help::execute(std::vector<String> args) {
    ConsoleR = 0;
    ConsoleG = 255;
    ConsoleB = 255;
    if (args.size() <= 0) {
        ConsoleMsg::create("List of Available Commands:");
        for (int i = 0; i < commandList.size(); i++) {
            ConsoleMsg::create("- " + commandList[i]->name);
        }
    } else {
        ConsoleCmd* foundCmd = nullptr;
        for (int i = 0; i < commandList.size(); i++) {
            if (commandList[i]->name.equals(args[0])) {
                foundCmd = commandList[i];
                break;
            } else {
                for (int j = 0; j < commandList[i]->aliases.size(); j++) {
                    if (commandList[i]->aliases[j].equals(args[0])) {
                        foundCmd = commandList[i];
                        break;
                    }
                }
            }
        }

        if (foundCmd != nullptr) {
            ConsoleMsg::create("HELP - " + foundCmd->name);
            ConsoleMsg::create(foundCmd->description);
        } else {
            ConsoleMsg::create("Command not found.", 255, 0, 0);
        }
    }

}

// Command execute definitions.
void Cmd_Status::execute(std::vector<String> args) {
    ConsoleR = 0;
    ConsoleG = 255;
    ConsoleB = 0;
    ConsoleMsg::create("******************************");
    ConsoleMsg::create("Status: ");
    ConsoleMsg::create("Coordinates: ");
    ConsoleMsg::create("    - collider: " + String(bbEntityX(mainPlayer->collider)) + ", " + String(bbEntityY(mainPlayer->collider)) + ", " + String(bbEntityZ(mainPlayer->collider)));
    ConsoleMsg::create("    - camera: " + String(bbEntityX(mainPlayer->cam)) + ", " + String(bbEntityY(mainPlayer->cam)) + ", " + String(bbEntityZ(mainPlayer->cam)));

    ConsoleMsg::create("Rotation: ");
    ConsoleMsg::create("    - collider: " + String(bbEntityPitch(mainPlayer->collider)) + ", " + String(bbEntityYaw(mainPlayer->collider)) + ", " + String(bbEntityRoll(mainPlayer->collider)));
    ConsoleMsg::create("    - camera: " + String(bbEntityPitch(mainPlayer->cam)) + ", " + String(bbEntityYaw(mainPlayer->cam)) + ", " + String(bbEntityRoll(mainPlayer->cam)));

    ConsoleMsg::create("Room: " + mainPlayer->currRoom->roomTemplate->name);

    ConsoleMsg::create("Room coordinates: " + String(bbFloor(bbEntityX(mainPlayer->currRoom->obj) / 8.f + 0.5f)) + ", " + String(bbFloor(bbEntityZ(mainPlayer->currRoom->obj) / 8.f + 0.5f)));
    ConsoleMsg::create("Stamina: " + String(mainPlayer->stamina));
    //ConsoleMsg::create("Dead: "+mainPlayer\dead)
    ConsoleMsg::create("Blinktimer: " + String(mainPlayer->blinkTimer));
    ConsoleMsg::create("Injuries: " + String(mainPlayer->injuries));
    ConsoleMsg::create("Bloodloss: " + String(mainPlayer->bloodloss));
    ConsoleMsg::create("******************************");
}

void Cmd_DebugHUD::execute(std::vector<String> args) {
    DebugHUD = !DebugHUD;
    if (DebugHUD) {
        ConsoleMsg::create("DebugHUD ON");
    } else {
        ConsoleMsg::create("DebugHUD OFF");
    }
}

void Cmd_Wireframe::execute(std::vector<String> args) {
    wireframeState = !wireframeState;
    bbWireFrame(wireframeState);
    if (wireframeState) {
        ConsoleMsg::create("Wireframe ON");
    } else {
        ConsoleMsg::create("Wireframe OFF");
    }
}

void Cmd_Noclip::execute(std::vector<String> args) {
    mainPlayer->noclip = !mainPlayer->noclip;
    if (mainPlayer->noclip) {
        ConsoleMsg::create("Noclip ON");
    } else {
        ConsoleMsg::create("Noclip OFF");
    }
}

void Cmd_GodMode::execute(std::vector<String> args) {
    mainPlayer->godMode = !mainPlayer->godMode;
    if (mainPlayer->godMode) {
        ConsoleMsg::create("GodMode ON");
    } else {
        ConsoleMsg::create("GodMode OFF");
    }
}

void Cmd_Teleport::execute(std::vector<String> args) {
    if (args.size() <= 0) {
        ConsoleMsg::create("Please specify a room. (e.g. teleport cont_914_1)", 255, 150, 0);
        return;
    }

    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);

        if (r->roomTemplate->name.equals(args[0])) {
            bbPositionEntity(mainPlayer->collider, r->x, r->y + 0.7f, r->z);
            bbResetEntity(mainPlayer->collider);
            UpdateDoors();
            UpdateRooms();
            mainPlayer->currRoom = r;
            return;
        }
    }

    ConsoleMsg::create("Room not found.", 255, 150, 0);
}

}

#if 0
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
        ConsoleMsg::create("HELP - camerafog");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Sets the draw distance of the fog.");
        ConsoleMsg::create("The fog begins generating at 'CameraFogNear' units");
        ConsoleMsg::create("away from the camera and becomes completely opaque");
        ConsoleMsg::create("at 'CameraFogFar' units away from the camera.");
        ConsoleMsg::create("Example: camerafog 20 40");
        ConsoleMsg::create("******************************");
    }
    case "spawnitem": {
        ConsoleMsg::create("HELP - spawnitem");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Spawns an item at the player's location.");
        ConsoleMsg::create("Any name that can appear in your inventory");
        ConsoleMsg::create("is a valid parameter.");
        ConsoleMsg::create("Example: spawnitem key5");
        ConsoleMsg::create("******************************");
    }
    case "spawn": {
        ConsoleMsg::create("HELP - spawn");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Spawns an NPC at the player's location.");
        ConsoleMsg::create("Valid parameters are:");
        ConsoleMsg::create("049 / zombie (049-2) / 096 / 106 / 173 / 513-1");
        ConsoleMsg::create("/ 966 / 1499-1 / guard / mtf");
        ConsoleMsg::create("******************************");
    }
    case "revive", "undead", "resurrect": {
        ConsoleMsg::create("HELP - revive");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Resets the player's death timer after the dying");
        ConsoleMsg::create("animation triggers.");
        ConsoleMsg::create("Does not affect injury, blood loss");
        ConsoleMsg::create("or 008 infection values.");
        ConsoleMsg::create("******************************");
    }
    case "teleport": {
        ConsoleMsg::create("HELP - teleport");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Teleports the player to the first instance");
        ConsoleMsg::create("of the specified room. Any room that appears");
        ConsoleMsg::create("in rooms.ini is a valid parameter.");
        ConsoleMsg::create("******************************");
    }
    case "stopsound", "stfu": {
        ConsoleMsg::create("HELP - stopsound");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Stops all currently playing sounds.");
        ConsoleMsg::create("******************************");
    }
    case "camerapick": {
        ConsoleMsg::create("HELP - camerapick");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Prints the texture name and coordinates of");
        ConsoleMsg::create("the model the camera is pointing at.");
        ConsoleMsg::create("******************************");
    }
    case "weed", "scp-420-j", "420": {
        ConsoleMsg::create("HELP - 420");
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Generates dank memes.");
        ConsoleMsg::create("******************************");

    }
    default: {
        ConsoleMsg::create("There is no help available for that command.", 255, 150, 0);
    }
    }

}
case "camerapick": {
    ConsoleR = 0;
    ConsoleG = 255;
    ConsoleB = 0;
    c = bbCameraPick(mainPlayer->cam, userOptions->screenWidth / 2, userOptions->screenHeight / 2);
    if (c == 0) {
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("No entity picked");
        ConsoleMsg::create("******************************");
    }
    else {
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Picked entity:");
        sf = bbGetSurface(c, 1);
        b = bbGetSurfaceBrush(sf);
        t = bbGetBrushTexture(b, 0);
        texname = StripPath(bbTextureName(t));
        ConsoleMsg::create("Texture name: " + texname);
        ConsoleMsg::create("Coordinates: " + String(bbEntityX(c)) + ", " + String(bbEntityY(c)) + ", " + String(bbEntityZ(c)));
        ConsoleMsg::create("******************************");
        bbFreeTexture(t);
        bbFreeBrush(b);
    }

}
case "hidedistance": {
    HideDistance = (float)(bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")));
    ConsoleMsg::create("Hidedistance set to " + String(HideDistance));

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
case "guh": {
    for (int iterator45 = 0; iterator45 < Room::getListSize(); iterator45++) {
        r = Room::getObject(iterator45);

        if (r->roomTemplate->name.equals("cont_914_1")) {
            //PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7f, EntityZ(r\obj))
            bbPositionEntity(mainPlayer->collider, r->x, r->y + 0.7f, r->z);
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
            ConsoleMsg::create(itt->name + " spawned.");
            it = CreateItem(itt->name, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));
            break;
        }
    }

    if (temp == false) {
        ConsoleMsg::create("Item not found.", 255, 150, 0);
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
    ConsoleMsg::create("Nerd.", 255, 150, 0);

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
        ConsoleMsg::create("WIREFRAME ON");
    }
    else {
        ConsoleMsg::create("WIREFRAME OFF");
    }

    bbWireFrame(WireframeState);

}
case "halloween": {
    HalloweenTex = !HalloweenTex;
    if (HalloweenTex) {
        tex = bbLoadTexture("GFX/npcs/173h.pt", 1);
        bbEntityTexture(Curr173->obj, tex, 0, 0);
        bbFreeTexture(tex);
        ConsoleMsg::create("173 JACK-O-LANTERN ON");
    }
    else {
        tex2 = bbLoadTexture("GFX/npcs/173texture.png", 1);
        bbEntityTexture(Curr173->obj, tex2, 0, 0);
        bbFreeTexture(tex2);
        ConsoleMsg::create("173 JACK-O-LANTERN OFF");
    }

}
case "sanic": {
    mainPlayer->superMan = !mainPlayer->superMan;
    if (mainPlayer->superMan == true) {
        ConsoleMsg::create("GOTTA GO FAST");
    }
    else {
        ConsoleMsg::create("WHOA SLOW DOWN");
    }
}
case "revive", "undead", "resurrect": {
    mainPlayer->dead = false;

    mainPlayer->dropSpeed = -0.1f;
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
case "showfps": {
    userOptions->showFPS = !userOptions->showFPS;
    ConsoleMsg::create("ShowFPS: " + String(userOptions->showFPS));

}
case "stopsound", "stfu": {
    StopSounds();

}
case "camerafog": {
    throw ("TODO: reimplement?");
    //args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    //CameraFogNear = (float)(Left(args, Len(args) - Instr(args, " ")))
    //CameraFogFar = (float)(Right(args, Len(args) - Instr(args, " ")))
    //ConsoleMsg::create("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

}
case "spawn": {
    StrTemp = bbRight(ConsoleInput, ConsoleInput.size() - bbInstr(ConsoleInput, " ")).toLower();
    Console_SpawnNPC(StrTemp);

}
case "infinitestamina", "infstam": {
    throw ("TODO: reimplement?");
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
case Chr($6A) + Chr($6F) + Chr($72) + Chr($67) + Chr($65): {
    throw ("Implement");
}
}
#endif
#include <bbblitz3d.h>
#include <bbmath.h>
#include <iostream>

#include "ConsoleCommands.h"
#include "../../Player/Player.h"
#include "../../Items/Items.h"
#include "../../Map/MapSystem.h"
#include "../../Map/Doors.h"
#include "../../AssetMgmt/Audio.h"
#include "../../Menus/Menu.h"
#include "../../NPCs/NPCs.h"
#include "../../Config/Options.h"
#include "../../AssetMgmt/TextMgmt.h"

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
    commandList.push_back(new Cmd_CameraPick());
    commandList.push_back(new Cmd_DebugHUD());
    commandList.push_back(new Cmd_Wireframe());
    commandList.push_back(new Cmd_Noclip());
    commandList.push_back(new Cmd_GodMode());
    commandList.push_back(new Cmd_Heal());
    commandList.push_back(new Cmd_Kill());
    commandList.push_back(new Cmd_Heal());
    commandList.push_back(new Cmd_Teleport());
    commandList.push_back(new Cmd_SpawnItem());
    commandList.push_back(new Cmd_Omni());
    commandList.push_back(new Cmd_StopSound());
    commandList.push_back(new Cmd_Halloween());
}
void ConsoleCmd::clearCommands() {
    for (int i = 0; i < commandList.size(); i++) {
        delete commandList[i];
    }
    commandList.clear();
}

void Cmd_Help::execute(std::vector<String> args) {
    txtMgmt->setMsg("JORGE", 70 * 8);
    console->msgR = 0;
    console->msgG = 255;
    console->msgB = 255;
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
            for (int i = 0; i < foundCmd->helpDesc.size(); i++) {
                ConsoleMsg::create(" - " + foundCmd->helpDesc[i]);
            }
            if (foundCmd->helpDesc.size() <= 0) {
                ConsoleMsg::create("No help description available for this command.", 255, 0, 0);
            }
        } else {
            ConsoleMsg::create("Command not found.", 255, 0, 0);
        }
    }

}

// Command execute definitions.
void Cmd_Status::execute(std::vector<String> args) {
    console->msgR = 0;
    console->msgG = 255;
    console->msgB = 0;
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

void Cmd_CameraPick::execute(std::vector<String> args) {
    console->msgR = 0;
    console->msgG = 255;
    console->msgB = 0;
    Object* c = bbCameraPick(mainPlayer->cam, userOptions->screenWidth / 2, userOptions->screenHeight / 2);
    if (c == nullptr) {
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("No entity picked");
        ConsoleMsg::create("******************************");
    } else {
        ConsoleMsg::create("******************************");
        ConsoleMsg::create("Picked entity:");
        Surface* sf = bbGetSurface(c->getModel()->getMeshModel(), 1);
        Brush* b = bbGetSurfaceBrush(sf);
        Texture* t = bbGetBrushTexture(b, 0);
        String texname = StripPath(bbTextureName(t));
        ConsoleMsg::create("Texture name: " + texname);
        ConsoleMsg::create("Coordinates: " + String(bbEntityX(c)) + ", " + String(bbEntityY(c)) + ", " + String(bbEntityZ(c)));
        ConsoleMsg::create("******************************");
        bbFreeTexture(t);
        bbFreeBrush(b);
    }
}

void Cmd_DebugHUD::execute(std::vector<String> args) {
    console->debugHUD = !console->debugHUD;
    if (console->debugHUD) {
        ConsoleMsg::create("DebugHUD ON");
    } else {
        ConsoleMsg::create("DebugHUD OFF");
    }
}

void Cmd_Wireframe::execute(std::vector<String> args) {
    console->wireframeState = !console->wireframeState;
    bbWireFrame(console->wireframeState);
    if (console->wireframeState) {
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

void Cmd_NoTarget::execute(std::vector<String> args) {
    console->noTarget = !console->noTarget;
    if (console->noTarget) {
        ConsoleMsg::create("NoTarget ON");
    } else {
        ConsoleMsg::create("NoTarget OFF");
    }
}

void Cmd_Heal::execute(std::vector<String> args) {
    mainPlayer->injuries = 0;
    mainPlayer->bloodloss = 0;
}

void Cmd_Kill::execute(std::vector<String> args) {
    Kill(mainPlayer);

    switch (bbRand(1, 4)) {
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

void Cmd_Revive::execute(std::vector<String> args) {
    if (!mainPlayer->dead) {
        ConsoleMsg::create("Player is not dead.", 255, 0, 0);
        return;
    }

    mainPlayer->dead = false;

    mainPlayer->dropSpeed = -0.1f;
    mainPlayer->camShake = 0;
    mainPlayer->moveSpeed = 0;

    mainPlayer->heartbeatIntensity = 0;

    mainPlayer->lightFlash = 0;
    mainPlayer->blurTimer = 0;

    mainPlayer->fallTimer = 0;

    bbShowEntity(mainPlayer->collider);

    ConsoleMsg::create("Player revived.");
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

void Cmd_SpawnItem::execute(std::vector<String> args) {
    if (args.size() <= 0) {
        ConsoleMsg::create("Please specify an item. (e.g. spawnitem gasmask)", 255, 150, 0);
        return;
    }

    for (int i = 0; i < ItemTemplate::getListSize(); i++) {
        ItemTemplate* itt = ItemTemplate::getObject(i);

        if (itt->name.equals(args[0])) {
            ConsoleMsg::create(itt->invName + " spawned.");
            CreateItem(itt->name, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->collider));
            return;
        }
    }

    ConsoleMsg::create("Item not found.", 255, 150, 0);
}

void Cmd_Omni::execute(std::vector<String> args) {
    Item* it = CreateItem("keycard", bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));
    AssignTag(it, ITEM_TAG_OMNI);
    ConsoleMsg::create("Nerd.", 255, 150, 0);
}

void Cmd_StopSound::execute(std::vector<String> args) {
    StopSounds();
    ConsoleMsg::create("Stopped all sounds.");
}

void Cmd_Halloween::execute(std::vector<String> args) {
    console->halloween173Tex = !console->halloween173Tex;

    if (console->halloween173Tex) {
        Texture* tex = bbLoadTexture("GFX/npcs/173h.pt", 1);
        bbEntityTexture(Curr173->obj, tex);
        bbFreeTexture(tex);
        ConsoleMsg::create("173 JACK-O-LANTERN ON");
    }
    else {
        Texture* tex = bbLoadTexture("GFX/npcs/173texture.png", 1);
        bbEntityTexture(Curr173->obj, tex);
        bbFreeTexture(tex);
        ConsoleMsg::create("173 JACK-O-LANTERN OFF");
    }
}

}

#if 0
//TODO: Overhaul this. Move all of the argument stuff to dedicated functions so this is actually readable/maintainable.
switch (StrTemp.toLower()) {
case "spawndoc": {
    StrTemp = bbRight(console->input, console->input.size() - bbInstr(console->input, " ")).toLower();
    CreatePaper(StrTemp, bbEntityX(mainPlayer->collider), bbEntityY(mainPlayer->cam, true), bbEntityZ(mainPlayer->collider));

}
case "camerafog": {
    throw ("TODO: reimplement?");
    //args$ = Lower(Right(console->input, Len(console->input) - Instr(console->input, " ")))
    //CameraFogNear = (float)(Left(args, Len(args) - Instr(args, " ")))
    //CameraFogFar = (float)(Right(args, Len(args) - Instr(args, " ")))
    //ConsoleMsg::create("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

}
case "spawn": {
    StrTemp = bbRight(console->input, console->input.size() - bbInstr(console->input, " ")).toLower();
    Console_SpawnNPC(StrTemp);

}
case "infinitestamina", "infstam": {
    throw ("TODO: reimplement?");
}
case Chr($6A) + Chr($6F) + Chr($72) + Chr($67) + Chr($65): {
    throw ("Implement");
}
}
#endif
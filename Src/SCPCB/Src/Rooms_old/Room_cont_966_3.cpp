#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Doors.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "../Events.h"
#include "../Player.h"
#include "../NPCs/NPCs.h"
#include "Room_cont_966_3.h"

namespace CBN {

// Functions.
void FillRoom_cont_966_3(Room* r) {
    Door* d = CreateDoor(r->x - 400.f * RoomScale, 0, r->z, -90, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);
    //: d\buttons[0] = False
    d = CreateDoor(r->x, 0, r->z - 480.f * RoomScale, 180, r, false, DOOR_TYPE_DEF, r->roomTemplate->name);

    //PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.f * RoomScale, True)
    //PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.f * RoomScale, True)

    SecurityCam* sc = CreateSecurityCam(r->x-312.f * RoomScale, r->y + 414*RoomScale, r->z + 656*RoomScale, r);
    sc->angle = 225;
    sc->turn = 45;
    bbTurnEntity(sc->cameraObj, 20, 0, 0);
    //sc\followPlayer = True
    sc->id = 9;

    r->objects[0] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[0], r->x, 0.5f, r->z + 512.f * RoomScale, true);

    r->objects[1] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[1], r->x + 64.f * RoomScale, 0.5f, r->z - 640.f * RoomScale, true);

    r->objects[2] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[2], r->x - 608.f * RoomScale, 0.5f, r->z, true);

    r->objects[3] = bbCreatePivot(r->obj);
    bbPositionEntity(r->objects[3], r->x + 320.f * RoomScale, 0.5f, r->z + 704.f * RoomScale, true);

    Item* it = CreateItem("nvgoggles", r->x + 320.f * RoomScale, 0.5f, r->z + 704.f * RoomScale);
    bbEntityParent(it->collider, r->obj);
}

void UpdateEvent_cont_966_3(Event* e) {
    if (mainPlayer->currRoom == e->room) {
        if (!e->loaded) {
            CreateNPC(NPCtype966, bbEntityX(e->room->objects[0],true), bbEntityY(e->room->objects[0],true), bbEntityZ(e->room->objects[0],true));
            CreateNPC(NPCtype966, bbEntityX(e->room->objects[1],true), bbEntityY(e->room->objects[1],true), bbEntityZ(e->room->objects[1],true));
            CreateNPC(NPCtype966, bbEntityX(e->room->objects[2],true), bbEntityY(e->room->objects[2],true), bbEntityZ(e->room->objects[2],true));

            e->loaded = true;
        }
        RemoveEvent(e);
    }
}

}

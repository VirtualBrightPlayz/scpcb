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
#include "Room_hll_dirty_3.h"

namespace CBN {

// Functions.
void FillRoom_hll_dirty_3(Room* r) {
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

    for (int iterator183 = 0; iterator183 < Room::getListSize(); iterator183++) {
        r2 = Room::getObject(iterator183);

        if (r2->roomTemplate->name.equals(r->roomTemplate->name) && r2 != r) {
            r->objects[0] = bbCopyMeshModelEntity((MeshModel*)r2->objects[0],r->obj);
            break;
        }
    }
    //If (r\objects[0]=0) Then r\objects[0] = LoadMesh("GFX/Map/room3z2_hb.b3d",r\obj)
    //EntityPickMode(r\objects[0],2)
    //EntityType(r\objects[0],HIT_MAP)
    //EntityAlpha(r\objects[0],0.0)
}

}

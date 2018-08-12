#include <bbblitz3d.h>

#include "../MapSystem.h"
#include "../Items/Items.h"
#include "../Decals.h"
#include "Room_hll_dirty_3.h"

namespace CBN {

// Functions.
void FillRoom_hll_dirty_3(Room* r) {
    for (int iterator183 = 0; iterator183 < Room::getListSize(); iterator183++) {
        Room* r2 = Room::getObject(iterator183);

        if (r2->roomTemplate->name.equals(r->roomTemplate->name) && r2 != r) {
            r->objects[0] = bbCopyMeshModelEntity((MeshModel*)r2->objects[0],r->obj);
            break;
        }
    }
    //If (r\objects[0]=0) Then r\objects[0] = LoadMesh("GFX/Map/room3z2_hb.b3d",r\obj)
    //EntityPickMode(r\objects[0],2)
    //EntityType(r\objects[0],HIT_MAP)
    //EntityAlpha(r\objects[0],0.f)
}

}

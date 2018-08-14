#include <StringType.h>
#include <bbblitz3d.h>
#include <bbfilesystem.h>

#include "Skybox.h"
#include "MapSystem.h"
#include "../Player/Player.h"
#include "../GameMain.h"

namespace CBN {

// Functions.
MeshModel* sky_CreateSky(const String& filename, Object* parent) {
    String fname;
    String direction;
    float x;
    float y;
    float z;
    float u;
    float v;
    int vert;

    MeshModel* sky = bbCreateMesh(parent);
    for (int face = 1; face <= 6; face++) {
        switch (face) {
            case 1: {
                direction = "_back";
            } break;
            case 2: {
                direction = "_left";
            } break;
            case 3: {
                direction = "_front";
            } break;
            case 4: {
                direction = "_right";
            } break;
            case 5: {
                direction = "_up";
            } break;
            case 6: {
                direction = "_down";
            } break;
        }
        fname = filename + direction + ".jpg";
        if (bbFileType(fname)==1) {
            Brush* b = bbLoadBrush(fname,0b110001);
            Surface* s = bbCreateSurface(sky,b);

            //TODO: finish and remove Data
            switch (face) {
                case 1: {

                } break;
                case 2: {

                } break;
                case 3: {

                } break;
                case 4: {

                } break;
                case 5: {

                } break;
                case 6: {

                } break;
            }
            bbAddTriangle(s,0,1,2);
            bbAddTriangle(s,0,2,3);
            bbFreeBrush(b);
        }
    }
    bbFlipMesh(sky);
    bbEntityFX(sky,1+8);
    bbEntityOrder(sky,1000);
    return sky;
}

void UpdateSky() {
    bbPositionEntity(Sky, bbEntityX(mainPlayer->cam),bbEntityY(mainPlayer->cam),bbEntityZ(mainPlayer->cam), true);
}

void Update1499Sky() {
    bbPositionEntity(NTF_1499Sky, bbEntityX(mainPlayer->cam),bbEntityY(mainPlayer->cam),bbEntityZ(mainPlayer->cam), true);
}

}

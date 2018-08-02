#include "Skybox.h"
#include "include.h"

namespace CBN {

// Functions.
int sky_CreateSky(String filename, int parent = 0) {
    int sky;
    String fname;
    String direction;
    float x;
    float y;
    float z;
    float u;
    float v;
    int b;
    int s;
    int vert;

    sky = bbCreateMesh(parent);
    int face;
    for (face = 1; face <= 6; face++) {
        switch (face) {
            case 1: {
                direction = "_back";
            }
            case 2: {
                direction = "_left";
            }
            case 3: {
                direction = "_front";
            }
            case 4: {
                direction = "_right";
            }
            case 5: {
                direction = "_up";
            }
            case 6: {
                direction = "_down";
            }
        }
        fname = filename + direction + ".jpg";
        if (bbFileType(fname)==1) {
            b = bbLoadBrush(fname,%110001);
            s = bbCreateSurface(sky,b);

            //TODO: finish and remove Data
            switch (face) {
                case 1: {

                }
                case 2: {

                }
                case 3: {

                }
                case 4: {

                }
                case 5: {

                }
                case 6: {

                }
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

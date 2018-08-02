#include "Dreamfilter.h"
#include "include.h"

namespace CBN {

// Globals.
int ark_blur_image;
int ark_blur_texture;
int ark_sw;
int ark_sh;
int ark_blur_cam;

// Functions.
void CreateBlurImage() {
    //Create blur mainPlayer\cam
    int cam = bbCreateCamera();
    bbCameraProjMode(cam,2);
    bbCameraZoom(cam,0.1);
    bbCameraClsMode(cam, 0, 0);
    bbCameraRange(cam, 0.1, 1.5);
    bbMoveEntity(cam, 0, 0, 10000);
    ark_blur_cam = cam;

    ark_sw = userOptions->screenWidth;
    ark_sh = userOptions->screenHeight;
    bbCameraViewport(cam,0,0,ark_sw,ark_sh);

    //Create sprite
    int spr = bbCreateMesh(cam);
    int sf = bbCreateSurface(spr);
    bbAddVertex(sf, -1, 1, 0, 0, 0);
    bbAddVertex(sf, 1, 1, 0, 1, 0);
    bbAddVertex(sf, -1, -1, 0, 0, 1);
    bbAddVertex(sf, 1, -1, 0, 1, 1);
    bbAddTriangle(sf, 0, 1, 2);
    bbAddTriangle(sf, 3, 2, 1);
    bbEntityFX(spr, 17);
    bbScaleEntity(spr, 2048.0 / Float(ark_sw), 2048.0 / Float(ark_sw), 1);
    bbPositionEntity(spr, 0, 0, 1.0001);
    bbEntityOrder(spr, -100000);
    bbEntityBlend(spr, 1);
    ark_blur_image = spr;

    //Create blur texture
    ark_blur_texture = bbCreateTexture(2048, 2048, 256);
    bbEntityTexture(spr, ark_blur_texture);
}

void UpdateBlur(float power) {

    bbEntityAlpha(ark_blur_image, power);

    //CopyRect(ark_sw / 2 - 1024, ark_sh / 2 - 1024, 2048, 2048, 0, 0, BackBuffer(), TextureBuffer(ark_blur_texture))
    bbCopyRect(0, 0, userOptions->screenWidth, userOptions->screenHeight, Int(1024.0 - userOptions->screenWidth / 2), Int(1024.0 - userOptions->screenHeight / 2), bbBackBuffer(), bbTextureBuffer(ark_blur_texture));

}

}

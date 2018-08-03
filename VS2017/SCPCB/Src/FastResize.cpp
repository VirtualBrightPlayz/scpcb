#include "FastResize.h"
#include "include.h"

namespace CBN {

// Globals.
int fresize_image;
int fresize_texture;
int fresize_texture2;
int fresize_cam;

// Functions.
int ResizeImage2(int image, int width, int height) {
    int img = bbCreateImage(width,height);

    int oldWidth = bbImageWidth(image);
    int oldHeight = bbImageHeight(image);
    bbCopyRect(0,0,oldWidth,oldHeight,1024-oldWidth/2,1024-oldHeight/2,bbImageBuffer(image),bbTextureBuffer(fresize_texture));
    bbSetBuffer(bbBackBuffer());
    ScaleRender(0,0,2048.0 / (float)(userOptions->screenWidth) * (float)(width) / (float)(oldWidth), 2048.0 / (float)(userOptions->screenWidth) * (float)(height) / (float)(oldHeight));
    //might want to replace (float)(userOptions\screenWidth) with Max(userOptions\screenWidth,userOptions\screenHeight) if portrait sizes cause issues
    //everyone uses landscape so it's probably a non-issue
    bbCopyRect(userOptions->screenWidth/2-width/2,userOptions->screenHeight/2-height/2,width,height,0,0,bbBackBuffer(),bbImageBuffer(img));

    bbFreeImage(image);
    return img;
}

void ScaleRender(float x, float y, float hscale = 1.0, float vscale = 1.0) {
    if (mainPlayer!=nullptr) {
        bbHideEntity(mainPlayer->cam);
    }
    bbWireFrame(0);
    bbShowEntity(fresize_image);
    bbScaleEntity(fresize_image,hscale,vscale,1.0);
    bbPositionEntity(fresize_image, x, y, 1.0001);
    bbShowEntity(fresize_cam);
    bbRenderWorld();
    bbHideEntity(fresize_cam);
    bbHideEntity(fresize_image);
    bbWireFrame(WireframeState);
    if (mainPlayer!=nullptr) {
        bbShowEntity(mainPlayer->cam);
    }
}

void InitFastResize() {
    //Create Camera
    int cam = bbCreateCamera();
    bbCameraProjMode(cam, 2);
    bbCameraZoom(cam, 0.1);
    bbCameraClsMode(cam, 0, 0);
    bbCameraRange(cam, 0.1, 1.5);
    bbMoveEntity(cam, 0, 0, -10000);

    fresize_cam = cam;

    //ark_sw = GraphicsWidth()
    //ark_sh = GraphicsHeight()

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
    bbScaleEntity(spr, 2048.0 / (float)(userOptions->screenWidth), 2048.0 / (float)(userOptions->screenHeight), 1);
    bbPositionEntity(spr, 0, 0, 1.0001);
    bbEntityOrder(spr, -100001);
    bbEntityBlend(spr, 1);
    fresize_image = spr;

    //Create texture
    fresize_texture = bbCreateTexture(2048, 2048, 1+256);
    fresize_texture2 = bbCreateTexture(2048, 2048, 1+256);
    bbTextureBlend(fresize_texture2,3);
    bbSetBuffer(bbTextureBuffer(fresize_texture2));
    bbClsColor(0,0,0);
    bbCls();
    bbSetBuffer(bbBackBuffer());
    //TextureAnisotropy(fresize_texture)
    bbEntityTexture(spr, fresize_texture,0,0);
    bbEntityTexture(spr, fresize_texture2,0,1);

    bbHideEntity(fresize_cam);
}

}

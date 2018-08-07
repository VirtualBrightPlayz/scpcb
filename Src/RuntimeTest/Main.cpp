#include <Windows.h>
#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>
#include <bbinput.h>
#include <bbmath.h>
#include <bbstring.h>
#include <iostream>
#include <vector>
#include <fstream>

#ifndef _WIN64
static bool isUsing4GBPatch() {
    char filename[128];
    GetModuleFileNameA(GetModuleHandle(NULL),filename,128);
    String f = filename;
    if (f.findLast('\\')!=-1) {
        f = f.substr(f.findLast('\\')+1);
    } else if (f.findLast('/') != -1) {
        f = f.substr(f.findLast('/')+1);
    }
    
    union intUnion {
        int i;
        char c[4];
    };

    std::ifstream stream; stream.open(f.cstr(),std::ios_base::in|std::ios_base::binary);

    //look for e_lfanew
    stream.seekg(0x3C);
    intUnion offset; offset.i=0; stream.read(offset.c,4);
    //look for characteristics
    offset.i+=0x16; stream.seekg(offset.i);
    intUnion flags; flags.i=0; stream.read(flags.c,2);
    
    stream.close();

    if ((flags.i&0x20) != 0) {
        return true; //used 4gb patch!
    }
    return false;
}
#endif

int main() {
#ifndef _WIN64
    if (isUsing4GBPatch()) {
        MessageBoxA(NULL,"Running 4GB patch - closing","Error",0);
        return 0;
    }
#endif

    bbruntime_create(GetModuleHandle(NULL));

    bbGraphics3D(1280, 720, 0, 2);

    bbAppTitle(String("     Test  thing     ").trim(),"");

    String test = "this is a  test";
    std::vector<String> list = test.split(" ", true); // Exception here.
    for (int i = 0; i < list.size(); i++) {
	    std::cout << list[i] << "\n";
    }

    bbAmbientLight(20,20,50);

    MeshModel* model = bbLoadMesh("173_2.b3d");
    bbScaleEntity(model,0.1,0.1,0.1,true);
    Camera* camera = bbCreateCamera();
    Light* light = bbCreateLight(1);

    bbHidePointer();

    bbLightColor(light,100,255,100);

    bbImage* cursor = bbLoadImage("cursor.png");

    bbSetBuffer(bbBackBuffer());
    float cnt = 0;
    while (!bbKeyHit(1)) {
        cnt+=0.01f;
        bbPositionEntity(camera, bbSin(cnt)*6.0f, 2.0, bbCos(cnt)*6.0f, true);
        bbPointEntity(camera, model, 0.0f);

        bbPositionEntity(light, bbSin(cnt*10.0f)*6.0f, bbSin(cnt*30.0f)*6.0f, bbCos(cnt*10.0f)*6.0f, true);
        bbPointEntity(light, model, 0.0f);

        bbCameraZoom(camera, 1.0f+bbSin(cnt*6.0f)*0.5f);

        bbRenderWorld(1.0f);
        bbDrawImage(cursor, bbMouseX(), bbMouseY(), 0);
        bbFlip(0);
    }

    bbFreeEntity(camera);
    bbFreeEntity(model);
    bbFreeEntity(light);

    bbFreeImage(cursor);

    bbEndGraphics();

    bbruntime_destroy();

    return 0;
}

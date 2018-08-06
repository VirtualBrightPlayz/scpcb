#include <Windows.h>
#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>
#include <bbinput.h>
#include <bbmath.h>
#include <bbstring.h>
#include <iostream>
#include <vector>

int main() {
    bbruntime_create(GetModuleHandle(NULL));

    bbGraphics3D(1280, 720, 0, 2);

    bbAppTitle(String("     Test  thing     ").trim(),"");

	String test = "this is a test";
	std::vector<String> list = test.split(" "); // Exception here.
	for (int i = 0; i < list.size(); i++) {
		std::cout << list[i];
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

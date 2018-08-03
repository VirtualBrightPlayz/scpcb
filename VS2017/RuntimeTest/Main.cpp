#include <Windows.h>
#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>

int main() {
    bbruntime_create(GetModuleHandle(NULL));

    bbGraphics3D(1280, 720, 0, 2);

    Entity* cube = bbCreateCube(nullptr);
    Entity* camera = bbCreateCamera(nullptr);
    bbPositionEntity(camera, 5.0f, 2.0f, -3.0f, false);
    bbPointEntity(camera, cube, 0.0f);

    bbRenderWorld(1.0f);
    bbFlip(0);

    bbDelay(10000);

    bbEndGraphics();

    bbruntime_destroy();

    return 0;
}

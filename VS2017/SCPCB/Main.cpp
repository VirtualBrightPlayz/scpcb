#include <Windows.h>
#include <bbruntime.h>
#include <bbgraphics.h>

int main() {
    bbruntime_create(GetModuleHandle(NULL));

    bbGraphics3D(1280,720,0,2);

    bbDelay(10000);

    bbEndGraphics();

    bbruntime_destroy();

    return 0;
}

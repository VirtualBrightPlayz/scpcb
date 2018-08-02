#include <Windows.h>
#include <bbruntime.h>
#include <bbgraphics.h>
#include <bbblitz3d.h>

#include "Src/include.h"

int main() {
    bbruntime_create(GetModuleHandle(nullptr));

    CBN::EntryPoint();

    bbruntime_destroy();

    return 0;
}

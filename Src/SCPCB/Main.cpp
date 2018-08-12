#include <Windows.h>
#include <bbruntime.h>
#include <fstream>

#include "Src/GameMain.h"

#ifndef _WIN64
static bool isUsing4GBPatch() {
    char filename[128];
    GetModuleFileNameA(GetModuleHandle(NULL), filename, 128);
    String f = filename;
    if (f.findLast('\\') != -1) {
        f = f.substr(f.findLast('\\') + 1);
    }
    else if (f.findLast('/') != -1) {
        f = f.substr(f.findLast('/') + 1);
    }

    union intUnion {
        int i;
        char c[4];
    };

    std::ifstream stream; stream.open(f.cstr(), std::ios_base::in | std::ios_base::binary);

    //look for e_lfanew
    stream.seekg(0x3C);
    intUnion offset; offset.i = 0; stream.read(offset.c, 4);
    //look for characteristics
    offset.i += 0x16; stream.seekg(offset.i);
    intUnion flags; flags.i = 0; stream.read(flags.c, 2);

    stream.close();

    if ((flags.i & 0x20) != 0) {
        return true; //used 4gb patch!
    }
    return false;
}
#endif

int main() {
#ifndef _WIN64
    if (isUsing4GBPatch()) {
        MessageBoxA(NULL, "Running 4GB patch - closing", "Error", 0);
        return 0;
    }
#endif

    bbruntime_create(GetModuleHandle(nullptr));

    CBN::EntryPoint();

    bbruntime_destroy();

    return 0;
}

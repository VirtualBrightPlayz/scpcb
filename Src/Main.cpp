#include <Init/Init.h>
#include <iostream>
#include <Graphics/Graphics.h>

#include "Wrap/Graphics.h"

int PGE::Main() {
    CBN::Graphics gfx = CBN::Graphics::create("The Test", 1280, 720, false);
    
    while (true) {
        gfx->swap(false);
    }
//    std::cout << ".....aaAAAAAAAAHHHH-";
    
    return 0;
}

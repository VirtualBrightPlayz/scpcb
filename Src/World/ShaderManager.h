#ifndef ShaderManager_H_INCLUDED
#define ShaderManager_H_INCLUDED

#include "../Wrap/Shader.h"

class ShaderManager {
private:
    Shader spriteShader;

public:
    ShaderManager(Graphics gfx);

    Shader getSpriteShader();
};

#endif // ShaderManager_H_INCLUDED

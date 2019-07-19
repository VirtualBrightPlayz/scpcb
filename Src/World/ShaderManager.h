#ifndef ShaderManager_H_INCLUDED
#define ShaderManager_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include "../Graphics/Camera.h"

class ShaderManager {
private:
    PGE::Shader* spriteShader;
    PGE::Shader::Constant* spriteShaderView;

public:
    ShaderManager(PGE::Graphics* gfx, const Camera* cam);

    void update(const Camera* cam);

    PGE::Shader* getSpriteShader() const;
};

#endif // ShaderManager_H_INCLUDED

#ifndef ShaderManager_H_INCLUDED
#define ShaderManager_H_INCLUDED

#include "../Wrap/Shader.h"
#include "../Graphics/Camera.h"

class ShaderManager {
private:
    Shader spriteShader;
    PGE::Shader::Constant* spriteShaderView;

    Shader uiShader;

public:
    ShaderManager(const Graphics& gfx, const Camera* cam);

    void update(const Camera* cam);

    const Shader& getSpriteShader() const;
    const Shader& getUIShader() const;
};

#endif // ShaderManager_H_INCLUDED

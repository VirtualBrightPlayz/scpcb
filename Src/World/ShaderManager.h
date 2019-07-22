#ifndef ShaderManager_H_INCLUDED
#define ShaderManager_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include "../Graphics/Camera.h"

class Config;

class ShaderManager {
private:
    PGE::Shader* spriteShader;
    PGE::Shader::Constant* spriteShaderView;
    
    PGE::Shader* fontShader;
    
    PGE::Matrix4x4f orthoMat;

public:
    ShaderManager(PGE::Graphics* gfx, Config* con, const Camera* cam);

    void update(const Camera* cam);

    PGE::Shader* getSpriteShader() const;
    PGE::Shader* getFontShader() const;
    
    void updateOrthoMat(float aspectRatio);
    PGE::Matrix4x4f getOrthoMat() const;
};

#endif // ShaderManager_H_INCLUDED

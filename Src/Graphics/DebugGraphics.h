#ifndef DEBUGGRAPHICS_H_INCLUDED
#define DEBUGGRAPHICS_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Material/Material.h>
#include <Shader/Shader.h>
#include <Mesh/Mesh.h>
#include <Color/Color.h>
#include <Math/Line.h>
#include <Math/Matrix.h>

class DebugGraphics {
    private:
        PGE::Mesh* mesh;
        PGE::Material* material;
        PGE::Shader* shader;

        PGE::Shader::Constant* colorConstant;
        PGE::Shader::Constant* viewMatrixConstant;
        PGE::Shader::Constant* projectionMatrixConstant;

    public:
        DebugGraphics(PGE::Graphics* gfx);
        ~DebugGraphics();

        void setViewMatrix(const PGE::Matrix4x4f& viewMatrix);
        void setProjectionMatrix(const PGE::Matrix4x4f& projectionMatrix);

        void draw3DLine(const PGE::Line3f& line, const PGE::Color& color, float thickness);
};

#endif // DEBUGGRAPHICS_H_INCLUDED

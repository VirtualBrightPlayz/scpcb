#ifndef Camera_H_INCLUDED
#define Camera_H_INCLUDED

#include <Math/Matrix.h>
#include <Graphics/Graphics.h>
#include <Shader/Shader.h>

class GraphicsResources;

class Camera {
    private:
        GraphicsResources* gfxRes;
    
        float xAngle;
        float yAngle;
        // The range of the the yAngle field before it is clamped.
        float yAngleLimit;
        float tilt;
    
        float fov;
    
        bool needsMatrixUpdate;
    
        PGE::Vector3f position;
        PGE::Vector3f lookAt;
        PGE::Vector3f upDir;
    
        PGE::Matrix4x4f viewMatrix;
        PGE::Matrix4x4f projectionMatrix;
        PGE::Matrix4x4f rotation;
    
        std::vector<PGE::Shader*> shaders;

    public:
        Camera(GraphicsResources* gr, float aspectRatio, float fov);
        Camera(GraphicsResources* gr, float aspectRatio);
        ~Camera();
    
        void addShader(PGE::FileName fn);
    
        void update();
    
        void setPosition(const PGE::Vector3f pos);
        void setTilt(float rad);
        void addAngle(float xAngle, float yAngle);

        const PGE::Matrix4x4f& getViewMatrix() const;
        const PGE::Matrix4x4f& getProjectionMatrix() const;
        const PGE::Matrix4x4f& getRotationMatrix() const;
    
        float getXAngle() const;
        float getYAngle() const;
};

#endif // Camera_H_INCLUDED

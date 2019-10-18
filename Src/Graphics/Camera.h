#ifndef Camera_H_INCLUDED
#define Camera_H_INCLUDED

#include <Math/Matrix.h>
#include <Graphics/Graphics.h>
#include <Shader/Shader.h>

class GraphicsResources;

class Camera {
    private:
        GraphicsResources* gfxRes;

        float yawAngle;
        float pitchAngle;
        // The range of the the pitchAngle field before it is clamped.
        float pitchAngleLimit;
        float tilt;

        float nearPlaneZ;
        float farPlaneZ;
        int width;
        int height;
        float fov;
        // Perspective or Orthographic matrix.
        bool orthographicProj;

        bool needsViewUpdate;
        bool needsProjUpdate;

        PGE::Vector3f position;
        PGE::Vector3f lookAt;
        PGE::Vector3f upDir;

        PGE::Matrix4x4f viewMatrix;
        PGE::Matrix4x4f projectionMatrix;
        PGE::Matrix4x4f rotation;

        float getAspectRatio() const;

    public:
        Camera(GraphicsResources* gr, int w, int h, float fov, float nearZ = 0.01f, float farZ = 30.f, bool orthographic = false);
        Camera(GraphicsResources* gr, int w, int h);

        void update();

        void setPosition(const PGE::Vector3f pos);
        void setTilt(float rad);
        void addAngle(float yawAngle, float pitchAngle);

        const PGE::Matrix4x4f& getViewMatrix() const;
        const PGE::Matrix4x4f& getProjectionMatrix() const;
        const PGE::Matrix4x4f& getRotationMatrix() const;

        float getYawAngle() const;
        float getPitchAngle() const;
};

#endif // Camera_H_INCLUDED

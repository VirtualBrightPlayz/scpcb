#ifndef Camera_H_INCLUDED
#define Camera_H_INCLUDED

#include <Math/Matrix.h>
#include "../Wrap/Graphics.h"

class Camera {
private:
    float xAngle;
    float yAngle;
    // The range of the the yAngle field before it is clamped.
    float yAngleLimit;
    float tilt;
    
    float fov;
    
    PGE::Vector3f position;
    PGE::Vector3f lookAt;
    PGE::Vector3f upDir;
    
    PGE::Matrix4x4f viewMatrix;
    PGE::Matrix4x4f projectionMatrix;
    PGE::Matrix4x4f rotation;

    Graphics gfx;

public:
    Camera(const Graphics& gfx, float aspectRatio, float fov);
    Camera(const Graphics& gfx, float aspectRatio);
    
    void update();
    
    void setPosition(const PGE::Vector3f pos);
    void setTilt(float rad);
    void addAngle(float xAngle, float yAngle);

    const PGE::Matrix4x4f& getViewMatrix() const;
    const PGE::Matrix4x4f& getProjectionMatrix() const;
    const PGE::Matrix4x4f& getRotationMatrix() const;
};

#endif // Camera_H_INCLUDED

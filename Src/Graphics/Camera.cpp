#include <math.h>
#include <Math/Rectangle.h>

#include "Camera.h"
#include "../Utils/MathUtil.h"

Camera::Camera(const Graphics& gfx, float aspectRatio, float fov) : gfx(gfx) {
    position = PGE::Vector3f(0.0f, 0.0f, 0.0f);
    lookAt = PGE::Vector3f(0.0f, 0.0f, 1.0f);
    upDir = PGE::Vector3f(0.0f, 1.0f, 0.0f);

    viewMatrix = PGE::Matrix4x4f::constructViewMat(position, lookAt, upDir);
    
    xAngle = 0.f;
    yAngle = 0.f;
    yAngleLimit = 3.14f * 0.5f;
    tilt = 0.f;

    float nearZ = 1.f;
    float farZ = 25.f;
    this->fov = fov;
    projectionMatrix = PGE::Matrix4x4f::constructProjectionMat(fov, aspectRatio, nearZ, farZ);
    
    rotation = PGE::Matrix4x4f::identity;
}

Camera::Camera(const Graphics& gfx, float aspectRatio) : Camera(gfx, aspectRatio, MathUtil::degToRad(90.0f)) { }

void Camera::update() {
    rotation = PGE::Matrix4x4f::constructWorldMat(PGE::Vector3f(0.f, 0.f, 0.f), PGE::Vector3f(1.f, 1.f, 1.f), PGE::Vector3f(-yAngle, xAngle, tilt));
    
    viewMatrix = PGE::Matrix4x4f::constructViewMat(position, rotation.transform(lookAt), rotation.transform(upDir));
}

void Camera::setPosition(const PGE::Vector3f pos) {
    position = pos;
}

void Camera::setTilt(float rad) {
    tilt = rad;
}

void Camera::addAngle(float x, float y) {
    xAngle += x;
    yAngle -= y;
    
    if (yAngle< -yAngleLimit) {
        yAngle = -yAngleLimit;
    } else if (yAngle > yAngleLimit) {
        yAngle = yAngleLimit;
    }
}

const PGE::Matrix4x4f& Camera::getViewMatrix() const {
    return viewMatrix;
}

const PGE::Matrix4x4f& Camera::getProjectionMatrix() const {
    return projectionMatrix;
}

const PGE::Matrix4x4f& Camera::getRotationMatrix() const {
    return rotation;
}

float Camera::getXAngle() const {
    return xAngle;
}

float Camera::getYAngle() const {
    return yAngle;
}

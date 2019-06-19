#include <Math/Rectangle.h>
#include "Camera.h"
#include "../Utils/MathUtil.h"

Camera::Camera(const Graphics& gfx, float aspectRatio) : gfx(gfx) {
    PGE::Vector3f position = PGE::Vector3f(0.0f, 0.0f, 0.0f);
    PGE::Vector3f lookAt = PGE::Vector3f(0.0f, 0.0f, 1.0f);
    PGE::Vector3f upDir = PGE::Vector3f(0.0f, 1.0f, 0.0f);

    viewMatrix = PGE::Matrix4x4f::constructViewMat(position, lookAt, upDir);

    float farClippingPlane = 25.f;
    float nearClippingPlane = 1.0f;

    projectionMatrix = PGE::Matrix4x4f::constructProjectionMat(MathUtil::degToRad(90.0f), aspectRatio, nearClippingPlane, farClippingPlane);
}

const PGE::Matrix4x4f& Camera::getViewMatrix() const {
    return viewMatrix;
}

const PGE::Matrix4x4f& Camera::getProjectionMatrix() const {
    return projectionMatrix;
}

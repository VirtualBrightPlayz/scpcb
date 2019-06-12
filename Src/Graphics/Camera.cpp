#include <Math/Rectangle.h>
#include "Camera.h"

Camera::Camera(Graphics& gfx) : gfx(gfx) {
    PGE::Vector3f position = PGE::Vector3f::zero;
    PGE::Vector3f lookAt = PGE::Vector3f(0.0f, 0.0f, 1.0f);
    PGE::Vector3f upDir = PGE::Vector3f(0.0f, 1.0f, 0.0f);

    viewMatrix = PGE::Matrix4x4f::constructViewMat(position, lookAt, upDir);

    float aspectRatio = (float)gfx->getWindow()->getWidth() / gfx->getWindow()->getHeight();
    float farClippingPlane = 10.f;
    float nearClippingPlane = 0.0f;

    projectionMatrix = PGE::Matrix4x4f::identity;
//    projectionMatrix.elements[0][0] = -1.0f / aspectRatio;
//    projectionMatrix.elements[1][1] = 1.f;
//    projectionMatrix.elements[2][2] = farClippingPlane / (nearClippingPlane - farClippingPlane);
//    projectionMatrix.elements[2][3] = -1.f;
//    projectionMatrix.elements[3][2] = (nearClippingPlane * farClippingPlane / (nearClippingPlane - farClippingPlane));
}

const PGE::Matrix4x4f& Camera::getViewMatrix() const {
    return viewMatrix;
}

const PGE::Matrix4x4f& Camera::getProjectionMatrix() const {
    return projectionMatrix;
}

#include "VRManager.h"

#include <iostream>

PGE::Matrix4x4f vrMatrixToNative(vr::HmdMatrix44_t vrm) {
    return PGE::Matrix4x4f(
        vrm.m[0][0], vrm.m[1][0], vrm.m[2][0], vrm.m[3][0],
        vrm.m[0][1], vrm.m[1][1], vrm.m[2][1], vrm.m[3][1],
        vrm.m[0][2], vrm.m[1][2], vrm.m[2][2], vrm.m[3][2],
        vrm.m[0][3], vrm.m[1][3], vrm.m[2][3], vrm.m[3][3]
    );
}

VRManager::VRManager(Config* config) {
    if (!vr::VR_IsHmdPresent()) {
        throw new std::runtime_error("No HMD found.");
    }
    if (!vr::VR_IsRuntimeInstalled()) {
        throw new std::runtime_error("Open VR runtime not installed");
    }
    vr::EVRInitError e;
    vrSystem = vr::VR_Init(&e, vr::VRApplication_Scene);
    if (vrSystem == nullptr || e != vr::VRInitError_None) {
        throw std::runtime_error("VR failed to initialize! " + e);
    }
    unsigned int width;
    unsigned int height;
    vrSystem->GetRecommendedRenderTargetSize(&width, &height);
    std::cout << "OpenVR recommends a render size of " << width << "x" << height << std::endl;
    config->setResolution(width, height);

    leftProjectionMatrix = vrMatrixToNative(vrSystem->GetProjectionMatrix(vr::Eye_Left, 0.01f, 30.f));
    rightProjectionMatrix = vrMatrixToNative(vrSystem->GetProjectionMatrix(vr::Eye_Right, 0.01f, 30.f));

    tdp[vr::k_unMaxTrackedDeviceCount] = {0};
    texture = nullptr;
}

VRManager::~VRManager() {
    vr::VR_Shutdown();
}

void VRManager::update(Camera* cam) {
    vr::VRCompositor()->WaitGetPoses(tdp, vr::k_unMaxTrackedDeviceCount, nullptr, 0);
    for (int i = 0; i < vr::k_unMaxTrackedDeviceCount; i++) {
        if (tdp[i].bDeviceIsConnected) {
            //std::cout << vrSystem->GetTrackedDeviceClass(i) << std::endl;
            if (tdp[i].bPoseIsValid) {
                vr::HmdMatrix34_t mat = tdp->mDeviceToAbsoluteTracking;
                PGE::Matrix4x4f pmat = PGE::Matrix4x4f(
                    mat.m[0][0], mat.m[1][0], mat.m[2][0], 0,
                    mat.m[0][1], mat.m[1][1], mat.m[2][1], 0,
                    mat.m[0][2], mat.m[1][2], mat.m[2][2], 0,
                    mat.m[0][3], mat.m[1][3], mat.m[2][3], 1
                );
                //system("cls");
                //std::cout << std::endl << std::endl << std::endl << "Matrix!!!!" << std::endl <<
                //    mat.m[0][0] << std::endl << mat.m[1][0] << std::endl << mat.m[2][0] << std::endl << std::endl << std::endl << "1" << std::endl <<
                //    mat.m[0][1] << std::endl << mat.m[1][1] << std::endl << mat.m[2][1] << std::endl << std::endl << std::endl << "2" << std::endl <<
                //    mat.m[0][2] << std::endl << mat.m[1][2] << std::endl << mat.m[2][2] << std::endl << std::endl << std::endl << "3" << std::endl <<
                //    mat.m[0][3] << std::endl << mat.m[1][3] << std::endl << mat.m[2][3] << std::endl << std::endl << std::endl;
                // Inverted looking in both directions, pls fix
                cam->position = PGE::Vector3f(mat.m[0][3], mat.m[1][3], mat.m[2][3]).multiply(25.f);
                PGE::Vector3f oldUp = PGE::Vector3f(-mat.m[0][1], mat.m[1][1], mat.m[2][1]);
                PGE::Vector3f oldForward = PGE::Vector3f(-mat.m[0][2], mat.m[1][2], mat.m[2][2]);
                PGE::Vector3f newForward = PGE::Vector3f(-mat.m[0][2], -mat.m[1][2], mat.m[2][2]);
                PGE::Vector3f newUp = oldUp.crossProduct(newForward).crossProduct(newForward).normalize();
                cam->setUpVector(oldUp);
                cam->setLookAt(oldForward);
            }
        }
    }
}

void VRManager::createTexture(PGE::Graphics* graphics, Config* config) {
    if (texture != nullptr) {
        throw std::runtime_error("TEXTURE HAS ALREADY BEEN CREATED!");
    } else {
        PGE::Texture::create(graphics, config->getWidth(), config->getHeight(), true, new uint8_t[1000 * 1000 * 4], PGE::Texture::FORMAT::RGBA32);
    }
}

PGE::Texture* VRManager::getTexture() {
    return texture;
}

PGE::Matrix4x4f VRManager::getProjectionMatrix(bool left) {
    if (left) {
        return leftProjectionMatrix;
    } else {
        return rightProjectionMatrix;
    }
}

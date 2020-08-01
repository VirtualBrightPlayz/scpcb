#ifndef VRMANAGER_H_INCLUDED
#define VRMANAGER_H_INCLUDED

#include <openvr.h>

#include "../Save/Config.h"
#include "../Graphics/Camera.h"
#include "../Graphics/GraphicsResources.h"

class VRCamera : public Camera {
	private:
		void updateDrawTransform(float interpolation) override {};

	public:
		VRCamera(GraphicsResources* gr, int w, int h) : Camera(gr, w, h) {};

		void setViewMatrix(const PGE::Matrix4x4f& viewMatrix);
		void setProjectionMatrix(const PGE::Matrix4x4f& viewMatrix);
};

class VRManager {
	private:
		vr::IVRSystem* vrSystem;
		VRCamera* camera;
		PGE::Matrix4x4f leftProjectionMatrix;
		PGE::Matrix4x4f rightProjectionMatrix;
		PGE::Texture* texture;
		vr::TrackedDevicePose_t tdp[vr::k_unMaxTrackedDeviceCount];

	public:

		VRManager(Config* config, GraphicsResources* gfxres);
		~VRManager();
		void update();
		void setEye(bool left);
		void createTexture(PGE::Graphics* graphics, Config* config);
		PGE::Texture* getTexture();
		VRCamera* getCamera();
};

#endif // VRMANAGER_H_INCLUDED
#ifndef VRMANAGER_H_INCLUDED
#define VRMANAGER_H_INCLUDED

#include <openvr.h>

#include "../Save/Config.h"
#include "../Graphics/Camera.h"
#include "../Graphics/GraphicsResources.h"

class VRManager {
	private:
		vr::IVRSystem* vrSystem;
		PGE::Matrix4x4f leftProjectionMatrix;
		PGE::Matrix4x4f rightProjectionMatrix;
		PGE::Texture* texture;
		vr::TrackedDevicePose_t tdp[vr::k_unMaxTrackedDeviceCount];

	public:
		VRManager(Config* config);
		~VRManager();
		void update(Camera* cam);
		void createTexture(PGE::Graphics* graphics, Config* config);
		PGE::Texture* getTexture();
		PGE::Matrix4x4f getProjectionMatrix(bool leftEye);
};

#endif // VRMANAGER_H_INCLUDED
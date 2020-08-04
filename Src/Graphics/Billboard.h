#ifndef Billboard_H_INCLUDED
#define Billboard_H_INCLUDED

#include <Mesh/Mesh.h>

#include "GraphicsResources.h"

class Billboard {
	private:
		PGE::Mesh* mesh;
		PGE::Material* material;
		PGE::Shader* shader;
		PGE::Shader::Constant* modelMatrix;

		PGE::Vector3f pos;

		GraphicsResources* gfxRes;

	public:
		Billboard(PGE::Graphics* gfx, GraphicsResources* gr, const PGE::Vector3f& pos, const PGE::String& texture);
		~Billboard();

		void render(const PGE::Vector3f& look) const;
};

#endif // Billboard_H_INCLUDED
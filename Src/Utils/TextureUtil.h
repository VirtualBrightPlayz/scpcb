#ifndef TEXTURE_UTIL_H_DEFINED
#define TEXTURE_UTIL_H_DEFINED

#include <ResourceManagement/Resource.h>
#include <Misc/FilePath.h>
#include <Texture/Texture.h>

class ImageData : public PGE::Resource<uint8_t*> {
	public:
		ImageData(const PGE::FilePath& file, int& width, int& height);
		ImageData(uint8_t* fileData, int size, int& width, int& height);
		~ImageData();
};

class TextureHelper {
	public:
		static PGE::Texture* load(PGE::Graphics* gfx, const PGE::FilePath& file);
		static PGE::Texture* load(PGE::Graphics* gfx, uint8_t* fileData, int size);
};

#endif // TEXTURE_UTIL_H_DEFINED

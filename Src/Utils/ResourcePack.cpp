#include "ResourcePack.h"

std::vector<ResourcePack> ResourcePack::resPacks = { ResourcePack("LOL", PGE::FilePath::fromStr("ResourcePacks") + '/') };

PGE::FilePath ResourcePack::getHighestModPath(const PGE::String& itemPath) {
	PGE::FilePath ret;
	for (std::vector<ResourcePack>::iterator it = resPacks.begin(); it != resPacks.end(); it++) {
		if ((ret = (it->path + itemPath + ".png")).exists()) {
			return ret;
		}
		if ((ret = (it->path + itemPath + ".jpg")).exists()) {
			return ret;
		}
	}
	ret = PGE::FilePath::fromStr(itemPath + ".png");
	if (ret.exists()) {
		return ret;
	}
	return PGE::FilePath::fromStr(itemPath + ".jpg");
}

ResourcePack::ResourcePack(const PGE::String& name, const PGE::FilePath& path) : name(name), path(path) {}

#include "ResourcePackManager.h"

#include <tinyxml2.h>
#include <Misc/FileUtil.h>

ResourcePackManager::ResourcePackManager(ArrayConfigValue* resPackLocs, ArrayConfigValue* activeResPackNames) : {
	this->resPackLocs = resPackLocs;
	this->activeResPackNames = activeResPackNames;

	loadResPacks();
}

void ResourcePackManager::loadResPacks() {
	resPacks.clear();
	activeResPacks.clear();

	std::vector<PGE::String> activeNames = this->activeResPackNames->value;

	std::vector<PGE::FilePath> modFolders;
	PGE::FilePath metaPath;
	tinyxml2::XMLDocument xmlDoc;
	tinyxml2::XMLError xmlErr;
	tinyxml2::XMLElement* xmlRoot;
	PGE::String modName;

	for (int i = 0; i < resPackLocs->value.size(); i++) {
		modFolders = PGE::FileUtil::enumerateFolders(PGE::FilePath::fromStr(resPackLocs->value[i]));
		for (int j = 0; j < modFolders.size(); j++) {
			metaPath = modFolders[j] + "meta.xml";
			if (metaPath.exists()) {
				xmlErr = xmlDoc.LoadFile(metaPath.cstr());
				if (xmlErr != tinyxml2::XML_SUCCESS) {
					throw new std::runtime_error("XML moment " + xmlErr);
				}
				xmlRoot = xmlDoc.RootElement();
				if (xmlRoot != nullptr && xmlRoot->FirstChildElement("Type")->GetText() == PGE::String("ResPack")) {
					modName = xmlRoot->FirstChildElement("Name")->GetText();
					resPacks.push_back({ modName, modFolders[j] });
					if (std::find(activeNames.begin(), activeNames.end(), modName) != activeNames.end()) {
						activeResPacks.push_back(resPacks[resPacks.size() - 1]);
					}
				}
			}
		}
	}
}

PGE::FilePath ResourcePackManager::getHighestModPath(const PGE::String& itemPath) {
	PGE::FilePath ret;
	for (int i = 0; i < activeResPacks.size(); i++) {
		if ((activeResPacks[i].path + itemPath + ".LOCK").exists()) {
			break;
		}
		if ((ret = (activeResPacks[i].path + itemPath + ".png")).exists()) {
			return ret;
		}
		if ((ret = (activeResPacks[i].path + itemPath + ".jpg")).exists()) {
			return ret;
		}
	}
	ret = PGE::FilePath::fromStr(itemPath + ".png");
	if (ret.exists()) {
		return ret;
	}
	return PGE::FilePath::fromStr(itemPath + ".jpg");
}

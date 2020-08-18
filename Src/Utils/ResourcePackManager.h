#ifndef RESOURCEPACK_H_INCLUDED
#define RESOURCEPACK_H_INCLUDED

#include <vector>

#include <Misc/FilePath.h>

#include "../Save/ConfigValues.h"

class ResourcePackManager {
    private:
        struct ResourcePack {
            const PGE::String name;
            const PGE::FilePath path;
        };
        
        ArrayConfigValue* resPackLocs;
        ArrayConfigValue* activeResPackNames;
        
        std::vector<ResourcePack> resPacks;
        std::vector<ResourcePack> activeResPacks;

    public:
        ResourcePackManager(ArrayConfigValue* resPackLocs, ArrayConfigValue* activeResPackNames);
        
        void loadResPacks();
        PGE::FilePath getHighestModPath(const PGE::String& itemPath);
};

#endif // RESOURCEPACK_H_INCLUDED

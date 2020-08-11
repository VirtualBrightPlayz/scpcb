#ifndef RESOURCEPACK_H_INCLUDED
#define RESOURCEPACK_H_INCLUDED

#include <vector>

#include <Misc/FilePath.h>

class ResourcePack {
    private:
        const PGE::String name;
        const PGE::FilePath path;

    public:
        ResourcePack(const PGE::String& name, const PGE::FilePath& path);
        
        static std::vector<ResourcePack> resPacks;
        static PGE::FilePath getHighestModPath(const PGE::String& itemPath);
};

#endif // RESOURCEPACK_H_INCLUDED

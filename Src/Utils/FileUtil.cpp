#include <fstream>

#include "FileUtil.h"

bool FileUtil::exists(PGE::String path) {
    std::ifstream file(path.cstr());
    return file.good();
}

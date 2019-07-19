#ifndef FileUtil_H_INCLUDED
#define FileUtil_H_INCLUDED

#include <Misc/String.h>

class FileUtil {
    public:
        static bool exists(PGE::String path);
};

#endif // FileUtil_H_INCLUDED

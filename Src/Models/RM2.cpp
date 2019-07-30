#include "RM2.h"

enum class FileSections {
    TEXTURES = 1,
    OPAQUE = 2,
    ALPHA = 3,
    INVISIBLE = 4,
    SCREEN = 5,
    WAYPOINT = 6,
    POINTLIGHT = 7,
    SPOTLIGHT = 8,
    SOUNDEMITTER = 9,
    PROP = 10
};

enum class TextureLoadFlags
{
    COLOR = 1,
    ALPHA = 2
};

enum class TextureBlendFlags
{
    NORMAL = 0,
    DIFFUSE = 1,
    LM = 2
};



#ifndef RM2_H_INCLUDED
#define RM2_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
const String RM2_HEADER(".RM2");
const int RM2_TEXTURES = 1;
const int RM2_OPAQUE = 2;
const int RM2_ALPHA = 3;
const int RM2_INVISIBLE = 4;
const int RM2_SCREEN = 5;
const int RM2_WAYPOINT = 6;
const int RM2_POINTLIGHT = 7;
const int RM2_SPOTLIGHT = 8;
const int RM2_SOUNDEMITTER = 9;
const int RM2_PROP = 10;
const int RM2_LOADFLAG_COLOR = 1;
const int RM2_LOADFLAG_ALPHA = 2;
const int RM2_BLENDFLAG_NORMAL = 0;
const int RM2_BLENDFLAG_DIFFUSE = 1;
const int RM2_BLENDFLAG_LM = 2;

// Functions.
String ReadByteString(int stream);

void LoadRM2(struct RoomTemplate* rt);

}
#endif // RM2_H_INCLUDED

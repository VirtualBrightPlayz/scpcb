#ifndef RM2_H_INCLUDED
#define RM2_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const String RM2_HEADER;
extern const int RM2_TEXTURES;
extern const int RM2_OPAQUE;
extern const int RM2_ALPHA;
extern const int RM2_INVISIBLE;
extern const int RM2_SCREEN;
extern const int RM2_WAYPOINT;
extern const int RM2_POINTLIGHT;
extern const int RM2_SPOTLIGHT;
extern const int RM2_SOUNDEMITTER;
extern const int RM2_PROP;
extern const int RM2_LOADFLAG_COLOR;
extern const int RM2_LOADFLAG_ALPHA;
extern const int RM2_BLENDFLAG_NORMAL;
extern const int RM2_BLENDFLAG_DIFFUSE;
extern const int RM2_BLENDFLAG_LM;

// Functions.
String ReadByteString(int stream);

void LoadRM2(RoomTemplate* rt);

}
#endif // RM2_H_INCLUDED

#include <bbblitz3d.h>
#include <bbmath.h>
#include <bbruntime.h>
#include <bbstring.h>

#include "MathUtils.h"
#include "../GameMain.h"

namespace CBN {

// Globals.
float Mesh_MinX;
float Mesh_MinY;
float Mesh_MinZ;
float Mesh_MaxX;
float Mesh_MaxY;
float Mesh_MaxZ;
float Mesh_MagX;
float Mesh_MagY;
float Mesh_MagZ;

// Functions.
float Distance(float x1, float y1, float x2, float y2) {
    float x = x2 - x1;
    float y = y2 - y1;

    return(bbSqr(x*x + y*y));
}

float CurveValue(float number, float old, float smooth) {
    if (number < old) {
        return Max(old + (number - old) * (1.f / smooth * timing->tickDuration), number);
    } else {
        return Min(old + (number - old) * (1.f / smooth * timing->tickDuration), number);
    }
}

float CurveAngle(float val, float old, float smooth) {
    float diff = WrapAngle(val) - WrapAngle(old);
    if (diff > 180) {
        diff = diff - 360;
    }
    if (diff < - 180) {
        diff = diff + 360;
    }
    return WrapAngle(old + diff * (1.f / smooth * timing->tickDuration));
}

float WrapAngle(float angle) {
    if (angle == INFINITY) {
        return 0.f;
    }
    while (angle < 0) {
        angle = angle + 360;
    }
    while (angle >= 360) {
        angle = angle - 360;
    }
    return angle;
}

float GetAngle(float x1, float y1, float x2, float y2) {
    return bbATan2( y2 - y1, x2 - x1 );
}

int CircleToLineSegIsect(float cx, float cy, float r, float l1x, float l1y, float l2x, float l2y) {

    //Palauttaa:
    //  True (1) kun:
    //      Ympyrä [keskipiste = (cx, cy): säde = r]
    //      leikkaa janan, joka kulkee pisteiden (l1x, l1y) & (l2x, l2y) kaitta
    //  False (0) muulloin

    //Ympyrän keskipisteen ja (ainakin toisen) janan päätepisteen etäisyys < r
    //-> leikkaus
    if (Distance(cx, cy, l1x, l1y) <= r) {
        return true;
    }

    if (Distance(cx, cy, l2x, l2y) <= r) {
        return true;
    }

    //Vectors (vector of a line and vectors at end points of a segment at the center of a circle)
    float SegVecX = l2x - l1x;
    float SegVecY = l2y - l1y;

    float PntVec1X = cx - l1x;
    float PntVec1Y = cy - l1y;

    float PntVec2X = cx - l2x;
    float PntVec2Y = cy - l2y;

    //Em. the points of the vectors
    float dp1 = SegVecX * PntVec1X + SegVecY * PntVec1Y;
    float dp2 = -SegVecX * PntVec2X - SegVecY * PntVec2Y;

    //Checks whether the value of another point is 0
    //tai molempien merkki sama
    if (dp1 == 0 | dp2 == 0) {
    } else if ((dp1 > 0 & dp2 > 0) || (dp1 < 0 & dp2 < 0)) { // TODO: wtf
    } else {
        //Neither -> no cutting
        return false;
    }

    //The straight line passing through the endpoints ;equation; (ax + by + c = 0)
    float a = (l2y - l1y) / (l2x - l1x);
    float b = -1;
    float c = -(l2y - l1y) / (l2x - l1x) * l1x + l1y;

    //Distance from the center of the circle
    float d = abs(a * cx + b * cy + c) / bbSqr(a * a + b * b);

    //The circle is too far away
    //-> not cutting
    if (d > r) {
        return false;
    }

    //Local kateetin_pituus# = Cos(angle) * hyp

    //If you get this far, a circle and a line segment intersect (or are intertwined)
    return true;
}

float Min(float a, float b) {
    if (a < b) {
        return a;
    } else {
        return b;
    }
}

float Max(float a, float b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}

float Sgn(float f) {
    if (f>0.0f) {
        return 1.0f;
    } else if (f<0.0f) {
        return -1.0f;
    } else {
        return 0.0f;
    }
}

float Modf(float a,float div) {
    while (a>div) {
        a-=div;
    }
    return a;
}

float angleDist(float a0, float a1) {
    float b = a0-a1;
    float bb;
    if (b<-180.f) {
        bb = b+360.f;
    } else if ((b>180.f)) {
        bb = b-360.f;
    } else {
        bb = b;
    }
    return bb;
}

float Inverse(float number) {

    return (float)(1.f - number);

}

float Rnd_Array(float numb1, float numb2, float Array1, float Array2) {
    int whatarray = bbRand(1,2);

    if (whatarray == 1) {
        return bbRnd(numb1, Array1);
    } else {
        return bbRnd(Array2, numb2);
    }

}

int TimeInPosMilliSecs() {
    int retVal = bbMilliSecs();
    if (retVal < 0) {
        retVal = retVal + 2147483648;
    }
    return retVal;
}

void MakeCollBox(class MeshModel* mesh) {
    float sx = EntityScaleX(mesh, 1);
    float sy = Max(EntityScaleY(mesh, 1), 0.001);
    float sz = EntityScaleZ(mesh, 1);
    GetMeshExtents(mesh);
    bbEntityBox(mesh, Mesh_MinX * sx, Mesh_MinY * sy, Mesh_MinZ * sz, Mesh_MagX * sx, Mesh_MagY * sy, Mesh_MagZ * sz);
}

void GetMeshExtents(MeshModel* mesh) {
    float minx = INFINITY;
    float miny = INFINITY;
    float minz = INFINITY;
    float maxx = -INFINITY;
    float maxy = -INFINITY;
    float maxz = -INFINITY;

    int surfs = bbCountSurfaces(mesh);

    for (int s = 1; s <= surfs; s++) {
        Surface* surf = bbGetSurface(mesh, s);
        int verts = bbCountVertices(surf);

        for (int v = 0; v < verts; v++) {
            float x = bbVertexX(surf, v);
            float y = bbVertexY(surf, v);
            float z = bbVertexZ(surf, v);

            if (x < minx) {
                minx = x;
            }
            if (x > maxx) {
                maxx = x;
            }
            if (y < miny) {
                miny = y;
            }
            if (y > maxy) {
                maxy = y;
            }
            if (z < minz) {
                minz = z;
            }
            if (z > maxz) {
                maxz = z;
            }
        }
    }

    Mesh_MinX = minx;
    Mesh_MinY = miny;
    Mesh_MinZ = minz;
    Mesh_MaxX = maxx;
    Mesh_MaxY = maxy;
    Mesh_MaxZ = maxz;
    Mesh_MagX = maxx-minx;
    Mesh_MagY = maxy-miny;
    Mesh_MagZ = maxz-minz;

}

float EntityScaleX(Object* entity, int globl) {
    if (globl) {
        bbTFormVector(1, 0, 0, entity, 0);
    } else {
        bbTFormVector( 1, 0, 0, entity, bbGetParent(entity));
    }
    return bbSqr(bbTFormedX() * bbTFormedX() + bbTFormedY() * bbTFormedY() + bbTFormedZ() * bbTFormedZ());
}

float EntityScaleY(Object* entity, int globl) {
    if (globl) {
        bbTFormVector(0, 1, 0, entity, 0);
    } else {
        bbTFormVector( 0, 1, 0, entity, bbGetParent(entity));
    }
    return bbSqr(bbTFormedX() * bbTFormedX() + bbTFormedY() * bbTFormedY() + bbTFormedZ() * bbTFormedZ());
}

float EntityScaleZ(Object* entity, int globl) {
    if (globl) {
        bbTFormVector(0, 0, 1, entity, 0);
    } else {
        bbTFormVector(0, 0, 1, entity, bbGetParent(entity));
    }
    return bbSqr(bbTFormedX() * bbTFormedX() + bbTFormedY() * bbTFormedY() + bbTFormedZ() * bbTFormedZ());
}

int SeedStringToInt(const String& seed) {
    int retVal = 0;

    for (int i = 1; i <= seed.size(); i++) {
        int chr = bbAsc(bbMid(seed,i,1));
        retVal = (retVal << 1) + chr;
    }

    if (retVal==0) {
        retVal = 1;
    }
    return abs(retVal);
}

float modFloat(float num, float divide) {
    float retVal = num;
    while (retVal > divide) {
        retVal -= divide;
    }
    return retVal;
}

}

#ifndef MATHUTILS_H_INCLUDED
#define MATHUTILS_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const float INFINITY;
extern const float NAN;

// Globals.
extern float Mesh_MinX;
extern float Mesh_MinY;
extern float Mesh_MinZ;
extern float Mesh_MaxX;
extern float Mesh_MaxY;
extern float Mesh_MaxZ;
extern float Mesh_MagX;
extern float Mesh_MagY;
extern float Mesh_MagZ;

// Functions.
float Distance(float x1, float y1, float x2, float y2);

float CurveValue(float number, float old, float smooth);

float CurveAngle(float val, float old, float smooth);

float WrapAngle(float angle);

float GetAngle(float x1, float y1, float x2, float y2);

int CircleToLineSegIsect(float cx, float cy, float r, float l1x, float l1y, float l2x, float l2y);

float Min(float a, float b);

float Max(float a, float b);

float angleDist(float a0, float a1);

float Inverse(float number);

float Rnd_Array(float numb1, float numb2, float Array1, float Array2);

int TimeInPosMilliSecs();

void MakeCollBox(int mesh);

void GetMeshExtents(int Mesh);

float EntityScaleX(int entity, int globl = false);

float EntityScaleY(int entity, int globl = false);

float EntityScaleZ(int entity, int globl = false);

int SeedStringToInt(String seed);

}
#endif // MATHUTILS_H_INCLUDED

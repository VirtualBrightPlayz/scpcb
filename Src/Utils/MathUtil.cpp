#include <cmath>

#include "MathUtil.h"

float MathUtil::degToRad(float degree) {
    return degree * PI / 180.0f;
}

bool MathUtil::eqFloats(float p1, float p2) {
    return fabs(p1 - p2) < MARGIN_ERROR;
}

int MathUtil::clamp(int val, int min, int max) {
    if (val <= min) { return min; }
    if (val >= max) { return max; }
    return val;
}

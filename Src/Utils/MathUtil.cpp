#include "MathUtil.h"

#include <Exception/Exception.h>

float MathUtil::degToRad(float degree) {
    return degree * PI / 180.0f;
}

float MathUtil::radToDeg(float radians) {
    return radians * 180.0f / PI;
}

bool MathUtil::equalFloats(float val, float other) {
    return absFloat(val - other) < MARGIN_ERROR;
}

int MathUtil::maxInt(int val, int other) {
    if (val > other) { return val; }
    return other;
}

int MathUtil::minInt(int val, int other) {
    if (val < other) { return val; }
    return other;
}

float MathUtil::maxFloat(float val, float other) {
    if (val > other) { return val; }
    return other;
}

float MathUtil::minFloat(float val, float other) {
    if (val < other) { return val; }
    return other;
}

int MathUtil::clampInt(int val, int min, int max) {
    PGE_ASSERT(min > max, "Incorrect order!");

    if (val < min) { return min; }
    if (val > max) { return max; }
    return val;
}

float MathUtil::clampFloat(float val, float min, float max) {
    PGE_ASSERT(min > max, "Incorrect order!");

    if (val < min) { return min; }
    if (val > max) { return max; }
    return val;
}

float MathUtil::absFloat(float val) {
    if (val < 0.f) {
        return -val;
    }
    return val;
}

double MathUtil::absDouble(double val) {
    if (val < 0.0) {
        return -val;
    }
    return val;
}

int MathUtil::floor(float val) {
    int i = (int)val;
    if (i > val) { i--; }
    return i;
}

int MathUtil::ceil(float val) {
    int i = (int)val;
    if (i < val) { i++; }
    return i;
}

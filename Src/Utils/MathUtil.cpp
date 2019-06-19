#define _USE_MATH_DEFINES
#include <math.h>

#include "MathUtil.h"

float MathUtil::degToRad(float degree) {
    return degree * M_PI / 180.0f;
}

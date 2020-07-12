#ifndef MATH_H_INCLUDED
#define MATH_H_INCLUDED

class MathUtil {
    public:
        constexpr static float MARGIN_ERROR = 0.001f;

        constexpr static float PI = 3.1415926535897932f;

        static float degToRad(float degrees);
        static float radToDeg(float radians);
    
        static bool eqFloats(float p1, float p2);
    
        // Clamps an integer between a min and max inclusively.
        static int clamp(int val, int min, int max);
};

#endif // MATH_H_INCLUDED

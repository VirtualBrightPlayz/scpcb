shared class FloatInterpolator {
    private float prevValue = 0.f;
    private float currValue = 0.f;

    void update(float value) {
        prevValue = currValue;
        currValue = value;
    }

    float lerp(float interpolation) {
        return prevValue + (currValue - prevValue) * interpolation;
    }
}
#include "Input.h"

const Input operator&(const Input& a, const Input& b) {
    return (Input)((int)a & (int)b);
}

const Input operator|(const Input& a, const Input& b) {
    return (Input)((int)a | (int)b);
}

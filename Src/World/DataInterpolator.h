#ifndef DATAINTERPOLATION_H_INCLUDED
#define DATAINTERPOLATION_H_INCLUDED

#include <Math/Vector.h>

class DataInterpolator {
private:
    struct TransformData {
        PGE::Vector3f position;
        PGE::Vector3f rotation;
        PGE::Vector3f scale;

        TransformData(const PGE::Vector3f& pos, const PGE::Vector3f& rot, const PGE::Vector3f& scal);
    };

    TransformData prevTransform;
    TransformData currTransform;

public:
    DataInterpolator(const PGE::Vector3f& position, const PGE::Vector3f& rotation, const PGE::Vector3f& scale);

    void update(const PGE::Vector3f& position, const PGE::Vector3f& rotation, const PGE::Vector3f& scale);

    PGE::Vector3f getInterpolatedPosition(float interpolation) const;
    PGE::Vector3f getInterpolatedRotation(float interpolation) const;
    PGE::Vector3f getInterpolatedScale(float interpolation) const;
};

#endif
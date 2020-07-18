#include "DataInterpolator.h"

DataInterpolator::TransformData::TransformData(const PGE::Vector3f& pos, const PGE::Vector3f& rot, const PGE::Vector3f& scal) {
    position = pos;
    rotation = rot;
    scale = scal;
}

DataInterpolator::DataInterpolator(const PGE::Vector3f& position, const PGE::Vector3f& rotation, const PGE::Vector3f& scale) 
: currTransform(TransformData(position, rotation, scale)), prevTransform(TransformData(position, rotation, scale)) { }

void DataInterpolator::update(const PGE::Vector3f& position, const PGE::Vector3f& rotation, const PGE::Vector3f& scale) {
    prevTransform = currTransform;
    currTransform = TransformData(position, rotation, scale);
}

PGE::Vector3f DataInterpolator::getInterpolatedPosition(float interpolation) const {
    return PGE::Vector3f::lerp(prevTransform.position, currTransform.position, interpolation);
}

PGE::Vector3f DataInterpolator::getInterpolatedRotation(float interpolation) const {
    return PGE::Vector3f::lerp(prevTransform.rotation, currTransform.rotation, interpolation);
}

PGE::Vector3f DataInterpolator::getInterpolatedScale(float interpolation) const {
    return PGE::Vector3f::lerp(prevTransform.scale, currTransform.scale, interpolation);
}
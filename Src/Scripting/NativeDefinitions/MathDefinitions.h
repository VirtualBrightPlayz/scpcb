#ifndef MATHDEFINITIONS_H_INCLUDED
#define MATHDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"
#include <Math/Vector.h>
#include <Math/Matrix.h>
#include <Math/Line.h>
#include <Math/Plane.h>
#include <Math/AABBox.h>
#include <Math/Rectangle.h>

class ScriptManager;

class MathDefinitions : public NativeDefinition {
    public:
        MathDefinitions(ScriptManager* mgr);
};

#endif

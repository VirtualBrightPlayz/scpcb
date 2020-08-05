#ifndef BILLBOARDDEFINITIONS_H_INCLUDED
#define BILLBOARDDEFINITIONS_H_INCLUDED

#include <Math/Vector.h>
#include <Color/Color.h>

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class BillboardManager;
class Billboard;
class RotatedBillboard;

class BillboardDefinitions : public NativeDefinition {
    private:
        BillboardManager* bm;
        
        template <class T>
        void registerClass(const char* className);
        
        Billboard* createBillboard(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector2f& scale);
        RotatedBillboard* createBillboardRotated(const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& rotation, const PGE::Vector2f& scale);
        void destroyBillboard(Billboard* billboard);

    public:
        BillboardDefinitions(ScriptManager* mgr, BillboardManager* bm);
};

#endif // BILLBOARDDEFINITIONS_H_INCLUDED


#ifndef LOADER_B3D_H
#define LOADER_B3D_H

#include "../gxruntime/StringType.h"

#include "meshloader.h"

class Loader_B3D : public MeshLoader{
public:
	MeshModel *load( String f,const Transform &conv,int hint );
};

#endif

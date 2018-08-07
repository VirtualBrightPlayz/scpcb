
#ifndef LOADER_3DS_H
#define LOADER_3DS_H

#include "../gxruntime/StringType.h"

#include "meshloader.h"

class Loader_3DS : public MeshLoader{
public:
	MeshModel *load( String f,const Transform &conv,int hint );
};

#endif
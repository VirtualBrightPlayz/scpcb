
#ifndef RENDERCONTEXT_H
#define RENDERCONTEXT_H

#include "frustum.h"

class RenderContext{
public:
	RenderContext( const Transform &t,const Frustum &f ):
	camera_tform( t ),camera_frustum(f){
		new( &world_frustum ) Frustum( f,t );
	}

	const Transform &getCameraTform()const{ return camera_tform; }
	const Frustum &getWorldFrustum()const{ return world_frustum; }
	const Frustum &getCameraFrustum()const{ return camera_frustum; }

private:
	Transform camera_tform;
	Frustum world_frustum,camera_frustum;
};

#endif
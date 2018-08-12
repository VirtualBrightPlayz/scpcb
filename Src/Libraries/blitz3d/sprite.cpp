#include <vector>

#include "../gxruntime/gxruntime.h"
#include "../gxruntime/gxgraphics.h"

#include "sprite.h"

extern float stats3d[];

static float null[]={0,0,0};

static float tex_coords0[2][2]={ {0,0},{0,0} };
static float tex_coords1[2][2]={ {1,0},{1,0} };
static float tex_coords2[2][2]={ {1,1},{1,1} };
static float tex_coords3[2][2]={ {0,1},{0,1} };

extern gxRuntime *gx_runtime;
extern gxGraphics *gx_graphics;

static gxMesh* mesh = nullptr;
static Vector verts[4];

static void allocIndex(){
	if( !mesh ){
		mesh=gx_graphics->createMesh( 4,2,0 );

        verts[0] = Vector(-1, 1, 0);
        verts[1] = Vector(1, 1, 0);
        verts[2] = Vector(1, -1, 0);
        verts[3] = Vector(-1, -1, 0);

        mesh->lock(false);
        mesh->setVertex(0, &verts[0].x, null, tex_coords0);
        mesh->setVertex(1, &verts[1].x, null, tex_coords1);
        mesh->setVertex(2, &verts[2].x, null, tex_coords2);
        mesh->setVertex(3, &verts[3].x, null, tex_coords3);
        mesh->setTriangle(0, 0, 1, 2);
        mesh->setTriangle(1, 0, 2, 3);
        mesh->unlock();
	}
}

Sprite::Sprite():
view_mode(VIEW_MODE_FREE),
xhandle(0),yhandle(0),
rot(0),xscale(1),yscale(1),captured(false){
	setRenderSpace( RENDER_SPACE_LOCAL );
	allocIndex();
}

Sprite::Sprite( const Sprite &t ):
Model(t),
view_mode(t.view_mode),
xhandle(t.xhandle),yhandle(t.yhandle),
rot(t.rot),xscale(t.xscale),yscale(t.yscale),captured(false){
	allocIndex();
}

Sprite::~Sprite(){
	
}

void Sprite::setRotation( float angle ){
	rot=angle;
}

void Sprite::setScale( float x,float y ){
	xscale=x;yscale=y;
}

void Sprite::setHandle( float x,float y ){
	xhandle=x;yhandle=y;
}

void Sprite::setViewmode( int mode ){
	view_mode=mode;
}

void Sprite::capture(){
	Model::capture();
	r_rot=rot;
	r_xscale=xscale;
	r_yscale=yscale;
	captured=true;
}

bool Sprite::beginRender( float tween ){
	Model::beginRender( tween );
	if( tween==1 || !captured ){
		r_rot=rot;
		r_xscale=xscale;
		r_yscale=yscale;
	}else{
		r_rot=(rot-r_rot)*tween+r_rot;
		r_xscale=(xscale-r_xscale)*tween+r_xscale;
		r_yscale=(yscale-r_yscale)*tween+r_yscale;
	}
	return true;
}

const Transform &Sprite::getRenderTform()const {
    return renderTForm;
}

bool Sprite::render( const RenderContext &rc ){

	Transform& t=renderTForm;
    t = getBaseRenderTform();

	if( view_mode==VIEW_MODE_FREE ){
		t.m=rc.getCameraTform().m;
	}else if( view_mode==VIEW_MODE_UPRIGHT ){
		t.m.k=rc.getCameraTform().m.k;t.m.orthogonalize();
	}else if( view_mode==VIEW_MODE_UPRIGHT2 ){
		t.m=yawMatrix( matrixYaw( rc.getCameraTform().m ) ) * t.m;
	}

    t.m=t.m * rollMatrix( r_rot ) * scaleMatrix( r_xscale,r_yscale,1 );
    t.v += t.m*Vector(-xhandle, -yhandle, 0.0f);

    static Frustum model_frustum;
    new(&model_frustum) Frustum(rc.getWorldFrustum(), -getRenderTform());

	if( !model_frustum.cull( verts,4 ) ) return false;
    
	enqueue( mesh,0,4,0,2 );
	return false;
}

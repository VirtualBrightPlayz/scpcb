
#ifndef TEXTURE_H
#define TEXTURE_H

#include "cachedtexture.h"

#include "../gxruntime/gxscene.h"
#include "../gxruntime/gxcanvas.h"
#include "../gxruntime/StringType.h"

class Texture{
public:
	Texture();
	Texture( const String& file,int flags );
	Texture( const String& file,int flags,int w,int h,int first,int cnt );
	Texture( int width,int height,int flags,int cnt );
	Texture( const Texture &texture );
	~Texture();

	Texture &operator=( const Texture &texture );

	void setScale( float u_scale,float v_scale );
	void setRotation( float rot );
	void setPosition( float u_pos,float v_pos );
	void setBlend( int blend );
	void setFlags( int flags );

	int getCanvasFlags()const;
	gxCanvas *getCanvas( int frame )const;
	const gxScene::Matrix *getMatrix()const;
	int getBlend()const;
	int getFlags()const;
	CachedTexture *getCachedTexture()const;

	bool isTransparent()const;
	bool operator<( const Texture &t )const;

	static void clearFilters();
	static void addFilter( const String& filter,int flags );

private:
	struct Rep;
	Rep *rep;
};

#endif
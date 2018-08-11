
#ifndef BBGRAPHICS_H
#define BBGRAPHICS_H

#include "bbsys.h"
#include "../gxruntime/gxgraphics.h"
#include "../gxruntime/StringType.h"

extern gxGraphics *gx_graphics;
extern gxCanvas *gx_canvas;
extern gxScene *gx_scene;

class bbImage;

//general graphics functions
int		 bbCountGfxDrivers();
String	 bbGfxDriverName( int n );
String	 bbGfxDriverDesc( int n );
void	 bbSetGfxDriver( int n );
int		 bbGfxModeExists( int w,int h,int d );
int		 bbCountGfxModes();
int		 bbCountGfxModes3D();
int		 bbGfxModeWidth( int n );
int		 bbGfxModeHeight( int n );
int		 bbGfxModeDepth( int n );
int		 bbGraphicsWidth();
int		 bbGraphicsHeight();
int		 bbGraphicsDepth();
int		 bbAvailVidMem();
int		 bbTotalVidMem();

//mode functions
void	 bbGraphics(int w, int h, int d, int mode);
void	 bbGraphics3D(int w, int h, int d, int mode);
gxCanvas * bbFrontBuffer();
gxCanvas * bbBackBuffer();
void	 bbEndGraphics();
int		 bbGraphicsLost();
int		 bbScanLine();
void	 bbVWait( int n=1 );
void	 bbFlip( int vwait=1 );

//graphics buffer functions
void	 bbSetBuffer( gxCanvas *buff );
gxCanvas * bbGraphicsBuffer();
int		 bbLoadBuffer( gxCanvas *surf,const String& str );
int		 bbSaveBuffer( gxCanvas *surf,const String& str );

//fast read/write operations...
void	 bbLockBuffer( gxCanvas *buff );
void	 bbUnlockBuffer( gxCanvas *buff );
int		 bbReadPixel( int x,int y,gxCanvas *buff );
void	 bbWritePixel( int x,int y,int argb,gxCanvas *buff );
int		 bbReadPixelFast( int x,int y,gxCanvas *buff );
void	 bbWritePixelFast( int x,int y,int argb,gxCanvas *buff );


//2d rendering functions
void	 bbOrigin( int x,int y );
void	 bbViewport( int x,int y,int w,int h );
void	 bbColor( int r,int g,int b );
void	 bbClsColor( int r,int g,int b );
void	 bbCls();
void	 bbPlot( int x,int y );
void	 bbLine( int x1,int y1,int x2,int y2 );
void	 bbRect( int x,int y,int w,int h,int solid=1 );
void	 bbOval( int x,int y,int w,int h,int solid=1 );
void	 bbText( int x,int y,const String& str,int centre_x=0,int centre_y=0 );
void bbCopyRect(int sx, int sy, int w, int h, int dx, int dy,class gxCanvas *src,class gxCanvas *dest);
void	 bbGetColor( int x,int y );
int		 bbColorRed();
int		 bbColorGreen();
int		 bbColorBlue();

//font functions
gxFont * bbLoadFont( const String& name,int height,int bold,int italic,int underline );
void	 bbFreeFont( gxFont *f );
void	 bbSetFont( gxFont *f );
int		 bbFontWidth();
int		 bbFontHeight();
int		 bbStringWidth( const String& str );
int		 bbStringHeight( const String& str );

//image functions
bbImage* bbLoadImage( const String& s );
bbImage* bbCopyImage( bbImage *i );
bbImage* bbCreateImage( int w,int h,int n=1 );
bbImage* bbLoadAnimImage( const String& s,int w,int h,int first,int cnt );
void	 bbFreeImage( bbImage *i );
int		 bbSaveImage( bbImage *i,const String& filename,int frame );
void	 bbGrabImage( bbImage *i,int x,int y,int n );
gxCanvas * bbImageBuffer( bbImage *i,int n=0 );
void	 bbDrawImage( bbImage *i,int x,int y,int frame=0 );
void	 bbDrawBlock( bbImage *i,int x,int y,int frame );
void	 bbTileImage( bbImage *i,int x,int y,int frame );
void	 bbTileBlock( bbImage *i,int x,int y,int frame );
void	 bbDrawImageRect( bbImage *i,int x,int y,int r_x,int r_y,int r_w,int r_h,int frame=0 );
void	 bbDrawBlockRect( bbImage *i,int x,int y,int r_x,int r_y,int r_w,int r_h,int frame=0 );
void	 bbMaskImage( bbImage *i,int r,int g,int b );
void	 bbHandleImage( bbImage *i,int x,int y );
void	 bbScaleImage( bbImage *i,float w,float h );
void	 bbResizeImage( bbImage *i,float w,float h );
void	 bbRotateImage( bbImage *i,float angle );
void	 bbTFormImage( bbImage *i,float a,float b,float c,float d );
void	 bbTFormFilter( int enable );
void	 bbAutoMidHandle( int enable );
void	 bbMidHandle( bbImage *i );
int		 bbImageWidth( bbImage *i );
int		 bbImageHeight( bbImage *i );
int		 bbImageXHandle( bbImage *i );
int		 bbImageYHandle( bbImage *i );
int		 bbImagesOverlap( bbImage *i1,int x1,int y1,bbImage *i2,int x2,int y2 );
int		 bbImagesCollide( bbImage *i1,int x1,int y1,int f1,bbImage *i2,int x2,int y2,int f2 );
int		 bbRectsOverlap( int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2 );
int		 bbImageRectOverlap( bbImage *i,int x,int y,int r_x,int r_y,int r_w,int r_h );
int		 bbImageRectCollide( bbImage *i,int x,int y,int f,int r_x,int r_y,int r_w,int r_h );

//simple print functions
void	 bbWrite( const String& str );
void	 bbPrint( const String& str );
String 	 bbInput( const String& prompt );
void	 bbLocate( int x,int y );

//mouse pointer functions
void bbShowPointer();
void bbHidePointer();

#endif
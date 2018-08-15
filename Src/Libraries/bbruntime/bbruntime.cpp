#include "../gxruntime/StringType.h"

#include "bbsys.h"
#include "bbruntime.h"

void  bbEnd(){
	RTEX( 0 );
}
void  bbStop(){
	gx_runtime->debugStop();
}

void  bbAppTitle( const String& ti,const String& cp ){
	gx_runtime->setTitle( ti,cp );
}

void  bbRuntimeError( const String& str ){
    String s = str;
	if( s.size()>255 ) s = s.substr(0,255);
	static char err[256];
	strcpy( err,s.cstr() );
	RTEX( err );
}

int bbExecFile( const String& f ){
	int n=gx_runtime->execute( f );
	return n;
}

void  bbDelay( int ms ){
	!gx_runtime->delay( ms );
}

int  bbMilliSecs(){
	return gx_runtime->getMilliSecs();
}

String  bbCommandLine(){
	return gx_runtime->commandLine();
}

String  bbSystemProperty( const String& p ){
	String t=gx_runtime->systemProperty( p );
	return t;
}

String   bbGetEnv( const String& env_var ){
	char *p=getenv( env_var.cstr() );
	String val=p ? p : "";
	return val;
}

void  bbSetEnv( const String& env_var,const String& val ){
	String t=env_var+"="+val;
	putenv( t.cstr() );
}

gxTimer * bbCreateTimer( int hertz ){
	gxTimer *t=gx_runtime->createTimer( hertz );
	return t;
}

int   bbWaitTimer( gxTimer *t ){
	int n=t->wait();
	return n;
}

void  bbFreeTimer( gxTimer *t ){
	gx_runtime->freeTimer( t );
}

void  _bbDebugStmt( int pos,const char *file ){
	gx_runtime->debugStmt( pos,file );
}

void  _bbDebugEnter( void *frame,void *env,const char *func ){
	gx_runtime->debugEnter( frame,env,func );
}

void  _bbDebugLeave(){
	gx_runtime->debugLeave();
}

bool math_create();
bool math_destroy();
bool string_create();
bool string_destroy();
bool stream_create();
bool stream_destroy();
bool sockets_create();
bool sockets_destroy();
bool filesystem_create();
bool filesystem_destroy();
bool graphics_create();
bool graphics_destroy();
bool input_create();
bool input_destroy();
bool audio_create();
bool audio_destroy();

bool blitz3d_create();
bool blitz3d_destroy();

//start up error
static void sue( const char *t ){
	String p=String( "Startup Error: " )+t;
	gx_runtime->debugInfo( p.cstr() );
}

bool bbruntime_create(HINSTANCE hinst){
    gx_runtime = gxRuntime::openRuntime(hinst, "");

    bool math_state = math_create(); if (!math_state) sue("math_create failed");
    bool string_state = string_create(); if (!string_state) sue("string_create failed");
    bool stream_state = stream_create(); if (!stream_state) sue("stream_create failed");
    bool sockets_state = sockets_create(); if (!sockets_state) sue("sockets_create failed");
    bool filesystem_state = filesystem_create(); if (!filesystem_state) sue("filesystem_create failed");
    bool graphics_state = graphics_create(); if (!graphics_state) sue("graphics_create failed");
    bool input_state = input_create(); if (!input_state) sue("input_create failed");
    bool audio_state = audio_create(); if (!audio_state) sue("audio_create failed");
    bool blitz3d_state = blitz3d_create(); if (!blitz3d_state) sue("blitz3d_create failed");

    if (math_state && string_state && stream_state && sockets_state && filesystem_state &&
        graphics_state && input_state && audio_state && blitz3d_state) return true;

    if (blitz3d_state) blitz3d_destroy();
    if (audio_state) audio_destroy();
    if (input_state) input_destroy();
    if (graphics_state) graphics_destroy();
    if (filesystem_state) filesystem_destroy();
    if (sockets_state) sockets_destroy();
    if (stream_state) stream_destroy();
    if (string_state) string_destroy();
    if (math_state) math_destroy();
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;

	return false;
}

bool bbruntime_destroy(){
	blitz3d_destroy();
	audio_destroy();
	input_destroy();
	graphics_destroy();
	filesystem_destroy();
	sockets_destroy();
	stream_destroy();
	string_destroy();
	math_destroy();
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;
	return true;
}

void bbruntime_panic( const char *err ){
	RTEX( err );
}

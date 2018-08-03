
#include "std.h"
#include "bbsys.h"
#include "bbruntime.h"

void  bbEnd(){
	RTEX( 0 );
}
void  bbStop(){
	gx_runtime->debugStop();
}

void  bbAppTitle( String ti,String cp ){
	gx_runtime->setTitle( ti,cp );
}

void  bbRuntimeError( String str ){
	if( str.size()>255 ) str = str.substr(0,255);
	static char err[256];
	strcpy( err,str.cstr() );
	RTEX( err );
}

int bbExecFile( String f ){
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

String  bbSystemProperty( String p ){
	String t=gx_runtime->systemProperty( p );
	return t;
}

String   bbGetEnv( String env_var ){
	char *p=getenv( env_var.cstr() );
	String val=p ? p : "";
	return val;
}

void  bbSetEnv( String env_var,String val ){
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

void  bbDebugLog( String t ){
	gx_runtime->debugLog( t.cstr() );
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

bool basic_create();
bool basic_destroy();
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
bool bank_create();
bool bank_destroy();
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
	string p=string( "Startup Error: " )+t;
	gx_runtime->debugInfo( p.c_str() );
}

bool bbruntime_create(HINSTANCE hinst){
    gx_runtime = gxRuntime::openRuntime(hinst, "");
	if( basic_create() ){
		if( math_create() ){
			if( string_create() ){
				if( stream_create() ){
					if( sockets_create() ){
						if( filesystem_create() ){
							if( bank_create() ){
								if( graphics_create() ){
									if( input_create() ){
										if( audio_create() ){
											if( blitz3d_create() ){
												return true;
											}else sue( "blitz3d_create failed" );
											audio_destroy();
										}else sue( "audio_create failed" );
										input_destroy();
									}else sue( "input_create failed" );
									graphics_destroy();
								}else sue( "graphics_create failed" );
								bank_destroy();
							}else sue( "bank_create failed" );
							filesystem_destroy();
						}else sue( "filesystem_create failed" );
						sockets_destroy();
					}else sue( "sockets_create failed" );
					stream_destroy();
				}else sue( "stream_create failed" );
				string_destroy();
			}else sue( "string_create failed" );
			math_destroy();
		}else sue( "math_create failed" );
		basic_destroy();
	}else sue( "basic_create failed" );
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;
	return false;
}

bool bbruntime_destroy(){
	blitz3d_destroy();
	audio_destroy();
	input_destroy();
	graphics_destroy();
	bank_destroy();
	filesystem_destroy();
	sockets_destroy();
	stream_destroy();
	string_destroy();
	math_destroy();
	basic_destroy();
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;
	return true;
}

void bbruntime_panic( const char *err ){
	RTEX( err );
}

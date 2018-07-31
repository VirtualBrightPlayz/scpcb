
#include "std.h"
#include "bbsys.h"
#include "bbruntime.h"

void  bbEnd(){
	RTEX( 0 );
}
void  bbStop(){
	gx_runtime->debugStop();
	if( !gx_runtime->idle() ) RTEX( 0 );
}

void  bbAppTitle( BBStr *ti,BBStr *cp ){
	gx_runtime->setTitle( *ti,*cp );
	delete ti;delete cp;
}

void  bbRuntimeError( BBStr *str ){
	string t=*str;delete str;
	if( t.size()>255 ) t[255]=0;
	static char err[256];
	strcpy( err,t.c_str() );
	RTEX( err );
}

int   bbExecFile( BBStr *f ){
	string t=*f;delete f;
	int n=gx_runtime->execute( t );
	if( !gx_runtime->idle() ) RTEX( 0 );
	return n;
}

void  bbDelay( int ms ){
	if( !gx_runtime->delay( ms ) ) RTEX( 0 );
}

int  bbMilliSecs(){
	return gx_runtime->getMilliSecs();
}

BBStr * bbCommandLine(){
	return d_new BBStr( gx_runtime->commandLine() );
}

BBStr * bbSystemProperty( BBStr *p ){
	string t=gx_runtime->systemProperty( *p );
	delete p;return d_new BBStr( t );
}

BBStr *  bbGetEnv( BBStr *env_var ){
	char *p=getenv( env_var->c_str() );
	BBStr *val=d_new BBStr( p ? p : "" );
	delete env_var;
	return val;
}

void  bbSetEnv( BBStr *env_var,BBStr *val ){
	string t=*env_var+"="+*val;
	putenv( t.c_str() );
	delete env_var;
	delete val;
}

gxTimer * bbCreateTimer( int hertz ){
	gxTimer *t=gx_runtime->createTimer( hertz );
	return t;
}

int   bbWaitTimer( gxTimer *t ){
	int n=t->wait();
	if( !gx_runtime->idle() ) RTEX( 0 );
	return n;
}

void  bbFreeTimer( gxTimer *t ){
	gx_runtime->freeTimer( t );
}

void  bbDebugLog( BBStr *t ){
	gx_runtime->debugLog( t->c_str() );
	delete t;
}

void  _bbDebugStmt( int pos,const char *file ){
	gx_runtime->debugStmt( pos,file );
	if( !gx_runtime->idle() ) RTEX( 0 );
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

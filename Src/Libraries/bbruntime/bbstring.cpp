#include "bbsys.h"
#include <time.h>

#define CHKPOS(x) if( (x)<0 ) RTEX( "parameter must be positive" );
#define CHKOFF(x) if( (x)<=0 ) RTEX( "parameter must be greater than 0" );

// TODO: Move all these functions to string type methods?
String bbString( const String& s,int n ){
	String t="";
	while( n-- > 0 ) t=String(t,s);
	return t;
}

String bbRight( const String& s,int n ){
	CHKPOS( n );
	n=s.size()-n;if( n<0 ) n=0;
	return s.substr( n );
}

int bbInstr( const String& s,const String& t,int from ){
	return s.findFirst(t,from)+1;
}

String bbMid( const String& s,int o,int n ){
	return s.substr(o-1,n);
}

String bbChr( int n ){
	return String((char)n);
}

String bbHex( int n ){
	char buff[12];
	for( int k=7;k>=0;n>>=4,--k ){
		int t=(n&15)+'0';
		buff[k]=t>'9' ? t+='A'-'9'-1 : t;
	}
	buff[8]=0;
	return String(buff);
}

String bbBin( int n ){
	char buff[36];
	for( int k=31;k>=0;n>>=1,--k ){
		buff[k]=n&1 ? '1' : '0';
	}
	buff[32]=0;
	return String( buff );
}

int bbAsc( const String& s ){
	int n=s.size() ? s.charAt(0) & 255 : -1;
	return n;
}

String bbCurrentDate(){
	time_t t;
	time( &t );
	char buff[256];
	strftime( buff,256,"%d %b %Y",localtime( &t ) );
	return String( buff );
}

String bbCurrentTime(){
	time_t t;
	time( &t );
	char buff[256];
	strftime( buff,256,"%H:%M:%S",localtime( &t ) );
	return String( buff );
}

bool string_create(){
	return true;
}

bool string_destroy(){
	return true;
}

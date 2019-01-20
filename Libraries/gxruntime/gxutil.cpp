
#include "gxutil.h"

#include <Windows.h>

int atoi( const String& s ){
	return atoi( s.cstr() );
}

double atof( const String& s ){
	return atof( s.cstr() );
}

String itoa( int n ){
	return String(n);
}

String ftoa( float n ){
    return String(n);
}

String tolower( const String& s ){
	return s.toLower();
}

String toupper( const String& s ){
	return s.toUpper();
}

String fullfilename( const String& t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	return String(buff);
}

String filenamepath( const String& t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	if( !p ) return "";
	*p=0;return String(buff);
}

String filenamefile( const String& t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	if( !p ) return "";
	return String( p );
}

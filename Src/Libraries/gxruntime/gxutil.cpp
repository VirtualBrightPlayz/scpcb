
#include "gxutil.h"

#include <Windows.h>

int atoi( String s ){
	return atoi( s.cstr() );
}

double atof( String s ){
	return atof( s.cstr() );
}

String itoa( int n ){
	return String(n);
}

String ftoa( float n ){
    return String(n);
}

String tolower( String s ){
	return s.toLower();
}

String toupper( String s ){
	return s.toUpper();
}

String fullfilename( String t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	return String(buff);
}

String filenamepath( String t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	if( !p ) return "";
	*p=0;return String(buff);
}

String filenamefile( String t ){
	char buff[MAX_PATH+1],*p;
	GetFullPathName( t.cstr(),MAX_PATH,buff,&p );
	if( !p ) return "";
	return String( p );
}

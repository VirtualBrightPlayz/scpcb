
#ifndef GXUTIL_H
#define GXUTIL_H

#include "StringType.h"

//TODO: remove the stuff String already implements
int atoi(const String& s );
double atof(const String& s );
String itoa( int n );
String ftoa( float n );
String tolower( const String& s );
String toupper( const String& s );

//keep this tho
String fullfilename( const String& t );
String filenamepath( const String& t );
String filenamefile( const String& t );

#endif

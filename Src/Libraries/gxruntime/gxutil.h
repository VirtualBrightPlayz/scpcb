
#ifndef GXUTIL_H
#define GXUTIL_H

#include "StringType.h"

//TODO: remove the stuff String already implements
int atoi(String s );
double atof(String s );
String itoa( int n );
String ftoa( float n );
String tolower( String s );
String toupper( String s );

//keep this tho
String fullfilename( String t );
String filenamepath( String t );
String filenamefile( String t );

#endif

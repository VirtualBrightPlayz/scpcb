
#ifndef BBSTRING_H
#define BBSTRING_H

#include "../gxruntime/StringType.h"

String   bbString( const String& s,int n );
String 	 bbRight( const String& s,int n );
int		 bbInstr( const String& s,const String& t,int from=1 );
String 	 bbMid( const String& s,int o,int n );
String   bbChr( int n );
int		 bbAsc( const String& s );
String 	 bbHex( int n );
String   bbBin( int n );
String   bbCurrentDate();
String   bbCurrentTime();

#endif
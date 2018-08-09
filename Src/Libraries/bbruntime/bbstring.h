
#ifndef BBSTRING_H
#define BBSTRING_H

#include "basic.h"
#include "../gxruntime/StringType.h"

String   bbString( String s,int n );
String 	 bbRight( String s,int n );
int		 bbInstr( String s,String t,int from=1 );
String 	 bbMid( String s,int o,int n );
String   bbChr( int n );
int		 bbAsc( String s );
String 	 bbHex( int n );
String   bbBin( int n );
String   bbCurrentDate();
String   bbCurrentTime();

#endif
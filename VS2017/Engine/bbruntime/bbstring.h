
#ifndef BBSTRING_H
#define BBSTRING_H

#include "basic.h"

String   bbString( String s,int n );
String 	 bbLeft( String s,int n );
String 	 bbRight( String s,int n );
String   bbReplace( String s,String from,String to );
int		 bbInstr( String s,String t,int from );
String 	 bbMid( String s,int o,int n );
String   bbUpper( String s );
String 	 bbLower( String s );
String   bbTrim( String s );
String   bbLSet( String s,int n );
String   bbRSet( String s,int n );
String   bbChr( int n );
int		 bbAsc( String s );
int		 bbLen( String s );
String 	 bbHex( int n );
String   bbBin( int n );
String   bbCurrentDate();
String   bbCurrentTime();

#endif

#include "std.h"
#include "bbsys.h"
#include <time.h>

#define CHKPOS(x) if( (x)<0 ) RTEX( "parameter must be positive" );
#define CHKOFF(x) if( (x)<=0 ) RTEX( "parameter must be greater than 0" );

String bbString( String s,int n ){
	String t="";
	while( n-- > 0 ) t=String(t,s);
	return t;
}

String bbLeft( String s,int n ){
	CHKPOS( n );
	s=s.substr( 0,n );return s;
}

String bbRight( String s,int n ){
	CHKPOS( n );
	n=s.size()-n;if( n<0 ) n=0;
	s=s.substr( n );return s;
}

String bbReplace( String s,String from,String to ){
	int n=0,from_sz=from->size(),to_sz=to->size();
	while( n<s->size() && (n=s->find( *from,n ))!=string::npos ){
		s->replace( n,from_sz,*to );
		n+=to_sz;
	}
	delete from;delete to;return s;
}

int bbInstr( String s,String t,int from ){
	CHKOFF( from );--from;
	int n=s->find( *t,from );
	delete s;delete t;
	return n==string::npos ? 0 : n+1;
}

String bbMid( String s,int o,int n ){
	CHKOFF( o );--o;
	if( o>s->size() ) o=s->size();
	if( n>=0 ) *s=s->substr( o,n );
	else *s=s->substr( o );
	return s;
}

String bbUpper( String s ){
	for( int k=0;k<s->size();++k ) (*s)[k]=toupper( (*s)[k] );
	return s;
}

String bbLower( String s ){
	for( int k=0;k<s->size();++k ) (*s)[k]=tolower( (*s)[k] );
	return s;
}

String bbTrim( String s ){
	int n=0,p=s->size();
	while( n<s->size() && !isgraph( (*s)[n] ) ) ++n;
	while( p>n && !isgraph( (*s)[p-1] ) ) --p;
	*s=s->substr( n,p-n );return s;
}

String bbLSet( String s,int n ){
	CHKPOS(n);
	if( s->size()>n ) *s=s->substr( 0,n );
	else{
		while( s->size()<n ) *s+=' ';
	}
	return s;
}

String bbRSet( String s,int n ){
	CHKPOS(n);
	if( s->size()>n ) *s=s->substr( s->size()-n );
	else{
		while( s->size()<n ) *s=' '+*s;
	}
	return s;
}

String bbChr( int n ){
	String t=d_new "";
	*t+=(char)n;return t;
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

int bbAsc( String s ){
	int n=s->size() ? (*s)[0] & 255 : -1;
	delete s;return n;
}

int bbLen( String s ){
	int n=s->size();
	delete s;return n;
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

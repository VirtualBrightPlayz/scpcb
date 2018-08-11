#include <set>

#include "../gxruntime/StringType.h"

#include "bbstream.h"

static std::set<bbStream*> stream_set;

void debugStream( bbStream *s ){
	if( stream_set.count(s) ) return;
	RTEX( "Stream does not exist" );
}

bbStream::bbStream(){
	stream_set.insert( this );
}

bbStream::~bbStream(){
	stream_set.erase( this );
}

int bbEof( bbStream *s ){
	if( debug ) debugStream( s );
	return s->eof();
}

int bbReadAvail( bbStream *s ){
	if( debug ) debugStream( s );
	return s->avail();
}

int bbReadByte( bbStream *s ){
	if( debug ) debugStream( s );
	int n=0;
	s->read( (char*)&n,1 );
	return n;
}

int bbReadShort( bbStream *s ){
	if( debug ) debugStream( s );
	int n=0;
	s->read( (char*)&n,2 );
	return n;
}

int bbReadInt( bbStream *s ){
	if( debug ) debugStream( s );
	int n=0;
	s->read( (char*)&n,4 );
	return n;
}

float bbReadFloat( bbStream *s ){
	if( debug ) debugStream( s );
	float n=0;
	s->read( (char*)&n,4 );
	return n;
}

String bbReadString( bbStream *s ){
	if( debug ) debugStream( s );
	int len;
	String str="";
	if( s->read( (char*)&len,4 ) ){
		char *buff=new char[len+1];
		if( s->read( buff,len ) ){
            buff[len]='\0';
			str=String( buff );
		}
		delete[] buff;
	}
	return str;
}

String bbReadLine( bbStream *s ){
	if( debug ) debugStream( s );
	unsigned char c;
	std::vector<char> str;
	for(;;){
		if( s->read( (char*)&c,1 )!=1 ) break;
		if( c=='\n' ) break;
		if( c!='\r' ) str.push_back((char)c);
	}
    str.push_back('\0');
	return String(str.data());
}

void bbWriteByte( bbStream *s,int n ){
	if( debug ) debugStream( s );
	s->write( (char*)&n,1 );
}

void bbWriteShort( bbStream *s,int n ){
	if( debug ) debugStream( s );
	s->write( (char*)&n,2 );
}

void bbWriteInt( bbStream *s,int n ){
	if( debug ) debugStream( s );
	s->write( (char*)&n,4 );
}

void bbWriteFloat( bbStream *s,float n ){
	if( debug ) debugStream( s );
	s->write( (char*)&n,4 );
}

void bbWriteString( bbStream *s,const String& t ){
	if( debug ) debugStream( s );
	int n=t.size();
	s->write( (char*)&n,4 );
	s->write( t.cstr(),t.size() );
}

void bbWriteLine( bbStream *s,const String& t ){
	if( debug ) debugStream( s );
	s->write( t.cstr(),t.size() );
	s->write( "\r\n",2 ); //TODO: change to lf only?
}

void bbCopyStream( bbStream *s,bbStream *d,int buff_size ){
	if( debug ){
		debugStream( s );debugStream( d );
		if( buff_size<1 || buff_size>1024*1024 ) RTEX( "Illegal buffer size" );
	}
	char *buff=new char[buff_size];
	while( s->eof()==0 && d->eof()==0 ){
		int n=s->read( buff,buff_size );
		d->write( buff,n );
		if( n<buff_size ) break;
	}
	delete buff;
}

bool stream_create(){
	return true;
}

bool stream_destroy(){
	return true;
}


#include "bbbank.h"
#include "bbstream.h"

static set<bbBank*> bank_set;

static inline void debugBank( bbBank *b ){
	if( debug ){
		if( !bank_set.count( b ) ) RTEX( "bbBank does not exist" );
	}
}

static inline void debugBank( bbBank *b,int offset ){
	if( debug ){
		debugBank( b );
		if( offset>=b->size ) RTEX( "Offset out of range" );
	}
}

bbBank *bbCreateBank( int size ){
	bbBank *b=new bbBank( size );
	bank_set.insert( b );
	return b;
}

void bbFreeBank( bbBank *b ){
	if( bank_set.erase( b ) ) delete b;
}

int bbBankSize( bbBank *b ){
	debugBank( b );
	return b->size;
}

void  bbResizeBank( bbBank *b,int size ){
	debugBank( b );
	b->resize( size );
}

void  bbCopyBank( bbBank *src,int src_p,bbBank *dest,int dest_p,int count ){
	if( debug ){ debugBank( src,src_p+count-1 );debugBank( dest,dest_p+count-1 ); }
	memmove( dest->data+dest_p,src->data+src_p,count );
}

int  bbPeekByte( bbBank *b,int offset ){
	debugBank( b,offset );
	return *(unsigned char*)(b->data+offset);
}

int  bbPeekShort( bbBank *b,int offset ){
	debugBank( b,offset+1 );
	return *(unsigned short*)(b->data+offset);
}

int  bbPeekInt( bbBank *b,int offset ){
	debugBank( b,offset+3 );
	return *(int*)(b->data+offset);
}

float  bbPeekFloat( bbBank *b,int offset ){
	debugBank( b,offset+3 );
	return *(float*)(b->data+offset);
}

void  bbPokeByte( bbBank *b,int offset,int value ){
	debugBank( b,offset );
	*(char*)(b->data+offset)=value;
}

void  bbPokeShort( bbBank *b,int offset,int value ){
	debugBank( b,offset );
	*(unsigned short*)(b->data+offset)=value;
}

void  bbPokeInt( bbBank *b,int offset,int value ){
	debugBank( b,offset );
	*(int*)(b->data+offset)=value;
}

void  bbPokeFloat( bbBank *b,int offset,float value ){
	debugBank( b,offset );
	*(float*)(b->data+offset)=value;
}

int   bbReadBytes( bbBank *b,bbStream *s,int offset,int count ){
	if( debug ){
		debugBank( b,offset+count-1 );
		debugStream( s );
	}
	return s->read( b->data+offset,count );
}

int   bbWriteBytes( bbBank *b,bbStream *s,int offset,int count ){
	if( debug ){
		debugBank( b,offset+count-1 );
		debugStream( s );
	}
	return s->write( b->data+offset,count );
}

bool bank_create(){
	return true;
}

bool bank_destroy(){
	while( bank_set.size() ) bbFreeBank( *bank_set.begin() );
	return true;
}

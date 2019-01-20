
#ifndef BBSTREAM_H
#define BBSTREAM_H

#include "bbsys.h"

class bbStream{
public:
	enum{
		EOF_ERROR=-1,EOF_NOT=0,EOF_OK=1
	};

	bbStream();
	virtual ~bbStream();

	//returns chars read
	virtual int read( char *buff,int size )=0;

	//returns chars written
	virtual int write( const char *buff,int size )=0;

	//returns chars avilable for reading
	virtual int avail()=0;

	//returns EOF status
	virtual int eof()=0;
};

void debugStream( bbStream *s );

int bbEof(class bbStream* s);
int bbReadAvail(class bbStream* s);
int bbReadByte(class bbStream* s);
int bbReadShort(class bbStream* s);
int bbReadInt(class bbStream* s);
float bbReadFloat(class bbStream* s);
String bbReadString(class bbStream* s);
String bbReadLine(class bbStream* s);
void bbWriteByte(class bbStream* s, int n);
void bbWriteShort(class bbStream* s, int n);
void bbWriteInt(class bbStream* s, int n);
void bbWriteFloat(class bbStream* s, float n);
void bbWriteString(class bbStream* s, const String& t);
void bbWriteLine(class bbStream* s, const String& t);

#endif
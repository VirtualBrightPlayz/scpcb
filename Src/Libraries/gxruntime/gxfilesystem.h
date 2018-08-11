
#ifndef GXFILESYSTEM_H
#define GXFILESYSTEM_H

#include "gxdir.h"
#include "StringType.h"

class gxFileSystem{
public:
	gxFileSystem();
	~gxFileSystem();

	/***** GX INTERFACE *****/
public:
	enum{
		FILE_TYPE_NONE=0,FILE_TYPE_FILE=1,FILE_TYPE_DIR=2
	};

	bool createDir( const String& dir );
	bool deleteDir( const String& dir );
	bool createFile( const String& file );
	bool deleteFile( const String& file );
	bool copyFile( const String& src,const String& dest );
	bool renameFile( const String& src,const String& dest );
	bool setCurrentDir( const String& dir );

	String getCurrentDir()const;
	int getFileSize( const String& name )const;
	int getFileType( const String& name )const;

	gxDir *openDir( const String& name,int flags );
	gxDir *verifyDir( gxDir *d );
	void closeDir( gxDir *dir );
};

#endif

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

	bool createDir( String dir );
	bool deleteDir( String dir );
	bool createFile( String file );
	bool deleteFile( String file );
	bool copyFile( String src,String dest );
	bool renameFile( String src,String dest );
	bool setCurrentDir( String dir );

	std::string getCurrentDir()const;
	int getFileSize( String name )const;
	int getFileType( String name )const;

	gxDir *openDir( String name,int flags );
	gxDir *verifyDir( gxDir *d );
	void closeDir( gxDir *dir );
};

#endif
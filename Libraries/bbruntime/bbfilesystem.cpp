#include <fstream>
#include <set>

#include "bbfilesystem.h"
#include "bbstream.h"

gxFileSystem *gx_filesys;

static std::set<bbFile*> file_set;

static inline void debugFile( bbFile *f ){
	if( debug ){
		if( !file_set.count( f ) ) RTEX( "File does not exist" );
	}
}

static inline void debugDir( gxDir *d ){
	if( debug ){
		if( !gx_filesys->verifyDir( d ) ) RTEX( "Directory does not exist" );
	}
}

static bbFile *open( const String& t,int n ){
    std::filebuf *buf=new std::filebuf();
	if( buf->open( t.cstr(),n|std::ios_base::binary ) ){
		bbFile *f=new bbFile( buf );
		file_set.insert( f );
		return f;
	}
	delete buf;
	return 0;
}

bbFile *bbReadFile( const String& f ){
	return open( f, std::ios_base::in );
}

bbFile *bbWriteFile( const String& f ){
	return open( f, std::ios_base::out| std::ios_base::trunc );
}

bbFile *bbOpenFile(const String& f) {
	return open( f, std::ios_base::in| std::ios_base::out );
}

void bbCloseFile( bbFile *f ){
	debugFile( f );
	file_set.erase( f );
	delete f;
}

int bbFilePos( bbFile *f ){
	return f->buf->pubseekoff( 0, std::ios_base::cur );
}

int bbSeekFile( bbFile *f,int pos ){
	return f->buf->pubseekoff( pos, std::ios_base::beg );
}

gxDir *bbReadDir( const String& d ){
	return gx_filesys->openDir( d,0 );
}

void bbCloseDir( gxDir *d ){
	gx_filesys->closeDir( d );
}

String bbNextFile( gxDir *d ){
	debugDir( d );
	return d->getNextFile();
}

String bbCurrentDir(){
	return gx_filesys->getCurrentDir();
}

void bbChangeDir( const String& d ){
	gx_filesys->setCurrentDir( d );
}

void bbCreateDir( const String& d ){
	gx_filesys->createDir( d );
}

void bbDeleteDir( const String& d ){
	gx_filesys->deleteDir( d );
}

int bbFileType( const String& f ){
	int n=gx_filesys->getFileType( f );
	return n==gxFileSystem::FILE_TYPE_FILE ? 1 : (n==gxFileSystem::FILE_TYPE_DIR ? 2 : 0);
}

int	bbFileSize( const String& f ){
	return gx_filesys->getFileSize( f );
}

void bbCopyFile( const String& src,const String& dest ){
	gx_filesys->copyFile( src,dest );
}

void bbDeleteFile( const String& f ){
	gx_filesys->deleteFile( f );
}

bool filesystem_create(){
	if( gx_filesys=gx_runtime->openFileSystem( 0 ) ){
		return true;
	}
	return false;
}

bool filesystem_destroy(){
	while( file_set.size() ) bbCloseFile( *file_set.begin() );
	gx_runtime->closeFileSystem( gx_filesys );
	return true;
}

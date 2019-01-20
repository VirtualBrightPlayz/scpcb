#include <set>

#include "gxfilesystem.h"
#include "StringType.h"

static std::set<gxDir*> dir_set;

gxFileSystem::gxFileSystem(){
	dir_set.clear();
}

gxFileSystem::~gxFileSystem(){
	while( dir_set.size() ) closeDir( *dir_set.begin() );
}

bool gxFileSystem::createDir( const String& dir ){
	return CreateDirectory( dir.cstr(),0 ) ? true : false;
}

bool gxFileSystem::deleteDir( const String& dir ){
	return RemoveDirectory( dir.cstr() ) ? true : false;
}

bool gxFileSystem::createFile( const String& file ){
	return false;
}

bool gxFileSystem::deleteFile( const String& file ){
	return DeleteFile( file.cstr() ) ? true : false;
}

bool gxFileSystem::copyFile( const String& src,const String& dest ){
	return CopyFile( src.cstr(),dest.cstr(),false ) ? true : false;
}

bool gxFileSystem::renameFile( const String& src,const String& dest ){
	return MoveFile( src.cstr(),dest.cstr() ) ? true : false;
}

bool gxFileSystem::setCurrentDir( const String& dir ){
	return SetCurrentDirectory( dir.cstr()) ? true : false;
}

String gxFileSystem::getCurrentDir()const{
	char buff[MAX_PATH];
	if( !GetCurrentDirectory( MAX_PATH,buff ) ) return "";
	String t=buff;if( t.size() && t.charAt(t.size()-1)!='\\' ) t=String(t,"\\");
	return t;
}

int gxFileSystem::getFileSize( const String& name )const{
	WIN32_FIND_DATA findData;
	HANDLE h=FindFirstFile( name.cstr(),&findData );
	if( h==INVALID_HANDLE_VALUE ) return 0;
	int n=findData.dwFileAttributes,sz=findData.nFileSizeLow;
	FindClose( h );return n & FILE_ATTRIBUTE_DIRECTORY ? 0 : sz;
}

int gxFileSystem::getFileType( const String& name )const{
	DWORD t=GetFileAttributes( name.cstr() );
	return t==-1 ? FILE_TYPE_NONE :
	(t & FILE_ATTRIBUTE_DIRECTORY ? FILE_TYPE_DIR : FILE_TYPE_FILE);
}

gxDir *gxFileSystem::openDir( const String& name,int flags ){
	String t=name.cstr();
	if( t.charAt(t.size()-1)=='\\' ) t=String(t,"*");
	else t=String(t,"\\*");
	WIN32_FIND_DATA f;
	HANDLE h=FindFirstFile( t.cstr(),&f );
	if( h!=INVALID_HANDLE_VALUE ){
		gxDir *d=new gxDir( h,f );
		dir_set.insert( d );
		return d;
	}
	return 0;
}
gxDir *gxFileSystem::verifyDir( gxDir *d ){
	return dir_set.count(d) ? d : 0;
}

void gxFileSystem::closeDir( gxDir *d ){
	if( dir_set.erase( d ) ) delete d;
}

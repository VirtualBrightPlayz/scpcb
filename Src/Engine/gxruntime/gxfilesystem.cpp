
#include "std.h"
#include "gxfilesystem.h"
#include "StringType.h"

static set<gxDir*> dir_set;

gxFileSystem::gxFileSystem(){
	dir_set.clear();
}

gxFileSystem::~gxFileSystem(){
	while( dir_set.size() ) closeDir( *dir_set.begin() );
}

bool gxFileSystem::createDir( String dir ){
	return CreateDirectory( dir.cstr(),0 ) ? true : false;
}

bool gxFileSystem::deleteDir( String dir ){
	return RemoveDirectory( dir.cstr() ) ? true : false;
}

bool gxFileSystem::createFile( String file ){
	return false;
}

bool gxFileSystem::deleteFile( String file ){
	return DeleteFile( file.cstr() ) ? true : false;
}

bool gxFileSystem::copyFile( String src,String dest ){
	return CopyFile( src.cstr(),dest.cstr(),false ) ? true : false;
}

bool gxFileSystem::renameFile( String src,String dest ){
	return MoveFile( src.cstr(),dest.cstr() ) ? true : false;
}

bool gxFileSystem::setCurrentDir( String dir ){
	return SetCurrentDirectory( dir.cstr()) ? true : false;
}

string gxFileSystem::getCurrentDir()const{
	char buff[MAX_PATH];
	if( !GetCurrentDirectory( MAX_PATH,buff ) ) return "";
	string t=buff;if( t.size() && t[t.size()-1]!='\\' ) t+='\\';
	return t;
}

int gxFileSystem::getFileSize( String name )const{
	WIN32_FIND_DATA findData;
	HANDLE h=FindFirstFile( name.cstr(),&findData );
	if( h==INVALID_HANDLE_VALUE ) return 0;
	int n=findData.dwFileAttributes,sz=findData.nFileSizeLow;
	FindClose( h );return n & FILE_ATTRIBUTE_DIRECTORY ? 0 : sz;
}

int gxFileSystem::getFileType( String name )const{
	DWORD t=GetFileAttributes( name.cstr() );
	return t==-1 ? FILE_TYPE_NONE :
	(t & FILE_ATTRIBUTE_DIRECTORY ? FILE_TYPE_DIR : FILE_TYPE_FILE);
}

gxDir *gxFileSystem::openDir( String name,int flags ){
	string t=name.cstr();
	if( t[t.size()-1]=='\\' ) t+="*";
	else t+="\\*";
	WIN32_FIND_DATA f;
	HANDLE h=FindFirstFile( t.c_str(),&f );
	if( h!=INVALID_HANDLE_VALUE ){
		gxDir *d=d_new gxDir( h,f );
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

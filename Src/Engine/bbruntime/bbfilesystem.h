
#ifndef BBFILESYSTEM_H
#define BBFILESYSTEM_H

#include "bbsys.h"
#include "../gxruntime/gxfilesystem.h"

extern gxFileSystem *gx_filesys;

class bbFile* bbOpenFile(String f);
class bbFile* bbReadFile(String f);
class bbFile* bbWriteFile(String f);
class gxDir* bbReadDir(String d);
void bbCloseDir(class gxDir* d);
String bbNextFile(class gxDir* d);
String bbCurrentDir();
void bbChangeDir(String d);
void bbCreateDir(String d);
void bbDeleteDir(String d);
int bbFileSize(String f);
int bbFileType(String f);
void bbCopyFile(String f, String to);
void bbDeleteFile(String f);

#endif

#ifndef BBFILESYSTEM_H
#define BBFILESYSTEM_H

#include "bbsys.h"
#include "../gxruntime/gxfilesystem.h"

extern gxFileSystem *gx_filesys;

class bbFile* bbOpenFile(class BBStr* f);
class bbFile* bbReadFile(class BBStr* f);
class bbFile* bbWriteFile(class BBStr* f);
class gxDir* bbReadDir(class BBStr* d);
void bbCloseDir(class gxDir* d);
class BBStr* bbNextFile(class gxDir* d);
class BBStr* bbCurrentDir();
void bbChangeDir(class BBStr* d);
void bbCreateDir(class BBStr* d);
void bbDeleteDir(class BBStr* d);
int bbFileSize(class BBStr* f);
int bbFileType(class BBStr* f);
void bbCopyFile(class BBStr* f, class BBStr* to);
void bbDeleteFile(class BBStr* f);

#endif

#ifndef BBFILESYSTEM_H
#define BBFILESYSTEM_H

#include "bbsys.h"
#include "../gxruntime/gxfilesystem.h"

extern gxFileSystem *gx_filesys;

class bbFile* bbOpenFile(struct BBStr* f);
class bbFile* bbReadFile(struct BBStr* f);
class bbFile* bbWriteFile(struct BBStr* f);
class gxDir* bbReadDir(struct BBStr* d);
void bbCloseDir(class gxDir* d);
struct BBStr* bbNextFile(class gxDir* d);
struct BBStr* bbCurrentDir();
void bbChangeDir(struct BBStr* d);
void bbCreateDir(struct BBStr* d);
void bbDeleteDir(struct BBStr* d);
int bbFileSize(struct BBStr* f);
int bbFileType(struct BBStr* f);
void bbCopyFile(struct BBStr* f, struct BBStr* to);
void bbDeleteFile(struct BBStr* f);

#endif
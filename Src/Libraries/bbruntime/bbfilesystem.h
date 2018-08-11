
#ifndef BBFILESYSTEM_H
#define BBFILESYSTEM_H

#include <fstream>

#include "bbsys.h"
#include "bbstream.h"
#include "../gxruntime/gxfilesystem.h"

struct bbFile : public bbStream {
    std::filebuf *buf;
    bbFile(std::filebuf *f) :buf(f) {
    }
    ~bbFile() {
        delete buf;
    }
    int read(char *buff, int size) {
        return buf->sgetn((char*)buff, size);
    }
    int write(const char *buff, int size) {
        return buf->sputn((char*)buff, size);
    }
    int avail() {
        return buf->in_avail();
    }
    int eof() {
        return buf->sgetc() == EOF;
    }
};

extern gxFileSystem *gx_filesys;

class bbFile* bbOpenFile(const String& f);
class bbFile* bbReadFile(const String& f);
class bbFile* bbWriteFile(const String& f);
void bbCloseFile(bbFile* f);
int bbFilePos(bbFile* f);
class gxDir* bbReadDir(const String& d);
void bbCloseDir(class gxDir* d);
String bbNextFile(class gxDir* d);
String bbCurrentDir();
void bbChangeDir(const String& d);
void bbCreateDir(const String& d);
void bbDeleteDir(const String& d);
int bbFileSize(const String& f);
int bbFileType(const String& f);
void bbCopyFile(const String& f, const String& to);
void bbDeleteFile(const String& f);

#endif
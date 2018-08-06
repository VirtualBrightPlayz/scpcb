
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

class bbFile* bbOpenFile(String f);
class bbFile* bbReadFile(String f);
class bbFile* bbWriteFile(String f);
void bbCloseFile(bbFile* f);
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
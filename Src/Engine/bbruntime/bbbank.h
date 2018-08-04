
#ifndef BBBANK_H
#define BBBANK_H

#include "bbsys.h"

struct bbBank {
    char *data;
    int size, capacity;

    bbBank(int sz) :size(sz) {
        capacity = (size + 15)&~15;
        data = d_new char[capacity];
        memset(data, 0, size);
    }
    virtual ~bbBank() {
        delete[] data;
    }
    void resize(int n) {
        if (n>size) {
            if (n>capacity) {
                capacity = capacity * 3 / 2;
                if (n>capacity) capacity = n;
                capacity = (capacity + 15)&~15;
                char *p = d_new char[capacity];
                memcpy(p, data, size);
                delete[] data;
                data = p;
            }
            else memset(data + size, 0, n - size);
        }
        size = n;
    }
};

class bbBank* bbCreateBank(int size);
void bbFreeBank(class bbBank* b);
int bbBankSize(class bbBank* b);
void bbResizeBank(class bbBank* b, int size);
int bbPeekByte(class bbBank* b, int offset);
int bbPeekShort(class bbBank* b, int offset);
int bbPeekInt(class bbBank* b, int offset);
float bbPeekFloat(class bbBank* b, int offset);
void bbPokeByte(class bbBank* b, int offset, int value);
void bbPokeShort(class bbBank* b, int offset, int value);
void bbPokeInt(class bbBank* b, int offset, int value);
void bbPokeFloat(class bbBank* b, int offset, float value);
int  bbReadBytes(class bbBank* b, class bbStream* s, int offset, int count);
int  bbWriteBytes(class bbBank* b, class bbStream* s, int offset, int count);

#endif

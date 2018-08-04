#include "Array.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<IntArray*> IntArray::list;
IntArray::IntArray() {
    list.push_back(this);
}
IntArray::~IntArray() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int IntArray::getListSize() {
    return list.size();
}
IntArray* IntArray::getObject(int index) {
    return list[index];
}

std::vector<IntArrayList*> IntArrayList::list;
IntArrayList::IntArrayList() {
    list.push_back(this);
}
IntArrayList::~IntArrayList() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int IntArrayList::getListSize() {
    return list.size();
}
IntArrayList* IntArrayList::getObject(int index) {
    return list[index];
}

// Constants.
const int INT_SIZE = 4;
const int ARRAYLIST_INITIAL_CAPACITY = 8;

// Functions.
IntArray* CreateIntArray(int x, int y = 1, int z = 1) {
    IntArray* array = new IntArray();
    array->bank = bbCreateBank(INT_SIZE * (x * y * z));
    array->xDim = x;
    array->yDim = y;
    array->zDim = z;
    return array;
}

int GetIntArrayElem(IntArray* array, int x, int y = 0, int z = 0) {
    return bbPeekInt(array->bank, INT_SIZE*((z*array->yDim*array->xDim)+(y*array->xDim)+x));
}

void SetIntArrayElem(IntArray* array, int val, int x, int y = 0, int z = 0) {
    bbPokeInt(array->bank, INT_SIZE * ((z*array->yDim*array->xDim)+(y*array->xDim)+x), val);
}

void DeleteIntArray(IntArray* array) {
    bbFreeBank(array->bank);
    delete array;
}

IntArrayList* CreateIntArrayList(int initialCapacity = ARRAYLIST_INITIAL_CAPACITY) {
    IntArrayList* arrayList = new IntArrayList();
    arrayList->bank = bbCreateBank(INT_SIZE * initialCapacity);
    arrayList->size = 0;
    arrayList->capacity = ARRAYLIST_INITIAL_CAPACITY;
    return arrayList;
}

int GetIntArrayListElem(IntArrayList* arrayList, int i) {
    return bbPeekInt(arrayList->bank,i*INT_SIZE);
}

void SetIntArrayListElem(IntArrayList* arrayList, int val, int i) {
    bbPokeInt(arrayList->bank,i*INT_SIZE,val);
}

void PushIntArrayListElem(IntArrayList* arrayList, int val) {
    if (arrayList->size>=arrayList->capacity) {
        arrayList->capacity = arrayList->capacity*2;
        bbResizeBank(arrayList->bank,INT_SIZE*arrayList->capacity);
    }

    SetIntArrayListElem(arrayList,val,arrayList->size);
    arrayList->size = arrayList->size+1;
}

void PopIntArrayListElem(IntArrayList* arrayList) {
    arrayList->size = arrayList->size-1;
    if (arrayList->size<0) {
        arrayList->size = 0;
    }
}

void EraseIntArrayListElem(IntArrayList* arrayList, int pos) {
    int i;
    int val;
    if (pos>=arrayList->size) {
        return;
    }

    for (i = pos; i <= arrayList->size-2; i++) {
        val = bbPeekInt(arrayList->bank,(i+1)*INT_SIZE);
        bbPokeInt(arrayList->bank,i*INT_SIZE,val);
    }
    PopIntArrayListElem(arrayList);
}

void DeleteIntArrayList(IntArrayList* arrayList) {
    bbFreeBank(arrayList->bank);
    delete arrayList;
}

}

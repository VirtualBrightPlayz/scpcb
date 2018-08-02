#ifndef ARRAY_H_INCLUDED
#define ARRAY_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct IntArray {
private:
    static std::vector<IntArray*> list;

public:
    IntArray();
    ~IntArray();
    static int getListSize();
    static IntArray* getObject(int index);

    int bank;

    int xDim;
    int yDim;
    int zDim;
};

struct IntArrayList {
private:
    static std::vector<IntArrayList*> list;

public:
    IntArrayList();
    ~IntArrayList();
    static int getListSize();
    static IntArrayList* getObject(int index);

    int bank;

    int size;
    int capacity;
};

// Constants.
extern const int INT_SIZE;
extern const int ARRAYLIST_INITIAL_CAPACITY;

// Functions.
IntArray* CreateIntArray(int x, int y = 1, int z = 1);

int GetIntArrayElem(IntArray* array, int x, int y = 0, int z = 0);

void SetIntArrayElem(IntArray* array, int val, int x, int y = 0, int z = 0);

void DeleteIntArray(IntArray* array);

IntArrayList* CreateIntArrayList(int initialCapacity = ARRAYLIST_INITIAL_CAPACITY);

int GetIntArrayListElem(IntArrayList* arrayList, int i);

void SetIntArrayListElem(IntArrayList* arrayList, int val, int i);

void PushIntArrayListElem(IntArrayList* arrayList, int val);

void PopIntArrayListElem(IntArrayList* arrayList);

void EraseIntArrayListElem(IntArrayList* arrayList, int pos);

void DeleteIntArrayList(IntArrayList* arrayList);

}
#endif // ARRAY_H_INCLUDED

#ifndef REFCOUNTER_H_INCLUDED
#define REFCOUNTER_H_INCLUDED

#include <map>

#include "NativeDefinition.h"

class RefCounter;
class RefCounterManager;

// If templates in C++ weren't so bad we could make this sooo nice!
class RefCounter : public NativeDefinition {
    protected:
        RefCounterManager* refCounterManager;

    public:
        virtual void addRef(void* ptr) = 0;
        virtual void release(void* ptr) = 0;
};

class RefCounterManager {
    private:
        std::map<void*,RefCounter*> links;

    public:
        void linkPtrToCounter(void* ptr, RefCounter* counter);
        void unlinkPtr(void* ptr);
        void addRef(void* ptr);
        void release(void* ptr);
};

#endif // REFCOUNTER_H_INCLUDED

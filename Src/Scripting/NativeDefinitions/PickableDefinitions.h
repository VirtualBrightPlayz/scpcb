#ifndef PICKABLEDEFINITIONS_H_INCLUDED
#define PICKABLEDEFINITIONS_H_INCLUDED

#include "../NativeDefinitions/RefCounter.h"

class ScriptManager;
class Pickable;

class PickableDefinitions : RefCounter {
	private:
		std::map<Pickable*,int> refCount;
		Pickable* pickableFactory();
        void addRef(void* ptr) override;
        void release(void* ptr) override;


	public:
		PickableDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr);
};

#endif // PICKABLEDEFINITIONS_H_INCLUDED
#include "RandomDefinitions.h"

#include "../ScriptManager.h"

PGE::Random* RandomDefinitions::randomFactory() {
	PGE::Random* random = ::new PGE::Random();
	refCount.emplace(random, 1);
	refCounterManager->linkPtrToCounter(random, this);
	return random;
}

PGE::Random* RandomDefinitions::randomFactorySeeded(uint64_t seed) {
	PGE::Random* random = ::new PGE::Random(seed);
	refCount.emplace(random, 1);
	refCounterManager->linkPtrToCounter(random, this);
	return random;
}

void RandomDefinitions::addRef(void* ptr) {
	PGE::Random* random = (PGE::Random*)ptr;
	if (refCount.find(random) == refCount.end()) { return; }
	refCount[random]++;
}

void RandomDefinitions::release(void* ptr) {
	PGE::Random* random = (PGE::Random*)ptr;
	if (refCount.find(random) == refCount.end()) { return; }
	refCount[random]--;

	if (refCount[random] <= 0) {
		refCount.erase(random);
		refCounterManager->unlinkPtr(random);
		::delete random;
	}
}

RandomDefinitions::RandomDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr) {
	genericRandom = ::new PGE::Random();

	engine = mgr->getAngelScriptEngine();

	refCounterManager = rcMgr;

	engine->SetDefaultNamespace("Random");
	// TODO: Consistent naming?
	engine->RegisterGlobalFunction("int get()", asMETHOD(PGE::Random, next), asCALL_THISCALL_ASGLOBAL, genericRandom);
	engine->RegisterGlobalFunction("bool getBool()", asMETHOD(PGE::Random, nextBool), asCALL_THISCALL_ASGLOBAL, genericRandom);
	engine->RegisterGlobalFunction("float getFloat()", asMETHOD(PGE::Random, nextFloat), asCALL_THISCALL_ASGLOBAL, genericRandom);
	engine->RegisterGlobalFunction("int getInt(int max)", asMETHODPR(PGE::Random, nextInt, (uint32_t), uint32_t), asCALL_THISCALL_ASGLOBAL, genericRandom);
	engine->RegisterGlobalFunction("int getInt(int min, int max)", asMETHODPR(PGE::Random, nextInt, (uint32_t, uint32_t), uint32_t), asCALL_THISCALL_ASGLOBAL, genericRandom);

	engine->SetDefaultNamespace("");
	engine->RegisterObjectType("Random", sizeof(PGE::Random), asOBJ_REF);
	engine->RegisterObjectBehaviour("Random", asBEHAVE_FACTORY, "Random@ f()", asMETHOD(RandomDefinitions, randomFactory), asCALL_THISCALL_ASGLOBAL, this);
	engine->RegisterObjectBehaviour("Random", asBEHAVE_FACTORY, "Random@ f(uint64 seed)", asMETHOD(RandomDefinitions, randomFactorySeeded), asCALL_THISCALL_ASGLOBAL, this);
	engine->RegisterObjectBehaviour("Random", asBEHAVE_ADDREF, "void f()", asMETHOD(RandomDefinitions, addRef), asCALL_THISCALL_OBJLAST, this);
	engine->RegisterObjectBehaviour("Random", asBEHAVE_RELEASE, "void f()", asMETHOD(RandomDefinitions, release), asCALL_THISCALL_OBJLAST, this);
	engine->RegisterObjectMethod("Random", "int next()", asMETHOD(PGE::Random, next), asCALL_THISCALL);
	engine->RegisterObjectMethod("Random", "bool nextBool()", asMETHOD(PGE::Random, nextBool), asCALL_THISCALL);
	engine->RegisterObjectMethod("Random", "float nextFloat()", asMETHOD(PGE::Random, nextFloat), asCALL_THISCALL);
	engine->RegisterObjectMethod("Random", "int nextInt(int max)", asMETHODPR(PGE::Random, nextInt, (uint32_t), uint32_t), asCALL_THISCALL);
	engine->RegisterObjectMethod("Random", "int nextInt(int min, int max)", asMETHODPR(PGE::Random, nextInt, (uint32_t, uint32_t), uint32_t), asCALL_THISCALL);
}

RandomDefinitions::~RandomDefinitions() {
	::delete genericRandom;
}

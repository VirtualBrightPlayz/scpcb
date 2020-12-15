#include "RandomDefinitions.h"

#include "../ScriptManager.h"

const static int randMaxHalf = RAND_MAX / 2;

static bool getRandomBoolEven() {
	int val = std::rand();
	if (val == 0) {
		return getRandomBoolEven();
	}
	return val > randMaxHalf;
}

static bool getRandomBoolOdd() {
	return std::rand() > randMaxHalf;
}

// [0, max)
static int getRandomInt(int max) {
	int val = ((double)(std::rand())) / RAND_MAX * max;
	if (val == max) {
		return getRandomInt(max);
	} else {
		return val;
	}
}

// [0, 1]
static float getRandomFloat() {
	return (float)(std::rand()) / RAND_MAX;
}

RandomDefinitions::RandomDefinitions(ScriptManager* mgr) {
	engine = mgr->getAngelScriptEngine();

	engine->SetDefaultNamespace("Random");
	// TODO: Custom random implementation, ewwwww.
	engine->RegisterGlobalFunction("bool getBool()", RAND_MAX % 2 == 0 ? asFUNCTION(getRandomBoolEven) : asFUNCTION(getRandomBoolOdd), asCALL_CDECL);
	engine->RegisterGlobalFunction("int getInt(int max)", asFUNCTION(getRandomInt), asCALL_CDECL);
	engine->RegisterGlobalFunction("float getFloat()", asFUNCTION(getRandomFloat), asCALL_CDECL);
}

#ifndef MESSAGEDEFINITIONS_H_INCLUDED
#define MESSAGEDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class MessageManager;

class MessageDefinitions : NativeDefinition {
	public:
		MessageDefinitions(ScriptManager* mgr, MessageManager* mm);
};

#endif // MESSAGEDEFINITIONS_H_INCLUDED

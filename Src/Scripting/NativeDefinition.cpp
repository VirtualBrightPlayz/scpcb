#include "NativeDefinition.h"

#include <stdexcept>

#include <Misc/String.h>
#include "../Graphics/Billboard.h"

template<class A, class B>
static B* refCast(A* a) {
	if (a == nullptr) { return nullptr; }
	return dynamic_cast<B*>(a);
}

template<class A, class B>
static B* refCastCount(A* a) {
	if (a == nullptr) { return nullptr; }
	B* b = dynamic_cast<B*>(a);
	if (b != nullptr) {
		b->addRef();
	}
	return dynamic_cast<B*>(a);
}

template<class BASE, class DERIVED>
void NativeDefinition::registerInheritance(const char* base, const char* derived) {
	if (!(engine->GetTypeInfoByName(base)->GetFlags() & asOBJ_NOCOUNT && engine->GetTypeInfoByName(derived)->GetFlags() & asOBJ_NOCOUNT)) {
		throw new std::runtime_error("POGGERS");
	}
	int err;
	err = engine->RegisterObjectMethod(base, (PGE::String(derived) + "@ opCast()").cstr(), asFUNCTION((refCast<BASE, DERIVED>)), asCALL_CDECL_OBJLAST);
	if (err < 0) { throw new std::runtime_error(PGE::String(("Function could not be registered: ") + err).cstr()); }
	err = engine->RegisterObjectMethod(derived, (PGE::String(base) + "@ opImplCast()").cstr(), asFUNCTION((refCast<DERIVED, BASE>)), asCALL_CDECL_OBJLAST);
	if (err < 0) { throw new std::runtime_error(PGE::String(("Function could not be registered: ") + err).cstr()); }
}

template<class BASE, class DERIVED>
void NativeDefinition::registerInheritanceCount(const char* base, const char* derived) {
	if (engine->GetTypeInfoByName(base)->GetFlags() & asOBJ_NOCOUNT || engine->GetTypeInfoByName(derived)->GetFlags() & asOBJ_NOCOUNT) {
		throw new std::runtime_error("POGGERS");
	}
	int err;
	err = engine->RegisterObjectMethod(base, (PGE::String(derived) + "@ opCast()").cstr(), asFUNCTION((refCastCount<BASE, DERIVED>)), asCALL_CDECL_OBJLAST);
	if (err < 0) { throw new std::runtime_error(PGE::String(("Function could not be registered: ") + err).cstr()); }
	err = engine->RegisterObjectMethod(derived, (PGE::String(base) + "@ opImplCast()").cstr(), asFUNCTION((refCastCount<DERIVED, BASE>)), asCALL_CDECL_OBJLAST);
	if (err < 0) { throw new std::runtime_error(PGE::String(("Function could not be registered: ") + err).cstr()); }
}

template void NativeDefinition::registerInheritance<Billboard, RotatedBillboard>(const char* base, const char* derived);

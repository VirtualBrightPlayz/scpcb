#include "NativeDefinition.h"

#include <stdexcept>

#include <Misc/String.h>
#include "../Graphics/Billboard.h"

static void throwOnError(int err) {
	if (err < 0) {
		throw new std::runtime_error(PGE::String(("Function could not be registered: ") + err).cstr());
	}
}

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
	throwOnError(engine->RegisterObjectMethod(base, (PGE::String(derived) + "@ opCast()").cstr(), asFUNCTION((refCast<BASE, DERIVED>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(derived, (PGE::String(base) + "@ opImplCast()").cstr(), asFUNCTION((refCast<DERIVED, BASE>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(base, ("const " + PGE::String(derived) + "@ opCast() const").cstr(), asFUNCTION((refCast<BASE, DERIVED>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(derived, ("const " + PGE::String(base) + "@ opImplCast() const").cstr(), asFUNCTION((refCast<DERIVED, BASE>)), asCALL_CDECL_OBJLAST));
}

template<class BASE, class DERIVED>
void NativeDefinition::registerInheritanceCount(const char* base, const char* derived) {
	if (engine->GetTypeInfoByName(base)->GetFlags() & asOBJ_NOCOUNT || engine->GetTypeInfoByName(derived)->GetFlags() & asOBJ_NOCOUNT) {
		throw new std::runtime_error("POGGERS");
	}
	throwOnError(engine->RegisterObjectMethod(base, (PGE::String(derived) + "@ opCast()").cstr(), asFUNCTION((refCastCount<BASE, DERIVED>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(derived, (PGE::String(base) + "@ opImplCast()").cstr(), asFUNCTION((refCastCount<DERIVED, BASE>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(base, ("const " + PGE::String(derived) + "@ opCast() const").cstr(), asFUNCTION((refCastCount<BASE, DERIVED>)), asCALL_CDECL_OBJLAST));
	throwOnError(engine->RegisterObjectMethod(derived, ("const " + PGE::String(base) + "@ opImplCast() const").cstr(), asFUNCTION((refCastCount<DERIVED, BASE>)), asCALL_CDECL_OBJLAST));
}

template void NativeDefinition::registerInheritance<Billboard, RotatedBillboard>(const char* base, const char* derived);

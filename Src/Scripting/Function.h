#ifndef FUNCTION_H_INCLUDED
#define FUNCTION_H_INCLUDED

#include <map>
#include <vector>
#include <angelscript.h>
#include <Misc/String.h>

class Script;
class ScriptManager;
class Type;

class Function {
    public:
        struct Signature {
            struct Argument {
                const Type* type;
                PGE::String name;

                Argument(const Type* t, const PGE::String& n);
            };
            std::vector<Argument> arguments;
            const Type* returnType;
            PGE::String functionName;

            PGE::String toString() const;
        };
    protected:
        Signature signature;

        //protected constructors to make this class non-instantiable
        Function() {}
        Function(const Function& other) {}
    public:
        int getArgumentIndex(const PGE::String& argument) const;

        virtual ~Function() {}

        const Signature& getSignature() const;
};

class ScriptFunction : public Function {
    private:
        asIScriptFunction* scriptFunction;
        asIScriptContext* scriptContext;

        std::map<int, PGE::String> stringArgs;

        const Type* typeFromTypeId(int typeId) const;

        Script* script;

    public:
        ScriptFunction(Script* scrpt,asIScriptFunction* asScriptFunction);
        ~ScriptFunction() override;

        void setArgument(const PGE::String& argument, int32_t i32);
        void setArgument(const PGE::String& argument, uint32_t u32);
        void setArgument(const PGE::String& argument, float f);
        void setArgument(const PGE::String& argument, double d);
        void setArgument(const PGE::String& argument, const PGE::String& s);
        void execute();
        int32_t getReturnInt32() const;
        uint32_t getReturnUInt32() const;
        float getReturnFloat() const;
        double getReturnDouble() const;
        PGE::String getReturnString() const;
};

class NativeFunction : public Function {
    private:
        ScriptManager* scriptManager;
        asSFuncPtr functionPointer;
    public:
        NativeFunction(ScriptManager* scriptMgr,asSFuncPtr fnPtr,Function::Signature sgntr);
        ~NativeFunction() override;
};

#endif

#ifndef FUNCTION_H_INCLUDED
#define FUNCTION_H_INCLUDED

#include <angelscript.h>
#include <Misc/String.h>
#include "ScriptManager.h"
#include "Script.h"

class Function {
    public:
        enum class Type {
            Int32,
            UInt32,
            Float,
            Double,
            String
        };

        static PGE::String typeToString(Type type);

        struct Signature {
            struct Argument {
                Function::Type type;
                PGE::String name;

                Argument(Function::Type t, const PGE::String& n);
            };
            std::vector<Argument> arguments;
            Function::Type returnType;
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
    public:
        ScriptFunction(Script* script,Function::Signature sgntr);
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

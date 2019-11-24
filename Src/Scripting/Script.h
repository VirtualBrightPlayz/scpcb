#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <angelscript.h>
#include <Misc/FileName.h>
#include <Misc/String.h>
#include <map>
#include <inttypes.h>

class Script {
    private:
        ScriptManager* scriptManager;
        asIScriptModule* scriptModule;
    public:
        Script(ScriptManager* scriptMgr, const PGE::FileName& filename, const PGE::String& moduleName);
        ~Script();

        asIScriptModule* getAngelScriptModule() const;

        enum class Type {
            Int32,
            UInt32,
            Float,
            Double,
            String
        };

        static PGE::String typeToString(Type type);

        class Function {
            public:
                struct Signature {
                    struct Argument {
                        Script::Type type;
                        PGE::String name;

                        Argument(Script::Type t, const PGE::String& n);
                    };
                    std::vector<Argument> arguments;
                    Script::Type returnType;
                    PGE::String functionName;
                };
            private:
                asIScriptFunction* scriptFunction;
                asIScriptContext* scriptContext;
                Signature signature;

                int getArgumentIndex(const PGE::String& argument) const;
            public:
                Function(Script* script, Signature sgntr);
                ~Function();

                const Signature& getSignature() const;
                void setArgument(const PGE::String& argument, int32_t i32);
                void setArgument(const PGE::String& argument, uint32_t u32);
                void setArgument(const PGE::String& argument, float f);
                void setArgument(const PGE::String& argument, double d);
                void setArgument(const PGE::String& argument, const PGE::String& s);
                void execute();
        };
};

#endif

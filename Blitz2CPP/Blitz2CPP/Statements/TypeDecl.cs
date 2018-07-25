using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class TypeDecl
    {
        public string Name;

        public List<Statement> Fields;

        public TypeDecl()
        {
            Fields = new List<Statement>();
        }

        public string GetVectorList() => "std::vector<" + Name + "*>";
        public string GetConstructor() => Name + "::" + Name + "()";
        public string GetDestructor() =>  Name + "::~" + Name + "()";

        public string Parse2CPP() => Parse2CPP(string.Empty);
        public string Parse2CPP(string indents)
        {
            string retVal = indents + "struct " + Name + " {";
            retVal += "\n" + indents + "public:";
            retVal += "\n" + indents + Constants.INDENTS + "static " + GetVectorList() + " list;\n";
            foreach (Statement stat in Fields)
            {
                retVal += "\n" + stat.Parse2CPP(indents + Constants.INDENTS);
            }
            retVal += "\n" + indents + "}";
            return retVal;
        }

        public override string ToString()
        {
            string retVal = "Type " + Name;
            foreach (Variable var in Fields)
            {
                retVal += "\n" + Constants.INDENTS +  var.ToString();
            }

            return retVal;
        }
    }
}
using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class TypeDecl
    {
        public string name;

        public List<Variable> fields;

        public TypeDecl()
        {
            fields = new List<Variable>();
        }

        public string GetVectorList() => "std::vector<" + name + "*>";

        public string Parse2CPP() => Parse2CPP(string.Empty);
        public string Parse2CPP(string indents)
        {
            string retVal = indents + "struct " + name + " {";
            retVal += "\n" + indents + "public:";
            retVal += "\n" + indents + Constants.INDENTS + "static " + GetVectorList() + " list;\n";
            foreach (Variable var in fields)
            {
                retVal += "\n" + var.Parse2CPP(indents + Constants.INDENTS);
            }
            retVal += "\n" + indents + "}";
            return retVal;
        }

        public override string ToString()
        {
            string retVal = "Type " + name;
            foreach (Variable var in fields)
            {
                retVal += "\n" + Constants.INDENTS +  var.ToString();
            }

            return retVal;
        }
    }
}
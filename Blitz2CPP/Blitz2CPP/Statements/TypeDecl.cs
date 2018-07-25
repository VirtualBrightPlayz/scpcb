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
        private string GetConstructorSignature() => Name + "::" + Name + "()";
        private string GetDestructorSignature() =>  Name + "::~" + Name + "()";

        public string GetConstructor()
        {
            return GetConstructorSignature() + " {\n"
                + Constants.INDENTS + "list.push_back(this);\n"
                + "}";
        }

        public string GetDestructor()
        {
            return GetDestructorSignature() + " {\n"
                + Constants.INDENTS + "for (int i = 0; i < list.size(); i++) {\n"
                + Constants.INDENTS + Constants.INDENTS + "if (list[i] == this) {\n"
                + Constants.INDENTS + Constants.INDENTS + Constants.INDENTS + "list.erase(list.begin() + i);\n"
                + Constants.INDENTS + Constants.INDENTS + Constants.INDENTS + "break;\n"
                + Constants.INDENTS + Constants.INDENTS + "}\n"
                + Constants.INDENTS + "}\n"
                + "}";
        }

        public string Parse2CPP() => Parse2CPP(string.Empty);
        public string Parse2CPP(string indents)
        {
            string retVal = indents + "struct " + Name + " {";
            retVal += "\n" + indents + "public:";
            retVal += "\n" + indents + Constants.INDENTS + "static " + GetVectorList() + " list;";
            retVal += "\n" + indents + Constants.INDENTS + GetConstructorSignature() + ";";
            retVal += "\n" + indents + Constants.INDENTS + GetDestructorSignature() + ";\n";
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
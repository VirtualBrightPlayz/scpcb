using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class Function : ScopeStatement
    {
        public string name;

        public string returnType;

        public List<Variable> parameters;

        public Function()
        {
            parameters = new List<Variable>();
        }

        public static Function Parse(string decl)
        {
            decl = decl.Substring("Function ".Length);
            Console.WriteLine(decl);

            Function func = new Function();

            int paramOpenIndex = decl.IndexOf('(');
            int paramCloseIndex = decl.IndexOf(')');

            string nameAndType = decl.Substring(0,paramOpenIndex).Trim();
            string type = "";
            string name = "";
            if (nameAndType.EndsWith('%'))
            {
                type = "int";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.EndsWith('#'))
            {
                type = "float";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.EndsWith('$'))
            {
                type = "String";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.IndexOf('.') > 0)
            {
                string[] splitOnType = nameAndType.Split('.');
                type = splitOnType[1];
                name = splitOnType[0];
            }
            else
            {
                type = "void";
                name = nameAndType;
            }

            string[] args = decl.Substring(paramOpenIndex+1,paramCloseIndex-paramOpenIndex-1).Trim().Split(',',StringSplitOptions.RemoveEmptyEntries);

            foreach (string arg in args)
            {
                Console.WriteLine(arg);
                func.parameters.Add(Variable.Parse(arg.Trim()));
            }

            return func;
        }

        private string GetSignature()
        {
            string retVal = returnType + " " + name + "(";
            List<string> paramsArr = new List<string>();

            foreach (Variable var in this.parameters)
            {
                paramsArr.Add(var.Parse2CPP());
            }

            retVal += string.Join(", ", paramsArr) + ")";
            return retVal;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
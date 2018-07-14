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
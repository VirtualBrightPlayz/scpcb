using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class Function : ScopeStatement
    {
        private string name;

        private string returnType;

        private List<Variable> parameters;

        public Function()
        {
            parameters = new List<Variable>();
        }

        private string GetSignature()
        {
            string retVal = returnType + " " + name + "(";
            // TODO: wtf
            List<string> params = new List<string>();

            foreach (Variable var in parameters)
            {
            }

            retVal += ")";
            return retVal;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
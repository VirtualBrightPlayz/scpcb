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

        public string Parse2CPP()
        {

            throw new NotImplementedException();
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
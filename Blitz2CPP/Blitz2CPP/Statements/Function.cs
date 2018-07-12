using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class Function : Statement
    {
        private string name;

        private string returnType;

        private List<Statement> statements;

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
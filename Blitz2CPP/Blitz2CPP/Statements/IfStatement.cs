using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class IfStatement : Statement
    {
        private List<Statement> scopeStatements;

        private List<IfStatement> elseIfStatements;

        private List<Statement> elseStatements;

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
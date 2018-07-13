using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class IfStatement : ScopeStatement
    {
        private Statement condition;

        private Dictionary<Statement, List<Statement>> elseIfStatements;

        private List<Statement> elseStatements;

        public IfStatement()
        {
            elseIfStatements = new Dictionary<Statement, List<Statement>>();
            elseStatements =  new List<Statement>();
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
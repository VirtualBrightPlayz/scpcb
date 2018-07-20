using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    /// <summary>
    /// Root class for statements which create a scope of other statements.
    /// </summary>
    public abstract class ScopeStatement : Statement
    {
        protected List<Statement> scopeStatements;

        public ScopeStatement()
        {
            scopeStatements = new List<Statement>();
        }

        public virtual void AddToScope(Statement stat)
        {
            scopeStatements.Add(stat);
        }

        public abstract override string Parse2CPP();
    }
}
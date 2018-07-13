using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    /// <summary>
    /// Root class for statements which create a scope of other statements.
    /// </summary>
    public abstract class ScopeStatement : Statement
    {
        private List<Statement> scopeStatements;
        public abstract override string Parse2CPP();
    }
}
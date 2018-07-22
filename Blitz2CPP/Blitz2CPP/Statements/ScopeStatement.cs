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

        public virtual void AddToScope(Statement stat) => scopeStatements.Add(stat);

        public override string Parse2CPP(string indents)
        {
            string retVal = "{\n";
            foreach (Statement stat in scopeStatements)
            {
                retVal += stat.Parse2CPP(indents + Constants.INDENTS) + "\n";
            }
            retVal += indents + "}";
            return retVal;
        }
    }
}
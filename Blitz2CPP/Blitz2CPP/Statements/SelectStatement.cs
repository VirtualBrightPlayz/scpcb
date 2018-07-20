using System;
using System.Collections.Generic;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class SelectStatement : ScopeStatement
    {
        public Dictionary<string, List<Statement>> cases;

        private List<Statement> defaultCase;

        public SelectStatement()
        {
            cases = new Dictionary<string, List<Statement>>();
        }

        public override void AddToScope(Statement stat)
        {
            if (defaultCase != null)
            {
                defaultCase.Add(stat);
            }
            else
            {
                cases.Last().Value.Add(stat);
            }
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
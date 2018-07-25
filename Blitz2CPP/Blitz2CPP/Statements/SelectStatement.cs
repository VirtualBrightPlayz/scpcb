using System;
using System.Collections.Generic;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class SelectStatement : ScopeStatement
    {
        private Statement controlVar;

        public Dictionary<string, List<Statement>> Cases;

        public List<Statement> DefaultCase;

        public SelectStatement()
        {
            Cases = new Dictionary<string, List<Statement>>();
        }

        public static SelectStatement Parse(string decl)
        {
            SelectStatement sStat = new SelectStatement();
            sStat.controlVar = ParseArithmetic(decl.JavaSubstring("Select ".Length, decl.LastIndexOf(")")));
            return sStat;
        }

        public override void AddToScope(Statement stat)
        {
            if (DefaultCase != null)
            {
                DefaultCase.Add(stat);
            }
            else
            {
                Cases.Last().Value.Add(stat);
            }
        }

        public override string Parse2CPP(string indents)
        {
            throw new NotImplementedException();
        }
    }
}
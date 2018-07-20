using System;
using System.Collections.Generic;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class IfStatement : ScopeStatement
    {
        private Statement condition;

        public Dictionary<Statement, List<Statement>> elseIfStatements;

        public List<Statement> elseStatements;

        public IfStatement()
        {
            elseIfStatements = new Dictionary<Statement, List<Statement>>();
        }

        public override void AddToScope(Statement stat)
        {
            if (elseStatements != null)
            {
                elseStatements.Add(stat);
            }
            else if (elseIfStatements.Any())
            {
                elseIfStatements.Last().Value.Add(stat);
            }
            else
            {
                scopeStatements.Add(stat);
            }
        }

        public static IfStatement Parse(string decl)
        {
            IfStatement iStat = new IfStatement();
            iStat.condition = Statement.ParseArithmetic(decl.JavaSubString("If (".Length, decl.IndexOf(") Then")));
            return iStat;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
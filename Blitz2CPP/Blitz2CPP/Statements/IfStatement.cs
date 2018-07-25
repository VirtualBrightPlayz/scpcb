using System;
using System.Collections.Generic;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class IfStatement : ScopeStatement
    {
        private Statement condition;

        public Dictionary<Statement, List<Statement>> ElseIfStatements;

        public List<Statement> ElseStatements;

        public IfStatement()
        {
            ElseIfStatements = new Dictionary<Statement, List<Statement>>();
        }

        public override void AddToScope(Statement stat)
        {
            if (ElseStatements != null)
            {
                ElseStatements.Add(stat);
            }
            else if (ElseIfStatements.Any())
            {
                ElseIfStatements.Last().Value.Add(stat);
            }
            else
            {
                scopeStatements.Add(stat);
            }
        }

        public static IfStatement Parse(string decl)
        {
            IfStatement iStat = new IfStatement();
            iStat.condition = ParseArithmetic(decl.JavaSubstring("If (".Length, decl.IndexOf(") Then")));
            iStat.condition.Semicolon = false;
            return iStat;
        }

        public override string Parse2CPP(string indents)
        {
            string retVal = indents + "if (" + condition.Parse2CPP() + ") " + base.Parse2CPP(indents);
            // else if statements.
            foreach (KeyValuePair<Statement, List<Statement>> elseIf in ElseIfStatements)
            {
                retVal += " else if (" + elseIf.Key.Parse2CPP() + ") {";
                foreach (Statement stat in elseIf.Value)
                {
                    retVal += "\n" + stat.Parse2CPP(indents + Constants.INDENTS);
                }
                retVal += "\n" + indents + "}";
            }
            // else statement.
            if (ElseStatements != null)
            {
                retVal += " else {";
                foreach (Statement stat in ElseStatements)
                {
                    retVal += "\n" + stat.Parse2CPP(indents + Constants.INDENTS);
                }
                retVal += "\n" + indents + "}";
            }

            return retVal;
        }
    }
}
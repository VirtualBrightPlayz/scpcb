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
            sStat.controlVar = ParseArithmetic(decl.JavaSubstring("Select (".Length, decl.LastIndexOf(")")));
            sStat.controlVar.Semicolon = false;
            return sStat;
        }

        public override void AddToScope(Statement stat)
        {
            if (DefaultCase != null)
            {
                DefaultCase.Add(stat);
            }
            else if (Cases.Any())
            {
                Cases.Last().Value.Add(stat);
            }
            else
            {
                Console.WriteLine("Statement in case dropped: " + stat.ToString());
            }
        }

        public override string Parse2CPP(string indents)
        {
            string retVal = indents + "switch (" + controlVar.Parse2CPP() + ") {";
            foreach (KeyValuePair<string, List<Statement>> selectCase in Cases)
            {
                retVal += "\n" + indents + Constants.INDENTS + "case " + selectCase.Key + ": {";
                foreach (Statement stat in selectCase.Value)
                {
                    retVal += "\n" + stat.Parse2CPP(indents + Constants.INDENTS + Constants.INDENTS);
                }
                retVal += "\n" + indents + Constants.INDENTS + "}";
            }

            // Default.
            if (DefaultCase != null)
            {
                retVal += "\n" + indents + Constants.INDENTS + "default: {";
                foreach (Statement stat in DefaultCase)
                {
                    retVal += "\n" + stat.Parse2CPP(indents + Constants.INDENTS + Constants.INDENTS);
                }
                retVal += "\n" + indents + Constants.INDENTS + "}";
            }
            retVal += "\n" + indents + "}";
            return retVal;
        }
    }
}
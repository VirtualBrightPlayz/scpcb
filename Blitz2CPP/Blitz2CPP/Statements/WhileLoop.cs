using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class WhileLoop : ScopeStatement
    {
        private Statement condition;

        public static WhileLoop Parse(string decl)
        {
            WhileLoop wl = new WhileLoop();
            wl.condition = ParseArithmetic(decl.JavaSubstring("While (".Length, decl.LastIndexOf(')')));
            wl.condition.semicolon = false;
            return wl;
        }

        public override string Parse2CPP(string indents)
        {
            string retVal = indents + "while (" + condition.Parse2CPP() + ") ";
            retVal += base.Parse2CPP(indents);
            return retVal;
        }
    }
}
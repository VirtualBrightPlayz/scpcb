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
            wl.condition = Statement.ParseArithmetic(decl.JavaSubstring(decl.IndexOf("While ("), decl.IndexOf(')')));
            return wl;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
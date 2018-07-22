using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class ForEachLoop : ScopeStatement
    {
        private const string iterName = "iteration";
        private static int iteration = 0;
        private static int Iteration { get => iteration++; }

        private string controlVarName;
        private string type;

        public override string Parse2CPP(string indents)
        {
            throw new NotImplementedException();
        }
    }
}
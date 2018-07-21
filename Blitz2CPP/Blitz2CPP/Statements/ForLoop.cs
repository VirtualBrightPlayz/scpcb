using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class ForLoop : ScopeStatement
    {
        private Variable controlVar;
        private string destValue;
        private string stepValue;

        public ForLoop()
        {
            stepValue = "1";
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
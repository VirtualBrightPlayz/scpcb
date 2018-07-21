using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class ForLoop : ScopeStatement
    {
        private Statement controlVar;
        private Statement destValue;
        private Statement stepValue;

        public ForLoop()
        {
            stepValue = new Statement("1");
        }

        public static ForLoop Parse(string decl)
        {
            ForLoop fl = new ForLoop();
            fl.controlVar = Statement.ParseArithmetic(decl.JavaSubstring("For ".Length, decl.IndexOf("To")).Trim());
            fl.destValue = Statement.ParseArithmetic(decl.Substring(decl.IndexOf("To") + "To".Length).Trim());

            int step = decl.IndexOf("Step");
            if (step > 0)
            {
                fl.stepValue = Statement.ParseArithmetic(decl.Substring(step + "Step ".Length).Trim());
            }

            return fl;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
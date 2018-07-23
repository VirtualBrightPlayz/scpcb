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
            fl.controlVar = Statement.ParseArithmetic(decl.JavaSubstring("For ".Length, decl.IndexOf("To ")).Trim());

            int step = decl.IndexOf("Step ");
            if (step > 0)
            {
                fl.destValue = Statement.ParseArithmetic(decl.JavaSubstring(decl.IndexOf("To") + "To".Length, step).Trim());
                fl.stepValue = Statement.ParseArithmetic(decl.Substring(step + "Step ".Length).Trim());
            }
            else
            {
                fl.destValue = Statement.ParseArithmetic(decl.Substring(decl.IndexOf("To ") + "To ".Length).Trim());
            }

            return fl;
        }

        public override string Parse2CPP(string indents)
        {
            // Get the control variable's name.
            string varName = controlVar.Parse2CPP();
            varName = varName.JavaSubstring(0, varName.IndexOf('=')).Trim();

            string retVal = indents + "for (" + controlVar.Parse2CPP(false) + "; ";
            retVal += varName + " <= " + destValue.Parse2CPP(false) + "; ";
            retVal += (stepValue.Parse2CPP(false) == "1" ? varName + "++" : varName + " += " + stepValue.Parse2CPP(false));
            retVal += ") ";
            retVal += base.Parse2CPP(indents);
            return retVal;
        }
    }
}
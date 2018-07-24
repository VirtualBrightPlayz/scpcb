using System;
using System.Collections.Generic;

namespace Blitz2CPP.Statements
{
    public class ForEachLoop : ScopeStatement
    {
        private const string iterName = "iterator";
        private static int iteration = 0;
        private static int Iteration { get => iteration++; }

        /// <summary>
        /// The variable that will be used for accessing the list's elements.
        /// </summary>
        private Variable accessVar;

        public static ForEachLoop Parse(string decl)
        {
            decl = decl.Substring("For ".Length);
            ForEachLoop fel = new ForEachLoop();

            string[] arr = decl.Split("= Each ");
            string varAndType = string.Join('.', arr);
            fel.accessVar = Variable.Parse(varAndType);

            return fel;
        }

        public override string Parse2CPP(string indents)
        {
            Console.WriteLine(accessVar.Name);
            string iterVar = iterName + Iteration;
            string iterType = accessVar.Type.Substring(0, accessVar.Type.Length-1); // Remove pointer.

            string retVal = indents + $"for (int {iterVar} = 0; {iterVar} < {iterType}::list.size(); {iterVar}++) ";
            retVal += base.Parse2CPP(indents);

            // Since this was a For Each loop, insert the original variable of whichever type this loop is iterating through.
            string decl = $"\n{indents}{Constants.INDENTS}{accessVar.Name} = {iterType}::list[{iterVar}];\n";
            retVal = retVal.Insert(retVal.IndexOf('{')+1, decl);
            return retVal;
        }
    }
}
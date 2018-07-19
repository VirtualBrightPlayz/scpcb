using System;
using System.Text.RegularExpressions;

namespace Blitz2CPP.Statements
{
    /// <summary>
    /// Root class for statements.
    /// </summary>
    public class Statement
    {
        /// <summary>
        /// The raw statement.
        /// </summary>
        private string raw;

        /// <summary>
        /// At which scope level is this statement.
        /// </summary>
        public int scope;

        public Statement(string raw) => this.raw = raw;

        protected Statement() { }

        public static Statement ParseArithmetic(string info)
        {
            // Parse functions.
            foreach (string funcName in Constants.BB_FUNCS)
            {
                string pattern = @"\b(" + funcName + @")\b";
                string replacement = @"bb$1";
                Regex.Replace(info, pattern, replacement);
            }

            // Operators.
            info = Toolbox.ReplaceNotInStr(info, "Not", "!");
            info = Toolbox.ReplaceNotInStr(info, "And", "&");
            info = Toolbox.ReplaceNotInStr(info, "Or", "|");
            info = Toolbox.ReplaceNotInStr(info, "Xor", "^");

            return new Statement(info);
        }

        public virtual string Parse2CPP()
        {
            return raw;
        }

        public override string ToString() => raw;
    }
}
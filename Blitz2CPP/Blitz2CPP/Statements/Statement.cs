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
        protected string raw;

        /// <summary>
        /// Determines whether or not the parsing of this statement should end with a semicolon.
        /// </summary>
        public bool semicolon;

        public Statement(string raw) : this() => this.raw = raw;

        protected Statement() => semicolon = true;

        /// <summary>
        /// Parses an arithmetic-based statement.
        /// </summary>
        public static Statement ParseArithmetic(string info)
        {
            // Parse functions.
            foreach (string funcName in Constants.BB_FUNCS)
            {
                string pattern = @"\b(" + funcName + @")\b";
                string replacement = @"bb$1";
                info = Regex.Replace(info, pattern, replacement);
            }

            // Operators.
            info = Toolbox.ReplaceNotInStr(info, "Not ", "!");
            info = Toolbox.ReplaceNotInStr(info, "And", "&");
            info = Toolbox.ReplaceNotInStr(info, "Or", "|");
            info = Toolbox.ReplaceNotInStr(info, "<>", "!=");
            info = Toolbox.ReplaceNotInStr(info, "Xor", "^");

            return new Statement(info);
        }

        public string Parse2CPP() => Parse2CPP(string.Empty);
        public virtual string Parse2CPP(string indents) => indents + raw + (semicolon ? ";" : "");
    }
}
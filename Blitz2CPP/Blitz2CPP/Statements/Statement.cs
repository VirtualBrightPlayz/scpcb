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

        public Statement(string raw) => this.raw = raw;

        protected Statement() { }

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
            info = Toolbox.ReplaceNotInStr(info, "Not", "!");
            info = Toolbox.ReplaceNotInStr(info, "And", "&");
            info = Toolbox.ReplaceNotInStr(info, "Or", "|");
            info = Toolbox.ReplaceNotInStr(info, "Xor", "^");

            return new Statement(info);
        }


        public string Parse2CPP() => Parse2CPP("");
        public string Parse2CPP(bool semicolon) => Parse2CPP("", semicolon);
        public virtual string Parse2CPP(string indents) => Parse2CPP(indents, true);
        public virtual string Parse2CPP(string indents, bool semicolon) => indents + raw + (semicolon ? ";" : "");
        public override string ToString() => raw;
    }
}
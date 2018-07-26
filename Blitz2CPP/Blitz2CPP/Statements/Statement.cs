using System;
using System.Collections.Generic;
using System.Linq;
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
        /// Determines whether or not the parsing of this statement should end with a Semicolon.
        /// </summary>
        public bool Semicolon;

        public Statement(string raw) : this() => this.raw = raw;

        protected Statement() => Semicolon = true;

        /// <summary>
        /// Parses an arithmetic-based statement.
        /// </summary>
        /// <param name="assignment">Whether the statement is an assignment or not.</param>
        /// <returns></returns>
        public static Statement ParseArithmetic(string info, bool assignment = false)
        {
            if (assignment && info.Contains("="))
            {
                string name = info.JavaSubstring(0, info.IndexOf('='));
                name = Toolbox.ReplaceNotInStr(name, @"\\", "->");
                string value = info.JavaSubstring(info.IndexOf('=')+1, info.Length);

                Statement stat = ParseArithmetic(value);
                stat.raw = name.Trim() + " = " + stat.raw.Trim();
                return stat;
            }

            return ParseArithmetic(info);
        }

        private static Statement ParseArithmetic(string info)
        {
            // Parse functions.
            foreach (string funcName in Constants.BB_FUNCS)
            {
                string pattern = @"\b(" + funcName + @")\b";
                string replacement = @"bb$1";
                info = Regex.Replace(info, pattern, replacement);
            }

            // Keywords.
            info = Toolbox.ReplaceNotInStr(info, "Return", "return", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "Delete", "delete", @"\b$1\b");

            // New Type keyword.
            if (Regex.IsMatch(info, @"\bNew \w+$"))
            {
                info = Toolbox.ReplaceNotInStr(info, "New", "new", @"\b$1\b");
                info += "()";
            }

            // Boolean constants.
            info = Toolbox.ReplaceNotInStr(info, "True", "true", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "False", "false", @"\b$1\b");

            // Null constant.
            info = Toolbox.ReplaceNotInStr(info, "Null", "nullptr", @"\b$1\b");

            // Operators.
            info = Toolbox.ReplaceNotInStr(info, "Not ", "!", @"\b$1");
            info = Toolbox.ReplaceNotInStr(info, "And", "&", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "Or", "|", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "Xor", "^", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "Mod", "%", @"\b$1\b");
            info = Toolbox.ReplaceNotInStr(info, "=>", ">=");
            info = Toolbox.ReplaceNotInStr(info, "=<", "<=");
            info = Toolbox.ReplaceNotInStr(info, "=", "==", "(?<=[^<>])$1");
            info = Toolbox.ReplaceNotInStr(info, "<>", "!=");

            // Struct members.
            info = Toolbox.ReplaceNotInStr(info, @"\\", "->");

            return new Statement(info);
        }

        public static string[] SplitMultiDecl(string info)
        {
            List<int> splitIndexes = new List<int>();
            int scopes = 0;
            for (int i = 0; i < info.Length; i++)
            {
                switch (info[i])
                {
                    case '(':
                    case '[':
                        scopes++;
                        break;
                    case ')':
                    case ']':
                        scopes--;
                        break;
                    case ',':
                        if (scopes == 0)
                            splitIndexes.Add(i);
                        break;
                }
            }

            return info.SplitAt(splitIndexes.ToArray());
        }

        public string Parse2CPP() => Parse2CPP(string.Empty);
        public virtual string Parse2CPP(string indents) => indents + raw + (Semicolon ? ";" : "");
    }
}
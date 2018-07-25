using System;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class Variable : Statement
    {
        public string Type;

        public string Name;

        private Statement assignment;

        public Variable((string type, string name) tuple)
        {
            this.Type = tuple.type;
            this.Name = tuple.name;
        }

        /// <summary>
        /// Figures out the type of the declared BB var.
        /// '%' -> int
        /// '#' -> float
        /// '$' -> String
        /// '.Type' -> Type
        /// </summary>
        private static (string type, string name) ParseDecl(string info)
        {
            string[] split = info.Split('[', StringSplitOptions.RemoveEmptyEntries).Select(s => s.Trim()).ToArray();
            string type = "";
            string varName = "";
            if (split[0].EndsWith('%'))
            {
                type = "int";
                varName = split[0].Substring(0, split[0].Length - 1);
            }
            else if (split[0].EndsWith('#'))
            {
                type = "float";
                varName = split[0].Substring(0, split[0].Length - 1);
            }
            else if (split[0].EndsWith('$'))
            {
                type = Constants.CPP_STR;
                varName = split[0].Substring(0, split[0].Length - 1);
            }

            else if (split[0].IndexOf('.') > 0)
            {
                string[] splitOnType = split[0].Split('.');
                type = splitOnType[1].Trim() + "*";
                varName = splitOnType[0].Trim();
            }

            if (string.IsNullOrWhiteSpace(type) || string.IsNullOrWhiteSpace(varName))
            {
                throw new Exception("Unable to parse variable type. Declaration: \"" + info + "\"");
            }

            if (split.Length > 1)
            {
                varName += "[" + split[1];
            }

            return (type, varName);
        }

        public static Variable Parse(string decl)
        {
            int split = decl.IndexOf('=');
            (string decl, string assignment) tup;

            if (split > 0)
            {
                tup = (decl.Substring(0, split), decl.Substring(split+1, decl.Length - 1 - split));
            }
            else
            {
                tup = (decl, null);
            }

            Variable var = new Variable(ParseDecl(tup.decl.Trim()));
            if (!string.IsNullOrEmpty(tup.assignment))
            {
                var.assignment = ParseArithmetic(tup.assignment.Trim());
                var.assignment.Semicolon = false;
            }

            return var;
        }

        public string GetDefinition() => Type + " " + Name + ";";

        public override string Parse2CPP(string indents)
        {
            string retVal = indents + Type + " " + Name;
            if (assignment != null)
            {
                retVal += " = " + assignment.Parse2CPP();
            }
            if (Semicolon)
            {
                retVal += ";";
            }
            return retVal;
        }

        public override string ToString() => Name + " " + Name + (assignment != null ? " = " + assignment.ToString() : "");
    }
}
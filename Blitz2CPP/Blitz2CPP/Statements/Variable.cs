using System;

namespace Blitz2CPP.Statements
{
    public class Variable : Statement
    {
        public string type;

        public string name;

        public Statement assignment;

        public Variable((string type, string name) tuple)
        {
            this.type = tuple.type;
            this.name = tuple.name;
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
            string[] split = info.Split('[', StringSplitOptions.RemoveEmptyEntries);
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
                type = "String";
                varName = split[0].Substring(0, split[0].Length - 1);
            }

            else if (split[0].IndexOf('.') > 0)
            {
                string[] splitOnType = split[0].Split('.');
                type = splitOnType[1];
                varName = splitOnType[0];
            }

            if (string.IsNullOrWhiteSpace(type) || string.IsNullOrWhiteSpace(varName))
            {
                throw new Exception("Unable to parse variable type. Declaration: " + info);
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
                tup = (decl.Substring(0, split), decl.Substring(split, decl.Length - 1 - split));
            }
            else
            {
                tup = (decl, null);
            }

            Variable var = new Variable(ParseDecl(tup.decl));
            if (!string.IsNullOrEmpty(tup.assignment))
            {
                var.assignment = Statement.ParseArithmetic(tup.assignment);
            }

            return var;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }

        public override string ToString() => type + " " + name + (assignment != null ? " = " + assignment.ToString() : "");
    }
}
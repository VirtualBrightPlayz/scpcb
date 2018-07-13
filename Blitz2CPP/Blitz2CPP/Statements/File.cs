using System;
using System.Collections.Generic;
using System.IO;

namespace Blitz2CPP.Statements
{
    public class File : IDisposable
    {
        private string filePath;

        /// <summary>
        /// The .bb file being parsed.
        /// </summary>
        private StreamReader bbFile;

        /// <summary>
        /// The resulting .cpp file.
        /// </summary>
        private StreamWriter srcFile;

        /// <summary>
        /// The resulting .h file.
        /// </summary>
        private StreamWriter headerFile;

        private Stack<ScopeStatement> scopes;

        private int currScope;

        private List<TypeDecl> typeDecls;

        private List<Variable> globals;

        private List<Function> functions;

        public File(string path)
        {
            filePath = path;
            bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            string dest = Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
            srcFile = new StreamWriter(new FileStream(dest + ".cpp", FileMode.Create));
            headerFile = new StreamWriter(new FileStream(dest + ".h", FileMode.Create));

            scopes = new Stack<ScopeStatement>();
            typeDecls = new List<TypeDecl>();
            globals = new List<Variable>();
            functions = new List<Function>();
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }

        private void ParseLine(string info)
        {
            // Global scope stuff.
            if (currScope <= 0)
            {
                if (info.StartsWith("Global "))
                {
                    string[] split = info.Substring(7).Split('=');

                    Variable var = new Variable();
                    (string type, string name) tup = ParseVar(split[0]);
                    var.type = tup.type;
                    var.name = tup.name;

                    if (split.Length > 1)
                    {
                        var.assignment = new Statement(ParseArithmetic(split[1]));
                    }
                }

                else if (info.StartsWith("Const "))
                {
                    string[] split = info.Substring(6).Split('=');

                    Variable var = new Variable();
                    (string type, string name) tup = ParseVar(split[0]);
                    var.type = tup.type;
                    var.name = tup.name;

                    if (split.Length > 1)
                    {
                        var.assignment = new Statement(ParseArithmetic(split[1]));
                    }
                }

                // FIXME
                else if (info.StartsWith("Function "))
                {
                    ParseFunctionDef(info);
                }
            }

            // If not anything above then it's probably arithmetic.
            srcFile.WriteLine(GetIndents() + ParseArithmetic(info) + ";");
        }

        private string ParseArithmetic(string info)
        {
            // TODO: Get this working.
            // Parse functions.
            // string pattern = @"[\W](\w+)";
            // MatchCollection matches = Regex.Matches(info, pattern);
            // foreach (Group big in matches)
            // {
            //     if (Constants.BB_FUNC.Contains(big[0]))
            //     {

            //     }
            // }

            // This probably breaks boolean logic.
            info = Toolbox.ReplaceNotInStr(info, "Not", "!");
            info = Toolbox.ReplaceNotInStr(info, "And", "&");
            info = Toolbox.ReplaceNotInStr(info, "Or", "|");
            info = Toolbox.ReplaceNotInStr(info, "Xor", "^");

            return info;
        }

        /// <summary>
        /// Figures out the type of the declared BB var.
        /// '%' -> int
        /// '#' -> float
        /// '$' -> String
        /// '.Type' -> Type
        /// </summary>
        public (string type, string name) ParseVar(string info)
        {
            if (info.EndsWith('%'))
            {
                return ("int", info.Substring(0, info.Length - 1));
            }

            if (info.EndsWith('#'))
            {
                return ("float", info.Substring(0, info.Length - 1));
            }

            if (info.EndsWith('$'))
            {
                return ("String ", info.Substring(0, info.Length - 1));
            }

            int index = info.IndexOf('.');
            if (index > 0)
            {
                string type = info.Substring(index+1, info.Length - index - 1);
                string varName = info.Substring(0, info.Length - index - 1);
                return (type, varName);
            }

            throw new Exception("Unable to parse variable type. File: " + filePath + " Line: " + bbFile);
        }
    }
}
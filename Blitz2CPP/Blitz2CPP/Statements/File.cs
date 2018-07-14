using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;

namespace Blitz2CPP.Statements
{
    public class File : IDisposable
    {
        private string filePath;
        private int currLine;

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

        private int GetScopeSize()
        {
            return scopes.Count;
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }

        public void ParseFile()
        {
            while (!bbFile.EndOfStream)
            {
                string line = bbFile.ReadLine();

                if (string.IsNullOrWhiteSpace(line))
                {
                    continue;
                }
                // Type declaration?
                else if (line.Trim().StartsWith("Type "))
                {

                }
                else
                {
                    // Multi-line?
                    string[] multi = line.Split(" : ", StringSplitOptions.RemoveEmptyEntries);
                    foreach (string statement in multi)
                    {
                        ParseLine(statement.Trim());
                    }
                }

                currLine++;
            }
        }

        private void ParseLine(string info)
        {
            // Global scope stuff.
            if (GetScopeSize() <= 0)
            {
                if (info.StartsWith("Global "))
                {
                    AddGlobal(info, "Global ".Length);
                }

                else if (info.StartsWith("Const "))
                {
                    AddGlobal(info, "Const ".Length, "const ");
                }

                // FIXME
                else if (info.StartsWith("Function "))
                {
                    // ParseFunctionDef(info);
                }
            }
        }

        private string ParseArithmetic(string info)
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

            return info;
        }

        /// <summary>
        /// Figures out the type of the declared BB var.
        /// '%' -> int
        /// '#' -> float
        /// '$' -> String
        /// '.Type' -> Type
        /// </summary>
        private (string type, string name) ParseVar(string info)
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

        private void AddGlobal(string decl, int startIndex, string prefix = "")
        {
            string[] split = decl.Substring(prefix.Length).Split('=');

            Variable var = new Variable();
            (string type, string name) tup = ParseVar(split[0]);
            var.type = tup.type;
            var.name = tup.name;

            if (split.Length > 1)
            {
                var.assignment = new Statement(ParseArithmetic(split[1]));
            }
        }
    }
}
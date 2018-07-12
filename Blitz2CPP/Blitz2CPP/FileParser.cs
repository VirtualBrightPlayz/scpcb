using System;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;

namespace Blitz2CPP
{
    public class FileParser : IDisposable
    {
        private string filePath;
        private int currLine;
        private int currScope;

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

        public FileParser(string path)
        {
            filePath = path;
            currLine = 0;
            currScope = 0;
            //bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            // string dest = Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
            // srcFile = new StreamWriter(new FileStream(dest + ".cpp", FileMode.Create));
            // headerFile = new StreamWriter(new FileStream(dest + ".h", FileMode.Create));
        }

        public void ParseFile()
        {
            while (!bbFile.EndOfStream)
            {
                string line = bbFile.ReadLine();

                if (string.IsNullOrWhiteSpace(line))
                {
                    srcFile.WriteLine();
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
            if (info.StartsWith("Global "))
            {
                string[] split = info.Substring(7).Split('=');
                headerFile.Write("extern " + ParseGlobal(split[0]));
                srcFile.Write(ParseGlobal(split[0]));
                if (split.Length > 1) { srcFile.Write(" = " + ParseArithmetic(split[1])); }

                srcFile.WriteLine();
                return;
            }

            if (info.StartsWith("Const "))
            {
                string[] split = info.Substring(6).Split('=');
                srcFile.Write(ParseConst(split[0]));
                if (split.Length > 1) { srcFile.Write(" = " + ParseArithmetic(split[1])); }

                srcFile.WriteLine();
                return;
            }

            if (info.StartsWith("Function "))
            {
                ParseFunctionDef(info);
            }

            // If not anything above then it's probably arithmetic.
            srcFile.WriteLine(GetIndents() + ParseArithmetic(info) + ";");
        }

        /// <summary>
        /// Figures out the type of the declared BB var.
        /// '%' -> int
        /// '#' -> float
        /// '$' -> String
        /// '.Type' -> Type
        /// </summary>
        public string ParseVar(string info)
        {
            if (info.EndsWith('%'))
            {
                return "int " + info.Substring(0, info.Length - 1);
            }

            if (info.EndsWith('#'))
            {
                return "float " + info.Substring(0, info.Length - 1);
            }

            if (info.EndsWith('$'))
            {
                return "String " + info.Substring(0, info.Length - 1);
            }

            int index = info.IndexOf('.');
            if (index > 0)
            {
                string type = info.Substring(index+1, info.Length - index - 1);
                string varName = info.Substring(0, info.Length - index - 1);
                return type + " " + varName;
            }

            throw new Exception("Unable to parse variable type. File: " + filePath + " Line: " + bbFile);
        }

        private string ParseGlobal(string info) => ParseVar(info);

        private string ParseConst(string info) => "const " + ParseVar(info);

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
            info = ReplaceNotInStr(info, "Not", "!");
            info = ReplaceNotInStr(info, "And", "&&");
            info = ReplaceNotInStr(info, "Or", "||");
            info = ReplaceNotInStr(info, "Xor", "^");

            return info;
        }

        /// <summary>
        /// Replaces all occurences of something so long as it is not in a string.
        /// </summary>
        private string ReplaceNotInStr(string str, string needle, string replacement)
        {
            string ret = "";
            string[] arr = str.Split(needle);
            foreach (string hay in arr)
            {
                // If there's an even number of quotation marks before it then it is not in a string.
                if (hay.Count(x => x == '"') % 2 == 0)
                {
                    ret = hay + replacement;
                }
                else
                {
                    ret = hay + needle;
                }
            }

            return ret;
        }

        private void ParseFunctionDef(string info)
        {
            string pattern = @"Function (\w+)([%#$]|\.\w+|)\((.)\)";
            MatchCollection matches = Regex.Matches(info, pattern);
            if (matches.Count > 0 && matches[0].Groups.Count > 1)
            {
                string funcName = matches[0].Groups[0].Value;
                string type = matches[0].Groups[1].Value;
                if (string.IsNullOrWhiteSpace(type))
                {
                    type = "void";
                }
                else
                {
                    type = ParseVar(type);
                }

                string args = matches[0].Groups[2].Value;
                string[] argsArr = args.Split(',');
                for (int i = 0; i < argsArr.Length; i++)
                {
                    argsArr[i] = ParseVar(argsArr[i].Trim());
                }
                args = string.Join(", ", argsArr);

                headerFile.WriteLine(type + funcName + "(" + args + ");");
                headerFile.WriteLine();
                srcFile.WriteLine(type + funcName + "(" + args + ") {");

                currScope++;
                return;
            }

            throw new Exception("Unable to parse function declaration. File: " + filePath + " Line: " + bbFile);
        }

        private string ParseIf(string info)
        {
            throw new NotImplementedException();
        }

        private string ParseFor(string info)
        {
            throw new NotImplementedException();
        }

        private string GetIndents()
        {
            string ret = "";
            for (int i = 0; i < currScope; i++)
            {
                ret += Constants.INDENTS;
            }

            return ret;
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }
    }
}
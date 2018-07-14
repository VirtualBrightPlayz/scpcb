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
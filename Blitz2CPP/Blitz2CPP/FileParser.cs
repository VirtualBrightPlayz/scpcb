using System;
using System.IO;

namespace Blitz2CPP
{
    public class FileParser : IDisposable
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

        public FileParser(string path)
        {
            filePath = path;
            currLine = 0;
            bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            string dest = Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
            srcFile = new StreamWriter(new FileStream(dest + ".cpp", FileMode.Create));
            headerFile = new StreamWriter(new FileStream(dest + ".h", FileMode.Create));
        }

        public void ParseFile()
        {

        }

        /// <summary>
        /// Figures out the type of the declared BB var.
        /// '%' -> int
        /// '#' -> float
        /// '$' -> String
        /// '.Type' -> Type
        /// </summary>
        private string ParseVar(string info)
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
                string type = info.Substring(index+1, info.Length - index+1);
                string varName = info.Substring(0, info.Length - index);
                return type + " " + varName;
            }

            throw new Exception("Unable to parse variable type. File: " + filePath + " Line: " + bbFile);
        }

        private string ParseGlobal(string info) => "public static " + ParseVar(info);

        private string ParseArithmetic(string info)
        {
            throw new NotImplementedException();
        }

        private string ParseFunctionDef(string info)
        {
            throw new NotImplementedException();
        }

        private string ParseIf(string info)
        {
            throw new NotImplementedException();
        }

        private string ParseFor(string info)
        {
            throw new NotImplementedException();
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }
    }
}
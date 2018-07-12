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

        private List<TypeDecl> typeDecls;

        private List<Variable> globals;

        public File(string path)
        {
            filePath = path;
            bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            string dest = Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
            srcFile = new StreamWriter(new FileStream(dest + ".cpp", FileMode.Create));
            headerFile = new StreamWriter(new FileStream(dest + ".h", FileMode.Create));

            typeDecls = new List<TypeDecl>();
            globals = new List<Variable>();
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }
    }
}
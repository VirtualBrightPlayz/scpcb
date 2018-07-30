using Blitz2CPP.Statements;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace Blitz2CPP
{
    class Program
    {
        private static readonly string FOLDER = Toolbox.GetProjectDirectory() + "../../SourceCode/";

        private static readonly StreamWriter includefile;

        private static void Main(string[] args)
        {
            // Create include.h.
            StreamWriter include = new StreamWriter(new FileStream(Constants.DIR_OUTPUT + Constants.CPP_INCLUDE_FILE, FileMode.Create));
            string includeGuard = Constants.CPP_INCLUDE_FILE.Replace('.', '_') + "_INCLUDED";
            include.WriteLine("#ifndef " + includeGuard);
            include.WriteLine("#define " + includeGuard);
            include.WriteLine();

            // Get .bb files.
            IEnumerable<string> files = Directory.GetFiles(FOLDER, "*.bb", SearchOption.AllDirectories);
            foreach (string fileStr in files)
            {
                // Get the folder structure inside of the SourceCode folder.
                string dir = fileStr.Substring(fileStr.IndexOf("SourceCode") + "SourceCode".Length + 1);
                include.WriteLine("#include \"" + Path.ChangeExtension(dir, "h") + "\"");
                dir = Path.GetDirectoryName(dir) + "/";
                dir = Path.Combine(Constants.DIR_OUTPUT + dir);
                // Re-create the directories.
                if (!Directory.Exists(dir)) { Directory.CreateDirectory(dir); }
                using (BlitzFile file = new BlitzFile(fileStr, dir))
                {
                    file.ParseFile();
                    file.WriteHeaderFile();
                    file.WriteCPPFile();
                }
            }

            include.WriteLine("#endif // " + includeGuard);
            include.Dispose();
        }

        private static bool Hack(string val)
        {
            Console.WriteLine(Path.GetFullPath(val));
            return true;
        }
    }
}
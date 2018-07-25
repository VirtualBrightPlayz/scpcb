using Blitz2CPP.Statements;
using System;
using System.IO;

namespace Blitz2CPP
{
    class Program
    {
        private static readonly string TEST_INPUT = Toolbox.GetProjectDirectory() + "tester.bb";

        private static readonly StreamWriter includefile;

        private static void Main(string[] args)
        {
            using (BlitzFile file = new BlitzFile(TEST_INPUT))
            {
                file.ParseFile();
                file.WriteHeaderFile();
                file.WriteCPPFile();
            }
            //Console.ReadLine();
        }
    }
}
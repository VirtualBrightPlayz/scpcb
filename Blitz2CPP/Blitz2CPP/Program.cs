using Blitz2CPP.Statements;
using System;

namespace Blitz2CPP
{
    class Program
    {
        private static readonly string TEST_INPUT = Toolbox.GetProjectDirectory() + "tester.bb";

        private static void Main(string[] args)
        {
            using (BlitzFile file = new BlitzFile(TEST_INPUT))
            {
                file.ParseFile();
                file.WriteCPPFile();
            }
            // Console.ReadLine();
        }
    }
}
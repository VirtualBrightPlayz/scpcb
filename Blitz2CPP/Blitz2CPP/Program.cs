using System;
using System.IO;
using Blitz2CPP.Statements;

namespace Blitz2CPP
{
    class Program
    {
        private const string TEST_INPUT = "tester.bb";

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
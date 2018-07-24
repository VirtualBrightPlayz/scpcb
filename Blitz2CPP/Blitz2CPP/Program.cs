using Blitz2CPP.Statements;
using System;
using System.Text.RegularExpressions;

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
                file.WriteHeaderFile();
                file.WriteCPPFile();
            }
            // Console.WriteLine(Regex.IsMatch("plyr\\loudness = 2.0", @"\\"));
            //Console.ReadLine();
        }
    }
}
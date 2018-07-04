using System;
using System.IO;

namespace Blitz2CPP
{
    class Program
    {
        private static void Main(string[] args)
        {
            Console.WriteLine(new FileParser("uh").ParseVar("jew.NPC"));
            Console.WriteLine(new FileParser("uh").ParseVar("jew%"));
            Console.WriteLine(new FileParser("uh").ParseVar("jew#"));
        }
    }
}
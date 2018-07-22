using System;
using System.Collections.Generic;
using System.Linq;

namespace Blitz2CPP.Statements
{
    public class Comment : Statement
    {
        private Comment(string comm) : base(comm) { }

        public static Comment Parse(string comm)
        {
            return new Comment(comm.Substring(comm.IndexOf(';')+1));
        }

        public override string Parse2CPP(string indents) => indents + "//" + raw;
    }
}
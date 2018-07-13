using System;

namespace Blitz2CPP.Statements
{
    public class Variable : Statement
    {
        public string type;

        public string name;

        public Statement assignment;

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
using System;

namespace Blitz2CPP.Statements
{
    public class Variable : Statement
    {
        public string type;

        public string name;

        public Statement assignment;

        public Variable((string type, string name) tuple)
        {
            this.type = tuple.type;
            this.name = tuple.name;
        }

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
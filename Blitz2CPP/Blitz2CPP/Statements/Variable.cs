using System;

namespace Blitz2CPP.Statements
{
    public class Variable : Statement
    {
        private string type;

        private string name;

        private Statement assignment;

        public override string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
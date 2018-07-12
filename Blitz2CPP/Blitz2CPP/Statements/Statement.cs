using System;

namespace Blitz2CPP.Statements
{
    /// <summary>
    /// Root class for statements.
    /// </summary>
    public class Statement
    {
        /// <summary>
        /// The raw statement.
        /// </summary>
        private string raw;

        /// <summary>
        /// At which scope level is this statement.
        /// </summary>
        private int scope;

        public virtual string Parse2CPP()
        {
            throw new NotImplementedException();
        }
    }
}
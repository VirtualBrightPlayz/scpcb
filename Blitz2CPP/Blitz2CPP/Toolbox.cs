using System.Linq;

namespace Blitz2CPP
{
    public static class Toolbox
    {
        public static string GetIndents(int scope)
        {
            string retVal = "";
            for (int i = 0; i < scope; i++)
            {
                retVal += Constants.INDENTS;
            }

            return retVal;
        }

        /// <summary>
        /// Replaces all occurences of something so long as it is not in a string.
        /// </summary>
        public static string ReplaceNotInStr(string str, string needle, string replacement)
        {
            string ret = "";
            string[] arr = str.Split(needle);
            foreach (string hay in arr)
            {
                // If there's an even number of quotation marks before it then it is not in a string.
                if (hay.Count(x => x == '"') % 2 == 0)
                {
                    ret = hay + replacement;
                }
                else
                {
                    ret = hay + needle;
                }
            }

            return ret;
        }
    }
}
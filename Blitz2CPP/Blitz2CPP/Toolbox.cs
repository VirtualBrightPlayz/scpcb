using System.Linq;

namespace Blitz2CPP
{
    public static class Toolbox
    {
        /// <summary>
        /// Replaces all occurences of something so long as it is not in a string.
        /// </summary>
        public static string ReplaceNotInStr(string str, string needle, string replacement)
        {
            if (!str.Contains(needle)) { return str; }

            string ret = "";
            string[] arr = str.Split(needle);
            for (int i = 0; i < arr.Length; i++)
            {
                if (i == arr.Length-1)
                {
                    ret += arr[i];
                    continue;
                }

                string hay = arr[i];
                int quoteCount = hay.Count(x => x == '"');

                // If there's an even number of quotation marks before it then it is not in a string.
                if (quoteCount % 2 == 0)
                {
                    ret += hay + replacement;
                }
                else
                {
                    ret += hay + needle;
                }
            }

            return ret;
        }
    }
}
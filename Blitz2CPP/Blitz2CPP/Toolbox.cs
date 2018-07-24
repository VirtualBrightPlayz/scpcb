using System.IO;
using System.Linq;
using System.Reflection;
using System.Text.RegularExpressions;

namespace Blitz2CPP
{
    public static class Toolbox
    {
        /// <summary>
        /// Replaces all occurences of something so long as it is not in a string.
        /// </summary>
        public static string ReplaceNotInStr(string str, string needle, string replacement)
        {
            if (!Regex.IsMatch(str, needle)) { return str; }

            string ret = "";
            string[] arr = Regex.Split(str, needle);
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

        /// <summary>
        /// Gets the directory of the current project.
        /// </summary>
        /// <returns></returns>
        public static string GetProjectDirectory()
        {
            string assemb = Assembly.GetAssembly(typeof(Toolbox)).GetName().Name;
            string dir = Directory.GetCurrentDirectory();
            return dir.JavaSubstring(0, dir.LastIndexOf(assemb) + assemb.Length) + "/";
        }
    }
}
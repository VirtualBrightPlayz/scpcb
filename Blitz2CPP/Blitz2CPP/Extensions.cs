using System;
using System.Collections.Generic;

namespace Blitz2CPP
{
    public static partial class Extensions
    {
        /// <summary>
        /// Returns a substring starting from startIndex (inclusive) to endIndex (exclusive).
        /// </summary>
        /// <param name="startIndex">Inclusive starting index.</param>
        /// <param name="endIndex">Exclusive ending index.</param>
        public static string JavaSubstring(this string str, int startIndex, int endIndex)
        {
            return str.Substring(startIndex, endIndex - startIndex);
        }

        public static List<int> AllIndexesOf(this string str, string value)
         {
            if (string.IsNullOrEmpty(value))
            {
                throw new ArgumentException("the string to find may not be empty", "value");
            }

            List<int> indexes = new List<int>();
            for (int index = 0;; index += value.Length)
            {
                index = str.IndexOf(value, index);
                if (index == -1) { return indexes; }
                indexes.Add(index);
            }
        }
    }
}
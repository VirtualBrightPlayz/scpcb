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
    }
}
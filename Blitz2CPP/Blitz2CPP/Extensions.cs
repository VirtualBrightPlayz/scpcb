namespace Blitz2CPP
{
    public static partial class Extensions
    {
        public static string JavaSubString(this string str, int startIndex, int endIndex)
        {
            return str.Substring(startIndex, endIndex - startIndex);
        }
    }
}
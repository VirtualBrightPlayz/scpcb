using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;

namespace Blitz2CPP.Statements
{
    public class BlitzFile : IDisposable
    {
        private string filePath;
        private int currLine;

        /// <summary>
        /// The .bb file being parsed.
        /// </summary>
        private StreamReader bbFile;

        /// <summary>
        /// The resulting .cpp file.
        /// </summary>
        private StreamWriter srcFile;

        /// <summary>
        /// The resulting .h file.
        /// </summary>
        private StreamWriter headerFile;

        private Stack<ScopeStatement> scopes;
        private int GetScopeSize => scopes.Count;
        private ScopeStatement GetCurrScope => scopes.Peek();

        public List<TypeDecl> typeDecls;

        private List<Variable> globals;
        private List<Variable> constants;

        private List<Function> functions;

        public BlitzFile(string path)
        {
            filePath = path;
            bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            string dest = Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
            srcFile = new StreamWriter(new FileStream(dest + ".cpp", FileMode.Create));
            headerFile = new StreamWriter(new FileStream(dest + ".h", FileMode.Create));

            scopes = new Stack<ScopeStatement>();
            typeDecls = new List<TypeDecl>();
            globals = new List<Variable>();
            constants = new List<Variable>();
            functions = new List<Function>();
        }

        public void Dispose()
        {
            bbFile?.Dispose();
            srcFile?.Dispose();
            headerFile?.Dispose();
        }

        public void ParseFile()
        {
            try
            {
                currLine = 1;
                for (string line = bbFile.ReadLine().Trim(); !bbFile.EndOfStream; line = bbFile.ReadLine()?.Trim(), currLine++)
                {
                    if (string.IsNullOrWhiteSpace(line))
                    {
                        continue;
                    }
                    // Type declaration?
                    else if (line.StartsWith("Type "))
                    {
                        TypeDecl typ = new TypeDecl();
                        typ.name = line.Substring("Type ".Length);
                        while (!line.StartsWith("End Type"))
                        {
                            if (bbFile.EndOfStream)
                            {
                                throw new EndOfStreamException("Type was declared without an End statement.");
                            }

                            line = bbFile.ReadLine().Trim();
                            currLine++;

                            if (string.IsNullOrWhiteSpace(line)) { continue; }
                            else if (line.StartsWith(';')) { continue; }
                            else if (line.StartsWith("Field "))
                            {
                                typ.fields.Add(Variable.Parse(line.Substring("Field ".Length)));
                            }
                        }

                        typeDecls.Add(typ);
                    }
                    else
                    {
                        // Multi-line?
                        string[] multi = line.Split(" : ", StringSplitOptions.RemoveEmptyEntries);
                        foreach (string statement in multi)
                        {
                            ParseLine(statement.Trim());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("CurrFile: " + filePath + ", CurrLine: " + currLine + "\n"
                    + e.ToString());
            }
        }

        private void ParseLine(string info)
        {
            // Global scope stuff.
            if (GetScopeSize <= 0)
            {
                if (info.StartsWith("Global "))
                {
                    AddGlobal(info);
                }

                else if (info.StartsWith("Const "))
                {
                    AddConstant(info);
                }
                
                else if (info.StartsWith("Function "))
                {
                    AddFunction(info);
                }
            }
            else
            {

            }
        }

        private void AddGlobal(string decl)
        {
            decl = decl.Substring("Global ".Length);
            Variable var = Variable.Parse(decl);

            globals.Add(var);
        }

        private void AddConstant(string decl)
        {
            decl = decl.Substring("Const ".Length);
            Variable var = Variable.Parse(decl);

            constants.Add(var);
        }

        private void AddFunction(string decl)
        {
            decl = decl.Substring("Function ".Length);
            Console.WriteLine(decl);

            Function func = new Function();

            int paramOpenIndex = decl.IndexOf('(');
            int paramCloseIndex = decl.IndexOf(')');

            string nameAndType = decl.Substring(0,paramOpenIndex).Trim();
            string type = "";
            string name = "";
            if (nameAndType.EndsWith('%'))
            {
                type = "int";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.EndsWith('#'))
            {
                type = "float";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.EndsWith('$'))
            {
                type = "String";
                name = nameAndType.Substring(0, nameAndType.Length - 1);
            }
            else if (nameAndType.IndexOf('.') > 0)
            {
                string[] splitOnType = nameAndType.Split('.');
                type = splitOnType[1];
                name = splitOnType[0];
            }
            else
            {
                type = "void";
                name = nameAndType;
            }
            
            string[] args = decl.Substring(paramOpenIndex+1,paramCloseIndex-paramOpenIndex-1).Trim().Split(',',StringSplitOptions.RemoveEmptyEntries);

            foreach (string arg in args)
            {
                Console.WriteLine(arg);
            }

            foreach (string arg in args)
            {
                func.parameters.Add(Variable.Parse(arg.Trim()));
            }

            functions.Add(func);
            scopes.Push(func);
        }
    }
}
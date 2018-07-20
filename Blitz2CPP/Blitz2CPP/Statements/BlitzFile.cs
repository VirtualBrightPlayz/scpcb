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

            // If statements.
            else if (info.StartsWith("If "))
            {
                AddIf(info);
            }

            else if (info.StartsWith("Else If "))
            {
                if (GetCurrScope is IfStatement iStat)
                {
                    Statement condition = Statement.ParseArithmetic(info.JavaSubString("Else If (".Length, info.IndexOf(") Then")));
                    iStat.elseIfStatements.Add(condition, new List<Statement>());
                }
                else
                {
                    throw new Exception("\"Else If\" without If statement.");
                }
            }

            else if (info.StartsWith("Else"))
            {
                if (GetCurrScope is IfStatement iStat)
                {
                    iStat.elseStatements = new List<Statement>();
                }
                else
                {
                    throw new Exception("\"Else\" without If statement.");
                }
            }

            // TODO:
            // Locals.
            else if (info.StartsWith("Local "))
            {

            }

            // Switches.
            else if (info.StartsWith("Select "))
            {

            }

            else if (info.StartsWith("Case "))
            {

            }

            else if (info.StartsWith("Default"))
            {

            }

            // Loops.
            else if (info.StartsWith("For "))
            {

            }

            else if (info.StartsWith("While "))
            {

            }

            // TODO: Missing some end statements.
            else if (info.StartsWith("EndIf") || info.StartsWith("End If") || info.StartsWith("End Select") || info.StartsWith("Wend") || info.StartsWith("Next"))
            {
                scopes.Pop();
            }

            // Normal statement.
            else
            {
                GetCurrScope.AddToScope(Statement.ParseArithmetic(info));
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
                func.parameters.Add(Variable.Parse(arg.Trim()));
            }

            functions.Add(func);
            scopes.Push(func);
        }

        private void AddIf(string tuch)
        {
            IfStatement iStat = IfStatement.Parse(tuch);
            GetCurrScope.AddToScope(iStat);

            // One line statement?
            if (tuch.EndsWith("Then"))
            {
                scopes.Push(iStat);
            }
            else
            {
                string statement = tuch.Substring(tuch.IndexOf("Then") + "Then".Length).Trim();
                iStat.AddToScope(Statement.ParseArithmetic(statement));
            }
        }
    }
}
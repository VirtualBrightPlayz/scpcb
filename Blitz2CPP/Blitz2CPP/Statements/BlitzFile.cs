using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
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

        private List<TypeDecl> typeDecls;

        private List<Variable> globals;
        private List<Variable> constants;

        private List<Function> functions;

        public BlitzFile(string path)
        {
            filePath = path;
            bbFile = new StreamReader(new FileStream(path, FileMode.Open));

            string dest = Toolbox.GetProjectDirectory() + Constants.DIR_OUTPUT + Path.GetFileNameWithoutExtension(path);
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
                    // Type declaration?
                    if (line.StartsWith("Type "))
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
                    // Functions.
                    else if (line.StartsWith("Function "))
                    {
                        AddFunction(line);
                    }
                    // End of function, clear scope stack.
                    else if (line.StartsWith("End Function"))
                    {
                        scopes.Clear();
                    }
                    else
                    {
                        // Check for inline comments on statements. Place them right before the statement intended on being parsed.
                        if (line.Contains(';') && !line.StartsWith(';'))
                        {
                            // Make sure the semicolon is not inside of a string.
                            List<int> indexes = line.AllIndexesOf(";");
                            foreach (int index in indexes)
                            {
                                string str = line.JavaSubstring(0, index);
                                if (str.Count(x => x == '"') % 2 == 0)
                                {
                                    // If it's not in a string then remove the comment and write it in above the statement.
                                    Comment comment = Comment.Parse(line.Substring(index));

                                    // This isn't going to work for global-scope stuff but whatever.
                                    GetCurrScope?.AddToScope(comment);

                                    line = line.JavaSubstring(0, index);
                                }
                            }
                        }

                        // TODO: This might break for ' : ' usages in strings or comments.
                        // Multi-line?
                        string[] multi = line.Split(" : ");
                        foreach (string statement in multi)
                        {
                            ParseLine(statement.Trim());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("CurrFile: " + filePath + ", CurrLine: " + currLine + "\n" + e.ToString());
            }
        }

        private void ParseLine(string info)
        {
            // Global scope stuff.
            if (!scopes.Any())
            {
                if (info.StartsWith("Global "))
                {
                    AddGlobal(info);
                }

                else if (info.StartsWith("Const "))
                {
                    AddConstant(info);
                }
            }

            // Block statements.

            // If statements.
            else if (info.StartsWith("If "))
            {
                AddIf(info);
            }

            else if (info.StartsWith("ElseIf "))
            {
                if (GetCurrScope is IfStatement iStat)
                {
                    Statement condition = Statement.ParseArithmetic(info.JavaSubstring("ElseIf (".Length, info.IndexOf(") Then")));
                    condition.semicolon = false;
                    iStat.elseIfStatements.Add(condition, new List<Statement>());
                }
                else
                {
                    throw new Exception("\"ElseIf\" without If statement.");
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

            // Locals.
            else if (info.StartsWith("Local "))
            {
                info = info.Substring("Local ".Length);
                string[] decls = info.Split(',', StringSplitOptions.RemoveEmptyEntries);
                foreach (string decl in decls)
                {
                    Variable var = Variable.Parse(decl);
                    GetCurrScope.AddToScope(var);
                }
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
                ForLoop fl = ForLoop.Parse(info);
                AddScope(fl);
            }

            else if (info.StartsWith("While "))
            {
                WhileLoop wl = WhileLoop.Parse(info);
                AddScope(wl);
            }

            else if (info.StartsWith("EndIf") || info.StartsWith("End Select") || info.StartsWith("Wend") || info.StartsWith("Next"))
            {
                scopes.Pop();
            }

            // Normal statement.
            else if (info.StartsWith(';'))
            {
                Comment comm = Comment.Parse(info);
                GetCurrScope.AddToScope(comm);
            }
            else if (string.IsNullOrWhiteSpace(info))
            {
                GetCurrScope.AddToScope(new NewLine());
            }
            else
            {
                GetCurrScope.AddToScope(Statement.ParseArithmetic(info, true));
            }
        }

        private void AddGlobal(string decl)
        {
            decl = decl.Substring("Global ".Length);
            string[] decls = decl.Split(',', StringSplitOptions.RemoveEmptyEntries);
            foreach (string glob in decls)
            {
                Variable var = Variable.Parse(glob);
                globals.Add(var);
            }
        }

        private void AddConstant(string decl)
        {
            decl = decl.Substring("Const ".Length);
            string[] decls = decl.Split(',', StringSplitOptions.RemoveEmptyEntries);
            foreach (string glob in decls)
            {
                Variable var = Variable.Parse(glob);
                constants.Add(var);
            }
        }

        private void AddFunction(string decl)
        {
            Function func = Function.Parse(decl);
            functions.Add(func);
            scopes.Push(func);
        }

        private void AddIf(string tuch)
        {
            IfStatement iStat = IfStatement.Parse(tuch);
            GetCurrScope.AddToScope(iStat);

            // One line if statement?
            if (tuch.EndsWith("Then"))
            {
                scopes.Push(iStat);
            }
            else
            {
                string statement = tuch.Substring(tuch.IndexOf("Then") + "Then".Length).Trim();
                iStat.AddToScope(Statement.ParseArithmetic(statement, true));
            }
        }

        private void AddScope(ScopeStatement scope)
        {
            GetCurrScope.AddToScope(scope);
            scopes.Push(scope);
        }

        public void WriteHeaderFile()
        {
            string headerInclude = Path.GetFileNameWithoutExtension(filePath).ToUpper() + "_H_INCLUDED";
            headerFile.WriteLine("#ifndef " + headerInclude);
            headerFile.WriteLine("#define " + headerInclude);
            headerFile.WriteLine();
            headerFile.WriteLine("namespace " + Constants.CPP_NAMESPACE + " {");
            headerFile.WriteLine();

            if (constants.Any())
            {
                headerFile.WriteLine("// Constants.");
                foreach (Variable constant in constants)
                {
                    headerFile.WriteLine("extern const " + constant.GetDefinition());
                }
                headerFile.WriteLine();
            }

            if (globals.Any())
            {
                headerFile.WriteLine("// Globals.");
                foreach (Variable global in globals)
                {
                    headerFile.WriteLine("extern " + global.GetDefinition());
                }
                headerFile.WriteLine();
            }

            if (functions.Any())
            {
                headerFile.WriteLine("// Functions.");
                foreach (Function func in functions)
                {
                    headerFile.WriteLine(func.GetSignature() + ";");
                    headerFile.WriteLine();
                }
            }
            headerFile.WriteLine("}");
            headerFile.WriteLine("#endif // " + headerInclude);
        }

        public void WriteCPPFile()
        {
            string headerFileName = Path.GetFileNameWithoutExtension(filePath) + ".h";
            srcFile.WriteLine("#include <" + headerFileName + ">");
            srcFile.WriteLine();

            srcFile.WriteLine("namespace " + Constants.CPP_NAMESPACE + " {");
            srcFile.WriteLine();

            if (constants.Any())
            {
                srcFile.WriteLine("// Constants.");
                foreach (Variable constant in constants)
                {
                    srcFile.WriteLine(constant.Parse2CPP());
                }
                srcFile.WriteLine();
            }

            if (globals.Any())
            {
                srcFile.WriteLine("// Globals.");
                foreach (Variable global in globals)
                {
                    srcFile.WriteLine(global.Parse2CPP());
                }
                srcFile.WriteLine();
            }

            if (functions.Any())
            {
                srcFile.WriteLine("// Functions.");
                foreach (Function func in functions)
                {
                    srcFile.WriteLine(func.Parse2CPP());
                    srcFile.WriteLine();
                }
            }
            srcFile.WriteLine("}");
        }
    }
}
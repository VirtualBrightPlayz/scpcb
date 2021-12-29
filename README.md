# CBN

The original game's source code can be found here: https://github.com/Regalis11/scpcb

The game is based on the works of the [SCP Foundation community](http://www.scp-wiki.net/).

# Requirements:

Windows:
* [Visual Studio 2022](https://visualstudio.microsoft.com/vs/).

# Before compiling:
* Make sure the Working Directory of the SCPCB project is set to `$(TargetDir)` (Configuration Properties -> Debugging).

# License
Licensing terms can be found in [License.txt](Content/License.txt).

# List of things to avoid doing in the game's code:

* Not adhering to the [PGE guidelines](https://github.com/juanjp600/pge/blob/main/GUIDELINES_TOC.md).
* Anything that isn't OTBS (Allman, GNU, etc).
* Omitting brackets when they can be used.
* Any language other than C++, AngelScript and JavaScript (for scripts).
* Anything other than lower camelCase for functions and variables.
* Anything other than upper CamelCase for class/struct/enum names.
* Anything other than screaming SNAKE_CASE for constants and enum values.
* Global variables.
* Tabs & indentations other than multiples of 4 spaces.
* Anything other than LF newlines.
* `#pragma once`.
* Text/source files without a newline at the end.
* Declaring several variables in one statement.
* Placing the pointer/reference symbol anywhere other than next to the type.

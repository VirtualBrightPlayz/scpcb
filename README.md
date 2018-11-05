# CBN

Requirements:
* [Visual Studio 2017][vs2017]

# License

The original game's source code can be found here: https://github.com/Regalis11/scpcb

The game is based on the works of the [SCP Foundation community][scp-wiki].

This game and the source code are licensed under [Creative Commons Attribution-ShareAlike 3.0 License][cc3.0].

# List of banned stuff:

* Anything that isn't K&R bracket style (Allman, GNU, etc).
* Omitting brackets when they can be used.
* Any language other than C or C++.
* Anything other than lower camelCase for functions and variables.
* Anything other than upper CamelCase for class/struct names.
* `using` directive for any namespace other than CBN.
* `using` directive in header files.
* `std::string`.
* `switch` cases that don't create a new scope.
* Tabs & indentations other than multiples of 4 spaces.
* Anything other than LF newlines.
* `#define` for anything but include safeguards.
* `#pragma once`.
* Text/source files without a newline at the end.
* Declaring several variables in one statement.
* Placing the pointer/reference symbol anywhere other than next to the type.
* Any doc commenting style besides [XML Comments][xmldoc].
* `auto` keyword.

[vs2017]: https://visualstudio.microsoft.com/vs/
[scp-wiki]: http://www.scp-wiki.net/
[cc3.0]: http://creativecommons.org/licenses/by-sa/3.0/
[xmldoc]: https://www.stack.nl/~dimitri/doxygen/manual/xmlcmds.html

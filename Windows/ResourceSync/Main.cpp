#include <filesystem>
#include <unordered_set>
#include <iostream>

#include <PGE/File/FilePath.h>
#include <PGE/String/Key.h>
#include <PGE/Exception/Exception.h>

using namespace PGE;

namespace fs = std::filesystem;

std::unordered_set<String::Key> extensions;

bool check(const fs::directory_entry& file) {
    if (file.is_directory()) {
        return false;
    }
    if (extensions.empty()) {
        return true;
    }
    if (extensions.find(String(file.path().extension().c_str())) != extensions.end()) {
        return true;
    }
    return false;
}

int main(int argc, const char** argv) {
    FilePath src;
    FilePath bin;

    if (argc > 2) {
        src = FilePath::fromStr(argv[1]);
        bin = FilePath::fromStr(argv[2]);
    } else {
        String input;
        std::cout << "src: ";
        std::cin >> input;
        src = FilePath::fromStr(input);
        input = String();
        std::cout << "bin: ";
        std::cin >> input;
        bin = FilePath::fromStr(input);
    }
    src = src.makeDirectory();
    bin = bin.makeDirectory();

    if (argc > 3) {
        // : because it's illegal in filenames.
        std::vector<String> ext = String(argv[3]).split(":", false);
        extensions.insert(ext.begin(), ext.end());
    }

    std::unordered_set<String::Key> sources;
    for (const fs::directory_entry& file : fs::recursive_directory_iterator(src.str().cstr())) {
        if (check(file)) {
            sources.insert(String(file.path().string()).substr(src.str().length()));
        }
    }

    for (const fs::directory_entry& file : fs::recursive_directory_iterator(bin.str().cstr())) {
        if (check(file) && sources.find(String(file.path().string()).substr(bin.str().length())) == sources.end()) {
            std::cout << "Deleting leftover: " << file.path() << std::endl;
            fs::remove(file);
        }
    }
}

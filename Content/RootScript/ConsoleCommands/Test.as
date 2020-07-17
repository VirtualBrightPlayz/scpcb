shared class TesttCommand : ConsoleCommand {
    string getName() {
        return "uh";
    }

    string getHelp() {
        return "Does a thing";
    }

    void execute(int param1, string param2) {
        Debug::log("gotem");
        Debug::log(string(param1));
        Debug::log(param2);
    }
}
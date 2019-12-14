class Test {
    string publicProperty;
    protected int protectedProperty;
    private int privateProperty;

    int getPrivateProperty(string someArg) {
        return privateProperty;
    }
};

int32 test(string arg) {
    int32 retVal = arg[0];
    retVal = retVal % 5;
    return retVal;
}

Test test2(Test inArg) {
    string a = inArg.publicProperty;
    string b = inArg.publicProperty;
    a += "b";
    b += "c";
    Test retVal;
    retVal.publicProperty = b+a.substr(1);
    return retVal;
}

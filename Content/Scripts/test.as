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

Test test2() {
    //assign same string constant to two variables, and then modify one
    //if all is working correctly then "acb" is returned
    string a = "a";
    string b = "a";
    a += "b";
    b += "c";
    Test retVal;
    retVal.publicProperty = b+a.substr(1);
    return retVal;
}

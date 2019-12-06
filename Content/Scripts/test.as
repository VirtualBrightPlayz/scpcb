class Test {
    int publicProperty;
    protected int protectedProperty;
    private int privateProperty;
};

int32 test(const string& in arg) {
    int32 retVal = arg[0];
    retVal = retVal % 5;
    return retVal;
}

string test2() {
    //assign same string constant to two variables, and then modify one
    //if all is working correctly then "a" is returned
    string a = "a";
    string b = "a";
    a += "b";
    b += "c";
    return b+a.substr(1);
}

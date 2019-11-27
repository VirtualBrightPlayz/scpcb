int32 test() {
    return 2;
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

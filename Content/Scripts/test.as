class Test {
    string publicProperty;
    protected array<int32> protectedProperty;
    private int32 privateProperty;

    int getPrivateProperty(string someArg) {
        return privateProperty;
    }
};

int32 test(string arg) {
    RM2@ testRm2 = LoadRM2("GFX/Map/Rooms/EntranceZone/hll_plain_2/hll_plain_2.rm2");
    DeleteRM2(testRm2);
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

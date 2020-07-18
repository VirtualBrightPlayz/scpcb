external shared class Room;

serialize EntranceZone@ entranceZone;

// TODO: remove these globals, they only exist for testing purposes
serialize Vector3f whVy = Vector3f(17, 14, 14);
serialize string uh = "aaah";
serialize Matrix4x4f worldMatrix = Matrix4x4f::constructWorldMat(Vector3f(0, 0, 0), Vector3f(1, 1, 1), Vector3f(0, 0, 0));

PlayerController@ testController;
Collision::Collection@ testCollCollection;

external shared enum RoomType;

external Zone@ test_shared_global;
external int testCounter;

void Test(int testString) {
    //Debug::log("Parameter: "+testString);
}

void main() {
    Debug::log("Starting up!");
    registerCommand("mytest", "THIS COMKMAND IS VERY EPIC", Test);
    registerCommand(
        "teleport",
        "Teleports the player to the coordinates sent as input",
        function(float x,float y,float z) {
            Debug::log("Called teleport!");
            testController.setPosition(Vector3f(x,y,z));
        });
    @entranceZone = EntranceZone();
    @test_shared_global = @entranceZone;
    @testCollCollection = Collision::Collection();
    @testController = PlayerController(5.0, 15.0);
    testController.setPosition(Vector3f(0,16,0));
    testController.setCollisionCollection(testCollCollection);
    entranceZone.registerRoom("hll_plain_4_a", Room4);
    entranceZone.registerRoom("hll_plain_2_a", Room2);
    entranceZone.generate();
    PerTick::register(update);
    PerFrame::register(render);
}

void update(float deltaTime) {
    __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(testController, Input::getDown());
}

void render(float interpolation) {
    if (test_shared_global == null) { return; }
    test_shared_global.render(interpolation);
}

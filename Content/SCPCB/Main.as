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

void main() {
	Debug::Log("Starting up!");
    @entranceZone = EntranceZone();
    @test_shared_global = @entranceZone;
    @testCollCollection = Collision::Collection();
    @testController = PlayerController(5.0, 15.0);
    testController.setPosition(Vector3f(0,16,0));
    testController.setCollisionCollection(testCollCollection);
    entranceZone.registerRoom("hll_plain_4", Room4, hll_plain_4::construct);
    entranceZone.registerRoom("hll_plain_2", Room2, hll_plain_2::construct);
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
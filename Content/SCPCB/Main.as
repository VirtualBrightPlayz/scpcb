external class Room;

serialize EntranceZone@ entranceZone;

// TODO: remove these globals, they only exist for testing purposes
serialize Vector3f whVy = Vector3f(17, 14, 14);
serialize string uh = "aaah";
serialize Matrix4x4f worldMatrix = Matrix4x4f::constructWorldMat(Vector3f(0.0, 0.0, 0.0), Vector3f(1.0, 1.0, 1.0), Vector3f(0, 0, 0));

PlayerController@ testController;
Collision::Collection@ testCollCollection;

external enum RoomType;

external Zone@ test_shared_global;
external int testCounter;

void Test(int testString) {
    //Debug::log("Parameter: "+testString);
}

Billboard@ lol;
Billboard@ two;

Model@ mask;
Model@ mask2;

external class Item;
external class ItemTemplate;

namespace Item {
    external void register(const string&in name, const string&in model, const string&in icon, float scale);
    external Item@ spawn(const string&in name, const Vector3f&in position);
    external void updateAll();
    external void renderAll();
}

shared class FirstAid : Item {
    void onPick() override {
        Debug::log("POG");
    }
    FirstAid(ItemTemplate it) {
        Debug::log("FirstAid constructor");
        super(it);
    }
}

external class Inventory;
external class MenuManager;
external Inventory@ inventory;

MenuManager@ menuManager;

void main() {
    Debug::log("Starting up!");

    @menuManager = MenuManager(inventory);

    Msg::set("LOL");

    Item::register("FirstAid", "SCPCB/GFX/Items/Firstaid/firstaid.fbx", "SCPCB/GFX/Items/Firstaid/inv_firstaid", 0.5);
    Item::spawn("FirstAid", Vector3f(0.0, 20.0, 20.0));
    
    Vector2f test = Vector2f(10.0, 10.0);
    Vector2f test2 = Vector2f(15.0, 10.0);

    @lol = Billboard::create("SCPCB/GFX/Sprites/smoke_white", Vector3f(1, 7, 5), 0.5, test, Color(1.0, 0.8, 0.5));
    
    Billboard::create("SCPCB/GFX/Map/Textures/dirtymetal", Vector3f(1, 4, 1), Vector3f(0, 3, 0), test2, Color(0.0, 1.0, 1.0));
    @two = Billboard::create("SCPCB/GFX/Map/Textures/dirtymetal", Vector3f(2, 7, 15), Vector3f(0, 3, 0), test2, Color(1.0, 0.0, 1.0));

    @mask = Model::create("SCPCB/GFX/Items/Gasmask/gasmask.fbx");
    mask.position = Vector3f(10, 5, 0);
    mask.rotation = Vector3f(-1, 0.1, 0);

    @mask2 = Model::create("SCPCB/GFX/Items/Gasmask/gasmask.fbx");
    mask2.position = Vector3f(-8, 4, 1);
    mask2.rotation = Vector3f(-1, -0.1, 0);

    //Debug::log(test_shared_global);

    registerCommand("mytest", "THIS COMKMAND IS VERY EPIC", Test);
    registerCommand(
        "teleport",
        "Teleports the player to the coordinates sent as input",
        function(float x,float y,float z) {
            Debug::log("Called teleport!");
            testController.setPosition(Vector3f(x,y,z));
        });
	registerCommand(
        "hElP",
        ":(",
        function(string s) {
            Debug::log(s);
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
    PerFrameGame::register(renderGame);
    PerFrameMenu::register(renderMenu);
}

float time = 0.0;

void update(float deltaTime) {
    if (!World::paused) {
        __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(testController, Input::getDown());
        entranceZone.update(deltaTime);
        time += deltaTime;
        if (time > 1.0) { // So you don't get a fucking seizure.
            two.visible = !two.visible;
            time = 0.0;
        }
    }
    Item::updateAll();
    menuManager.update(Input::getMousePosition(), Input::getMouseWheelDelta());
}

void renderGame(float interpolation) {
    if (test_shared_global == null) { return; }
    test_shared_global.render(interpolation);
    mask.render();
    mask2.render();
    Billboard::renderAll();
    Item::renderAll();
}

void renderMenu(float interpolation) {
    menuManager.render();
}

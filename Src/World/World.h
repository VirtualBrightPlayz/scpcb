#ifndef World_H_INCLUDED
#define World_H_INCLUDED

#include "../Wrap/Graphics.h"
#include "../Wrap/IO.h"
#include "../Graphics/Sprite.h"
#include "../Graphics/UIMesh.h"
#include "../Menus/GUI/GUIButton.h"

class Camera;
class Timing;
class UIAssets;
class ShaderManager;
class PauseMenu;

enum class GameState {
    Playing,
    Inventory,
    PauseMenu,
    Console
};

class World {
private:
    Graphics graphics;
    IO io;
    Camera* camera;

    Timing* timing;

    UIAssets* uiAssets;
    // TODO: Remove, these are a test.
    Sprite poster;
    GUIButton btn;

    ShaderManager* shaderMngt;

    GameState currState;
    // Changes the game state and does any initialization for that state.
    // This should be the only way to modify the current state.
    void setGameState(GameState gs);

    PauseMenu* pauseMenu;

    bool isRoadRollered;

    //-- Update/Draw code.

    // Goes through the update phase of a single tick.
    void runTick(float timeStep);

    // Update code for GameState::Playing game state.
    void updatePlaying(float timeStep);
    // Draw code for GameState::Playing game state.
    void drawPlaying();

    // Goes through the draw phase.
    void draw();

public:
    World();
    ~World();

    // Goes through the update/draw phrase of a single frame.
    bool run();
};

#endif // World_H_INCLUDED

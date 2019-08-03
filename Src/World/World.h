#ifndef WORLD_H_INCLUDED
#define WORLD_H_INCLUDED

#include <ft2build.h>
#include FT_FREETYPE_H
#include <Graphics/Graphics.h>
#include <IO/IO.h>

#include "../Graphics/Sprite.h"
#include "../Graphics/UIMesh.h"
#include "../Graphics/Font.h"
#include "../Input/KeyBinds.h"
#include "../Save/Config.h"

class Camera;
class Timing;
class GraphicsResources;
class TxtManager;
class PauseMenu;

enum class GameState {
    Playing,
    Inventory,
    PauseMenu,
    Console
};

class World {
    private:
        PGE::Graphics* graphics;
        PGE::IO* io;

        Camera* camera;

        KeyBinds* keyBinds;
        Config* config;
        Timing* timing;

        FT_Library ftLibrary;
        Font* largeFont;
        UIMesh* uiMesh;

        PGE::Mesh* spriteMesh;

        // TODO: Remove, these are a test.
        Sprite* poster;
        PGE::Texture* dirtymetal;

        GraphicsResources* gfxRes;
        TxtManager* txtMngt;

        GameState currState;

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

        // Prompts the world object to kill itself.
        void quit();

        // Changes the game state and does any initialization for that state.
        // This should be the only way to modify the current state.
        void setGameState(GameState gs);
    
        GameState getGameState() const;

};

#endif // WORLD_H_INCLUDED

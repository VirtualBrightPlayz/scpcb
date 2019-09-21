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
class FPSCounter;
class GUIText;
class Menu;

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
    
        Menu* currMenu;
        // When a menu is markedForRemoval it's moved here where it'll be deleted in the update loop.
        Menu* menuGraveyard;

        // TODO: Remove, these are a test.
        Sprite* poster;
        PGE::Texture* dirtymetal;

#ifdef DEBUG
        GUIText* mouseTxtX;
        GUIText* mouseTxtY;
#endif

        GraphicsResources* gfxRes;
        TxtManager* txtMngt;

        PauseMenu* pauseMenu;

        FPSCounter* fps;

        bool isRoadRollered;

        //-- Update/Draw code.

        // Goes through the update phase of a single tick.
        void runTick(float timeStep);

        // Update code for GameState::Playing game state.
        void updatePlaying(float timeStep, Input input);
        // Draw code for GameState::Playing game state.
        void drawPlaying();
        void loadPlaying();
        void destroyPlaying();

        // Goes through the draw phase.
        void draw();

    public:
        World();
        ~World();
    
        void activateMenu(Menu* mu);
        void deactivateMenu(Menu* mu);

        // Goes through the update/draw phrase of a single frame.
        bool run();

        // Prompts the world object to kill itself.
        void quit();

};

#endif // WORLD_H_INCLUDED

#include <iostream>
#include <cmath>
#include <Color/Color.h>

#include "World.h"
#include "Timing.h"
#include "ShaderManager.h"
#include "../Menus/PauseMenu.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIComponent.h"
#include "../Input/KeyBinds.h"

World::World() {
//    int width = 1280;
//    int height = 720;

    config = new Config("options.ini");
//    config->setResolution(width, height);

    graphics = PGE::Graphics::create("SCP - Containment Breach", config->getWidth(), config->getHeight(), false);
    graphics->setViewport(PGE::Rectanglei(0, 0, config->getWidth(), config->getHeight()));
    io = PGE::IO::create(graphics->getWindow());
    camera = new Camera(graphics, config->getAspectRatio());

    timing = new Timing(60);

    shaderMngt = new ShaderManager(graphics, camera);

    FT_Init_FreeType(&ftLibrary);
    fontShader = PGE::Shader::load(graphics, PGE::FileName::create("GFX/Shaders/Text/"));
    largeFont = new Font(ftLibrary, graphics, config, PGE::FileName::create("GFX/Font/cour.ttf"), 20, fontShader);
    spriteMesh = Sprite::createSpriteMesh(graphics);
    uiMesh = new UIMesh(graphics, config);
    keyBinds = new KeyBinds(io);

    dirtymetal = PGE::Texture::load(graphics, PGE::FileName::create("GFX/Map/Textures/dirtymetal.jpg"));
    poster = new Sprite(spriteMesh, dirtymetal, shaderMngt->getSpriteShader());
    poster->setPosition(0.f, 0.f, 2.f);
    poster->setRotation(0.5f);
    poster->setScale(1.f);

    setGameState(GameState::Playing);
    pauseMenu = new PauseMenu(uiMesh, largeFont, keyBinds, config);
    pauseMenu->hide();

    isRoadRollered = false;
}

World::~World() {
    delete pauseMenu;
    delete uiMesh;
    delete keyBinds;
    delete poster;
    delete spriteMesh;

    delete camera;
    delete timing;
    delete shaderMngt;

    delete io;
    delete graphics;
}

void World::setGameState(GameState gs) {
    GameState prev = currState;
    currState = gs;

    switch (currState) {
        case GameState::Playing: {
            io->setMousePosition(PGE::Vector2f(config->getWidth() / 2, config->getHeight() / 2));
        } break;

        case GameState::PauseMenu: {
            pauseMenu->show();
        } break;
    }

    io->setMouseVisibility(currState != GameState::Playing);

    if (prev == GameState::PauseMenu) { pauseMenu->hide(); }
}

GameState World::getGameState() const {
    return currState;
}

bool World::run() {
    if (isRoadRollered) {
        return false;
    }

    //Game logic updates first, use accumulator
    while (timing->tickReady()) {
        runTick((float)timing->getTimeStep());
        timing->subtractTick();
    }

    // Rendering next, don't use accumulator.
    graphics->update();

    graphics->clear(PGE::Color(0.f, 0.71f, 0.76f, 1.f)); // Turquoise.

    draw();

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);

    return graphics->getWindow()->isOpen();
}

void World::runTick(float timeStep) {
    SysEvents::update();
    io->update();

    // Get mouse position and convert it to screen coordinates.

    // Convert it to [0, 100].
    PGE::Vector2f scale = PGE::Vector2f(100.f / config->getWidth() , 100.f / config->getHeight());
    PGE::Vector2f mousePosition = PGE::Vector2f(io->getMousePosition().x * scale.x, io->getMousePosition().y * scale.y);
    mousePosition.x *= config->getAspectRatio();

    // Subtract 50 to bring it inline with the [-50, 50] screen coordinates.
    mousePosition.x -= 50.f * config->getAspectRatio();
    mousePosition.y -= 50.f;

    if (keyBinds->mouse1->isHit()) {
        std::cout << "MouseX: " << mousePosition.x << std::endl;
        std::cout << "MouseY: " << mousePosition.y << std::endl;
    }

    switch (currState) {
        case GameState::Playing: {
            updatePlaying(timeStep);
        } break;

        case GameState::PauseMenu: {
            pauseMenu->update(this, mousePosition);
        } break;
    }

    shaderMngt->update(camera);
}

void World::draw() {
    drawPlaying();

    // UI.
    graphics->setDepthTest(false);
    
    pauseMenu->render(this);
    
    graphics->setDepthTest(true);

    graphics->swap(config->isVsync());
}

void World::updatePlaying(float timeStep) {
    int centerX = config->getWidth() / 2;
    int centerY = config->getHeight()  / 2;

    // TODO: Sensitivity from Config class.
    float mouseXDiff = (float)(io->getMousePosition().x - centerX) / 300.f;
    float mouseYDiff = (float)(io->getMousePosition().y - centerY) / 300.f;

    camera->addAngle(mouseXDiff, mouseYDiff);
    camera->update();

    // Update the view matrix for the shaders.
    shaderMngt->update(camera);

    // Reset mouse to center.
    io->setMousePosition(PGE::Vector2f(centerX, centerY));

    poster->addRotation(5.f * timeStep);
    poster->update();

    if (keyBinds->escape->isHit()) {
        setGameState(GameState::PauseMenu);
    }
}

void World::drawPlaying() {
   poster->render();
}

void World::quit() {
    isRoadRollered = true;
}

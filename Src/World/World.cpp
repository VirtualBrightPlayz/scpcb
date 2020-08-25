#include "World.h"

#include <iostream>
#include <cmath>
#include <Color/Color.h>
#include <Misc/FileUtil.h>
#include <filesystem>

#include "Timing.h"
#include "Pickable.h"
#include "FPSCounter.h"
#include "ScriptWorld.h"
#include "../Graphics/Camera.h"
#include "../Graphics/GraphicsResources.h"
#include "../Graphics/DebugGraphics.h"
#include "../Menus/PauseMenu.h"
#include "../Menus/Console.h"
#include "../Menus/Inventory.h"
#include "../Menus/Menu.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIComponent.h"
#include "../Menus/GUI/GUIText.h"
#include "../Input/KeyBinds.h"
#include "../Input/Input.h"
#include "../Input/MouseData.h"
#include "../Utils/MessageManager.h"
#include "../Utils/LocalizationManager.h"
#include "../Utils/MathUtil.h"
#include "../Scripting/ScriptObject.h"

World::World() {
    config = new Config("options.ini");

    if (config->vr->value) {
        vrm = new VRManager(config, gfxRes);
        camera = vrm->getCamera();
    } else {
        vrm = nullptr;
        camera = new Camera(gfxRes, config->getWidth(), config->getHeight());
    }

    graphics = PGE::Graphics::create("SCP - Containment Breach", config->getWidth(), config->getHeight(), false);
    graphics->setViewport(PGE::Rectanglei(0, 0, config->getWidth(), config->getHeight()));
    io = PGE::IO::create(graphics->getWindow());

    timing = new Timing(60);

    gfxRes = new GraphicsResources(graphics, config);

    FT_Init_FreeType(&ftLibrary);
    largeFont = new Font(ftLibrary, gfxRes, config, PGE::FilePath::fromStr("SCPCB/GFX/Font/Inconsolata-Regular.ttf"), 20);
    uiMesh = new UIMesh(gfxRes);
    keyBinds = new KeyBinds(io);
    mouseData = new MouseData(io, config);

    locMng = new LocalizationManager(config->languageCode->value);
    msgMng = new MessageManager(locMng, uiMesh, keyBinds, config, largeFont);
    
    currMenu = nullptr;
    menuGraveyard = nullptr;
    io->setMouseVisibility(false);
    io->setMousePosition(PGE::Vector2f((float)config->getWidth() / 2, (float)config->getHeight() / 2));
    pauseMenu = new PauseMenu(this, uiMesh, largeFont, keyBinds, config, locMng, io);
    console = new Console(this, uiMesh, largeFont, keyBinds, config, locMng, io);
    inventory = new Inventory(this, uiMesh, keyBinds, config, 10);

    pickMng = new PickableManager(camera, uiMesh, keyBinds);

    billMng = new BillboardManager(graphics, gfxRes, camera);

    fps = new FPSCounter(uiMesh, keyBinds, config, largeFont);
    fps->visible = true;

    oldPaused = false;
    paused = false;

    scripting = new ScriptWorld(this, gfxRes, camera, keyBinds, mouseData, io, msgMng, locMng, pickMng, uiMesh, config, (float)timing->getTimeStep(), console, billMng);

    applyConfig(config);

#ifdef DEBUG
    mouseTxtX = new GUIText(uiMesh, keyBinds, config, largeFont, nullptr, 0.f, -5.f, false, false, Alignment::Bottom | Alignment::Left);
    mouseTxtY = new GUIText(uiMesh, keyBinds, config, largeFont, nullptr, 0.f, -2.5f, false, false, Alignment::Bottom | Alignment::Left);
#endif

    shutdownRequested = false;
    
    if (vrm != nullptr) {
        vrm->createTexture(graphics, config);
    }
}

World::~World() {
    delete scripting;

    delete fps;
    delete pickMng;
    delete billMng;
    delete pauseMenu;
    delete console;
    delete inventory;
    delete uiMesh;
    delete keyBinds;
    delete mouseData;

#ifdef DEBUG
    delete mouseTxtX;
    delete mouseTxtY;
#endif

    delete vrm;

    delete camera;
    delete timing;
    delete locMng;
    delete msgMng;

    delete io;
    delete graphics;
    delete gfxRes;
}

void World::applyConfig(const Config* config) {
    const Config::KeyBindsMap& keyboardMappings = config->getKeyboardBindings();
    for (const auto& it : keyboardMappings) {
        keyBinds->bindInput(it.first, it.second);
    }
}

void World::activateMenu(Menu* mu) {
    if (currMenu != nullptr) {
        throw std::runtime_error("Attempted to activate a menu while another was active.");
    }

    currMenu = mu;
    currMenu->onActivate();
}

void World::deactivateMenu(Menu* mu) {
    if (mu != currMenu) {
        throw std::runtime_error("Attempted to deactivate a menu that wasn't active.");
    }

    if (mu->isMarkedForDeath()) {
        menuGraveyard = mu;
    }
    currMenu = nullptr;
    mu->onDeactivate();
}

bool World::run() {
    if (shutdownRequested) {
        return false;
    }

    // Game logic updates first, use accumulator.
    while (timing->tickReady()) {
        timing->updateInterpolationFactor();
        runTick((float)timing->getTimeStep());
        timing->subtractTick();
    }

    // Rendering next, don't use accumulator.
    graphics->update();

    if (vrm == nullptr) {
        graphics->resetRenderTarget();
        graphics->clear(PGE::Color(1.f, 0.f, 1.f));
        draw((float)timing->getInterpolationFactor(), RenderType::All);
    } else {
        vrm->update();
        graphics->setRenderTarget(vrm->getTexture());

        vrm->setEye(true);
        graphics->clear(PGE::Color(0.f, 0.f, 0.f));
        draw(1.f, RenderType::NoUI);
        vr::Texture_t leftEyeTexture = {
            vrm->getTexture()->getNative(),
#ifdef __APPLE__
            vr::TextureType_OpenGL,
#else
            vr::TextureType_DirectX,
#endif
            vr::ColorSpace_Gamma
        };
        vr::VRCompositor()->Submit(vr::Eye_Left, &leftEyeTexture, NULL);

        vrm->setEye(false);
        graphics->clear(PGE::Color(0.f, 0.f, 0.f));
        draw(1.f, RenderType::NoUI);
        vr::Texture_t rightEyeTexture = {
        vrm->getTexture()->getNative(),
#ifdef __APPLE__
            vr::TextureType_OpenGL,
#else
            vr::TextureType_DirectX,
#endif
         vr::ColorSpace_Gamma
        };
        vr::VRCompositor()->Submit(vr::Eye_Right, &rightEyeTexture, NULL);

        graphics->resetRenderTarget();
        graphics->setDepthTest(false);
        uiMesh->setTextured(vrm->getTexture(), false);
        uiMesh->addRect(PGE::Rectanglef(-GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
            -GUIComponent::SCALE_MAGNITUDE,
            GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
            GUIComponent::SCALE_MAGNITUDE));
        uiMesh->endRender();
        draw(1.f, RenderType::UIOnly);
    }

    // Get elapsed seconds since last run.
    double secondsPassed = timing->getElapsedSeconds();
    timing->addSecondsToAccumulator(secondsPassed);
    fps->update(secondsPassed);

    return graphics->getWindow()->isOpen();
}

void World::runTick(float timeStep) {
    SysEvents::update();
    io->update();
    keyBinds->update();
    mouseData->update();

    Input downInputs = keyBinds->getDownInputs();
    Input hitInputs = keyBinds->getHitInputs();

#ifdef DEBUG
    mouseTxtX->setText(PGE::String("DownInputs: ") + (int)downInputs);
    mouseTxtY->setText(PGE::String("MouseWheelX: ") + io->getMouseWheelDelta().y);
#endif

    if (vrm != nullptr) {
        vrm->tick(timeStep);
    }

    // If a menu is in the graveyard then remove it.
    if (menuGraveyard != nullptr) {
        delete menuGraveyard;
        menuGraveyard = nullptr;
    }

    if (!paused) {
        updatePlaying(timeStep);
    } else {
        if (currMenu != nullptr) {
            currMenu->update(mouseData->getPosition(), mouseData->getWheelDelta());
        }
    }

    scripting->update();

    /*if (keyBinds->escape->isHit()) {
        // If a text input is active then escape de-selects it.
        // Unless it's the console's input.
        if (GUITextInput::hasSubscriber() && currMenu != nullptr && !currMenu->getType().equals("console")) {
            GUITextInput::deselectSubscribed();
        } else if (currMenu != nullptr) {
            currMenu->onEscapeHit();
        } else {
            activateMenu(pauseMenu);
            paused = true;
        }
    } else */if (inputWasFired(hitInputs, Input::ToggleConsole)) {
        if (currMenu == nullptr) {
            activateMenu(console);
        } else if (currMenu == console) {
            console->onEscapeHit();
        } // Otherwise another menu is already open.
    }/* else if (inputWasFired(hitInputs, Input::Inventory)) {
        if (currMenu == nullptr) {
            activateMenu(inventory);
        } else if (currMenu == inventory) {
            inventory->onEscapeHit();
        } // Otherwise another menu is already open.
    }*/

    /*if (currMenu == nullptr && !graphics->getWindow()->isFocused()) {
        activateMenu(pauseMenu);
    }*/

    // If a menu was closed this tick then reset the mouse position.
    if (paused != oldPaused) {
        if (paused) {
            io->setMouseVisibility(true);
        } else {
            io->setMouseVisibility(false);
            io->setMousePosition(PGE::Vector2f((float)config->getWidth() / 2, (float)config->getHeight() / 2));
        }
        oldPaused = paused;
    }
}

void World::draw(float interpolation, RenderType r) {
    if (r != RenderType::UIOnly) {
        drawPlaying(interpolation);
        scripting->drawGame(interpolation);

        if (vrm != nullptr && vrm->getFade() > 0.f) {
            graphics->setDepthTest(false);

            uiMesh->setTextureless();
            uiMesh->setColor(PGE::Color(0.f, 0.f, 0.f, vrm->getFade()));
            uiMesh->addRect(PGE::Rectanglef(-GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
                -GUIComponent::SCALE_MAGNITUDE,
                GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
                GUIComponent::SCALE_MAGNITUDE));
            uiMesh->endRender();
            graphics->setDepthTest(true);
        }
    }
    
    if (vrm != nullptr) {
        gfxRes->getDebugGraphics()->draw3DLine(PGE::Line3f(camera->position, camera->position.add(vrm->getHandPosition(true))), PGE::Color::Green, 1.f);
        gfxRes->getDebugGraphics()->draw3DLine(PGE::Line3f(camera->position, camera->position.add(vrm->getHandPosition(false))), PGE::Color::Red, 1.f);
    }

    if (r != RenderType::NoUI) {
        graphics->setDepthTest(false);

        scripting->drawMenu(interpolation);
        
        if (currMenu != nullptr) {
            currMenu->render();
        } else {
            msgMng->draw();
        }

        fps->draw();
#ifdef DEBUG
        mouseTxtX->render();
        mouseTxtY->render();
#endif

        graphics->setDepthTest(true);
    }

    graphics->swap(config->vsync->value);
}

void World::updatePlaying(float timeStep) {
    PGE::Vector2f center = PGE::Vector2f((float)config->getWidth(), (float)config->getHeight()).multiply(0.5f);
    
    PGE::Vector2f addAngle = io->getMousePosition().subtract(center).multiply(config->sensitivity->value / 30000.f);
    camera->addAngle(addAngle.x, addAngle.y);

    // Reset mouse to center.
    io->setMousePosition(center);

    // View/Projection matrix.
    camera->update();

    pickMng->update();

    msgMng->update(timeStep);
}

void World::drawPlaying(float interpolation) {
    camera->updateDrawTransform(interpolation);
    gfxRes->setCameraUniforms(camera);
    pickMng->render();
}

void World::quit() {
    shutdownRequested = true;
}

Font* World::getFont() const {
    return largeFont;
}

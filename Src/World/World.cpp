#include <iostream>
#include <cmath>
#include <Color/Color.h>
#include <Misc/FileUtil.h>
#include <filesystem>

#include "World.h"
#include "Timing.h"
#include "FPSCounter.h"
#include "ScriptWorld.h"
#include "../Graphics/Camera.h"
#include "../Graphics/GraphicsResources.h"
#include "../Menus/PauseMenu.h"
#include "../Menus/Console.h"
#include "../Menus/Menu.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIComponent.h"
#include "../Menus/GUI/GUIText.h"
#include "../Input/KeyBinds.h"
#include "../Input/Input.h"
#include "../Utils/TextMgmt.h"
#include "../Utils/MathUtil.h"
#include "../Scripting/ScriptObject.h"

PGE::Texture* left;
PGE::Texture* right;

World::World() {
    config = new Config("options.ini");

    if (config->isVr()) {
        if (!vr::VR_IsHmdPresent()) {
            throw new std::runtime_error("No HMD found.");
        }
        if (!vr::VR_IsRuntimeInstalled()) {
            throw new std::runtime_error("Open VR runtime not installed");
        }
        vr::EVRInitError e;
        vrSystem = vr::VR_Init(&e, vr::VRApplication_Scene);
        if (vrSystem == nullptr || e != vr::VRInitError_None) {
            throw std::runtime_error("VR failed to initialize! " + e);
        }
        unsigned int width;
        unsigned int height;
        vrSystem->GetRecommendedRenderTargetSize(&width, &height);
        std::cout << "OpenVR recommends a render size of " << width << "x" << height << std::endl;
        config->setResolution(width, height);
    }

    graphics = PGE::Graphics::create("SCP - Containment Breach", config->getWidth(), config->getHeight(), false);
    graphics->setViewport(PGE::Rectanglei(0, 0, config->getWidth(), config->getHeight()));
    io = PGE::IO::create(graphics->getWindow());

    timing = new Timing(60);

    gfxRes = new GraphicsResources(graphics, config);
    txtMngt = new TxtManager(config->getLangCode());

    FT_Init_FreeType(&ftLibrary);
    largeFont = new Font(ftLibrary, gfxRes, config, PGE::FilePath::fromStr("SCPCB/GFX/Font/Inconsolata-Regular.ttf"), 20);
    spriteMesh = Sprite::createSpriteMesh(graphics);
    uiMesh = new UIMesh(gfxRes);
    keyBinds = new KeyBinds(io);

    camera = new Camera(gfxRes, config->getWidth(), config->getHeight());
    camera->position = PGE::Vector3f(0.f, 10.f, 0.f);
    
    currMenu = nullptr;
    menuGraveyard = nullptr;
    io->setMouseVisibility(false);
    io->setMousePosition(PGE::Vector2f(config->getWidth() / 2, config->getHeight() / 2));
    pauseMenu = new PauseMenu(this, uiMesh, largeFont, keyBinds, config, txtMngt, io);
    console = new Console(this, uiMesh, largeFont, keyBinds, config, txtMngt, io);

    fps = new FPSCounter(uiMesh, keyBinds, config, largeFont);
    fps->visible = true;

    scripting = new ScriptWorld(gfxRes, camera, keyBinds, config, timing->getTimeStep(), console);

    applyConfig(config);

#ifdef DEBUG
    mouseTxtX =  new GUIText(uiMesh, keyBinds, config, largeFont, 0.f, -5.f, Alignment::Bottom | Alignment::Left);
    mouseTxtY =  new GUIText(uiMesh, keyBinds, config, largeFont, 0.f, -2.5f, Alignment::Bottom | Alignment::Left);
#endif

    shutdownRequested = false;

    left = PGE::Texture::create(graphics, config->getWidth(), config->getHeight(), true, new uint8_t[1000 * 1000 * 4], PGE::Texture::FORMAT::RGBA32); // TODO: Condition.
    right = PGE::Texture::create(graphics, config->getWidth(), config->getHeight(), true, new uint8_t[1000 * 1000 * 4], PGE::Texture::FORMAT::RGBA32);
}

World::~World() {
    delete pauseMenu;
    delete console;
    delete uiMesh;
    delete keyBinds;
    delete spriteMesh;
#ifdef DEBUG
    delete mouseTxtX;
    delete mouseTxtY;
#endif

    vr::VR_Shutdown();

    delete camera;
    delete timing;
    delete scripting;
    delete gfxRes;
    delete txtMngt;

    delete io;
    delete graphics;
}

void World::applyConfig(const Config* config) {
    std::map<Input, std::vector<PGE::KeyboardInput::KEYCODE>> keyboardMappings = config->getKeyboardBindings();
    std::map<Input, std::vector<PGE::KeyboardInput::KEYCODE>>::const_iterator it;
    for (it = keyboardMappings.begin(); it != keyboardMappings.end(); it++) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            keyBinds->bindInput(it->first, it->second[i]);
        }
    }
}

void World::activateMenu(Menu* mu) {
    if (currMenu != nullptr) {
        throw std::runtime_error("Attempted to activate a menu while another was active.");
    }

    currMenu = mu;
}

void World::deactivateMenu(Menu* mu) {
    if (mu != currMenu) {
        throw std::runtime_error("Attempted to deactivate a menu that wasn't active.");
    }

    if (mu->isMarkedForDeath()) {
        menuGraveyard = mu;
    }
    currMenu = nullptr;
}

vr::TrackedDevicePose_t tdp[vr::k_unMaxTrackedDeviceCount];
float eyedistance = 0.f;

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

    if (config->isVr()) {
        vr::VRCompositor()->WaitGetPoses(tdp, vr::k_unMaxTrackedDeviceCount, nullptr, 0);
        for (int i = 0; i < vr::k_unMaxTrackedDeviceCount; i++) {
            if (tdp[i].bDeviceIsConnected) {
                //std::cout << vrSystem->GetTrackedDeviceClass(i) << std::endl;
                if (tdp[i].bPoseIsValid) {
                    vr::HmdMatrix34_t mat = tdp->mDeviceToAbsoluteTracking;
                    PGE::Matrix4x4f pmat = PGE::Matrix4x4f(
                        mat.m[0][0], mat.m[1][0], mat.m[2][0], 0,
                        mat.m[0][1], mat.m[1][1], mat.m[2][1], 0,
                        mat.m[0][2], mat.m[1][2], mat.m[2][2], 0,
                        mat.m[0][3], mat.m[1][3], mat.m[2][3], 1
                    );
                    //system("cls");
                    //std::cout << std::endl << std::endl << std::endl << "Matrix!!!!" << std::endl <<
                    //    mat.m[0][0] << std::endl << mat.m[1][0] << std::endl << mat.m[2][0] << std::endl << std::endl << std::endl << "1" << std::endl <<
                    //    mat.m[0][1] << std::endl << mat.m[1][1] << std::endl << mat.m[2][1] << std::endl << std::endl << std::endl << "2" << std::endl <<
                    //    mat.m[0][2] << std::endl << mat.m[1][2] << std::endl << mat.m[2][2] << std::endl << std::endl << std::endl << "3" << std::endl <<
                    //    mat.m[0][3] << std::endl << mat.m[1][3] << std::endl << mat.m[2][3] << std::endl << std::endl << std::endl;
                    // Inverted looking in both directions, pls fix
                    camera->position = PGE::Vector3f(mat.m[0][3], mat.m[1][3], mat.m[2][3]).multiply(25.f);
                    camera->setUpVector(PGE::Vector3f(mat.m[0][1], mat.m[1][1], mat.m[2][1])); // Although flipping both x (exclusive) or z values fixes the horizontal viewing direction there exists
                    camera->setLookAt(PGE::Vector3f(mat.m[0][2], -mat.m[1][2], mat.m[2][2]));  // a deeper rooted problem that is now causing problems when looking ~45° up/down
                }
            }
        }
    }

    if (!config->isVr()) {
        graphics->resetRenderTarget();
        graphics->clear(PGE::Color(1.f, 0.f, 1.f, 1.f));
        draw((float)timing->getInterpolationFactor());
    } else {
        graphics->setRenderTarget(left);
        graphics->clear(PGE::Color(0.f, 0.f, 0.f, 1.f));
        draw(1.f);

        PGE::Vector3f lol = camera->getViewMatrix().extractViewTarget().crossProduct(camera->getViewMatrix().extractViewUp()).normalize().multiply(eyedistance); // Right vector
        graphics->setRenderTarget(right);
        camera->position = camera->position.add(lol);
        camera->update();
        graphics->clear(PGE::Color(0.f, 0.f, 0.f, 1.f));
        draw(1.f);
        camera->position = camera->position.subtract(lol);
        camera->update();

        graphics->resetRenderTarget();
        graphics->clear(PGE::Color::Green); // We probably don't need to clear here, but it illustrates the DephTest problem.
        graphics->setDepthTest(false);
        uiMesh->setTextured(right, false);
        uiMesh->addRect(PGE::Rectanglef(-GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
            -GUIComponent::SCALE_MAGNITUDE,
            GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio(),
            GUIComponent::SCALE_MAGNITUDE));
        uiMesh->endRender();
        graphics->setDepthTest(true);
        graphics->swap(config->isVsync());

        vr::Texture_t leftEyeTexture = {
        left->getNative(),
#ifdef __APPLE__
            vr::TextureType_OpenGL,
#else
            vr::TextureType_DirectX,
#endif
            vr::ColorSpace_Gamma
        };
        vr::Texture_t rightEyeTexture = {
        right->getNative(),
#ifdef __APPLE__
            vr::TextureType_OpenGL,
#else
            vr::TextureType_DirectX,
#endif
         vr::ColorSpace_Gamma
        };

        vr::VRCompositor()->Submit(vr::Eye_Left, &leftEyeTexture, NULL);
        vr::VRCompositor()->Submit(vr::Eye_Right, &rightEyeTexture, NULL);
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

    Input downInputs = keyBinds->getDownInputs();
    Input hitInputs = keyBinds->getHitInputs();

    // Get mouse position and convert it to screen coordinates.

    // Convert it to [0, 100].
    PGE::Vector2f scale = PGE::Vector2f(GUIComponent::SCALE_MAGNITUDE * 2 / config->getWidth(), GUIComponent::SCALE_MAGNITUDE * 2 / config->getHeight());
    PGE::Vector2f mousePosition = PGE::Vector2f(io->getMousePosition().x * scale.x, io->getMousePosition().y * scale.y);
    mousePosition.x *= config->getAspectRatio();

    // Subtract 50 to bring it inline with the [-50, 50] screen coordinates.
    mousePosition.x -= GUIComponent::SCALE_MAGNITUDE * config->getAspectRatio();
    mousePosition.y -= GUIComponent::SCALE_MAGNITUDE;

    PGE::Vector2i mouseWheelDelta = io->getMouseWheelDelta();
    eyedistance += mouseWheelDelta.y * 0.1f;
    std::cout << eyedistance << std::endl;

#ifdef DEBUG
    mouseTxtX->rt.text = PGE::String("DownInputs: ", PGE::String((int)downInputs));
    mouseTxtY->rt.text = PGE::String("MouseWheelX: ", PGE::String(io->getMouseWheelDelta().y));
#endif

    // If a menu is in the graveyard then remove it.
    if (menuGraveyard != nullptr) {
        delete menuGraveyard;
        menuGraveyard = nullptr;
    }

    bool menuWasOpened = currMenu != nullptr;
    if (!menuWasOpened) {
        updatePlaying(timeStep);
    } else {
        currMenu->update(mousePosition, mouseWheelDelta);
    }

    scripting->update();

    if (keyBinds->escape->isHit()) {
        // If a text input is active then escape de-selects it.
        // Unless it's the console's input.
        if (GUITextInput::hasSubscriber() && currMenu != nullptr && !currMenu->getType().equals("console")) {
            GUITextInput::deselectSubscribed();
        } else if (currMenu != nullptr) {
            currMenu->onEscapeHit();
        } else {
            activateMenu(pauseMenu);
        }
    } else if (inputWasFired(hitInputs, Input::ToggleConsole)) {
        if (currMenu == nullptr) {
            activateMenu(console);
        }
        else {
            console->onEscapeHit();
        }
    }

    if (currMenu == nullptr && !graphics->getWindow()->isFocused()) {
        activateMenu(pauseMenu);
    }

    // If a menu was closed this tick then reset the mouse position.
    if (menuWasOpened && currMenu == nullptr) {
        io->setMouseVisibility(false);
        io->setMousePosition(PGE::Vector2f(config->getWidth() / 2, config->getHeight() / 2));
    } else if (!menuWasOpened && currMenu != nullptr) {
        io->setMouseVisibility(true);
    }
}

void World::draw(float interpolation) {
    drawPlaying(interpolation);

    scripting->draw(interpolation);

    // UI.
    graphics->setDepthTest(false);

    if (currMenu != nullptr) {
        currMenu->render();
    }

    fps->draw();
#ifdef DEBUG
    mouseTxtX->render();
    mouseTxtY->render();
#endif

    graphics->setDepthTest(true);

    graphics->swap(config->isVsync());
}

void World::updatePlaying(float timeStep) {
    int centerX = config->getWidth() / 2;
    int centerY = config->getHeight() / 2;

    // TODO: Sensitivity from Config class.
    float addXAngle = (float)(io->getMousePosition().x - centerX) / 300.f;
    float addYAngle = (float)(io->getMousePosition().y - centerY) / 300.f;

    camera->addAngle(addXAngle, addYAngle);

    // Reset mouse to center.
    io->setMousePosition(PGE::Vector2f(centerX, centerY));

    // View/Projection matrix.
    camera->update();
}

void World::drawPlaying(float interpolation) {
    camera->updateDrawTransform(interpolation);
    gfxRes->setCameraUniforms(camera);
}

void World::destroyPlaying() {

}

void World::quit() {
    shutdownRequested = true;
}

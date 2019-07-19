#ifndef PAUSEMENU_H_INCLUDED
#define PAUSEMENU_H_INCLUDED

#include "../World/World.h"
#include "GUI/GUIButton.h"
#include "GUI/GUIFrame.h"

class PauseMenu {
public:
    enum class SubState {
        Main,
        NewGame,
        LoadGame,
        Options,
        Quitting,
        Hidden
    };

private:
    // Whether the pause menu is being shown on startup as oppose to in the middle of a game.
    bool isMainMenu;

    enum class OptionsTab {
        Graphics,
        Audio,
        Controls,
        Gameplay
    };

    SubState currState;
    OptionsTab currOptionsTab;

    void setOptionsTab(OptionsTab tab);

    // --Buttons.

    // Main state.
    GUIButton* newgame;
    GUIButton* loadgame;
    GUIButton* options;
    GUIButton* quit;

    // Quitting state.
    GUIFrame* quitFrame;
    GUIButton* quitYes;
    GUIButton* quitNo;

    UIMesh* uiMesh;
    KeyBinds* keyBinds;

public:
    PauseMenu(UIMesh* um, KeyBinds* kb);

    void setState(SubState state);

    void update(World* world, PGE::Vector2f mousePosition);
    void render();
};

#endif // PAUSEMENU_H_INCLUDED

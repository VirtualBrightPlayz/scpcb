#ifndef PAUSEMENU_H_INCLUDED
#define PAUSEMENU_H_INCLUDED

#include "GUI/GUIButton.h"
#include "GUI/GUIFrame.h"
#include "GUI/GUITextInput.h"
#include "../Graphics/Font.h"

class TxtManager;
class World;

class PauseMenu {
    private:
        enum class SubState {
            Main,
            NewGame,
            LoadGame,
            Options,
            Quitting,
            Hidden
        };

        // Whether the pause menu is being shown on startup as oppose to in the middle of a game.
        bool mainMenu;

        enum class OptionsTab {
            Graphics,
            Audio,
            Controls,
            Gameplay
        };

        SubState currState;
        OptionsTab currOptionsTab;

        void setState(SubState state);
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
    
        GUITextInput* inputTest;

        UIMesh* uiMesh;
        Font* largeFont;
        KeyBinds* keyBinds;

    public:
        PauseMenu(UIMesh* um, Font* largeFnt, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io);

        void show();
        void hide();
        bool isMainMenu() const;
        bool inSubMenu() const;

        void update(World* world, PGE::Vector2f mousePosition);
        void render();
};

#endif // PAUSEMENU_H_INCLUDED

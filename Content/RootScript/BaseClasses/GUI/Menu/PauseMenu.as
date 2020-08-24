shared PauseMenu@ pauseMenu = PauseMenu();

namespace PauseMenu {
    shared enum State {
        Main,
        Options,
        Quit
    }
}

shared class PauseMenu : Menu {
    private PauseMenu::State currState = PauseMenu::State::Main;

    GUIButton@ newGame;
    GUIButton@ loadGame;
    GUIButton@ options;
    GUIButton@ quit;

    GUIText@ quitText;
    GUIButton@ quitYes;
    GUIButton@ quitNo;

    PauseMenu() {
        super("pausemenu");

        const float btnWidth = 40.f;
        const float btnHeight = 7.f;

        const float btnSpacing = 10.f;
        const float btnX = -btnWidth / 2.f;
        float btnY = -40.f;

        @newGame = GUIButton(this, btnX, btnY, btnWidth, btnHeight, "MainMenu.NewGame");
        btnY += btnSpacing;
        @loadGame = GUIButton(this, btnX, btnY, btnWidth, btnHeight, "MainMenu.LoadGame");
        btnY += btnSpacing;
        @options = GUIButton(this, btnX, btnY, btnWidth, btnHeight, "MainMenu.Options");
        btnY += btnSpacing;
        @quit = GUIButton(this, btnX, btnY, btnWidth, btnHeight, "MainMenu.Quit");

        @quitText = GUIText(this, 0.f, -5.f, true, true, true);
        quitText.text = "MainMenu.Quit.Prompt";
        @quitYes = GUIButton(this, -25.f, 5.f, 12.f, 4.f, "MainMenu.Quit.Yes");
        @quitNo = GUIButton(this, 13.f, 5.f, 12.f, 4.f, "MainMenu.Quit.No");
        quitText.active = false;
        quitYes.active = false;
        quitNo.active = false;
    }

    bool onEscapeHit() override {
        return true;
    }

    bool update() override {
        return Input::getHit() & Input::Crouch != 0;
    }
}

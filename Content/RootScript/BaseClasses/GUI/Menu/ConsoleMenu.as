namespace ConsoleMenu {
    shared ConsoleMenu@ instance = ConsoleMenu();
    shared const float lineHeight = Font::large.getHeight(GUIText::defaultScale);
}

shared class ConsoleMenu : Menu {
    private GUIFrame@ frame;
    private GUITextInput@ input;

    private int windowMaxLineCount;

    private array<GUIText@> messageHistory;
    private float scrollOffset = 0.f;

    private float firstMessagePosition;

    ConsoleMenu() {
        super("console");
        float frameX = -GUIComponent::SCALE_MAGNITUDE * UI::getAspectRatio();
        float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * UI::getAspectRatio();

        @frame = GUIFrame(this, frameX, -GUIComponent::SCALE_MAGNITUDE, frameWidth, GUIComponent::SCALE_MAGNITUDE);
        @input = GUITextInput(this, frameX, 0, frameWidth, 5.f, true);

        firstMessagePosition = -Font::large.getHeight(GUIText::defaultScale) / 1.5f * 2 - GUIComponent::borderThickness * 2;
    }

    private void addConsoleMessage(const string&in msg, const Color&in color) {
        GUIText@ newText = GUIText(this, 0, 0, false, false, false); // -GUIComponent::SCALE_MAGNITUDE * UI::getAspectRatio() + GUIComponent::borderThickness * 2
        newText.text = msg;
        newText.color = color;
        messageHistory.insertLast(newText);
    }

    void onActivate() override {
        input.select();
    }

    bool onEscapeHit() override {
        return true;
    }

    bool update() override {
        if (Input::Enter::isHit() && input.txt != "") {
            addConsoleMessage("> " + input.txt, Color::Yellow);

            Console::execute(input.txt);

            input.clearTextAndMementos();
        }
        scrollOffset = Math::minFloat(0.f, scrollOffset - Input::getMouseWheelDelta().y);
        float currentHeight = firstMessagePosition - scrollOffset;
        for (int i = messageHistory.length() - 1; i >= 0; i--) {
            if (currentHeight <= 0.f && currentHeight > -GUIComponent::SCALE_MAGNITUDE) {
                messageHistory[i].active = true;
                messageHistory[i].y = currentHeight;
            } else {
                messageHistory[i].active = false;
            }
            currentHeight -= ConsoleMenu::lineHeight;
        }
        return Input::getHit() & Input::ToggleConsole != 0;
    }
}

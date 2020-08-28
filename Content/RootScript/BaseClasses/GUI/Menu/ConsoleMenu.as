namespace ConsoleMenu {
    shared ConsoleMenu@ instance = ConsoleMenu();
    shared const float lineHeight = Font::large.getHeight(GUIText::defaultScale) + 1.f;
}

namespace Console {
    shared void addMessage(const string&in msg, const Color&in color = Color::White) {
        ConsoleMenu::instance.addConsoleMessage(msg, color);
    }
}

shared class ConsoleMenu : Menu {
    private GUIFrame@ frame;
    private GUITextInput@ input;

    private array<string> commandHistory;
    private array<GUIText@> messageHistory;
    private float scrollOffset = 0.f;

    private float firstMessagePosition;

    ConsoleMenu() {
        super("console");
        float frameX = -GUIComponent::SCALE_MAGNITUDE * UI::getAspectRatio();
        float frameWidth = GUIComponent::SCALE_MAGNITUDE * 2.f * UI::getAspectRatio();

        @frame = GUIFrame(this, frameX, -GUIComponent::SCALE_MAGNITUDE, frameWidth, GUIComponent::SCALE_MAGNITUDE);
        @input = GUITextInput(this, frameX, 0, frameWidth, 5.f, true);

        firstMessagePosition = -Font::large.getHeight(GUIText::defaultScale) - GUIComponent::borderThickness * 2;
    }

    private void updateCoordinates() {
        scrollOffset = Math::minFloat(0.f, scrollOffset - Input::getMouseWheelDelta().y);
        float currentHeight = firstMessagePosition - scrollOffset;
        float lowestPoint = -Font::large.getHeight(GUIText::defaultScale) - GUIComponent::borderThickness * 2;
        float highestPoint = -GUIComponent::SCALE_MAGNITUDE + GUIComponent::borderThickness * 2;
        for (int i = messageHistory.length() - 1; i >= 0; i--) {
            if (currentHeight <= lowestPoint && currentHeight >= highestPoint) {
                messageHistory[i].active = true;
                messageHistory[i].y = currentHeight;
            } else {
                messageHistory[i].active = false;
            }
            currentHeight -= ConsoleMenu::lineHeight;
        }
    }

    void addConsoleMessage(const string&in msg, const Color&in color) {
        GUIText@ newText = GUIText(this, -GUIComponent::SCALE_MAGNITUDE * UI::getAspectRatio() + GUIComponent::borderThickness * 2, 0, false, false, false);
        newText.text = msg;
        newText.color = color;
        messageHistory.insertLast(newText);
        updateCoordinates();
    }

    void clear() {
        components = array<GUIComponent@>(0);
        messageHistory = array<GUIText@>(0);
        frame.addToMenu(this);
        input.addToMenu(this);
    }

    void onActivate() override {
        input.select();
        updateCoordinates();
    }

    void onDeactivate() override {
        input.deselect();
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
        if (Input::getMouseWheelDelta().y != 0.0) {
            updateCoordinates();
        }
        return Input::getHit() & Input::ToggleConsole != 0;
    }
}

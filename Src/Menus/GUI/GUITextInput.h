#ifndef GUITextInput_H_INCLUDED
#define GUITextInput_H_INCLUDED

#include <regex>

#include "GUIComponent.h"
#include "../../Graphics/Font.h"

class GUITextInput : public GUIComponent {
private:
    static GUITextInput* subscriber;

    PGE::FileName menuwhite;
    PGE::FileName menublack;
    PGE::Color hoverColor;
    float borderThickness;
    PGE::Vector2f txtScale;

    Font* font;
    PGE::IO* io;
    PGE::String text;

    // If this pattern is defined, text will only be accepted if it matches it.
    std::regex rgxPattern;
    bool patternMatching;
    // Max amount of characters the input box can have.
    int characterLimit;

#if DEBUG
    PGE::String selectTxt;
#endif

#ifdef __APPLE__
    // Basically shift+arrow key after a drag or multi-click on macOS has different behavior than on Windows.
    // On macOS after a select drag or multi-click select the first shift+arrow combo you hit determines which side of the selection you're manipulating.
    // So this boolean determines when exactly to trigger that behavior because other types of highlighting/text-modifying don't do that.
    bool selectionWasDraggedOrClicked = false;
#endif

    // Determines if the box should track the mouse's movement as a drag action for selecting text.
    bool draggable;

    // Current position of the caret relative to the text.
    int caretPosition;
    // X coordinate of the caret.
    float caretX;
    // Current position of the start point of a selection.
    int selectionStartPosition;
    // Current position of the end point of a selection.
    int selectionEndPosition;
    bool anyTextSelected() const;

    // Returns an index corresponding to where the caret should be based on the provided X coordinate.
    int getCaretPosition(float mouseX);
    void setCaretPositionFromMouse(float mouseX);
    // Update caret's X position.
    void updateCaretX();
    // Updates the display text and stores momentos.
    void updateText(const PGE::String& newText);
    void deleteSelectedText();
    void addText(PGE::String& append);

    // Split up updateInternal() into these.

    // Update input text with any new characters that were inputted.
    void updateTextActions();
    // Deletion actions.
    void updateDeleleKeyActions();
    // Arrow keys actions.
    void updateArrowActions();
    // Mouse click/drag actions.
    void updateMouseActions(PGE::Vector2f mousePos);
    // Copy/Undo actions.
    void updateShortcutActions();

    void updateInternal(PGE::Vector2f mousePos) override;
    void renderInternal() override;

public:
    GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, PGE::IO* inIo, float x, float y, float width, float height, const PGE::String& defaultText = "", int limit = INT_MAX, const PGE::String& pattern = "", Alignment alignment = Alignment::CenterXY);

    void setText(const PGE::String& txt);
    PGE::String getText() const;

    // Select this textbox.
    void select();
    // Deselect this textbox;
    void deselect();

    static bool hasSubscriber();
    static void deselectSubscribed();
};

#endif // GUITextInput_H_INCLUDED

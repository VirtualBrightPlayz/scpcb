#ifndef GUITextInput_H_INCLUDED
#define GUITextInput_H_INCLUDED

#include <regex>

#include "GUIComponent.h"
#include "../../Graphics/Font.h"

class GUITextInput : public GUIComponent {
private:
    static GUITextInput* subscriber;

    PGE::FilePath menuwhite;
    PGE::FilePath menublack;
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

    /// <summary>
    /// Whether the text should be aligned to the left side of the input box.
    /// </summary>
    bool leftAlignedText;

    // Y coordinate of the text in UI space, used for caret and selection.
    const float textY;
    // Current position of the caret relative to the text.
    int caretPosition;
    // X coordinate of the caret in UI space.
    float caretX;
    // Current position of the start point of a selection.
    int selectionStartPosition;
    // Current position of the end point of a selection.
    int selectionEndPosition;
    // Start and end coordinates of the selection in UI space.
    float selectionStartX;
    float selectionEndX;
    bool anyTextSelected() const;

    int getFirstLeftWordBoundary(int startingPosition) const;
    int getFirstRightWordBoundary(int startingPosition) const;

    /// <summary>
    /// Passes the XY coordinates of the text's starting point to the parameters by reference.
    /// </summary>
    /// <param name="outTextX">The X coordinate of the text.</param>
    /// <param name="outTextY">The Y coordinate of the text.</param>
    void fillTextCoordinates(float& outTextX, float& outTextY) const;
    // Returns an index corresponding to where the caret should be based on the provided X coordinate.
    int getCaretPosition(float mouseX);
    void setCaretPositionFromMouse(float mouseX);
    // Update caret's X position.
    void updateCoordinates();
    // Updates the display text and stores momentos.
    void updateText(const PGE::String& newText);
    void deleteSelectedText();
    void addText(PGE::String& append);

    // Split up updateInternal() into these.

    // Update input text with any new characters that were inputted.
    void updateTextActions();
    // Deletion actions.
    void updateDeleteKeyActions();
    // Arrow keys actions.
    void updateArrowActions();
    // Mouse click/drag actions.
    void updateMouseActions(PGE::Vector2f mousePos);
    // Copy/Undo actions.
    void updateShortcutActions();

    void updateInternal(PGE::Vector2f mousePos) override;
    void renderInternal() override;

public:
    GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, PGE::IO* inIo, float x, float y, float width, float height, bool alignLeft = false, const PGE::String& defaultText = "", int limit = INT_MAX, const PGE::String& pattern = "", Alignment alignment = Alignment::CenterXY);

    void setText(const PGE::String& txt);
    PGE::String getText() const;

    /// <summary>
    /// Select this textbox.
    /// </summary>
    void select();
    /// <summary>
    /// Deselect this textbox
    /// </summary>
    void deselect();

    static bool hasSubscriber();
    static void deselectSubscribed();
};

#endif // GUITextInput_H_INCLUDED

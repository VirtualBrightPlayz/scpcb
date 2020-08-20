#ifndef GUITextInput_H_INCLUDED
#define GUITextInput_H_INCLUDED

#include <regex>

#include "GUIComponent.h"
#include "MementoManager.h"
#include "../../Graphics/Font.h"

class GUIFrame;
class GUIText;

class GUITextInput : public GUIComponent {
    private:
        GUIFrame* frame;
        GUIText* text;
        
        MementoManager* mementoManager;

        static GUITextInput* subscriber;

        PGE::Color hoverColor;

        PGE::String defaultText;
        bool defaultTextDisplayed;

        PGE::IO* io;

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

        // Current position of the caret.
        int caretPosition;
        // Current position of the start and end points of the selection.
        int selectionStartPosition;
        int selectionEndPosition;

        // Cached positions for coordinate updates.
        int oldCaretPosition;
        int oldSelectionStartPosition;
        int oldSelectionEndPosition;

        // Various coordinates of the caret and selection in UI space.
        static constexpr float caretBreathingSpace = 0.25;
        float caretTop;
        float caretBottom;
        float caretX;
        float selectionStartX;
        float selectionEndX;

        void setCaretAndSelection(int pos);

        bool anyTextSelected() const;

        int getFirstLeftWordBoundary(int startingPosition) const;
        int getFirstRightWordBoundary(int startingPosition) const;
        
        // Returns an index corresponding to where the caret should be based on the provided X coordinate.
        int getCaretPosition(float mouseX);
        void setCaretPositionFromMouse(float mouseX);
        // Update caret's X position.
        void updateCoordinates();
        // These three functions must be used internally in order for mementos to work correctly.
        void addText(PGE::String& append);
        void deleteSelectedText();
        void removeText(int start, int end);

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
        GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, PGE::IO* inIo, float x, float y, float width, float height, bool alignLeft = false, int mementoMaxMemSize = 1048576, const PGE::String& defaultText = "", int limit = INT_MAX, const PGE::String& pattern = "", Alignment alignment = Alignment::CenterXY);
        ~GUITextInput();

        void setText(const PGE::String& txt);
        void clearTextAndMementos();
        PGE::String getText() const;

        // Select this textbox.
        void select();
        // Deselect this textbox
        void deselect();

        static bool hasSubscriber();
        static void deselectSubscribed();
};

#endif // GUITextInput_H_INCLUDED

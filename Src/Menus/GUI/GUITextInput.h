#ifndef GUITextInput_H_INCLUDED
#define GUITextInput_H_INCLUDED

#include "GUIComponent.h"
#include "../../Graphics/Font.h"

class TxtManager;

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
    
    bool pressed;
    
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
    // Updates the caret position and resets selection.
    void updateText(PGE::String newText, int oldCaretPosition);
    void undo();
    void redo();
    
    void updateInternal(PGE::Vector2f mousePos) override;
    void renderInternal() override;
    
public:
    GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* inIo, float x, float y, float width, float height, const PGE::String& defaultText, Alignment alignment = Alignment::CenterXY);
    
    PGE::String getText() const;
    
    // Select this textbox.
    void select();
    // Deselect this textbox;
    void deselect();
    
    static bool hasSubscriber();
    static void deselectSubscribed();
};

#endif // GUITextInput_H_INCLUDED

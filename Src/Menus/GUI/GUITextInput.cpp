#include "GUITextInput.h"
#include "../../Utils/TextMgmt.h"

GUITextInput* GUITextInput::subscriber = nullptr;

GUITextInput::GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* inIo, float x, float y, float width, float height, const PGE::String& defaultText, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    menuwhite = PGE::FileName::create("GFX/Menu/menuwhite.jpg");
    menublack = PGE::FileName::create("GFX/Menu/menublack.jpg");
    hoverColor = PGE::Color(70, 70, 150, 200);
    borderThickness = 0.33f;
    
    font = fnt;
    io = inIo;
    text = tm->getLocalTxt(defaultText);
    txtScale = PGE::Vector2f(100.f / 720.f);
    
    caretPosition = 0;
    selectionStartPosition = 0;
    selectionEndPosition = 0;
}

PGE::String GUITextInput::getText() const {
    return text;
}

bool GUITextInput::anyTextSelected() const {
    return selectionStartPosition != selectionEndPosition;
}

void GUITextInput::select() {
    if (subscriber != nullptr) {
        subscriber->deselect();
    }
    
    subscriber = this;
    io->startTextInputCapture();
    
    caretPosition = 0;
    selectionStartPosition = 0;
    selectionEndPosition = 0;
}

void GUITextInput::deselect() {
    io->stopTextInputCapture();
    subscriber = nullptr;
    
    caretPosition = 0;
    selectionStartPosition = 0;
    selectionEndPosition = 0;
}

void GUITextInput::deselectSubscribed() {
    subscriber->deselect();
}

void GUITextInput::setCaretPositionFromMouse(float mouseX) {
    caretX = getX() + width / 2 + font->stringWidth(text, txtScale) / 2;
    int newCaret;
    for (newCaret = text.size(); newCaret > 0; newCaret--) {
        float charWidth = font->stringWidth(text.charAt(newCaret - 1), txtScale);
        if (mouseX > caretX - charWidth / 2) {
            break;
        }
        caretX -= charWidth;
    }
    
    caretPosition = newCaret;
    selectionStartPosition = newCaret;
    selectionEndPosition = newCaret;
}

void GUITextInput::updateText(PGE::String newText, int oldCaretPosition) {
    // TODO: Momentos.
    text = newText;
}

void GUITextInput::updateInternal(PGE::Vector2f mousePos) {
    // TODO: Deck the hell out of this textbox.
    // Drag.
    // Shift click.
    // Arrow moving left and right.
    // Arrow while shift held moving selection left and right.
    // Arrow while selection is made snapping cursor to start/end of selection.
    // Copy, cut, paste.
    // Undo, redo.
    
    // Are we selected?
    if (subscriber != this) {
        // Check if the mouse selected us.
        if (mousePos.x >= getX() && mousePos.y >= getY()
            && mousePos.x <= getX2() && mousePos.y <= getY2()) {
            if (keyBinds->mouse1->isHit()) {
                select();
                // Move caret to mouse position.
                setCaretPositionFromMouse(mousePos.x);
            }
        }
    } else {
        // Update input text.
        PGE::String append = io->getTextInput();
        if (!append.isEmpty()) {
            PGE::String newText = text;
            int oldCaret = caretPosition;
            
            // If any text was selected then delete it.
            if (selectionEndPosition >= text.size()) {
                newText = text.substr(0, selectionStartPosition) + append;
            } else {
                newText = text.substr(0, selectionStartPosition) + append + text.substr(selectionEndPosition);
            }
            caretPosition = selectionStartPosition + append.size();
            selectionStartPosition = caretPosition;
            selectionEndPosition = caretPosition;
            updateText(newText, oldCaret);
        }
        
        if (keyBinds->mouse1->isHit()) {
            // If we're still in the textbox move the caret to the mouse's position.
            if (mousePos.x >= getX() && mousePos.y >= getY()
                && mousePos.x <= getX2() && mousePos.y <= getY2()) {
                // Move caret to mouse position.
                setCaretPositionFromMouse(mousePos.x);
            } else {
                // Mouse click outside of textbox.
                deselect();
            }
        }
        
        // Highlight all.
        // TODO: Crt+A cause why not.
        if (keyBinds->mouse1->doubleClicked()) {
            selectionStartPosition = 0;
            selectionEndPosition = text.size();
            caretPosition = 0;
        }
        
        // Update caret's X position.
        caretX = getX() + width / 2 - font->stringWidth(text, txtScale) / 2;
        caretX += font->stringWidth(text.substr(0, caretPosition), txtScale);
    }
}

void GUITextInput::renderInternal() {
    uiMesh->setTextured(menuwhite, true);
    uiMesh->addRect(PGE::Rectanglef(PGE::Vector2f(getX(), getY()), PGE::Vector2f(getX2(), getY2())));
    
    PGE::Rectanglef foreground = PGE::Rectanglef(PGE::Vector2f(getX() + borderThickness, getY() + borderThickness), PGE::Vector2f(getX2() - borderThickness, getY2() - borderThickness));
    uiMesh->setTextured(menublack, true);
    uiMesh->addRect(foreground);
    
    if (!text.isEmpty()) {
        // Render anything buffered so the text doesn't get overlapped.
        uiMesh->endRender();
        uiMesh->startRender();
        float txtX; float txtY;
        font->centerTextCoords(txtX, txtY, text, getX(), getY(), width, height, txtScale);
        
        font->draw(text, PGE::Vector2f(txtX, txtY), txtScale);
        
        // Render caret.
        if (subscriber == this) {
            float middleOffset = 1.f;
            float caretY = getY() + height / 2.f - middleOffset;
            
            uiMesh->setTextureless();
            uiMesh->addRect(PGE::Rectanglef(caretX, caretY, caretX + 0.3f, caretY + 3.f));
        }
    }
}

#include <iostream>

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
    
    pressed = false;
    
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

bool GUITextInput::hasSubscriber() {
    return subscriber != nullptr;
}

int GUITextInput::getCaretPosition(float mouseX) {
    float caretX = getX() + width / 2.f + font->stringWidth(text, txtScale) / 2.f;
    for (int newCaret = text.size(); newCaret > 0; newCaret--) {
        float charWidth = font->stringWidth(text.charAt(newCaret - 1), txtScale);
        if (mouseX > caretX - charWidth / 2.f) {
            return newCaret;
        }
        caretX -= charWidth;
    }
    
    return 0;
}

void GUITextInput::setCaretPositionFromMouse(float mouseX) {
    caretPosition = getCaretPosition(mouseX);
    selectionStartPosition = caretPosition;
    selectionEndPosition = caretPosition;
}

void GUITextInput::updateCaretX() {
    if (text.isEmpty()) {
        caretX = getX() + width / 2;
    } else {
        caretX = getX() + width / 2 - font->stringWidth(text, txtScale) / 2;
        caretX += font->stringWidth(text.substr(0, caretPosition), txtScale);
    }
}

void GUITextInput::updateText(PGE::String newText, int oldCaretPosition) {
    // TODO: Momentos.
    text = newText;
}

void GUITextInput::updateInternal(PGE::Vector2f mousePos) {
    // TODO: Deck the hell out of this textbox.
    // Shift click.
    // Arrow moving left and right.
    // Arrow while shift held moving selection left and right.
    // Arrow while shift after drag snaps to moving at the start and end.
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
                pressed = true;
                updateCaretX();
            }
        }
    } else {
        // Update input text with any new characters that were inputted.
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
        
        // Deletion.
        if (keyBinds->backspace->isHit() || keyBinds->del->isHit()) {
            // Delete selected text.
            if (anyTextSelected()) {
                PGE::String newText = text;
                int oldCaret = caretPosition;
                
                if (selectionEndPosition >= text.size()) {
                    newText = text.substr(0, selectionStartPosition);
                } else {
                    newText = text.substr(0, selectionStartPosition) + text.substr(selectionEndPosition);
                }
                caretPosition = selectionStartPosition;
                selectionStartPosition = caretPosition;
                selectionEndPosition = caretPosition;
                updateText(newText, oldCaret);
            } else {
                // Remove preceeding character if backspace, suceeding if delete.
                if (keyBinds->backspace->isHit() && caretPosition > 0) {
                    PGE::String newText = text.substr(0, caretPosition - 1) + text.substr(caretPosition);
                    int oldCaret = caretPosition;
                    updateText(newText, oldCaret);
                    
                    caretPosition--;
                    selectionStartPosition = caretPosition;
                    selectionEndPosition = caretPosition;
                } else if (keyBinds->del->isHit() && caretPosition < text.size()) {
                    PGE::String newText = text.substr(0, caretPosition) + text.substr(caretPosition + 1);
                    int oldCaret = caretPosition;
                    updateText(newText, oldCaret);
                }
            }
        }
        
        if (keyBinds->mouse1->isHit()) {
            // If we're still in the textbox move the caret to the mouse's position.
            if (mousePos.x >= getX() && mousePos.y >= getY()
                && mousePos.x <= getX2() && mousePos.y <= getY2()) {
                // Move caret to mouse position.
                setCaretPositionFromMouse(mousePos.x);
                pressed = true;
            } else {
                // Mouse click outside of textbox.
                deselect();
            }
        } else if (pressed && keyBinds->mouse1->isDown()) {
            // If we're dragging then select any text between the cursor and the caret.
            int mouseSnap = getCaretPosition(mousePos.x);
            if (mouseSnap != caretPosition) {
                if (mouseSnap > caretPosition) {
                    selectionEndPosition = mouseSnap;
                    selectionStartPosition = caretPosition;
                } else {
                    selectionStartPosition = mouseSnap;
                    selectionEndPosition = caretPosition;
                }
            }
        } else {
            pressed = false;
        }
        
        // Highlight all.
        // TODO: Crt+A cause why not.
        if (keyBinds->mouse1->doubleClicked()) {
            selectionStartPosition = 0;
            selectionEndPosition = text.size();
            caretPosition = 0;
            pressed = false; // Prevents a double click from being registered as a drag action.
        }
        
        updateCaretX();
        
        // Debug printing highlighted text.
        if (anyTextSelected()) {
            PGE::String str = "";
            for (int i = 0; i < text.size(); i++) {
                if (i == selectionStartPosition) {
                    str = str + "[";
                }
                
                str = str + text.charAt(i);
                
                if (i + 1 == selectionEndPosition) {
                    str = str + "]";
                }
            }
            std::cout << "HIGHLIGHT: " << str << std::endl;
        }
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
    }
    
    // Render caret.
    if (subscriber == this) {
        float middleOffset = 1.f;
        float caretY = getY() + height / 2.f - middleOffset;
        
        uiMesh->setTextureless();
        uiMesh->addRect(PGE::Rectanglef(caretX, caretY, caretX + 0.3f, caretY + 3.f));
    }
}

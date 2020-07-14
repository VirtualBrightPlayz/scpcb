#include <iostream>
#include <string>

#include "GUITextInput.h"
#include "../../Utils/MathUtil.h"

GUITextInput* GUITextInput::subscriber = nullptr;

GUITextInput::GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, PGE::IO* inIo, float x, float y, float width, float height, bool alignLeft, const PGE::String& defaultText, int limit, const PGE::String& pattern, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    menuwhite = PGE::FilePath::fromStr("SCPCB/GFX/Menu/menuwhite.jpg");
    menublack = PGE::FilePath::fromStr("SCPCB/GFX/Menu/menublack.jpg");
    hoverColor = PGE::Color(70, 70, 150, 200);
    borderThickness = 0.33f;

    font = fnt;
    io = inIo;
    text = defaultText;
    txtScale = PGE::Vector2f(100.f / 720.f);

    if (!pattern.isEmpty()) {
        patternMatching = true;
        rgxPattern = std::regex(pattern.cstr());
    } else {
        patternMatching = false;
    }

    characterLimit = limit;
    draggable = false;
    leftAlignedText = alignLeft;

    caretPosition = 0;
    selectionStartPosition = 0;
    selectionEndPosition = 0;
}

// Regex stuff.
std::regex word("\\w");

void GUITextInput::setText(const PGE::String& txt) {
    text = txt;
    caretPosition = text.size();
    selectionStartPosition = caretPosition;
    selectionEndPosition = caretPosition;
}

PGE::String GUITextInput::getText() const {
    return text;
}

bool GUITextInput::anyTextSelected() const {
    return selectionStartPosition != selectionEndPosition;
}

int GUITextInput::nextShortcutJump(bool right) const {
    int jumpPos = std::max(caretPosition - (right ? 0 : 1), 0);
    while (text.charAt(jumpPos) == ' ' && ((right && jumpPos < text.size()) || (!right && jumpPos >= 0))) { //If left jumpPos may reach -1 because in the end +1 is always added.
        jumpPos = jumpPos + (right ? 1 : -1);
    }
    while (text.charAt(jumpPos) != ' ' && ((right && jumpPos < text.size()) || (!right && jumpPos >= 0))) { //Through some Xor stuff these two while loops could be merged, but it doesn't worth it.
        jumpPos = jumpPos + (right ? 1 : -1);
    }
    if (right) {
        while (text.charAt(jumpPos) == ' ' && jumpPos < text.size()) {
            jumpPos++;
        }
    }
    return right ? jumpPos : jumpPos + 1;
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

void GUITextInput::fillTextCoordinates(float& outTextX, float& outTextY) const {
    font->centerTextCoords(outTextX, outTextY, text, getX(), getY(), width, height, txtScale);
    if (leftAlignedText) {
        outTextX = getX();
    }
}

int GUITextInput::getCaretPosition(float mouseX) {
    float textWidth = font->stringWidth(text, txtScale);
    float textX = 0.f; float textY = 0.f;
    fillTextCoordinates(textX, textY);
    
    float caretX = textX + textWidth;
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
    float textX = 0.f; float textY = 0.f;
    fillTextCoordinates(textX, textY);

    caretX = textX;
    if (!text.isEmpty()) {
        caretX += font->stringWidth(text.substr(0, caretPosition), txtScale);
    }
}

void GUITextInput::updateText(const PGE::String& newText) {
    // TODO: Momentos.

    text = newText;
}

void GUITextInput::deleteSelectedText() {
    PGE::String newText = text;

    if (selectionEndPosition >= text.size()) {
        newText = text.substr(0, selectionStartPosition);
    } else {
        newText = text.substr(0, selectionStartPosition) + text.substr(selectionEndPosition);
    }
    updateText(newText);

    caretPosition = selectionStartPosition;
    selectionStartPosition = caretPosition;
    selectionEndPosition = caretPosition;
}

void GUITextInput::addText(PGE::String& append) {
    // Truncate the string if it's over the capacity.
    int newSize = text.size() + append.size();
    if (newSize > characterLimit) {
        append = append.substr(0, characterLimit - text.size());
    }

    PGE::String newText = text;

    // If any text was selected then delete it.
    if (selectionEndPosition >= text.size()) {
        newText = text.substr(0, selectionStartPosition) + append;
    } else {
        newText = text.substr(0, selectionStartPosition) + append + text.substr(selectionEndPosition);
    }

    updateText(newText);
    caretPosition = selectionStartPosition + append.size();
    selectionStartPosition = caretPosition;
    selectionEndPosition = caretPosition;
}

void GUITextInput::updateInternal(PGE::Vector2f mousePos) {
    // TODO: Undo, redo.

    // Are we selected?
    if (subscriber != this) {
        // Check if the mouse selected us.
        if (mousePos.x >= getX() && mousePos.y >= getY()
            && mousePos.x <= getX2() && mousePos.y <= getY2()) {
            if (keyBinds->mouse1->isHit()) {
                select();
                // Move caret to mouse position.
                setCaretPositionFromMouse(mousePos.x);
                draggable = true;
                updateCaretX();
            }
        }
    } else {
        updateTextActions();
        updateDeleleKeyActions();
        updateArrowActions();
        updateMouseActions(mousePos);
        updateShortcutActions();

        updateCaretX();

#if DEBUG
        // Debug printing highlighted text.
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
        if (!str.equals(selectTxt)) {
            selectTxt = str;
            std::cout << "HIGHLIGHT: " << str << std::endl;
        }
#endif
    }
}

void GUITextInput::updateTextActions() {
    PGE::String append = io->getTextInput();
    if (!append.isEmpty() && text.size() < characterLimit) {
        addText(append);
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    }
}

void GUITextInput::updateDeleleKeyActions() {
    if (keyBinds->backspace->isHit() || keyBinds->del->isHit()) {
        // Delete selected text.
        if (anyTextSelected()) {
            deleteSelectedText();
        } else {
            // Remove preceeding character if backspace, suceeding if delete.
            if (keyBinds->backspace->isHit() && caretPosition > 0) {
                PGE::String newText;
                if (keyBinds->anyShortcutDown()) {
                    int delPos = nextShortcutJump(false);
                    newText = text.substr(0, delPos) + text.substr(caretPosition);
                    caretPosition -= caretPosition - delPos;
                } else {
                    newText = text.substr(0, caretPosition - 1) + text.substr(caretPosition);
                    caretPosition--;
                }
                selectionStartPosition = caretPosition;
                selectionEndPosition = caretPosition;
                updateText(newText);
            } else if (keyBinds->del->isHit() && caretPosition < text.size()) {
                PGE::String newText;
                if (keyBinds->anyShortcutDown()) {
                    newText = text.substr(0, caretPosition) + text.substr(nextShortcutJump(true));
                } else {
                    newText = text.substr(0, caretPosition) + text.substr(caretPosition + 1);
                }
                updateText(newText);
            }
        }
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    }
}

void GUITextInput::updateArrowActions() {
    if (keyBinds->leftArrow->isHit() || keyBinds->rightArrow->isHit()) {
        bool right = keyBinds->rightArrow->isHit();
        if (!keyBinds->anyShiftDown()) {
            if (anyTextSelected()) {
                // Snap to one side of the selection.
                caretPosition = right ? selectionEndPosition : selectionStartPosition;
            } else {
                // Shift caret position.
                if (keyBinds->anyShortcutDown()) {
                    caretPosition = nextShortcutJump(right);
                } else {
                    caretPosition = MathUtil::clamp(right ? caretPosition + 1 : caretPosition - 1, 0, text.size());
                }
            }
            selectionStartPosition = caretPosition;
            selectionEndPosition = caretPosition;
#ifdef __APPLE__
            selectionWasDraggedOrClicked = false;
#endif
        } else {
#ifdef __APPLE__
            if (selectionWasDraggedOrClicked) {
                caretPosition = right ? selectionStartPosition : selectionEndPosition;
                selectionWasDraggedOrClicked = false;
            }
#endif
            // Shift the selection index.
            if (keyBinds->anyShortcutDown()) {
                int prevCaret = caretPosition;
                int jumpTo;
                if (selectionStartPosition == caretPosition) {
                    caretPosition = selectionEndPosition;
                    jumpTo = nextShortcutJump(right);
                    if (jumpTo < selectionStartPosition) {
                        selectionStartPosition = jumpTo;
                        selectionEndPosition = prevCaret;
                    } else {
                        selectionEndPosition = jumpTo;
                    }
                } else {
                    caretPosition = selectionStartPosition;
                    jumpTo = nextShortcutJump(right);
                    if (jumpTo > selectionEndPosition) {
                        selectionEndPosition = jumpTo;
                        selectionStartPosition = prevCaret;
                    } else {
                        selectionStartPosition = jumpTo;
                    }
                }
                caretPosition = prevCaret;
            } else {
                if (right) {
                    if (selectionStartPosition != caretPosition) {
                        selectionStartPosition++;
                    } else {
                        selectionEndPosition++;
                    }
                } else {
                    if (selectionEndPosition != caretPosition) {
                        selectionEndPosition--;
                    } else {
                        selectionStartPosition--;
                    }
                }
            }
            if (selectionStartPosition < 0) { selectionStartPosition = 0; }
            if (selectionEndPosition > text.size()) { selectionEndPosition = text.size(); }
        }
    }
}

void GUITextInput::updateMouseActions(PGE::Vector2f mousePos) {
    if (keyBinds->mouse1->isHit() && keyBinds->mouse1->getClickCount() < 2) {
        // If we're still in the textbox move the caret to the mouse's position.
        if (mousePos.x >= getX() && mousePos.y >= getY()
            && mousePos.x <= getX2() && mousePos.y <= getY2()) {
            if (keyBinds->anyShiftDown()) {
                // Select text under cursor to caret.
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
                // Move caret to mouse position.
                setCaretPositionFromMouse(mousePos.x);
                draggable = true;
            }
        } else {
            // Mouse click outside of textbox.
            deselect();
        }
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    } else if (draggable && keyBinds->mouse1->isDown()) {
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
#ifdef __APPLE__
            selectionWasDraggedOrClicked = true;
#endif
        } else {
            selectionStartPosition = caretPosition;
            selectionEndPosition = caretPosition;
        }
    } else if (keyBinds->mouse1->getClickCount() == 2) {
        // TODO: Crt+A cause why not.
        selectionStartPosition = caretPosition;
        selectionEndPosition = caretPosition;

        // What direction are we going in?
        bool right = (mousePos.x >= caretX && caretPosition != text.size()) || caretPosition == 0;

        // Select all word-based characters until either the end or a boundary.
        // Unless the first character found IS a boundary, then we ONLY select that.
        // Let's check for that first character being a boundary first.
        bool boundaryContact = false;
        if (right) {
            boundaryContact = !std::regex_match(std::string(1, text.charAt(selectionEndPosition)), word, std::regex_constants::match_any);
            selectionEndPosition++;
        } else {
            boundaryContact = !std::regex_match(std::string(1, text.charAt(selectionStartPosition - 1)), word, std::regex_constants::match_any);
            selectionStartPosition--;
        }

        if (!boundaryContact) {
            // Scan both left and right sides of the caret for word characters.
            while (selectionEndPosition < text.size()) {
                if (std::regex_match(std::string(1, text.charAt(selectionEndPosition)), word, std::regex_constants::match_any)) {
                    selectionEndPosition++;
                } else {
                    break;
                }
            }
            while (selectionStartPosition > 0) {
                if (std::regex_match(std::string(1, text.charAt(selectionStartPosition - 1)), word, std::regex_constants::match_any)) {
                    selectionStartPosition--;
                } else {
                    break;
                }
            }
        }
        draggable = false; // Prevents a double click from being registered as a drag action.

#ifdef __APPLE__
        selectionWasDraggedOrClicked = true;
#elif defined(WINDOWS)
        // If you shift+arrow after a click selection on Windows, it defaults to manipulating the right-hand side.
        // So move the caret to the left to replicate that behavior.
        caretPosition = selectionStartPosition;
#endif
    } else if (keyBinds->mouse1->getClickCount() >= 3) {
        // Select all.
        selectionStartPosition = 0;
        selectionEndPosition = text.size();
        caretPosition = 0;
        draggable = false; // Prevents a triple click from being registered as a drag action.

#ifdef __APPLE__
        selectionWasDraggedOrClicked = true;
#elif defined(WINDOWS)
        // If you shift+arrow after a click selection on Windows, it defaults to manipulating the right-hand side.
        // So move the caret to the left to replicate that behavior.
        caretPosition = selectionStartPosition;
#endif
    }
}

void GUITextInput::updateShortcutActions() {
    if (keyBinds->copyIsHit() && anyTextSelected()) {
        io->setClipboardText(text.substr(selectionStartPosition, selectionEndPosition - selectionStartPosition));
    } else if (keyBinds->cutIsHit() && anyTextSelected()) {
        io->setClipboardText(text.substr(selectionStartPosition, selectionEndPosition - selectionStartPosition));
        deleteSelectedText();
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    } else if (keyBinds->pasteIsHit()) {
        PGE::String append = io->getClipboardText();
        if (!append.isEmpty() && text.size() < characterLimit) {
            if (anyTextSelected()) {
                deleteSelectedText();
            }
            addText(append);
#ifdef __APPLE__
            selectionWasDraggedOrClicked = false;
#endif
        }
    } else if (keyBinds->undoIsHit()) {
//        InputState is = memento.undo();
//        text = is.text;
//        caretPosition = is.caretPosition;
//        selectionStartPosition = is.selectionStartPosition;
//        selectionEndPosition = is.selectionEndPosition;
    } else if (keyBinds->redoIsHit()) {
//        InputState is = memento.redo();
//        text = is.text;
//        caretPosition = is.caretPosition;
//        selectionStartPosition = is.selectionStartPosition;
//        selectionEndPosition = is.selectionEndPosition;
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

        float txtX = 0.f;
        float txtY = 0.f;
        fillTextCoordinates(txtX, txtY);

        font->draw(text, PGE::Vector2f(txtX, txtY), txtScale);
    }

    // TODO: Hide caret when any text is highlighted.
    // Render caret.
    if (subscriber == this) {
        float middleOffset = 1.f;
        float caretY = getY() + height / 2.f - middleOffset;

        uiMesh->setTextureless();
        uiMesh->addRect(PGE::Rectanglef(caretX, caretY, caretX + 0.3f, caretY + 2.5f));
    }
}

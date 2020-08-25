#include "GUITextInput.h"

#if DEBUG
#include <iostream>
#endif

#include "GUIFrame.h"
#include "GUIText.h"
#include "../../Utils/MathUtil.h"
#include "../../Save/Config.h"

GUITextInput* GUITextInput::subscriber = nullptr;

GUITextInput::GUITextInput(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, PGE::IO* inIo, float x, float y, float width, float height, bool alignLeft, int mementoMaxMemSize, const PGE::String& defaultText, int limit, const PGE::String& pattern, Alignment alignment)
: GUIComponent(um, kb, con, x, y, width, height, alignment) {
    frame = new GUIFrame(um, kb, con, x, y, width, height, alignment);
    text = new GUIText(um, kb, con, fnt, nullptr, alignLeft ? x + um->borderThickness * 2 : x + width/2, y + height/2, !alignLeft, true, alignment);

    float textHeight = fnt->getHeight(text->scale);
    caretTop = getY() + (height - textHeight)/2 - caretBreathingSpace;
    caretBottom = caretTop + textHeight + caretBreathingSpace*2;

    mementoManager = new MementoManager(mementoMaxMemSize);

    setCaretAndSelection(0);
    oldCaretPosition = -1;
    oldSelectionEndPosition = -1;
    oldSelectionStartPosition = -1;
    updateCoordinates();

    this->defaultText = defaultText;
    text->setText(defaultText);
    text->color = PGE::Color::Gray;
    defaultTextDisplayed = true;
    
    io = inIo;

    if (!pattern.isEmpty()) {
        patternMatching = true;
        rgxPattern = std::regex(pattern.cstr());
    } else {
        patternMatching = false;
    }

    characterLimit = limit;
    draggable = false;
}

GUITextInput::~GUITextInput() {
    delete frame;
    delete text;
    delete mementoManager;
}

// Regex stuff.
std::regex word("\\w");
std::regex leftBoundWord("(\\b)\\w+$|\\w(\\b)\\W+$");
std::regex rightBoundWord("^\\w+(\\b)|^\\W+(\\b)\\w");

void GUITextInput::setText(const PGE::String& txt) {
    mementoManager->push(Memento(0, text->getText(), false, true));
    mementoManager->push(Memento(0, txt, true, true));
    text->setText(txt);
    setCaretAndSelection(text->getText().size());
}

void GUITextInput::clearTextAndMementos() {
    text->setText("");
    setCaretAndSelection(0);
    mementoManager->clear();
}

PGE::String GUITextInput::getText() const {
    return text->getText();
}

void GUITextInput::setCaretAndSelection(int pos) {
    caretPosition = pos;
    selectionStartPosition = pos;
    selectionEndPosition = pos;
}

bool GUITextInput::anyTextSelected() const {
    return selectionStartPosition != selectionEndPosition;
}

int GUITextInput::getFirstLeftWordBoundary(int startingPosition) const {
    if (startingPosition > 0) {
        PGE::String leftmostString = getText().substr(0, startingPosition);
        std::cmatch matches = leftmostString.regexMatch(leftBoundWord);
        int position = (int) (matches[1].matched ? matches.position(1) : matches.position(2));

        return position;
    }

    return startingPosition;
}

int GUITextInput::getFirstRightWordBoundary(int startingPosition) const {
    if (startingPosition < getText().size()) {
        PGE::String rightmostString = getText().substr(startingPosition, getText().size() - startingPosition);
        std::cmatch matches = rightmostString.regexMatch(rightBoundWord);
        int position = (int) (matches[1].matched ? matches.position(1) : matches.position(2));

        return position + startingPosition;
    }

    return startingPosition;
}

void GUITextInput::select() {
    if (subscriber != nullptr) {
        subscriber->deselect();
    }

    subscriber = this;
    io->startTextInputCapture();

    if (defaultTextDisplayed) {
        text->setText("");
        text->color = PGE::Color::White;
        defaultTextDisplayed = false;
    }
}

void GUITextInput::deselect() {
    io->stopTextInputCapture();
    subscriber = nullptr;

    if (text->getText().isEmpty()) {
        text->setText(defaultText);
        text->color = PGE::Color::Gray;
        defaultTextDisplayed = true;
    }
}

void GUITextInput::deselectSubscribed() {
    subscriber->deselect();
}

bool GUITextInput::hasSubscriber() {
    return subscriber != nullptr;
}

int GUITextInput::getCaretPosition(float mouseX) {
    float caretX = text->getTextPos().x + text->getWidth();
    for (int newCaret = text->getText().size(); newCaret > 0; newCaret--) {
        float charWidth = text->getWidth(text->getText().charAt(newCaret - 1));
        if (mouseX > caretX - charWidth / 2.f) {
            return newCaret;
        }
        caretX -= charWidth;
    }

    return 0;
}

void GUITextInput::setCaretPositionFromMouse(float mouseX) {
    setCaretAndSelection(getCaretPosition(mouseX));
}

void GUITextInput::updateCoordinates() {
    if (caretPosition == oldCaretPosition && selectionStartPosition == oldSelectionStartPosition && selectionEndPosition == oldSelectionEndPosition) {
        return;
    }

    oldCaretPosition = caretPosition;
    oldSelectionStartPosition = selectionStartPosition;
    oldSelectionEndPosition = selectionEndPosition;

    float textX = text->getTextPos().x;

    caretX = textX;
    if (!text->getText().isEmpty()) {
        caretX += text->getWidth(text->getText().substr(0, caretPosition));
    }

    selectionStartX = textX;
    if (!text->getText().isEmpty()) {
        selectionStartX += text->getWidth(text->getText().substr(0, selectionStartPosition));
    }

    selectionEndX = selectionStartX + text->getWidth(text->getText().substr(selectionStartPosition, selectionEndPosition - selectionStartPosition));
}

void GUITextInput::addText(PGE::String& append) {
    // Truncate the string if it's over the capacity.
    int newSize = text->getText().size() + append.size();
    if (newSize > characterLimit) {
        append = append.substr(0, characterLimit - text->getText().size());
    }

    if (selectionStartPosition != selectionEndPosition) {
        mementoManager->push(Memento(selectionStartPosition, text->getText().substr(selectionStartPosition, selectionEndPosition - selectionStartPosition), false, true));
    }
    mementoManager->push(Memento(selectionStartPosition, append, true, selectionStartPosition != selectionEndPosition));

    // If any text was selected then delete it.
    if (selectionEndPosition >= text->getText().size()) {
        text->setText(text->getText().substr(0, selectionStartPosition) + append);
    } else {
        text->setText(text->getText().substr(0, selectionStartPosition) + append + text->getText().substr(selectionEndPosition));
    }

    setCaretAndSelection(selectionStartPosition + append.size());
}

void GUITextInput::deleteSelectedText() {
    removeText(selectionStartPosition, selectionEndPosition);

    setCaretAndSelection(selectionStartPosition);
}

void GUITextInput::removeText(int start, int end) {
    mementoManager->push(Memento(start, text->getText().substr(start, end - start), false));
    text->setText(text->getText().substr(0, start) + text->getText().substr(end));
}

void GUITextInput::updateInternal(PGE::Vector2f mousePos) {
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
                updateCoordinates();
            }
        }
    } else {
        updateTextActions();
        updateDeleteKeyActions();
        updateArrowActions();
        updateMouseActions(mousePos);
        updateShortcutActions();

        updateCoordinates();

#if DEBUG
        // Debug printing highlighted text.
        PGE::String str = "";
        for (int i = 0; i < text->getText().size(); i++) {
            if (i == selectionStartPosition) {
                str = str + "[";
            }

            str = str + text->getText().charAt(i);

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
    if (!append.isEmpty() && text->getText().size() < characterLimit) {
        addText(append);
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    }
}

void GUITextInput::updateDeleteKeyActions() {
    if (keyBinds->backspace->isHit() || keyBinds->del->isHit()) {
        // Delete selected text.
        if (anyTextSelected()) {
            deleteSelectedText();
        } else {
            // Remove preceeding character if backspace, suceeding if delete.
            if (keyBinds->backspace->isHit() && caretPosition > 0) {
                int deletionStart = keyBinds->anyShortcutDown() ? getFirstLeftWordBoundary(caretPosition) : caretPosition - 1;
                removeText(deletionStart, caretPosition);
                setCaretAndSelection(deletionStart);
            } else if (keyBinds->del->isHit() && caretPosition < text->getText().size()) {
                removeText(caretPosition, keyBinds->anyShortcutDown() ? getFirstRightWordBoundary(caretPosition) : caretPosition + 1);
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
                setCaretAndSelection(right ? selectionEndPosition : selectionStartPosition);
            } else {
                // Shift caret position.
                if (keyBinds->anyShortcutDown()) {
                    if (right) {
                        setCaretAndSelection(getFirstRightWordBoundary(caretPosition));
                    } else {
                        setCaretAndSelection(getFirstLeftWordBoundary(caretPosition));
                    }
                } else {
                    setCaretAndSelection(MathUtil::clampInt(right ? caretPosition + 1 : caretPosition - 1, 0, text->getText().size()));
                }
            }
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
                if (right) {
                    if (selectionStartPosition != caretPosition) {
                        selectionStartPosition = getFirstRightWordBoundary(selectionStartPosition);
                    } else {
                        selectionEndPosition = getFirstRightWordBoundary(selectionEndPosition);
                    }
                } else {
                    if (selectionEndPosition != caretPosition) {
                        selectionEndPosition = getFirstLeftWordBoundary(selectionEndPosition);
                    } else {
                        selectionStartPosition = getFirstLeftWordBoundary(selectionStartPosition);
                    }
                }

                // If the selection areas overlapped while looking for boundaries swap them.
                if (selectionStartPosition > selectionEndPosition) {
                    int buff = selectionStartPosition;
                    selectionStartPosition = selectionEndPosition;
                    selectionEndPosition = buff;
                }
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
            if (selectionEndPosition > text->getText().size()) { selectionEndPosition = text->getText().size(); }
        }
    }
}

void GUITextInput::updateMouseActions(PGE::Vector2f mousePos) {
    if (keyBinds->mouse1->getClickCount() == 2) {
        // Prevents selectionEndPosition from assuming an illegal value.
        if (!text->getText().isEmpty()) {
            selectionStartPosition = caretPosition;
            selectionEndPosition = caretPosition;

            // What direction are we going in?
            bool right = (mousePos.x >= caretX && caretPosition != text->getText().size()) || caretPosition == 0;

            // Select all word-based characters until either the end or a boundary.
            // Unless the first character found IS a boundary, then we ONLY select that.
            // Let's check for that first character being a boundary first.
            bool boundaryContact = false;
            if (right) {
                boundaryContact = !std::regex_match(std::string(1, text->getText().charAt(selectionEndPosition)), word, std::regex_constants::match_any);
                selectionEndPosition++;
            } else {
                boundaryContact = !std::regex_match(std::string(1, text->getText().charAt(selectionStartPosition - 1)), word, std::regex_constants::match_any);
                selectionStartPosition--;
            }

            if (!boundaryContact) {
                // Scan both left and right sides of the caret for word characters.
                while (selectionEndPosition < text->getText().size()) {
                    if (std::regex_match(std::string(1, text->getText().charAt(selectionEndPosition)), word, std::regex_constants::match_any)) {
                        selectionEndPosition++;
                    } else {
                        break;
                    }
                }
                while (selectionStartPosition > 0) {
                    if (std::regex_match(std::string(1, text->getText().charAt(selectionStartPosition - 1)), word, std::regex_constants::match_any)) {
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
        }
    } else if (keyBinds->mouse1->getClickCount() >= 3) {
        // Select all.
        selectionStartPosition = 0;
        selectionEndPosition = text->getText().size();
        caretPosition = 0;
        draggable = false; // Prevents a triple click from being registered as a drag action.

#ifdef __APPLE__
        selectionWasDraggedOrClicked = true;
#elif defined(WINDOWS)
        // If you shift+arrow after a click selection on Windows, it defaults to manipulating the right-hand side.
        // So move the caret to the left to replicate that behavior.
        caretPosition = selectionStartPosition;
#endif
    } else if (keyBinds->mouse1->isHit()) {
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
    }
}

void GUITextInput::updateShortcutActions() {
    if (keyBinds->copyIsHit() && anyTextSelected()) {
        io->setClipboardText(text->getText().substr(selectionStartPosition, selectionEndPosition - selectionStartPosition));
    } else if (keyBinds->cutIsHit() && anyTextSelected()) {
        io->setClipboardText(text->getText().substr(selectionStartPosition, selectionEndPosition - selectionStartPosition));
        deleteSelectedText();
#ifdef __APPLE__
        selectionWasDraggedOrClicked = false;
#endif
    } else if (keyBinds->pasteIsHit()) {
        PGE::String append = io->getClipboardText();
        if (!append.isEmpty() && text->getText().size() < characterLimit) {
            if (anyTextSelected()) {
                deleteSelectedText();
            }
            addText(append);
#ifdef __APPLE__
            selectionWasDraggedOrClicked = false;
#endif
        }
    } else if (keyBinds->undoIsHit() || keyBinds->redoIsHit()) {
        text->setText(mementoManager->execute(text->getText(), caretPosition, keyBinds->undoIsHit()));
        setCaretAndSelection(caretPosition);
    } else if (keyBinds->selectAllIsHit()) {
        selectionStartPosition = 0;
        selectionEndPosition = text->getText().size();
    }
}

void GUITextInput::renderInternal() {
    /*frame->render();

    // Render caret.
    if (selectionStartPosition == selectionEndPosition) {
        if (subscriber == this) {
            uiMesh->setTextureless();
            uiMesh->addRect(PGE::Rectanglef(caretX, caretTop, caretX + 0.3f, caretBottom));
        }
    // Render selection backdrop.
    } else {
        uiMesh->setTextureless();
        uiMesh->setColor(subscriber == this ? PGE::Color(0.75f, 0.75f, 0.75f) : PGE::Color(0.25f, 0.25f, 0.25f)); // This is not necessary if deselection automatically resets the selection (which is currently the case as of commit #badafdb4).
        uiMesh->addRect(PGE::Rectanglef(selectionStartX, caretTop - 0.2f, selectionEndX, caretBottom + 0.2f));
        uiMesh->setColor(PGE::Color::White);
    }

    text->render();*/
}

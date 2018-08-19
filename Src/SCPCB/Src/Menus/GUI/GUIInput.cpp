#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIInput.h"
#include "../Menu.h"
#include "../../Assetmgmt/Assets.h"
#include "../../MathUtils/MathUtils.h"
#include "../../GameMain.h"

namespace CBN {

GUIInput::GUIInput() : GUIInput(0, 0, 0, 0, "", 0) { };

GUIInput::GUIInput(int x, int y, int width, int height, const String& displayTxt, int txtOffset, const String& defaultTxt, bool relative)
    : GUIButtonBase(x, y, width, height, relative) {
    displayText = displayTxt;
    textOffset = txtOffset;
    input = defaultTxt;
    caretPos = 0;
    selected = false;
    caretTimer = 0;
}

void GUIInput::update() {
    GUIButtonBase::update();
    if (isMouseHit()) {
        selected = true;

        int x = this->x + width / 2 + bbStringWidth(input) / 2;
        for (caretPos = input.size(); caretPos > 0; caretPos--) {
            if (bbMouseX() > x - (bbStringWidth(input.charAt(caretPos - 1))/2)) {
                break;
            }
            x -= bbStringWidth(input.charAt(caretPos-1));
        }
        bbFlushKeys();
        caretTimer = TimeInPosMilliSecs();
    } else if (MouseHit1) {
        selected = false;
    }

    if (selected) {
        int value = bbGetKey();
        int length = input.size();

        // backsapce
        if (value == 8) {
            value = 0;
            if (length > 0 && caretPos > 0) {
                if (caretPos == input.size()) {
                    input = input.substr(0, input.size()-1);
                } else {
                    input = input.substr(0, caretPos-1) + input.substr(caretPos, input.size() - caretPos);
                }
                --caretPos;
            }
            caretTimer = TimeInPosMilliSecs();
        }

        // left and right arrow
        if (bbKeyHit(203)) {
            --caretPos;
            if (caretPos < 0) { caretPos = 0; }
            caretTimer = TimeInPosMilliSecs();
        } else if (bbKeyHit(205)) {
             ++caretPos;
             if (caretPos > input.size()) { caretPos = input.size(); }
             caretTimer = TimeInPosMilliSecs();
        }

        if (!((value > 0 && value < 7) || (value > 26 && value < 32) || value == 9 || value == 13 || value == 0)) {
            if (caretPos == input.size()) {
                input = input + (char)value;
            } else {
                input = input.substr(0, caretPos) + (char)value + input.substr(caretPos, input.size() - caretPos);
            }
            ++caretPos;
            caretTimer = TimeInPosMilliSecs();
        }
    }
}

void GUIInput::draw() {
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);

    GUIButtonBase::draw();

    // Caret.
     if (selected) {
         if (caretTimer + 500 > TimeInPosMilliSecs()) {
             int pos = (x + width / 2 + bbStringWidth(input) / 2);
             if (caretPos == 0) {
                 pos -= bbStringWidth(input);
             } else {
                 pos -= bbStringWidth(input.substr(caretPos, input.size() - caretPos));
             }
             bbRect(pos, y + height / 2 - 5, 2, 12);
         } else if (caretTimer + 1000 < TimeInPosMilliSecs()) {
             caretTimer = TimeInPosMilliSecs();
         }
     }

    bbText(x + width / 2, y + height / 2, input, true, true);
}

}
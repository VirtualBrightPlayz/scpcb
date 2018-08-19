#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIInput.h"
#include "../Menu.h"
#include "../../Assetmgmt/Assets.h"
#include "../../MathUtils/MathUtils.h"
#include "../../GameMain.h"

namespace CBN {

GUIInput::GUIInput(int x, int y, int width, int height, const String& displayTxt, int txtOffset, const String& defaultTxt, bool relative)
    : GUIButtonBase(x, y, width, height, relative) {
    displayText = displayTxt;
    textOffset = txtOffset;
    input = defaultTxt;
    caretPos = 0;
    selected = false;
}

void GUIInput::update() {
    if (isMouseHit()) {
        selected = true;

        int x = this->x + width / 2;// + bbStringWidth(input) / 2;
        for (caretPos = 0; caretPos < input.size(); caretPos++) {
            if (bbMouseX() < x) {
                break;
            }
            x += bbStringWidth(input.charAt(caretPos)) / 2;
        }
        bbFlushKeys();
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
                    input = input.substr(0, caretPos-1) + input.substr(caretPos, input.size() - 1 - caretPos);
                }
            }
        }

        if (!((value > 0 && value < 7) || (value > 26 && value < 32) || value == 9 || value == 13 || value == 0)) {
            if (caretPos == input.size()) {
                input = input + (char)value;
            } else {
                input = input = input.substr(0, caretPos) + (char)value + input.substr(caretPos, input.size() - 1 - caretPos);
            }
        }
    }
}

void GUIInput::draw() {
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);

    GUIButtonBase::draw();

    // Caret.
    // if (selected) {
    //     if ((TimeInPosMilliSecs() % 800) < 400) {
    //         bbRect(x + width / 2 + bbStringWidth(input) / 2 + 2, y + height / 2 - 5, 2, 12);
    //     }
    // }

    bbText(x + width / 2, y + height / 2, input, true, true);
}

}
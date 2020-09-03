#include "MementoManager.h"

Memento::Memento(int startPos, PGE::String content, bool write, bool linked) {
    this->startPos = startPos;
    this->content = content;
    this->write = write;
    this->linked = linked;
}

PGE::String Memento::execute(const PGE::String& txt, bool flip) const {
    if (write ^ flip) {
        return txt.substr(0, startPos) + txt.substr(startPos + content.length());
    } else {
        return txt.substr(0, startPos) + content + txt.substr(startPos);
    }
}

int Memento::getSize() const {
    return content.byteLength() + content.byteLength() * 2 + sizeof(Memento) + sizeof(PGE::String);
}

MementoManager::MementoManager(int mementoMaxMemSize) {
    maxMemSize = mementoMaxMemSize;
    position = -1;
    memSize = 0;
}

PGE::String MementoManager::execute(const PGE::String& txt, int& pos, bool undo) {
    if (undo) {
        if (position >= 0) {
            PGE::String temp = history[position].execute(txt, false);
            position--;
            if (history[(long long)position + 1].linked) {
                temp = history[position].execute(temp, false);
                position--;
            }
            pos = 0; // TODO: Caret position setting.
            return temp;
        } else {
            return txt;
        }
    } else {
        if (position < (int)history.size() - 1) {
            position++;
            PGE::String temp = history[position].execute(txt, true);
            if (history[position].linked) {
                position++;
                temp = history[position].execute(temp, true);
            }
            pos = 0;
            return temp;
        } else {
            return txt;
        }
    }
}

void MementoManager::clear() {
    history.clear();
    memSize = 0;
    position = -1;
}

void MementoManager::push(int startPos, const PGE::String& content, bool write, bool linked) {
    Memento m = Memento(startPos, content, write, linked);
    if (position < history.size() - 1) {
        while (position < history.size() - 1) { // Truncating to the current position.
            removeBack();
        }
    }
    if (m.getSize() > maxMemSize) {
        clear();
    } else {
        memSize += m.getSize();
        while (memSize > maxMemSize) {
            if (history.front().linked) {
                removeFront();
            }
            removeFront();
        }
        history.push_back(m);
        position++;
    }
}

void MementoManager::removeBack() {
    memSize -= history.back().getSize();
    history.pop_back();
    if (position >= history.size()) {
        position = (int)history.size() - 1;
    }
}

void MementoManager::removeFront() {
    memSize -= history.front().getSize();
    history.pop_front();
    position--;
}

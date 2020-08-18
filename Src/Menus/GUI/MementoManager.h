#ifndef MEMENTOMANAGER_H_INCLUDED
#define MEMENTOMANAGER_H_INCLUDED

#include <deque>

#include <Misc\String.h>

class Memento {
    public:
        Memento(int startPos, PGE::String content, bool write, bool linked = false);

        int startPos;
        // Whether the action was a deletion or a write.
        bool write;
        PGE::String content;
        // Whether the action was a deletion in addtion to a write, which need to be un-/redone together.
        bool linked;

        PGE::String execute(const PGE::String& txt, bool flip) const;
        int getSize() const;
};

class MementoManager {
    private:
        std::deque<Memento> history;
        int position;
        int memSize;
        int maxMemSize;

        void removeBack();
        void removeFront();

    public:
        MementoManager(int mementoMaxMemSize);

        PGE::String execute(const PGE::String& txt, int& pos, bool undo);
        void clear();
        void push(const Memento m);
};

#endif // MEMENTOMANAGER_H_INCLUDED

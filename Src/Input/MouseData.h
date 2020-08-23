#ifndef MOUSEDATA_H_INCLUDED
#define MOUSEDATA_H_INCLUDED

#include <IO/IO.h>

class Config;

class MouseData {
    private:
        PGE::IO* io;
        Config* config;

        PGE::Vector2f mousePosition;
        PGE::Vector2i mouseWheelDelta;

    public:
        MouseData(PGE::IO* io, Config* config);

        const PGE::Vector2f& getPosition() const;
        const PGE::Vector2f& getWheelDelta() const;

        void update();
};

#endif // MOUSEDATA_H_INCLUDED

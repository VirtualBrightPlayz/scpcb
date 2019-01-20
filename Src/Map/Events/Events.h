#ifndef EVENTS_H_INCLUDED
#define EVENTS_H_INCLUDED
#include <vector>
#include <StringType.h>

class gxSound;
class gxChannel;

namespace CBN {

struct Room;

// Constants.
class Event {
protected:
    struct Room* room;
public:
    virtual void update() =0;

    static void AssignEvent(struct Room* room);
};

}
#endif // EVENTS_H_INCLUDED

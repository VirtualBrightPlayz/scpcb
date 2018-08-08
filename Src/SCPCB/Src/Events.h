#ifndef EVENTS_H_INCLUDED
#define EVENTS_H_INCLUDED
#include <vector>
#include <StringType.h>

namespace CBN {

// Constants.
const int EVENT_MAX_STATE_COUNT = 5;
const int EVENT_SOUND_COUNT = 5;
const int EVENT_CHANNEL_COUNT = 2;

// Structs.
struct Event {
private:
    static std::vector<Event*> list;

public:
    Event();
    ~Event();
    static int getListSize();
    static Event* getObject(int index);

    String name;
    struct Room* room;

    //TODO: deprecated
    float eventState;
    float eventState2;
    float eventState3;

    int intState[EVENT_MAX_STATE_COUNT];
    float floatState[EVENT_MAX_STATE_COUNT];

    bool loaded;

    //Field img% ;???

    class gxChannel* soundChannels[EVENT_CHANNEL_COUNT];
    class gxSound* sounds[EVENT_SOUND_COUNT];

    String musicTrack;
    bool overwriteMusic;
};

// Functions.
Event* CreateEvent(String eventname, String roomname, int id, float prob = 0.0);

void RemoveEvent(Event* e);

void InitEvents();

void UpdateEvents();

}
#endif // EVENTS_H_INCLUDED

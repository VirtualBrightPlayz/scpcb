#ifndef EVENTS_H_INCLUDED
#define EVENTS_H_INCLUDED
#include <vector>

namespace CBN {

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
    Room* room;

    //TODO: deprecated
    float eventState;
    float eventState2;
    float eventState3;

    int intState[EVENT_MAX_STATE_COUNT];
    float floatState[EVENT_MAX_STATE_COUNT];

    int loaded;

    //Field img% ;???

    int soundChannels[EVENT_CHANNEL_COUNT];
    int sounds[EVENT_SOUND_COUNT];

    String musicTrack;
    int overwriteMusic;
};

// Constants.
extern const int EVENT_MAX_STATE_COUNT;
extern const int EVENT_SOUND_COUNT;
extern const int EVENT_CHANNEL_COUNT;

// Functions.
Event* CreateEvent(String eventname, String roomname, int id, float prob = 0.0);

void RemoveEvent(Event* e);

void InitEvents();

void UpdateEvents();

}
#endif // EVENTS_H_INCLUDED

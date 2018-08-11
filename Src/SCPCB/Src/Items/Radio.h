#ifndef RADIO_H_INCLUDED
#define RADIO_H_INCLUDED
#include <vector>

class gxSound;
class gxChannel;

namespace CBN {

struct Item;

const int RADIO_CHANNEL_COUNT = 5;

// Structs.
struct Radio {
private:
    static std::vector<Radio*> list;

public:
    Radio();
    ~Radio();
    static int getListSize();
    static Radio* getObject(int index);

    int currChn;

    //How long each channel has been played for.
    float airTime[RADIO_CHANNEL_COUNT];

    gxChannel* channels[RADIO_CHANNEL_COUNT];

    //Sounds
    gxSound* sndStatic;
};

// Globals.
extern Radio* radio;

// Functions.
Radio* CreateRadio();

void ChangeRadioChannel(int newChn);

void UpdateRadio(Item* i);

}
#endif // RADIO_H_INCLUDED

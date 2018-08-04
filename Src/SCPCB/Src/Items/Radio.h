#ifndef RADIO_H_INCLUDED
#define RADIO_H_INCLUDED
#include <vector>

namespace CBN {

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

    int channels[RADIO_CHANNEL_COUNT];

    //Sounds
    int sndStatic;
};

// Constants.
extern const int RADIO_CHANNEL_COUNT;

// Globals.
extern Radio* radio;

// Functions.
Radio* CreateRadio();

void ChangeRadioChannel(int newChn);

void UpdateRadio(Item* i);

}
#endif // RADIO_H_INCLUDED

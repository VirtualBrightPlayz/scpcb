#ifndef LOADINGSCREEN_H_INCLUDED
#define LOADINGSCREEN_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct LoadingScreen {
private:
    static std::vector<LoadingScreen*> list;

public:
    LoadingScreen();
    ~LoadingScreen();
    static int getListSize();
    static LoadingScreen* getObject(int index);

    String imgpath;
    int img;
    int id;
    String title;
    int alignx;
    int aligny;
    int disablebackground;
    String txt[5];
    int txtamount;
};

// Globals.
extern LoadingScreen* SelectedLoadingScreen;
extern int LoadingScreenAmount;
extern int LoadingScreenText;
extern int LoadingBack;

// Functions.
void InitLoadingScreens(String file);

void DrawLoading(int percent, int shortloading = false);

}
#endif // LOADINGSCREEN_H_INCLUDED

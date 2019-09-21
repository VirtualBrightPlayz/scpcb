#ifndef MENU_H_INCLUDED
#define MENU_H_INCLUDED

#include <Misc/String.h>
#include <Math/Vector.h>

class World;

class Menu {
private:
    PGE::String type;
    
protected:
    World* world;
    bool markedForRemoval;
    
public:
    Menu(World* wrld, const PGE::String& type);
    virtual ~Menu()=default;
    
    void detach();
    bool isMarkedForDeath() const;
    const PGE::String& getType() const;
    
    // What should the menu do when escape is pressed.
    virtual void onEscapeHit()=0;
    
    virtual void update(const PGE::Vector2f& mousePosition)=0;
    virtual void render() const=0;
};

#endif // MENU_H_INCLUDED

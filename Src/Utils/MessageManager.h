#ifndef MESSAGEMANAGER_H_INCLUDED
#define MESSAGEMANAGER_H_INCLUDED

#include <Misc/String.h>
#include <Color/Color.h>

class LocalizationManager;
class GUIText;
class UIMesh;
class KeyBinds;
class Config;
class Font;

class MessageManager {
	private:
		UIMesh* const um;
		Font* const font;

		GUIText* const text;
		float displayTimer;

	public:
		MessageManager(LocalizationManager* lm, UIMesh* um, KeyBinds* kb, Config* con, Font* font);
		
		void setMsg(const PGE::String& local, float seconds, const PGE::Color& color);
		void update(float timeStep);
		void draw();
};

#endif // MESSAGEMANAGER_H_INCLUDED

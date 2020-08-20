#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED

#include <deque>

#include <IO/IO.h>
#include <Color/Color.h>

#include "Menu.h"

class asIScriptFunction;
class asIScriptContext;

class UIMesh;
class Font;
class KeyBinds;
class Config;
class LocalizationManager;
class GUIFrame;
class GUITextInput;

class Command;
class Message;

class Console : public Menu {
    private:
        GUIFrame* frame;
        GUITextInput* input;

        std::vector<Message*> messageHistory;
        void updateMessageWindow();

        int commandHistoryIndex;
        std::deque<PGE::String> commandHistory;

        int windowMaxLineCount;
        float windowScrollOffset;
    
        UIMesh* uiMesh;
        Font* font;
        KeyBinds* keyBinds;
        Config* config;

        std::vector<Command*> commands;
        void registerInternalCommands();

        void executeCommand(const PGE::String& in);

    public:
        Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, LocalizationManager* tm, PGE::IO* io);
        ~Console();

        void onActivate() override;
        void onDeactivate() override;

        void update(const PGE::Vector2f& mousePosition, const PGE::Vector2i& mouseWheelDelta) override;
        void render() const override;

        void addConsoleMessage(const PGE::String& resp, const PGE::Color& color = PGE::Color::White);
        void logWarning(const PGE::String& resp);
        void logError(const PGE::String& resp);
    
        void showHelp(const PGE::String& com);
        void listCommands();
        void clear();

        void registerExternalCommand(const PGE::String& name, const PGE::String& helpText, asIScriptFunction* f, asIScriptContext* context);
};

#endif // CONSOLE_H_INCLUDED

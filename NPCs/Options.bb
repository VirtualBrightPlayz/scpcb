Const OptionFile$ = "options.ini"

Type Options
    ;General
    Field introEnabled%
    Field allowConsole%
    Field consoleOpenOnError%
    Field achvPopup%
    Field mapWidth%
    Field roomLights

    ;Graphics
    Field screenWidth%
    Field screenHeight%
    Field fullscreen%
    Field borderlessWindowed%
    Field gfxDriver%
    Field screenGamma#
    Field framelimit%
    Field vsync%

    ;audio
    Field musicVolume#
    Field soundVolume#

    ;HUD
    Field hudEnabled%
    Field showFPS%

    Field launcher%

End Type

Global userOptions.Options = Null

Global KEY_RIGHT = GetINIInt(OptionFile, "binds", "Right key")
Global KEY_LEFT = GetINIInt(OptionFile, "binds", "Left key")
Global KEY_UP = GetINIInt(OptionFile, "binds", "Up key")
Global KEY_DOWN = GetINIInt(OptionFile, "binds", "Down key")

Global KEY_BLINK = GetINIInt(OptionFile, "binds", "Blink key")
Global KEY_SPRINT = GetINIInt(OptionFile, "binds", "Sprint key")
Global KEY_INV = GetINIInt(OptionFile, "binds", "Inventory key")
Global KEY_CROUCH = GetINIInt(OptionFile, "binds", "Crouch key")
Global KEY_SAVE = GetINIInt(OptionFile, "binds", "Save key")
Global KEY_CONSOLE = GetINIInt(OptionFile, "binds", "Console key")

Function LoadOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optHud$ = "hud"

    userOptions\introEnabled = GetINIInt(OptionFile, optGen, "intro enabled")
    MouseSens = GetINIFloat(OptionFile, optGen, "mouse sensitivity")
    InvertMouse = GetINIInt(OptionFile, optGen, "invert mouse y")
    userOptions\allowConsole = GetINIInt(OptionFile, optGen, "enable console")
    userOptions\consoleOpenOnError = GetINIInt(OptionFile, optGen, "open console on error")
    userOptions\achvPopup = GetINIInt(OptionFile, optGen, "achievement popup")
    userOptions\mapWidth = GetINIInt(OptionFile, optGen, "map size")
    userOptions\roomLights = GetINIInt(OptionFile, optGen, "room lights")

    userOptions\screenWidth = GetINIInt(OptionFile, optGra, "screen width")
    userOptions\screenHeight = GetINIInt(OptionFile, optGra, "screen height")
    userOptions\fullscreen = GetINIInt(OptionFile, optGra, "fullscreen")
    userOptions\borderlessWindowed = GetINIInt(OptionFile, optGra, "borderless windowed")
    userOptions\gfxDriver = Max(GetINIInt(OptionFile, optGra, "gfx driver"), 1)
    userOptions\screenGamma = GetINIFloat(OptionFile, optGra, "screengamma")
    userOptions\framelimit = GetINIInt(OptionFile, optGra, "framelimit")
    userOptions\vsync = GetINIInt(OptionFile, optGra, "vsync")

    userOptions\musicVolume = GetINIFloat(OptionFile, optAud, "music volume")
    userOptions\soundVolume = GetINIFloat(OptionFile, optAud, "sound volume")

    userOptions\hudEnabled = GetINIInt(OptionFile, optHud, "HUD enabled")
    userOptions\showFPS = GetINIInt(OptionFile, optHud, "show FPS")

    userOptions\launcher = GetINIInt(OptionFile, "launcher", "launcher enabled")
End Function

Function SaveOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optHud$ = "hud"

    PutINIValue(OptionFile, optGen, "intro enabled", userOptions\introEnabled)
    PutINIValue(OptionFile, optGen, "mouse sensitivity", userOptions\mouseSensitivity)
    PutINIValue(OptionFile, optGen, "invert mouse y", InvertMouse)
    PutINIValue(OptionFile, optGen, "enable console", userOptions\allowConsole)
    PutINIValue(OptionFile, optGen, "open console on error", userOptions\consoleOpenOnError)
    PutINIValue(OptionFile, optGen, "achievement popup", userOptions\achvPopup)
    PutINIValue(OptionFile, optGen, "map size", userOptions\mapWidth)
    PutINIValue(OptionFile, optGen, "room lights", userOptions\roomLights)

    PutINIValue(OptionFile, optGra, "screen width", userOptions\screenWidth)
    PutINIValue(OptionFile, optGra, "screen height", userOptions\screenHeight)
    PutINIValue(OptionFile, optGra, "fullscreen", userOptions\fullscreen)
    PutINIValue(OptionFile, optGra, "borderless windowed", userOptions\borderlessWindowed)
    PutINIValue(OptionFile, optGra, "gfx driver", userOptions\gfxDriver)
    PutINIValue(OptionFile, optGra, "screengamma", userOptions\screenGamma)
    PutINIValue(OptionFile, optGra, "framelimit", userOptions\framelimit)
    PutINIValue(OptionFile, optGra, "vsync", userOptions\vsync)

    PutINIValue(OptionFile, optAud, "music volume", userOptions\musicVolume)
    PutINIValue(OptionFile, optAud, "sound volume", userOptions\soundVolume)
    	
    PutINIValue(OptionFile, optHud, "HUD enabled", userOptions\hudEnabled)
    PutINIValue(OptionFile, optHud, "show FPS", userOptions\showFPS)

    PutINIValue(OptionFile, "launcher", "launcher enabled", userOptions\launcher)
    
    PutINIValue(OptionFile, "binds", "Right key", KEY_RIGHT)
    PutINIValue(OptionFile, "binds", "Left key", KEY_LEFT)
    PutINIValue(OptionFile, "binds", "Up key", KEY_UP)
    PutINIValue(OptionFile, "binds", "Down key", KEY_DOWN)
    PutINIValue(OptionFile, "binds", "Blink key", KEY_BLINK)
    PutINIValue(OptionFile, "binds", "Sprint key", KEY_SPRINT)
    PutINIValue(OptionFile, "binds", "Inventory key", KEY_INV)
    PutINIValue(OptionFile, "binds", "Crouch key", KEY_CROUCH)
    PutINIValue(OptionFile, "binds", "Save key", KEY_SAVE)
    PutINIValue(OptionFile, "binds", "Console key", KEY_CONSOLE)
End Function
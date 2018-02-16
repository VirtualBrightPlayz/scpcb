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

Global KEY_RIGHT%
Global KEY_LEFT%
Global KEY_UP%
Global KEY_DOWN%

Global KEY_BLINK%
Global KEY_SPRINT%
Global KEY_INV%
Global KEY_CROUCH%
Global KEY_SAVE%
Global KEY_CONSOLE%

Function LoadOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optBin$ = "binds"
    Local optHud$ = "hud"

    userOptions\introEnabled        = GetINIInt(OptionFile, optGen, "intro enabled")
    MouseSens                       = GetINIFloat(OptionFile, optGen, "mouse sensitivity")
    InvertMouse                     = GetINIInt(OptionFile, optGen, "invert mouse y")
    userOptions\allowConsole        = GetINIInt(OptionFile, optGen, "enable console")
    userOptions\consoleOpenOnError  = GetINIInt(OptionFile, optGen, "open console on error")
    userOptions\achvPopup           = GetINIInt(OptionFile, optGen, "achievement popup")
    userOptions\mapWidth            = GetINIInt(OptionFile, optGen, "map size")
    userOptions\roomLights          = GetINIInt(OptionFile, optGen, "room lights")

    userOptions\screenWidth         = Max(GetINIInt(OptionFile, optGra, "screen width"), 640)
    userOptions\screenHeight        = Max(GetINIInt(OptionFile, optGra, "screen height"), 480)
    userOptions\fullscreen          = GetINIInt(OptionFile, optGra, "fullscreen")
    userOptions\borderlessWindowed  = GetINIInt(OptionFile, optGra, "borderless windowed")
    userOptions\gfxDriver           = Max(GetINIInt(OptionFile, optGra, "gfx driver"), 1)
    userOptions\screenGamma         = GetINIFloat(OptionFile, optGra, "screengamma")
    userOptions\framelimit          = GetINIInt(OptionFile, optGra, "framelimit")
    userOptions\vsync               = GetINIInt(OptionFile, optGra, "vsync")

    userOptions\musicVolume         = GetINIFloat(OptionFile, optAud, "music volume")
    userOptions\soundVolume         = GetINIFloat(OptionFile, optAud, "sound volume")

    userOptions\hudEnabled          = GetINIInt(OptionFile, optHud, "HUD enabled")
    userOptions\showFPS             = GetINIInt(OptionFile, optHud, "show FPS")

    KEY_RIGHT                       = GetINIInt(OptionFile, optBin, "Right key")
    KEY_LEFT                        = GetINIInt(OptionFile, optBin, "Left key")
    KEY_UP                          = GetINIInt(OptionFile, optBin, "Up key")
    KEY_DOWN                        = GetINIInt(OptionFile, optBin, "Down key")

    KEY_BLINK                       = GetINIInt(OptionFile, optBin, "Blink key")
    KEY_SPRINT                      = GetINIInt(OptionFile, optBin, "Sprint key")
    KEY_INV                         = GetINIInt(OptionFile, optBin, "Inventory key")
    KEY_CROUCH                      = GetINIInt(OptionFile, optBin, "Crouch key")
    KEY_SAVE                        = GetINIInt(OptionFile, optBin, "Save key")
    KEY_CONSOLE                     = GetINIInt(OptionFile, optBin, "Console key")

    userOptions\launcher            = GetINIInt(OptionFile, "launcher", "launcher enabled")
End Function

Function SaveOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optBin$ = "binds"
    Local optHud$ = "hud"

    PutINIValue(OptionFile, optGen, "intro enabled", userOptions\introEnabled)
    PutINIValue(OptionFile, optGen, "mouse sensitivity", MouseSens)
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
    
    PutINIValue(OptionFile, optBin, "Right key", KEY_RIGHT)
    PutINIValue(OptionFile, optBin, "Left key", KEY_LEFT)
    PutINIValue(OptionFile, optBin, "Up key", KEY_UP)
    PutINIValue(OptionFile, optBin, "Down key", KEY_DOWN)
    PutINIValue(OptionFile, optBin, "Blink key", KEY_BLINK)
    PutINIValue(OptionFile, optBin, "Sprint key", KEY_SPRINT)
    PutINIValue(OptionFile, optBin, "Inventory key", KEY_INV)
    PutINIValue(OptionFile, optBin, "Crouch key", KEY_CROUCH)
    PutINIValue(OptionFile, optBin, "Save key", KEY_SAVE)
    PutINIValue(OptionFile, optBin, "Console key", KEY_CONSOLE)

    PutINIValue(OptionFile, "launcher", "launcher enabled", userOptions\launcher)
End Function
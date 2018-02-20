Const OptionFile$ = "options.ini"

Type KeyBinds
    Field right%
    Field left%
    Field up%
    Field down%

    Field blink%
    Field sprint%
    Field inv%
    Field crouch%

    Field save%
    Field console% 
End Type

Global MouseSens#
Global InvertMouse%

Type Options
    ;General
    Field introEnabled%
    Field allowConsole%
    Field consoleOpenOnError%
    Field achvPopup%
    Field mapWidth%

    ;Graphics
    Field screenWidth%
    Field screenHeight%
    Field fullscreen%
    Field borderlessWindowed%
    Field gfxDriver%
    Field screenGamma#
    Field framelimit%
    Field vsync%

    ;Audio
    Field musicVolume#
    Field soundVolume#

    ;HUD
    Field hudEnabled%
    Field showFPS%

    Field launcher%

End Type

Global keyBinds.KeyBinds = new KeyBinds
Global userOptions.Options = New Options
LoadOptionsINI()

Function LoadOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optBin$ = "binds"
    Local optHud$ = "hud"

    userOptions\introEnabled        = GetINIInt(OptionFile, optGen, "intro enabled")
    MouseSens                       = GetINIFloat(OptionFile, optGen, "mouse sensitivity")
    InvertMouse                     = GetINIInt(OptionFile, optGen, "invert mouse y")
    userOptions\consoleOpenOnError  = GetINIInt(OptionFile, optGen, "open console on error")
    userOptions\achvPopup           = GetINIInt(OptionFile, optGen, "achievement popup")
    userOptions\mapWidth            = GetINIInt(OptionFile, optGen, "map size")

    ;Min is 640x480.
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

    keyBinds\right                  = GetINIInt(OptionFile, optBin, "Right key")
    keyBinds\left                   = GetINIInt(OptionFile, optBin, "Left key")
    keyBinds\up                     = GetINIInt(OptionFile, optBin, "Up key")
    keyBinds\down                   = GetINIInt(OptionFile, optBin, "Down key")

    keyBinds\blink                  = GetINIInt(OptionFile, optBin, "Blink key")
    keyBinds\sprint                 = GetINIInt(OptionFile, optBin, "Sprint key")
    keyBinds\inv                    = GetINIInt(OptionFile, optBin, "Inventory key")
    keyBinds\crouch                 = GetINIInt(OptionFile, optBin, "Crouch key")

    keyBinds\save                   = GetINIInt(OptionFile, optBin, "Save key")
    keyBinds\console                = GetINIInt(OptionFile, optBin, "Console key")

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
    
    PutINIValue(OptionFile, optBin, "Right key", keyBinds\right)
    PutINIValue(OptionFile, optBin, "Left key", keyBinds\left)
    PutINIValue(OptionFile, optBin, "Up key", keyBinds\up)
    PutINIValue(OptionFile, optBin, "Down key", keyBinds\down)

    PutINIValue(OptionFile, optBin, "Blink key", keyBinds\blink)
    PutINIValue(OptionFile, optBin, "Sprint key", keyBinds\sprint)
    PutINIValue(OptionFile, optBin, "Inventory key", keyBinds\inv)
    PutINIValue(OptionFile, optBin, "Crouch key", keyBinds\crouch)

    PutINIValue(OptionFile, optBin, "Save key", keyBinds\save)
    PutINIValue(OptionFile, optBin, "Console key", keyBinds\console)

    PutINIValue(OptionFile, "launcher", "launcher enabled", userOptions\launcher)
End Function
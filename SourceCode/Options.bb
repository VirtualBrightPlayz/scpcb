Const OptionFile$ = "options.ini"

Type KeyBinds
    Field rght% ;can't call it right because blitz
    Field lft% ;can't call it left because blitz
    Field up%
    Field down%

    Field blink%
    Field sprint%
    Field inv%
    Field crouch%

    Field save%
    Field console%
End Type

Type Options
    ;General
    Field introEnabled%
    Field mouseSensitivity#
    Field invertMouseY%
    Field consoleOpenOnError%

    ;Graphics
    Field screenWidth%
    Field screenHeight%
    Field fullscreen%
    Field gfxDriver%
    Field screenGamma#
    Field framelimit%
    Field vsync%

    ;Audio
    Field musicVolume#
    Field sndVolume#

    ;HUD
    Field hudEnabled%
    Field showFPS%

    Field launcher%

End Type

Global keyBinds.KeyBinds
Global userOptions.Options

Function LoadOptionsINI()
    Local optGen$ = "general"
    Local optGra$ = "graphics"
    Local optAud$ = "audio"
    Local optBin$ = "binds"
    Local optHud$ = "hud"

    userOptions\introEnabled        = GetINIInt(OptionFile, optGen, "intro enabled")
    userOptions\mouseSensitivity    = GetINIFloat(OptionFile, optGen, "mouse sensitivity")
    userOptions\invertMouseY        = GetINIInt(OptionFile, optGen, "invert mouse y")
    userOptions\consoleOpenOnError  = GetINIInt(OptionFile, optGen, "open console on error")

    ;Min is 640x480.
    userOptions\screenWidth         = Int(Max(GetINIInt(OptionFile, optGra, "screen width"), 640))
    userOptions\screenHeight        = Int(Max(GetINIInt(OptionFile, optGra, "screen height"), 480))
    userOptions\fullscreen          = GetINIInt(OptionFile, optGra, "fullscreen")
    userOptions\gfxDriver           = Int(Max(GetINIInt(OptionFile, optGra, "gfx driver"), 1))
    userOptions\screenGamma         = GetINIFloat(OptionFile, optGra, "screengamma")
    userOptions\framelimit          = GetINIInt(OptionFile, optGra, "framelimit")
    userOptions\vsync               = GetINIInt(OptionFile, optGra, "vsync")

    userOptions\musicVolume         = GetINIFloat(OptionFile, optAud, "music volume")
    userOptions\sndVolume           = GetINIFloat(OptionFile, optAud, "sound volume")

    userOptions\hudEnabled          = GetINIInt(OptionFile, optHud, "HUD enabled")
    userOptions\showFPS             = GetINIInt(OptionFile, optHud, "show FPS")

    keyBinds\rght                   = GetINIInt(OptionFile, optBin, "Right key")
    keyBinds\lft                    = GetINIInt(OptionFile, optBin, "Left key")
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

    PutINIValue(OptionFile, optGen, "intro enabled", Str(userOptions\introEnabled))
    PutINIValue(OptionFile, optGen, "mouse sensitivity", Str(userOptions\mouseSensitivity))
    PutINIValue(OptionFile, optGen, "invert mouse y", Str(userOptions\invertMouseY))
    PutINIValue(OptionFile, optGen, "open console on error", Str(userOptions\consoleOpenOnError))

    PutINIValue(OptionFile, optGra, "screen width", Str(userOptions\screenWidth))
    PutINIValue(OptionFile, optGra, "screen height", Str(userOptions\screenHeight))
    PutINIValue(OptionFile, optGra, "fullscreen", Str(userOptions\fullscreen))
    PutINIValue(OptionFile, optGra, "gfx driver", Str(userOptions\gfxDriver))
    PutINIValue(OptionFile, optGra, "screengamma", Str(userOptions\screenGamma))
    PutINIValue(OptionFile, optGra, "framelimit", Str(userOptions\framelimit))
    PutINIValue(OptionFile, optGra, "vsync", Str(userOptions\vsync))

    PutINIValue(OptionFile, optAud, "music volume", Str(userOptions\musicVolume))
    PutINIValue(OptionFile, optAud, "sound volume", Str(userOptions\sndVolume))
    PutINIValue(OptionFile, optHud, "HUD enabled", Str(userOptions\hudEnabled))
    PutINIValue(OptionFile, optHud, "show FPS", Str(userOptions\showFPS))

    PutINIValue(OptionFile, optBin, "Right key", Str(keyBinds\rght))
    PutINIValue(OptionFile, optBin, "Left key", Str(keyBinds\lft))
    PutINIValue(OptionFile, optBin, "Up key", Str(keyBinds\up))
    PutINIValue(OptionFile, optBin, "Down key", Str(keyBinds\down))

    PutINIValue(OptionFile, optBin, "Blink key", Str(keyBinds\blink))
    PutINIValue(OptionFile, optBin, "Sprint key", Str(keyBinds\sprint))
    PutINIValue(OptionFile, optBin, "Inventory key", Str(keyBinds\inv))
    PutINIValue(OptionFile, optBin, "Crouch key", Str(keyBinds\crouch))

    PutINIValue(OptionFile, optBin, "Save key", Str(keyBinds\save))
    PutINIValue(OptionFile, optBin, "Console key", Str(keyBinds\console))

    PutINIValue(OptionFile, "launcher", "launcher enabled", Str(userOptions\launcher))
End Function
;~IDEal Editor Parameters:
;~F#2
;~C#Blitz3D
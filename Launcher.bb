Type Launcher
    Field width%
    Field height%

    Field background%

    Field tileWhite%
    Field tileBlack%

    Field font%
    
	Field resWidths.IntArray
	Field resHeights.IntArray

    Field selectedGFXMode%
End Type
Global launcher.Launcher

Function CreateLauncher.Launcher()
    Local launch.Launcher = New Launcher

    launch\width = Min(GetINIInt(OptionFile, "launcher", "launcher width"), 1024)
    launch\height = Min(GetINIInt(OptionFile, "launcher", "launcher height"), 768)
	
	launch\resWidths = CreateIntArray(CountGfxModes3D())
	launch\resHeights = CreateIntArray(CountGfxModes3D())
	
    Local i%
	For i = 1 To CountGfxModes3D()
		If (GfxModeDepth(i) <> 16) Then
			SetIntArrayElem(launch\resWidths, GfxModeWidth(i), i - 1) 
            SetIntArrayElem(launch\resHeights, GfxModeHeight(i), i - 1) 
		EndIf
	Next
	
	Graphics3DExt(launcher\width, launcher\height, 0, 2)
	
	MenuScale = 1
	
	launcher\background = LoadImage("GFX\menu\launcher.jpg")
	
    launcher\tileWhite = LoadImage("GFX\menu\menuwhite.jpg")
    launcher\tileBlack = LoadImage("GFX\menu\menublack.jpg")
	
    launcher\font = LoadFont("GFX\font\cour\Courier New.ttf", 18, 0,0,0)
	
	SetBuffer BackBuffer()
	
	SetFont(launcher\font)
	MaskImage(launcher\tileBlack, 255,255,0)
	
    Return launch
End Function

Function DestroyLauncher(launch.Launcher)
    FreeImage(launch\background)

    FreeImage(launch\tileWhite)
    FreeImage(launch\tileBlack)

    FreeFont(launch\font)

    Delete launch
End Function

Function UpdateLauncher()
    ;Cls
    Color(0,0,0)
    
    MouseHit1 = MouseHit(1)
    
    Color(255, 255, 255)
    
    Local x% = 40
    Local y% = 280 - 65

    Local i%
    For i = 1 To CountGfxModes3D()
        If (GfxModeDepth(i) <> 16) Then
            Color(0, 0, 0)

            If MouseOn(x - 1, y - 1, 100, 20) Then
                If MouseHit1 Then launcher\selectedGFXMode = i
            EndIf
            
            y=y+20
            If (y >= 240 - 65 + (launcher\height - 80 - 260)) Then
                y = 280 - 65
                x = x + 100
            EndIf
        EndIf
    Next
    
    ;-----------------------------------------------------------------
    Color(255, 255, 255)
    x = 30
    y = 369
    
    y = y + 10
    For i = 1 To CountGfxDrivers()
        Color(0, 0, 0)
        If userOptions\gfxDriver = i Then Rect(x - 1, y - 1, 290, 20, False)
        
        LimitText(GfxDriverName(i), x, y, 290, False)
        If MouseOn(x - 1, y - 1, 290, 20) Then
            If MouseHit1 Then userOptions\gfxDriver = i
        EndIf
        
        y = y + 20
    Next
    
    ;TODO: Reimplement.
    userOptions\fullscreen = UpdateUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions\fullscreen, userOptions\borderlessWindowed)
    
    ;userOptions\borderlessWindowed = DrawTick(40 + 430 - 15, 260 - 55 + 35, userOptions\borderlessWindowed)

    userOptions\launcher = UpdateUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions\launcher)

    ;Don't allow selecting of fullscren when borderless windowed is enabled.
    ;If userOptions\borderlessWindowed
    ;   Color 255, 0, 0
    ;   userOptions\fullscreen = False
    ;Else
    ;  Color 255, 255, 255
    ;EndIf

    
    
    If UpdateUIButton(launcher\width - 30 - 90, launcher\height - 50 - 55, 100, 30, "LAUNCH", False) Then
        userOptions\screenWidth = GetIntArrayElem(launcher\resWidths, launcher\selectedGFXMode)
        userOptions\screenHeight = GetIntArrayElem(launcher\resHeights, launcher\selectedGFXMode)
        GraphicWidth = userOptions\screenWidth
        GraphicHeight = userOptions\screenHeight
		
        userOptions\gfxDriver = userOptions\gfxDriver

        SaveOptionsINI()
        DestroyLauncher(launcher)
        Return
    EndIf
    
    If UpdateUIButton(launcher\width - 30 - 90, launcher\height - 50, 100, 30, "EXIT", False) Then End
End Function

Function DrawLauncher()
    Rect(0, 0, launcher\width, launcher\height, True)

    Color(255, 255, 255)
    DrawImage(launcher\background, 0, 0)
    
    Text(20, 240 - 65, "Resolution: ")

    Local x% = 40
    Local y% = 280 - 65

    Local i%
    For i = 1 To CountGfxModes3D()
        If (GfxModeDepth(i) <> 16) Then
            Color(0, 0, 0)

            If (launcher\selectedGFXMode = i) Then
                Rect(x - 1, y - 1, 100, 20, False)
            EndIf
            
            Text(x, y, (GetIntArrayElem(launcher\resWidths, i - 1) + "x" + GetIntArrayElem(launcher\resHeights, i - 1)))
            If MouseOn(x - 1, y - 1, 100, 20) Then
                Color(100, 100, 100)
                Rect(x - 1, y - 1, 100, 20, False)
            EndIf
            
            y=y+20
            If (y >= 240 - 65 + (launcher\height - 80 - 260)) Then
                y = 280 - 65
                x = x + 100
            EndIf
        EndIf
    Next

    Color(255, 255, 255)
    x = 30
    y = 369
    Rect(x - 10, y, 340, 95)
    Text(x - 10, y - 25, "Graphics:")

    y = y + 10
    For i = 1 To CountGfxDrivers()
        Color(0, 0, 0)
        If userOptions\gfxDriver = i Then Rect(x - 1, y - 1, 290, 20, False)
        ;text(x, y, bbGfxDriverName(i))

        LimitText(GfxDriverName(i), x, y, 290, False)
        If MouseOn(x - 1, y - 1, 290, 20) Then
            Color(100, 100, 100)
            Rect(x - 1, y - 1, 290, 20, False)
        EndIf
        
        y = y + 20
    Next

    DrawUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions\fullscreen, userOptions\borderlessWindowed)
    DrawUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions\launcher)

    Text(40 + 430 + 15, 262 - 55 + 5 - 8, "Fullscreen")

    Color 255, 255, 255
    ;Text(40 + 430 + 15, 262 - 55 + 35 - 8, "Borderless",False,False)
    ;Text(40 + 430 + 15, 262 - 55 + 35 + 12, "windowed mode",False,False)

    Text(40 + 430 + 15, 262 - 55 + 95 + 8, "Use launcher")
    
    Text(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+GetIntArrayElem(launcher\resWidths, launcher\selectedGFXMode) + "x" + GetIntArrayElem(launcher\resHeights, launcher\selectedGFXMode))
    
    ;If GfxModeWidths(SelectedGFXMode)<G_viewport_width Then
    ;	Text(40+ 260 + 65, 262 - 55 + 160, "(upscaled to")
    ;ElseIf GfxModeWidths(SelectedGFXMode)>G_viewport_width Then
    ;	Text(40+ 260 + 65, 262 - 55 + 160, "(downscaled to")
    ;EndIf
    
    ;Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ")")

    DrawUIButton(LauncherWidth - 30 - 90, LauncherHeight - 50 - 55, 100, 30, "LAUNCH", False)

    DrawUIButton(LauncherWidth - 30 - 90, LauncherHeight - 50, 100, 30, "EXIT", False)
    Flip
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D